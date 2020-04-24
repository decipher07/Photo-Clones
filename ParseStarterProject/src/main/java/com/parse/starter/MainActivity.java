/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {


    Button signUpButton ;
    EditText usernameEditText ;
    EditText passwordEditText ;
    TextView loginTextView ;
    ImageView logoImageView ;
    RelativeLayout backgroundLayout ;

    Boolean signUpModeActive = true ;

    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            Log.i("Enter", "Clicked");
            signUpClicked(view);
        }

        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loginTextView){

            if (signUpModeActive){
                signUpModeActive = false;
                signUpButton.setText("LOGIN");
                loginTextView.setText("Or, Sign Up");
            }else {
                signUpModeActive = true;
                signUpButton.setText("SIGN UP");
                loginTextView.setText("Or, Login");
            }

        } else if (view.getId() == R.id.logoImageView || view.getId() == R.id.backgroundLayout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0 );
        }
    }

  public void signUpClicked (View view){

    if (usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")){
      Toast.makeText(this, "Please Enter Proper Username and Password", Toast.LENGTH_SHORT).show();
    } else {
        if (signUpModeActive) {
            ParseUser user = new ParseUser();
            user.setUsername(usernameEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("Sign-Up", "Success");
                        showUserList();
                    }else
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Login Situation
            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null){
                        Log.i("Login" , "OK");
                        showUserList();
                    } else {
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

    setTitle("Photo Clone");

    signUpButton = (Button) findViewById(R.id.signUpButton);
    usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    loginTextView = (TextView) findViewById(R.id.loginTextView);
    logoImageView = (ImageView) findViewById(R.id.logoImageView);
    backgroundLayout = (RelativeLayout) findViewById(R.id.backgroundLayout);

    backgroundLayout.setOnClickListener(this);
    loginTextView.setOnClickListener(this);
    passwordEditText.setOnKeyListener(this);
    logoImageView.setOnClickListener(this);

    if(ParseUser.getCurrentUser() != null)
        showUserList();

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}


/*

      ParseUser.logOut();

    if (ParseUser.getCurrentUser() != null)
      Log.i("Signed iN ", ParseUser.getCurrentUser().getUsername());
    else
      Log.i("Not Signed in", "Nope");
    ParseUser user = new ParseUser() ;

    user.setUsername("dj");
    user.setPassword("myPass");

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null){
          Log.i("SignUp is Ok !", "Work Done is F*s");
        } else {
          e.printStackTrace();
        }
      }
    });


    ParseUser.logInInBackground("dj", "myPas", new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if (user != null){
          Log.i("Success", "Logged IN");
        }
        else{
          e.printStackTrace();
        }
      }
    });


    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

    //query.whereGreaterThan("score", 0);

    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        if (e == null || objects.size() != 0  ){
          for (ParseObject score : objects){
            score.put("score", score.getInt("score") + 20 );
            score.saveInBackground();
            Log.i("Done", "Done");
          }
        }
        else {
          Log.i("Failure","Failed !");
        }
      }

    });



    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
//
//    query.whereEqualTo("username", "DJ");
//    query.setLimit(1);

    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        if (e == null){
          if (objects.size() > 0)
            for(ParseObject object : objects){
//              if (object.getInt("score") >= 80)
//                object.put("score", object.getInt("score") + 20 );
              Log.i("username",object.getString("username"));
              Log.i("score", Integer.toString(object.getInt("score")));
            }
        }
        else {
          e.printStackTrace();
        }
      }
    });

//    ParseObject tweet = new ParseObject("Tweet");
//    tweet.put("username", "decipher08");
//    tweet.saveInBackground(new SaveCallback() {
//      @Override
//      public void done(ParseException e) {
//        if (e == null)
//          Log.i("Message", "Success");
//        else
//          Log.i("Message", "Failed");
//      }
//    });

    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");

    query.getInBackground("Q7ft6XbEwX", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {
        if (e == null && object != null){
          Log.i("Username", object.getString("username"));
          object.put("Username", "Workdabel");
          object.saveInBackground();
        }else
          Log.i("Message", "Failed");
      }

    });


    ParseObject score = new ParseObject("Score");
    score.put("username", "DJ");
    score.put("score", 21);
    score.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null)
            Log.i("Message", "Success");
        else
          e.printStackTrace();
      }
    });


    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

    query.getInBackground("FemZRYp6hh", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {
        if (e == null && object != null){

          object.put("score", 85);
          object.saveInBackground();

          Log.i("username", object.getString("username"));
          Log.i("score", Integer.toString(object.getInt("score")));
        }
      }
    });

    */

