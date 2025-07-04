package com.lksnext.parkingplantilla.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.lksnext.parkingplantilla.R;
import com.lksnext.parkingplantilla.model.utils.QRGenerator;
import com.lksnext.parkingplantilla.viewmodel.MainViewModel;

public class QRFragment extends Fragment {

    private static final String TAG = "QRFragment";

    // ViewModel compartido
    private MainViewModel mainViewModel;

    // Views
    private TextView textQR;
    private ImageView imageViewQrCode;

    public QRFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Usar el MainViewModel compartido
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        generateAndDisplayQR();
    }

    private void initViews(View view) {
        textQR = view.findViewById(R.id.textQR);
        imageViewQrCode = view.findViewById(R.id.imageViewQrCode);
    }

    private void generateAndDisplayQR() {
        // Generar datos del usuario para el QR
        String qrData = generateUserQRData();

        if (qrData != null && !qrData.isEmpty()) {
            try {
                // Generar el bitmap del QR
                Bitmap qrBitmap = QRGenerator.generateQRCodeBitmap(qrData, 280, 280);

                // Mostrar el QR
                imageViewQrCode.setImageBitmap(qrBitmap);
                imageViewQrCode.setVisibility(View.VISIBLE);

                Log.d(TAG, "QR generado exitosamente: " + qrData);

            } catch (WriterException e) {
                Log.e(TAG, "Error generando QR: " + e.getMessage());
                showError("Error al generar el código QR");
            }
        } else {
            showError("No se pudo obtener la información del usuario");
        }
    }

    private String generateUserQRData() {
        // QR único que no cambia para cada usuario
        String userId = getUserId();
        String userEmail = getUserEmail();

        if (userId == null || userId.isEmpty()) {
            return null;
        }

        // Formato JSON simple para el QR estático
        return String.format("{\"userId\":\"%s\",\"email\":\"%s\",\"type\":\"parking\",\"version\":\"1.0\"}",
                userId, userEmail != null ? userEmail : "");
    }

    private String getUserId() {
        // Obtener ID del usuario desde SharedPreferences
        if (getContext() != null) {
            return getContext().getSharedPreferences("user_prefs", 0)
                    .getString("user_id", null);
        }
        return null;
    }

    private String getUserEmail() {
        // Obtener email del usuario desde SharedPreferences
        if (getContext() != null) {
            return getContext().getSharedPreferences("user_prefs", 0)
                    .getString("user_email", null);
        }
        return null;
    }

    private void showError(String message) {
        imageViewQrCode.setVisibility(View.GONE);

        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

        Log.e(TAG, "Error: " + message);
    }

    @Override
    public void onResume() {
        super.onResume();
        generateAndDisplayQR();
    }
}