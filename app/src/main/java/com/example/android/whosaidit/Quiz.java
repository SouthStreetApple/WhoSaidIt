package com.example.android.whosaidit;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class Quiz extends Activity {

    /**
     * Current Question and Answer Variables
     */
    int numberOfQuestions;
    Question questions[];
    int questionIndex;
    int questionsCorrect;
    float firstRoundScore;
    String currentQuestion;
    String currentAnswer;

    /**
     * Variable that holds our startTime
     */
    Long startTime;
    /**
     * Holds the users name
     */
    String userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        /**
         * Loads variables from first screen
         */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Variables
            userName = extras.getString("userName");
            startTime = extras.getLong("startTime");
        }

        /**
         * Load the question data
         */
        numberOfQuestions = Integer.valueOf(getString(R.string.total_questions));
        questions = new Question[numberOfQuestions];
        /**
         * Calls function that loads the questions and answers
         */
        loadQuestions();

        /**
         *
         * Load the first question and possible answers into the available TextViews/OptionBoxes
         *
         */
        setQuestion(0);
    }

    /**
     * This is where is make sure to 'save' our current variables and interface look so that
     * it reloads correctly on screen rotate.
     * Idea from: Miriam E. (ABND) (Slack)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("numberOfQuestions", numberOfQuestions);
        outState.putInt("questionIndex", questionIndex);
        outState.putInt("questionsCorrect", questionsCorrect);
        outState.putFloat("firstRoundScore", firstRoundScore);
        outState.putString("currentQuestion", currentQuestion);
        outState.putString("currentAnswer", currentAnswer);
        outState.putLong("startTime", startTime);
        outState.putString("userName", userName);
        //Saves button text from current view as this is dynamic
        Button checkButton = (Button) findViewById(R.id.check_button);
        outState.putString("buttonText", checkButton.getText().toString());
        //Saves answer text from current view as this is dynamic
        TextView answerResult = (TextView) findViewById(R.id.answer_result);
        outState.putString("answerResult", answerResult.getText().toString());
    }

    /**
     * Loads the saved state
     * Idea from: Miriam E. (ABND) (Slack)
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfQuestions = savedInstanceState.getInt("numberOfQuestions");
        questionIndex = savedInstanceState.getInt("questionIndex");
        questionsCorrect = savedInstanceState.getInt("questionsCorrect");
        firstRoundScore = savedInstanceState.getFloat("firstRoundScore");
        currentQuestion = savedInstanceState.getString("currentQuestion");
        currentAnswer = savedInstanceState.getString("currentAnswer");
        startTime = savedInstanceState.getLong("startTime");
        userName = savedInstanceState.getString("userName");
        //Load Questions into memory
        loadQuestions();
        /**
         * Load interface information.  Supposedly this is suppose to be persistent if the
         * controls (views) have ID's but in practice this seems not to be the case.
         */
        //Run setQuestion(questionIndex); This will load the question based on the index, and the
        //answer options.
        setQuestion(questionIndex);
        //Make sure the the button text is correct, what if they already clicked it?
        Button checkButton = (Button) findViewById(R.id.check_button);
        checkButton.setText(savedInstanceState.getString("buttonText"));
        //If they already checked the answer, we need to make sure if the result is showing.
        TextView answerResult = (TextView) findViewById(R.id.answer_result);
        answerResult.setText(savedInstanceState.getString("answerResult"));
        //Make sure the question counter is correct
        TextView whichQuestion = (TextView) findViewById(R.id.question_count);
        whichQuestion.setText((questionIndex + 1) + "/" + numberOfQuestions);
    }

    private void setQuestion(int i) {
        //Sets the next question, changes interface and sets appropriate variables.
        currentQuestion = questions[i].question;
        currentAnswer = questions[i].answer;
        questionIndex = i;
        //change question text on the view
        TextView t = (TextView) findViewById(R.id.quiz_quote);
        t.setText("''" + currentQuestion + "''");
        //Change answer options
        RadioButton option1 = (RadioButton) findViewById(R.id.option1);
        RadioButton option2 = (RadioButton) findViewById(R.id.option2);
        option1.setText(getString(R.string.option1).toString());
        option2.setText(getString(R.string.option2).toString());
    }

    public void nextQuestion(View view) {
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
        if (possibleAnswers.getCheckedRadioButtonId() == -1) {
            // Exit this
            return;
        }
        //define radiobutton
        RadioButton r;
        //define variable to hold ID of radiobutton
        int answerID;
        answerID = possibleAnswers.getCheckedRadioButtonId();
        r = findViewById(answerID);
        if (buttonText.equalsIgnoreCase(getString(R.string.submit_button))) {
            if (r.getText() == currentAnswer) {
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
            if ((questionIndex + 1) >= numberOfQuestions) {
                //First Round is over
                //Save score from first round.
                firstRoundScore = ((float) ((float) questionsCorrect / (float) numberOfQuestions) * 100);
                //Start bonus round!
                bonusRound(view);
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
                whichQuestion.setText((questionIndex + 1) + "/" + numberOfQuestions);
            }
        }

    }

    private void bonusRound(View view) {
        /**
         * Use intent to load Bonus screen, and pass current variable values?
         */
        Intent i = new Intent(this, Bonus.class);
        i.putExtra("firstRoundScore", firstRoundScore);
        i.putExtra("startTime", startTime);
        i.putExtra("userName", userName);
        this.startActivity(i);
    }

    private void loadQuestions() {
        //Loading questions and answers into array
        /**
         * Special Thanks to @Neural & @BigMikeDog from the Grow with Google Scholarship Slack.
         * They were instrumental in helping me debug my array usage and getIdentifier issues.
         * All the questions and answers are stored as strings in a resource file.  This loads them
         * in sequence into an array with a custom type/class that will allow us to call
         * each Question answer combination with an index of 0-numberOfQuestions
         * All this allows for extremely easy updating of the questions
         */
        int x;
        String questionID;
        String answerID;
        int resID;
        for (x = 0; x < numberOfQuestions; x++) {
            questionID = "q" + (x + 1);
            answerID = "a" + (x + 1);
            questions[x] = new Question();
            /*Grab Answer*/
            resID = getResources().getIdentifier(answerID, "string", getPackageName());
            questions[x].setAnswer(getString(resID));

            /*Grab Question*/
            resID = getResources().getIdentifier(questionID, "string", getPackageName());
            questions[x].setQuestion(getString(resID));
            Log.e("LOADING ARRAY", questions[x].question);
            Log.e("LOADING ARRAY", questions[x].answer);
        }
        Log.e("TOTAL", String.valueOf(questions.length));
    }

}
