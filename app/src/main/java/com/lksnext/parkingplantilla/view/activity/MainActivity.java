// MainActivity.java
package com.lksnext.parkingplantilla.view.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.lksnext.parkingplantilla.R;
import com.lksnext.parkingplantilla.view.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Solo carga el fragment si no hay uno ya presente
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new MainFragment())
                    .commit();
        }
    }
}
