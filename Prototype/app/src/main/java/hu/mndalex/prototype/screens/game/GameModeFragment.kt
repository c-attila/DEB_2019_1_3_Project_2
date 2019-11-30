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
import androidx.navigation.fragment.findNavController
import hu.mndalex.prototype.POJO.Building
import hu.mndalex.prototype.POJO.Cell
import hu.mndalex.prototype.POJO.Player
import hu.mndalex.prototype.R
import hu.mndalex.prototype.databinding.GameModeFragmentBinding
import kotlinx.android.synthetic.main.play_fragment.*

class GameModeFragment : Fragment() {

    private lateinit var binding: GameModeFragmentBinding

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
            listOf(Color.rgb(235, 225, 223), Color.argb(COLOR_ALPHA, 235, 225, 223)),
            listOf(Color.rgb(219, 178, 209), Color.argb(COLOR_ALPHA, 219, 178, 209)),
            listOf(Color.rgb(191, 216, 51), Color.argb(COLOR_ALPHA, 191, 216, 51))
        )

    private var actualPlayerId = 0
    private var nextPlayerId = 1
    private var listOfPlayers = mutableListOf<Player>()
    private var listOfCells = mutableListOf<Cell>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_mode_fragment, container, false)

        initTableSize()

        if (arguments!!.getString("gameMode") != "testColors") {
            initPlayers()

            setGameInfoLayout(listOfPlayers[actualPlayerId])

            setNavigationButtonOnClickListeners()
        } else {
            testColors()
        }

        return binding.root
    }

    private fun testColors() {
        for (i in 0 until 9) {
            if (i >= 7) {
                getCellFromTableLayout(2, i - 7).setBackgroundColor(listOfColors[i][0])
                getCellFromTableLayout(3, i - 7).setBackgroundColor(listOfColors[i][1])
            } else {
                getCellFromTableLayout(0, i).setBackgroundColor(listOfColors[i][0])
                getCellFromTableLayout(1, i).setBackgroundColor(listOfColors[i][1])
            }

        }
    }

    private fun initTableSize() {
        tableHeight = binding.tableLayout.childCount
        tableWidth = (binding.tableLayout.getChildAt(0) as TableRow).childCount
    }

    private fun initPlayers() {
        for (i in 0 until arguments?.getInt("numOfPlayers")!!) {
            setPlayer(listOfColors[i][0], listOfColors[i][1], "Player" + (i + 1))
        }
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

        if (arguments?.getString("gameMode") == "gameMode4")
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

    private fun hideSurroundingCellsText(posX: Int, posY: Int, radius: Int = 1) {
        for (x in posX - radius..posX + radius) {
            for (y in posY - radius..posY + radius) {
                if (x >= 0 && x <= tableWidth - 1 && y >= 0 && y <= tableHeight - 1) {
                    val row = binding.tableLayout.getChildAt(y) as TableRow
                    val cell = row.getChildAt(x) as TextView
                    if (!isThereAnyPlayer(x, y, radius))
                        cell.text = ""
                }
            }
        }
    }

    private fun isThereAnyPlayer(posX: Int, posY: Int, radius: Int): Boolean {
        var otherPlayer = false
        for (player in listOfPlayers) {
            var x = player.posX
            var y = player.posY
            if (x >= posX - radius && x <= posX + radius && y >= posY - radius && y <= posY + radius){
                val (ActualPlayerPosX, ActualPlayerPosY) = getActualPlayerCoordinates()
                if (x != ActualPlayerPosX || y != ActualPlayerPosY){
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

    private fun setCell(cellTextView: TextView, x: Int, y: Int) {

        var cell = getCellFromList(x, y)
        if (cell == null){
            val building = listOfBuildings.shuffled().take(1)[0]
            cellTextView.text = building.name + "\n" + building.cost
            cellTextView.id = building.id

            listOfCells.add(
                Cell(
                    x, y, resources.getColor(R.color.table_cell_background_color), building.id
                )
            )
        }
        else {
            val building = listOfBuildings[cell.buildingId]
            if (cell.ownerId == -1) {
                cellTextView.text = building.name + "\n" + building.cost
            }
            else {
                cellTextView.text = building.name + "\n" + listOfPlayers[actualPlayerId].name
            }
        }


    }

    private fun setCellBackgroundColor(x: Int, y: Int, color: Int) {
        getCellFromTableLayout(x, y).setBackgroundColor(color)
    }

    private fun getCellFromTableLayout(x: Int, y: Int): TextView {
        return (binding.tableLayout.getChildAt(y) as TableRow).getChildAt(x) as TextView
    }

    private fun setNavigationButtonOnClickListeners() {
        binding.buttonRight.setOnClickListener {
            onMoveHorizontally(1)
            disableMoveButtons()
        }
        binding.buttonLeft.setOnClickListener {
            onMoveHorizontally(-1)
            disableMoveButtons()
        }
        binding.buttonUp.setOnClickListener {
            onMoveVertically(-1)
            disableMoveButtons()
        }
        binding.buttonDown.setOnClickListener {
            onMoveVertically(1)
            disableMoveButtons()
        }
        binding.buttonBuy.setOnClickListener { onBuy() }
        binding.buttonSkip.setOnClickListener { endRound() }
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

    /**
     * Only work with 1 or -1 parameter
     */
    private fun onMoveHorizontally(x: Int) {
        val (posX, posY) = getActualPlayerCoordinates()

        if (checkPlayerCollisionHorizontally(posX, posY, x)) return

        if (checkBorderCollisionHorizontally(x, posX)) {

            moveHorizontally(posX, posY, x)

            if (arguments?.getString("gameMode") == "gameMode2")
                endRound()

        }
    }

    private fun checkPlayerCollisionHorizontally(
        actualPlayerPosX: Int,
        actualPlayerPosY: Int,
        x: Int
    ): Boolean {
        for (player in listOfPlayers)
            if (player != listOfPlayers[actualPlayerId])
                if (actualPlayerPosX + x == player.posX && actualPlayerPosY == player.posY)
                    return true
        return false
    }

    private fun checkBorderCollisionHorizontally(x: Int, actualPlayerPosX: Int) =
        (x > 0 && actualPlayerPosX + x < tableWidth) || (x < 0 && actualPlayerPosX + x > -1)

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
        }
        else {
            hideSurroundingCellsText(posX, posY)
            generateSurroundingCells(posX1, posY)
        }

        listOfPlayers[actualPlayerId].posX = posX1
        listOfPlayers[actualPlayerId].posY = posY
    }

    /**
     * Only work with 1 or -1 parameter
     */
    private fun onMoveVertically(y: Int) {
        val (actualPlayerPosX, actualPlayerPosY) = getActualPlayerCoordinates()

        if (checkPlayerCollisionVertically(actualPlayerPosX, actualPlayerPosY, y)) return

        if (checkBorderCollisionVertically(y, actualPlayerPosY)) {

            moveVertically(actualPlayerPosX, actualPlayerPosY, y)

            if (arguments?.getString("gameMode") == "gameMode2")
                endRound()
        }
    }

    private fun checkPlayerCollisionVertically(
        actualPlayerPosX: Int,
        actualPlayerPosY: Int,
        y: Int
    ): Boolean {
        for (player in listOfPlayers)
            if (player != listOfPlayers[actualPlayerId])
                if (actualPlayerPosX == player.posX && actualPlayerPosY + y == player.posY)
                    return true
        return false
    }

    private fun checkBorderCollisionVertically(
        y: Int,
        actualPlayerPosY: Int
    ) = (y > 0 && actualPlayerPosY + y < tableHeight) || (y < 0 && actualPlayerPosY + y > -1)

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
        }
        else {
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

    private fun endRound() {
        gameOver = true
        for (cell in listOfCells)
            if (cell.ownerId == -1)
                gameOver = false

        listOfPlayers[actualPlayerId].money += listOfPlayers[actualPlayerId].profit

        if (gameOver) {
            findNavController().navigate(
                GameModeFragmentDirections.actionGameDestinationToEndDestination(
                    listOfPlayers[actualPlayerId].name,
                    listOfPlayers[actualPlayerId].money,
                    listOfPlayers[actualPlayerId].money - listOfPlayers[nextPlayerId].money
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

    private fun onBuy() {
        val actualPlayer = listOfPlayers[actualPlayerId]
        val cell = getCellFromList(actualPlayer.posX, actualPlayer.posY)

        if (cell!!.ownerId == -1 && actualPlayer.money >= listOfBuildings[cell.buildingId].cost) {
            buyCell(cell, actualPlayer)

            if (arguments!!.getString("gameMode") != "gameMode2")
                endRound()
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
