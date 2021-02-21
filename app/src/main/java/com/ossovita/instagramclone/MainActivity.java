package com.ossovita.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener {

    Boolean signUpModeActive = true;
    TextView loginTextView;
    EditText usernameEditText,passwordEditText;
    ConstraintLayout backgroundLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginTextView = findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(this);
        usernameEditText = findViewById(R.id.usernameText);
        passwordEditText = findViewById(R.id.passwordText);
        ImageView logoImageView = findViewById(R.id.logoImageView);
        backgroundLayout = findViewById(R.id.backgroundLayout);
        passwordEditText.setOnKeyListener(this);

        if(ParseUser.getCurrentUser()!=null){//hali hazırda giriş yapmış kullanıcı varsa direkt diğer aktiviteye gönder
            showUserList();
        }




        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

    public void showUserList(){
        finish();
        startActivity(new Intent(MainActivity.this,UserListActivity.class));
    }

    public void signUpClicked(View v){

        if(usernameEditText.getText().toString().matches("")||passwordEditText.getText().toString().matches("")){
            Toast.makeText(this, "A username and a password are required.", Toast.LENGTH_SHORT).show();
        }else{
            if(signUpModeActive){//signup'a basıldıysa
                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){//kaydolursa diğer aktiviteye gönder
                            showUserList();
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{//login'e basıldıysa
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null){//başarılıyla giriş yaparsa diğer aktiviteye gönder
                            showUserList();
                        }else{
                            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loginTextView) {

            Button signUpButton = findViewById(R.id.signUpButton);

            if (signUpModeActive) {
                signUpModeActive = false;
                signUpButton.setText("Login");
                loginTextView.setText("or, Sign Up");
            } else {
                signUpModeActive = true;
                signUpButton.setText("Sign Up");
                loginTextView.setText("or, Login");
            }
        }else if(view.getId()==R.id.logoImageView||view.getId()==R.id.backgroundLayout){
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i==KeyEvent.KEYCODE_ENTER&&keyEvent.getAction()==KeyEvent.ACTION_DOWN){
            signUpClicked(view);
        }
        return false;
    }
}