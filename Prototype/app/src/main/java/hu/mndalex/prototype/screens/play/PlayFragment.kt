package hu.mndalex.prototype.screens.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.PlayFragmentBinding

class PlayFragment : Fragment (){

    lateinit var binding: PlayFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.play_fragment,container,false)

        binding.playButton1.setOnClickListener{findNavController()
            .navigate(PlayFragmentDirections.actionPlayDestinationToGameDestination())}

        return binding.root
    }

}
