package com.vension.multistatelayout.demo

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import me.vension.multistatelayout.MultiStateLayout

/**
 * ========================================================
 * @author: Created by Vension on 2018/10/12 16:45.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */

open abstract class BaseActivity : AppCompatActivity(),MultiStateLayout.OnRetryClickListener {

    protected val mMultiLayoutBuilder by lazy {
        MultiStateLayout.Builder(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(attachLayoutRes())
        mMultiLayoutBuilder.initPage(this).setOnRetryListener(this).build()
        initViews(savedInstanceState)
    }

    @LayoutRes
    abstract fun attachLayoutRes():Int

    abstract fun initViews(savedInstanceState: Bundle?)

    open fun loadData() {
       //TODO 获取网络数据
    }

    override fun onRetry() {
        loadData()
    }

}