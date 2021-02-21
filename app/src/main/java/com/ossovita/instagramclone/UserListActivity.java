package com.ossovita.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    Bitmap selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Instagram");
        setContentView(R.layout.activity_user_list);
        final ListView listView = findViewById(R.id.listView);
        final ArrayList<String> usernames = new ArrayList<>();
        final ArrayAdapter aa = new ArrayAdapter(UserListActivity.this, android.R.layout.simple_list_item_1,usernames);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getApplicationContext(),UserFeedActivity.class).putExtra("username",usernames.get(i)));
            }
        });
        listView.setAdapter(aa);

        //Sorgu objesi oluşturduk
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        //Kendi usernamemimiz hariç diğerlerini tarayacak ve username'lere göre alfabetik sıralayacak
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername()).addAscendingOrder("username").findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null&&objects.size()>0){//Hata yoksa ve gelen obje varsa
                    for(ParseUser user : objects){//her bir parse user için
                        usernames.add(user.getUsername());//usernamelerini al, listeye at.
                    }
                }else{
                    Toast.makeText(UserListActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                aa.notifyDataSetChanged();//Liste içerisindeki veriler değiştiği için adaptöre notify veriyoruz
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.share_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.Share){
            //izin yoksa önce izin iste
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                //izin istiyoruz
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }else{//izin varsa galeriye git
               goToGallery();
            }
        }
        if(item.getItemId()==R.id.logout){
            ParseUser.logOut();
            finish();
            startActivity(new Intent(UserListActivity.this,MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    //Sorulan iznin sonucuna göre
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                goToGallery();
            }
        }
    }

    //Galeriden gelen sonuca göre
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&resultCode==RESULT_OK&&data!=null){//başarıyla gelen data vasra
           Uri imageData = data.getData();
            try{
                if(Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    Log.i("Image Selected","Good Work");
                    uploadToServer();
                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    Log.i("Image Selected","Good Work");
                    uploadToServer();

                }
            }catch (Exception e){
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadToServer() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG,100,os);
        byte[] byteArray = os.toByteArray();
        //byteArray'e donusturdugumuz dosyayı alıp parseFile'a donusturuyoruz
        ParseFile file = new ParseFile("image.png",byteArray);
        //Image adında class içine bir 2 veri alan bir obje koyuyoruz
        ParseObject object = new ParseObject("Image");
        object.put("image",file);
        object.put("username",ParseUser.getCurrentUser().getUsername());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(UserListActivity.this, "Image has been shared!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UserListActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToGallery() {
        Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentToGallery,2);
    }
}