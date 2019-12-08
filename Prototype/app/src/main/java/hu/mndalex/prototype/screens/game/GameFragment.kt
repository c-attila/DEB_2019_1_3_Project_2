package hu.mndalex.prototype.screens.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.GameFragmentBinding

@Suppress("DEPRECATION")
class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding

    private lateinit var gameController: GameController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        gameController = GameController(
            binding,
            resources,
            this,
            arguments!!.getStringArray("names")!!,
            arguments!!.getInt("numOfPlayers")!!,
            arguments!!.getString("gameMode")!!
        )

        gameController.initTableSize()

        gameController.initPlayers()

        gameController.initGameInfoLayout()

        gameController.setNavigationButtonOnClickListeners()

        return binding.root
    }
}


























