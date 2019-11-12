package hu.mndalex.prototype.screens.game

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.GameMode1FragmentBinding

class GameMode1Fragment : Fragment() {

    private lateinit var binding: GameMode1FragmentBinding

    private val TABLE_WIDTH = 7
    private val TABLE_HEIGHT = 8

    private val INVISIBLE_CELL_TEXT_SIZE = 0F
    private val VISIBLE_CELL_TEXT_SIZE = 12F

    private val START_CELL_TEXT = "Start"

    private val cellTextList =
        listOf("Hotel", "Gas Station", "Restaurant", "Shop", "Casino", "Bakery")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_mode_1_fragment, container, false)

//        generateCells()
        setStartPos()

        return binding.root
    }

    private fun generateCells(posX: Int, posY: Int) {

        var row = binding.tableLayout.getChildAt(posY) as TableRow
        var cell: TextView
        if (posX < TABLE_WIDTH - 1) {
            cell = row.getChildAt(posX + 1) as TextView
            if (cell.text.isEmpty())
                cell.text = cellTextList.shuffled().take(1)[0]
        }
        if (posX > 0) {
            cell = row.getChildAt(posX - 1) as TextView
            if (cell.text.isEmpty())
                cell.text = cellTextList.shuffled().take(1)[0]
        }

        if (posY > 0) {
            row = binding.tableLayout.getChildAt(posY - 1) as TableRow
            cell = row.getChildAt(posX) as TextView
            if (cell.text.isEmpty())
                cell.text = cellTextList.shuffled().take(1)[0]
        }

        if (posY < TABLE_HEIGHT - 1) {
            row = binding.tableLayout.getChildAt(posY + 1) as TableRow
            cell = row.getChildAt(posX) as TextView
            if (cell.text.isEmpty())
                cell.text = cellTextList.shuffled().take(1)[0]
        }

    }

    private fun setStartPos() {

        val posX = (0 until TABLE_WIDTH).random()
        val posY = (0 until TABLE_HEIGHT).random()

        val row = binding.tableLayout.getChildAt(posY) as TableRow
        val cell = row.getChildAt(posX) as TextView
        cell.text = START_CELL_TEXT
        cell.setBackgroundColor(resources.getColor(R.color.player_cell_background_color))

        generateCells(posX, posY)
    }


}