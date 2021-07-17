package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
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
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean signupModeActive =true;
    TextView changeSignupModeTextView;
    EditText passwordEditText ;
public void showUserList(){
    Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
    startActivity(intent);
}

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.changeSignupModeTextView){

            Button signupButton = (Button)findViewById(R.id.signupButton);

            if(signupModeActive){
                signupModeActive=false;
                signupButton.setText("Login");
                changeSignupModeTextView.setText("or, Signup");
            }else{
                signupModeActive=true;
                signupButton.setText("Sign up");
                changeSignupModeTextView.setText("or, Login");
            }
        }else if(v.getId()==R.id.backgroundConstraintLayout || v.getId()==R.id.logoImageView ) {
            //for keyboard
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() ,0);
        }
    }

    public void signUp(View view){

        EditText usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();
        }else {
            if(signupModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("SignUp", "Successful");
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null){
                            Log.i("Signup","Loin Successful");
                            showUserList();
                        }else{
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Instagram");

        ConstraintLayout backgroundConstraintLayout =(ConstraintLayout)findViewById(R.id.backgroundConstraintLayout);
        ImageView logoImageView =(ImageView)findViewById(R.id.logoImageView);
        changeSignupModeTextView= (TextView)findViewById(R.id.changeSignupModeTextView);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        changeSignupModeTextView.setOnClickListener(this); //min17:44

        backgroundConstraintLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);


        //for keyboard
        passwordEditText.setOnKeyListener(this);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        if (ParseUser.getCurrentUser() != null){
            showUserList();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode== event.KEYCODE_ENTER && event.getAction()==event.ACTION_DOWN){
            // if the enter key has been pressed we're going to implement the Sign-Up method.
            signUp(v);
        }
        return false;
    }
}