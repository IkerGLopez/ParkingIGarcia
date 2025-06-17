package com.lksnext.parkingplantilla.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingplantilla.data.DataRepository;
import com.lksnext.parkingplantilla.databinding.ActivityLoginBinding;
import com.lksnext.parkingplantilla.domain.Callback;
import com.lksnext.parkingplantilla.utils.InputValidator;
import com.lksnext.parkingplantilla.utils.ValidationResult;

public class LoginViewModel extends ViewModel {

    // Aquí puedes declarar los LiveData y métodos necesarios para la vista de inicio de sesión
    MutableLiveData<Boolean> logged = new MutableLiveData<>(null);
    MutableLiveData<String> errorMessage = new MutableLiveData<>(null);

    public LiveData<Boolean> isLogged() {
        return logged;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loginUser(String email, String password) {
        ValidationResult emailValidation = InputValidator.validateNotEmpty(email);
        if (!emailValidation.isSuccess()) {
            errorMessage.setValue(emailValidation.getErrorMessage());
            logged.setValue(Boolean.FALSE);
            return;
        }

        ValidationResult passValidation = InputValidator.validateNotEmpty(password);
        if (!emailValidation.isSuccess()) {
            errorMessage.setValue(passValidation.getErrorMessage());
            logged.setValue(Boolean.FALSE);
            return;
        }

//        Clase para comprobar si los datos de inicio de sesión son correctos o no
        DataRepository.getInstance().login(email, password, new Callback() {
            //En caso de que el login sea correcto, que se hace
            @Override
            public void onSuccess() {
                //TODO
                logged.setValue(Boolean.TRUE);
            }

            //En caso de que el login sea incorrecto, que se hace
            @Override
            public void onFailure() {
                //TODO
                logged.setValue(Boolean.FALSE);
            }
        });




//        logged.setValue(Boolean.TRUE);
    }
}
