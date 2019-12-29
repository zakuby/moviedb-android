package org.themoviedb.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import org.themoviedb.R
import org.themoviedb.databinding.DlgFavoriteAddBinding
import org.themoviedb.databinding.DlgFavoriteRemoveBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomDialog @Inject constructor(
    val resources: Resources
) {
    private var dlg: Dialog? = null

    private fun isShowing(): Boolean = dlg?.isShowing ?: false

    fun dismiss() {
        dlg?.dismiss()
        dlg = null
    }

    private fun showDialog(
        context: Context,
        view: View,
        dismissListener: DialogInterface.OnDismissListener
    ) {
        if (isShowing()) return
        dlg = Dialog(context, R.style.AlertDialogTheme)
        dlg?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setOnDismissListener(dismissListener)
            setContentView(view)
            show()
        }
    }

    fun showRemoveFromFavoriteDialog(
        context: Context,
        dismissListener: DialogInterface.OnDismissListener = DialogInterface.OnDismissListener { }
    ) {
        val binding: DlgFavoriteRemoveBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dlg_favorite_remove,
                null,
                false
            )
        showDialog(context, binding.root, dismissListener)
    }

    fun showAddToFavoriteDialog(
        context: Context,
        dismissListener: DialogInterface.OnDismissListener = DialogInterface.OnDismissListener { }
    ) {
        val binding: DlgFavoriteAddBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dlg_favorite_add,
                null,
                false
            )
        showDialog(context, binding.root, dismissListener)
    }
}