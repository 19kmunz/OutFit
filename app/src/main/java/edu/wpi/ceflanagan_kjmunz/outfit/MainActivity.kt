package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.wpi.ceflanagan_kjmunz.outfit.ui.main.MainFragment

class MainActivity : AppCompatActivity(), ClosetFragment.Callbacks, NewClothingFragment.Callbacks, OutfitListFragment.Callbacks, NewOutfitFragment.Callbacks, SearchFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ClosetFragment())
                .commitNow()
        }
    }

    override fun onNewClothingRequested() {
        val fragment = NewClothingFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onNewOutfitRequested() {
        val fragment = NewOutfitFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onNewOutfitSaved() {
        val fragment = OutfitListFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onExit() {
        val fragment = ClosetFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onNavCloset() {
        val fragment = ClosetFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onNavOutfits() {
        val fragment = OutfitListFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onNavSearch() {
        val fragment = SearchFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}