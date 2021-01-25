package com.jeanboy.component.wheelfortune

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Created by jeanboy on 2021/1/21 14:42.
 *
 * 转盘彩色托盘
 */
class DiskTray : View {

    private val colorDefault = Color.parseColor("#652CC9")
    private val colorPalette: Array<Int> = arrayOf(
        Color.parseColor("#652CC9"),
        Color.parseColor("#BD3C8C"),
        Color.parseColor("#1E4AC1"),
        Color.parseColor("#232AAF"),
        Color.parseColor("#386730"),
        Color.parseColor("#712636"),
        Color.parseColor("#BE5F14"),
        Color.parseColor("#957C15"),
        Color.parseColor("#1D7973"),
    )

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var mLeft: Float = 0f
    private var mTop: Float = 0f
    private var panelSize: Float = 0f

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val resetAngle: Float = -90f // 开始角度
    private val totalAngle: Float = 360f // 一圈角度
    private var sweepAngle: Float = 0f // 每个 item 角度
    private var rotateAngle: Float = 0f // 旋转角度

    private var itemCount = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

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

        mLeft = paddingLeft.toFloat()
        mTop = paddingTop.toFloat()
        panelSize = mWidth.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawPanel(canvas)
    }

    private fun drawPanel(canvas: Canvas?) {
        val rectF = RectF(mLeft, mTop, mLeft + panelSize, mTop + panelSize)
        if (itemCount == 0) {
            paint.color = colorDefault
            canvas?.drawArc(rectF, resetAngle, totalAngle, true, paint)
        } else {
            var startAngle = resetAngle + rotateAngle
            for (i in 0 until itemCount) {
                val colorIndex = if (i >= colorPalette.size) 0 else i
                paint.color = colorPalette[colorIndex]
                canvas?.drawArc(rectF, startAngle, sweepAngle, true, paint)
                startAngle += sweepAngle
            }
        }
    }

    fun setData(itemCount: Int) {
        this.itemCount = itemCount
        this.sweepAngle = if (itemCount == 0) totalAngle else totalAngle / itemCount
        postInvalidate()
    }

    fun setRotateAngle(rotateAngle: Float) {
        this.rotateAngle = rotateAngle
        postInvalidate()
    }
}