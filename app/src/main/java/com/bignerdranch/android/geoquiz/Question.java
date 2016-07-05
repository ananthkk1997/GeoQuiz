package com.bignerdranch.android.geoquiz;

/**
 * Created by Krishnak97 on 6/5/2016.
 */
public class Question {
    private int mTextResId; // member variable
    private boolean mAnswerTrue; // member variables

    public Question(int textResId, boolean answerTrue) {
        this.mTextResId = textResId;
        this.mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int mTextResId) {
        this.mTextResId = mTextResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean mAnswerTrue) {
        this.mAnswerTrue = mAnswerTrue;
    }
}
