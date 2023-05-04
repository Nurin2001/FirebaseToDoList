package com.assignment.todolistrecycler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.TaskViewHolder> {

    public ArrayList<Task> taskList;
    private Context context;

    public MyAdapter(ArrayList<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View task_row=  LayoutInflater.from(context).inflate(R.layout.list_row,parent,false);
        return new TaskViewHolder(task_row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.task.setText("Task: " + taskList.get(position).getTask());
        holder.startDate.setText("End date: " + taskList.get(position).getStartDate());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView task, startDate;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.taskTv);
            startDate = itemView.findViewById(R.id.startDateTv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            //String idTask = taskList.get(getAdapterPosition()).getTask();
            //String idTask = ((Activity)view.getContext()).getIntent().getStringExtra("uniqueID");
            Intent intent = new Intent(view.getContext(), UpdateTask.class);
            intent.putExtra("task", taskList.get(getAdapterPosition()).getTask());
            intent.putExtra("startDate", taskList.get(getAdapterPosition()).getStartDate());
            intent.putExtra("idTask", taskList.get(getAdapterPosition()).getIdTask());

            view.getContext().startActivity(intent);

        }
    }
}
