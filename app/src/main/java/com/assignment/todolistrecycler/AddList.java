package com.assignment.todolistrecycler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AddList extends AppCompatActivity {

    EditText task, startDate;
    Button addBtn, pickBtn;
    ImageButton calendarStartBtn;
    VideoView videoView;
    MediaController mediaController;

    DatabaseReference dbRef;
    StorageReference storageReference;

    Calendar calendar;
    Uri vidUri;
    String timeStamp, fileName;
    int randomNumber;

    DatePickerDialog.OnDateSetListener setCalendarListener;

    private static final int PICK_VIDEO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        task = findViewById(R.id.taskEdit);
        startDate = findViewById(R.id.endDateEdit);
        startDate.setEnabled(false);
        addBtn = findViewById(R.id.updateBtn);
        pickBtn = findViewById(R.id.pickBtn);
        calendarStartBtn = findViewById(R.id.calendarEnd);
        videoView = findViewById(R.id.vidView);

        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int mth = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //preview picked video
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();

        calendarStartBtn.setOnClickListener(new View.OnClickListener() {
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
        storageReference = FirebaseStorage.getInstance().getReference();

        pickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseVideo(view);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_VIDEO && resultCode == RESULT_OK ){
            if(data != null && data.getData()!=null ) {    // Get the selected video file's Uri
                vidUri = data.getData();

                // Get the current date/time and a random number to create a unique file name
                timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                randomNumber = new Random().nextInt(10000);
                fileName = "video_" + timeStamp + "_" + randomNumber + ".mp4";

                // Set the video file to the VideoView
                videoView.setVideoURI(vidUri);
                videoView.start();
            }
            // Create a reference to the video file in Firebase Storage
            StorageReference videoRef = storageReference.child("videos/"+fileName);

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadDb(videoRef);
                }
            });
        }
        else if(resultCode==RESULT_CANCELED) {
            Toast.makeText(this, "Video selection canceled", Toast.LENGTH_SHORT).show();
        }
    }

    public void chooseVideo(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO);
    }

    private void uploadDb(StorageReference videoRef) {
        Task taskList = new Task(task.getText().toString().trim(), startDate.getText().toString().trim());
        //Task taskList = new Task(task.getText().toString().trim(), startDate.getText().toString().trim(), uniqID);

        String uniqID="";

        if(!TextUtils.isEmpty(task.getText().toString())) {
            dbRef.push().setValue(taskList);
            uniqID = dbRef.push().getKey();

            task.setText("");
            startDate.setText("");
        }
        else
            Toast.makeText(this, "Don't leave the task empty", Toast.LENGTH_SHORT).show();

        if(videoView.isPlaying()){
// Upload the video file to Firebase Storage
            UploadTask uploadTask = videoRef.putFile(vidUri);
            String finalUniqID = uniqID;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Video uploaded successfully
                    Toast.makeText(AddList.this, "Video uploaded", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddList.this, MainActivity.class);
                    intent.putExtra("uniqueID", finalUniqID);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Video upload failed
                    Toast.makeText(AddList.this, "Error uploading video", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}