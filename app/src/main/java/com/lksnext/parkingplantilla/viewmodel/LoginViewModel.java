package com.lksnext.parkingplantilla.viewmodel;


import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingplantilla.R;
import com.lksnext.parkingplantilla.model.data.FirebaseService;
import com.lksnext.parkingplantilla.model.data.FirebaseServiceImpl;
import com.lksnext.parkingplantilla.model.domain.Callback;
import com.lksnext.parkingplantilla.model.utils.InputValidator;
import com.lksnext.parkingplantilla.model.utils.ValidationResult;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<Boolean> logged = new MutableLiveData<>(null);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);

    private FirebaseService firebaseService;

    public LoginViewModel() {
        this.firebaseService = new FirebaseServiceImpl();
    }

    public LoginViewModel(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    public LiveData<Boolean> isLogged() {
        return logged;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }


    public void loginUser(String email, String password) {
        ValidationResult emailValidation = InputValidator.validateNotEmpty(email);
        if (!emailValidation.isSuccess()) {
            errorMessage.setValue(emailValidation.getErrorMessage());
            logged.setValue(Boolean.FALSE);
            return;
        }

        ValidationResult passValidation = InputValidator.validateNotEmpty(password);
        if (!passValidation.isSuccess()) {
            errorMessage.setValue(passValidation.getErrorMessage());
            logged.setValue(Boolean.FALSE);
            return;
        }

        firebaseService.login(email, password, new Callback() {
            @Override
            public void onSuccess() {
                logged.setValue(true);
            }

            @Override
            public void onFailure() {
                logged.setValue(false);
                errorMessage.setValue("Error de autenticación. Verifica tu correo y contraseña.");
            }
        });
    }

    public void forgotPassword(Context context) {
        EditText input = new EditText(context);
        input.setId(R.id.dialog_email); // Asignar un ID al EditText
        input.setHint("Email");
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setPadding(50, 40, 50, 40);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Recuperar contraseña");
        builder.setMessage("Inserta tu email");
        builder.setView(input);

        builder.setPositiveButton("Confirmar", (dialog, which) -> {
            String email = input.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(context, "Introduce un correo válido.", Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseService.sendPasswordResetEmail(email, new Callback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, "Correo de recuperación enviado.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(context, "Error al enviar el correo.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
