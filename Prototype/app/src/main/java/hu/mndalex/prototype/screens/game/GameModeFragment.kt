package hu.mndalex.prototype.screens.game

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import hu.mndalex.prototype.POJO.Building
import hu.mndalex.prototype.POJO.Cell
import hu.mndalex.prototype.POJO.Player
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.GameModeFragmentBinding

class GameModeFragment : Fragment() {

    private lateinit var binding: GameModeFragmentBinding

    private val TABLE_WIDTH = 7
    private val TABLE_HEIGHT = 8

    private val listOfBuildings =
        listOf(
            Building(0, "Hotel", 1000, 300),
            Building(1, "Gas Station", 300, 60),
            Building(2, "Restaurant", 700, 200),
            Building(3, "Bakery", 100, 10),
            Building(4, "Shop", 300, 100)
        )

    var actualPlayerId = 0

    var listOfPlayers = mutableListOf<Player>()
    var listOfCells = mutableListOf<Cell>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_mode_fragment, container, false)

        setPlayer(Color.CYAN, "Player1")
        setPlayer(Color.GREEN, "Player2")

        setGameInfoLayout(listOfPlayers[actualPlayerId])

        binding.buttonRight.setOnClickListener({ movePlayerHorizontally(1) })
        binding.buttonLeft.setOnClickListener({ movePlayerHorizontally(-1) })
        binding.buttonUp.setOnClickListener({ movePlayerVertically(-1) })
        binding.buttonDown.setOnClickListener({ movePlayerVertically(1) })

        binding.buttonBuy.setOnClickListener({ onBuy() })

        return binding.root
    }

    private fun generateCells(posX: Int, posY: Int) {

        var row = binding.tableLayout.getChildAt(posY) as TableRow
        var cell: TextView
        if (posX < TABLE_WIDTH - 1) {
            cell = row.getChildAt(posX + 1) as TextView
            if (cell.text.isEmpty())
                setCell(cell, posX + 1, posY)
        }
        if (posX > 0) {
            cell = row.getChildAt(posX - 1) as TextView
            if (cell.text.isEmpty())
                setCell(cell, posX - 1, posY)
        }

        if (posY > 0) {
            row = binding.tableLayout.getChildAt(posY - 1) as TableRow
            cell = row.getChildAt(posX) as TextView
            if (cell.text.isEmpty())
                setCell(cell, posX, posY - 1)
        }

        if (posY < TABLE_HEIGHT - 1) {
            row = binding.tableLayout.getChildAt(posY + 1) as TableRow
            cell = row.getChildAt(posX) as TextView
            if (cell.text.isEmpty())
                setCell(cell, posX, posY + 1)
        }

    }

    private fun setCell(cell: TextView, x: Int, y: Int) {
        val building = listOfBuildings.shuffled().take(1)[0]
        cell.text = building.name
        cell.id = building.id

        listOfCells.add(
            Cell(
                x, y, building.id
            )
        )
    }


    private fun setPlayer(color: Int, name: String) {

        val actualPlayerPosX = (0 until TABLE_WIDTH).random()
        val actualPlayerPosY = (0 until TABLE_HEIGHT).random()

        for (player in listOfPlayers)
            if (actualPlayerPosX == player.posX && actualPlayerPosY == player.posY)
                return setPlayer(color, name)

        listOfPlayers.add(
            Player(
                actualPlayerPosX,
                actualPlayerPosY,
                color,
                1000,
                0,
                name
            )
        )

        val row = binding.tableLayout.getChildAt(actualPlayerPosY) as TableRow
        val cell = row.getChildAt(actualPlayerPosX) as TextView
        setCell(cell, actualPlayerPosX, actualPlayerPosY)
        cell.setBackgroundColor(color)

        generateCells(actualPlayerPosX, actualPlayerPosY)
    }

    private fun setCellBackgroundColor(x: Int, y: Int, color: Int) {
        getCellFromTableLayout(x,y).setBackgroundColor(color)
    }

    private fun getCellFromTableLayout(x: Int, y: Int): TextView{
        val row = binding.tableLayout.getChildAt(y) as TableRow
        return row.getChildAt(x) as TextView
    }

    //Only work with 1 or -1 parameter
    fun movePlayerHorizontally(x: Int) {
        var actualPlayerPosX = listOfPlayers[actualPlayerId].posX
        var actualPlayerPosY = listOfPlayers[actualPlayerId].posY

        for (player in listOfPlayers)
            if (player != listOfPlayers[actualPlayerId])
                if (actualPlayerPosX + x == player.posX && actualPlayerPosY == player.posY)
                    return

        if ((x > 0 && actualPlayerPosX + x < TABLE_WIDTH) || (x < 0 && actualPlayerPosX + x > -1)) {
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

            endOfRound()

        }
    }

    //Only work with 1 or -1 parameter
    fun movePlayerVertically(y: Int) {
        var actualPlayerPosX = listOfPlayers[actualPlayerId].posX
        var actualPlayerPosY = listOfPlayers[actualPlayerId].posY

        for (player in listOfPlayers)
            if (player != listOfPlayers[actualPlayerId])
                if (actualPlayerPosX == player.posX && actualPlayerPosY + y == player.posY)
                    return

        if ((y > 0 && actualPlayerPosY + y < TABLE_HEIGHT) || (y < 0 && actualPlayerPosY + y > -1)) {
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

            endOfRound()
        }
    }

    private fun setGameInfoLayout(actualPlayer: Player) {
        val actualPlayerPosX = actualPlayer.posX
        val actualPlayerPosY = actualPlayer.posY

        binding.gameInfoLayout.setBackgroundColor(listOfPlayers[actualPlayerId].color)

        val cell = getCellFromList(actualPlayerPosX, actualPlayerPosY)
        val building = listOfBuildings[cell!!.buildingId]

        binding.buildingCost.text = "Cost: " + building.cost
        binding.buildingProfit.text = "Profit: " + building.profit
        binding.buildingName.text = "Name: " + building.name
        if (cell.ownerId != -1)
            binding.buildingOwner.text = "Owner: " + listOfPlayers[cell.ownerId].name
        else
            binding.buildingOwner.text = "Owner: None"

        binding.moneyTextView.text = "Money: " + listOfPlayers[actualPlayerId].money
        binding.playerProfit.text = "Profit: " + listOfPlayers[actualPlayerId].profit

    }


    fun onBuy() {
        val actualPlayer = listOfPlayers[actualPlayerId]
        val cell = getCellFromList(actualPlayer.posX, actualPlayer.posY)

        if (cell!!.ownerId == -1) {
            val building = listOfBuildings[cell.buildingId]
            actualPlayer.money -= building.cost
            actualPlayer.profit += building.profit
            cell.ownerId = actualPlayerId

            getCellFromTableLayout(cell.x,cell.y).text = building.name + "\n" + actualPlayer.name

            endOfRound()
        }
    }

    private fun getCellFromList(x: Int, y: Int): Cell? {
        for (cell in listOfCells)
            if (cell.x == x && cell.y == y)
                return cell
        return null
    }

    private fun endOfRound() {
        listOfPlayers[actualPlayerId].money += listOfPlayers[actualPlayerId].profit

        actualPlayerId += 1
        if (actualPlayerId > listOfPlayers.size - 1)
            actualPlayerId = 0

        setGameInfoLayout(
            listOfPlayers[actualPlayerId]
        )
    }

}