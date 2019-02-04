package com.example.ritvik_cardiobook1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/*
 * @author Ritvik Khanna
 * @ccid ritvik
 * Copyright 2019, Ritvik khanna
 * @github RitvikKhanna
 * oldMeasurementsCustomized is the class for editing or viewing the old measurements
 * A user can view, or change the values entered for the various measurements
 * This class updates the clicked measurement and saves it.
 */

public class oldMeasurementsCustomized extends AppCompatActivity {

    // Initialising the EditText fields
    private EditText oldDateText, oldTimeText, oldSysText, oldDiasText,oldHRText,oldComText;
    private String date,time,sys,dia,HR,comment;

    // On create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_measurements_customized);
    }

    // onStart
    protected void onStart() {
        super.onStart();
        enterPreviousValues();
        Button save = (Button)findViewById(R.id.saveButton);

        /*
        Setting up an onClickListener for the save button for when when save is presses
        It takes the intent with which this class was started, gets the position of the
        clicked item and then fills in the previous values.
        The user can leave the values as it is and view it
        OR
        The user can edit one or more fields and update the measurement
        After updation when clicked on save the file is updated with the new updates if any.
         */
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
                int position = Integer.parseInt(message);

                // Get position of the clicked measurement
                Measurements editMeasurement = MainActivity.measurementsList.get(position);

                // Save the values where user made changes
                editMeasurement.setDate(oldDateText.getText().toString());
                editMeasurement.setTime(oldTimeText.getText().toString());
                editMeasurement.setDiastolicPressure(oldDiasText.getText().toString());
                editMeasurement.setSystolicPressure(oldSysText.getText().toString());
                editMeasurement.setHeartRate(oldHRText.getText().toString());
                editMeasurement.setComment(oldComText.getText().toString());

                // Update that particular measurement
                MainActivity.measurementsList.set(position,editMeasurement);

                // Save the updated measurement in the file again
                try{
                    FileOutputStream fos = openFileOutput(MainActivity.FILENAME,0);
                    MainActivity.saveInFile(fos,MainActivity.FILENAME);
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Toast.makeText(oldMeasurementsCustomized.this, "Measurement dated: " + editMeasurement.getDate() + " updated", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }



    /*
    This method has a very simple implementation to just fill
    the edit text with the values previously entered by the user
     */
    private void enterPreviousValues(){

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        int position = Integer.parseInt(message);
        Measurements editMeasurement = MainActivity.measurementsList.get(position);

        date = editMeasurement.getDate();
        time = editMeasurement.getTime();
        sys = editMeasurement.getSystolicPressure();
        dia = editMeasurement.getDiastolicPressure();
        HR = editMeasurement.getHeartRate();
        comment = editMeasurement.getComment();


        oldDateText = (EditText)findViewById(R.id.oldDateText1);
        oldTimeText = (EditText)findViewById(R.id.oldTimeText1);
        oldDiasText = (EditText)findViewById(R.id.oldDiasText1);
        oldSysText = (EditText)findViewById(R.id.oldSysText1);
        oldHRText = (EditText) findViewById(R.id.oldHRText1);
            oldComText = (EditText)findViewById(R.id.oldComText1);

        oldDateText.setText(date);
        oldTimeText.setText(time);
        oldSysText.setText(sys);
        oldDiasText.setText(dia);
        oldHRText.setText(HR);
        oldComText.setText(comment);

    }

}
