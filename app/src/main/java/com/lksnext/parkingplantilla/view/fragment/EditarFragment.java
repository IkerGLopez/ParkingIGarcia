package com.lksnext.parkingplantilla.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.lksnext.parkingplantilla.databinding.FragmentEditarBinding;
import com.lksnext.parkingplantilla.model.adapter.ReservaActivaAdapter;
import com.lksnext.parkingplantilla.model.domain.Callback;
import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.model.listener.ReservaListener;
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;

import java.util.ArrayList;

public class EditarFragment extends Fragment implements ReservaListener {
    private FragmentEditarBinding binding;
    private MainViewModel viewModel;
    private ReservaActivaAdapter adapter;

    public EditarFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        adapter = new ReservaActivaAdapter(new ArrayList<>(), this);
        binding.rvReservasActivas.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvReservasActivas.setAdapter(adapter);

        viewModel.getReservasActivas().observe(getViewLifecycleOwner(), reservas -> {
            if (reservas != null && !reservas.isEmpty()) {
                adapter.setReservas(reservas);
                binding.rvReservasActivas.setVisibility(View.VISIBLE);
                binding.tvSinReservas.setVisibility(View.GONE);
            } else {
                binding.rvReservasActivas.setVisibility(View.GONE);
                binding.tvSinReservas.setVisibility(View.VISIBLE);
            }
        });

        viewModel.cargarReservasActivas();
    }

    @Override
    public void onCancelar(Reserva reserva, String docId) {
        if (docId == null) {
            Toast.makeText(requireContext(), "Error: ID de reserva inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Cancelar reserva")
                .setMessage("¿Estás seguro de que quieres cancelar esta reserva?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    viewModel.cancelarReserva(docId, new Callback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(requireContext(), "Reserva cancelada", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(requireContext(), "Error al cancelar reserva", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


