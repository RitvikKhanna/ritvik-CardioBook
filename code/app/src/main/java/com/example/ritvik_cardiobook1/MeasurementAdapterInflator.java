package com.example.ritvik_cardiobook1;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.w3c.dom.Text;
import java.util.ArrayList;

/*
 * @author Ritvik Khanna
 * @ccid ritvik
 * Copyright 2019, Ritvik khanna
 * @github RitvikKhanna
 * This class changes the view from a normal simple adapter to a cusotmized adapter
 * We will be flagging values here for the user to see without a problem
 */

/*
For this class help was taken from
https://www.javatpoint.com/android-custom-listview
https://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
 */

public class MeasurementAdapterInflator extends ArrayAdapter<Measurements> {

    private Context mContext;
    private int mresource;

    public MeasurementAdapterInflator(@NonNull Context context, int resource, ArrayList<Measurements> measurementsList) {
        super(context, 0, measurementsList);
        mContext = context;
        this.mresource = resource;
    }

    // Get the initial View and then change it according to our customized list view
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        //We creat a new layoutInflator to have a custom layout so that we can show flagged items
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(mresource, parent, false);

        Measurements measurementCheck = getItem(position);

        // We find the textview items in our layout and set the color of those to be 'BLUE'
        TextView dateTime = (TextView) view.findViewById(R.id.DateTime);
        TextView systolicT = (TextView) view.findViewById(R.id.Systolic);
        TextView diastolicT = (TextView) view.findViewById(R.id.Diastolic);
        TextView heartRateT = (TextView) view.findViewById(R.id.HeartRate);
        TextView commentT = (TextView) view.findViewById(R.id.Comment);
        dateTime.setTextColor(Color.BLUE);
        commentT.setTextColor(Color.BLUE);
        heartRateT.setTextColor(Color.BLUE);
        systolicT.setTextColor(Color.BLUE);
        diastolicT.setTextColor(Color.BLUE);

        /*
        The following if-else-if checks if the measurement object is null or not.
        If it is null then do the else-if clause and auto substitute values
        If the object is not null, for the entered values, we check
        if the Systolic pressure is between 90 and 140 and
        if the diastolic pressure is between 60 and 90
        If they are under the normal range we do not do anything and the color
        of the textview remains blue and the background of the layout is set to transparent
        If any or both of the values are not in the normal range
        we change the text color of that particular pressure to 'CYAN'
        and the background of the particular pressure text view to 'RED' to highlight
        Even if one of them is not under the normal range the color of the whole view
        changes to yellow to further highlight which particular measurement has been flagged.
         */

        if (measurementCheck != null) {

            Integer sysI = Integer.parseInt(measurementCheck.getSystolicPressure());
            Integer diaI = Integer.parseInt(measurementCheck.getDiastolicPressure());

            // Check for normal range pressures
            if (sysI <= 90 || sysI >= 140) {
                view.setBackgroundColor(Color.YELLOW);
                systolicT.setTextColor(Color.CYAN);
                systolicT.setBackgroundColor(Color.RED);
            }
            if (diaI <= 60 || diaI >= 90) {
                view.setBackgroundColor(Color.YELLOW);
                diastolicT.setTextColor(Color.CYAN);
                diastolicT.setBackgroundColor(Color.RED);
            }

            // Set each textview with string text
            String DateTimeS = "Date: "+measurementCheck.getDate()+"    ||    Time: "+measurementCheck.getTime();
            String systoP = "Systlolic Pressure: "+measurementCheck.getSystolicPressure();
            String diasP = "Diastolic Pressure: "+measurementCheck.getDiastolicPressure();
            String hearP = "Heart Rate: "+measurementCheck.getHeartRate();
            String commetP = "Comment(s): "+measurementCheck.getComment();

            dateTime.setText(DateTimeS);
            systolicT.setText(systoP);
            diastolicT.setText(diasP);
            heartRateT.setText(hearP);
            commentT.setText(commetP);


        }
        else if(measurementCheck==null){

            String DateTimeS = "Date: N/A    ||    Time: N/A ";
            String systoP = "Systlolic Pressure: 0 ";
            String diasP = "Diastolic Pressure: 0";
            String hearP = "Heart Rate: 0";
            String commetP = "Comment(s): N/A ";
            dateTime.setText(DateTimeS);
            systolicT.setText(systoP);
            diastolicT.setText(diasP);
            heartRateT.setText(hearP);
            commentT.setText(commetP);

        }

        // Return the original view after inflating the with the cusotm layout inflator
        return view;
    }
}

