package com.lksnext.parkingplantilla.view.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.lksnext.parkingplantilla.R;
import com.lksnext.parkingplantilla.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private Button reservarBtn, editarBtn, historialBtn, qrBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Referencias a los botones del layout
        reservarBtn = findViewById(R.id.reservarBtn);
        editarBtn = findViewById(R.id.editarBtn);
        historialBtn = findViewById(R.id.historialBtn);
        qrBtn = findViewById(R.id.qrBtn);

        // Asignar acciones a los botones
        reservarBtn.setOnClickListener(v -> {
            // Abrir actividad de reservas, por ejemplo:
            // startActivity(new Intent(this, ReservarActivity.class));
        });

        editarBtn.setOnClickListener(v -> {
            // Abrir actividad para editar reservas
        });

        historialBtn.setOnClickListener(v -> {
            // Abrir historial
        });

        qrBtn.setOnClickListener(v -> {
            // Mostrar código QR
        });

        // También puedes configurar el botón flotante (logout)
//        findViewById(R.id.logoutBtn).setOnClickListener(v -> {
//             Cerrar sesión o volver a login
//        });
    }
}
