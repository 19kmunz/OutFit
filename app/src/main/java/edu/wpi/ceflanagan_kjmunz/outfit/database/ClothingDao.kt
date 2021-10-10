package edu.wpi.ceflanagan_kjmunz.outfit.database

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.wpi.ceflanagan_kjmunz.outfit.Clothing
import edu.wpi.ceflanagan_kjmunz.outfit.ClothingType
import java.util.*

@Dao
interface ClothingDao {
    @Query("SELECT * FROM clothing")
    fun getClothes(): LiveData<List<Clothing>>


    @Query("SELECT * FROM clothing WHERE type=(:type)")
    fun getClothesByType(type: ClothingType): LiveData<List<Clothing>>

    @Query("SELECT * FROM clothing WHERE id=(:id)")
    fun getClothing(id: UUID): LiveData<Clothing?>

    @Update
    fun updateClothing(clothing: Clothing)

    @Insert
    fun addClothing(clothing: Clothing)

    @Delete
    fun deleteClothing(clothing: Clothing)
}