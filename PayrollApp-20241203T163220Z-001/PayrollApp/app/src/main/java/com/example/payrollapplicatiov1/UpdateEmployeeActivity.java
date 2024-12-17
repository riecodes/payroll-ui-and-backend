package com.example.payrollapplicatiov1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateEmployeeActivity extends AppCompatActivity {

    private EditText etWorkingHours, etSalary;
    private Button btnDone;
    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);

        etWorkingHours = findViewById(R.id.etWorkingHours);
        etSalary = findViewById(R.id.etSalary);
        btnDone = findViewById(R.id.btnDone);

        employee = (Employee) getIntent().getSerializableExtra("employee");

        if (employee != null) {
            etWorkingHours.setText(String.valueOf(employee.getWorkingHours()));
            etSalary.setText(String.valueOf(employee.getSalary()));
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int workingHours = Integer.parseInt(etWorkingHours.getText().toString());
                double salary = Double.parseDouble(etSalary.getText().toString());

                employee.setWorkingHours(workingHours);
                employee.setSalary(salary);

                DatabaseHelper dbHelper = new DatabaseHelper(UpdateEmployeeActivity.this);
                dbHelper.updateEmployee(employee);  // Update employee in database

                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedEmployee", employee);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }
}