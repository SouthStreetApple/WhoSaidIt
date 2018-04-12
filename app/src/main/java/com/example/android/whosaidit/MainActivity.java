package com.example.android.whosaidit;

import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.icu.util.DateInterval;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
     * Load some values from Strings file
     */

    int numberOfQuestions;

    /**
     * Variables that allow us to capture enter or GO in  the keyboard and a button click.
     */
    EditText nameInput;
    Button startQuizButton;

    /**
     * Variables that allow us to calculate how much time has passed.
     */
    ZonedDateTime startTime;
    ZonedDateTime endTime;
    private Question question;

    /**
     * Current Question and Answer Variables
     *
     */
    Question questions[];
    int questionIndex;
    int questionsCorrect;
    float firstRoundScore;
    String currentQuestion;
    String currentAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Create hook for 'Enter' on name_input
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

       numberOfQuestions = Integer.valueOf(getString(R.string.total_questions));
       questions = new Question[numberOfQuestions];

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
        startTime = ZonedDateTime.now();
        Toast t =  Toast.makeText(this,"GET READY!",Toast.LENGTH_SHORT);
        t.show();

        /**
         * Calls function that loads the questions and answers
         */
        loadQuestions();

        /**
         * This function hides the first linear layout and adds the one from the quiz layout
         * welcome_layout is our activity_main (the first screen you see when loading the app).
         * We first hide welcome_layout so that we don't see it.
         * main_layout is the parent View of the welcome_layout, we will be adding the quiz layout from the quiz layout file.
         * The below link is where we got the cool inflate trick below which allows us to insert the layout into our main_layout
         * https://stackoverflow.com/questions/16812276/how-to-initialize-a-ui-component-from-a-layout-file#16812431
         */

        LinearLayout welcome_layout = (LinearLayout) findViewById(R.id.welcome_layout);
        welcome_layout.setVisibility(View.GONE);
        LinearLayout main_layout = (LinearLayout) findViewById(R.id.main_layout);
        LinearLayout quiz_layout = (LinearLayout)getLayoutInflater().inflate(R.layout.quiz,main_layout,true);

        /**
         *
         * Load the first question and possible answers into the available TextViews/OptionBoxes
         *
         */
        setQuestion(0);

    }

    private void setQuestion(int i){
        //Sets the next question, changes interface and sets appropriate variables.
        currentQuestion = questions[i].question;
        currentAnswer = questions[i].answer;
        questionIndex = i;
        //change question text on the view
        TextView t = (TextView) findViewById(R.id.quiz_quote);
        t.setText("''" +  currentQuestion  + "''");
        //Change answer options
        RadioButton option1 = (RadioButton) findViewById(R.id.option1);
        RadioButton option2 = (RadioButton) findViewById(R.id.option2);
        option1.setText(getString(R.string.option1).toString());
        option2.setText(getString(R.string.option2).toString());
    }

    public void nextQuestion(View view)  {
        /**
         * This will check to see if the answer is correct, display if it is
         * then will display the next question.
         * If there is NO next question, then it will grade the quiz and display
         * results.
         */
        Button checkButton = (Button) findViewById(R.id.check_button);
        String buttonText = checkButton.getText().toString();
        TextView answerResult = (TextView) findViewById(R.id.answer_result);
        RadioGroup possibleAnswers = (RadioGroup) findViewById(R.id.possible_answers);
        //Check to make sure they selected an answer
        if (possibleAnswers.getCheckedRadioButtonId() == -1)
        {
            // Exit this
            return;
        }
        //define radiobutton
        RadioButton r;
        //define variable to hold ID of radiobutton
        int answerID;
        answerID = possibleAnswers.getCheckedRadioButtonId();
        r = findViewById(answerID);

        if (buttonText == getString(R.string.submit_button)) {
            /*TextView answerResult = (TextView) findViewById(R.id.answer_result);*/
            if (r.getText() == currentAnswer){
                //They got the answer right.

                //Add 1 to questionsCorrect so we can calculate score.
                questionsCorrect++;
                //Show that the answer is correct!
                answerResult.setText(getString(R.string.answer_result_correct));
                checkButton.setText(getString(R.string.submit_button_next));

            } else {
                //They got the answer wrong.
                answerResult.setText(getString(R.string.answer_result_incorrect));
                checkButton.setText(getString(R.string.submit_button_next));

            }

        } else {
            //The button has now changed to 'Next Question'
            //Load Next Question and answer or grade.

            //Check to see if we have done our last question.
            if ((questionIndex+1) >= numberOfQuestions) {
                //First Round is over
                //Save score from first round.
                firstRoundScore = ((float)((float)questionsCorrect/(float)numberOfQuestions)*100);
                //Start bonus round!
                bonusRound(view);

                //displayResult(); <- This will be used at the end.
            } else {
                //Let's load the next question.
                //Maybe have some kind of fade out fade in here?
                questionIndex++;
                setQuestion(questionIndex);
                //Set button text back to original state.
                checkButton.setText(getString(R.string.submit_button));
                //Clear out the answer result text.
                answerResult.setText("");
               //Uncheck the RadioButton
                possibleAnswers.clearCheck();
                //Update x/Total question count
               TextView whichQuestion = (TextView) findViewById(R.id.question_count);
                whichQuestion.setText((questionIndex+1) + "/" + numberOfQuestions);
            }
        }





    }

    private void bonusRound(View view){
        /**
         * This function hides the quiz layout and adds the one from the bonus
         * quiz_layout is our quiz.xml (quiz questions).
         * We first hide quiz so that we don't see it.
         * main_layout is the parent View of the quiz layout, we will be adding the bonus layout from the bonus.xml layout file.
         * The below link is where we got the cool inflate trick below which allows us to insert the layout into our main_layout
         * https://stackoverflow.com/questions/16812276/how-to-initialize-a-ui-component-from-a-layout-file#16812431
         */

        LinearLayout quiz_layout = (LinearLayout) findViewById(R.id.quiz_layout);
        quiz_layout.setVisibility(View.GONE);
        LinearLayout main_layout = (LinearLayout) findViewById(R.id.main_layout);
        LinearLayout bonus_layout = (LinearLayout)getLayoutInflater().inflate(R.layout.bonus,main_layout,true);
        //We've now switched to the bonus screen!

    }


    private void displayResult(){
        /*This calculates the score based on percentage the user got right divided by total number of questions
         *then it displays in a toast the result in a nice simple message.  Also tells the user how many
         * seconds it took!
         *
         * DEPRECIATED FOR NOW
         */
        endTime = ZonedDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        String seconds = String.valueOf(duration.toMillis()/1000);
        String score = String.valueOf((float)((float)questionsCorrect/(float)numberOfQuestions)*100);
        TextView name = (TextView) findViewById(R.id.name_input);
        String resultText = "You finished! You scored " + score + "% in " + seconds + " seconds, congratulations " + name.getText().toString() + "!";
        Toast resultToast = Toast.makeText(this,resultText,Toast.LENGTH_LONG);
        resultToast.show();
    }

    private void loadQuestions(){
        //Loading questions and answers into array
        /**
         * Special Thanks to @Neural & @BigMikeDog from the Grow with Google Scholarship Slack.
         * They were instrumental in helping me debug my array usage and getIdentifier issues.
         * All the questions and answers are stored as strings in a resource file.  This loads them
         * in sequence into an array with a custom type/class that will allow us to call
         * each Question answer combination with an index of 0-numberOfQuestions
         */

        int x;
        String questionID;
        String answerID;
        int resID;
        for (x=0;x<numberOfQuestions;x++){
            questionID = "q" + (x+1);
            answerID = "a" + (x+1);
            questions[x] = new Question();
            /*Grab Answer*/
            resID = getResources().getIdentifier(answerID,"string",getPackageName());
            questions[x].setAnswer(getString(resID));

            /*Grab Question*/
            resID = getResources().getIdentifier(questionID,"string",getPackageName());
            questions[x].setQuestion(getString(resID));


            Log.e("LOADING ARRAY",questions[x].question);
            Log.e("LOADING ARRAY",questions[x].answer);
        }
        Log.e("TOTAL", String.valueOf(questions.length));
    }
}




