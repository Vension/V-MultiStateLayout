package com.vension.multistatelayout.demo

import android.os.Bundle
import android.os.Handler

/**
 * ========================================================
 * @author: Created by Vension on 2018/10/17 14:23.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class TestFragment : BaseFragment() {

    override fun attachLayoutRes(): Int {
       return R.layout.fragment_test
    }

    override fun initViews(savedInstanceState: Bundle?) {

    }


    override fun loadData() {
        super.loadData()
        mMultiLayoutBuilder.build().showLoading()
        Handler().postDelayed({
            mMultiLayoutBuilder.build().showContent()
        }, 3000)
    }

}