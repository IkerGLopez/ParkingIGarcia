package com.lksnext.parkingplantilla.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.lksnext.parkingplantilla.databinding.ActivityRegisterCarBinding;
import com.lksnext.parkingplantilla.viewmodel.RegisterCarViewModel;

public class RegisterCarActivity extends AppCompatActivity {
    private ActivityRegisterCarBinding binding;
    private RegisterCarViewModel registerCarViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Asignamos la vista/interfaz de registro
        binding = ActivityRegisterCarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Asignamos el viewModel de register
        registerCarViewModel = new ViewModelProvider(this).get(RegisterCarViewModel.class);

        binding.btnContinuar.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterCarActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}