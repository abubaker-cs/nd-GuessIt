package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 * 1. The ViewModel is a stable place to store the data to display in the associated UI controller.
 * 2. The Fragment draws the data on screen and captures input events.
 * 3. It should not decide what to display on screen or process what happens during an input event.
 * 4. The ViewModel never contains references to activities, fragments, or views.
 */
class GameViewModel : ViewModel() {

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L

        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L

        // This is the total time of the game (Game Duration)
        const val COUNTDOWN_TIME = 10000L
    }

    private val timer: CountDownTimer

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    // Transformations.map()
    val currentTimeString = Transformations.map(currentTime) { time ->

        //
        DateUtils.formatElapsedTime(time)

    }

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
        _eventGameFinish.value = false
        resetList()
        nextWord()

        // Default Value for Score
        _score.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                _currentTime.value = DONE
                _eventGameFinish.value = true
            }

        }

        timer.start()
        // DateUtils.formatElapsedTime(newTime)
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
            // _eventGameFinish.value = true
            resetList()
        }

        _word.value = wordList.removeAt(0)

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

    // It is representing that the event has been handled.
    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

}