package edu.wpi.ceflanagan_kjmunz.outfit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import android.widget.AdapterView




class NewClothingFragment : Fragment() {
    private lateinit var clothing: Clothing
    // private lateinit var photoFile: File
    // private lateinit var photoUri: File

    private lateinit var NameEditText: EditText
    private lateinit var TypeSpinner: Spinner
    // private lateinit var photoImageView: ImageView
    // private lateinit var cameraButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clothing = Clothing("Sample Clothing", ClothingType.NONE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_clothing, container, false)
        NameEditText = view.findViewById(R.id.name_input) as EditText
        TypeSpinner = view.findViewById(R.id.type_input) as Spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
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

        val nameWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // blank
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                clothing.name = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                // blank
            }
        }
        NameEditText.addTextChangedListener(nameWatcher)
    }
}