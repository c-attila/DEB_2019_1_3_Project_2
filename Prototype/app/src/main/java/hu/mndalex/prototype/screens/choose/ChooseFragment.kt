package hu.mndalex.prototype.screens.choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.ChooseFragmentBinding

class ChooseFragment : Fragment (){

    lateinit var binding: ChooseFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.choose_fragment,container,false)

        binding.playButton.setOnClickListener{
            findNavController().navigate(ChooseFragmentDirections.actionChooseFragmentToGameDestination(
                arguments!!.getString("gameMode").toString(),
                binding.numOfPlayers.text.toString().toInt()))
        }

        return binding.root
    }

}
