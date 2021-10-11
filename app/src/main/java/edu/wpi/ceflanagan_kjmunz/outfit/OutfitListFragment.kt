package edu.wpi.ceflanagan_kjmunz.outfit

import android.content.Context
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
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val TAG = "OutfitListFragment"

class OutfitListFragment : Fragment() {

    interface Callbacks {
        fun onNewOutfitRequested()
    }

    private lateinit var outfitRecyclerView: RecyclerView
    private var adapter: OutfitAdapter? = null
    private lateinit var  newButton: FloatingActionButton
    private var callbacks: Callbacks? = null

    private val outfitListViewModel: OutfitListViewModel by lazy {
        ViewModelProvider(this).get(OutfitListViewModel::class.java)
    }

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
        val view = inflater.inflate(R.layout.fragment_outfits_list, container, false)
        newButton = view.findViewById(R.id.add_fit)

        outfitRecyclerView =
            view.findViewById(R.id.outfits_recycler_view) as RecyclerView
        outfitRecyclerView.layoutManager = LinearLayoutManager(context)

        Log.d(TAG, "outfitRecyclerView attributes set")

        newButton.setOnClickListener{
            callbacks?.onNewOutfitRequested()
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
        outfitListViewModel.outfitLiveData.observe(
            viewLifecycleOwner,
            { outfits ->
                outfits?.let {
                    Log.i(TAG, "Got outfits ${outfits.size}")
                    // updateUITops
                    adapter = OutfitAdapter(outfits)
                    outfitRecyclerView.adapter = adapter
                }})
    }

    private inner class OutfitHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        private lateinit var outfit: Outfit

        private val outfitName: TextView = itemView.findViewById(R.id.outfit_name)
        private val clothesName: TextView = itemView.findViewById(R.id.clothing_names)

        fun bind(outfit: Outfit) {
            this.outfit = outfit
                outfitName.text = outfit.name
                var clothes : String = outfit.top!!.name + ", " + outfit.bottom!!.name + ", " + outfit.accessory!!.name
            clothesName.setText(clothes)
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

    override fun onDetach() {
        Log.d(TAG, "onDetach() called")
        super.onDetach()
        callbacks = null
    }



}
