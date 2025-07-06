// MainActivity.java
package com.lksnext.parkingplantilla.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lksnext.parkingplantilla.R;
import com.lksnext.parkingplantilla.view.fragment.ConfigFragment;
import com.lksnext.parkingplantilla.view.fragment.EditarFragment;
import com.lksnext.parkingplantilla.view.fragment.HistorialFragment;
import com.lksnext.parkingplantilla.view.fragment.QRFragment;
import com.lksnext.parkingplantilla.view.fragment.ReservarFragment;
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private BottomNavigationView bottomNavigationView;
    private boolean isNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Inicializar BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);


        // Comprobar si es usuario nuevo (pasado en el Intent)
        isNewUser = getIntent().getBooleanExtra("IS_NEW_USER", false);
        Log.d("MainActivity", "isNewUser recibido: " + isNewUser);
        getIntent().removeExtra("IS_NEW_USER");

        if (savedInstanceState == null) {
            if (isNewUser) {
                ConfigFragment configFragment = new ConfigFragment();
                Bundle args = new Bundle();
                args.putBoolean("isNewUser", true);
                configFragment.setArguments(args);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView, configFragment)
                        .commit();
            } else {
                // Cargar QRFragment como fragmento inicial
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView, new QRFragment())
                        .commit();
            }
        }

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_qr) {
                selectedFragment = new QRFragment();
            } else if (itemId == R.id.nav_reservar) {
                selectedFragment = new ReservarFragment();
            } else if (itemId == R.id.nav_editar) {
                selectedFragment = new EditarFragment();
            } else if (itemId == R.id.nav_historial) {
                selectedFragment = new HistorialFragment();
            } else if (itemId == R.id.nav_config) {
                selectedFragment = new ConfigFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        // Establecer ítem seleccionado por defecto solo si no es usuario nuevo
        if (!isNewUser) {
            bottomNavigationView.setSelectedItemId(R.id.nav_qr);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.nav_config);
            Toast.makeText(this, "Introduce los datos de tu vehículo.", Toast.LENGTH_SHORT).show();
        }
    }
}
