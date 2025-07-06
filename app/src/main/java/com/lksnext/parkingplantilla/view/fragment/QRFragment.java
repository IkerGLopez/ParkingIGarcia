package com.lksnext.parkingplantilla.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lksnext.parkingplantilla.databinding.FragmentQrBinding;
import com.lksnext.parkingplantilla.view.activity.LoginActivity;
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;

public class QRFragment extends Fragment {

    private FragmentQrBinding binding;
    private MainViewModel viewModel;

    public QRFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQrBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        viewModel.getQrBitmap().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null) {
                binding.imageViewQrCode.setImageBitmap(bitmap);
                binding.imageViewQrCode.setVisibility(View.VISIBLE);
            } else {
                binding.imageViewQrCode.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "No se pudo generar el código QR", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.generarQR(requireContext());

        binding.cerrarSesion.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Está seguro de que desea cerrar sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        viewModel.cerrarSesion();
                        Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.generarQR(requireContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}