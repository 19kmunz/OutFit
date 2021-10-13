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

        outfit = Outfit(UUID.randomUUID(), "test outfit", "test top", "test bottom", "test accessory", "IMG_outfiticon.png", "IMG_shirtlogo.png", "IMG_magnifyingglass.png")
        top = Clothing(UUID.randomUUID(), "test top", ClothingType.TOP)
        bottom = Clothing(UUID.randomUUID(), "test bottom", ClothingType.BOTTOM)
        accessory  = Clothing(UUID.randomUUID(), "test accessory", ClothingType.ACCESSORY)
        clothingApplication = ClothingApplication()
        clothingApplication.onCreate()

    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        Assert.assertEquals("edu.wpi.ceflanagan_kjmunz.outfit", appContext.packageName)
    }

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
