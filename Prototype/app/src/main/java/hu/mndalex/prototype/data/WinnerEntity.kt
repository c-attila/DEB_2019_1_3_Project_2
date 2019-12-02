package hu.mndalex.prototype.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "winner_table")
data class WinnerEntity(
    var name: String?,
    var money: Int?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}