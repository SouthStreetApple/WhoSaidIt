package com.example.android.whosaidit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.ZonedDateTime;

public class Bonus extends Activity {

    /**
     * Variables that allow us to capture enter or GO in the keyboard and a button click.
     * Bonus Screen
     */
    EditText bonusQuestion2;
    Button checkButtonBonus;

    /**
     * Score from first round
     */
    float firstRoundScore = 0;

    /**
     * Holds our start time
     */
    Long startTime;
    /**
     * Holds the users name
     */
    String userName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bonus);

        /**
         * Loads variables from first screen
         */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Variables
            userName = extras.getString("userName");
            startTime = extras.getLong("startTime");
            firstRoundScore = extras.getFloat("firstRoundScore");
        }

        /**
         * Create hook for 'Enter' on bonus_answer_2, on bonus layout
         */
        bonusQuestion2 = (EditText) findViewById(R.id.bonus_answer_2_et);
        checkButtonBonus = (Button) findViewById(R.id.check_button_bonus);
        bonusQuestion2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    checkButtonBonus.performClick();
                }
                return false;
            }
        });
    }

    public void displayResult(View view) {
        view.setEnabled(false);
        /**
         * Hide the keyboard
         *https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
         */
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        /*
         *Check answers first.
         * Add 5 points (percentage) for each bonus question correct!
         *
         */
        //Bonus Question 1
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.bonus_checkbox1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.bonus_checkbox2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.bonus_checkbox3);
        if (checkBox1.isChecked() == getResources().getBoolean(R.bool.boptiona1) && checkBox2.isChecked() == getResources().getBoolean(R.bool.boptiona2) && checkBox3.isChecked() == getResources().getBoolean(R.bool.boptiona3)) {
            firstRoundScore = firstRoundScore + 5;
        }
        //Bonus Question 2
        EditText bonusAnswer2 = (EditText) findViewById(R.id.bonus_answer_2_et);
        if (bonusAnswer2.getText().toString().equalsIgnoreCase(getString(R.string.bqa2).toLowerCase())) {
            firstRoundScore = firstRoundScore + 5;
        }
        /*This calculates the score based on percentage the user got right divided by total number of questions
         *then it displays in a toast the result in a nice simple message (also takes into account
         * bonus points.  Also tells the user how many seconds it took!
         */
        Long endTime;
        endTime = ZonedDateTime.now().toEpochSecond();
        Long duration = (endTime - startTime);
        String seconds = String.valueOf(duration);
        String resultText = "You finished! You scored " + firstRoundScore + "% in " + seconds + " seconds, congratulations " + userName + "!";
        Toast resultToast = Toast.makeText(this, resultText, Toast.LENGTH_LONG);
        resultToast.show();
    }

    /**
     * This is where is make sure to 'save' our current variables and interface look so that
     * it reloads correctly on screen rotate.
     * Idea from: Miriam E. (ABND) (Slack)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Save the disabled state of the check_answer button
        Button check_answer = (Button) findViewById(R.id.check_button_bonus);
        outState.putBoolean("check_enabled",check_answer.isEnabled());
        //Save the state of the checkboxes
        CheckBox check_box_1 = (CheckBox) findViewById(R.id.bonus_checkbox1);
        outState.putBoolean("checkBox1",check_box_1.isChecked());
        CheckBox check_box_2 = (CheckBox) findViewById(R.id.bonus_checkbox2);
        outState.putBoolean("checkBox2",check_box_2.isChecked());
        CheckBox check_box_3 = (CheckBox) findViewById(R.id.bonus_checkbox3);
        outState.putBoolean("checkBox3",check_box_3.isChecked());
        //Save the state of the EditText
        EditText fillInTheBlank = (EditText) findViewById(R.id.bonus_answer_2_et);
        outState.putString("fillInTheBlank",fillInTheBlank.getText().toString());
    }
    /**
     * Loads the saved state
     * Idea from: Miriam E. (ABND) (Slack)
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //Load state of the check_answer button
        Button check_answer = (Button) findViewById(R.id.check_button_bonus);
        check_answer.setEnabled(savedInstanceState.getBoolean("check_enabled"));
        //Load the state of the checkboxes
        CheckBox check_box_1 = (CheckBox) findViewById(R.id.bonus_checkbox1);
        check_box_1.setChecked(savedInstanceState.getBoolean("checkBox1"));
        CheckBox check_box_2 = (CheckBox) findViewById(R.id.bonus_checkbox2);
        check_box_2.setChecked(savedInstanceState.getBoolean("checkBox2"));
        CheckBox check_box_3 = (CheckBox) findViewById(R.id.bonus_checkbox3);
        check_box_3.setChecked(savedInstanceState.getBoolean("checkBox3"));
        //Load the state of the EditText
        EditText fillInTheBlank = (EditText) findViewById(R.id.bonus_answer_2_et);
        fillInTheBlank.setText(savedInstanceState.getString("fillInTheBlank"));
    }

}
