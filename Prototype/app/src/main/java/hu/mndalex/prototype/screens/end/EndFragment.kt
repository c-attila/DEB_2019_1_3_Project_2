package hu.mndalex.prototype.screens.end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.EndFragmentBinding
import kotlinx.android.synthetic.main.end_fragment.*

class EndFragment : Fragment(){

    private lateinit var binding: EndFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.end_fragment, container, false)
        binding.playerName.text = arguments?.getString("name")
        binding.moneyValue.text = arguments?.getInt("money").toString()
        binding.moneyDifferenceValue.text = EndFragmentArgs.fromBundle(arguments!!).moneyDifference.toString()

        return binding.root
    }
}