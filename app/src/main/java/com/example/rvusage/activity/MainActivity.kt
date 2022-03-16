package com.example.rvusage.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rvusage.R
import com.example.rvusage.adapter.MyItemTouchHelperCallBack
import com.example.rvusage.adapter.RvAdapter
import com.example.rvusage.adapter.StartDrawListener
import com.example.rvusage.bean.RvData

class MainActivity : AppCompatActivity(), StartDrawListener {
    private val dataList = ArrayList<RvData>()

    private lateinit var mRvShow : RecyclerView
    private lateinit var rvAdapter : RvAdapter
    private lateinit var myItemTouchHelperCallBack: MyItemTouchHelperCallBack
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initView()
    }

    private fun initData(){
        for(i in 0 until 20){
            dataList.add(RvData("Lance_He$i"))
        }
    }

    private fun initView(){
        mRvShow = findViewById(R.id.main_rv_show)
        rvAdapter = RvAdapter(dataList,this)
        myItemTouchHelperCallBack = MyItemTouchHelperCallBack(rvAdapter)

        mRvShow.adapter = rvAdapter
        mRvShow.layoutManager = LinearLayoutManager(this)

        itemTouchHelper = ItemTouchHelper(myItemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(mRvShow)

        mRvShow.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

    }

    override fun onStartDrag(innerHolder: RvAdapter.InnerHolder) {
        itemTouchHelper.startDrag(innerHolder)
    }

}