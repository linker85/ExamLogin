package com.example.com.exam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.com.exam.parser.MyParser;
import com.example.com.exam.parser.User;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG_";

    private EditText user;
    private EditText pass;
    private boolean  canLogin;
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);

        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.password);
        rememberMe = (CheckBox) findViewById(R.id.remember);
        canLogin   = false;
        String rem = null;
        String defaultValue = null;
        String name2 = null;
        try {
            defaultValue = getIntent().getStringExtra("rem");
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            name2 = sharedPref.getString("name", "");
        } catch (Exception e) {

        }
        if (defaultValue != null && defaultValue.equals("1")) {
            Log.d(TAG, "restore: ");
            user.setText(name2);
            user.setText(getIntent().getStringExtra("name"));
            rememberMe.setChecked(true);
        } else {
            Log.d(TAG, "do not restore: ");
            user.setText("");
            pass.setText("");
            rememberMe.setChecked(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        String rem = null;
        String defaultValue = null;
        String name2 = null;
        try {
            rem = getIntent().getStringExtra("rem");
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            defaultValue = sharedPref.getString("rem", "0");
            name2 = sharedPref.getString("name", "");
        } catch (Exception e) {

        }
        if (defaultValue != null && defaultValue.equals("1")) {
            Log.d(TAG, "restore: ");
            user.setText(getIntent().getStringExtra("name"));
            user.setText(name2);
            rememberMe.setChecked(true);
        } else {
            Log.d(TAG, "do not restore: ");
            user.setText("");
            pass.setText("");
            rememberMe.setChecked(false);
        }
    }

    public void doLogin(View view) {
        getJSON();
    }


    private void getJSON() {
        // because its running on the main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://www.mocky.io/v2/57a4dfb40f0000821dc9a3b8");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        } catch( Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        Log.d(TAG, "getJSON: " + result.toString());
        try {
            MyParser myParser = new MyParser();
            List<User> users = myParser.parseMagic(result.toString());
            Log.d(TAG, "getJSON: " + users.toString());
            if (users != null && !users.isEmpty()) {
                for (User u : users) {
                    Log.d(TAG, "user.getText(): |" + user.getText() + "|, pass.getText(): |" + pass.getText() + "|, u.getName(): |" + u.getName() + "|, u.getPassword(): |" + u.getPassword() +"|");
                    Log.d(TAG, "user.getText()>: " + (user.getText().toString().equals(u.getName())) + ", pass.getText(): " + (pass.getText().toString().equals(u.getPassword())));

                    if (user.getText().toString().equals(u.getName()) && pass.getText().toString().equals(u.getPassword())) {
                        Log.d(TAG, "login");
                        canLogin = true;
                        Intent intent = new Intent(this, Main2Activity.class);
                        intent.putExtra("name", u.getName());
                        intent.putExtra("age", "" + u.getAge());
                        intent.putExtra("grade", "" + u.getGrade());

                        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();



                        if (rememberMe.isChecked()) {
                            intent.putExtra("rem", "1");
                            editor.putString("rem", "1");
                            editor.putString("name", u.getName());
                        } else {
                            intent.putExtra("rem", "0");
                            editor.putString("rem", "0");
                            editor.putString("name", "");
                        }
                        editor.commit();
                        startActivity(intent);
                        break;
                    }
                }
            } else {
                canLogin = false;
                Log.d(TAG, "userNotFound: ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}