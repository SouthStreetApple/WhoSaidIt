package com.example.android.whosaidit;

import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.content.res.Configuration;
import android.icu.util.DateInterval;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    /**
     * Variables that allow us to capture enter or GO in  the keyboard and a button click.
     * Welcome Screen
     */
    EditText nameInput;
    Button startQuizButton;
    /**
     * Variables that allow us to calculate how much time has passed.
     */
    Long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Create hook for 'Enter' on name_input, on welcome layout
         */
        nameInput = (EditText) findViewById(R.id.name_input);
        startQuizButton = (Button) findViewById(R.id.start_quiz);

       nameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    startQuizButton.performClick();
                }
                return false;
            }
        });


    }

    public void startQuiz(View view){
        /**
         * Hide the keyboard
         *https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
         */
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        /**
         * This part of the code saves the current time, so we can calculate how long it took the user to complete the quiz.
         */
        startTime = ZonedDateTime.now().toEpochSecond();
        Toast t =  Toast.makeText(this,"GET READY!",Toast.LENGTH_SHORT);
        t.show();
        /**
         * Using intents to create new window.
         * Sends variables along to next window
         */
        Intent i = new Intent(this, Quiz.class);
        i.putExtra("startTime",startTime);
        i.putExtra("userName",nameInput.getText().toString());
        this.startActivity(i);
    }







}




