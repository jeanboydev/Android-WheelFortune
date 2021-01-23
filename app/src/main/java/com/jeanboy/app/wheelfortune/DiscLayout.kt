package com.jeanboy.app.wheelfortune

import android.content.Context
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 * Created by jeanboy on 2021/1/21 14:42.
 */
class DiscLayout : FrameLayout {

    private var discTray: DiscTray? = null

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private val resetAngle: Float = -90f // 开始角度
    private val totalAngle: Float = 360f // 一圈角度
    private var sweepAngle: Float = 0f // 每个 item 角度

    private val dataList = mutableListOf<ItemData>()

    fun setData(dataList: MutableList<ItemData>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        this.sweepAngle = totalAngle / dataList.size
        toUpdateView()
        requestLayout()
    }

    fun addData(itemData: ItemData) {
        this.dataList.add(itemData)
        this.sweepAngle = totalAngle / dataList.size
        toAddItemView(itemData)
        requestLayout()
    }

    fun clearData() {
        this.dataList.clear()
        toClearItemView()
        requestLayout()
    }

    fun toRunning(removeIndex: Int) {

    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
//        inflate(context, R.layout.layout_disc_container, this)
        discTray = DiscTray(context)
        discTray?.apply {
            alpha = 0.9f
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        toAddTray()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        val finalSpec = if (width < height) widthMeasureSpec else heightMeasureSpec
        super.onMeasure(finalSpec, finalSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = measuredWidth - paddingLeft - paddingRight
        mHeight = measuredHeight - paddingTop - paddingBottom

        val halfSweepAngle = sweepAngle * 0.5f
        val radius = mWidth * 0.5f * 0.6f
        val centerX = mWidth * 0.5f
        val centerY = mHeight * 0.5f

        val childCount = childCount
        for (i in 0 until childCount) {
            val childAt = getChildAt(i)
            if (childAt !is DiscTray) {
                val angle = resetAngle + sweepAngle * i + halfSweepAngle
                val point = getCirclePoint(centerX, centerY, radius, angle)

                childAt.left = point.x.toInt()
                childAt.top = point.y.toInt()
            }
        }
    }

    private fun getCirclePoint(
        centerX: Float,
        centerY: Float,
        radius: Float,
        angle: Float
    ): PointF {
        val x = (centerX + radius * cos(angle * Math.PI / 180)).toFloat()
        val y = (centerY + radius * sin(angle * Math.PI / 180)).toFloat()
        return PointF(x, y)
    }

    private fun toUpdateView() {
        removeAllViews()
        toAddTray()
        for (i in 0 until dataList.size) {
            toAddItemView(dataList[i])
        }
    }

    private fun toAddItemView(data: ItemData) {
        discTray?.addData(data)
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.layout_disk_avatar, null, false)
        addView(itemView)
    }

    private fun toClearItemView() {
        removeAllViews()
        discTray?.clearData()
        toAddTray()
    }

    private fun toAddTray() {
        addView(discTray, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }
}