package com.github.libretube.ui.sheets

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager

/**
 * A bottom sheet that allows touches on its top/background
 */
open class UndimmedBottomSheet : ExpandedBottomSheet() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // BottomSheetDialogFragment passthrough user outside touch event
        dialog.setOnShowListener {
            dialog.findViewById<View>(com.google.android.material.R.id.touch_outside)?.apply {
                setOnTouchListener { v, event ->
                    event.setLocation(event.rawX - v.x, event.rawY - v.y)
                    activity?.dispatchTouchEvent(event)
                    v.performClick()
                    false
                }
            }
        }

        dialog.apply {
            setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (childFragmentManager.backStackEntryCount > 0) {
                        childFragmentManager.popBackStack()
                        return@setOnKeyListener true
                    }
                }
                return@setOnKeyListener false
            }

            window?.let {
                it.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
                it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }

            setCanceledOnTouchOutside(false)
        }

        return dialog
    }
}
