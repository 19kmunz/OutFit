package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.wpi.ceflanagan_kjmunz.outfit.ui.main.MainFragment

class MainActivity : AppCompatActivity(), ClosetFragment.Callbacks, NewClothingFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NewOutfitFragment())
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

    override fun onExit() {
        val fragment = ClosetFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}