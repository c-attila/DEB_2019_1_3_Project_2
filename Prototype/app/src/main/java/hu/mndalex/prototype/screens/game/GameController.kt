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
        setGameInfoLayout(listOfPlayers[actualPlayerId)
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

            if (arguments?.getString("gameMode") == "gameMode2")
                endRound()

        }
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

            if (arguments?.getString("gameMode") == "gameMode2")
                endRound()
        }
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
                context,
                "Already sold or you don't have enough money!",
                Toast.LENGTH_SHORT
            ).show()
        }
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
            findNavController().navigate(
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



}