package edu.wpi.ceflanagan_kjmunz.outfit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "OutfitListFragment"

class OutfitListFragment : Fragment() {

    private lateinit var outfitRecyclerView: RecyclerView
    private var adapter: OutfitAdapter? = null

    private val outfitListViewModel: OutfitListViewModel by lazy {
        ViewModelProvider(this).get(OutfitListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total outfits: ${outfitListViewModel.outfits.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_outfits_list, container, false)
        outfitRecyclerView =
            view.findViewById(R.id.outfits_recycler_view) as RecyclerView
        outfitRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()
        Log.d(TAG, "outfitRecyclerView attributes set")

        return view
    }


companion object {
        fun newInstance(): OutfitListFragment {
            return OutfitListFragment()
        }
    }

    private inner class OutfitHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        private lateinit var outfit: Outfit

        private val outfitName: TextView = itemView.findViewById(R.id.outfit_name)
        private val clothesName: TextView = itemView.findViewById(R.id.clothing_names)

        fun bind(outfit: Outfit) {
            this.outfit = outfit
                outfitName.text = outfit.name
                clothesName.text = outfit.top.name + ", " + outfit.bottom.name + ", " + outfit.accessory.name
            }
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

    private fun updateUI() {
        Log.d(TAG,"updateUI called")
        val outfits = outfitListViewModel.outfits
        adapter = OutfitAdapter(outfits)
        outfitRecyclerView.adapter = adapter
    }

}
