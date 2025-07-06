// MainFragment.java
package com.lksnext.parkingplantilla.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textview.MaterialTextView;
import com.lksnext.parkingplantilla.R;

public class MainFragment extends Fragment {

    public MainFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        MaterialTextView title = view.findViewById(R.id.title);
//        Button reservarBtn = view.findViewById(R.id.reservarBtn);
//        Button editarBtn = view.findViewById(R.id.editarBtn);
//        Button historialBtn = view.findViewById(R.id.historialBtn);
//        Button qrBtn = view.findViewById(R.id.qrBtn);

//        reservarBtn.setOnClickListener(v -> {
//            // Acción para reservar
//        });
//
//        editarBtn.setOnClickListener(v -> {
//            // Acción para editar
//        });
//
//        historialBtn.setOnClickListener(v -> {
//            // Acción para historial
//        });
//
//        qrBtn.setOnClickListener(v -> {
//            // Acción para QR
//        });
    }
}
