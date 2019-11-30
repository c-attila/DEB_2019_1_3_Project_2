package hu.mndalex.prototype.data

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@TypeConverters(Converters::class)
@Database(entities = [WinnerEntity::class], version = 1)
abstract class WinnerDatabase : RoomDatabase() {

    abstract fun winnerDAO(): WinnerDAO

    companion object {
        private var instance: WinnerDatabase? = null

        fun getInstance(context: Context): WinnerDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    WinnerDatabase::class.java, "winner_database"
                ).build()
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }

    }
}