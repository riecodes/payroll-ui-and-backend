package com.example.payrollapplicatiov1;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Optional: Handle button clicks (to be customized as needed)
        Button employeeLoginButton = findViewById(R.id.btn_employee_login);
        Button employerLoginButton = findViewById(R.id.btn_employer_login);

        employeeLoginButton.setOnClickListener(v -> {
            // Navigate to EmployeeLoginActivity
            Intent intent = new Intent(MainActivity.this, EmployeeLoginActivity.class);
            startActivity(intent);
        });

        employerLoginButton.setOnClickListener(v -> {
            // Navigate to EmployerLoginActivity
            Intent intent = new Intent(MainActivity.this, EmployerLoginActivity.class);
            startActivity(intent);
        });


    }

}
