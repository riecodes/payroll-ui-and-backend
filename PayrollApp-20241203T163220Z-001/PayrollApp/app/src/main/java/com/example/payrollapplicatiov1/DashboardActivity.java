package com.example.payrollapplicatiov1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployeeAdapter adapter;
    private List<Employee> employeeList;
    private DatabaseHelper dbHelper;
    private TextView tvTotalHours, tvTotalSalary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = findViewById(R.id.recyclerView);
        tvTotalHours = findViewById(R.id.tvTotalHours);
        tvTotalSalary = findViewById(R.id.tvTotalSalary);
        Button btnLogout = findViewById(R.id.btnLogout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        employeeList = dbHelper.getAllEmployees();

        adapter = new EmployeeAdapter(employeeList);
        recyclerView.setAdapter(adapter);

        updateTotals();

        // Handle logout button click
        btnLogout.setOnClickListener(v -> {
            // Navigate to Login Activity
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish current activity to remove it from the back stack
        });
    }

    private void updateTotals() {
        int totalHours = 0;
        double totalSalary = 0.0;
        for (Employee employee : employeeList) {
            totalHours += employee.getWorkingHours();
            totalSalary += employee.getSalary();
        }
        tvTotalHours.setText(String.valueOf(totalHours));
        tvTotalSalary.setText("PHP " + String.format("%.2f", totalSalary));
    }

    private class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

        private List<Employee> employees;

        EmployeeAdapter(List<Employee> employees) {
            this.employees = employees;
        }

        @Override
        public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
            return new EmployeeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EmployeeViewHolder holder, int position) {
            Employee employee = employees.get(position);
            holder.tvFullName.setText(employee.getFullName());
            holder.tvUsername.setText(employee.getUsername());
            holder.tvWorkingHours.setText(String.valueOf(employee.getWorkingHours()));
            holder.tvSalary.setText(String.format("%.2f", employee.getSalary()));

            holder.btnUpdate.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, UpdateEmployeeActivity.class);
                intent.putExtra("employee", employee);
                startActivityForResult(intent, 1);
            });

            // Set up delete button with confirmation dialog
            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(DashboardActivity.this)
                        .setTitle("Delete Employee")
                        .setMessage("Are you sure you want to delete this employee?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            // Delete employee from database
                            dbHelper.deleteEmployee(employee.getUsername());
                            // Remove from the list and notify adapter
                            employees.remove(position);
                            notifyItemRemoved(position);
                            updateTotals(); // Update totals after deletion
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            });
        }

        @Override
        public int getItemCount() {
            return employees.size();
        }

        class EmployeeViewHolder extends RecyclerView.ViewHolder {
            TextView tvFullName, tvUsername, tvWorkingHours, tvSalary;
            Button btnUpdate, btnDelete;

            EmployeeViewHolder(View itemView) {
                super(itemView);
                tvFullName = itemView.findViewById(R.id.tvFullName);
                tvUsername = itemView.findViewById(R.id.tvUsername);
                tvWorkingHours = itemView.findViewById(R.id.tvWorkingHours);
                tvSalary = itemView.findViewById(R.id.tvSalary);
                btnUpdate = itemView.findViewById(R.id.btnUpdate);
                btnDelete = itemView.findViewById(R.id.btnDelete); // Ensure this button is defined in your item_employee layout
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Employee updatedEmployee = (Employee) data.getSerializableExtra("updatedEmployee");
            for (int i = 0; i < employeeList.size(); i++) {
                if (employeeList.get(i).getUsername().equals(updatedEmployee.getUsername())) {
                    employeeList.set(i, updatedEmployee);
                    break;
                }
            }
            adapter.notifyDataSetChanged();
            updateTotals();
        }
    }
}
