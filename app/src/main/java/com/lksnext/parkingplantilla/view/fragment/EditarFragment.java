package com.lksnext.parkingplantilla.view.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.lksnext.parkingplantilla.R;
import com.lksnext.parkingplantilla.databinding.FragmentEditarBinding;
import com.lksnext.parkingplantilla.model.adapter.ReservaActivaAdapter;
import com.lksnext.parkingplantilla.model.data.FirebaseService;
import com.lksnext.parkingplantilla.model.data.FirebaseServiceImpl;
import com.lksnext.parkingplantilla.model.domain.Callback;
import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.model.listener.ReservaListener;
import com.lksnext.parkingplantilla.model.utils.HourValidator;
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditarFragment extends Fragment implements ReservaListener {
    private FragmentEditarBinding binding;
    private FirebaseService firebaseService;
    private ReservaActivaAdapter adapter;
    private List<ReservaActivaAdapter.ReservaConId> reservasActivas = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            binding = FragmentEditarBinding.inflate(inflater, container, false);
            firebaseService = new FirebaseServiceImpl();

            // Configurar RecyclerView con LayoutManager
            binding.rvReservasActivas.setLayoutManager(new LinearLayoutManager(requireContext()));

            adapter = new ReservaActivaAdapter(reservasActivas, this);
            binding.rvReservasActivas.setAdapter(adapter);

            cargarReservas();

            return binding.getRoot();
        } catch (Exception e) {
            Log.e("EditarFragment", "Error en onCreateView", e);
            // Retornar una vista simple en caso de error
            TextView errorView = new TextView(requireContext());
            errorView.setText("Error al cargar el fragmento");
            return errorView;
        }
    }

    private void cargarReservas() {
        try {
            firebaseService.obtenerReservasUsuario((reservasMap) -> {
                try {
                    if (reservasMap != null) {
                        reservasActivas.clear();
                        for (Map.Entry<String, Reserva> entry : reservasMap.entrySet()) {
                            if (entry.getValue() != null && entry.getKey() != null) {
                                reservasActivas.add(new ReservaActivaAdapter.ReservaConId(entry.getValue(), entry.getKey()));
                            }
                        }
                        if (adapter != null) {
                            adapter.setReservas(reservasActivas);
                        }
                    }
                } catch (Exception e) {
                    Log.e("EditarFragment", "Error al procesar reservas", e);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error al cargar reservas", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("EditarFragment", "Error al cargar reservas", e);
            if (getContext() != null) {
                Toast.makeText(getContext(), "Error al conectar con Firebase", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCancelar(Reserva reserva, String docId) {
        if (docId == null) {
            Toast.makeText(requireContext(), "Error: ID de reserva inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseService.eliminarReserva(docId, new Callback() {
            @Override
            public void onSuccess() {
                try {
                    // Quitar de la lista y actualizar
                    reservasActivas.removeIf(item -> item.getDocId().equals(docId));
                    if (adapter != null) {
                        adapter.setReservas(reservasActivas);
                    }
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Reserva cancelada", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("EditarFragment", "Error al actualizar UI después de cancelar", e);
                }
            }

            @Override
            public void onFailure() {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error al cancelar reserva", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


