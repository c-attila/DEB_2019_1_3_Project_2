package hu.mndalex.prototype.data

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDate

@Parcelize
@Entity(tableName = "winner_table")
data class WinnerEntity(

    var name: String?,
    var money: Int?,
    var moneyDifference: Int?,
    var time: LocalDate? = null
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Parcelize
class Winners : LiveData<List<WinnerEntity>>(), Parcelable