package ru.netology.nmedia.activity

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.netology.nmedia.auth.AppAuth

class DialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Accept exit")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes") {dialog,_ ->  AppAuth.getInstance().removeAuth()
                    dialog.cancel()
                }
                .setNegativeButton("No") { dialog,_ -> dialog.cancel() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}