package hu.mndalex.prototype.POJO

class Cell(
    var x: Int,
    var y: Int,
    var buildingId: Int,
    var ownerId: Int
    ) {

    override fun toString(): String {
        return "Cell(x=$x, y=$y, buildingId=$buildingId, ownerId=$ownerId)"
    }
}