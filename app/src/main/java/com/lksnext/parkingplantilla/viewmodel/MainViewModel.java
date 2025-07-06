// MainViewModel.java
package com.lksnext.parkingplantilla.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.zxing.WriterException;
import com.lksnext.parkingplantilla.model.adapter.ReservaActivaAdapter;
import com.lksnext.parkingplantilla.model.data.FirebaseService;
import com.lksnext.parkingplantilla.model.data.FirebaseServiceImpl;
import com.lksnext.parkingplantilla.model.domain.Callback;
import com.lksnext.parkingplantilla.model.domain.Plaza;
import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.model.domain.Vehiculo;
import com.lksnext.parkingplantilla.model.utils.HorarioUIState;
import com.lksnext.parkingplantilla.model.utils.HourValidator;
import com.lksnext.parkingplantilla.model.utils.QRGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class MainViewModel extends ViewModel {

    private final FirebaseService firebaseService;

    public MainViewModel() {
        firebaseService = new FirebaseServiceImpl();
    }

    public MainViewModel(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    // ================================== RESERVAR ==================================

    // VARIABLES
    private final MutableLiveData<LocalTime> selectedStartTime = new MutableLiveData<>();
    private final MutableLiveData<LocalTime> selectedEndTime = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isTimeValid = new MutableLiveData<>(false);
    private final MutableLiveData<HorarioUIState> horarioUIState = new MutableLiveData<>();
    private final MutableLiveData<Plaza> selectedPlaza = new MutableLiveData<>();
    private final MutableLiveData<Reserva> reservaData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> reservaCompletada = new MutableLiveData<>();

    // GETTERS
    public LiveData<LocalTime> getSelectedStartTime() {
        return selectedStartTime;
    }

    public LiveData<LocalTime> getSelectedEndTime() {
        return selectedEndTime;
    }

    public LiveData<Boolean> isTimeValid() {
        return isTimeValid;
    }

    public LiveData<HorarioUIState> getHorarioUIState() {
        return horarioUIState;
    }

    public LiveData<Plaza> getSelectedPlaza() {
        return selectedPlaza;
    }

    public LiveData<Reserva> getReservaData() {
        return reservaData;
    }

    public LiveData<Boolean> getReservaCompletada() {
        return reservaCompletada;
    }

    public LiveData<LocalDate> getSelectedDate() {
        return selectedDate;
    }


    // SETTER

    public void setSelectedPlaza(Plaza plaza) {
        selectedPlaza.setValue(plaza);
    }

    // MÉTODOS
    public void setupInitialTime(ZoneId zoneId) {
        LocalTime now = LocalTime.now(zoneId).truncatedTo(ChronoUnit.MINUTES);
        int minute = now.getMinute();
        LocalTime startTime;

        if (minute == 0) {
            startTime = now;
        } else if (minute <= 30) {
            startTime = now.withMinute(30);
        } else {
            startTime = now.plusHours(1).withMinute(0);
        }

        LocalTime endTime = startTime.plusMinutes(30);

        selectedStartTime.setValue(startTime);
        selectedEndTime.setValue(endTime);
    }

    public void onDateSelected(int year, int month, int day) {
        selectedDate.setValue(LocalDate.of(year, month + 1, day));
        checkEnableButton();
    }

    public void increaseStartTime() {
        LocalTime start = selectedStartTime.getValue();
        LocalTime end = selectedEndTime.getValue();
        if (start != null && end != null) {
            start = start.plusMinutes(30);
            if (start.isAfter(end.minusMinutes(30))) {
                end = start.plusMinutes(30);
                selectedEndTime.setValue(end);
            }
            selectedStartTime.setValue(start);
            checkEnableButton();
        }
    }

    public void decreaseStartTime() {
        LocalTime start = selectedStartTime.getValue();
        LocalTime end = selectedEndTime.getValue();
        if (start != null && end != null) {
            start = start.minusMinutes(30);
            if (start.isAfter(end.minusMinutes(30))) {
                end = start.plusMinutes(30);
                selectedEndTime.setValue(end);
            }
            selectedStartTime.setValue(start);
            checkEnableButton();
        }
    }

    public void increaseEndTime() {
        LocalTime end = selectedEndTime.getValue();
        if (end != null) {
            selectedEndTime.setValue(end.plusMinutes(30));
            checkEnableButton();
        }
    }

    public void decreaseEndTime() {
        LocalTime start = selectedStartTime.getValue();
        LocalTime end = selectedEndTime.getValue();
        if (start != null && end != null && end.isAfter(start.plusMinutes(30))) {
            selectedEndTime.setValue(end.minusMinutes(30));
            checkEnableButton();
        }
    }

    private void checkEnableButton() {
        LocalTime start = selectedStartTime.getValue();
        LocalTime end = selectedEndTime.getValue();
        LocalDate date = selectedDate.getValue();
        ZoneId zoneId = ZoneId.of("Europe/Madrid");

        boolean isValid = date != null && start != null && end != null
                && HourValidator.isValidRange(start, end, date, zoneId);

        horarioUIState.setValue(new HorarioUIState(
                start,
                end,
                HourValidator.canDecreaseStart(start, date, zoneId),
                HourValidator.canDecreaseEnd(start, end),
                HourValidator.canIncreaseEnd(start, end),
                isValid
        ));

        isTimeValid.setValue(isValid);
    }

    public void confirmarHorario() {
        if (isTimeValid.getValue() != null && isTimeValid.getValue()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalDate fecha = selectedDate.getValue();
            LocalTime horaInicio = selectedStartTime.getValue();
            LocalTime horaFin = selectedEndTime.getValue();

            Reserva reserva = new Reserva();
            reserva.setFecha(fecha != null ? fecha.toString() : null);
            reserva.setHoraInicio(horaInicio != null ? horaInicio.format(formatter) : null);
            reserva.setHoraFin(horaFin != null ? horaFin.format(formatter) : null);

            reservaData.setValue(reserva);
        }
    }

    public void reservarPlaza() {
        Reserva reserva = reservaData.getValue();
        Plaza plaza = selectedPlaza.getValue();

        if (reserva != null && plaza != null) {
            reserva.setPlaza(plaza);

            firebaseService.addReserva(reserva, new Callback() {
                @Override
                public void onSuccess() {
                    // Marcar plaza como ocupada
                    Plaza p = reserva.getPlaza();
                    if (p != null) p.setEstado(Plaza.Estado.OCUPADA);

                    reservaCompletada.postValue(true);
                }

                @Override
                public void onFailure() {
                    reservaCompletada.postValue(false);
                }
            });

        } else {
            reservaCompletada.postValue(false);
        }
    }

    public void resetReservaCompletada() {
        reservaCompletada.setValue(false);
    }

    // ================================== RESERVAR ==================================


    // ================================== EDITAR ==================================

    // VARIABLES
    private final MutableLiveData<List<ReservaActivaAdapter.ReservaConId>> reservasActivas = new MutableLiveData<>();

    // GETTERS
    public LiveData<List<ReservaActivaAdapter.ReservaConId>> getReservasActivas() {
        return reservasActivas;
    }

    // MÉTODOS
    public void cargarReservasActivas() {
        firebaseService.obtenerReservasUsuario(reservasMap -> {
            if (reservasMap != null) {
                List<ReservaActivaAdapter.ReservaConId> lista = new ArrayList<>();

                long ahora = System.currentTimeMillis();

                for (Map.Entry<String, Reserva> entry : reservasMap.entrySet()) {
                    Reserva reserva = entry.getValue();
                    String docId = entry.getKey();

                    if (reserva != null && docId != null) {
                        Long finEnMillis = getMillisDesdeFechaYHora(reserva.getFecha(), reserva.getHoraFin());
                        Log.d("MainViewModel", "Reserva: " + reserva + ", finEnMillis: " + finEnMillis + ", ahora: " + ahora);
                        if (finEnMillis != null && finEnMillis > ahora) {
                            lista.add(new ReservaActivaAdapter.ReservaConId(reserva, docId));
                        }
                    }
                }

                reservasActivas.postValue(lista);
            } else {
                reservasActivas.postValue(Collections.emptyList());
            }
        });
    }

    public void cancelarReserva(String docId, Callback callback) {
        firebaseService.eliminarReserva(docId, new Callback() {
            @Override
            public void onSuccess() {
                List<ReservaActivaAdapter.ReservaConId> listaActual = reservasActivas.getValue();
                if (listaActual != null) {
                    List<ReservaActivaAdapter.ReservaConId> nuevaLista = new ArrayList<>(listaActual);
                    nuevaLista.removeIf(item -> item.getDocId().equals(docId));
                    reservasActivas.postValue(nuevaLista);
                }
                callback.onSuccess();
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    private Long getMillisDesdeFechaYHora(String fecha, String hora) {
        if (fecha == null || hora == null) return null;
        try {
            String fechaHora = fecha + " " + hora;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date date = sdf.parse(fechaHora);
            if (date == null) {
                Log.e("MainViewModel", "No se pudo parsear: " + fechaHora);
            }
            return (date != null) ? date.getTime() : null;
        } catch (ParseException e) {
            Log.e("MainViewModel", "Error parseando fecha/hora: " + fecha + " " + hora, e);
            return null;
        }
    }


    // ================================== EDITAR ==================================


    // ================================== QR ==================================

    // VARIABLES
    private final MutableLiveData<Bitmap> qrBitmap = new MutableLiveData<>();

    // GETTERS
    public LiveData<Bitmap> getQrBitmap() {
        return qrBitmap;
    }

    // MÉTODOS
    public void generarQR(Context context) {
        try {
            String userId = getUserIdFromPrefs(context);
            String userEmail = getUserEmailFromPrefs(context);

            if (userId == null || userId.isEmpty()) {
                qrBitmap.postValue(null);
                return;
            }

            String qrData = String.format(
                    "{\"userId\":\"%s\",\"email\":\"%s\",\"type\":\"parking\",\"version\":\"1.0\"}",
                    userId, userEmail != null ? userEmail : "");

            Bitmap bitmap = QRGenerator.generateQRCodeBitmap(qrData, 280, 280);
            qrBitmap.postValue(bitmap);

        } catch (WriterException e) {
            Log.e("MainViewModel", "Error generando QR: " + e.getMessage());
            qrBitmap.postValue(null);
        }
    }

    private String getUserIdFromPrefs(Context context) {
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("user_id", null);
    }

    private String getUserEmailFromPrefs(Context context) {
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("user_email", null);
    }

    public void cerrarSesion() {
        firebaseService.signOut();
    }


    // ================================== QR ==================================


    // ================================== HISTORIAL ==================================

    // VARIABLES
    private final MutableLiveData<List<Reserva>> reservasPasadas = new MutableLiveData<>();

    // GETTERS
    public LiveData<List<Reserva>> getReservasPasadas() {
        return reservasPasadas;
    }

    // MÉTODOS
    public void cargarReservasPasadas() {
        firebaseService.obtenerReservasUsuario(reservasMap -> {
            List<Reserva> pasadas = new ArrayList<>();

            long ahora = System.currentTimeMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

            for (Reserva reserva : reservasMap.values()) {
                try {
                    String fecha = reserva.getFecha();
                    String horaFin = reserva.getHoraFin();

                    if (fecha != null && horaFin != null) {
                        Date fechaHoraFin = sdf.parse(fecha + " " + horaFin);
                        if (fechaHoraFin != null && fechaHoraFin.getTime() < ahora) {
                            pasadas.add(reserva);
                        }
                    }
                } catch (Exception e) {
                    Log.e("MainViewModel", "Reserva inválida o error al parsear fecha/hora", e);
                }
            }

            // Ordenar por fecha descendente
            Collections.sort(pasadas, (r1, r2) -> {
                try {
                    String fecha1 = r1.getFecha();
                    String horaFin1 = r1.getHoraFin();
                    String fecha2 = r2.getFecha();
                    String horaFin2 = r2.getHoraFin();

                    if (fecha1 == null || horaFin1 == null || fecha2 == null || horaFin2 == null) {
                        return 0;
                    }

                    Date d1 = sdf.parse(fecha1 + " " + horaFin1);
                    Date d2 = sdf.parse(fecha2 + " " + horaFin2);

                    if (d1 == null || d2 == null) return 0;

                    return d2.compareTo(d1);
                } catch (ParseException e) {
                    return 0;
                }
            });


            reservasPasadas.postValue(pasadas);
        });
    }

    // ================================== HISTORIAL ==================================

    // ================================== CONFIG ==================================

    // VARIABLES
    private final MutableLiveData<Vehiculo> vehiculoLiveData = new MutableLiveData<>();

    // GETTERS
    public LiveData<Vehiculo> getVehiculoLiveData() {
        return vehiculoLiveData;
    }

    // MÉTODOS
    public void cargarDatosVehiculo() {
        firebaseService.getVehiculoForCurrentUser()
                .addOnSuccessListener(vehiculo -> {
                    vehiculoLiveData.setValue(vehiculo != null ? vehiculo : new Vehiculo());
                })
                .addOnFailureListener(e -> {
                    // Puedes mostrar error desde el fragment con otro LiveData si quieres
                });
    }

    public void guardarVehiculo(Vehiculo vehiculo, boolean isNewUser, Runnable onSuccess, Consumer<String> onError) {
        firebaseService.saveVehiculoForCurrentUser(vehiculo)
                .addOnSuccessListener(unused -> {
                    vehiculoLiveData.setValue(vehiculo);
                    onSuccess.run();
                })
                .addOnFailureListener(e -> onError.accept("Error guardando vehículo"));
    }

    // ================================== CONFIG ==================================

}