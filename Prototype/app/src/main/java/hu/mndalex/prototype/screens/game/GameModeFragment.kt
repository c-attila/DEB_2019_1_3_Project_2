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
import hu.mndalex.prototype.Building
import hu.mndalex.prototype.Player
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.GameModeFragmentBinding

class GameModeFragment : Fragment() {

    private lateinit var binding: GameModeFragmentBinding

    private val TABLE_WIDTH = 7
    private val TABLE_HEIGHT = 8

    private val listOfBuildings =
        listOf(Building(0,"Hotel",1000,300),
            Building(1,"Gas Station",300,60),
            Building(2,"Restaurant",700,200),
            Building(3,"Bakery",100,10),
            Building(4,"Shop",300,100))

    var actualPlayerId = 0

    var listOfPlayers = mutableListOf<Player>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_mode_fragment, container, false)

        setPlayer(Color.CYAN)
        setPlayer(Color.GREEN)
//        setPlayer(Color.RED)

        setGameInfoLayout(listOfPlayers[actualPlayerId].posX,listOfPlayers[actualPlayerId].posY)

        binding.buttonRight.setOnClickListener({ movePlayerHorizontally(1) })
        binding.buttonLeft.setOnClickListener({ movePlayerHorizontally(-1) })
        binding.buttonUp.setOnClickListener({ movePlayerVertically(-1) })
        binding.buttonDown.setOnClickListener({ movePlayerVertically(1) })

        return binding.root
    }

    private fun generateCells(posX: Int, posY: Int) {

        var row = binding.tableLayout.getChildAt(posY) as TableRow
        var cell: TextView
        if (posX < TABLE_WIDTH - 1) {
            cell = row.getChildAt(posX + 1) as TextView
            if (cell.text.isEmpty())
                setCell(cell)
        }
        if (posX > 0) {
            cell = row.getChildAt(posX - 1) as TextView
            if (cell.text.isEmpty())
                setCell(cell)
        }

        if (posY > 0) {
            row = binding.tableLayout.getChildAt(posY - 1) as TableRow
            cell = row.getChildAt(posX) as TextView
            if (cell.text.isEmpty())
                setCell(cell)
        }

        if (posY < TABLE_HEIGHT - 1) {
            row = binding.tableLayout.getChildAt(posY + 1) as TableRow
            cell = row.getChildAt(posX) as TextView
            if (cell.text.isEmpty())
                setCell(cell)
        }

    }

    private fun setCell(cell: TextView){
        val building = listOfBuildings.shuffled().take(1)[0]
        cell.text = building.name
        cell.id = building.id
    }


    private fun setPlayer(color: Int) {

        val actualPlayerPosX = (0 until TABLE_WIDTH).random()
        val actualPlayerPosY = (0 until TABLE_HEIGHT).random()

        listOfPlayers.add(Player(actualPlayerPosX, actualPlayerPosY, color))

        val row = binding.tableLayout.getChildAt(actualPlayerPosY) as TableRow
        val cell = row.getChildAt(actualPlayerPosX) as TextView
        setCell(cell)
        cell.setBackgroundColor(color)

        generateCells(actualPlayerPosX, actualPlayerPosY)
    }

    private fun setCellBackgroundColor(x: Int, y: Int, color: Int) {
        val row = binding.tableLayout.getChildAt(y) as TableRow
        val cell = row.getChildAt(x) as TextView
        cell.setBackgroundColor(color)
    }

    private fun setCellText(x: Int, y: Int, text: String) {
        val row = binding.tableLayout.getChildAt(y) as TableRow
        val cell = row.getChildAt(x) as TextView
        cell.text = text
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

            setGameInfoLayout(listOfPlayers[actualPlayerId].posX,listOfPlayers[actualPlayerId].posY)
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

            setGameInfoLayout(listOfPlayers[actualPlayerId].posX,listOfPlayers[actualPlayerId].posY)
        }
    }

    private fun getCell(x:Int, y:Int):TextView{
        val row = binding.tableLayout.getChildAt(y) as TableRow
        return row.getChildAt(x) as TextView
    }

    private fun setGameInfoLayout(actualPlayerPosX: Int, actualPlayerPosY: Int){
        binding.gameInfoLayout.setBackgroundColor(listOfPlayers[actualPlayerId].color)

        val actualCellBuildingId = getCell(actualPlayerPosX,actualPlayerPosY).id
        binding.buildingCost.text = "Cost: " + listOfBuildings[actualCellBuildingId].cost
        binding.buildingProfit.text = "Profit: " + listOfBuildings[actualCellBuildingId].profit
        binding.buildingName.text = "Name: " + listOfBuildings[actualCellBuildingId].name
    }

}