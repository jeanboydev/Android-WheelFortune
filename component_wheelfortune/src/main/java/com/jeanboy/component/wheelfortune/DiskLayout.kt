package com.jeanboy.component.wheelfortune

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Created by jeanboy on 2021/1/21 14:42.
 *
 * 转盘用户头像
 */
class DiskLayout : FrameLayout {

    private var diskTray: DiskTray? = null

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private val resetAngle: Float = -90f // 开始角度
    private val totalAngle: Float = 360f // 一圈角度
    private var sweepAngle: Float = 0f // 每个 item 角度
    private var rotateAngle: Float = 0f // 旋转角度

    private var isRunning: Boolean = false

    private val dataList = mutableListOf<ItemData>()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        diskTray = DiskTray(context)
        diskTray?.apply {
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

        val sweepAngleHalf = sweepAngle * 0.5f
        val radius = measuredWidth * 0.5f * 0.56f
        val centerX = measuredWidth * 0.5f
        val centerY = measuredHeight * 0.5f

        val startAngle = resetAngle - sweepAngle

        val childCount = childCount
        for (i in 0 until childCount) {
            val childAt = getChildAt(i)
            if (childAt !is DiskTray) {
                val offsetAngle = rotateAngle + sweepAngle * i + sweepAngleHalf
                val childAngle = startAngle + offsetAngle
                val point = getCirclePoint(centerX, centerY, radius, childAngle)

                val widthOffset = childAt.measuredWidth * 0.5f
                val heightOffset = childAt.measuredHeight * 0.5f

                childAt.layout(
                    (point.x - widthOffset).toInt(),
                    (point.y - heightOffset).toInt(),
                    (point.x + widthOffset).toInt(),
                    (point.y + heightOffset).toInt()
                )
                childAt.rotation = offsetAngle - sweepAngle
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
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.layout_disk_avatar, this, false)
        val iv_shape = itemView.findViewById<ImageView>(R.id.iv_shape)
        val tv_me = itemView.findViewById<TextView>(R.id.tv_me)

        if (data.self) {
            iv_shape.drawable.level = 3333
            iv_shape.visibility = View.VISIBLE
            tv_me.visibility = View.VISIBLE
        } else {
            iv_shape.visibility = View.GONE
            tv_me.visibility = View.GONE
        }

        val iv_avatar = itemView.findViewById<ImageView>(R.id.iv_avatar)
        Glide.with(context).load(data.avatar).placeholder(R.drawable.default_avatar)
            .error(R.drawable.default_avatar).into(iv_avatar)
        addView(itemView)
    }

    private fun toClearItemView() {
        this.dataList.clear()
        removeAllViews()
        toAddTray()
    }

    private fun toAddTray() {
        diskTray?.setData(dataList.size)
        addView(diskTray, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    fun setData(dataList: MutableList<ItemData>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        this.sweepAngle = totalAngle / this.dataList.size
        toUpdateView()
        requestLayout()
    }

    fun addData(itemData: ItemData) {
        this.dataList.add(itemData)
        this.sweepAngle = totalAngle / this.dataList.size
        toUpdateView()
        requestLayout()
    }

    fun removeData(itemData: ItemData) {
        if (this.dataList.isEmpty()) return
        this.dataList.remove(itemData)
        this.sweepAngle = totalAngle / this.dataList.size
        toUpdateView()
        requestLayout()
    }

    fun getDataSize(): Int {
        return dataList.size
    }

    fun remove(index: Int) {
        if (this.dataList.isEmpty()) return
        if (index >= 0 && index < dataList.size) {
            this.dataList.removeAt(index)
            if (this.dataList.size > 0) {
                this.sweepAngle = totalAngle / dataList.size
            } else {
                this.sweepAngle = totalAngle
            }
            toUpdateView()
            requestLayout()
        }
    }

    fun clearData() {
        this.dataList.clear()
        toClearItemView()
        requestLayout()
    }

    fun toRunning(removeIndex: Int) {
        if (dataList.isEmpty() || dataList.size == 1) return
        if (isRunning) return
        isRunning = true

        val circleAngle = getRandomAngleForRotate() // 随机旋转圈数
        val rangeAngle = totalAngle - sweepAngle * removeIndex  // 旋转到目标 item 需要的角度
        val offsetAngle =
            (getRandomAngleInItem(sweepAngle) + abs(rotateAngle)) % sweepAngle // item 中随机角度
        val targetAngle = circleAngle + rangeAngle - offsetAngle
        val startAngle = rotateAngle % totalAngle
        val valueAnimator = ValueAnimator.ofFloat(startAngle, targetAngle).apply {
            interpolator = DecelerateInterpolator()
            duration = 2500
        }
        valueAnimator.addUpdateListener {
            rotateAngle = it.animatedValue as Float
            diskTray?.setRotateAngle(rotateAngle)
            requestLayout()
        }

        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                isRunning = true
                listener?.onStart()
            }

            override fun onAnimationEnd(animation: Animator?) {
                remove(removeIndex)
                isRunning = false
                listener?.onEnd(dataList[removeIndex], dataList.size)
            }

            override fun onAnimationCancel(animation: Animator?) {
                isRunning = false
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
        valueAnimator.start()
    }

    /**
     * 获取命中 item 区间的旋转角度
     */
    private fun getRandomAngleInItem(sweepAngle: Float): Float {
        return Random.nextInt(1, sweepAngle.toInt()).toFloat()
    }

    /**
     * 获取旋转圈数的旋转角度
     */
    private fun getRandomAngleForRotate(): Float {
        return Random.nextInt(1, 3) * totalAngle
    }

    private var listener: OnRunningListener? = null

    fun setListener(listener: OnRunningListener) {
        this.listener = listener
    }

    interface OnRunningListener {
        fun onStart()
        fun onEnd(removeData: ItemData, count: Int)
    }
}