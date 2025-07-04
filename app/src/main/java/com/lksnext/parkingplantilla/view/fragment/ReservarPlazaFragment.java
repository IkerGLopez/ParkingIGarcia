package com.lksnext.parkingplantilla.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lksnext.parkingplantilla.R;
import com.lksnext.parkingplantilla.model.data.FirebaseService;
import com.lksnext.parkingplantilla.model.data.FirebaseServiceImpl;
import com.lksnext.parkingplantilla.model.domain.Plaza;
import com.lksnext.parkingplantilla.model.adapter.PlazaAdapter;
import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReservarPlazaFragment extends Fragment {

    private MainViewModel viewModel;
    private RecyclerView recyclerViewPlazas;
    private Button btnReservar;
    private PlazaAdapter adapter;

    public ReservarPlazaFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reservar_plaza, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        recyclerViewPlazas = view.findViewById(R.id.recyclerViewPlazas);
        btnReservar = view.findViewById(R.id.btnReservarAparcamiento);

        recyclerViewPlazas.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Cargar lista de plazas (esto luego lo puedes traer de Firebase)
        List<Plaza> plazaList = getPlazasDePrueba();

        // Crear adaptador
        adapter = new PlazaAdapter(requireContext(), plazaList, plazaSeleccionada -> {
            // Guardar plaza seleccionada en ViewModel
            viewModel.setSelectedPlaza(plazaSeleccionada);

            // Habilitar el botón
            btnReservar.setEnabled(true);
            btnReservar.setBackgroundColor(getResources().getColor(R.color.Moradito));
        });

        recyclerViewPlazas.setAdapter(adapter);

        // Botón comienza deshabilitado
        btnReservar.setEnabled(false);
        btnReservar.setBackgroundColor(getResources().getColor(R.color.NotMoradito));

        btnReservar.setOnClickListener(v -> {
            Reserva reserva = (Reserva) viewModel.getReservaData().getValue();
            Plaza plaza = (Plaza) viewModel.getSelectedPlaza().getValue();

            if (reserva == null || plaza == null) {
                Toast.makeText(getContext(), "Falta información para completar la reserva", Toast.LENGTH_SHORT).show();
                return;
            }

            reserva.setPlaza(plaza);

            FirebaseService firebaseService = new FirebaseServiceImpl();
            firebaseService.addReserva(reserva);

            Toast.makeText(getContext(), "Reserva enviada", Toast.LENGTH_SHORT).show();
        });
    }

    private List<Plaza> getPlazasDePrueba() {
        List<Plaza> plazas = new ArrayList<>();

        plazas.add(new Plaza("P1", Plaza.Tipo.NORMAL, Plaza.Estado.LIBRE));
        plazas.add(new Plaza("P2", Plaza.Tipo.ELECTRICO, Plaza.Estado.OCUPADA));
        plazas.add(new Plaza("P3", Plaza.Tipo.MOTO, Plaza.Estado.LIBRE));
        plazas.add(new Plaza("P4", Plaza.Tipo.MINUSVALIDO, Plaza.Estado.LIBRE));
        plazas.add(new Plaza("P5", Plaza.Tipo.NORMAL, Plaza.Estado.OCUPADA));
        plazas.add(new Plaza("P6", Plaza.Tipo.ELECTRICO, Plaza.Estado.LIBRE));

        return plazas;
    }
}