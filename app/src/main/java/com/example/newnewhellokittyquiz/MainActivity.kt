package com.example.newnewhellokittyquiz

import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import android.media.MediaPlayer

// tags and indexes
private const val tag = "MainActivity"
private const val keyIndex = "index"
private const val answerIndex = "answers"
private const val questionIndex = "questions"

var cheated = mutableListOf<Int>()
var answered = mutableListOf<Int>()
var currentIndex = 0


class MainActivity : AppCompatActivity() {
    //create button objects
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button
    private lateinit var music : MediaPlayer

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        music = MediaPlayer.create(this, R.raw.hellokittytheme)
        music.setLooping(true)
        //music.start()

        val currentIndexSaved = savedInstanceState?.getInt(keyIndex, 0) ?: 0
        currentIndex = currentIndexSaved

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text)
        cheatButton = findViewById(R.id.cheat_button)

        fun updateQuestions(){
            val questionTextResId = quizViewModel.currentQuestionText
            questionTextView.setText(questionTextResId)
            quizViewModel.counter = 0
        }
        fun checkAnswer(userAnswer: Boolean){
            val correctAnswer = quizViewModel.currentQuestionAnswer

            //if user cheated on a question, prints out a message
            if(cheated.contains(currentIndex)){
                val judgementToast = Toast.makeText(this, R.string.cheater, Toast.LENGTH_SHORT)
                judgementToast.show()
            }

            if(userAnswer == correctAnswer) {
                val correctToast = Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_LONG)
                correctToast.setGravity(Gravity.TOP,0,150)
                correctToast.show()
                quizViewModel.correctAnswers++

            }else{
                val incorrectToast = Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_LONG)
                incorrectToast.setGravity(Gravity.TOP,0,150)
                incorrectToast.show()
            }
            quizViewModel.questionCount++
            if(quizViewModel.questionCount == quizViewModel.questionBankSize){
                val percentage = (quizViewModel.correctAnswers.toDouble() / quizViewModel.questionCount.toDouble() * 100).toInt()
                val gradeToast = Toast.makeText(this, "Your score is $percentage%", Toast.LENGTH_LONG)
                gradeToast.setGravity(Gravity.BOTTOM,0,150)
                gradeToast.show()
            }
        }

        questionTextView.setOnClickListener{
            currentIndex = (currentIndex + 1) % quizViewModel.questionBankSize
            updateQuestions()
        }

        trueButton.setOnClickListener{
            //If it is the first time visiting this question, checks if the question is true
            if(!(answered.contains(currentIndex))){
                checkAnswer(true)
                answered.add(currentIndex)
            }
        }

        falseButton.setOnClickListener {
            //If it is the first time visiting this question, checks if the question is false
            if(!(answered.contains(currentIndex))) {
                checkAnswer(false)
                answered.add(currentIndex)
            }
        }

        cheatButton.setOnClickListener{
            //If I press this button, I will go to the second activity
            //wrap your second activity into an intent

            val cheatToast = Toast.makeText(this, "NO CHEATING ALLOWED!", Toast.LENGTH_LONG)
            cheatToast.setGravity(Gravity.TOP,0,150)
            cheatToast.show()
            /*val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivity(intent)
            */
            //startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        updateQuestions()

        nextButton.setOnClickListener {
            //Cycle to the next question in the list
            quizViewModel.moveToNext()
            updateQuestions()
        }

        previousButton.setOnClickListener {
            //Cycle to the previous question in the list
            quizViewModel.moveToPrev()
            updateQuestions()
        }
    }

    // New, after create, log processes
    override fun onStart(){
        super.onStart()
        Log.d(tag, "onStart() is called")
    }

    override fun onPause() {
        super.onPause()
        music.stop()
        Log.d(tag, "onPause() is called")
    }

    override fun onResume() {
        super.onResume()
        music.start()
        Log.d(tag, "onResume() is called")
    }

    override fun onStop() {
        super.onStop()
        music.release()
        Log.d(tag, "onStop() is called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy() is called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(tag, "onSaveInstanceState")
        savedInstanceState.putInt(keyIndex, currentIndex)
        savedInstanceState.putInt(answerIndex, quizViewModel.correctAnswers)
        savedInstanceState.putInt(questionIndex, quizViewModel.questionCount)
    }
}