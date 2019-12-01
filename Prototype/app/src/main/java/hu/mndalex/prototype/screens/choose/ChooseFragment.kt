package hu.mndalex.prototype.screens.choose

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.ChooseFragmentBinding

class ChooseFragment : Fragment() {

    lateinit var binding: ChooseFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.choose_fragment, container, false)

        binding.numOfPlayersEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                for (i in 2..9) {
                    val numOfPlayersString = binding.numOfPlayersEditText.text.toString()
                    if (numOfPlayersString.isNotEmpty() && i == numOfPlayersString.toInt()) {
                        createPlayerNameEditTexts(numOfPlayersString.toInt())
                        break
                    }
                }
            }

        })

        binding.playButton.setOnClickListener {
            for (i in 2..9) {
                val numOfPlayersString = binding.numOfPlayersEditText.text.toString()
                if (numOfPlayersString.isNotEmpty() && i == numOfPlayersString.toInt()) {
                    val names: Array<String> = readEditTexts(numOfPlayersString.toInt())
                    findNavController().navigate(
                        ChooseFragmentDirections.actionChooseFragmentToGameDestination(
                            arguments!!.getString("gameMode").toString(),
                            numOfPlayersString.toInt(),
                            names
                        )
                    )
                    break
                }
            }
        }

        return binding.root
    }

    private fun readEditTexts(numberOfEditTexts: Int): Array<String> {
        var names = Array(numberOfEditTexts){""}
        for(i in 0 until numberOfEditTexts){
            val text =
                ((binding.playerNamesLayout.getChildAt(i) as ConstraintLayout).getChildAt(0) as EditText).text
            if(text.isNotEmpty())
                names[i] = text.toString()
            else
                names[i] = "Player${i+1}"
        }
        return names
    }

    private fun createPlayerNameEditTexts(numberOfPlayers: Int) {
        binding.playerNamesLayout.removeAllViews()

        var editTextStyle = ContextThemeWrapper(activity, R.style.player_name_edit_text_style)
        var wrapperLayoutStyle = ContextThemeWrapper(activity, R.style.player_name_edit_text_wrapper_layout_style)

        for (i in 0 until numberOfPlayers) {
            val wrapperLayout = ConstraintLayout(wrapperLayoutStyle)
            val editText = EditText(editTextStyle)

            editText.setText("Player${i+1}")
            wrapperLayout.addView(editText)

            binding.playerNamesLayout.addView(wrapperLayout)
        }
    }

}
