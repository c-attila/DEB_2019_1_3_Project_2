package hu.mndalex.prototype.screens.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.MenuFragmentBinding

class MenuFragment : Fragment(){

    lateinit var binding: MenuFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.menu_fragment, container, false)

        binding.menuButton1.setOnClickListener {
            findNavController().navigate(MenuFragmentDirections.actionTitleDestinationToPlayDestination())
        }

//        binding.menuButton3.setOnClickListener {
//            findNavController().navigate(MenuFragmentDirections.actionMenuDestinationToToplistDestination())
//        }

        return binding.root
    }

}