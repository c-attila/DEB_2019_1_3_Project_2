package hu.mndalex.prototype.pojo

class Player(
    var posX: Int,
    var posY: Int,
    var color: Int,
    var colorOfOwnedCell: Int,
    var money: Int,
    var profit: Int,
    var name: String){
    override fun toString(): String {
        return "$name: $money"
    }

}