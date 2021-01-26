package com.jeanboy.component.wheelfortune

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import java.lang.ref.WeakReference

/**
 * Created by jeanboy on 2021/1/21 14:42.
 *
 * 转盘入口
 */
class WheelFortuneView : FrameLayout {

    private var view_light1: View? = null
    private var view_light2: View? = null
    private var view_action_join: View? = null
    private var view_action_running: View? = null
    private var diskLayout: DiskLayout? = null

    private val myHandler = MyHandler(this)
    private var index = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        inflate(context, R.layout.layout_wheel_fortune, this)

        view_light1 = findViewById(R.id.view_light1)
        view_light2 = findViewById(R.id.view_light2)
        view_action_join = findViewById(R.id.view_action_join)
        view_action_running = findViewById(R.id.view_action_running)

        view_action_join?.setOnClickListener {
            onJoinClick()
        }

        diskLayout = findViewById(R.id.discLayout)
        diskLayout?.setListener(object : DiskLayout.OnRunningListener {
            override fun onStart() {
                view_action_join?.visibility = View.GONE
                view_action_running?.visibility = View.VISIBLE
            }

            override fun onEnd(removeData: ItemData, count: Int) {
                view_action_join?.visibility = View.VISIBLE
                view_action_running?.visibility = View.GONE
                listener?.onCountChange(count)
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        val finalSpec = if (width < height) widthMeasureSpec else heightMeasureSpec
        super.onMeasure(finalSpec, finalSpec)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        toStartMarquee()
    }

    override fun onDetachedFromWindow() {
        myHandler.removeCallbacksAndMessages(null)
        super.onDetachedFromWindow()
    }

    private fun toStartMarquee() {
        val message = Message.obtain(myHandler, MSG_LIGHT)
        val bundle = Bundle()
        bundle.putInt(KEY_INDEX, index++);
        message.data = bundle
        myHandler.sendMessageDelayed(message, 600)
    }

    private fun toSwitchLight(count: Int) {
        if (count % 2 == 0) {
            view_light1?.visibility = View.VISIBLE
            view_light2?.visibility = View.GONE
        } else {
            view_light1?.visibility = View.GONE
            view_light2?.visibility = View.VISIBLE
        }
        toStartMarquee()
    }


    private fun onJoinClick() {
        this.listener?.onJoinClick()
    }

    private var listener: WheelStateListener? = null

    fun setListener(listener: WheelStateListener) {
        this.listener = listener
    }

    interface WheelStateListener {
        fun onJoinClick()
        fun onCountChange(count: Int)
    }

    fun toRunning(removeIndex: Int) {
        diskLayout?.toRunning(removeIndex)
    }

    fun setData(dataList: MutableList<ItemData>) {
        diskLayout?.setData(dataList)
        listener?.onCountChange(diskLayout?.getDataSize() ?: 0)
    }

    fun addData(itemData: ItemData) {
        diskLayout?.addData(itemData)
        listener?.onCountChange(diskLayout?.getDataSize() ?: 0)
    }

    fun clearData() {
        diskLayout?.clearData()
        listener?.onCountChange(diskLayout?.getDataSize() ?: 0)
    }

    private class MyHandler(context: WheelFortuneView) : Handler(Looper.myLooper()!!) {
        private var reference: WeakReference<WheelFortuneView>? = WeakReference(context)
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (reference?.get() == null) return
            when (msg.what) {
                MSG_LIGHT -> {
                    reference?.get()?.toSwitchLight(msg.data.getInt(KEY_INDEX))
                }
            }
        }
    }

    companion object {
        const val MSG_LIGHT = 0x101
        const val KEY_INDEX = "index"
    }
} 