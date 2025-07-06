package com.lksnext.parkingplantilla.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lksnext.parkingplantilla.R;
import com.lksnext.parkingplantilla.model.data.FirebaseService;
import com.lksnext.parkingplantilla.model.data.FirebaseServiceImpl;
import com.lksnext.parkingplantilla.model.domain.Plaza;
import com.lksnext.parkingplantilla.model.adapter.PlazaAdapter;
import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.model.domain.Vehiculo;
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReservarPlazaFragment extends Fragment {

    private MainViewModel viewModel;
    private RecyclerView recyclerViewPlazas;
    private Button btnReservar;
    private PlazaAdapter adapter;
    private FirebaseService firebaseService;


    public ReservarPlazaFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reservar_plaza, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        firebaseService = new FirebaseServiceImpl();

        recyclerViewPlazas = view.findViewById(R.id.recyclerViewPlazas);
        btnReservar = view.findViewById(R.id.btnReservarAparcamiento);

        recyclerViewPlazas.setLayoutManager(new GridLayoutManager(getContext(), 2));

        btnReservar.setEnabled(false);
        btnReservar.setBackgroundColor(getResources().getColor(R.color.NotMoradito));

        firebaseService.obtenerPlazas(plazaList -> {
            firebaseService.getVehiculoForCurrentUser().addOnSuccessListener(vehiculo -> {
                if (vehiculo != null) {
                    List<Plaza> plazasCompatibles = new ArrayList<>();
                    for (Plaza plaza : plazaList) {
                        if (esCompatible(plaza, vehiculo)) {
                            plazasCompatibles.add(plaza);
                        } else if (plaza.getEstado() == Plaza.Estado.LIBRE) {
                            plaza.setEstado(Plaza.Estado.INACCESIBLE);
                            plazasCompatibles.add(plaza);
                        }
                    }
                    adapter = new PlazaAdapter(requireContext(), plazasCompatibles, plazaSeleccionada -> {
                        viewModel.setSelectedPlaza(plazaSeleccionada);
                        btnReservar.setEnabled(true);
                        btnReservar.setBackgroundColor(getResources().getColor(R.color.Moradito));
                    });
                    recyclerViewPlazas.setAdapter(adapter);
                }
            });
        });

        btnReservar.setOnClickListener(v -> {
            Reserva reserva = viewModel.getReservaData().getValue();
            Plaza plaza = viewModel.getSelectedPlaza().getValue();

            if (reserva == null || plaza == null) {
                Toast.makeText(getContext(), "Falta información para completar la reserva", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.reservarPlaza();
        });

        viewModel.getReservaCompletada().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Reserva reserva = viewModel.getReservaData().getValue();
                if (reserva != null) {
                    programarNotificacionesReserva(reserva);
                }
                Toast.makeText(getContext(), "Reserva realizada con éxito", Toast.LENGTH_SHORT).show();
                navigateToQRFragment();
                viewModel.resetReservaCompletada();
            } else if (success != null && !success) {
                Toast.makeText(getContext(), "Error al realizar la reserva", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToQRFragment() {
        QRFragment qrFragment = new QRFragment();

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        fragmentTransaction.replace(R.id.fragmentContainerView, qrFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private boolean esCompatible(Plaza plaza, Vehiculo vehiculo) {
        switch (plaza.getTipo()) {
            case NORMAL:
                return vehiculo.getTipo().equalsIgnoreCase("coche");
            case MOTO:
                return vehiculo.getTipo().equalsIgnoreCase("moto");
            case ELECTRICO:
                return vehiculo.isElectrico();
            case MINUSVALIDO:
                return vehiculo.isDiscapacidad();
            default:
                return false;
        }
    }

    private void programarNotificacionesReserva(Reserva reserva) {
        try {
            String fecha = reserva.getFecha(); // "2025-07-01"
            String horaInicio = reserva.getHoraInicio(); // "17:00"
            String horaFin = reserva.getHoraFin(); // "18:00"

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
            java.util.Date inicio = sdf.parse(fecha + " " + horaInicio);
            java.util.Date fin = sdf.parse(fecha + " " + horaFin);

            if (inicio == null || fin == null) return;

            long[] offsets = {30, 15}; // minutos antes
            for (long offset : offsets) {
                // Notificación antes de la hora de inicio
                programarAlarma(inicio.getTime() - offset * 60 * 1000,
                        "Reserva próxima",
                        "Tu reserva empieza en " + offset + " minutos");

                // Notificación antes de la hora de fin
                programarAlarma(fin.getTime() - offset * 60 * 1000,
                        "Reserva por terminar",
                        "Tu reserva termina en " + offset + " minutos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void programarAlarma(long triggerAtMillis, String title, String message) {
        if (triggerAtMillis < System.currentTimeMillis()) return; // No programar en el pasado

        android.app.AlarmManager alarmManager = (android.app.AlarmManager) requireContext().getSystemService(android.content.Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), com.lksnext.parkingplantilla.model.notification.ReservaNotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        int requestCode = (int) (triggerAtMillis % Integer.MAX_VALUE);
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(
                requireContext(), requestCode, intent, android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }
}