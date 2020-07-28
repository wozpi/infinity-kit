package com.wozpi.infinity.kit.framework.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wozpi.infinity.kit.R
import com.wozpi.infinity.kit.framework.model.ErrorResponse
import com.wozpi.infinity.kit.presentation.dialog.CommonDialog
import com.wozpi.infinity.kit.presentation.dialog.LoseNetworkDialog

abstract class KitFragment<out T:KitViewModel> : Fragment(), View.OnClickListener{
    enum class StatusLoading{
        FIRST_LOAD,
        FINISH_LOAD,
        REFRESH_LOAD
    }

    abstract val mViewModel : T

    var mStatusLoading = StatusLoading.FIRST_LOAD

    private var mViewMask: View? = null
    var mRefreshLayout: SwipeRefreshLayout? = null

    internal lateinit var viewRoot: View

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.mCommonErrorLive.observe(viewLifecycleOwner, Observer {
            errorResponseMessage(it)
            commonError()
        })
        loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewRoot = inflater.inflate(setLayout(), container, false)
        setupViewMask(viewRoot)
        setupRefreshLayout(viewRoot)
        initView(viewRoot)
        return viewRoot
    }

    internal fun setupViewOnClickListener(vararg ids:Int){
        for (id in ids) {
            viewRoot.findViewById<View>(id).setOnClickListener(this)
        }
    }
    private fun setupRefreshLayout(viewRoot: View){

        mRefreshLayout = viewRoot.findViewById(R.id.refreshLayout)

        context?.let { context->
            mRefreshLayout?.setColorSchemeColors(
                ContextCompat.getColor(context,R.color.colorAccent),
                ContextCompat.getColor(context,R.color.colorPrimary),
                ContextCompat.getColor(context,R.color.colorRefresh))
        }

        mRefreshLayout?.setOnRefreshListener {
            onRefreshData()
        }
    }

    open fun onRefreshData(){
        mStatusLoading = StatusLoading.REFRESH_LOAD
    }

    private fun setupViewMask(viewRoot: View){
        mViewMask = viewRoot.findViewById(R.id.viewMask)
        mViewMask?.setOnClickListener {  }
    }

    fun showCommonMessage(message:String, event: CommonDialog.CommonDialogListener? = null){
        context?.apply {
            CommonDialog(this)
                .setContent(message)
                .addOnListener(event)
                .show()
        }
    }

    private fun showNoNetworkFoundDialog(){
//        context?.apply {
//            LoseNetworkDialog(this).show()
//        }
    }

    private fun errorResponseMessage(errorResponse: ErrorResponse){
        if(errorResponse.noNetwork){
            showNoNetworkFoundDialog()
        }else{
            showCommonMessage(errorResponse.message)
        }
    }

    open fun hideRefreshLoading(){
        mRefreshLayout?.isRefreshing = false
    }

    open fun viewMask(){
        mViewMask?.visibility = View.VISIBLE
    }

    open fun hideMask(){
        mViewMask?.visibility = View.GONE
    }

    fun toast(message: String){
        context?.apply {
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
        }

    }

    open fun commonError(){
        hideMask()
        hideRefreshLoading()
    }

    abstract fun setLayout():Int

    abstract fun initView(viewRoot: View)

    abstract fun loadData()

    override fun onClick(v: View) {

    }

    override fun onDestroy() {
        mViewModel.cancelAllRequest()
        super.onDestroy()
    }
}