package hu.mndalex.prototype.screens.topList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.ToplistFragmentBinding

class TopListFragment : Fragment() {

    private lateinit var binding: ToplistFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var winners = TopListFragmentArgs.fromBundle(arguments!!).winners

        binding = DataBindingUtil.inflate(
            inflater, R.layout.toplist_fragment, container, false)
        binding.textView.text = winners.toString()

        return binding.root
    }
}