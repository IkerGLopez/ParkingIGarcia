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
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;

public class HistorialFragment extends Fragment {

//    private static final String TAG = "HistorialFragment";
//    // private HistorialViewModel historialViewModel;
//    private MainViewModel mainViewModel;
//    private RecyclerView recyclerViewHistorial; // Ejemplo con RecyclerView
//    // private HistorialAdapter historialAdapter; // Ejemplo con RecyclerView
//    private TextView textViewEmptyState;
//
//    public HistorialFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // historialViewModel = new ViewModelProvider(this).get(HistorialViewModel.class);
//        if (getActivity() != null) {
//            mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_historial, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        recyclerViewHistorial = view.findViewById(R.id.recyclerViewHistorial); // Asume IDs
//        textViewEmptyState = view.findViewById(R.id.textViewHistorialEmpty);
//
//        setupRecyclerView();
//        Log.d(TAG, "HistorialFragment cargado.");
//
//        // Observar datos del historial desde el ViewModel
//        // historialViewModel.getHistorialItems().observe(getViewLifecycleOwner(), items -> {
//        //     if (items != null && !items.isEmpty()) {
//        //         historialAdapter.submitList(items);
//        //         recyclerViewHistorial.setVisibility(View.VISIBLE);
//        //         textViewEmptyState.setVisibility(View.GONE);
//        //     } else {
//        //         recyclerViewHistorial.setVisibility(View.GONE);
//        //         textViewEmptyState.setVisibility(View.VISIBLE);
//        //     }
//        // });
//
//        if (mainViewModel != null) {
//            mainViewModel.isUserLoggedIn.observe(getViewLifecycleOwner(), isLoggedIn -> {
//                Log.d(TAG, "Estado de login en HistorialFragment: " + isLoggedIn);
//                if (isLoggedIn != null && isLoggedIn) {
//                    // Cargar historial
//                    // historialViewModel.loadHistorial();
//                } else {
//                    // Limpiar historial o mostrar mensaje de login requerido
//                    // historialAdapter.submitList(null); // o una lista vacía
//                    // recyclerViewHistorial.setVisibility(View.GONE);
//                    // textViewEmptyState.setText("Inicia sesión para ver tu historial");
//                    // textViewEmptyState.setVisibility(View.VISIBLE);
//                }
//            });
//        }
//    }
//
//    private void setupRecyclerView() {
//        // historialAdapter = new HistorialAdapter(); // Inicializa tu adapter
//        // recyclerViewHistorial.setLayoutManager(new LinearLayoutManager(getContext()));
//        // recyclerViewHistorial.setAdapter(historialAdapter);
//        // De momento, un log para indicar que se configuraría
//        Log.d(TAG, "RecyclerView del historial se configuraría aquí.");
//    }
}