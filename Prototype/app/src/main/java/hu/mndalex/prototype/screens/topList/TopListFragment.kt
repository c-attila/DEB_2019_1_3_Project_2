package hu.mndalex.prototype.screens.topList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import hu.mndalex.prototype.R
import hu.mndalex.prototype.data.WinnerDatabase
import hu.mndalex.prototype.databinding.ToplistFragmentBinding

class TopListFragment : Fragment() {

    private lateinit var binding: ToplistFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        val db = Room.databaseBuilder(
            activity!!.applicationContext,
            WinnerDatabase::class.java, "database-name"
        )   .allowMainThreadQueries()
            .build()

        binding = DataBindingUtil.inflate(
            inflater, R.layout.toplist_fragment, container, false)

        var winners = db.winnerDAO().getAllWinners()
        binding.textView.text = winners.toString()
        println(winners.size)
        println(winners.toString())

        return binding.root
    }
}