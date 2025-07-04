package com.lksnext.parkingplantilla.viewmodel;


import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.parkingplantilla.model.utils.InputValidator;
import com.lksnext.parkingplantilla.model.utils.ValidationResult;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<Boolean> logged = new MutableLiveData<>(null);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
        if (!emailValidation.isSuccess()) {
            errorMessage.setValue(passValidation.getErrorMessage());
            logged.setValue(Boolean.FALSE);
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            logged.setValue(Boolean.TRUE);
                            return;
                        } else {
                            logged.setValue(Boolean.FALSE);
                            return;
                        }
                    }
                });
    }

    public void forgotPassword(Context context) {
        EditText input = new EditText(context);
        input.setHint("Email");
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setPadding(50, 40, 50, 40); //espacio con las esquinas

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Recuperar contraseña.");
        builder.setMessage("Inserta tu email");
        builder.setView(input); //añadir el campo de texto

        builder.setPositiveButton("Confirmar", (dialog, which) -> {
            Toast.makeText(context, "Correo enviado.", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
