package com.lksnext.parkingplantilla.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.lksnext.parkingplantilla.LiveDataTestUtil;
import com.lksnext.parkingplantilla.model.data.FirebaseService;
import com.lksnext.parkingplantilla.model.domain.Callback;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LoginViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    FirebaseService firebaseService;

    LoginViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new LoginViewModel(firebaseService);
    }

    @Test
    public void loginUser_emptyEmail_setsErrorAndLoggedFalse() throws InterruptedException {
        viewModel.loginUser("", "password");

        String error = LiveDataTestUtil.getValue(viewModel.getErrorMessage());
        Boolean logged = LiveDataTestUtil.getValue(viewModel.isLogged());

        assertNotNull(error);
        assertFalse(logged);
    }

    @Test
    public void loginUser_emptyPassword_setsErrorAndLoggedFalse() throws InterruptedException {
        viewModel.loginUser("test@example.com", "");

        String error = LiveDataTestUtil.getValue(viewModel.getErrorMessage());
        Boolean logged = LiveDataTestUtil.getValue(viewModel.isLogged());

        assertNotNull(error);
        assertFalse(logged);
    }

    @Test
    public void loginUser_validCredentials_onSuccess_setsLoggedTrue() throws InterruptedException {
        // Preparar captor para el callback
        ArgumentCaptor<Callback> callbackCaptor = ArgumentCaptor.forClass(Callback.class);

        viewModel.loginUser("test@example.com", "password");

        // Verificar que firebaseService.login fue llamado y capturar callback
        verify(firebaseService).login(eq("test@example.com"), eq("password"), callbackCaptor.capture());

        // Simular éxito
        callbackCaptor.getValue().onSuccess();

        Boolean logged = LiveDataTestUtil.getValue(viewModel.isLogged());
        String error = LiveDataTestUtil.getValue(viewModel.getErrorMessage());

        assertTrue(logged);
        assertNull(error);
    }


    @Test
    public void loginUser_validCredentials_onFailure_setsLoggedFalseAndError() throws InterruptedException {
        ArgumentCaptor<Callback> callbackCaptor = ArgumentCaptor.forClass(Callback.class);

        viewModel.loginUser("test@example.com", "password");

        verify(firebaseService).login(eq("test@example.com"), eq("password"), callbackCaptor.capture());

        // Simular fallo
        callbackCaptor.getValue().onFailure();

        Boolean logged = LiveDataTestUtil.getValue(viewModel.isLogged());
        String error = LiveDataTestUtil.getValue(viewModel.getErrorMessage());

        assertFalse(logged);
        assertEquals("Error de autenticación. Verifica tu correo y contraseña.", error);
    }
}