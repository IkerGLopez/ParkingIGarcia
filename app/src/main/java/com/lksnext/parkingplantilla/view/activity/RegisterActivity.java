package com.lksnext.parkingplantilla.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.lksnext.parkingplantilla.databinding.ActivityRegisterBinding;
import com.lksnext.parkingplantilla.model.utils.QRGenerator;
import com.lksnext.parkingplantilla.model.utils.ValidationResult;
import com.lksnext.parkingplantilla.viewmodel.RegisterViewModel;

import java.io.FileOutputStream;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    TextInputEditText emailEditText, passwordEditText, password2EditText;
    Button register;
    TextView volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailEditText = binding.email;
        passwordEditText = binding.password;
        password2EditText = binding.password2;
        register = binding.btnRegister;
        volver = binding.volver;

        // OBSERVER 1: Registro completado
        registerViewModel.isRegistered().observe(this, registered -> {
            if (registered != null && registered) {
                Log.d("RegisterActivity", "Usuario registrado, lanzando MainActivity");

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("IS_NEW_USER", true);
                startActivity(intent);

                finish();
            }
        });

        // OBSERVER 2: UID obtenido
        registerViewModel.getUserUid().observe(this, uid -> {
            if (uid != null) {
                try {
                    String qrContent = String.format("{\"userId\":\"%s\",\"type\":\"parking\",\"version\":\"1.0\"}", uid);
                    Bitmap qrBitmap = QRGenerator.generateQRCodeBitmap(qrContent, 300, 300);
                    String filePath = getFilesDir().getPath() + "/" + uid + ".png";
                    QRGenerator.saveBitmapToFile(qrBitmap, filePath);

                    SharedPreferences prefs = getSharedPreferences("user_prefs", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("user_id", uid);
                    editor.apply();

                } catch (Exception e) {
                    Toast.makeText(this, "Error generando el QR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // OBSERVER 3: Errores
        registerViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        register.setOnClickListener(v -> {
            String email = String.valueOf(emailEditText.getText());
            String password = String.valueOf(passwordEditText.getText());
            String password2 = String.valueOf(password2EditText.getText());
            registerViewModel.registrarUsuario(email, password, password2);
        });

        volver.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}