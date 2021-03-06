package com.example.newnewhellokittyquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.example.newnewhellokittyquiz.R

private const val EXTRA_ANSWER_IS_TRUE = "com.nikita.android.NewNewHelloKittyQuiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.nikita.android.NewNewHelloKittyQuiz.answer_shown"

class CheatActivity : AppCompatActivity() {
    private lateinit var answerTextView: TextView;
    private lateinit var showAnswerButton: Button;

    private var answerIsTrue = false;

    private var didCheat = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        answerTextView = findViewById(R.id.answer_text_view);
        showAnswerButton = findViewById(R.id.show_answer_button);
        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button;
                else -> R.string.false_button;
            }
            answerTextView.setText(answerText);
            setAnswerShownResult(true);
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data);
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean(EXTRA_ANSWER_SHOWN, didCheat);
    }




    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
            }
        }
    }


}