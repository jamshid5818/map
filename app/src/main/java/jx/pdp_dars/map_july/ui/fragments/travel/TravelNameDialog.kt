package jx.pdp_dars.map_july.ui.fragments.travel

import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import jx.pdp_dars.map_july.databinding.DialogTravelBinding

class TravelNameDialog(context: Context) :
    AlertDialog(context) {
    val binding = DialogTravelBinding
        .inflate(LayoutInflater.from(context))
    private var onButtonClickListener: ((String) -> Unit)? = null

    fun setOnButtonClickListener(f: (name: String) -> Unit) {
        onButtonClickListener = f
    }

    init {
//        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        setCancelable(false)

        binding.okay.setOnClickListener {
            onButtonClickListener?.invoke(binding.travelName.text.toString())
        }
        setView(binding.root)
    }


}