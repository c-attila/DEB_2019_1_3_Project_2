package hu.mndalex.prototype.data

import androidx.room.*

@Dao
interface WinnerDAO {

    @Insert
    fun insert(winner: WinnerEntity)

    @Update
    fun update(winner: WinnerEntity)

    @Delete
    fun delete(winner: WinnerEntity)

    @Query("DELETE FROM winner_table")
    fun deleteAllWinners()

    @Query("SELECT * FROM winner_table ORDER BY time DESC")
    fun getAllWinners(): List<WinnerEntity>
}