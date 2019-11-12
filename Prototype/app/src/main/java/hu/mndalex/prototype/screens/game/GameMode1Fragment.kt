package hu.mndalex.prototype.screens.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
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

    private var playerPosX: Int = 0
    private var playerPosY: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_mode_1_fragment, container, false)

        setStartPos()

        binding.buttonRight.setOnClickListener({ movePlayerHorizontally(1) })
        binding.buttonLeft.setOnClickListener({ movePlayerHorizontally(-1) })
        binding.buttonUp.setOnClickListener({ movePlayerVertically(-1) })
        binding.buttonDown.setOnClickListener({ movePlayerVertically(1) })

        return binding.root
    }

    private fun movePlayerHorizontally(x: Int) {
        if ((x == 1 && playerPosX < TABLE_WIDTH - 1) || (x == -1 && playerPosX > 0)) {
            setCellBackgroundColor(
                playerPosX,
                playerPosY,
                resources.getColor(R.color.table_cell_background_color)
            )

            playerPosX += x

            setCellBackgroundColor(
                playerPosX,
                playerPosY,
                resources.getColor(R.color.player_cell_background_color)
            )

            generateCells(playerPosX, playerPosY)
        }
    }

    private fun movePlayerVertically(y: Int) {
        if ((y == 1 && playerPosY < TABLE_HEIGHT - 1) || (y == -1 && playerPosY > 0)) {
            setCellBackgroundColor(
                playerPosX,
                playerPosY,
                resources.getColor(R.color.table_cell_background_color)
            )

            playerPosY += y

            setCellBackgroundColor(
                playerPosX,
                playerPosY,
                resources.getColor(R.color.player_cell_background_color)
            )

            generateCells(playerPosX, playerPosY)
        }
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

        playerPosX = (0 until TABLE_WIDTH).random()
        playerPosY = (0 until TABLE_HEIGHT).random()

        val row = binding.tableLayout.getChildAt(playerPosY) as TableRow
        val cell = row.getChildAt(playerPosX) as TextView
        cell.text = START_CELL_TEXT
        cell.setBackgroundColor(resources.getColor(R.color.player_cell_background_color))

        generateCells(playerPosX, playerPosY)
    }

    private fun setCellBackgroundColor(x: Int, y: Int, color: Int) {
        val row = binding.tableLayout.getChildAt(y) as TableRow
        val cell = row.getChildAt(x) as TextView
        cell.setBackgroundColor(color)
    }


}