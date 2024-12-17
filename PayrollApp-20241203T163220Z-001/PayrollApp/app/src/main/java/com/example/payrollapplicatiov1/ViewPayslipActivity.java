package com.example.payrollapplicatiov1;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewPayslipActivity extends AppCompatActivity {

    private TextView tvPayslipDetails;
    private Employee employee; // Assuming Employee object is passed from EmployeeHomePage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payslip);

        tvPayslipDetails = findViewById(R.id.tvPayslipDetails);

        // Get employee object from intent
        employee = (Employee) getIntent().getSerializableExtra("employee");

        // Display payslip details
        if (employee != null) {
            String payslipDetails = generatePayslip(employee);
            tvPayslipDetails.setText(payslipDetails);
        }
    }

    private String generatePayslip(Employee employee) {
        StringBuilder payslip = new StringBuilder();
        payslip.append("Payslip for ").append(employee.getFullName()).append("\n");

        // Assuming getWorkingHours() returns total hours worked in a month
        int totalWorkingHours = employee.getWorkingHours();
        double totalSalary = employee.getSalary();

        // Calculate hourly rate
        double hourlyRate = (totalWorkingHours > 0) ? totalSalary / totalWorkingHours : 0;

        // Calculate deductions
        double tax = totalSalary * 0.10; // Assuming 10% tax
        double netPay = totalSalary - tax; // Net pay after tax

        // Append payslip details
        payslip.append("Total Working Hours: ").append(totalWorkingHours).append(" hours\n");
        payslip.append("Hourly Rate: PHP ").append(String.format("%.2f", hourlyRate)).append("\n");
        payslip.append("Total Salary: PHP ").append(String.format("%.2f", totalSalary)).append("\n");
        payslip.append("------------------------------\n");
        payslip.append("Deductions: \n");
        payslip.append("Tax (10%): PHP ").append(String.format("%.2f", tax)).append("\n");
        payslip.append("------------------------------\n");
        payslip.append("Net Pay: PHP ").append(String.format("%.2f", netPay)).append("\n");

        return payslip.toString();
    }
}
