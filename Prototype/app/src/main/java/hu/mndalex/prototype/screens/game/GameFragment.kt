package hu.mndalex.prototype.screens.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import hu.mndalex.prototype.pojo.Building
import hu.mndalex.prototype.pojo.Cell
import hu.mndalex.prototype.pojo.Player
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.GameFragmentBinding

@Suppress("DEPRECATION")
class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding

    private lateinit var gameController: GameController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        gameController = GameController(
            binding,
            resources
            arguments!!.getStringArray("names")!!,
            arguments!!.getInt("numOfPlayers")!!,
            arguments!!.getString("gameMode")!!
        )

        gameController.initTableSize()

        gameController.initPlayers()

        gameController.initGameInfoLayout()

        setNavigationButtonOnClickListeners()

        return binding.root
    }


    private fun hideSurroundingCellsText(posX: Int, posY: Int, radius: Int = 1) {
        for (x in posX - radius..posX + radius) {
            for (y in posY - radius..posY + radius) {
                if (x >= 0 && x <= tableWidth - 1 && y >= 0 && y <= tableHeight - 1) {
                    val row = binding.tableLayout.getChildAt(y) as TableRow
                    val cell = row.getChildAt(x) as TextView
                    if (!isThereAnySurroundingPlayer(x, y, radius))
                        cell.text = ""
                }
            }
        }
    }

    private fun isThereAnySurroundingPlayer(posX: Int, posY: Int, radius: Int): Boolean {
        var otherPlayer = false
        for (player in listOfPlayers) {
            var x = player.posX
            var y = player.posY
            if (x >= posX - radius && x <= posX + radius && y >= posY - radius && y <= posY + radius) {
                val (ActualPlayerPosX, ActualPlayerPosY) = getActualPlayerCoordinates()
                if (x != ActualPlayerPosX || y != ActualPlayerPosY) {
                    otherPlayer = true
                }
            }
        }
        return otherPlayer
    }

    private fun generateCellAtRight(posX: Int, posY: Int) {
        val row = binding.tableLayout.getChildAt(posY) as TableRow
        if (posX < tableWidth - 1) {
            val cell = row.getChildAt(posX + 1) as TextView
            if (cell.text.isEmpty())
                setCell(cell, posX + 1, posY)
        }
    }

    private fun generateCellAtLeft(posX: Int, posY: Int) {
        val row = binding.tableLayout.getChildAt(posY) as TableRow
        if (posX > 0) {
            val cell = row.getChildAt(posX - 1) as TextView
            if (cell.text.isEmpty())
                setCell(cell, posX - 1, posY)
        }
    }

    private fun generateCellAtTop(posY: Int, posX: Int) {
        if (posY > 0) {
            val row = binding.tableLayout.getChildAt(posY - 1) as TableRow
            val cell = row.getChildAt(posX) as TextView
            if (cell.text.isEmpty())
                setCell(cell, posX, posY - 1)
        }
    }

    private fun generateCellAtBottom(posY: Int, posX: Int) {
        if (posY < tableHeight - 1) {
            val row = binding.tableLayout.getChildAt(posY + 1) as TableRow
            val cell = row.getChildAt(posX) as TextView
            if (cell.text.isEmpty())
                setCell(cell, posX, posY + 1)
        }
    }


    private fun setCellBackgroundColor(x: Int, y: Int, color: Int) {
        getCellFromTableLayout(x, y).setBackgroundColor(color)
    }

    private fun getCellFromTableLayout(x: Int, y: Int): TextView {
        return (binding.tableLayout.getChildAt(y) as TableRow).getChildAt(x) as TextView
    }


    private fun disableMoveButtons() {
        val actualPlayer = listOfPlayers[actualPlayerId]
        val x = actualPlayer.posX
        val y = actualPlayer.posY
        val cell = getCellFromList(x, y)

        if (cell!!.ownerId != actualPlayerId) {
            binding.buttonRight.isEnabled = false
            binding.buttonLeft.isEnabled = false
            binding.buttonUp.isEnabled = false
            binding.buttonDown.isEnabled = false
        }
    }



    private fun checkPlayerCollisionHorizontally(
        actualPlayerPosX: Int,
        actualPlayerPosY: Int,
        x: Int
    ): Boolean {
        for (player in listOfPlayers)
            if (player != listOfPlayers[actualPlayerId])
                if (actualPlayerPosX + x == player.posX && actualPlayerPosY == player.posY) {
                    Toast.makeText(
                        context,
                        "There's already a player in this direction!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }
        return false
    }

    private fun checkBorderCollisionHorizontally(x: Int, actualPlayerPosX: Int): Boolean {
        if ((x > 0 && actualPlayerPosX + x < tableWidth) || (x < 0 && actualPlayerPosX + x > -1)) {
            return true
        } else {
            Toast.makeText(context, "You have reached the edge of the board.", Toast.LENGTH_SHORT)
                .show()
            return false
        }
    }

    private fun moveHorizontally(posX: Int, posY: Int, x: Int) {
        var posX1 = posX
        var cell = getCellFromList(posX, posY)

        setCellBackgroundColor(
            posX1,
            posY,
            cell!!.color
        )

        posX1 += x

        setCellBackgroundColor(
            posX1,
            posY,
            listOfPlayers[actualPlayerId].color
        )

        refreshCellInfo(getCellFromList(posX1, posY)!!)

        if (arguments!!.getString("gameMode") == "gameMode3")
            payTax(posX1, posY)

        if (arguments?.getString("gameMode") == "gameMode4") {
            hideSurroundingCellsText(posX, posY, 2)
            generateSurroundingCells(posX1, posY, 2)
        } else {
            hideSurroundingCellsText(posX, posY)
            generateSurroundingCells(posX1, posY)
        }

        listOfPlayers[actualPlayerId].posX = posX1
        listOfPlayers[actualPlayerId].posY = posY
    }



    private fun checkPlayerCollisionVertically(
        actualPlayerPosX: Int,
        actualPlayerPosY: Int,
        y: Int
    ): Boolean {
        for (player in listOfPlayers)
            if (player != listOfPlayers[actualPlayerId])
                if (actualPlayerPosX == player.posX && actualPlayerPosY + y == player.posY) {
                    Toast.makeText(
                        context,
                        "There's already a player in this direction!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }
        return false
    }

    private fun checkBorderCollisionVertically(y: Int, actualPlayerPosY: Int): Boolean {
        if ((y > 0 && actualPlayerPosY + y < tableHeight) || (y < 0 && actualPlayerPosY + y > -1)) {
            return true
        } else {
            Toast.makeText(context, "You have reached the edge of the board.", Toast.LENGTH_SHORT)
                .show()
            return false
        }
    }


    private fun moveVertically(
        posX: Int,
        posY: Int,
        y: Int
    ) {
        var posY1 = posY
        var cell = getCellFromList(posX, posY)
        setCellBackgroundColor(
            posX,
            posY1,
            cell!!.color
        )

        posY1 += y

        setCellBackgroundColor(
            posX,
            posY1,
            listOfPlayers[actualPlayerId].color
        )

        refreshCellInfo(getCellFromList(posX, posY1)!!)

        if (arguments!!.getString("gameMode") == "gameMode3")
            payTax(posX, posY1)

        if (arguments?.getString("gameMode") == "gameMode4") {
            hideSurroundingCellsText(posX, posY, 2)
            generateSurroundingCells(posX, posY1, 2)
        } else {
            hideSurroundingCellsText(posX, posY)
            generateSurroundingCells(posX, posY1)
        }

        listOfPlayers[actualPlayerId].posX = posX
        listOfPlayers[actualPlayerId].posY = posY1
    }

    private fun payTax(posX1: Int, posY: Int) {
        val cell = getCellFromList(posX1, posY)

        if (cell!!.ownerId != -1 && cell!!.ownerId != actualPlayerId) {
            val profit = listOfBuildings[cell.buildingId].profit
            listOfPlayers[actualPlayerId].money -= profit
            listOfPlayers[cell.ownerId].money += profit
        }

        refreshMoneyProfitOwner(listOfPlayers[actualPlayerId])
    }


    private fun enableMoveButtons() {
        binding.buttonRight.isEnabled = true
        binding.buttonLeft.isEnabled = true
        binding.buttonUp.isEnabled = true
        binding.buttonDown.isEnabled = true
    }

    private fun logPlayers() {
        for (player in listOfPlayers) {
            Log.i("Player: ", player.toString())
        }
    }


    private fun buyCell(
        cell: Cell,
        actualPlayer: Player
    ) {
        val building = listOfBuildings[cell.buildingId]
        actualPlayer.money -= building.cost
        actualPlayer.profit += building.profit
        cell.ownerId = actualPlayerId
        cell.color = actualPlayer.colorOfOwnedCell

        refreshMoneyProfitOwner(actualPlayer)

        val cellTextView = getCellFromTableLayout(cell.x, cell.y)
        cellTextView.text = building.name + "\n" + actualPlayer.name
    }

    private fun refreshMoneyProfitOwner(actualPlayer: Player) {
        binding.moneyTextView.text = "Money: " + actualPlayer.money
        binding.playerProfit.text = "Profit: " + actualPlayer.profit
        binding.buildingOwner.text = "Owner: " + actualPlayer.name
    }

    private fun refreshCellInfo(cell: Cell) {
        val building = listOfBuildings[cell.buildingId]

        binding.buildingName.text = "Name: " + building.name
        binding.buildingCost.text = "Cost: " + building.cost
        binding.buildingProfit.text = "Profit: " + building.profit
        if (cell.ownerId == -1)
            binding.buildingOwner.text = "Owner: None"
        else
            binding.buildingOwner.text = "Owner: " + listOfPlayers[cell.ownerId].name

    }





    private fun setTopList(listOfPlayers1: MutableList<Player>): Pair<Player, Array<String>> {
        var listOfPlayers =
            listOfPlayers1.sortedWith(compareBy(Player::money, Player::profit)).reversed()

        val players = Array(listOfPlayers.size) { "" }

        var i = 0
        for (player in listOfPlayers) {
            players[i++] = "$i. $player"
        }

        return Pair(listOfPlayers[0], players)
    }

}
