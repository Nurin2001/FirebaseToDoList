package com.assignment.todolistrecycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateTask extends AppCompatActivity {

    EditText taskTxt, startDate;
    Button updBtn, dltBtn;
    DatabaseReference dbRef;

    private String idTask;
    private String[] TITLE = {"Update Task", "Delete Task"};
    private String message = "Are you sure you want to proceed?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        Intent intent = getIntent();

        taskTxt = findViewById(R.id.taskEdit);
        startDate = findViewById(R.id.endDateEdit);
        updBtn = findViewById(R.id.updateBtn);
        dltBtn = findViewById(R.id.deleteBtn);

        idTask = intent.getStringExtra("idTask");
        taskTxt.setText(intent.getStringExtra("task"));
        startDate.setText(intent.getStringExtra("startDate"));

        dbRef = FirebaseDatabase.getInstance().getReference("Item");

        updBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog(TITLE[0], message);
            }
        });
        dltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog(TITLE[1], message);
            }
        });

    }

    private void alertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Item");
                if(title.equals(TITLE[0])) {
                    Task taskList = new Task(taskTxt.getText().toString().trim(), startDate.getText().toString().trim());
                    ref.child(idTask).setValue(taskList).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(UpdateTask.this, "Task Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UpdateTask.this, MainActivity.class));
                            }

                        }
                    });
                }
                else if(title.equals(TITLE[1])) {
                    ref.
                            child(idTask).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(UpdateTask.this, "Task deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UpdateTask.this, MainActivity.class));
                            }

                        }
                    });
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        }).create().show();
    }
}