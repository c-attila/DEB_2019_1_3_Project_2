package hu.mndalex.prototype.screens.end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import hu.mndalex.prototype.R
import hu.mndalex.prototype.WinnerViewModel
import hu.mndalex.prototype.data.WinnerEntity
import hu.mndalex.prototype.databinding.EndFragmentBinding
import kotlinx.android.synthetic.main.end_fragment.*

class EndFragment : Fragment(){

    private lateinit var binding: EndFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val name = arguments?.getString("name")
        val money = arguments?.getInt("money")
        val moneyDifference = arguments?.getInt("moneyDifference")

        binding = DataBindingUtil.inflate(
            inflater, R.layout.end_fragment, container, false)
        binding.playerName.text = name
        binding.moneyValue.text = money.toString()
        binding.moneyDifferenceValue.text = moneyDifference.toString()

        lateinit var winnerViewModel: WinnerViewModel
        winnerViewModel.insert(WinnerEntity(name, money, moneyDifference))

        return binding.root
    }
}