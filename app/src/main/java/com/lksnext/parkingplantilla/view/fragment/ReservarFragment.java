package com.lksnext.parkingplantilla.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lksnext.parkingplantilla.R;
import com.lksnext.parkingplantilla.model.utils.DisabledDaysDecorator;
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.format.DateTimeFormatter;
import java.time.ZoneId;


public class ReservarFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private TextView tvTimeStart, tvTimeEnd;
    private Button btnIncreaseStart, btnDecreaseStart, btnIncreaseEnd, btnDecreaseEnd, btnSeleccionarHora;


    private final ZoneId zoneId = ZoneId.of("Europe/Madrid");

    private MainViewModel viewModel;

    public ReservarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservar, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        calendarView = view.findViewById(R.id.calendarView);
        tvTimeStart = view.findViewById(R.id.tvTimeStart);
        tvTimeEnd = view.findViewById(R.id.tvTimeEnd);
        btnIncreaseStart = view.findViewById(R.id.btnIncreaseStart);
        btnDecreaseStart = view.findViewById(R.id.btnDecreaseStart);
        btnIncreaseEnd = view.findViewById(R.id.btnIncreaseEnd);
        btnDecreaseEnd = view.findViewById(R.id.btnDecreaseEnd);
        btnSeleccionarHora = view.findViewById(R.id.btnSeleccionarHora);

        btnIncreaseStart.setOnClickListener(v -> viewModel.increaseStartTime());
        btnDecreaseStart.setOnClickListener(v -> viewModel.decreaseStartTime());
        btnIncreaseEnd.setOnClickListener(v -> viewModel.increaseEndTime());
        btnDecreaseEnd.setOnClickListener(v -> viewModel.decreaseEndTime());

        btnSeleccionarHora.setOnClickListener(v -> {
            if (Boolean.TRUE.equals(viewModel.isTimeValid().getValue())) {
                viewModel.confirmarHorario();
                Toast.makeText(getContext(), "Horario seleccionado", Toast.LENGTH_SHORT).show();
                navigateToReservarPlazaFragment();
            } else {
                Toast.makeText(getContext(), "Selecciona una fecha y un horario", Toast.LENGTH_SHORT).show();
            }
        });

        calendarView.state().edit()
                .setMinimumDate(CalendarDay.today())
                .commit();

        calendarView.addDecorator(new DisabledDaysDecorator(requireContext()));

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            viewModel.onDateSelected(date.getYear(), date.getMonth(), date.getDay());
        });

        viewModel.getHorarioUIState().observe(getViewLifecycleOwner(), state -> {
            if (state != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                if (state.startTime != null && state.endTime != null) {
                    tvTimeStart.setText(state.startTime.format(formatter));
                    tvTimeEnd.setText(state.endTime.format(formatter));
                    btnIncreaseEnd.setEnabled(state.canIncreaseEnd);
                    btnDecreaseStart.setEnabled(state.canDecreaseStart);
                }

                btnSeleccionarHora.setEnabled(state.isValid);
                btnSeleccionarHora.setBackgroundColor(
                        getResources().getColor(state.isValid ? R.color.Moradito : R.color.NotMoradito)
                );

                btnDecreaseStart.setEnabled(state.canDecreaseStart);
                btnDecreaseEnd.setEnabled(state.canDecreaseEnd);
            }
        });

        viewModel.setupInitialTime(zoneId);

        return view;
    }

    private void navigateToReservarPlazaFragment() {
        ReservarPlazaFragment reservarPlazaFragment = new ReservarPlazaFragment();

        FragmentManager fragmentManager = getParentFragmentManager(); // O requireActivity().getSupportFragmentManager()
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        fragmentTransaction.replace(R.id.fragmentContainerView, reservarPlazaFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}