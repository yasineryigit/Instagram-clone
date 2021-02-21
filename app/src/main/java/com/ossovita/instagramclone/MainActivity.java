package com.ossovita.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseObject score = new ParseObject("Score");
        score.put("username","nick");
        score.put("score",45);
        score.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    //OK
                    Log.i("Success","We saved the score");
                }else{
                    e.printStackTrace();
                }
            }
        });
        /*
        //It will make query in Score class
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
        //Id'si verilen obje üzerinde query yapacak
        query.getInBackground("p0DgwEXLv3", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e==null && object !=null){

                    object.put("score",63);
                    object.saveInBackground();
                    Log.i("username", object.getString("username")); //objecnin username keyi ile tutulan değerini aldık
                    Log.i("score",Integer.toString(object.getInt("score")));

                }
            }
        });
        */
        /*
        //Create a tweet class, username tweet, save it to the parse, query it, update the tweet
        //tweet1 objesini Tweets tablosu içinde olusturduk
        //create&save tweet1
        ParseObject tweet1 = new ParseObject("Tweets");
        tweet1.put("username","mike");
        tweet1.put("tweet","i bought doge");
        tweet1.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Log.i("Success","tweet1 saved");
                }else{
                    e.printStackTrace();
                }
            }
        });
        //create&save tweet2
        ParseObject tweet2 = new ParseObject("Tweets");
        tweet2.put("username","john");
        tweet2.put("tweet","btc to the moon");
        tweet2.saveInBackground();
        */

        //Tweets class'ında query yapacağımızı söylüyoruz
        ParseQuery query = new ParseQuery("Tweets");
        query.getInBackground("w8TRU7UFbV", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(object!=null&&e ==null){
                    object.put("tweet","it was a good investment");
                    object.saveInBackground();
                    //Verilen objenin username,tweet keyi ile tutulan verileri getirdik
                    Log.i("username",object.getString("username"));
                    Log.i("tweet",object.getString("tweet"));
                }
            }
        });





        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }
}