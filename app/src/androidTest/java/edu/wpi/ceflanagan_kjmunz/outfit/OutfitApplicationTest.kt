package edu.wpi.ceflanagan_kjmunz.outfit

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class OutfitApplicationTest {

    private lateinit var outfitViewModel: OutfitViewModel
    private lateinit var outfitListViewModel: OutfitListViewModel
    private lateinit var clothingViewModel: ClothingViewModel
    private lateinit var closetViewModel: ClosetViewModel
    private lateinit var outfit: Outfit
    private lateinit var top: Clothing
    private lateinit var bottom: Clothing
    private lateinit var accessory: Clothing
    private lateinit var clothingApplication: ClothingApplication
    private lateinit var appContext : Context


    @Before
        fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        outfitViewModel = OutfitViewModel()
        outfitListViewModel = OutfitListViewModel()
        closetViewModel = ClosetViewModel()
        clothingViewModel = ClothingViewModel()

        top = Clothing(UUID.randomUUID(), "test top", ClothingType.TOP)
        bottom = Clothing(UUID.randomUUID(), "test bottom", ClothingType.BOTTOM)
        accessory = Clothing(UUID.randomUUID(), "test accessory", ClothingType.ACCESSORY)
        outfit = Outfit(UUID.randomUUID(), "test outfit", "test", "test", "test", null, null, null)


        clothingApplication = ClothingApplication()
        clothingApplication.onCreate()
    }

    fun validOutfit(outfit: Outfit) : Boolean {
        if (outfit == null ) {
            return false
        }
        return true
    }

    fun validClothing(clothing: Clothing) : Boolean {
        if (clothing == null ) {
            return false
        }
        return true
    }

    @Test
    fun validClothingNoImageTest() {
        bottom = Clothing(UUID.randomUUID(), "test bottom 2", ClothingType.BOTTOM)
        assertTrue(validClothing(bottom))
    }

    @Test
    fun validOutfitFromClothesTest() {
        outfit = Outfit(UUID.randomUUID(), "Test Outfit 2", top.name, bottom.name, accessory.name, top.photoFileName, bottom.photoFileName, accessory.photoFileName)
        assertTrue(validOutfit(outfit))
    }

    @Test
    fun validOutfitTopBottomAccTest() {
        outfit = Outfit(UUID.randomUUID(), "test outfit", "test top", "test bottom", "test accessory", null, null, null)
        assertTrue(validOutfit(outfit))
    }

    @Test
    fun validOutfitTopBottomNoAccTest() {
        outfit = Outfit(UUID.randomUUID(), "test outfit", "test top", "test bottom", null, null, null, null)
        assertTrue(validOutfit(outfit))
    }

    @Test
    fun validOutfitTopNoBottomAccTest() {
        outfit = Outfit(UUID.randomUUID(), "test outfit", "test top", null, "test accessory", null, null, null)
        assertTrue(validOutfit(outfit))
    }

    @Test
    fun validOutfitTopNoBottomNoAccTest() {
        outfit = Outfit(UUID.randomUUID(), "test outfit", "test top", null, null, null, null, null)
        assertTrue(validOutfit(outfit))
    }

    /*
    @Test
    fun useAppContext() {
        // Context of the app under test.
        Assert.assertEquals("edu.wpi.ceflanagan_kjmunz.outfit", appContext.packageName)
    }

     */

    /* Cannot invoke setValue on a background thread
    @Test
    fun testClothesAddsDB() {
        clothingViewModel.addClothing(top)
        clothingViewModel.loadClothing(top.id)
        assertEquals(clothingViewModel.clothingLiveData, top.id)
    }

     */

    @Test
    fun testClothesDeleteDB() {
        clothingViewModel.addClothing(top)
        closetViewModel.deleteClothing(top)
        assertEquals(clothingViewModel.clothingLiveData.value, null)
    }

    /* Cannot invoke setValue on a background thread
    @Test
    fun testOutfitAddsDB() {
        outfitViewModel.addOutfit(outfit)
        outfitViewModel.loadOutfit(outfit.id)
        assertEquals(outfitListViewModel.outfitLiveData.value, outfit)
    }

     */

    @Test
    fun testOutfitDeleteDB() {
        outfitViewModel.addOutfit(outfit)
        outfitListViewModel.deleteOutfit(outfit)
        assertEquals(outfitViewModel.outfitLiveData.value, null)
    }


}
