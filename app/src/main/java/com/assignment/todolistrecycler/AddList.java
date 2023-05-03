package com.assignment.todolistrecycler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddList extends AppCompatActivity {

    EditText task, startDate;
    Button addBtn;
    ImageButton calendarBtn;

    DatabaseReference dbRef;

    Calendar calendar;

    DatePickerDialog.OnDateSetListener setCalendarListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        task = findViewById(R.id.taskEdit);
        startDate = findViewById(R.id.startDateEdit);
        startDate.setEnabled(false);
        addBtn = findViewById(R.id.updateBtn);
        calendarBtn = findViewById(R.id.calendarEnd);

        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int mth = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddList.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setCalendarListener, year, mth, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setCalendarListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mth, int day) {
                mth = mth+1;
                String date = day+"/"+mth+"/"+year;
                startDate.setText(date);
            }
        };
        //firebase
        dbRef = FirebaseDatabase.getInstance().getReference("Item");

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDb();
            }
        });
    }

    private void uploadDb() {
        Task taskList = new Task(task.getText().toString().trim(), startDate.getText().toString().trim());
        //Task taskList = new Task(task.getText().toString().trim(), startDate.getText().toString().trim(), uniqID);

        if(!TextUtils.isEmpty(task.getText().toString())) {
            dbRef.push().setValue(taskList);
            String uniqID = dbRef.push().getKey();

            Intent intent = new Intent(AddList.this, MainActivity.class);
            intent.putExtra("uniqueID", uniqID);
            task.setText("");
            startDate.setText("");
            startActivity(intent);

//            dbRef.child("Item").child(uniqID).setValue(taskList)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task2) {
//
//                            if(task2.isSuccessful()){
//                                Toast.makeText(AddList.this, "New task added", Toast.LENGTH_SHORT).show();
//                                task.setText("");
//                                startDate.setText("");
//                                startActivity(new Intent(AddList.this, MainActivity.class));
//                            }
//                            else
//                                Toast.makeText(AddList.this, "Task is not added", Toast.LENGTH_SHORT).show();
//                        }
//                    });
        }
        else
            Toast.makeText(this, "Don't leave the task empty", Toast.LENGTH_SHORT).show();

    }
}