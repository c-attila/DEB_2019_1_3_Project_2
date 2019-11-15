package hu.mndalex.prototype.DAO

class Building {

    var id: Int = 0
    var name: String = ""
    var cost: Int = 0
    var profit: Int = 0

    constructor(id: Int, name: String, cost: Int, profit: Int) {
        this.id = id
        this.name = name
        this.cost = cost
        this.profit = profit
    }

    override fun toString(): String {
        return name + " " + cost + " +" + profit
    }


}