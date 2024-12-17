package com.example.payrollapplicatiov1;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URI;

public class EmployeeHomePage extends AppCompatActivity {

    private TextView tvWelcome, tvTotalWorkingHours, tvTotalSalary;
    private Button btnViewPayslip, btnLogout;
    private DatabaseHelper dbHelper;
    private ImageView imageViewPhoto;
    private Employee employee; // Assuming Employee object is passed from login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home_page);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvTotalWorkingHours = findViewById(R.id.tvTotalWorkingHours);
        tvTotalSalary = findViewById(R.id.tvTotalSalary);
        btnViewPayslip = findViewById(R.id.btnViewPayslip);
        btnLogout = findViewById(R.id.btnLogout);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Get employee object from intent
        employee = (Employee) getIntent().getSerializableExtra("employee");
        if (employee != null) {
            tvWelcome.setText("Welcome, " + employee.getFullName() + "!");
            tvTotalWorkingHours.setText("Total Working Hours: " + employee.getWorkingHours());
            tvTotalSalary.setText("Total Salary: " + String.format("%.2f", employee.getSalary()));
            imageViewPhoto.setImageURI(Uri.parse(employee.getPhoto()));
        }

        btnViewPayslip.setOnClickListener(v -> {
            // Navigate to ViewPayslipActivity
            Intent intent = new Intent(EmployeeHomePage.this, ViewPayslipActivity.class);
            intent.putExtra("employee", employee); // Pass employee object
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            // Handle logout logic
            finish(); // Close this activity and go back to login
        });
    }
}
