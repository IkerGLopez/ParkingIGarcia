package com.lksnext.parkingplantilla.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.lksnext.parkingplantilla.R;
// import com.lksnext.parkingplantilla.viewmodel.ConfigViewModel;
import com.lksnext.parkingplantilla.view.activity.LoginActivity;
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;

import java.util.HashMap;
import java.util.Map;

public class ConfigFragment extends Fragment {

    private RadioGroup radioGroupTipo;
    private RadioButton radioBtnCoche, radioBtnMoto;
    private TextInputEditText marcaText, modeloText, matriculaText;
    private Switch electricoSwitch, discapacidadSwitch;
    private Button btnGuardar;

    private FirebaseFirestore db;
    private String uid;

    // Para controlar si es primera vez
    private boolean isNewUser = false;

    private String initialMarca, initialModelo, initialMatricula;
    private boolean initialElectrico, initialDiscapacidad;
    private String initialTipoVehiculo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("ConfigFragment", "Cargado fragmento, isNewUser: " + isNewUser);


        View view = inflater.inflate(R.layout.fragment_config, container, false);

        radioGroupTipo = view.findViewById(R.id.radioBtns);
        radioBtnCoche = view.findViewById(R.id.RadioBtnCoche);
        radioBtnMoto = view.findViewById(R.id.RadioBtnMoto);
        marcaText = view.findViewById(R.id.marcaText);
        modeloText = view.findViewById(R.id.modeloText);
        matriculaText = view.findViewById(R.id.matriculaText);
        electricoSwitch = view.findViewById(R.id.electricoSw);
        discapacidadSwitch = view.findViewById(R.id.discapacidadSw);
        btnGuardar = view.findViewById(R.id.btnContinuar);

        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (getArguments() != null) {
            isNewUser = getArguments().getBoolean("isNewUser", false);
        }

        cargarDatosVehiculo();

        btnGuardar.setOnClickListener(v -> guardarDatosVehiculo());

        return view;
    }

    private void cargarDatosVehiculo() {
        db.collection("usuarios").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> vehiculo = (Map<String, Object>) documentSnapshot.get("vehiculo");
                        if (vehiculo != null) {
                            initialMarca = (String) vehiculo.get("marca");
                            initialModelo = (String) vehiculo.get("modelo");
                            initialMatricula = (String) vehiculo.get("matricula");
                            initialElectrico = vehiculo.get("electrico") != null && (Boolean) vehiculo.get("electrico");
                            initialDiscapacidad = vehiculo.get("discapacidad") != null && (Boolean) vehiculo.get("discapacidad");
                            initialTipoVehiculo = (String) vehiculo.get("tipo");

                            marcaText.setText(initialMarca);
                            modeloText.setText(initialModelo);
                            matriculaText.setText(initialMatricula);
                            electricoSwitch.setChecked(initialElectrico);
                            discapacidadSwitch.setChecked(initialDiscapacidad);

                            if ("Moto".equalsIgnoreCase(initialTipoVehiculo)) {
                                radioBtnMoto.setChecked(true);
                            } else {
                                radioBtnCoche.setChecked(true);
                            }
                        } else {
                            // Si no hay vehículo registrado
                            initialMarca = "";
                            initialModelo = "";
                            initialMatricula = "";
                            initialElectrico = false;
                            initialDiscapacidad = false;
                            initialTipoVehiculo = "Coche";
                            radioBtnCoche.setChecked(true);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al cargar vehículo", Toast.LENGTH_SHORT).show());
    }

    private void guardarDatosVehiculo() {
        String marca = marcaText.getText().toString().trim();
        String modelo = modeloText.getText().toString().trim();
        String matricula = matriculaText.getText().toString().trim();
        boolean electrico = electricoSwitch.isChecked();
        boolean discapacidad = discapacidadSwitch.isChecked();
        String tipoVehiculo = radioBtnMoto.isChecked() ? "Moto" : "Coche";

        if (marca.isEmpty() || modelo.isEmpty() || matricula.isEmpty()) {
            Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> vehiculoMap = new HashMap<>();
        vehiculoMap.put("marca", marca);
        vehiculoMap.put("modelo", modelo);
        vehiculoMap.put("matricula", matricula);
        vehiculoMap.put("electrico", electrico);
        vehiculoMap.put("discapacidad", discapacidad);
        vehiculoMap.put("tipo", tipoVehiculo);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("vehiculo", vehiculoMap);

        db.collection("users").document(uid)
                .set(updateMap, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Vehículo guardado", Toast.LENGTH_SHORT).show();

                    // Si es nuevo usuario, ya terminó la configuración inicial, ir a Main o lo que toque
                    if (isNewUser) {
                        // Por ejemplo, cerrar todo el stack y abrir pantalla principal
                        requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        // Aquí lanzar fragment o actividad principal si tienes
                    } else {
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error guardando vehículo", Toast.LENGTH_SHORT).show());
    }
}