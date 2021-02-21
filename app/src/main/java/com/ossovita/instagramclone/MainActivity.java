package com.ossovita.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //We gonna make some queries on Scores class
        ParseQuery<ParseObject> query = new ParseQuery("Score");
        query.whereGreaterThan("score",50).findInBackground(new FindCallback<ParseObject>() {//arama yaptırıyoruz
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null&&objects.size()>0){
                    for(ParseObject object : objects){
                        if(object.getInt("score")>50){
                            object.put("score",object.getInt("score")+20);
                            object.saveInBackground();
                            Log.i("Success","Scores Increased");
                        }
                    }
                }
            }
        });



        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }
}