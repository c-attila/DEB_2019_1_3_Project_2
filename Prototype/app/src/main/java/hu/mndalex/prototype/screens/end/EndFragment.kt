package hu.mndalex.prototype.screens.end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.mndalex.prototype.R
import hu.mndalex.prototype.data.WinnerEntity
import hu.mndalex.prototype.databinding.EndFragmentBinding
import androidx.room.Room
import hu.mndalex.prototype.IOnBackPressed
import hu.mndalex.prototype.data.WinnerDatabase
import org.threeten.bp.LocalDateTime

class EndFragment : Fragment(), IOnBackPressed{

    private lateinit var binding: EndFragmentBinding

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


        val name = arguments?.getString("name")
        val money = arguments?.getInt("money")
        val moneyDifference = arguments?.getInt("moneyDifference")

        binding = DataBindingUtil.inflate(
            inflater, R.layout.end_fragment, container, false)
        binding.endButton1.setOnClickListener{
            findNavController().navigate(EndFragmentDirections.actionEndDestinationToMenuDestination())
        }
        binding.endButton2.setOnClickListener{
            findNavController().navigate(EndFragmentDirections.actionEndDestinationToToplistDestination())
        }

        binding.playerName.text = name
        binding.moneyValue.text = money.toString()
        binding.moneyDifferenceValue.text = moneyDifference.toString()

        db.winnerDAO().insert(WinnerEntity(name, money, moneyDifference, LocalDateTime.now()))

        return binding.root
    }

    override fun onBackPressedd(): Boolean {
        return run {
            findNavController().navigate(EndFragmentDirections.actionEndDestinationToMenuDestination())
            true
        }
    }
}