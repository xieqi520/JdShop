package com.example.duxiaoming.jdshop.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.adapter.CartAdapter
import com.example.duxiaoming.jdshop.adapter.decoration.DividerItemDecoration
import com.example.duxiaoming.jdshop.bean.ShoppingCart
import com.example.duxiaoming.jdshop.utils.CartProvider
import com.example.duxiaoming.jdshop.widget.JDToolBar


open class CartFragment : Fragment(), View.OnClickListener {


    private var mRecyclerView: RecyclerView? = null

    private var cartProvider: CartProvider? = null
    private var mAdapter: CartAdapter? = null
    protected var mToolbar: JDToolBar? = null
    private var mCheckBox: CheckBox? = null
    private var mTextTotal: TextView? = null
    private var mBtnDel: Button? = null

    private var carts: MutableList<ShoppingCart>? = mutableListOf()

    companion object {
        val ACTION_EDIT = 1

        val ACTION_CAMPLATE = 2
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_cart, container, false)
        initView(view)
        changeToolbar()
        showData()
        return view
    }

    private fun initView(view: View) {
        mToolbar = view.findViewById(R.id.toolbar) as JDToolBar
        mRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        mCheckBox = view.findViewById(R.id.checkbox_all) as CheckBox
        mTextTotal = view.findViewById(R.id.txt_total) as TextView
        mBtnDel = view.findViewById(R.id.btn_del) as Button

        mBtnDel?.setOnClickListener {
            mAdapter?.delCart()
        }

        cartProvider = CartProvider(context)
    }

    private fun showData() {


        carts = cartProvider!!.getAll()

        mAdapter = CartAdapter(context, carts, mCheckBox!!, mTextTotal!!)

        mRecyclerView!!.adapter = mAdapter
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST))


    }

    fun refData() {
        mAdapter!!.clear()
        carts = cartProvider?.getAll()
        mAdapter!!.addData(carts)
        mAdapter!!.showTotalPrice()


    }

    fun changeToolbar() {

        mToolbar!!.hideSearchView()
        mToolbar!!.showTitleView()
        mToolbar!!.setTitle(R.string.cart)
        mToolbar!!.getRightButton()!!.visibility = View.VISIBLE
        mToolbar!!.setRightButtonText("编辑")

        mToolbar!!.getRightButton()!!.setOnClickListener(this)

        mToolbar!!.getRightButton()!!.tag = ACTION_EDIT


    }

    private fun showDelControl() {
        mToolbar?.getRightButton()?.text = "完成"
        mTextTotal?.visibility = View.GONE
        mBtnDel?.visibility = View.VISIBLE
        mToolbar?.getRightButton()?.tag = ACTION_CAMPLATE

        mAdapter?.checkAll_None(false)
        mCheckBox?.isChecked = false

    }

    private fun hideDelControl() {

        mTextTotal?.visibility = View.VISIBLE
        mBtnDel?.visibility = View.GONE
        mToolbar?.setRightButtonText("编辑")
        mToolbar?.getRightButton()?.tag = ACTION_EDIT

        mAdapter?.checkAll_None(true)
        mAdapter?.showTotalPrice()

        mCheckBox?.isChecked = true
    }


    override fun onClick(v: View?) {
        val action = v?.tag as Int
        if (action === ACTION_EDIT) {

            showDelControl()
        } else if (action === ACTION_CAMPLATE) {

            hideDelControl()
        }
    }

}
