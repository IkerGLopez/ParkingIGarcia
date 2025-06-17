package com.lksnext.parkingplantilla.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingplantilla.domain.Callback;
import com.lksnext.parkingplantilla.domain.Parking;
import com.lksnext.parkingplantilla.domain.Reserva;
import com.lksnext.parkingplantilla.domain.Usuario;

public class FirebaseServiceImpl implements FirebaseService {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    public FirebaseServiceImpl() {
        this.auth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void registerUser(Usuario usuario, Callback callback) {
        String email = usuario.getEmail();
        String contrasena = usuario.getContrasena();

        auth.createUserWithEmailAndPassword(email, contrasena)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();

                    // Por seguridad, no guardes la contraseña
                    usuario.setContrasena(null);

                    firestore.collection("users")
                            .document(uid)
                            .set(usuario)
                            .addOnSuccessListener(unused -> {
                                callback.onSuccess(); // Usuario guardado con éxito
                            })
                            .addOnFailureListener(e -> {
                                callback.onFailure(); // Falló al guardar en Firestore
                            });
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(); // Falló el registro (email duplicado, etc.)
                });
    }

    @Override
public void addReserva(Reserva reserva) {

}

@Override
public void storeParking(Parking parking) {

}

@Override
public void login(Usuario usuario) {

}
}
