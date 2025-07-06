package com.lksnext.parkingplantilla.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.lksnext.parkingplantilla.R;
import com.lksnext.parkingplantilla.model.domain.Vehiculo;
import com.lksnext.parkingplantilla.model.utils.InputValidator;
import com.lksnext.parkingplantilla.model.utils.MatriculaValidator;
import com.lksnext.parkingplantilla.model.utils.ValidationResult;
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;

public class ConfigFragment extends Fragment {

    private RadioButton radioBtnCoche, radioBtnMoto;
    private TextInputEditText marcaText, modeloText, matriculaText;
    private Switch electricoSwitch, discapacidadSwitch;
    private Button btnGuardar;

    private boolean isNewUser = false;

    private MainViewModel viewModel;

    public ConfigFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_config, container, false);

        radioBtnCoche = view.findViewById(R.id.RadioBtnCoche);
        radioBtnMoto = view.findViewById(R.id.RadioBtnMoto);
        marcaText = view.findViewById(R.id.marcaText);
        modeloText = view.findViewById(R.id.modeloText);
        matriculaText = view.findViewById(R.id.matriculaText);
        electricoSwitch = view.findViewById(R.id.electricoSw);
        discapacidadSwitch = view.findViewById(R.id.discapacidadSw);
        btnGuardar = view.findViewById(R.id.btnContinuar);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        if (getArguments() != null) {
            isNewUser = getArguments().getBoolean("isNewUser", false);
        }

        viewModel.getVehiculoLiveData().observe(getViewLifecycleOwner(), vehiculo -> {
            if (vehiculo != null) {
                marcaText.setText(vehiculo.getMarca());
                modeloText.setText(vehiculo.getModelo());
                matriculaText.setText(vehiculo.getMatricula());
                electricoSwitch.setChecked(vehiculo.isElectrico());
                discapacidadSwitch.setChecked(vehiculo.isDiscapacidad());

                if ("Moto".equalsIgnoreCase(vehiculo.getTipo())) {
                    radioBtnMoto.setChecked(true);
                } else {
                    radioBtnCoche.setChecked(true);
                }
            }
        });

        btnGuardar.setOnClickListener(v -> {
            String marca = marcaText.getText().toString().trim();
            String modelo = modeloText.getText().toString().trim();
            String matricula = matriculaText.getText().toString().trim().toUpperCase(); // Matricula en mayúsculas

            boolean electrico = electricoSwitch.isChecked();
            boolean discapacidad = discapacidadSwitch.isChecked();
            String tipo = radioBtnMoto.isChecked() ? "Moto" : "Coche";

            ValidationResult marcaResult = InputValidator.validateNotEmpty(marca);
            ValidationResult modeloResult = InputValidator.validateNotEmpty(modelo);
            ValidationResult matriculaResult = MatriculaValidator.validate(matricula);

            if (!marcaResult.isSuccess()) {
                Toast.makeText(getContext(), marcaResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!modeloResult.isSuccess()) {
                Toast.makeText(getContext(), modeloResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!matriculaResult.isSuccess()) {
                Toast.makeText(getContext(), matriculaResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            Vehiculo vehiculo = new Vehiculo(marca, modelo, matricula, electrico, discapacidad, tipo);
            viewModel.guardarVehiculo(vehiculo, isNewUser,
                    () -> {
                        Toast.makeText(getContext(), "Vehículo guardado", Toast.LENGTH_SHORT).show();
                        if (isNewUser) {
                            requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        } else {
                            requireActivity().getSupportFragmentManager().popBackStack();
                        }
                    },
                    errorMsg -> Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show()
            );
        });


        viewModel.cargarDatosVehiculo();

        return view;
    }
}