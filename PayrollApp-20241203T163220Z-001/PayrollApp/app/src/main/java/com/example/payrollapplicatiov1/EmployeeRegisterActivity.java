package com.example.payrollapplicatiov1;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EmployeeRegisterActivity extends AppCompatActivity {

    private EditText etFullName, etUsername, etPassword, etConfirmPassword;
    private Button btnRegister, btnSelectPhoto;
    private ImageView imageViewProfilePic;
    private DatabaseHelper dbHelper;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String> pickImageLauncher;
    private String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_register);

        dbHelper = new DatabaseHelper(this);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openImagePicker();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Image Picker Launcher
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageViewProfilePic.setImageURI(uri);
                        saveImageToAppFolder(uri);
                    }
                }
        );

        btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
        imageViewProfilePic = findViewById(R.id.imageViewProfilePic);
        etFullName = findViewById(R.id.etFullName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionAndOpenPicker();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = etFullName.getText().toString().trim();
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (imagePath.isEmpty()) {
                    Toast.makeText(EmployeeRegisterActivity.this, "Please select a profile picture", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(EmployeeRegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (password.equals(confirmPassword)) {
                    long result = dbHelper.addUser(fullName, username, password,imagePath);
                    if (result > 0) {
                        Toast.makeText(EmployeeRegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to login screen
                    } else {
                        Toast.makeText(EmployeeRegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EmployeeRegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveImageToAppFolder(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            // Define app-specific directory for images
            File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "AppImages");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create a unique file name for the image
            String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
            File file = new File(directory, fileName);

            // Save the bitmap to the file
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Store the path in SQLite database
            imagePath = file.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }
    // Method to check permission and open image picker
    private void checkPermissionAndOpenPicker() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    // Method to open the image picker
    private void openImagePicker() {
        pickImageLauncher.launch("image/*");
    }
}


