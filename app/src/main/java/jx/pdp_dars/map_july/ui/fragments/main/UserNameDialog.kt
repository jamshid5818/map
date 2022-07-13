package jx.pdp_dars.map_july.ui.fragments.main

import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import jx.pdp_dars.map_july.databinding.DialogUsernameBinding

class UserNameDialog(context: Context) :
    AlertDialog(context) {
    val binding = DialogUsernameBinding
        .inflate(LayoutInflater.from(context))
    private var onButtonClickListener: ((String,String) -> Unit)? = null

    fun setOnButtonClickListener(f: (username: String,password:String) -> Unit) {
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
            onButtonClickListener?.invoke(binding.userName.text.toString(),binding.password.text.toString())
        }
        setView(binding.root)
    }


}