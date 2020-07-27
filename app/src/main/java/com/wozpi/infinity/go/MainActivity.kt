package com.wozpi.infinity.go

import androidx.lifecycle.Observer
import com.wozpi.infinity.kit.framework.base.KitActivity
import org.koin.android.ext.android.inject

class MainActivity : KitActivity<MainViewModel>() {
    override val mViewModel: MainViewModel
        by inject()

    override fun setLayout(): Int = R.layout.activity_main

    override fun initView() {
        mViewModel.mCommonErrorLive.observe(this, Observer {

        })
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
