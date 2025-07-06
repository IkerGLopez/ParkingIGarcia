package com.lksnext.parkingplantilla.model.data;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.lksnext.parkingplantilla.model.domain.Callback;
import com.lksnext.parkingplantilla.model.domain.CallbackBoolean;
import com.lksnext.parkingplantilla.model.domain.Plaza;
import com.lksnext.parkingplantilla.model.domain.Reserva;
import com.lksnext.parkingplantilla.model.domain.Usuario;
import com.lksnext.parkingplantilla.model.domain.Vehiculo;
import com.lksnext.parkingplantilla.model.listener.OnReservasObtenidasListener;

import java.util.List;
import java.util.function.Consumer;

public interface FirebaseService {

    void checkEmailExists(String email, CallbackBoolean callback);
    void registerUser(String email, String password, Callback callback);
    void saveUserData(String uid, String email, String qrCode, Callback callback);
    void addReserva(Reserva reserva, Callback callback);
    void login(String email, String password, Callback callback);
    void sendPasswordResetEmail(String email, Callback callback);
    void eliminarReserva(String docId, Callback callback);
    void obtenerReservasUsuario(OnReservasObtenidasListener listener);
    Task<Vehiculo> getVehiculoForCurrentUser();
    Task<Void> saveVehiculoForCurrentUser(Vehiculo vehiculo);
    String getCurrentUserId();
    String getCurrentUserEmail();
    void signOut();
    void obtenerPlazas(Consumer<List<Plaza>> callback);
}
