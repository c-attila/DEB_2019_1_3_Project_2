package hu.mndalex.prototype.screens.game

import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.GameFragmentBinding
import hu.mndalex.prototype.pojo.Building
import hu.mndalex.prototype.pojo.Cell
import hu.mndalex.prototype.pojo.Player

class GameController(
    private val binding: GameFragmentBinding,
    private val resources: Resources,
    private val gameFragment: GameFragment,
    private val names: Array<String>,
    private val numOfPlayers: Int,
    private val gameMode: String
) {

    private var tableWidth = 0
    private var tableHeight = 0

    private var COLOR_ALPHA = 150

    private var gameOver = false

    private val listOfBuildings =
        listOf(
            Building(0, "Hotel", 2000, 400),
            Building(1, "Gas Station", 300, 60),
            Building(2, "Gas Station", 300, 60),
            Building(3, "Restaurant", 1500, 300),
            Building(4, "Bakery", 100, 20),
            Building(5, "Bakery", 100, 20),
            Building(6, "Shop", 1000, 200)
        )

    private val listOfColors =
        listOf(
            listOf(Color.rgb(236, 219, 83), Color.argb(COLOR_ALPHA, 236, 219, 83)),
            listOf(Color.rgb(69, 239, 239), Color.argb(COLOR_ALPHA, 69, 239, 239)),
            listOf(Color.rgb(227, 65, 50), Color.argb(COLOR_ALPHA, 227, 65, 50)),
            listOf(Color.rgb(108, 160, 220), Color.argb(COLOR_ALPHA, 108, 160, 220)),
            listOf(Color.rgb(235, 150, 135), Color.argb(COLOR_ALPHA, 235, 150, 135)),
            listOf(Color.rgb(147, 71, 66), Color.argb(COLOR_ALPHA, 147, 71, 66)),
            listOf(Color.rgb(191, 216, 51), Color.argb(COLOR_ALPHA, 191, 216, 51)),
            listOf(Color.rgb(219, 178, 209), Color.argb(COLOR_ALPHA, 219, 178, 209)),
            listOf(Color.rgb(235, 225, 223), Color.argb(COLOR_ALPHA, 235, 225, 223))
        )

    private var actualPlayerId = 0
    private var nextPlayerId = 1
    private var listOfPlayers = mutableListOf<Player>()
    private var listOfCells = mutableListOf<Cell>()

    fun initTableSize() {
        tableHeight = binding.tableLayout.childCount
        tableWidth = (binding.tableLayout.getChildAt(0) as TableRow).childCount
    }

    fun initPlayers() {
        for (i in 0 until numOfPlayers) {
            setPlayer(listOfColors[i][0], listOfColors[i][1], names[i])
        }
    }

    fun initGameInfoLayout() {
        setGameInfoLayout(listOfPlayers[actualPlayerId])
    }

    fun setNavigationButtonOnClickListeners() {
        binding.buttonRight.setOnClickListener {
            onMoveHorizontally(1)
        }
        binding.buttonLeft.setOnClickListener {
            onMoveHorizontally(-1)
        }
        binding.buttonUp.setOnClickListener {
            onMoveVertically(-1)
        }
        binding.buttonDown.setOnClickListener {
            onMoveVertically(1)
        }
        binding.buttonBuy.setOnClickListener { onBuy() }
        binding.buttonSkip.setOnClickListener { endRound() }
    }

    /**
     * Only work with 1 or -1 parameter
     */
    private fun onMoveHorizontally(x: Int) {
        val (posX, posY) = getActualPlayerCoordinates()

        if (checkPlayerCollisionHorizontally(posX, posY, x)) return

        if (checkBorderCollisionHorizontally(x, posX)) {

            moveHorizontally(posX, posY, x)
            disableMoveButtons()

            if (gameMode == "gameMode2")
                endRound()

        }
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

        if (gameMode == "gameMode3")
            payTax(posX1, posY)

        if (gameMode == "gameMode4") {
            hideSurroundingCellsText(posX, posY, 2)
            generateSurroundingCells(posX1, posY, 2)
        } else {
            hideSurroundingCellsText(posX, posY)
            generateSurroundingCells(posX1, posY)
        }

        listOfPlayers[actualPlayerId].posX = posX1
        listOfPlayers[actualPlayerId].posY = posY
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

    private fun payTax(posX1: Int, posY: Int) {
        val cell = getCellFromList(posX1, posY)

        if (cell!!.ownerId != -1 && cell!!.ownerId != actualPlayerId) {
            val profit = listOfBuildings[cell.buildingId].profit
            listOfPlayers[actualPlayerId].money -= profit
            listOfPlayers[cell.ownerId].money += profit
        }

        refreshMoneyProfitOwner(listOfPlayers[actualPlayerId])
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



    private fun checkBorderCollisionHorizontally(x: Int, actualPlayerPosX: Int): Boolean {
        if ((x > 0 && actualPlayerPosX + x < tableWidth) || (x < 0 && actualPlayerPosX + x > -1)) {
            return true
        } else {
            Toast.makeText(gameFragment.context, "You have reached the edge of the board.", Toast.LENGTH_SHORT)
                .show()
            return false
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
                        gameFragment.context,
                        "There's already a player in this direction!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }
        return false
    }


    /**
     * Only work with 1 or -1 parameter
     */
    private fun onMoveVertically(y: Int) {
        val (actualPlayerPosX, actualPlayerPosY) = getActualPlayerCoordinates()

        if (checkPlayerCollisionVertically(actualPlayerPosX, actualPlayerPosY, y)) return

        if (checkBorderCollisionVertically(y, actualPlayerPosY)) {

            moveVertically(actualPlayerPosX, actualPlayerPosY, y)
            disableMoveButtons()

            if (gameMode == "gameMode2")
                endRound()
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

        if (gameMode == "gameMode3")
            payTax(posX, posY1)

        if (gameMode == "gameMode4") {
            hideSurroundingCellsText(posX, posY, 2)
            generateSurroundingCells(posX, posY1, 2)
        } else {
            hideSurroundingCellsText(posX, posY)
            generateSurroundingCells(posX, posY1)
        }

        listOfPlayers[actualPlayerId].posX = posX
        listOfPlayers[actualPlayerId].posY = posY1
    }


    private fun checkBorderCollisionVertically(y: Int, actualPlayerPosY: Int): Boolean {
        if ((y > 0 && actualPlayerPosY + y < tableHeight) || (y < 0 && actualPlayerPosY + y > -1)) {
            return true
        } else {
            Toast.makeText(gameFragment.context, "You have reached the edge of the board.", Toast.LENGTH_SHORT)
                .show()
            return false
        }
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
                        gameFragment.context,
                        "There's already a player in this direction!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }
        return false
    }


    private fun onBuy() {
        val actualPlayer = listOfPlayers[actualPlayerId]
        val cell = getCellFromList(actualPlayer.posX, actualPlayer.posY)

        if (cell!!.ownerId == -1 && actualPlayer.money >= listOfBuildings[cell.buildingId].cost) {
            buyCell(cell, actualPlayer)

//            if (arguments!!.getString("gameMode") != "gameMode2")
            endRound()
        } else {
            Toast.makeText(
                gameFragment.context,
                "Already sold or you don't have enough money!",
                Toast.LENGTH_SHORT
            ).show()
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
        cellTextView.text = building.name
    }

    private fun getCellFromTableLayout(x: Int, y: Int): TextView {
        return (binding.tableLayout.getChildAt(y) as TableRow).getChildAt(x) as TextView
    }



    private fun endRound() {
        gameOver = true
        for (cell in listOfCells)
            if (cell.ownerId == -1)
                gameOver = false

        listOfPlayers[actualPlayerId].money += listOfPlayers[actualPlayerId].profit

        if (gameOver) {
            val top: Pair<Player, Array<String>> = setTopList(listOfPlayers)
            Log.i("Top Player: ", top.first.toString())
            gameFragment.findNavController().navigate(
                GameFragmentDirections.actionGameDestinationToEndDestination(
                    top.second, top.first.name, top.first.money
                )
            )
        }

        actualPlayerId = nextPlayerId
        nextPlayerId++
        if (nextPlayerId > listOfPlayers.size - 1)
            nextPlayerId = 0

        setGameInfoLayout(
            listOfPlayers[actualPlayerId]
        )

        enableMoveButtons()

        logPlayers()
    }

    private fun logPlayers() {
        for (player in listOfPlayers) {
            Log.i("Player: ", player.toString())
        }
    }


    private fun enableMoveButtons() {
        binding.buttonRight.isEnabled = true
        binding.buttonLeft.isEnabled = true
        binding.buttonUp.isEnabled = true
        binding.buttonDown.isEnabled = true
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


    private fun setGameInfoLayout(actualPlayer: Player) {
        val actualPlayerPosX = actualPlayer.posX
        val actualPlayerPosY = actualPlayer.posY

        binding.gameInfoLayout.setBackgroundColor(listOfPlayers[actualPlayerId].color)

        val cell = getCellFromList(actualPlayerPosX, actualPlayerPosY)
        val building = listOfBuildings[cell!!.buildingId]

        setGameInfoTexts(building, cell)

    }

    private fun setGameInfoTexts(
        building: Building,
        cell: Cell
    ) {
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



    private fun setPlayer(color: Int, colorOfOwnedCell: Int, name: String) {

        val posX = (0 until tableWidth).random()
        val posY = (0 until tableHeight).random()

        if (isPlayerExistWithThisCoordinates(posX, posY)) return setPlayer(
            color,
            colorOfOwnedCell,
            name
        )

        listOfPlayers.add(
            Player(
                posX,
                posY,
                color,
                colorOfOwnedCell,
                1000,
                0,
                name
            )
        )

        generateStartCell(posY, posX, color)

        if (gameMode == "gameMode4")
            generateSurroundingCells(posX, posY, 2)
        else
            generateSurroundingCells(posX, posY)
    }

    private fun isPlayerExistWithThisCoordinates(
        posX: Int,
        posY: Int
    ): Boolean {
        for (player in listOfPlayers)
            if (posX == player.posX && posY == player.posY)
                return true
        return false
    }

    private fun generateStartCell(
        actualPlayerPosY: Int,
        actualPlayerPosX: Int,
        color: Int
    ) {
        val row = binding.tableLayout.getChildAt(actualPlayerPosY) as TableRow
        val cell = row.getChildAt(actualPlayerPosX) as TextView
        setCell(cell, actualPlayerPosX, actualPlayerPosY)
        cell.setBackgroundColor(color)
    }

    private fun generateSurroundingCells(posX: Int, posY: Int, radius: Int = 1) {
        for (x in posX - radius..posX + radius) {
            for (y in posY - radius..posY + radius) {
                if (x >= 0 && x <= tableWidth - 1 && y >= 0 && y <= tableHeight - 1) {
                    val row = binding.tableLayout.getChildAt(y) as TableRow
                    val cell = row.getChildAt(x) as TextView
                    if (cell.text.isEmpty())
                        setCell(cell, x, y)
                }
            }
        }
    }

    private fun setCell(cellTextView: TextView, x: Int, y: Int) {

        var cell = getCellFromList(x, y)
        if (cell == null) {
            val building = listOfBuildings.shuffled().take(1)[0]
            cellTextView.text = building.name + "\n" + building.cost
            cellTextView.id = building.id

            listOfCells.add(
                Cell(
                    x, y, resources.getColor(R.color.table_cell_background_color), building.id
                )
            )
        } else {
            val building = listOfBuildings[cell.buildingId]
            if (cell.ownerId == -1) {
                cellTextView.text = building.name + "\n" + building.cost
            } else {
                cellTextView.text = building.name + "\n" + listOfPlayers[actualPlayerId].name
            }
        }


    }

    private fun getCellFromList(x: Int, y: Int): Cell? {
        for (cell in listOfCells)
            if (cell.x == x && cell.y == y)
                return cell
        return null
    }

    private fun getActualPlayerCoordinates(): Pair<Int, Int> {
        val posX = listOfPlayers[actualPlayerId].posX
        val posY = listOfPlayers[actualPlayerId].posY
        return Pair(posX, posY)
    }

    private fun setCellBackgroundColor(x: Int, y: Int, color: Int) {
        getCellFromTableLayout(x, y).setBackgroundColor(color)
    }

    private fun refreshMoneyProfitOwner(actualPlayer: Player) {
        binding.moneyTextView.text = "Money: " + actualPlayer.money
        binding.playerProfit.text = "Profit: " + actualPlayer.profit
        binding.buildingOwner.text = "Owner: " + actualPlayer.name
    }



}