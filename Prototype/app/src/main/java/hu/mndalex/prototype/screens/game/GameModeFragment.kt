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

class GameModeFragment : Fragment() {

    private lateinit var binding: GameModeFragmentBinding

    private var tableWidth = 0
    private var tableHeight = 0

    private val listOfBuildings =
        listOf(
            Building(0, "Hotel", 2000, 300),
            Building(1, "Gas Station", 300, 60),
            Building(2, "Restaurant", 1500, 200),
            Building(3, "Bakery", 100, 10),
            Building(4, "Shop", 1200, 100)
        )

    private val listOfColors =
        listOf(
            Color.rgb(236, 219, 83),
            Color.rgb(0, 166, 140),
            Color.rgb(227, 65, 50),
            Color.rgb(108, 160, 220),
            Color.rgb(235, 150, 135),
            Color.rgb(147, 71, 66),
            Color.rgb(235, 225, 223),
            Color.rgb(219, 178, 209),
            Color.rgb(191, 216, 51)
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

        initPlayers()

        setGameInfoLayout(listOfPlayers[actualPlayerId])

        setNavigationButtonOnClickListeners()

        return binding.root
    }

    private fun initTableSize() {
        tableHeight = binding.tableLayout.childCount
        tableWidth = (binding.tableLayout.getChildAt(0) as TableRow).childCount
    }

    private fun initPlayers() {
        for (i in 0 until arguments?.getInt("numOfPlayers")!!) {
            setPlayer(listOfColors[i], "Player" + (i + 1))
        }
    }

    private fun setPlayer(color: Int, name: String) {

        val posX = (0 until tableWidth).random()
        val posY = (0 until tableHeight).random()

        if (isPlayerExistWithThisCoordinates(posX, posY)) return setPlayer(
            color,
            name
        )

        listOfPlayers.add(
            Player(
                posX,
                posY,
                color,
                1000,
                0,
                name
            )
        )

        generateStartCell(posY, posX, color)

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
        for (x in posX-radius .. posX+radius) {
            for (y in posY-radius .. posY+radius) {
                if (x >= 0 && x <= tableWidth -1 && y >= 0 && y <= tableHeight -1) {
                    val row = binding.tableLayout.getChildAt(y) as TableRow
                    val cell = row.getChildAt(x) as TextView
                    if (cell.text.isEmpty())
                        setCell(cell, x, y)
                }
            }
        }
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

    private fun setCellBackgroundColor(x: Int, y: Int, color: Int) {
        getCellFromTableLayout(x, y).setBackgroundColor(color)
    }

    private fun getCellFromTableLayout(x: Int, y: Int): TextView {
        return (binding.tableLayout.getChildAt(y) as TableRow).getChildAt(x) as TextView
    }

    private fun setNavigationButtonOnClickListeners() {
        binding.buttonRight.setOnClickListener { onMoveHorizontally(1) }
        binding.buttonLeft.setOnClickListener { onMoveHorizontally(-1) }
        binding.buttonUp.setOnClickListener { onMoveVertically(-1) }
        binding.buttonDown.setOnClickListener { onMoveVertically(1) }
        binding.buttonBuy.setOnClickListener { onBuy() }
    }

    /**
     * Only work with 1 or -1 parameter
     */
    private fun onMoveHorizontally(x: Int) {
        val (posX, posY) = getActualPlayerCoordinates()

        if (checkPlayerCollisionHorizontally(posX, posY, x)) return

        if (checkBorderCollisionHorizontally(x, posX)) {

            moveHorizontally(posX, posY, x)

            endOfRound()

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
        setCellBackgroundColor(
            posX1,
            posY,
            resources.getColor(R.color.table_cell_background_color)
        )

        posX1 += x

        setCellBackgroundColor(
            posX1,
            posY,
            listOfPlayers[actualPlayerId].color
        )

        if (arguments!!.getString("gameMode") == "gameMode3")
            payTax(posX1, posY)

        generateSurroundingCells(posX1, posY)

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

            endOfRound()
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
        setCellBackgroundColor(
            posX,
            posY1,
            resources.getColor(R.color.table_cell_background_color)
        )

        posY1 += y

        setCellBackgroundColor(
            posX,
            posY1,
            listOfPlayers[actualPlayerId].color
        )

        if (arguments!!.getString("gameMode") == "gameMode3")
            payTax(posX, posY1)

        generateSurroundingCells(posX, posY1)

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

    private fun endOfRound() {
        listOfPlayers[actualPlayerId].money += listOfPlayers[actualPlayerId].profit
        //Todo: set multiplier back to 4
        if (listOfPlayers[actualPlayerId].money >= (listOfPlayers[nextPlayerId].money * 2)) {
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

        logPlayers()
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

            if (arguments?.getString("gameMode") == "gameMode2")
                endOfRound()
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
        refreshMoneyProfitOwner(actualPlayer)

        getCellFromTableLayout(cell.x, cell.y).text = building.name + "\n" + actualPlayer.name
    }

    private fun refreshMoneyProfitOwner(actualPlayer: Player) {
        binding.moneyTextView.text = "Money: " + actualPlayer.money
        binding.playerProfit.text = "Profit: " + actualPlayer.profit
        binding.buildingOwner.text = "Owner: " + actualPlayer.name
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