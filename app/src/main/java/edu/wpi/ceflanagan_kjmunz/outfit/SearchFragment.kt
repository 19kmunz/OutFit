package edu.wpi.ceflanagan_kjmunz.outfit

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.wpi.ceflanagan_kjmunz.outfit.api.KohlsClothing
import org.json.JSONObject
import java.util.*
import android.graphics.drawable.Drawable
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors


private const val TAG = "SearchFragment"

class SearchFragment : Fragment() {
    interface Callbacks {
        fun onNavCloset()
        fun onNavOutfits()
    }

    private var callbacks: Callbacks? = null
    private val executor = Executors.newSingleThreadExecutor()

    private var kohlsClothingList: List<KohlsClothing> = emptyList()

    private lateinit var navCloset : ImageView
    private lateinit var navOutfits : ImageView

    private lateinit var ClothingRecyclerView: RecyclerView
    private var adapter: ClothingAdapter? = ClothingAdapter(emptyList())

    private lateinit var TypeSpinner: Spinner
    private lateinit var KeywordsInput: EditText

    private val kohlsViewModel: KohlsViewModel by lazy {
        ViewModelProvider(this).get(KohlsViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach() called")
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate() called")
        super.onCreate(savedInstanceState)


        kohlsViewModel.kohlsLiveData.observe(
            this,
            Observer { responseString ->
                Log.d(TAG, "Response received: $responseString")
                if (responseString != null) {
                    kohlsClothingList = emptyList();
                    val jsonObj = JSONObject(responseString)
                    val products = jsonObj.getJSONObject("payload").getJSONArray("products")
                    val type = kohlsViewModel.typeLiveData.value ?: ClothingType.ACCESSORY
                    val tempList = mutableListOf<KohlsClothing>()
                    for(i in 0 until products.length()){
                        val currKohls = products.getJSONObject(i)
                        val name = currKohls.getString("productTitle")
                        val imgLink = currKohls.getJSONObject("image").getString("url")
                        tempList.add(KohlsClothing(name, imgLink, type))
                    }
                    kohlsClothingList = Collections.unmodifiableList(tempList)
                    Log.d(TAG, "Kohls Clothing List Updated to size " + kohlsClothingList.size)
                }
                adapter = ClothingAdapter(kohlsClothingList)
                ClothingRecyclerView.adapter = adapter
            })
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
        TypeSpinner = view.findViewById(R.id.type_input)
        KeywordsInput = view.findViewById(R.id.name_input)
        ClothingRecyclerView= view.findViewById(R.id.clothing_recycler_view)
        ClothingRecyclerView.layoutManager = GridLayoutManager(context, 3)
        ClothingRecyclerView.adapter = adapter

        navCloset.setOnClickListener {
            callbacks?.onNavCloset()
        }
        navOutfits.setOnClickListener {
            callbacks?.onNavOutfits()
        }

        ArrayAdapter.createFromResource(
            view.context,
            R.array.types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            TypeSpinner.adapter = adapter
        }

        TypeSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                when (parentView?.getItemAtPosition(position) as String) {
                    "Top" -> {
                        kohlsViewModel.loadClothes(ClothingType.TOP)
                    }
                    "Bottom" -> {
                        kohlsViewModel.loadClothes(ClothingType.BOTTOM)
                    }
                    "Accessory" -> {
                        kohlsViewModel.loadClothes(ClothingType.ACCESSORY)
                    }
                    else -> {
                        kohlsViewModel.loadClothes(ClothingType.NONE)
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })

        return view;
    }

    override fun onStart() {
        super.onStart()

        KeywordsInput.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // blank
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                kohlsViewModel.loadClothes(KeywordsInput.text.toString())
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

    private inner class ClothingHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var clothing: KohlsClothing
        private val nameTextView: TextView = itemView.findViewById(R.id.name)
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val add: ImageView = itemView.findViewById(R.id.add)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(clothing: KohlsClothing) {
            this.clothing = clothing
            nameTextView.text = clothing.name;
            Log.d(TAG, "Set to " + clothing.name)
            executor.execute {
                val inputStream: InputStream = URL(clothing.imageLink).getContent() as InputStream
                val drawable = Drawable.createFromStream(inputStream, "src name")
                activity?.runOnUiThread(java.lang.Runnable {
                    image.setImageDrawable(drawable)
                })
            }
            /*
            add.setOnClickListener {
                clothingRepository.addClothing(clothing)
                callback?.onAddClothing() // return to closet
            }*/
        }
        override fun onClick(v: View) {
            //callbacks?.onScoreSelected(score.id) TODO: stretch goal to edit view
        }
    }

    private inner class ClothingAdapter(var clothing: List<KohlsClothing>) : RecyclerView.Adapter<ClothingHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingHolder {
            val view = layoutInflater.inflate(R.layout.list_item_clothing_add, parent, false)
            return ClothingHolder(view)
        }

        override fun getItemCount() = clothing.size

        override fun onBindViewHolder(holder: ClothingHolder, position: Int) {
            val clothing = clothing[position]
            holder.bind(clothing)
        }
    }
}