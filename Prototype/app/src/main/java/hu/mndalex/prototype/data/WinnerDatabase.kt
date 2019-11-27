package hu.mndalex.prototype.data

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.threeten.bp.LocalDate

@Database(entities = [WinnerEntity::class], version = 1)
abstract class WinnerDatabase : RoomDatabase() {

    abstract fun winnerDAO(): WinnerDAO

    companion object {
        private var instance: WinnerDatabase? = null

        fun getInstance(context: Context): WinnerDatabase? {
            if (instance == null) {
                synchronized(WinnerDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WinnerDatabase::class.java, "winner_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance)
                    .execute()
            }
        }
    }

    class PopulateDbAsyncTask(db: WinnerDatabase?) : AsyncTask<Unit, Unit, Unit>() {
        private val winnerDAO = db?.winnerDAO()

        override fun doInBackground(vararg params: Unit?) {
            winnerDAO?.insert(WinnerEntity("Test 1", 100, 50, LocalDate.now()))
            winnerDAO?.insert(WinnerEntity("Test 2", 200, 100, LocalDate.now()))
            winnerDAO?.insert(WinnerEntity("Test 3", 400, 200, LocalDate.now()))
        }
    }
}