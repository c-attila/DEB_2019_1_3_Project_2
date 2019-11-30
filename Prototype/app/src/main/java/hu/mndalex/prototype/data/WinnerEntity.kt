package hu.mndalex.prototype.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Entity(tableName = "winner_table")
data class WinnerEntity(

    var name: String?,
    var money: Int?,
    var moneyDifference: Int?,
    var time: LocalDateTime? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}