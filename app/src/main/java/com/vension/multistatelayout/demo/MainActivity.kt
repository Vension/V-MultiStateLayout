package com.vension.multistatelayout.demo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import me.vension.commonlistadapter.CommonListAdapterForKotlin
import me.vension.commonlistadapter.CommonListViewHolderForKotlin
import me.vension.commonlistadapter.demo.TestBean

class MainActivity : BaseActivity() {

    override fun attachLayoutRes(): Int {
        return R.layout.activity_main
    }

    @SuppressLint("ResourceAsColor")
    override fun initViews(savedInstanceState: Bundle?) {
        mMultiLayoutBuilder.setCustomLayout(layoutInflater.inflate(R.layout.layout_custom,null)
            .apply {
                findViewById<ImageView>(R.id.iv_custom).setImageResource(R.drawable.img_custom)
                findViewById<TextView>(R.id.tv_custom_content).setOnClickListener {
                    startActivity(Intent(this@MainActivity,UseFragmentActivity::class.java))
                }
            })
            .build()
        val mList = ArrayList<TestBean>()
        for (i in 0..5) {
            val mBean = TestBean( R.drawable.img_custom,"我是标题$i", "我是简介$i")
            mList.add(mBean)
        }
        layout_refresh.setColorSchemeColors(R.color.colorPageRetry)
        layout_refresh.setOnRefreshListener {
             loadData()
        }
        val header = layoutInflater.inflate(R.layout.layout_header,null)
        lv_test.addHeaderView(header)
        lv_test.adapter = object : CommonListAdapterForKotlin<TestBean>(this,R.layout.item_list,mList){
            override fun convert(holder: CommonListViewHolderForKotlin, position: Int, item: TestBean) {
                holder.setImageResource(R.id.iv_image,item.imageRes)
                holder.setText(R.id.tv_title,item.title)
                holder.setText(R.id.tv_desc,item.desc)
            }
        }
        mMultiLayoutBuilder.build().showLoading()
        loadData()//获取数据
    }


    /**模拟网络获取数据*/
    override fun loadData() {
        Handler().postDelayed({
            mMultiLayoutBuilder.build().showContent()
            layout_refresh.isRefreshing = false
        }, 3000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menus, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_loading -> loadData()
            R.id.menu_empty -> mMultiLayoutBuilder.build().showEmpty()
            R.id.menu_loadfail -> mMultiLayoutBuilder.build().showError()
            R.id.menu_content -> mMultiLayoutBuilder.build().showContent()
            R.id.menu_not_net -> mMultiLayoutBuilder.build().showNotNetWork()
            R.id.menu_customer -> mMultiLayoutBuilder.build().showCustom()
            R.id.menu_custom_empty -> {
                mMultiLayoutBuilder
                    .setEmptyImageRes(R.drawable.img_change_empty)
                    .setEmptyText("我是自定义的空文本~")
                    .setEmptyTextColor(R.color.colorPrimaryDark)
                    .build()
                    .showEmpty()
            }
            R.id.menu_custom_error -> {
                mMultiLayoutBuilder
                    .setErrorImageRes(R.drawable.img_change_error)
                    .setErrorText("我是自定义的错误文本~")
                    .setErrorTextColor(R.color.colorAccent)
                    .build()
                    .showError()
            }
            else -> {
                mMultiLayoutBuilder.build().showError()
            }

        }
        return super.onOptionsItemSelected(item)
    }

}
