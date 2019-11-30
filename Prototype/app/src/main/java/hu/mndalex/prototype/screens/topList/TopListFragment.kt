package hu.mndalex.prototype.screens.topList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import hu.mndalex.prototype.R
import hu.mndalex.prototype.data.WinnerDatabase
import hu.mndalex.prototype.databinding.ToplistFragmentBinding
import java.time.LocalDate

class TopListFragment : Fragment() {

    private lateinit var binding: ToplistFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.toplist_fragment, container, false
        )
        binding.toplistButton1.setOnClickListener {
            findNavController().navigate(TopListFragmentDirections.actionToplistDestinationToMenuDestination())
        }

        val db = Room.databaseBuilder(
            activity!!.applicationContext,
            WinnerDatabase::class.java, "database-name"
        ).allowMainThreadQueries()
            .build()

        var winners = db.winnerDAO().getTopTen()
        binding.textView.text = winners.toString()
        println(winners.size)
        println(winners.toString())

        return binding.root
    }
}