/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 *
 * 1. Only displays and gets users/os events
 * 2. Does not make decisions
 */
class GameFragment : Fragment() {

    // Get reference to the GameViewModel
    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_fragment,
            container,
            false
        )

        //
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        // #2. Pass in ViewModel
        binding.gameViewModel = viewModel

        // #2.1 Set Lifecycle Owner
        /**
         * To make your data binding lifecycle aware and to have it play nicely with LiveData,
         * you need to call binding.lifecycleOwner.
         */
        binding.lifecycleOwner = this


        // #3. Remove setOnClickListener{} as we are using 2-way databinding through XML
        // binding.correctButton.setOnClickListener {
        //     viewModel.onCorrect()
        // }

        // binding.skipButton.setOnClickListener {
        //     viewModel.onSkip()
        // }

        /** Methods for updating the UI **/
        // viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
        //     binding.scoreText.text = newScore.toString()
        // })

        // viewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
        //     binding.wordText.text = newWord.toString()
        // })

        // viewModel.currentTime.observe(viewLifecycleOwner, Observer { newTime ->
        //     binding.timerText.text = DateUtils.formatElapsedTime(newTime)
        // })

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished) {

                gameFinished()

                // This ensures that the code is executed only once, and not being fetching again on screen rotations.
                viewModel.onGameFinishComplete()

            }
        })

        // Buzzes when triggered with different buzz events
        viewModel.eventBuzz.observe(viewLifecycleOwner, Observer { buzzType ->
            if (buzzType != GameViewModel.BuzzType.NO_BUZZ) {
                buzz(buzzType.pattern)
                viewModel.onBuzzComplete()
            }
        })

        return binding.root

    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action =
            GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0) // Elvis operator
        findNavController().navigate(action)
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()

        buzzer?.let {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }

        }
    }

}
