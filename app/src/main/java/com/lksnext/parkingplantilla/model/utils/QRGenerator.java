package com.lksnext.parkingplantilla.model.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import java.io.FileOutputStream;
import java.io.IOException;

public class QRGenerator {

    /**
     * Genera un QR code como Bitmap
     * @param text Texto a codificar
     * @param width Ancho del QR
     * @param height Alto del QR
     * @return Bitmap del QR generado
     */
    public static Bitmap generateQRCodeBitmap(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        return bitmap;
    }

    /**
     * Genera un QR code con tamaño por defecto
     * @param text Texto a codificar
     * @return Bitmap del QR generado
     */
    public static Bitmap generateQRCodeBitmap(String text) throws WriterException {
        return generateQRCodeBitmap(text, 512, 512);
    }

    /**
     * Método auxiliar para guardar un Bitmap como archivo PNG
     */
    public static void saveBitmapToFile(Bitmap bitmap, String filePath) {
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.d("RegisterActivity", "QR guardado en: " + filePath);
        } catch (IOException e) {
            Log.e("RegisterActivity", "Error guardando QR: " + e.getMessage());
            throw new RuntimeException("Error guardando archivo QR", e);
        }
    }
}