package com.example.ritvik_cardiobook1;

/*
All the necessary imports we need for the app
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.ls.LSException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/*
 * @author Ritvik Khanna
 * @ccid ritvik
 * Copyright 2019, Ritvik khanna
 * @github RitvikKhanna
 * Main activity design for the app ritvik-CardioBook
 * The app is designed for a user to
 * add/edit/delete measurements
 * The measurements include - Date, Time, Systolic and Diastolic Pressure, Heart Rate and comments
 * The app will show the flagged Systolic and Diastolic Pressures
 * i.e. The user will be able to clearly see on which days was his measurements not normal
 */



public class MainActivity extends AppCompatActivity {

    // Declaring project package
    public static final String EXTRA_MESSAGE = "com.example.ritvik_cardiobook1.MESSAGE";
    // Setting file name to store the entered values by user
    public static final String FILENAME = "oldMeasurementList.sav";
    // Creating a list View for the previous measurements, entered by the user
    public ListView oldMeasurements;

    // Button for adding a measurement
    private ImageButton addMeasurementButton;

    /*
    Initialising the adapter for refreshing when the user adds/edits anything.
    We use MeasurementAdapterInflator - custom adapter so that we show flagged values
     */

    public static ArrayList<Measurements> measurementsList = new ArrayList<Measurements>();
    public static MeasurementAdapterInflator customAdapter;


    // onCreation this method is called
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Calls the method to check if there are measurements or not
        See method definition
         */
        noMeasurementsView();

        // Setting the list view of measurements
        oldMeasurements = (ListView) findViewById(R.id.oldMeasurements);
        //Initializing the cusom adapter for the MainActivity, and the measurement list we have
        customAdapter = new MeasurementAdapterInflator(MainActivity.this, R.layout.activity_list_item,measurementsList);
        oldMeasurements.setAdapter(customAdapter);


        // View/Edit/Delete for the items in the list using a dialog box
        oldMeasurements.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> parent, View view, final int position, long id){

                // Initialising
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("What do you want to do?"); // Title of the dialof box

                /*
                 Create 2 buttons
                 Positive Button as -> View or edit measurement
                 On Click for View/Edit take to an activity
                 The view/edit class is called with the position of the item clicked for changes
                 The delete measurement button just simply deletes that measurements and updates
                 The Toast.makeText basically prompts a message on the screen
                 It confirms the user of his action, whether it was updated, added or deleted.
                  */
                builder.setPositiveButton("View/Edit Measurement",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int which){
                        Intent intent = new Intent(MainActivity.this,oldMeasurementsCustomized.class);
                        intent.putExtra(EXTRA_MESSAGE, Integer.toString(position));
                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("Delete Measurement", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Measurements deletedMeasurement = (Measurements) measurementsList.get(position);
                        String dateDelete = deletedMeasurement.getDate();
                        measurementsList.remove(position);

                        // On delete, update the saved file to have the required measurements only
                        try{
                            FileOutputStream fos = openFileOutput(FILENAME,0);
                            saveInFile(fos,FILENAME);
                            noMeasurementsView();
                            customAdapter.notifyDataSetChanged();
                        }catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this, "Measurement dated: " + dateDelete + " deleted", Toast.LENGTH_SHORT).show();

                    }
                });

                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                builder.show();

            }
        });


        // For the image button,
        addMeasurementButton = (ImageButton) findViewById(R.id.addMeasurementButton);

        /*
         When the '+' sign is clicked on the main page, the add/edit measurement page is opened.
         The addEditMeasurement class is created with a startActivityForResult so that on return
         the new measurement (object) can be stored in the fie without any complications
          */
        addMeasurementButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,addEditMeasurement.class);
                intent.putExtra("Add",true);
                startActivityForResult(intent,1);

                // Refresh so that the initial message of "Add a measurement is no longer displayed"
                customAdapter.notifyDataSetChanged();
                noMeasurementsView();
            }
        });


    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile(); // load from old file when the app starts

        /*
        Create a new custom adapter again for the layout so that we can load the files with
        the required custom view and to show the flagged values
         */
        customAdapter = new MeasurementAdapterInflator(MainActivity.this, R.layout.activity_list_item,measurementsList);
        oldMeasurements.setAdapter(customAdapter);

    }


    /*
    This method is called when the user presses submit on the addEditMeasurement.
    This is the result for the startActivityForResult method.
    It receives a measurement type object with the required values and stores it in the file
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Measurements measurements = (Measurements) data.getSerializableExtra("myList");
                measurementsList.add(measurements);
                noMeasurementsView();
                try{
                    FileOutputStream fos = openFileOutput(FILENAME,0);
                    saveInFile(fos,FILENAME);
                    customAdapter.notifyDataSetChanged();
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }


    }


    /* emptyView() checks if our measurements list is empty or not.
       If it's not empty display the measurements. */
    private void noMeasurementsView(){

        TextView noMeasurements = findViewById(R.id.noMeasurementsInList);
        if(measurementsList.size()==0||measurementsList == null){

            noMeasurements.setVisibility(View.VISIBLE);
        }
        else {

            noMeasurements.setVisibility(View.INVISIBLE);
        }
    }


    /*
    This method loads the saved file measurements for the display
    The code was taken from Lonely Twitter shown in the labs.
    https://github.com/Vikuku/lonelyTwitter/tree/t19lab2
     */
    private void loadFromFile() {

        try {

            FileInputStream fis = openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Measurements>>(){}.getType();
            measurementsList = gson.fromJson(reader, listType);

            // Called to check if the initial message of "Add a measurement" is to be shown or not
            noMeasurementsView();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            measurementsList = new ArrayList<Measurements>();
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /*
    This method saves the measurement in the filename mentioned.
    The code was taken from Lonely Twitter shown in the labs.
    https://github.com/Vikuku/lonelyTwitter/tree/t19lab2
     */
    public static void saveInFile(FileOutputStream fos, String FileName){
        try {
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);

            Gson gson =new Gson();

            gson.toJson(measurementsList,writer);

            writer.flush();
            fos.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
