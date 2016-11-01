package com.example.com.exam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private TextView name;
    private TextView age;
    private TextView grade;
    private String nameS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        name  = (TextView) findViewById(R.id.name);
        age   = (TextView) findViewById(R.id.age);
        grade = (TextView) findViewById(R.id.grade);

        nameS         = getIntent().getStringExtra("name");
        String ageS   = getIntent().getStringExtra("age");
        String gradeS = getIntent().getStringExtra("grade");



        name.setText(nameS);
        age.setText(ageS);
        grade.setText(gradeS);
    }

    public void doLogOff(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", nameS);
        String rem = getIntent().getStringExtra("rem");
        intent.putExtra("rem", rem);
        startActivity(intent);
    }
}
