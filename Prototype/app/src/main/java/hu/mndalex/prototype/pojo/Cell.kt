package hu.mndalex.prototype.pojo

class Cell(
    var x: Int,
    var y: Int,
    var color: Int,
    var buildingId: Int
    ) {

    var ownerId: Int = -1

    override fun toString(): String {
        return "Cell(x=$x, y=$y, buildingId=$buildingId, ownerId=$ownerId)"
    }
}