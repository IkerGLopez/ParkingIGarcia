package com.lksnext.parkingplantilla.data;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingplantilla.domain.Callback;
import com.lksnext.parkingplantilla.domain.Parking;
import com.lksnext.parkingplantilla.domain.Reserva;
import com.lksnext.parkingplantilla.domain.Usuario;
import com.lksnext.parkingplantilla.domain.Vehiculo;

import java.util.List;

public interface FirebaseService {

    void registerUser(Usuario usuario, Callback callback);

    void registerUser(Usuario usuario, Callback callback);

    void addReserva(Reserva reserva);
    void storeParking(Parking parking);
    void login(Usuario usuario);

}
