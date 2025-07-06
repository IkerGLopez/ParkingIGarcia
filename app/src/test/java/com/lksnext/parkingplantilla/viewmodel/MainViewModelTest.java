package com.lksnext.parkingplantilla.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingplantilla.LiveDataTestUtil;
import com.lksnext.parkingplantilla.model.data.FirebaseServiceImpl;
import com.lksnext.parkingplantilla.model.domain.Plaza;
import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.model.listener.OnReservasObtenidasListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;

public class MainViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    Plaza mockPlaza;

    MainViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        FirebaseServiceImpl mockService = mock(FirebaseServiceImpl.class);
        viewModel = new MainViewModel(mockService);

        // Simula el comportamiento de obtenerReservasUsuario
        doAnswer(invocation -> {
            OnReservasObtenidasListener listener = invocation.getArgument(0);
            listener.onComplete(Collections.emptyMap());
            return null;
        }).when(mockService).obtenerReservasUsuario(any());
    }

    @Test
    public void testCargarReservasActivas() throws InterruptedException {
        viewModel.cargarReservasActivas();
        assertEquals(Collections.emptyList(), LiveDataTestUtil.getValue(viewModel.getReservasActivas()));
    }

    @Test
    public void testSetSelectedPlaza() throws InterruptedException {
        viewModel.setSelectedPlaza(mockPlaza);
        Plaza result = LiveDataTestUtil.getValue(viewModel.getSelectedPlaza());
        assertEquals(mockPlaza, result);
    }

    @Test
    public void testOnDateSelected() throws InterruptedException {
        // Configura los horarios iniciales
        viewModel.setupInitialTime(ZoneId.of("Europe/Madrid"));
        // Llama al método para seleccionar una fecha
        viewModel.onDateSelected(2024, 5, 1);

        // Verifica que la fecha seleccionada sea la esperada
        LocalDate result = LiveDataTestUtil.getValue(viewModel.getSelectedDate());
        assertEquals(LocalDate.of(2024, 6, 1), result); //Porque el calendario empieza a contar desde 0
    }

    @Test
    public void testReservarPlaza() throws InterruptedException {
        Reserva reserva = new Reserva(LocalDate.of(2024, 6, 1), LocalTime.of(10, 0), LocalTime.of(11, 0));
        viewModel.setSelectedPlaza(mockPlaza);
        viewModel.confirmarHorario();
        viewModel.reservarPlaza();
        Boolean result = LiveDataTestUtil.getValue(viewModel.getReservaCompletada());
        assertFalse(result); // Simula que no se completó porque no hay FirebaseService mockeado.
    }

    @Test
    public void testResetReservaCompletada() throws InterruptedException {
        viewModel.resetReservaCompletada();
        Boolean result = LiveDataTestUtil.getValue(viewModel.getReservaCompletada());
        assertFalse(result);
    }
}