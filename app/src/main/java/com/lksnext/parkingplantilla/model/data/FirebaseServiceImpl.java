package com.lksnext.parkingplantilla.model.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.lksnext.parkingplantilla.model.domain.Callback;
import com.lksnext.parkingplantilla.model.domain.CallbackBoolean;
import com.lksnext.parkingplantilla.model.domain.Plaza;
import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.model.domain.Vehiculo;
import com.lksnext.parkingplantilla.model.listener.OnReservasObtenidasListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FirebaseServiceImpl implements FirebaseService {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public FirebaseServiceImpl() {
        this.auth = FirebaseAuth.getInstance();
//        this.db = FirebaseFirestore.getInstance();
    }

    public FirebaseServiceImpl(FirebaseAuth auth, FirebaseFirestore db) {
        this.auth = auth;
        this.db = db;
    }

    // =========== LOG IN ===========

    @Override
    public void login(String email, String password, Callback callback) {
        auth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure();
                    }
                });
    }

    @Override
    public void sendPasswordResetEmail(String email, Callback callback) {
        auth
                .sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure();
                    }
                });
    }

    public void saveUserDataToPrefs(Context context) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            prefs.edit()
                    .putString("user_id", user.getUid())
                    .putString("user_email", user.getEmail())
                    .apply();
        }
    }

    // =========== LOG IN ===========


    // =========== REGISTER ===========

    @Override
    public void checkEmailExists(String email, CallbackBoolean callback) {
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean exists = task.getResult().getSignInMethods() != null &&
                                !task.getResult().getSignInMethods().isEmpty();
                        callback.onComplete(exists);
                    } else {
                        callback.onComplete(false); // Asumimos que no existe si hay error
                    }
                });
    }

    @Override
    public void registerUser(String email, String password, Callback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure();
                    }
                });
    }

    @Override
    public void saveUserData(String uid, String email, String qrCode, Callback callback) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("qrCode", qrCode);
        userData.put("userId", uid);
        userData.put("createdAt", System.currentTimeMillis());

        db
                .collection("users")
                .document(uid)
                .set(userData)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure());
    }

    // saveUserDataToPrefs

    // =========== REGISTER ===========


    // =========== RESERVAR ===========

    @Override
    public void addReserva(Reserva reserva, Callback callback) {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Log.e("FirebaseService", "Usuario no autenticado");
            callback.onFailure();
            return;
        }

        String userId = user.getUid();

        db.collection("users")
                .document(userId)
                .collection("reservas")
                .add(reserva)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FirebaseService", "Reserva guardada con ID: " + documentReference.getId());
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseService", "Error al guardar reserva", e);
                    callback.onFailure();
                });
    }

    @Override
    public void obtenerPlazas(Consumer<List<Plaza>> callback) {
        db.collection("plazas")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Plaza> plazas = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        try {
                            Plaza plaza = doc.toObject(Plaza.class);
                            if (plaza != null) plazas.add(plaza);
                        } catch (Exception e) {
                            Log.e("FirebaseService", "Error al deserializar plaza", e);
                        }
                    }
                    callback.accept(plazas);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseService", "Error al obtener plazas", e);
                    callback.accept(Collections.emptyList());
                });
    }

    // =========== RESERVAR ===========


    // =========== EDITAR ===========

    @Override
    public void obtenerReservasUsuario(OnReservasObtenidasListener listener) {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Log.e("FirebaseService", "Usuario no autenticado");
            listener.onComplete(Collections.emptyMap());
            return;
        }

        String userId = user.getUid();

        db.collection("users")
                .document(userId)
                .collection("reservas")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Map<String, Reserva> reservasMap = new HashMap<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        try {
                            Reserva reserva = doc.toObject(Reserva.class);
                            if (reserva != null) {
                                reservasMap.put(doc.getId(), reserva);
                            }
                        } catch (Exception e) {
                            Log.e("FirebaseService", "Error al deserializar reserva: " + doc.getId(), e);
                        }
                    }
                    listener.onComplete(reservasMap);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseService", "Error al obtener reservas", e);
                    listener.onComplete(Collections.emptyMap());
                });
    }

    @Override
    public void eliminarReserva(String docId, Callback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Log.e("FirebaseService", "Usuario no autenticado");
            callback.onFailure();
            return;
        }

        db.collection("users")
                .document(user.getUid())
                .collection("reservas")
                .document(docId)
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.d("FirebaseService", "Reserva eliminada con éxito");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseService", "Error al eliminar reserva", e);
                    callback.onFailure();
                });
    }

    // =========== EDITAR ===========


    // =========== QR ===========

    @Override
    public void signOut() {
        auth.signOut();
    }

    // =========== QR ===========


    // =========== HISTORIAL ===========

    // obtenerReservassUsuario de EDITAR

    // =========== HISTORIAL ===========


    // =========== CONFIG ===========

    @Override
    public Task<Vehiculo> getVehiculoForCurrentUser() {
        String uid = auth.getCurrentUser().getUid();
        return db.collection("users").document(uid).get().continueWith(task -> {
            DocumentSnapshot snapshot = task.getResult();
            if (snapshot.exists()) {
                Map<String, Object> vehiculoMap = (Map<String, Object>) snapshot.get("vehiculo");
                if (vehiculoMap != null) {
                    return new Vehiculo(
                            (String) vehiculoMap.get("marca"),
                            (String) vehiculoMap.get("modelo"),
                            (String) vehiculoMap.get("matricula"),
                            vehiculoMap.get("electrico") != null && (Boolean) vehiculoMap.get("electrico"),
                            vehiculoMap.get("discapacidad") != null && (Boolean) vehiculoMap.get("discapacidad"),
                            (String) vehiculoMap.get("tipo")
                    );
                }
            }
            return null;
        });
    }

    @Override
    public Task<Void> saveVehiculoForCurrentUser(Vehiculo vehiculo) {
        String uid = auth.getCurrentUser().getUid();
        Map<String, Object> vehiculoMap = new HashMap<>();
        vehiculoMap.put("marca", vehiculo.getMarca());
        vehiculoMap.put("modelo", vehiculo.getModelo());
        vehiculoMap.put("matricula", vehiculo.getMatricula());
        vehiculoMap.put("electrico", vehiculo.isElectrico());
        vehiculoMap.put("discapacidad", vehiculo.isDiscapacidad());
        vehiculoMap.put("tipo", vehiculo.getTipo());

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("vehiculo", vehiculoMap);

        return db.collection("users").document(uid).set(updateMap, SetOptions.merge());
    }

    // =========== CONFIG ===========


    // No se usan (?)

    @Override
    public String getCurrentUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    @Override
    public String getCurrentUserEmail() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

}