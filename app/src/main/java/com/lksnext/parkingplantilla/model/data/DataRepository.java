package com.lksnext.parkingplantilla.model.data;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingplantilla.model.domain.Plaza;
import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.model.domain.Vehiculo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import java.util.UUID;

public class DataRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public void generateInitialDataIfNeeded(Context context) {
        // SOLO USAR UNA VEZ, luego comentar esta línea
        generatePlazas();
        generateTestUsersWithReservas(context);
    }

    private void generatePlazas() {
        CollectionReference plazasRef = db.collection("plazas");

        String[] tipos = {"NORMAL", "NORMAL", "NORMAL", "MOTO", "MOTO", "ELECTRICO", "ELECTRICO", "MINUSVALIDO", "NORMAL", "MOTO", "ELECTRICO", "NORMAL"};

        for (int i = 0; i < tipos.length; i++) {
            String id = "P" + (i + 1);
            Plaza plaza = new Plaza(id, Plaza.Tipo.valueOf(tipos[i]), Plaza.Estado.LIBRE);
            plazasRef.document(id).set(plaza);
        }
    }

    private void generateTestUsersWithReservas(Context context) {
        String[] emails = {"u1@u.com", "u2@u.com", "u3@u.com"};
        String[] passwords = {"password1", "password1", "password1"};
        generateUserRecursively(context, emails, passwords, 0);
    }

    private void generateUserRecursively(Context context, String[] emails, String[] passwords, int index) {
        if (index >= emails.length) return;

        String email = emails[index];
        String password = passwords[index];

        auth.fetchSignInMethodsForEmail(email)
                .addOnSuccessListener(result -> {
                    boolean exists = result.getSignInMethods() != null && !result.getSignInMethods().isEmpty();
                    if (!exists) {
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnSuccessListener(authResult -> {
                                    FirebaseUser user = authResult.getUser();
                                    if (user != null) {
                                        String uid = user.getUid();
                                        String qr = uid + "_" + UUID.randomUUID().toString().substring(0, 8);

                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("email", email);
                                        userData.put("qrCode", qr);
                                        userData.put("userId", uid);
                                        userData.put("createdAt", System.currentTimeMillis());

                                        Vehiculo v = switch (index) {
                                            case 0 -> new Vehiculo("Toyota", "Corolla", "1111AAA", false, false, "Coche");
                                            case 1 -> new Vehiculo("Seat", "Mii", "2222BBB", true, false, "Coche");
                                            default -> new Vehiculo("Yamaha", "MT-07", "3333CCC", false, false, "Moto");
                                        };

                                        Map<String, Object> vehiculoMap = new HashMap<>();
                                        vehiculoMap.put("marca", v.getMarca());
                                        vehiculoMap.put("modelo", v.getModelo());
                                        vehiculoMap.put("matricula", v.getMatricula());
                                        vehiculoMap.put("electrico", v.isElectrico());
                                        vehiculoMap.put("discapacidad", v.isDiscapacidad());
                                        vehiculoMap.put("tipo", v.getTipo());

                                        userData.put("vehiculo", vehiculoMap);

                                        db.collection("users").document(uid).set(userData)
                                                .addOnSuccessListener(unused -> {
                                                    android.util.Log.d("DataRepository", "Usuario creado: " + email);
                                                    crearReservas(uid, index);
                                                    // Procesa el siguiente usuario
                                                    generateUserRecursively(context, emails, passwords, index + 1);
                                                })
                                                .addOnFailureListener(e -> {
                                                    android.util.Log.e("DataRepository", "Error guardando usuario: " + email, e);
                                                    generateUserRecursively(context, emails, passwords, index + 1);
                                                });
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    android.util.Log.e("DataRepository", "Error creando usuario: " + email, e);
                                    generateUserRecursively(context, emails, passwords, index + 1);
                                });
                    } else {
                        android.util.Log.d("DataRepository", "El usuario ya existe: " + email);
                        generateUserRecursively(context, emails, passwords, index + 1);
                    }
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("DataRepository", "Error comprobando email: " + email, e);
                    generateUserRecursively(context, emails, passwords, index + 1);
                });
    }

    private void crearReservas(String userId, int index) {
        CollectionReference reservasRef = db.collection("users").document(userId).collection("reservas");

        LocalDate hoy = LocalDate.now();
        LocalDate ayer = hoy.minusDays(1);

        // Reserva activa para hoy
        Reserva activa = new Reserva(hoy, LocalTime.of(10 + index, 0), LocalTime.of(11 + index, 0));
        activa.setPlaza(new Plaza("P" + (index + 1), Plaza.Tipo.NORMAL, Plaza.Estado.OCUPADA));
        reservasRef.add(activa);

        // Reserva pasada
        Reserva pasada = new Reserva(ayer, LocalTime.of(8 + index, 0), LocalTime.of(9 + index, 0));
        pasada.setPlaza(new Plaza("P" + (index + 5), Plaza.Tipo.NORMAL, Plaza.Estado.LIBRE));
        reservasRef.add(pasada);
    }
}
