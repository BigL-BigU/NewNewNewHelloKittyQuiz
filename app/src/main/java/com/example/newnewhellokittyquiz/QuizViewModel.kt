package com.example.newnewhellokittyquiz

import Question
import androidx.lifecycle.ViewModel

// saves the current information for when a screen update occurs
class QuizViewModel: ViewModel() {
    var correctAnswers = 0
    var questionCount = 0
    var counter = 0

    private val questionBank = listOf(
        Question(R.string.kitty1, true),
        Question(R.string.kitty2, false),
        Question(R.string.kitty3, false),
        Question(R.string.kitty4, true)
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textReID

    val questionBankSize: Int
        get() = questionBank.size

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev(){
        currentIndex -= 1
        if(currentIndex < 0){
            currentIndex = questionBank.size - 1
        }
    }

}