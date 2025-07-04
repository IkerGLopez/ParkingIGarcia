package com.lksnext.parkingplantilla.model.data;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lksnext.parkingplantilla.model.domain.Callback;
import com.lksnext.parkingplantilla.model.domain.CallbackBoolean;
import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.model.domain.Usuario;
import com.lksnext.parkingplantilla.model.listener.OnReservasObtenidasListener;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    public void checkEmailExists(String email, CallbackBoolean callback) {
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean emailExists = task.getResult().getSignInMethods() != null &&
                                !task.getResult().getSignInMethods().isEmpty();
                        callback.onComplete(emailExists);
                    } else {
                        callback.onComplete(true); // asumimos que existe si hay error
                    }
                });
    }


    @Override
    public void addReserva(Reserva reserva) {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Log.e("FirebaseService", "Usuario no autenticado");
            return;
        }

        String userId = user.getUid();

        firestore.collection("users")
                .document(userId)
                .collection("reservas")
                .add(reserva)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FirebaseService", "Reserva guardada con ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseService", "Error al guardar reserva", e);
                });
    }


    @Override
    public void login(Usuario usuario) {

    }

    @Override
    public void eliminarReserva(String docId, Callback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Log.e("FirebaseService", "Usuario no autenticado");
            callback.onFailure();
            return;
        }

        firestore.collection("users")
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

    @Override
    public void obtenerReservasUsuario(OnReservasObtenidasListener listener) {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Log.e("FirebaseService", "Usuario no autenticado");
            listener.onComplete(Collections.emptyMap());
            return;
        }

        String userId = user.getUid();

        firestore.collection("users")
                .document(userId)
                .collection("reservas")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Map<String, Reserva> reservasMap = new HashMap<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Reserva reserva = doc.toObject(Reserva.class);
                        reservasMap.put(doc.getId(), reserva);
                    }
                    listener.onComplete(reservasMap);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseService", "Error al obtener reservas", e);
                    listener.onComplete(Collections.emptyMap());
                });
    }

}


