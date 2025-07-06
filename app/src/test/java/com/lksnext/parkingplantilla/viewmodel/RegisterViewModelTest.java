package com.lksnext.parkingplantilla.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.lksnext.parkingplantilla.LiveDataTestUtil;
import com.lksnext.parkingplantilla.model.data.FirebaseService;
import com.lksnext.parkingplantilla.model.domain.Callback;
import com.lksnext.parkingplantilla.model.domain.CallbackBoolean;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RegisterViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    FirebaseService firebaseService;

    RegisterViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new RegisterViewModel(firebaseService);
    }

    @Test
    public void registrarUsuario_emptyEmail_setsErrorAndRegisteredFalse() throws InterruptedException {
        viewModel.registrarUsuario("", "password", "password");

        String error = LiveDataTestUtil.getValue(viewModel.getErrorMessage());
        Boolean registered = LiveDataTestUtil.getValue(viewModel.isRegistered());

        assertNotNull(error);
        assertFalse(registered);
    }

    @Test
    public void registrarUsuario_emptyPassword_setsErrorAndRegisteredFalse() throws InterruptedException {
        viewModel.registrarUsuario("test@example.com", "", "");

        String error = LiveDataTestUtil.getValue(viewModel.getErrorMessage());
        Boolean registered = LiveDataTestUtil.getValue(viewModel.isRegistered());

        assertNotNull(error);
        assertFalse(registered);
    }

    @Test
    public void registrarUsuario_passwordsDoNotMatch_setsErrorAndRegisteredFalse() throws InterruptedException {
        viewModel.registrarUsuario("test@example.com", "password1", "password2");

        String error = LiveDataTestUtil.getValue(viewModel.getErrorMessage());
        Boolean registered = LiveDataTestUtil.getValue(viewModel.isRegistered());

        assertNotNull(error);
        assertFalse(registered);
    }

    @Test
    public void registrarUsuario_emailAlreadyExists_setsErrorAndRegisteredFalse() throws InterruptedException {
        ArgumentCaptor<CallbackBoolean> callbackCaptor = ArgumentCaptor.forClass(CallbackBoolean.class);

        viewModel.registrarUsuario("test@example.com", "password", "password");

        verify(firebaseService).checkEmailExists(eq("test@example.com"), callbackCaptor.capture());

        // Simular que el correo ya existe
        callbackCaptor.getValue().onComplete(true);

        String error = LiveDataTestUtil.getValue(viewModel.getErrorMessage());
        Boolean registered = LiveDataTestUtil.getValue(viewModel.isRegistered());

        assertEquals("El correo ya está registrado.", error);
        assertFalse(registered);
    }

    @Test
    public void registrarUsuario_validData_onSuccess_setsRegisteredTrue() throws InterruptedException {
        // Simular que el correo no existe
        ArgumentCaptor<CallbackBoolean> emailCallbackCaptor = ArgumentCaptor.forClass(CallbackBoolean.class);
        ArgumentCaptor<Callback> registerCallbackCaptor = ArgumentCaptor.forClass(Callback.class);
        ArgumentCaptor<Callback> saveUserDataCaptor = ArgumentCaptor.forClass(Callback.class);

        viewModel.registrarUsuario("test@example.com", "password", "password");

        // Simular respuesta de checkEmailExists
        verify(firebaseService).checkEmailExists(eq("test@example.com"), emailCallbackCaptor.capture());
        emailCallbackCaptor.getValue().onComplete(false);

        // Simular respuesta de registerUser
        verify(firebaseService).registerUser(eq("test@example.com"), eq("password"), registerCallbackCaptor.capture());
        registerCallbackCaptor.getValue().onSuccess();

        // Simular respuesta de saveUserData
        verify(firebaseService).saveUserData(anyString(), eq("test@example.com"), anyString(), saveUserDataCaptor.capture());
        saveUserDataCaptor.getValue().onSuccess();

        // Verificar resultados
        Boolean registered = LiveDataTestUtil.getValue(viewModel.isRegistered());
        String error = LiveDataTestUtil.getValue(viewModel.getErrorMessage());

        assertTrue(registered);
        assertNull(error);
    }


    @Test
    public void registrarUsuario_validData_onFailure_setsErrorAndRegisteredFalse() throws InterruptedException {
        ArgumentCaptor<CallbackBoolean> emailCallbackCaptor = ArgumentCaptor.forClass(CallbackBoolean.class);
        ArgumentCaptor<Callback> registerCallbackCaptor = ArgumentCaptor.forClass(Callback.class);

        viewModel.registrarUsuario("test@example.com", "password", "password");

        verify(firebaseService).checkEmailExists(eq("test@example.com"), emailCallbackCaptor.capture());

        // Simular que el correo no existe
        emailCallbackCaptor.getValue().onComplete(false);

        verify(firebaseService).registerUser(eq("test@example.com"), eq("password"), registerCallbackCaptor.capture());

        // Simular fallo en el registro
        registerCallbackCaptor.getValue().onFailure();

        Boolean registered = LiveDataTestUtil.getValue(viewModel.isRegistered());
        String error = LiveDataTestUtil.getValue(viewModel.getErrorMessage());

        assertFalse(registered);
        assertEquals("Error al crear la cuenta. Verifica los datos.", error);
    }
}