package com.wozpi.infinity.kit.framework.base

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.wozpi.infinity.kit.R
import com.wozpi.infinity.kit.framework.model.ErrorResponse
import com.wozpi.infinity.kit.presentation.dialog.CommonDialog

abstract class KitActivity<out T:KitViewModel> : AppCompatActivity() {
    enum class StatusLoading{
        FIRST_LOAD,
        FINISH_LOAD,
        REFRESH_LOAD
    }

    var mStatusLoading = StatusLoading.FIRST_LOAD

    abstract val mViewModel : T

    private var mViewMask: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayout())

        setupToolBar()
        setupViewMask()
        mViewModel.mCommonErrorLive.observe(this, Observer {
            errorResponseMessage(it)
            commonError()
        })
        initView()
        loadData()
    }

    private fun setupToolBar(){
//        findViewById<DramaToolbarBack>(R.id.toolbarBack)?.addEventNavigation(object: DramaToolbarBack.EventNavigation{
//            override fun onClickBack() {
//                finish()
//            }
//            override fun onClickOption() {
//                addEventClickOption()
//            }
//
//        })
    }

    private fun setupViewMask(){
        mViewMask = findViewById(R.id.viewMask)
        mViewMask?.setOnClickListener {  }
    }

    fun showCommonMessage(message:String, event: CommonDialog.CommonDialogListener? = null){
        CommonDialog(this)
            .setContent(message)
            .addOnListener(event)
            .show()
    }

    private fun showNoNetworkFoundDialog(){
//        LoseNetworkDialog(this).show()
    }

    private fun errorResponseMessage(errorResponse: ErrorResponse){
        if(errorResponse.noNetwork){
            showNoNetworkFoundDialog()
        }else{
            showCommonMessage(errorResponse.message)
        }
    }


    open fun viewMask(){
        mViewMask?.visibility = View.VISIBLE
    }

    open fun hideMask(){
        mViewMask?.visibility = View.GONE
    }

    fun toast(message: String){
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }


    fun hideSoftKeyboard() {
        currentFocus?.let { viewFocus->
            (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(viewFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    open fun commonError(){
        hideMask()
    }

    abstract fun setLayout():Int

    abstract fun initView()

    abstract fun loadData()



    override fun onDestroy() {
        mViewModel.cancelAllRequest()
        super.onDestroy()
    }

    protected fun isHideSoftKeyBoardTouchOutSide(): Boolean {
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (isHideSoftKeyBoardTouchOutSide()) {
            val viewFocus = window.currentFocus
            (viewFocus as? EditText)?.let { focus->

                val screenLocation = IntArray(2)
                viewFocus.getLocationOnScreen(screenLocation)
                val x = event.rawX + focus.left - screenLocation[0]
                val y = event.rawY + focus.top - screenLocation[1]

                if (event.action == MotionEvent.ACTION_DOWN) {
                    if (x < focus.left || x >= focus.right
                        || y < focus.top || y >= focus.bottom
                    ) {
                        hideSoftKeyboard()
                    }
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }
}