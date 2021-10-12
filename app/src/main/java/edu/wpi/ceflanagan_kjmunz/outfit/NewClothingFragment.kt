package edu.wpi.ceflanagan_kjmunz.outfit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import getScaledAndRotatedBitmap
import java.io.File
import java.util.*
import androidx.lifecycle.Observer

private const val TAG = "NewClothingFragment"
private const val REQUEST_PHOTO = 1

class NewClothingFragment : Fragment() {
    interface Callbacks {
        fun onExit()
        fun onNavSearch()
        fun onNavCloset()
        fun onNavOutfits()
    }

    private var callbacks: Callbacks? = null

    private lateinit var navCloset : ImageView
    private lateinit var navSearch : ImageView
    private lateinit var navOutfits : ImageView

    private var assumeInitInDb = false

    private lateinit var clothing: Clothing
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    private lateinit var NameEditText: EditText
    private lateinit var TypeSpinner: Spinner
    private lateinit var SaveButton: Button
    private lateinit var ExitButton: Button
    private lateinit var photoImageView: ImageView
    private lateinit var cameraButton: ImageButton

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
        navCloset = view.findViewById(R.id.closet)
        navOutfits = view.findViewById(R.id.outfits)
        navSearch = view.findViewById(R.id.search)

        NameEditText = view.findViewById(R.id.name_input) as EditText
        TypeSpinner = view.findViewById(R.id.type_input) as Spinner
        SaveButton = view.findViewById(R.id.save) as Button
        ExitButton = view.findViewById(R.id.exit) as Button
        photoImageView = view.findViewById(R.id.image)
        cameraButton = view.findViewById(R.id.image_button)
        // Create an ArrayAdapter using the string array and a default spinner layout

        navCloset.setOnClickListener {
            callbacks?.onNavCloset()
        }
        navSearch.setOnClickListener {
            callbacks?.onNavSearch()
        }
        navOutfits.setOnClickListener {
            callbacks?.onNavOutfits()
        }

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

        cameraButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager

            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
            if(resolvedActivity == null) {
                Log.d(TAG, "Camera UnResolved")
                isEnabled = false;
            }

            setOnClickListener { view : View ->
                Log.d(TAG, "Camera Button Clicked!")
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                val cameraActivities: List<ResolveInfo> = packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)

                for(cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated() called, begin observation")
        photoFile = clothingViewModel.getPhotoFile(clothing)
        photoUri = FileProvider.getUriForFile(requireActivity(), "edu.wpi.ceflanagan_kjmunz.outfit.fileprovider", photoFile)
        clothingViewModel.clothingLiveData.observe(
            viewLifecycleOwner,
            Observer { clothing ->
                clothing?.let {
                    this.clothing = clothing
                    photoFile = clothingViewModel.getPhotoFile(clothing)
                    photoUri = FileProvider.getUriForFile(requireActivity(), "edu.wpi.ceflanagan_kjmunz.outfit.fileprovider", photoFile)
                    Log.d(TAG, "photoUri: $photoUri")
                    updateUI()
                }
            })
    }

    private fun updateUI() {
        updatePhotoView()
    }

    private fun updatePhotoView() {
        if (photoFile.exists()) {
            val bitmap = getScaledAndRotatedBitmap(photoFile.path, requireActivity())
            photoImageView.setImageBitmap(bitmap)
        } else {
            photoImageView.setImageDrawable(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_PHOTO -> {
                requireActivity().revokeUriPermission(photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updatePhotoView()
            }
        }
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
        requireActivity().revokeUriPermission(photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }
}