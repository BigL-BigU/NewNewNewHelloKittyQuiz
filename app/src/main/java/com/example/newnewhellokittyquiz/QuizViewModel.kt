package com.example.newnewhellokittyquiz

import Question
import androidx.lifecycle.ViewModel

class QuizViewModel: ViewModel() {
    private val questionList = listOf(
        Question(R.string.kitty1, true),
        Question(R.string.kitty2, false),
        Question(R.string.kitty3, false),
        Question(R.string.kitty4, true)
    )

    var cheatedList = arrayOf(false, false, false, false)  

    var currentIndex = 0  
    var numCorrectAnswers = 0  
    var questionCounter = 0  

    var isCheater = false  


    val currentQuestionAnswer: Boolean
        get() = questionList[currentIndex].answer  
    val currentQuestionText: Int
        get() = questionList[currentIndex].textReID  
    val questionListSize: Int
        get() = questionList.size  

    fun nextQuestion() {
        currentIndex = (currentIndex + 1) % questionList.size  
    }

    fun prevQuestion() {
        currentIndex -= 1  
        if (currentIndex < 0) {
            currentIndex = questionList.size - 1  
        }
    }


}