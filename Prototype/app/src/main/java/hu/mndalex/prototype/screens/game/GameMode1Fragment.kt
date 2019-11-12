package hu.mndalex.prototype.screens.game

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import hu.mndalex.prototype.Player
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

    private var actualPlayerId = 0

    private var listOfPlayers = mutableListOf<Player>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_mode_1_fragment, container, false)

        setPlayer(0, Color.CYAN)
        setPlayer(1, Color.GREEN)

        binding.moneyTextView.setBackgroundColor(listOfPlayers[actualPlayerId].color)

        binding.buttonRight.setOnClickListener({ movePlayerHorizontally(1) })
        binding.buttonLeft.setOnClickListener({ movePlayerHorizontally(-1) })
        binding.buttonUp.setOnClickListener({ movePlayerVertically(-1) })
        binding.buttonDown.setOnClickListener({ movePlayerVertically(1) })

        return binding.root
    }

    private fun movePlayerHorizontally(x: Int) {
        var actualPlayerPosX = listOfPlayers[actualPlayerId].posX
        var actualPlayerPosY = listOfPlayers[actualPlayerId].posY

        if ((x == 1 && actualPlayerPosX < TABLE_WIDTH - 1) || (x == -1 && actualPlayerPosX > 0)) {
            setCellBackgroundColor(
                actualPlayerPosX,
                actualPlayerPosY,
                resources.getColor(R.color.table_cell_background_color)
            )

            actualPlayerPosX += x

            setCellBackgroundColor(
                actualPlayerPosX,
                actualPlayerPosY,
                listOfPlayers[actualPlayerId].color
            )

            generateCells(actualPlayerPosX, actualPlayerPosY)

            listOfPlayers[actualPlayerId].posX = actualPlayerPosX
            listOfPlayers[actualPlayerId].posY = actualPlayerPosY

            actualPlayerId += 1;
            if (actualPlayerId > listOfPlayers.size - 1)
                actualPlayerId = 0

            binding.moneyTextView.setBackgroundColor(listOfPlayers[actualPlayerId].color)
        }
    }

    private fun movePlayerVertically(y: Int) {
        var actualPlayerPosX = listOfPlayers[actualPlayerId].posX
        var actualPlayerPosY = listOfPlayers[actualPlayerId].posY

        if ((y == 1 && actualPlayerPosY < TABLE_HEIGHT - 1) || (y == -1 && actualPlayerPosY > 0)) {
            setCellBackgroundColor(
                actualPlayerPosX,
                actualPlayerPosY,
                resources.getColor(R.color.table_cell_background_color)
            )

            actualPlayerPosY += y

            setCellBackgroundColor(
                actualPlayerPosX,
                actualPlayerPosY,
                listOfPlayers[actualPlayerId].color
            )

            generateCells(actualPlayerPosX, actualPlayerPosY)

            listOfPlayers[actualPlayerId].posX = actualPlayerPosX
            listOfPlayers[actualPlayerId].posY = actualPlayerPosY

            actualPlayerId += 1;
            if (actualPlayerId > listOfPlayers.size - 1)
                actualPlayerId = 0

            binding.moneyTextView.setBackgroundColor(listOfPlayers[actualPlayerId].color)
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

    private fun setPlayer(playerId: Int, color: Int) {

        val actualPlayerPosX = (0 until TABLE_WIDTH).random()
        val actualPlayerPosY = (0 until TABLE_HEIGHT).random()

        listOfPlayers.add(Player(playerId, actualPlayerPosX, actualPlayerPosY, color))

        val row = binding.tableLayout.getChildAt(actualPlayerPosY) as TableRow
        val cell = row.getChildAt(actualPlayerPosX) as TextView
        cell.text = cellTextList.shuffled().take(1)[0]
        cell.setBackgroundColor(color)

        generateCells(actualPlayerPosX, actualPlayerPosY)
    }

    private fun setCellBackgroundColor(x: Int, y: Int, color: Int) {
        val row = binding.tableLayout.getChildAt(y) as TableRow
        val cell = row.getChildAt(x) as TextView
        cell.setBackgroundColor(color)
    }


}