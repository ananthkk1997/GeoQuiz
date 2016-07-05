package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;


public class QuizActivity extends AppCompatActivity {

    public static final String TAG = "QuizActivity";
    public static final int REQUEST_CODE_CHEAT = 0;
    

    private Button mTrueButton; //m naming convention for member variables
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    // saved variable
    private int mCurrentIndex = 0;
    private static final String mCurrentIndexKey = "mCurrentIndex";

    // saved variable
    private boolean mIsCheater;
    private static final String mIsCheaterKey = "mIsCheater";

    //set of all questions the user cheated on
    private HashSet<Integer> mCheatedQuestions = new HashSet<>();
    private static final String mCheatedQuestionsKey = "mCheatedQuestions";

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        //set of all questions the user cheated on
        mCheatedQuestions = new HashSet<>();

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // what the button will do when clicked
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // what the button will do when clicked
                checkAnswer(false);
            }
        });

        //takes user to next question
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // loops the questions
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

                //resets cheating boolean each next question
                mIsCheater = false;
                updateQuestion();
            }
        });

        //takes user to previous question
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex == 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                } else {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                }
                updateQuestion();
            }
        });

        //click this button to start CheatActivity
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent startCheatActivity = CheatActivity.newIntent(
                        QuizActivity.this, answerIsTrue);

                // because we want to hear back from CheatActivity (if cheating occured)
                startActivityForResult(startCheatActivity, REQUEST_CODE_CHEAT);
            }
        });

        //get saved variables from last destroy
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(mCurrentIndexKey);
            mIsCheater = savedInstanceState.getBoolean(mIsCheaterKey);
            mCheatedQuestions = (HashSet<Integer>) savedInstanceState.getSerializable(mCheatedQuestionsKey);
        }
        updateQuestion();
    }

    @Override
    protected void onSaveInstanceState(Bundle onState) {
        super.onSaveInstanceState(onState);

        //get saved variables
        onState.putInt(mCurrentIndexKey, mCurrentIndex);
        onState.putBoolean(mIsCheaterKey, mIsCheater);
        onState.putSerializable(mCheatedQuestionsKey, mCheatedQuestions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            if (mIsCheater) {
                mCheatedQuestions.add(mCurrentIndex);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy (){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        // updates TextView text to question at mCurrentIndex
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        // checks user answer vs question answer
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater || mCheatedQuestions.contains(mCurrentIndex)) {
            messageResId = R.string.judgement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
