package com.vension.multistatelayout.demo

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.vension.multistatelayout.MultiStateLayout

/**
 * ========================================================
 * @author: Created by Vension on 2018/10/12 16:45.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */

open abstract class BaseFragment : Fragment(),MultiStateLayout.OnRetryClickListener {

    protected val mMultiLayoutBuilder by lazy {
        MultiStateLayout.Builder(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(attachLayoutRes(),null,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMultiLayoutBuilder.initPage(this@BaseFragment).setOnRetryListener(this).build()
        initViews(savedInstanceState)
        loadData()
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