package com.lksnext.parkingplantilla.view.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager; // Si usas RecyclerView
import androidx.recyclerview.widget.RecyclerView; // Si usas RecyclerView
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lksnext.parkingplantilla.R;
// import com.lksnext.parkingplantilla.viewmodel.HistorialViewModel;
// import com.lksnext.parkingplantilla.adapter.HistorialAdapter; // Si usas RecyclerView
import com.lksnext.parkingplantilla.model.adapter.ReservaPasadaAdapter;
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;

import java.util.ArrayList;

public class HistorialFragment extends Fragment {

    private MainViewModel viewModel;
    private ReservaPasadaAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvSinReservas;

    public HistorialFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_historial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvReservasPasadas);
        tvSinReservas = view.findViewById(R.id.tvSinReservas);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ReservaPasadaAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        viewModel.getReservasPasadas().observe(getViewLifecycleOwner(), reservas -> {
            boolean hayReservas = reservas != null && !reservas.isEmpty();

            tvSinReservas.setVisibility(hayReservas ? View.GONE : View.VISIBLE);
            recyclerView.setVisibility(hayReservas ? View.VISIBLE : View.GONE);

            adapter.setReservas(reservas);
        });

        viewModel.cargarReservasPasadas();
    }
}
