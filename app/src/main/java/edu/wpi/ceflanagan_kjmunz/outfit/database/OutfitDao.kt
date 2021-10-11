package edu.wpi.ceflanagan_kjmunz.outfit.database

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.wpi.ceflanagan_kjmunz.outfit.Outfit
import java.util.*

@Dao
interface OutfitDao {
    @Query("SELECT * FROM outfit")
    fun getOutfits(): LiveData<List<Outfit>>

    @Query("SELECT * FROM outfit WHERE id=(:id)")
    fun getOutfit(id: UUID): LiveData<Outfit?>

    @Update
    fun updateOutfit(outfit: Outfit)

    @Insert
    fun addOutfit(outfit: Outfit)

    @Delete
    fun deleteOutfit(outfit: Outfit)
}