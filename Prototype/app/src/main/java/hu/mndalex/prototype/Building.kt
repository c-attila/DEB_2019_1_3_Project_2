package hu.mndalex.prototype

class Building {

    var name: String = ""
    var cost: Int = 0
    var profit: Int = 0

    constructor(name: String, cost: Int, profit: Int) {
        this.name = name
        this.cost = cost
        this.profit = profit
    }

    override fun toString(): String {
        return name + " " + cost + " +" + profit
    }


}