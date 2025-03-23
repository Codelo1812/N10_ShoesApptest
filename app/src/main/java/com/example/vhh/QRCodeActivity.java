package com.example.vhh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QRCodeActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 100;
    private EditText editTextInput;
    private ImageView qrCodeImageView;
    private Button btnGenerate, btnSave;
    private Bitmap qrBitmap;

    private ImageButton imbBackqr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        editTextInput = findViewById(R.id.edtinputqr);
        qrCodeImageView = findViewById(R.id.imgqr);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnSave = findViewById(R.id.btnSave);
        imbBackqr = findViewById(R.id.imbBackqr);

        imbBackqr.setOnClickListener(v -> {
            finish();
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextInput.getText().toString().trim();
                if (!text.isEmpty()) {
                    qrBitmap = generateQRCode(text);
                    if (qrBitmap != null) {
                        qrCodeImageView.setImageBitmap(qrBitmap);
                    }
                } else {
                    Toast.makeText(QRCodeActivity.this, "Please write your content", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qrBitmap != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        saveImageToGallery();
                    } else {
                        if (checkPermission()) {
                            saveImageToGalleryLegacy();
                        } else {
                            requestPermission();
                        }
                    }
                } else {
                    Toast.makeText(QRCodeActivity.this, "Please create your QR ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private Bitmap generateQRCode(String text) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {

            BitMatrix bitMatrix = multiFormatWriter.encode(text,
                    BarcodeFormat.QR_CODE,
                    500, 500);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    private void saveImageToGallery() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "QR_" + timestamp + ".jpg";

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "QRCodes");

        Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        try {
            OutputStream outputStream = getContentResolver().openOutputStream(imageUri);
            qrBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error QR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void saveImageToGalleryLegacy() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "QR_" + timestamp + ".jpg";

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/QRCodes");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File file = new File(storageDir, fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            qrBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();


            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error QR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImageToGalleryLegacy();
            } else {
                Toast.makeText(this, "Need storage:", Toast.LENGTH_SHORT).show();
            }
        }
    }
}