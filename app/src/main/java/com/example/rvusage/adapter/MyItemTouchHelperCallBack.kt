package com.example.rvusage.adapter

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class MyItemTouchHelperCallBack(private val itemTouchMoveListener: ItemTouchMoveListener) :ItemTouchHelper.Callback() {
    private val mDefaultScrollX = 200

    //当侧滑滑动的距离 / RecyclerView的宽大于该方法返回值，那么就会触发侧滑删除的操作
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 100f //设为最大值,目的是不触发系统原本的侧滑删除
    }

    //当侧滑的速度大于该方法的返回值，也会触发侧滑删除的操作
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 100f //设为最大值,目的是不触发系统原本的侧滑删除
    }

    //获取移动的方向 dragFlags是拖动的方向 swipeFlags是滑动的方向
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    //移动时的回调
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (viewHolder.itemViewType != viewHolder.itemViewType) {
            return false    //不同条目类型不能移动
        }
        return itemTouchMoveListener.onItemMove(
            viewHolder.adapterPosition,
            target.adapterPosition
        )
    }

    //侧滑时的回调
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        itemTouchMoveListener.onItemRemove(viewHolder.adapterPosition)
    }

    //item选择状态发生改变
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder?.itemView?.alpha = 0.5f
        }
    }

    //实时绘制child的方法
    //在ItemView进行滑动时会回调，这里的滑动包括：
    // 1.手指滑动；2.ItemView的位移动画。
    // 可以根据isCurrentlyActive参数来判断是手指滑动还是动画滑动。
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        var mCurrentScrollX = 0 //当前scrollX
        var mCurrentScrollXWhenInactive = 0 //
        var mInitXWhenInactive = 0f
        var mFirstInactive = false

        if(dX == 0f){
            mCurrentScrollX = viewHolder.itemView.scrollX
            mFirstInactive = true
        }
        if(isCurrentlyActive){
            viewHolder.itemView.scrollTo(mCurrentScrollX - dX.toInt(), 0)
        }
        else{
            if (mFirstInactive) {
                mFirstInactive = false
                mCurrentScrollXWhenInactive = viewHolder.itemView.scrollX
                mInitXWhenInactive = dX
            }
            if (viewHolder.itemView.scrollX >= mDefaultScrollX) {
                // 当手指松开时，ItemView的滑动距离大于给定阈值，那么最终就停留在阈值，显示删除按钮。
                viewHolder.itemView.scrollTo((mCurrentScrollX - dX.toInt()).coerceAtLeast(mDefaultScrollX), 0)
            } else {
                // 这里只能做距离的比例缩放，因为回到最初位置必须得从当前位置开始，dx不一定与ItemView的滑动距离相等
                viewHolder.itemView.scrollTo((mCurrentScrollXWhenInactive * dX / mInitXWhenInactive).toInt(), 0)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    //刷新view,防止在重用view的时候出现错位
    //在ItemView滑动完成之后会回调，所以想要实现侧滑ItemView停在某种状态，此方法是核心之点
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.alpha = 1.0f
        if (viewHolder.itemView.scrollX > mDefaultScrollX) {
            viewHolder.itemView.scrollTo(mDefaultScrollX, 0)
        } else if (viewHolder.itemView.scrollX < 0) {
            viewHolder.itemView.scrollTo(0, 0)
        }
        super.clearView(recyclerView, viewHolder)
    }

    //是否开启长按移动
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }
}