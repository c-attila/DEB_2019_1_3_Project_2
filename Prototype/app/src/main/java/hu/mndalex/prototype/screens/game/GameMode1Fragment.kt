package hu.mndalex.prototype.screens.game

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.GameMode1FragmentBinding
import org.w3c.dom.Text

class GameMode1Fragment : Fragment(){

    private lateinit var binding: GameMode1FragmentBinding

    private val TABLE_WIDTH = 7
    private val TABLE_HEIGHT = 8

    private val cellTextList = listOf("Hotel","Gas Station", "Restaurant")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_mode_1_fragment,container,false)

        generateCells()

        return binding.root
    }

    private fun generateCells(){

        for (i in 0 until TABLE_HEIGHT){
            for(j in 0 until TABLE_WIDTH){
                val row = binding.tableLayout.getChildAt(i) as TableRow
                val cell = row.getChildAt(j) as TextView
                cell.text  = cellTextList.shuffled().take(1)[0]
                cell.textSize = 0F
            }
        }

    }


}