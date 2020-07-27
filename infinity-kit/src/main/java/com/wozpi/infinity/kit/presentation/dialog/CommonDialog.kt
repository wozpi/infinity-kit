package com.wozpi.infinity.kit.presentation.dialog

import android.content.Context
import android.view.View
import android.view.WindowManager
import com.wozpi.infinity.kit.R
import com.wozpi.infinity.kit.framework.base.KitDialog
import kotlinx.android.synthetic.main.dialog_common.*

class CommonDialog (context: Context) : KitDialog(context){
    private var mMessage:String? = null
    override fun initView() {
        textContent.text = mMessage
    }

    override fun resContentView(): Int = R.layout.dialog_common

    fun setContent(message:String):CommonDialog{
        mMessage = message
        return this
    }

    fun addOnListener(commonDialogListener: CommonDialogListener?):CommonDialog{
        return this
    }

    interface CommonDialogListener{
    }

    override fun show() {
        window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        super.show()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

    }
}