package hu.mndalex.prototype

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import hu.mndalex.prototype.data.WinnerEntity
import hu.mndalex.prototype.data.WinnerRepository

class WinnerViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: WinnerRepository = WinnerRepository(application)

    private var allWinners: LiveData<List<WinnerEntity>> = repository.getAllWinners()

    fun insert(winner: WinnerEntity) {
        repository.insert(winner)
    }

    fun update(winner: WinnerEntity) {
        repository.update(winner)
    }

    fun delete(winner: WinnerEntity) {
        repository.delete(winner)
    }

    fun deleteAllWinners() {
        repository.deleteAllWinners()
    }

    fun getAllWinners(): LiveData<List<WinnerEntity>> {
        return allWinners
    }
}