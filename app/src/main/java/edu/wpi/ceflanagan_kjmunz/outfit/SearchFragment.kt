package edu.wpi.ceflanagan_kjmunz.outfit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

private const val TAG = "SearchFragment"

class SearchFragment : Fragment() {
    interface Callbacks {
        fun onNavCloset()
        fun onNavOutfits()
    }

    private var callbacks: Callbacks? = null

    private lateinit var navCloset : ImageView
    private lateinit var navOutfits : ImageView

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach() called")
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        navCloset = view.findViewById(R.id.closet)
        navOutfits = view.findViewById(R.id.outfits)

        navCloset.setOnClickListener {
            callbacks?.onNavCloset()
        }
        navOutfits.setOnClickListener {
            callbacks?.onNavOutfits()
        }
        return view;
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach() called")
        super.onDetach()
        callbacks = null
    }
}