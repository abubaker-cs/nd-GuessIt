package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 1. The ViewModel is a stable place to store the data to display in the associated UI controller.
 * 2. The Fragment draws the data on screen and captures input events.
 * 3. It should not decide what to display on screen or process what happens during an input event.
 * 4. The ViewModel never contains references to activities, fragments, or views.
 */
class GameViewModel : ViewModel() {

    /**
     * Encapsulation
     */
    // The current word
    private var _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    // MutableLiveData = It is the data whose values can be changed (Variable)
    private var _score = MutableLiveData<Int>() // R/W for Internal
    val score: LiveData<Int>
        get() = _score // Write Only | For External

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    // Game finished Event
    private var _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish


    init {
        Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()

        // Default Value for Score
        _score.value = 0
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            // gameFinished()
        } else {
            _word.value = wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1) // score--
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1) // score++
        nextWord()
    }

}