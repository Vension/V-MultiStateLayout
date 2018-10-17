package me.vension.multistatelayout

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/10/12 14:19
 * 描  述：多状态Layout --使用Builder建造者模式链式调用
 * 致敬开源.参考自@Link{https://github.com/Hankkin/PageLayoutDemo}
 *
 * ========================================================
 */

class MultiStateLayout : FrameLayout {

    /**状态类型枚举*/
    enum class State{
        TYPE_LOADING,     //加载中...
        TYPE_EMPTY,       //空数据状态
        TYPE_CONTENT,     //内容（正常）
        TYPE_ERROR_LOAD,  //加载出错状态
        TYPE_ERROR_NET,   //网络连接问题
        TYPE_CUSTOM,      //自定义
    }

    private var mLoadingLayout: View? = null  //加载中布局
    private var mEmptyLayout: View? = null    //空布局
    private var mContentLayout: View? = null  //内容布局
    private var mErrorLayout: View? = null    //错误布局
    private var mNoNetLayout: View? = null    //无网络布局
    private var mCustomLayout: View? = null    //自定义布局

    private var mCurrentState = State.TYPE_LOADING //当前状态
    /*构造函数*/
    constructor(mContext: Context):super(mContext)
    constructor(mContext: Context, attrs: AttributeSet?):super(mContext,attrs)
    constructor(mContext: Context, attrs: AttributeSet?, defStyleAttr: Int):super(mContext,attrs,defStyleAttr)


    /**显示布局*/
    private fun showLayout(mStateType: State) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            switchLayout(mStateType)
        } else {
            post { switchLayout(mStateType) }
        }
    }

    /**
     * 切换布局
     * @param mStateType  当前状态类型
     */
    private fun switchLayout(mStateType: State) {
        mCurrentState = mStateType
        mLoadingLayout?.visibility = if (mStateType == State.TYPE_LOADING) View.VISIBLE else View.GONE
        mEmptyLayout?.visibility = if (mStateType == State.TYPE_EMPTY) View.VISIBLE else View.GONE
        mContentLayout?.visibility = if (mStateType == State.TYPE_CONTENT) View.VISIBLE else View.GONE
        mErrorLayout?.visibility = if (mStateType == State.TYPE_ERROR_LOAD) View.VISIBLE else View.GONE
        mNoNetLayout?.visibility = if (mStateType == State.TYPE_ERROR_NET) View.VISIBLE else View.GONE
        mCustomLayout?.visibility = if (mStateType == State.TYPE_CUSTOM) View.VISIBLE else View.GONE
    }


    /**
     * 获取当前状态
     */
    fun getViewStatus(): State {
        return mCurrentState
    }

    /**显示loading布局*/
    fun showLoading() {
        showLayout(State.TYPE_LOADING)
        mLoadingLayout?.apply {
            //doSomeThing 例如开启加载动画处理
        }
    }

    /**显示加载出错布局*/
    fun showError() {
        showLayout(State.TYPE_ERROR_LOAD)
    }

    /**显示空布局*/
    fun showEmpty() {
        showLayout(State.TYPE_EMPTY)
    }

    /**显示内容布局*/
    fun showContent() {
        showLayout(State.TYPE_CONTENT)
        mLoadingLayout?.apply {
            //doSomeThing 例如停止加载动画处理
        }
    }

    /**显示网络不可用布局*/
    fun showNotNetWork(){
        showLayout(State.TYPE_ERROR_NET)
    }


    /**显示自定义布局*/
    fun showCustom(){
        showLayout(State.TYPE_CUSTOM)
    }

    /**
     * 获取对应状态下的Layout
     * @param mStateType
     * @return View
     */
    fun getLayout(mStateType: State) : View?{
        if (mStateType == State.TYPE_LOADING){
            return mLoadingLayout
        }
        if (mStateType == State.TYPE_EMPTY){
            return mEmptyLayout
        }
        if (mStateType == State.TYPE_ERROR_NET){
            return mNoNetLayout
        }
        if (mStateType == State.TYPE_ERROR_LOAD){
            return mErrorLayout
        }
        if (mStateType == State.TYPE_CONTENT){
            return mContentLayout
        }
        if (mStateType == State.TYPE_CUSTOM){
            return mCustomLayout
        }
        return null
    }

    /**重试点击监听器*/
    interface OnRetryClickListener {
        fun onRetry()
    }



    /** ========================  Builder建造者模式链式调用 ============================ */
    class Builder {
        private var mMultiStateLayout: MultiStateLayout
        private var mInflater: LayoutInflater
        private var mContext: Context
        private lateinit var tvPageLoading: AppCompatTextView

        private lateinit var ivPageEmpty: AppCompatImageView
        private lateinit var tvPageEmpty: AppCompatTextView

        private lateinit var ivPageError: AppCompatImageView
        private lateinit var tvPageError: AppCompatTextView
        private lateinit var tvPageErrorRetry: AppCompatTextView

        private lateinit var ivPageNotNetWork: AppCompatImageView
        private lateinit var tvPageNotNetWork: AppCompatTextView
        private lateinit var tvPageNotNetWorkRetry: AppCompatTextView

        private var mOnRetryClickListener: OnRetryClickListener? = null

        constructor(context: Context) {
            this.mContext = context
            this.mMultiStateLayout = MultiStateLayout(context)
            mInflater = LayoutInflater.from(context)
        }

        /**
         * set target view for root
         */
        fun initPage(targetView: Any): Builder {
            var content: ViewGroup? = null
            when (targetView) {
                //如果是Activity，获取到android.R.content
                is Activity -> {
                    Log.e("vension--","is Activity")
                    mContext = targetView
                    content = (mContext as Activity).findViewById(android.R.id.content)
                }
                //如果是Fragment获取到parent
                is Fragment -> {
                    Log.e("vension--","is Fragment")
                    mContext = targetView.activity!!
                    content = (targetView.view)?.parent as ViewGroup
                }
                //如果是View，也取到parent
                is View -> {
                    Log.e("vension--","is View")
                    mContext = targetView.context
                    try {
                        content = (targetView.parent) as ViewGroup
                    } catch (e: TypeCastException) {
                        e.printStackTrace()
                    }
                }
            }

            val childCount = content?.childCount
            var index = 0
            val oldContent: View
            if (targetView is View) {
                //如果是某个线性布局或者相对布局时，遍历它的孩子，找到对应的索引，记录下来
                oldContent = targetView
                childCount?.let {
                    for (i in 0 until childCount) {
                        if (content?.getChildAt(i) === oldContent) {
                            index = i
                            break
                        }
                    }
                }

            } else {
                //如果是Activity或者Fragment时，取到索引为第一个的View
                oldContent = content!!.getChildAt(0)
            }
            //给MultiStateLayout设置contentView
            mMultiStateLayout.mContentLayout = oldContent
            //移除所有子view
            mMultiStateLayout.removeAllViews()
            //将本身content移除，并且把MultiStateLayout添加到DecorView中去
            content?.removeView(oldContent)
            val lp = oldContent.layoutParams
            content?.addView(mMultiStateLayout, index, lp)
            mMultiStateLayout.addView(oldContent)
            initDefault()   //设置默认状态布局
            return this
        }


        private fun initDefault() {
            if (mMultiStateLayout.mLoadingLayout == null) {
                setDefaultLoading()
            }
            if (mMultiStateLayout.mEmptyLayout == null) {
                setDefaultEmpty()
            }
            if (mMultiStateLayout.mErrorLayout == null) {
                setDefaultError()
            }
            if (mMultiStateLayout.mNoNetLayout == null) {
                setDefaultNoNet()
            }
        }

        //设置默认加载中布局
        private fun setDefaultLoading() {
            mMultiStateLayout.mLoadingLayout = mInflater.inflate(R.layout.layout_default_loading, mMultiStateLayout, false)
                .apply {
                    tvPageLoading = findViewById(R.id.tv_page_loading)
                }
            mMultiStateLayout.mLoadingLayout?.visibility = View.GONE
            mMultiStateLayout.addView(mMultiStateLayout.mLoadingLayout)
        }

        //设置默认空布局
        private fun setDefaultEmpty() {
            mMultiStateLayout.mEmptyLayout = mInflater.inflate(R.layout.layout_default_empty, mMultiStateLayout, false)
                .apply {
                    tvPageEmpty = findViewById(R.id.tv_page_empty)
                    ivPageEmpty = findViewById(R.id.iv_page_empty)
                }
            mMultiStateLayout.mEmptyLayout?.visibility = View.GONE
            mMultiStateLayout.addView(mMultiStateLayout.mEmptyLayout)
        }

        //设置默认加载出错布局
        private fun setDefaultError() {
            mMultiStateLayout.mErrorLayout = mInflater.inflate(R.layout.layout_default_loadfail, mMultiStateLayout, false)
                .apply {
                    ivPageError = findViewById(R.id.iv_page_error)
                    tvPageError = findViewById(R.id.tv_page_error)
                    tvPageErrorRetry = findViewById(R.id.tv_page_error_retry)
                    tvPageErrorRetry?.setOnClickListener { mOnRetryClickListener?.onRetry() }
                }
            mMultiStateLayout.mErrorLayout?.visibility = View.GONE
            mMultiStateLayout.addView(mMultiStateLayout.mErrorLayout)
        }

        //设置默认网络错误布局
        private fun setDefaultNoNet() {
            mMultiStateLayout.mNoNetLayout = mInflater.inflate(R.layout.layout_default_not_net, mMultiStateLayout, false)
                .apply {
                    ivPageNotNetWork = findViewById(R.id.iv_page_not_network)
                    tvPageNotNetWork = findViewById(R.id.tv_page_not_network)
                    tvPageNotNetWorkRetry = findViewById(R.id.tv_page_not_network_retry)
                    tvPageNotNetWorkRetry?.setOnClickListener { mOnRetryClickListener?.onRetry() }
                }
            mMultiStateLayout.mNoNetLayout?.visibility = View.GONE
            mMultiStateLayout.addView(mMultiStateLayout.mNoNetLayout)
        }



        /**
         * 设置loading布局
         * @param layoutId loading布局的id
         * @param tvLoadingId loading布局的中加载文本的id
         */
        fun setLoading(layoutId: Int,tvLoadingId: Int): Builder {
            mInflater.inflate(layoutId, mMultiStateLayout, false).apply {
                if (tvLoadingId > 0){
                    tvPageLoading = findViewById(tvLoadingId)
                }
                mMultiStateLayout.mLoadingLayout = this
                mMultiStateLayout.addView(this)
            }
            return this
        }

        /**
         * 设置loading布局
         * @param view loading布局的中加载文本的id
         */
        fun setLoading(view: View): Builder {
            mMultiStateLayout.apply {
                mLoadingLayout = view
                addView(view)
            }
            return this
        }

        /**
         * 自定义空布局
         */
        fun setEmpty(layoutId: Int,ivEmptyId: Int,tvEmptyId: Int): Builder {
            mInflater.inflate(layoutId, mMultiStateLayout, false).apply {
                if (ivEmptyId > 0){
                    ivPageEmpty = findViewById(ivEmptyId)
                }
                if (tvEmptyId > 0){
                    tvPageEmpty = findViewById(tvEmptyId)
                }
                mMultiStateLayout.mEmptyLayout = this
                mMultiStateLayout.addView(this)
            }
            return this
        }

        fun setEmpty(view: View): Builder {
            mMultiStateLayout.apply {
                mEmptyLayout = view
                addView(view)
            }
            return this
        }

        /**
         * 设置错误布局
         * @param layoutId 错误布局的id
         * @param ivErrorId 错误布局中图片的id
         * @param tvErrorId 错误布局中加载文本的id
         * @param viewRetryId 错误布局中重试按钮的id
         */
        fun setError(layoutId: Int,ivErrorId : Int,tvErrorId : Int, viewRetryId: Int, onRetryClickListener: OnRetryClickListener): Builder {
            mInflater.inflate(layoutId, mMultiStateLayout, false).apply {
                mMultiStateLayout.mErrorLayout = this
                mMultiStateLayout.addView(this)
                if (ivErrorId > 0){
                    ivPageError = findViewById(tvErrorId)
                }
                if (tvErrorId > 0){
                    tvPageError = findViewById(tvErrorId)
                }
                if (viewRetryId > 0){
                    tvPageErrorRetry = findViewById(viewRetryId)
                }
                tvPageErrorRetry?.setOnClickListener { onRetryClickListener.onRetry() }
            }
            return this
        }

        fun setError(view: View): Builder {
            mMultiStateLayout.apply {
                mErrorLayout = view
                addView(view)
            }
            return this
        }


        /**
         * 设置无网络布局
         * @param layoutId 无网络布局的id
         * @param ivErrorId 无网络布局中图片的id
         * @param tvErrorId 无网络布局中加载文本的id
         * @param viewRetryId 无网络布局中重试按钮的id
         */
        fun setNotNetWork(layoutId: Int,ivErrorId : Int,tvErrorId : Int, viewRetryId: Int, onRetryClickListener: OnRetryClickListener): Builder {
            mInflater.inflate(layoutId, mMultiStateLayout, false).apply {
                mMultiStateLayout.mNoNetLayout = this
                mMultiStateLayout.addView(this)
                if (ivErrorId > 0){
                    ivPageNotNetWork = findViewById(tvErrorId)
                }
                if (tvErrorId > 0){
                    tvPageNotNetWork = findViewById(tvErrorId)
                }
                if (viewRetryId > 0){
                    tvPageNotNetWorkRetry = findViewById(viewRetryId)
                }
                tvPageNotNetWorkRetry?.setOnClickListener { onRetryClickListener.onRetry() }
            }
            return this
        }

        fun setNotNetWork(view: View): Builder {
            mMultiStateLayout.apply {
                mNoNetLayout = view
                addView(view)
            }
            return this
        }

        /**
         * 自定义布局
         */
        fun setCustomLayout(layoutId: Int): Builder{
            mInflater.inflate(layoutId, mMultiStateLayout, false).apply {
                mMultiStateLayout.mCustomLayout = this
                mMultiStateLayout.addView(this)
            }
            return this
        }
        fun setCustomLayout(layout: View): Builder{
            mMultiStateLayout.apply {
                mCustomLayout = layout
                addView(layout)
            }
            return this
        }

        /**
         * 设置加载文案
         */
        fun setLoadingText(text: String): Builder {
            tvPageLoading?.text = text
            return this
        }


        /**
         * 设置加载文字颜色
         */
        fun setLoadingTextColor(color: Int): Builder {
            tvPageLoading?.setTextColor(mContext.resources.getColor(color))
            return this
        }


        /**
         * 设置空布局文案
         */
        fun setEmptyText(text: String): Builder {
            tvPageEmpty?.text = text
            return this
        }

        /**
         * 设置空布局文案颜色
         */
        fun setEmptyTextColor(color: Int): Builder {
            tvPageEmpty?.setTextColor(mContext.resources.getColor(color))
            return this
        }

        /**
         * 设置错误布局文案
         */
        fun setErrorText(text: String): Builder {
            tvPageError?.text = text
            return this
        }
        /**
         * 设置空布局文案颜色
         */
        fun setErrorTextColor(color: Int): Builder {
            tvPageError?.setTextColor(mContext.resources.getColor(color))
            return this
        }
        /**
         * 设置无网络布局文案
         */
        fun setNotNetWorkText(text: String): Builder {
            tvPageNotNetWork?.text = text
            return this
        }
        /**
         * 设置无网络布局文案颜色
         */
        fun setNotNetWorkTextColor(color: Int): Builder {
            tvPageNotNetWork?.setTextColor(mContext.resources.getColor(color))
            return this
        }


        /**
         * 设置错误布局重试文案
         */
        fun setErrorRetryText(text: String): Builder {
            tvPageErrorRetry.text = text
            tvPageNotNetWorkRetry.text = text
            return this
        }

        /**
         * 设置默认错误布局重试文案颜色
         */
        fun setErrorRetryTextColor(color: Int): Builder {
            tvPageErrorRetry.setTextColor(mContext.resources.getColor(color))
            tvPageNotNetWorkRetry.setTextColor(mContext.resources.getColor(color))
            return this
        }

        /**
         * 设置空布局提醒图片
         */
        fun setEmptyImageRes(resId: Int): Builder{
            ivPageEmpty.setImageResource(resId)
            return this
        }

        /**
         * 设置错误布局提醒图片
         */
        fun setErrorImageRes(resId: Int): Builder{
            ivPageError.setImageResource(resId)
            return this
        }
        /**
         * 设置无网络布局图片
         */
        fun setNotNetWorkImageRes(resId: Int): Builder{
            ivPageNotNetWork.setImageResource(resId)
            return this
        }


        fun setOnRetryListener(onRetryClickListener: OnRetryClickListener): Builder {
            this.mOnRetryClickListener = onRetryClickListener
            return this
        }


        fun build() = mMultiStateLayout
    }





}