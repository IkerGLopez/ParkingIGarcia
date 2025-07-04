package com.lksnext.parkingplantilla.model.data;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.lksnext.parkingplantilla.model.domain.Callback;
import com.lksnext.parkingplantilla.model.domain.CallbackBoolean;
import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.model.domain.Usuario;
import com.lksnext.parkingplantilla.model.listener.OnReservasObtenidasListener;

public interface FirebaseService {

    void registerUser(Usuario usuario, Callback callback);
    void checkEmailExists(String email, CallbackBoolean callback);
    void addReserva(Reserva reserva);
    void login(Usuario usuario);
    void eliminarReserva(String docId, Callback callback);
    void obtenerReservasUsuario(OnReservasObtenidasListener listener);
}
