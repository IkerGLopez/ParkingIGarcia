package com.lksnext.parkingplantilla.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.lksnext.parkingplantilla.databinding.ActivityRegisterBinding;
import com.lksnext.parkingplantilla.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Asignamos la vista/interfaz de registro
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Asignamos el viewModel de register
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding.btnContinuar.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, RegisterCarActivity.class);
            startActivity(intent);
        });
    }
}