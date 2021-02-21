package com.ossovita.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {
    LinearLayout linLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        setTitle(username + "'s photos");

        linLayout = findViewById(R.id.linLayout);
        //Image classı uzerinde parseobject arayacagiz
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        query.whereEqualTo("username",username).orderByAscending("createdAt");//Üzerine tıklanılan kullanıcının paylaşımlarını createdAt'e göre sıralayarak alacağız
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null&&objects.size()>0){//hata yoksa ve gelen dosya varsa
                    for(ParseObject object : objects){
                        ParseFile file = (ParseFile) object.get("image");//image adındaki dosyayı al parsefile'a at
                        file.getDataInBackground(new GetDataCallback() {//ilgili dosyayı arkaplanda indir
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e==null&&data!=null){//hatasız indirilen data varsa
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);

                                    ImageView imageView = new ImageView(getApplicationContext());
                                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    ));
                                    imageView.setImageBitmap(bitmap);
                                    linLayout.addView(imageView);
                                }
                            }
                        });
                    }
                }

            }
        });



    }
}