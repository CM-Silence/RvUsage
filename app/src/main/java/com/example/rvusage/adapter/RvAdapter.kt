package com.example.rvusage.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rvusage.R
import com.example.rvusage.bean.RvData
import java.util.*
import kotlin.collections.ArrayList

class RvAdapter(private val dataList : ArrayList<RvData>,private val startDragListener: StartDrawListener) : RecyclerView.Adapter<RvAdapter.InnerHolder>(), ItemTouchMoveListener{

    companion object{
        public var buttonWidth : Int = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdapter.InnerHolder {
        return InnerHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_main, parent, false))
    }

    override fun onBindViewHolder(holder: RvAdapter.InnerHolder, position: Int) {
        holder.mTvData.text = dataList[position].data
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(dataList,fromPosition,toPosition)//交换数据
        notifyItemMoved(fromPosition, toPosition) //item移动
        return true
    }

    override fun onItemRemove(position: Int): Boolean {
        dataList.removeAt(position) //移除数据
        notifyItemRemoved(position) //移除item
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mTvData: TextView = itemView.findViewById(R.id.rv_tv_data)
        private val mBtnDelete: Button = itemView.findViewById(R.id.rv_btn_delete)
        private val mLinearLayout: LinearLayout = itemView.findViewById(R.id.main_rv)

        init{
            buttonWidth = mBtnDelete.scrollX
            mLinearLayout.setOnTouchListener(View.OnTouchListener { view: View, motionEvent: MotionEvent ->
                if(motionEvent.action == MotionEvent.ACTION_POINTER_DOWN){
                    startDragListener.onStartDrag(this);
                }
                return@OnTouchListener false
            }

            )
            mBtnDelete.setOnClickListener{
                onItemRemove(this.adapterPosition)
                mBtnDelete.isEnabled = false
            }
        }
    }


}