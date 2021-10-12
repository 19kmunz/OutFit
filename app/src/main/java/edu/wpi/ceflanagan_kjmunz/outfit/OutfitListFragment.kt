package edu.wpi.ceflanagan_kjmunz.outfit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import getScaledAndRotatedBitmap
import java.io.File

private const val TAG = "OutfitListFragment"

class OutfitListFragment : Fragment() {
    interface Callbacks {
        fun onNewOutfitRequested()
        fun onNavSearch()
        fun onNavCloset()
    }


    private lateinit var navCloset : ImageView
    private lateinit var navSearch : ImageView

    private lateinit var outfitRecyclerView: RecyclerView
    private var adapter: OutfitAdapter? = null
    private lateinit var  newButton: FloatingActionButton
    private var callbacks: Callbacks? = null

    private val outfitListViewModel: OutfitListViewModel by lazy {
        ViewModelProvider(this).get(OutfitListViewModel::class.java)
    }

    private val clothingViewModel: ClothingViewModel by lazy {
        ViewModelProvider(this).get(ClothingViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach() called")
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.d(TAG, "Total outfits: ${outfitListViewModel.outfits.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_outfits_list, container, false)
        newButton = view.findViewById(R.id.add_fit)

        navCloset = view.findViewById(R.id.closet)
        navSearch = view.findViewById(R.id.search)

        outfitRecyclerView =
            view.findViewById(R.id.outfits_recycler_view) as RecyclerView
        outfitRecyclerView.layoutManager = LinearLayoutManager(context)

        Log.d(TAG, "outfitRecyclerView attributes set")

        newButton.setOnClickListener{
            callbacks?.onNewOutfitRequested()
        }

        navCloset.setOnClickListener {
            callbacks?.onNavCloset()
        }
        navSearch.setOnClickListener {
            callbacks?.onNavSearch()
        }

        return view
    }


companion object {
        fun newInstance(): OutfitListFragment {
            return OutfitListFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated() called")
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "observer abt to be called")

        outfitListViewModel.outfitLiveData.observe(
            viewLifecycleOwner,
            { outfits ->
                outfits?.let {
                    Log.i(TAG, "Got outfits ${outfits.size}")
                    updateUI(outfits)
                }
                Log.d(TAG, "Observer called")
            })

    }

    private inner class OutfitHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        private lateinit var outfit: Outfit
        private lateinit var photoTopFile: File
        private lateinit var photoTopUri: Uri
        private lateinit var photoBottomFile: File
        private lateinit var photoBottomUri: Uri
        private lateinit var photoAccessoryFile: File
        private lateinit var photoAccessoryUri: Uri

        private val delete: ImageView = itemView.findViewById(R.id.delete)
        private val imageTop: ImageView = itemView.findViewById(R.id.top_image)
        private val imageBottom: ImageView = itemView.findViewById(R.id.bottom_image)
        private val imageAccessory: ImageView = itemView.findViewById(R.id.accessory_image)

        private val outfitNameTextView: TextView = itemView.findViewById(R.id.outfit_name)
        private val clothesNameTextView: TextView = itemView.findViewById(R.id.clothing_names)

        fun bind(outfit: Outfit) {
            this.outfit = outfit

            var topText = "No top"
            var bottomText = "No bottom"
            var accText = "No accessories"


            outfitNameTextView.text = outfit.name
            if (outfit.top != null) {
                topText = outfit.top!!
            }
            if (outfit.bottom != null) {
                bottomText = outfit.bottom!!
            }
            if (outfit.accessory != null) {
                accText = outfit.accessory!!
            }

            var clothes: String = topText + ", " + bottomText + ", " + accText
            Log.d(TAG, "Clothes name set to " + clothes)
            clothesNameTextView.setText(clothes)
            delete.setOnClickListener {
                outfitListViewModel.deleteOutfit(outfit)
            }
            updateTopPhotoView()
            updateBottomPhotoView()
            updateAccessoryPhotoView()
        }


        private fun updateTopPhotoView() {
            photoTopFile = outfitListViewModel.getTopPhotoFile(outfit)
            photoTopUri = FileProvider.getUriForFile(
                requireActivity(),
                "edu.wpi.ceflanagan_kjmunz.outfit.fileprovider",
                photoTopFile
            )
            if (photoTopFile.exists()) {
                val bitmap = getScaledAndRotatedBitmap(photoTopFile.path, requireActivity())
                imageTop.setImageBitmap(bitmap)
            } else {
                imageTop.setImageDrawable(null)
            }
        }

        private fun updateBottomPhotoView() {
            photoBottomFile = outfitListViewModel.getBottomPhotoFile(outfit)
            photoBottomUri = FileProvider.getUriForFile(
                requireActivity(),
                "edu.wpi.ceflanagan_kjmunz.outfit.fileprovider",
                photoBottomFile
            )
            if (photoBottomFile.exists()) {
                val bitmap = getScaledAndRotatedBitmap(photoBottomFile.path, requireActivity())
                imageBottom.setImageBitmap(bitmap)
            } else {
                imageBottom.setImageDrawable(null)
            }
        }

        private fun updateAccessoryPhotoView() {
            photoAccessoryFile = outfitListViewModel.getAccessoryPhotoFile(outfit)
            photoAccessoryUri = FileProvider.getUriForFile(
                requireActivity(),
                "edu.wpi.ceflanagan_kjmunz.outfit.fileprovider",
                photoAccessoryFile
            )
            if (photoAccessoryFile.exists()) {
                val bitmap = getScaledAndRotatedBitmap(photoAccessoryFile.path, requireActivity())
                imageAccessory.setImageBitmap(bitmap)
            } else {
                imageAccessory.setImageDrawable(null)
            }
        }
    }

    private fun updateUI(outfits: List<Outfit>) {
        Log.d(TAG, "updateUI() called")
        adapter = OutfitAdapter(outfits)
        outfitRecyclerView.adapter = adapter
    }

    private inner class OutfitAdapter(var outfits: List<Outfit>)
        : RecyclerView.Adapter<OutfitHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : OutfitHolder {
            val view = layoutInflater.inflate(R.layout.list_item_outfit, parent, false)
            return OutfitHolder(view)
        }

        override fun getItemCount() = outfits.size

        override fun onBindViewHolder(holder: OutfitHolder, position: Int) {
            val outfit = outfits[position]
            holder.bind(outfit)
        }
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach() called")
        super.onDetach()
        callbacks = null
    }




}
