package hu.mndalex.prototype.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class WinnerRepository(application: Application) {

    private var winnerDAO: WinnerDAO

    private var allWinners: LiveData<List<WinnerEntity>>

    init {
        val database: WinnerDatabase = WinnerDatabase.getInstance(
            application.applicationContext
        )!!
        winnerDAO = database.winnerDAO()
        allWinners = winnerDAO.getAllWinners()
    }

    fun insert(winner: WinnerEntity) {
        val insertWinnerAsyncTask = InsertWinnerAsyncTask(winnerDAO).execute(winner)
    }

    fun update(winner: WinnerEntity) {
        val updateWinnerAsyncTask = UpdateWinnerAsyncTask(winnerDAO).execute(winner)
    }

    fun delete(winner: WinnerEntity) {
        val deleteWinnerAsyncTask = DeleteWinnerAsyncTask(winnerDAO).execute(winner)
    }

    fun deleteAllWinners() {
        val deleteAllWinnersAsyncTask = DeleteAllWinnersAsyncTask(winnerDAO).execute()
    }

    fun getAllWinners(): LiveData<List<WinnerEntity>> {
        return allWinners
    }

    companion object {
        private class InsertWinnerAsyncTask(winnerDAO: WinnerDAO) : AsyncTask<WinnerEntity, Unit, Unit>() {
            val winnerDAO = winnerDAO

            override fun doInBackground(vararg p0: WinnerEntity?) {
                winnerDAO.insert(p0[0]!!)
            }
        }
        
        private class UpdateWinnerAsyncTask(winnerDao: WinnerDAO) : AsyncTask<WinnerEntity, Unit, Unit>() {
            val winnerDao = winnerDao

            override fun doInBackground(vararg p0: WinnerEntity?) {
                winnerDao.update(p0[0]!!)
            }
        }

        private class DeleteWinnerAsyncTask(winnerDao: WinnerDAO) : AsyncTask<WinnerEntity, Unit, Unit>() {
            val winnerDao = winnerDao

            override fun doInBackground(vararg p0: WinnerEntity?) {
                winnerDao.delete(p0[0]!!)
            }
        }

        private class DeleteAllWinnersAsyncTask(winnerDao: WinnerDAO) : AsyncTask<Unit, Unit, Unit>() {
            val winnerDao = winnerDao

            override fun doInBackground(vararg p0: Unit?) {
                winnerDao.deleteAllWinners()
            }
        }
    }
}