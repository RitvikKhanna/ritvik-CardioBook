package com.example.ritvik_cardiobook1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;


/*
 * @author Ritvik Khanna
 * @ccid ritvik
 * Copyright 2019, Ritvik khanna
 * @github RitvikKhanna
 * The addEditMeasurement class is for adding measurements
 * We have an onCreate method for when it's created
 * We have an onStart method for right after onCreate
 * We have a getMeasurements method which takes the EditText typed by the user
 * converts it to a string and stores it in the measurement object
 * After storing of the data the measurement object thus created is returned to onActivityForResult
 */

public class addEditMeasurement extends AppCompatActivity {


    // The edit text fields for taking in the user input
    private EditText systolic,diastolic,heart,comment,datePick,timePick;

    // Instance of Measurements class created to input new measurement
    private Measurements measurements = new Measurements();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_measurement);

        // Initialising the EditText fields for the user input
        systolic = (EditText) findViewById(R.id.systolicPressureEntered);
        diastolic = (EditText) findViewById(R.id.diastolicPressureEntered);
        heart = (EditText) findViewById(R.id.heartRateEntered);
        comment = (EditText) findViewById(R.id.commentEntered);
        datePick = (EditText) findViewById(R.id.datePick);
        timePick = (EditText) findViewById(R.id.timePick);

    };

    protected void onStart() {
        super.onStart();
        Button button = (Button) findViewById(R.id.button);

        // Setting up an onClickListerner for submit, for when submit is clicked
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // See definition above
                getMeasurement();
                // Return to MainActivity
                Intent intent = new Intent();
                intent.putExtra("myList",measurements);
                setResult(RESULT_OK, intent);
                Toast.makeText(addEditMeasurement.this, "Measurement dated: " + measurements.getDate() + " saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // See method definition above
    public void getMeasurement(){

        // Getting the text entered by the user in EditText class
        String commentEntered, dateEntered, timeEntered,systolicPressure, diastolicPressure, heartRate;

        //Converting the EditText to Strings and then storing them in the list.
        commentEntered = comment.getText().toString();
        dateEntered = datePick.getText().toString();
        timeEntered = timePick.getText().toString();
        heartRate = heart.getText().toString();
        systolicPressure = systolic.getText().toString();
        diastolicPressure = diastolic.getText().toString();

        /*
        * The following if statements make sure
        * that the app does not crash in the event when submit is clicked without
        * entering any values
        * The following if statements fill up the empty fields automatically
         */
        if(commentEntered==null||commentEntered.isEmpty()){
            commentEntered = "N/A";
        }
        if(dateEntered==null||dateEntered.isEmpty()){
            dateEntered = "0000/00/00";
        }
        if(timeEntered==null||timeEntered.isEmpty()){
            timeEntered= "00:00";
        }
        if(heartRate==null||heartRate.isEmpty()){
            heartRate = "0";
        }
        if (systolicPressure==null||systolicPressure.isEmpty()){
            systolicPressure = "0";
        }
        if (diastolicPressure==null||diastolicPressure.isEmpty()){
            diastolicPressure = "0";
        }

        measurements.setComment(commentEntered);
        measurements.setDate(dateEntered);
        measurements.setTime(timeEntered);
        measurements.setHeartRate(heartRate);
        measurements.setSystolicPressure(systolicPressure);
        measurements.setDiastolicPressure(diastolicPressure);

    }


}


