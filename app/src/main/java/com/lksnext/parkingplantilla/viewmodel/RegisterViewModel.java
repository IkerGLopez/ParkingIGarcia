package com.lksnext.parkingplantilla.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingplantilla.model.data.FirebaseService;
import com.lksnext.parkingplantilla.model.data.FirebaseServiceImpl;
import com.lksnext.parkingplantilla.model.utils.InputValidator;
import com.lksnext.parkingplantilla.model.utils.PasswordValidator;
import com.lksnext.parkingplantilla.model.utils.ValidationResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<Boolean> registered = new MutableLiveData<>(null);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);
    private final MutableLiveData<String> userUid = new MutableLiveData<>(null);

    private final FirebaseService firebaseService = new FirebaseServiceImpl();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LiveData<Boolean> isRegistered() {
        return registered;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getUserUid() {
        return userUid;
    }

    public void registrarUsuario(String email, String password, String password2) {
        ValidationResult emailVal = InputValidator.validateNotEmpty(email);
        if (!emailVal.isSuccess()) {
            errorMessage.setValue(emailVal.getErrorMessage());
            registered.setValue(Boolean.FALSE);
            return;
        }

        ValidationResult pswVal = InputValidator.validateNotEmpty(password);
        if (!pswVal.isSuccess()) {
            errorMessage.setValue(pswVal.getErrorMessage());
            registered.setValue(Boolean.FALSE);
            return;
        }

        ValidationResult psw2Val = InputValidator.validateNotEmpty(password2);
        if (!psw2Val.isSuccess()) {
            errorMessage.setValue(psw2Val.getErrorMessage());
            registered.setValue(Boolean.FALSE);
            return;
        }

        ValidationResult pswMatch = PasswordValidator.passwordValidator(password, password2);
        if (!pswMatch.isSuccess()) {
            errorMessage.setValue(pswMatch.getErrorMessage());
            registered.setValue(Boolean.FALSE);
            return;
        }

        // Verificar si el email ya está en uso
        firebaseService.checkEmailExists(email, exists -> {
            if (exists) {
                errorMessage.setValue("El correo ya está registrado.");
                registered.setValue(Boolean.FALSE);
                return;
            }

            // Crear usuario en Firebase Auth
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = task.getResult().getUser();
                                if (firebaseUser != null) {
                                    userUid.setValue(firebaseUser.getUid());
                                    saveUserDataWithQR(firebaseUser, email);
                                }
                            } else {
                                registered.setValue(Boolean.FALSE);
                                if (task.getException() != null) {
                                    errorMessage.setValue(task.getException().getMessage());
                                } else {
                                    errorMessage.setValue("Error desconocido durante el registro.");
                                }
                            }
                        }
                    });
        });
    }


    private void saveUserDataWithQR(FirebaseUser firebaseUser, String email) {
        // Generar un código QR único basado en el UID del usuario
        String uniqueQRCode = generateUniqueQRCode(firebaseUser.getUid());

        // Crear mapa con los datos del usuario
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("qrCode", uniqueQRCode);
        userData.put("userId", firebaseUser.getUid());
        userData.put("createdAt", System.currentTimeMillis());

        // Guardar en Firestore
        db.collection("users")
                .document(firebaseUser.getUid())
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    registered.setValue(Boolean.TRUE);
                })
                .addOnFailureListener(e -> {
                    registered.setValue(Boolean.FALSE);
                    errorMessage.setValue("Error al guardar datos del usuario: " + e.getMessage());
                });
    }

    private String generateUniqueQRCode(String userId) {
        // Generar un código único combinando el UID del usuario con un UUID
        return userId + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}