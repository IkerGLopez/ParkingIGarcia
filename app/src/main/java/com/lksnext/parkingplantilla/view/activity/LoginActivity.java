package com.lksnext.parkingplantilla.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.lksnext.parkingplantilla.databinding.ActivityLoginBinding;
import com.lksnext.parkingplantilla.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Asignamos la vista/interfaz login (layout)
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Asignamos el viewModel de login
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        //Acciones a realizar cuando el usuario clica el boton de login
        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailText.getText().toString();
            String password = binding.passwordText.getText().toString();
            loginViewModel.loginUser(email, password);
        });

        //Acciones a realizar cuando el usuario clica el boton de crear cuenta (se cambia de pantalla)
        binding.createAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.forgotPassword.setOnClickListener(v -> {
            EditText input = new EditText(this);
            input.setHint("Email");
            input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            input.setPadding(50, 40, 50, 40); //espacio con las esquinas

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Recuperar contraseña.");
            builder.setMessage("Inserta tu email");
            builder.setView(input); //añadir el campo de texto

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                Toast.makeText(this, "Correo enviado.", Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        //Observamos la variable logged, la cual nos informara cuando el usuario intente hacer login y se
        //cambia de pantalla en caso de login correcto
        loginViewModel.isLogged().observe(this, logged -> {
            if (logged != null) {
                if (logged) {
                    //Login Correcto
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    //Login incorrecto
                }
            }
        });
    }
}