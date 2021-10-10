package edu.wpi.ceflanagan_kjmunz.outfit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "ClosetFragment"

class ClosetFragment : Fragment() {
    private lateinit var topsRecyclerView: RecyclerView
    private lateinit var bottomsRecyclerView: RecyclerView
    private lateinit var accsRecyclerView: RecyclerView
    private var topAdapter: ClothingAdapter? = ClothingAdapter(emptyList())
    private var bottomAdapter: ClothingAdapter? = ClothingAdapter(emptyList())
    private var accAdapter: ClothingAdapter? = ClothingAdapter(emptyList())

    private val closetViewModel: ClosetViewModel by lazy {
        ViewModelProvider(this).get(ClosetViewModel::class.java)
    }

    companion object {
        fun newInstance(): ClosetFragment {
            return ClosetFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_closet, container, false)
        topsRecyclerView = view.findViewById(R.id.tops_list) as RecyclerView
        topsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        topsRecyclerView.adapter = topAdapter

        bottomsRecyclerView = view.findViewById(R.id.bottoms_list) as RecyclerView
        bottomsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bottomsRecyclerView.adapter = bottomAdapter

        accsRecyclerView = view.findViewById(R.id.acc_list) as RecyclerView
        accsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        accsRecyclerView.adapter = accAdapter
        //updateUI()
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
    }

    /*
    private fun updateUI() {
        topAdapter = ClothingAdapter(closetViewModel.tops)
        topsRecyclerView.adapter = topAdapter
        bottomAdapter = ClothingAdapter(closetViewModel.bottoms)
        bottomsRecyclerView.adapter = bottomAdapter
        accAdapter = ClothingAdapter(closetViewModel.accs)
        accsRecyclerView.adapter = accAdapter
    }
*/

    private inner class ClothingHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var clothing: Clothing
        private val nameTextView: TextView = itemView.findViewById(R.id.name)
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val delete: ImageView = itemView.findViewById(R.id.delete)

        init {
            itemView.setOnClickListener(this)
        }
        fun bind(clothing: Clothing) {
            this.clothing = clothing
            nameTextView.text = clothing.name;
            Log.d(TAG, "Set to " + clothing.name)
            // TODO: set delete on click listener
            /* TODO: set image
            image.setImageResource(clothingImage.get())
            */
        }
        override fun onClick(v: View) {
            //callbacks?.onScoreSelected(score.id) TODO: stretch go to edit view
        }
    }

    private inner class ClothingAdapter(var clothes: List<Clothing>) : RecyclerView.Adapter<ClothingHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingHolder {
            val view = layoutInflater.inflate(R.layout.list_item_clothing, parent, false)
            return ClothingHolder(view)
        }

        override fun getItemCount() = clothes.size

        override fun onBindViewHolder(holder: ClothingHolder, position: Int) {
            val clothing = clothes[position]
            holder.bind(clothing)
        }
    }
}