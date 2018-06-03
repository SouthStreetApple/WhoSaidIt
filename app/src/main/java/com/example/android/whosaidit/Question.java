package com.example.android.whosaidit;

/**
 * Simple little class that lets me create an array of answers and questions so that I can
 * easily pull the associated questions and answers with a simple index number. Keeps everything
 * in order so that we don't have to worry about questions being out of order, and allows
 * everything to be dynamic so that we can add/remove questions as will in the resource file.
 */

public class Question {
    String question = "";
    String answer = "";

    public void setQuestion(String string_question) {
        question = string_question;
    }

    public void setAnswer(String string_answer) {
        answer = string_answer;
    }
}