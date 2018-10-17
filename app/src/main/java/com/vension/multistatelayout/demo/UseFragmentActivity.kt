package com.vension.multistatelayout.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * ========================================================
 * @author: Created by Vension on 2018/10/17 14:22.
 * @email:  2506856664@qq.com
 * @desc:   character determines attitude, attitude determines destiny
 * ========================================================
 */
class UseFragmentActivity : AppCompatActivity() {

    private var fragment : TestFragment?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_use_fragment)

        //添加fragment
        val transaction = supportFragmentManager.beginTransaction()
        fragment?.let {
            transaction.show(it)
        } ?: TestFragment().let {
            fragment = it
            transaction.add(R.id.layout_fragment, it, "test")
        }
        transaction.commitAllowingStateLoss()
    }

}