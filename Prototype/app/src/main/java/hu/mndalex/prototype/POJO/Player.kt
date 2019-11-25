package hu.mndalex.prototype.POJO

class Player(
    var posX: Int,
    var posY: Int,
    var color: Int,
    var money: Int,
    var profit: Int,
    var name: String){
    override fun toString(): String {
        return "Player(posX=$posX, posY=$posY, color=$color, money=$money, profit=$profit, name='$name')"
    }
}