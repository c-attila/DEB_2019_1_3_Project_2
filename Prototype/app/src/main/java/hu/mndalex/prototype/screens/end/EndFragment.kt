package hu.mndalex.prototype.screens.end

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.EndFragmentBinding
import hu.mndalex.prototype.IOnBackPressed

class EndFragment : Fragment(), IOnBackPressed {

    private lateinit var binding: EndFragmentBinding

    var listOfPlayers = ArrayList<Any>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        val db = Room.databaseBuilder(
//            activity!!.applicationContext,
//            WinnerDatabase::class.java, "database-name"
//        )   .allowMainThreadQueries()
//            .build()


        val arrayOfPlayerScores = arguments!!.getStringArray("players")

        binding = DataBindingUtil.inflate(
            inflater, R.layout.end_fragment, container, false
        )
        binding.endButton1.setOnClickListener {
            findNavController().navigate(EndFragmentDirections.actionEndDestinationToMenuDestination())
        }
        binding.endButton2.setOnClickListener {
            findNavController().navigate(EndFragmentDirections.actionEndDestinationToToplistDestination())
        }

        buildScoreLayout(arrayOfPlayerScores)

//        db.winnerDAO().insert(WinnerEntity(name, money, moneyDifference, LocalDateTime.now()))

        return binding.root
    }

    private fun buildScoreLayout(arrayOfPlayerScores: Array<String>?) {
        val wrapperLayoutStyle = ContextThemeWrapper(activity, R.style.wrapper_layout_style)
        val textViewStyle = ContextThemeWrapper(activity, R.style.score_text_view_style)

        for (player in arrayOfPlayerScores!!) {
            val wrapperLayout = ConstraintLayout(wrapperLayoutStyle)
            val textView = TextView(textViewStyle)
            textView.text = player

            wrapperLayout.addView(textView)
            binding.scoreLayout.addView(wrapperLayout)
        }
    }

    override fun onBackPressedd(): Boolean {
        return run {
            findNavController().navigate(EndFragmentDirections.actionEndDestinationToMenuDestination())
            true
        }
    }
}