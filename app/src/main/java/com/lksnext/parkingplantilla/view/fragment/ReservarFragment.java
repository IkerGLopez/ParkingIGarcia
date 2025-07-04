package com.lksnext.parkingplantilla.view.fragment;

import android.graphics.Color;
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
import com.lksnext.parkingplantilla.model.utils.DisabledPastDaysDecorator;
import com.lksnext.parkingplantilla.model.utils.HourValidator;
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;


public class ReservarFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private TextView tvTimeStart, tvTimeEnd;
    private Button btnIncreaseStart, btnDecreaseStart, btnIncreaseEnd, btnDecreaseEnd, btnSeleccionarHora;

    private LocalTime selectedStartTime;
    private LocalTime selectedEndTime;
    private LocalDate selectedDate;

    private boolean isDateSelected = false;
    private boolean isTimeSelected = false;
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

        viewModel.setupInitialTime(zoneId);
        setupListeners();

        checkEnableButton();

        calendarView.state().edit()
                .setMinimumDate(CalendarDay.today())
                .commit();

        calendarView.addDecorator(new DisabledPastDaysDecorator(requireContext()));


        btnSeleccionarHora.setEnabled(false);
        btnSeleccionarHora.setBackgroundColor(getResources().getColor(R.color.NotMoradito));

        return view;
    }

//    private void setupInitialTime() {
//        LocalTime now = LocalTime.now(zoneId).truncatedTo(ChronoUnit.MINUTES);
//        int minute = now.getMinute();
//
//        if (minute == 0) {
//            selectedStartTime = now;
//        } else if (minute <= 30) {
//            selectedStartTime = now.withMinute(30);
//        } else {
//            selectedStartTime = now.plusHours(1).withMinute(0);
//        }
//
//        selectedEndTime = selectedStartTime.plusMinutes(30);
//        updateTimeDisplay();
//    }



    private void setupListeners() {
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            selectedDate = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
            isDateSelected = true;
            checkEnableButton();
        });

        btnIncreaseStart.setOnClickListener(v -> {
            selectedStartTime = selectedStartTime.plusMinutes(30);
            if (selectedStartTime.isAfter(selectedEndTime.minusMinutes(30))) {
                selectedEndTime = selectedStartTime.plusMinutes(30);
            }
            isTimeSelected = true;
            updateTimeDisplay();
            checkEnableButton();
        });

        btnDecreaseStart.setOnClickListener(v -> {
            selectedStartTime = selectedStartTime.minusMinutes(30);
            if (selectedStartTime.isAfter(selectedEndTime.minusMinutes(30))) {
                selectedEndTime = selectedStartTime.plusMinutes(30);
            }
            isTimeSelected = true;
            updateTimeDisplay();
            checkEnableButton();
        });

        btnIncreaseEnd.setOnClickListener(v -> {
            selectedEndTime = selectedEndTime.plusMinutes(30);
            isTimeSelected = true;
            updateTimeDisplay();
            checkEnableButton();
        });

        btnDecreaseEnd.setOnClickListener(v -> {
            if (selectedEndTime.isAfter(selectedStartTime.plusMinutes(30))) {
                selectedEndTime = selectedEndTime.minusMinutes(30);
            }
            isTimeSelected = true;
            updateTimeDisplay();
            checkEnableButton();
        });

        btnSeleccionarHora.setOnClickListener(v -> {
            if (selectedDate != null && selectedStartTime != null && selectedEndTime != null) {
                viewModel.setReservaData(selectedDate, selectedStartTime, selectedEndTime);
                Toast.makeText(getContext(), "Horario seleccionado", Toast.LENGTH_SHORT).show();
                navigateToReservarPlazaFragment();
            } else {
                Toast.makeText(getContext(), "Selecciona una fecha y un horario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTimeDisplay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        tvTimeStart.setText(selectedStartTime.format(formatter));
        tvTimeEnd.setText(selectedEndTime.format(formatter));
    }

    private void checkEnableButton() {
        if (isDateSelected && isTimeSelected) {
            btnSeleccionarHora.setEnabled(true);
            btnSeleccionarHora.setBackgroundColor(getResources().getColor(R.color.purple_500));
        } else {
            btnSeleccionarHora.setEnabled(false);
            btnSeleccionarHora.setBackgroundColor(Color.parseColor("#AAA9BA"));
        }

        btnDecreaseStart.setEnabled(HourValidator.canDecreaseStart(selectedStartTime, zoneId));
        btnDecreaseEnd.setEnabled(HourValidator.canDecreaseEnd(selectedStartTime, selectedEndTime));
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