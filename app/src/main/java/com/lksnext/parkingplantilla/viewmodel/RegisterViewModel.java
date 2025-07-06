package com.lksnext.parkingplantilla.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.parkingplantilla.model.data.FirebaseService;
import com.lksnext.parkingplantilla.model.data.FirebaseServiceImpl;
import com.lksnext.parkingplantilla.model.domain.Callback;
import com.lksnext.parkingplantilla.model.utils.InputValidator;
import com.lksnext.parkingplantilla.model.utils.PasswordValidator;
import com.lksnext.parkingplantilla.model.utils.ValidationResult;

import java.util.UUID;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<Boolean> registered = new MutableLiveData<>(null);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);
    private final MutableLiveData<String> userUid = new MutableLiveData<>(null);
    private FirebaseService firebaseService;

    public RegisterViewModel() {
        firebaseService = new FirebaseServiceImpl();
    }

    public RegisterViewModel(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

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
            registered.setValue(false);
            return;
        }

        ValidationResult pswVal = InputValidator.validateNotEmpty(password);
        if (!pswVal.isSuccess()) {
            errorMessage.setValue(pswVal.getErrorMessage());
            registered.setValue(false);
            return;
        }

        ValidationResult psw2Val = InputValidator.validateNotEmpty(password2);
        if (!psw2Val.isSuccess()) {
            errorMessage.setValue(psw2Val.getErrorMessage());
            registered.setValue(false);
            return;
        }

        ValidationResult pswMatch = PasswordValidator.passwordValidator(password, password2);
        if (!pswMatch.isSuccess()) {
            errorMessage.setValue(pswMatch.getErrorMessage());
            registered.setValue(false);
            return;
        }

        firebaseService.checkEmailExists(email, exists -> {
            if (exists) {
                errorMessage.setValue("El correo ya está registrado.");
                registered.setValue(false);
            } else {
                firebaseService.registerUser(email, password, new Callback() {
                    @Override
                    public void onSuccess() {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        userUid.setValue(uid);

                        String qrCode = generateUniqueQRCode(uid);

                        firebaseService.saveUserData(uid, email, qrCode, new Callback() {
                            @Override
                            public void onSuccess() {
                                registered.setValue(true);
                            }

                            @Override
                            public void onFailure() {
                                errorMessage.setValue("Error al guardar datos del usuario.");
                                registered.setValue(false);
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        errorMessage.setValue("Error al crear la cuenta. Verifica los datos.");
                        registered.setValue(false);
                    }
                });
            }
        });
    }

    private String generateUniqueQRCode(String userId) {
        // Generar un código único combinando el UID con un UUID
        return userId + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}