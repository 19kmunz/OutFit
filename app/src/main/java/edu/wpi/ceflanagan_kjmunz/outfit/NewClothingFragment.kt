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
import androidx.lifecycle.ViewModelProvider
import java.util.*

private const val TAG = "NewClothingFragment"

class NewClothingFragment : Fragment() {
    interface Callbacks {
        fun onExit()
    }

    private var callbacks: Callbacks? = null

    private var assumeInitInDb = false

    private lateinit var clothing: Clothing
    // private lateinit var photoFile: File
    // private lateinit var photoUri: File

    private lateinit var NameEditText: EditText
    private lateinit var TypeSpinner: Spinner
    private lateinit var SaveButton: Button
    private lateinit var ExitButton: Button
    // private lateinit var photoImageView: ImageView
    // private lateinit var cameraButton: ImageButton

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
        clothing = Clothing(UUID.randomUUID(), "Sample Clothing", ClothingType.NONE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_clothing, container, false)
        NameEditText = view.findViewById(R.id.name_input) as EditText
        TypeSpinner = view.findViewById(R.id.type_input) as Spinner
        SaveButton = view.findViewById(R.id.save) as Button
        ExitButton = view.findViewById(R.id.exit) as Button
        // Create an ArrayAdapter using the string array and a default spinner layout

        SaveButton.setOnClickListener { view: View ->
            if(assumeInitInDb) {
                Log.d(TAG, "Save Button Clicked, Updating Clothing in Db")
                clothingViewModel.saveClothing(clothing)
            } else {
                Log.d(TAG, "Save Button Clicked, Inserting Clothing to Db")
                clothingViewModel.addClothing(clothing)
                clothingViewModel.loadClothing(clothing.id);
                assumeInitInDb = true;
            }
        }

        ExitButton.setOnClickListener{
            callbacks?.onExit()
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
                val selectedItemText = parentView?.getItemAtPosition(position) as String
                if(selectedItemText == "Top") {
                    clothing.type = ClothingType.TOP
                } else if (selectedItemText == "Bottom") {
                    clothing.type = ClothingType.BOTTOM
                } else if(selectedItemText == "Accessory") {
                    clothing.type = ClothingType.ACCESSORY
                } else {
                    clothing.type = ClothingType.NONE
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
        return view
    }

    override fun onStart() {
        super.onStart()

        NameEditText.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // blank
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                clothing.name = NameEditText.text.toString()
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