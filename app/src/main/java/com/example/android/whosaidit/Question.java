package com.example.android.whosaidit;


import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;

/**
 * In order to save the state of variables and interface elements I needed to handle onSaveInstanceState, and
 * to save the array of questions and answers.  However I used a custom class to store my question and answer
 * data and for that I need to use putParcelable, so the class needs to implement Parcelable.
 * Source: https://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-parcelable#7181792
 */

public class Question   {
    String question = "";
    String answer = "";

    public void setQuestion(String string_question){
        question = string_question;
    }

    public void setAnswer(String string_answer){
        answer = string_answer;
    }


    /**protected Question(Parcel in) {
        question = in.readString();
        answer = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    /
    public static Question[] toMyObjects(Parcelable[] parcelables) {
        if (parcelables == null)
            return null;
        return Arrays.copyOf(parcelables, parcelables.length, Question[].class);
    }
    **/
}