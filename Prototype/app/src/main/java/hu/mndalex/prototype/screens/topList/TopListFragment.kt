package hu.mndalex.prototype.screens.topList

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.TopListFragmentBinding

class TopListFragment : Fragment() {

    private lateinit var binding: TopListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var winners

        binding = DataBindingUtil.inflate(
            inflater, R.layout.end_fragment, container, false)

        return binding.root
    }
}