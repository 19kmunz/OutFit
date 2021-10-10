package edu.wpi.ceflanagan_kjmunz.outfit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "NewOutfitFragment"

class NewOutfitFragment : Fragment() {
    private lateinit var topsRecyclerView: RecyclerView
    private lateinit var bottomsRecyclerView: RecyclerView
    private lateinit var accsRecyclerView: RecyclerView
    private var topAdapter: ClothingAdapter? = ClothingAdapter(emptyList())
    private var bottomAdapter: ClothingAdapter? = ClothingAdapter(emptyList())
    private var accAdapter: ClothingAdapter? = ClothingAdapter(emptyList())

    private val closetViewModel: ClosetViewModel by lazy {
        ViewModelProvider(this).get(ClosetViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total tops: ${closetViewModel.tops.size}")
    }

    companion object {
        fun newInstance(): NewOutfitFragment {
            return NewOutfitFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_new_outfit, container, false)
        topsRecyclerView = view.findViewById(R.id.tops_list) as RecyclerView
        bottomsRecyclerView = view.findViewById(R.id.bottoms_list) as RecyclerView
        accsRecyclerView = view.findViewById(R.id.acc_list) as RecyclerView
        topsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        topsRecyclerView.adapter = topAdapter
        bottomsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bottomsRecyclerView.adapter = bottomAdapter
        accsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        accsRecyclerView.adapter = accAdapter
        updateUI()
        return view
    }

    private fun updateUI() {
        topAdapter = ClothingAdapter(closetViewModel.tops)
        topsRecyclerView.adapter = topAdapter
        bottomAdapter = ClothingAdapter(closetViewModel.bottoms)
        bottomsRecyclerView.adapter = bottomAdapter
        accAdapter = ClothingAdapter(closetViewModel.accs)
        accsRecyclerView.adapter = accAdapter
    }

    private inner class ClothingHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var clothing: Clothing
        private val nameTextView: TextView = itemView. findViewById(R.id.name)

        init {
            itemView.setOnClickListener(this)
        }
        fun bind(clothing: Clothing) {
            this.clothing = clothing
            nameTextView.text = clothing.name;
            Log.d(TAG, "Set to " + clothing.name)
        }
        override fun onClick(v: View) {
            Log.d(TAG, clothing.type.toString() + " " + clothing.name + " selected")

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
}