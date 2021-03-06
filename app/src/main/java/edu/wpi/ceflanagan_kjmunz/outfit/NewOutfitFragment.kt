package edu.wpi.ceflanagan_kjmunz.outfit

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import java.lang.Exception
import java.util.*

private const val TAG = "NewOutfitFragment"

class NewOutfitFragment : Fragment() {
    interface Callbacks {
        fun onNewOutfitSaved()
        fun onNewOutfitRequested()
        fun onNavSearch()
        fun onNavCloset()
        fun onNavOutfits()
    }


    private lateinit var navCloset : ImageView
    private lateinit var navSearch : ImageView
    private lateinit var navOutfits : ImageView

    private lateinit var topsRecyclerView: RecyclerView
    private lateinit var bottomsRecyclerView: RecyclerView
    private lateinit var accsRecyclerView: RecyclerView
    private var topAdapter: ClothingAdapter? = ClothingAdapter(emptyList())
    private var bottomAdapter: ClothingAdapter? = ClothingAdapter(emptyList())
    private var accAdapter: ClothingAdapter? = ClothingAdapter(emptyList())
    private var highlightedTops: LinkedList<View> = LinkedList<View>(emptyList())
    private var highlightedBottoms: LinkedList<View> = LinkedList<View>(emptyList())
    private var highlightedAccessories: LinkedList<View> = LinkedList<View>(emptyList())
    private lateinit var outfitNameEditText: EditText
    private var setTop: String? = null
    private var setBottom: String? = null
    private var setAccessory: String? = null
    private var photoTop: String? = null
    private var photoBottom: String? = null
    private var photoAccessory: String? = null
    private lateinit var outfitName: String
    private lateinit var outfit: Outfit
    private lateinit var saveOutfit: FloatingActionButton
    private var callbacks: NewOutfitFragment.Callbacks? = null


    private val closetViewModel: ClosetViewModel by lazy {
        ViewModelProvider(this).get(ClosetViewModel::class.java)
    }

