package com.example.newnewhellokittyquiz

import Question
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.newnewhellokittyquiz.R

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var prevButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var questionText: TextView
    private lateinit var cheatButton: Button;

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java);
    }

    private val TAG = "MainActivity";
    private val keyIndex = "index";
    private val answerIndex = "answers"
    private val questionIndex = "questions"

    private var questionAnswered = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(keyIndex, 0) ?: 0;
        quizViewModel.currentIndex = currentIndex;

        trueButton=findViewById(R.id.true_button)
        falseButton=findViewById(R.id.false_button)
        prevButton=findViewById(R.id.prev_button)
        nextButton=findViewById(R.id.next_button)
        questionText=findViewById(R.id.question_text)

        cheatButton=findViewById(R.id.cheat_button);

        var getResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()) {
                if(it.resultCode == RESULT_OK) {
                    quizViewModel.isCheater = it.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false;
                    quizViewModel.cheatedList[quizViewModel.currentIndex] = quizViewModel.isCheater;
                }
            }

        fun updateQuestions() {
            val id = quizViewModel.currentQuestionText;
            questionText.setText(id);
            questionAnswered = false;
        }

        fun checkAnswer(userAnswer: Boolean) {
            val correctAnswer = quizViewModel.currentQuestionAnswer;

            if (quizViewModel.cheatedList[quizViewModel.currentIndex]) {
                var toastText = Toast.makeText(this, R.string.cheated_toast, Toast.LENGTH_LONG);
                toastText.show()
            } else if (userAnswer == correctAnswer) {
                var toastText = Toast.makeText(this, R.string.correct_toast,Toast.LENGTH_LONG);
                toastText.setGravity(Gravity.TOP,0,0);
                toastText.show();
                quizViewModel.numCorrectAnswers += 1;

            } else {
                var toastText = Toast.makeText(this, R.string.incorrect_toast,Toast.LENGTH_LONG);
                toastText.setGravity(Gravity.TOP,0,0);
                toastText.show();

            }

            quizViewModel.questionCounter++;
            if (quizViewModel.questionCounter == quizViewModel.questionListSize) {
                var finalScore = (quizViewModel.numCorrectAnswers.toDouble() / quizViewModel.questionListSize) * 100;
                var finalScoreString = "%.2f".format(finalScore);
                var toastText = Toast.makeText(this, "Final Score: $finalScoreString%", Toast.LENGTH_LONG);
                toastText.setGravity(Gravity.TOP,0,0);
                toastText.show();
                quizViewModel.numCorrectAnswers = 0;

                quizViewModel.cheatedList[0] = false;
                quizViewModel.cheatedList[1] = false;
                quizViewModel.cheatedList[2] = false;
                quizViewModel.cheatedList[3] = false;
            }
        }

        updateQuestions()

        trueButton.setOnClickListener {
            if (!questionAnswered) {
                checkAnswer(true);
                questionAnswered = true;
            }
        }

        falseButton.setOnClickListener {
            if (!questionAnswered) {
                checkAnswer(false);
                questionAnswered = true;
            }

        }

        prevButton.setOnClickListener {
            quizViewModel.prevQuestion();
            updateQuestions();
        }

        nextButton.setOnClickListener {
            quizViewModel.nextQuestion();
            updateQuestions()
        }

        questionText.setOnClickListener {
            quizViewModel.nextQuestion();
            updateQuestions()
        }


        cheatButton.setOnClickListener {
            //If Pressed, change Activity to CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer;
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            getResult.launch(intent);

        }
    }

    override fun onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    override fun onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    override fun onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    override fun onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    override fun onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    override fun onSaveInstanceState (savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(keyIndex, quizViewModel.currentIndex);
        savedInstanceState.putInt(answerIndex, quizViewModel.numCorrectAnswers);
        savedInstanceState.putInt(questionIndex, quizViewModel.questionCounter);
    }
}