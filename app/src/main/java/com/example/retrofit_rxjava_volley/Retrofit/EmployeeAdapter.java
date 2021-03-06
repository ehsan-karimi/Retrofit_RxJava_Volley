package com.example.retrofit_rxjava_volley.Retrofit;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_rxjava_volley.EmployeeModel;
import com.example.retrofit_rxjava_volley.R;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private List<EmployeeModel> employeeModel;

    public EmployeeAdapter(List<EmployeeModel> employeeModel) {
        this.employeeModel = employeeModel;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmployeeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false));
    }

    public void addEmployee(EmployeeModel employeeModel) {
        this.employeeModel.add(0, employeeModel);
        notifyItemInserted(0);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        holder.bind(employeeModel.get(position));
    }

    @Override
    public int getItemCount() {
        return employeeModel.size();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private TextView fullNameTv;
        private TextView courseTitleTv;
        private TextView scoreTv;
        private TextView firstCharacterTv;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTv = itemView.findViewById(R.id.tv_student_fullName);
            courseTitleTv = itemView.findViewById(R.id.tv_student_course);
            scoreTv = itemView.findViewById(R.id.tv_student_score);
            firstCharacterTv = itemView.findViewById(R.id.tv_student_firstCharacter);
        }

        @SuppressLint("SetTextI18n")
        public void bind(EmployeeModel employeeModel) {
            fullNameTv.setText(employeeModel.getFirstname() + " " + employeeModel.getLastname());
            courseTitleTv.setText(employeeModel.getLastname());
            scoreTv.setText(String.valueOf(employeeModel.getPhonenumber()));
            firstCharacterTv.setText(employeeModel.getId());
        }
    }
}