    private val outfitViewModel: OutfitViewModel by lazy {
        ViewModelProvider(this).get(OutfitViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach() called")
        super.onAttach(context)
        callbacks = context as NewOutfitFragment.Callbacks?
    }

    companion object {
        fun newInstance(): NewOutfitFragment {
            return NewOutfitFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        outfitName = "Sample Outfit"
        outfit = Outfit(UUID.randomUUID(), outfitName, setTop, setBottom, setAccessory, photoTop, photoBottom, photoAccessory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_new_outfit, container, false)
        navCloset = view.findViewById(R.id.closet)
        navOutfits = view.findViewById(R.id.outfits)
        navSearch = view.findViewById(R.id.search)

        topsRecyclerView = view.findViewById(R.id.tops_list) as RecyclerView
        bottomsRecyclerView = view.findViewById(R.id.bottoms_list) as RecyclerView
        accsRecyclerView = view.findViewById(R.id.acc_list) as RecyclerView
        topsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        topsRecyclerView.adapter = topAdapter
        bottomsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bottomsRecyclerView.adapter = bottomAdapter
        accsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        accsRecyclerView.adapter = accAdapter
        saveOutfit = view.findViewById(R.id.add_fit)
        outfitNameEditText = view.findViewById(R.id.outfit_name)

        navCloset.setOnClickListener {
            callbacks?.onNavCloset()
        }
        navSearch.setOnClickListener {
            callbacks?.onNavSearch()
        }
        navOutfits.setOnClickListener {
            callbacks?.onNavOutfits()
        }
//        updateUI()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated() called")
        super.onViewCreated(view, savedInstanceState)
        closetViewModel.topsLiveData.observe(
            viewLifecycleOwner,
            { tops ->
                tops?.let {
                    Log.i(TAG, "Got tops ${tops.size}")
                    // updateUITops
                    topAdapter = ClothingAdapter(tops)
                    topsRecyclerView.adapter = topAdapter
                }
                Log.d(TAG, "Tops Observer called")
            })
        closetViewModel.bottomsLiveData.observe(
            viewLifecycleOwner,
            { bottoms ->
                bottoms?.let {
                    Log.i(TAG, "Got bottoms ${bottoms.size}")
                    // updateUIBottoms
                    bottomAdapter = ClothingAdapter(bottoms)
                    bottomsRecyclerView.adapter = bottomAdapter
                }
                Log.d(TAG, "Bottoms Observer called")
            })
        closetViewModel.accsLiveData.observe(
            viewLifecycleOwner,
            { accs ->
                accs?.let {
                    Log.i(TAG, "Got scores ${accs.size}")
                    // updateUIAccs
                    accAdapter = ClothingAdapter(accs)
                    accsRecyclerView.adapter = accAdapter
                }
                Log.d(TAG, "Accs Observer called")
            })

        saveOutfit.setOnClickListener { view: View ->
            outfit.name = outfitName
            outfit.top = setTop
            outfit.bottom = setBottom
            outfit.accessory = setAccessory
            outfit.topPhotoFileName = photoTop
            outfit.bottomPhotoFileName = photoBottom
            outfit.accessoryPhotoFileName = photoAccessory
            Log.d(TAG, "Save Button clicked, inserting outfit into database")
            outfitViewModel.addOutfit(outfit)
            callbacks?.onNewOutfitSaved()
        }
        }

//    private fun updateUI() {
//        topAdapter = ClothingAdapter(tops)
//        topsRecyclerView.adapter = topAdapter
//        bottomAdapter = ClothingAdapter(bottoms)
//        bottomsRecyclerView.adapter = bottomAdapter
//        accAdapter = ClothingAdapter(accs)
//        accsRecyclerView.adapter = accAdapter
//    }

    private inner class ClothingHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var clothing: Clothing
        private lateinit var photoFile: File
        private lateinit var photoUri: Uri
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val nameTextView: TextView = itemView. findViewById(R.id.name)

        init {
            itemView.setOnClickListener(this)
        }
        fun bind(clothing: Clothing) {
            this.clothing = clothing
            nameTextView.text = clothing.name;
            Log.d(TAG, "NameTextView set to " + clothing.name)
            updatePhotoView()

        }

            private fun updatePhotoView() {
                try {
                photoFile = closetViewModel.getPhotoFile(clothing)
                photoUri = FileProvider.getUriForFile(requireActivity(), "edu.wpi.ceflanagan_kjmunz.outfit.fileprovider", photoFile)
                if (photoFile.exists()) {
                    val bitmap = getScaledAndRotatedBitmap(photoFile.path, requireActivity())
                    image.setImageBitmap(bitmap)
                } else {
                    image.setImageDrawable(null)
                }
            }
                catch (e : Exception) {
                    image.setImageDrawable(null)
                }}

        override fun onClick(v: View) {
            Log.d(TAG, clothing.type.toString() + " " + clothing.name + " selected")

            val trans = v.resources.getColor(R.color.white)
            if (clothing.type.equals(ClothingType.TOP))
            {
                for (i in 0 until highlightedTops.size) {
                    highlightedTops[i].setBackgroundColor(trans)
                    Log.d(TAG, "Unhighlighted all tops")
                }
                highlightedTops.add(v)
                setTop = clothing.name
                photoTop = clothing.photoFileName
            }
            else if (clothing.type.equals(ClothingType.BOTTOM))
            {
                for (i in 0 until highlightedBottoms.size) {
                    highlightedBottoms[i].setBackgroundColor(trans)
                    Log.d(TAG, "Unhighlighted all bottoms")
                }
                highlightedBottoms.add(v)
                setBottom = clothing.name
                photoBottom = clothing.photoFileName
            }
            else if (clothing.type.equals(ClothingType.ACCESSORY))
            {
                for (i in 0 until highlightedAccessories.size) {
                    highlightedAccessories[i].setBackgroundColor(trans)
                    Log.d(TAG, "Unhighlighted all accessories")
                }
                highlightedAccessories.add(v)
                setAccessory = clothing.name
                photoAccessory = clothing.photoFileName
            }

            val color = v.resources.getColor(R.color.app_default)
            v.setBackgroundColor(color)

            //val rect = Rect(v.left, v.top, v.right, v.bottom)
        }
    }

    private inner class ClothingAdapter(var clothes: List<Clothing>) : RecyclerView.Adapter<ClothingHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingHolder {
            val view = layoutInflater.inflate(R.layout.list_item_clothing_delete, parent, false)
            return ClothingHolder(view)
        }

        override fun getItemCount() = clothes.size

        override fun onBindViewHolder(holder: ClothingHolder, position: Int) {
            val clothing = clothes[position]
            holder.bind(clothing)
        }
    }

    override fun onStart() {
        super.onStart()

        outfitNameEditText.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // blank
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                outfitName = outfitNameEditText.text.toString()
                outfit.name = outfitName
                Log.d(TAG, "Outfit name set to: " + outfitName)
            }

            override fun afterTextChanged(p0: Editable?) {
                // blank
            }
        })
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach() called")
        super.onDetach()
        callbacks = null
    }
}