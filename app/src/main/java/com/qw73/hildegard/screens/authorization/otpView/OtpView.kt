package com.qw73.hildegard.screens.authorization.otpView

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.TextUtils
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.EditorInfo
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import com.qw73.hildegard.R
import com.qw73.hildegard.screens.authorization.otpView.DefaultMovementMethod.Companion.instance
import kotlin.math.abs
import kotlin.math.roundToInt

class OtpView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.otpViewStyle
) : AppCompatEditText(context, attrs, defStyleAttr), OnTouchListener {
    private val viewType: Int
    private var otpViewItemCount: Int
    private var otpViewItemWidth: Int
    private var otpViewItemHeight: Int
    private var otpViewItemRadius: Int
    private var otpViewItemSpacing: Int
    private val paint: Paint

    /**
     * Gets the line colors for the different states (normal, selected, focused) of the OtpView.
     *
     * @attr ref R.styleable#OtpView_lineColor
     * @see .setLineColor
     * @see .setLineColor
     */
    //    private final TextPaint animatorTextPaint = new TextPaint();
    private var lineColors: ColorStateList?

    /**
     *
     * Return the current color selected for normal line.
     *
     * @return Returns the current item's line color.
     */
    @get:ColorInt
    var currentLineColor = com.google.android.material.R.attr.colorOnSurface
        private set
    private var lineWidth: Int
    private val textRect = Rect()
    private val itemBorderRect = RectF()
    private val itemLineRect = RectF()
    private val path = Path()
    private val itemCenterPoint = PointF()
    private var blink: Blink? = null
    private var isCursorVisible: Boolean
    private var drawCursor = false
    private var cursorHeight = 0f
    private var cursorWidth: Int
    private var cursorColor: Int
    private var itemBackground: Drawable?
    private var hideLineWhenFilled: Boolean
    private val rtlTextDirection: Boolean
    private var maskingChar: String?
    private var onOtpCompletionListener: OnOtpCompletionListener? = null

    /*@Override
  public void setTypeface(Typeface tf, int style) {
    super.setTypeface(tf, style);
  }

  @Override
  public void setTypeface(Typeface tf) {
    super.setTypeface(tf);
    if (animatorTextPaint != null) {
      animatorTextPaint.set(getPaint());
    }
  }*/
    private fun setMaxLength(maxLength: Int) {
        filters =
            if (maxLength >= 0) arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength)) else NO_FILTERS
    }

    private fun checkItemRadius() {
        if (viewType == VIEW_TYPE_LINE) {
            val halfOfLineWidth = lineWidth.toFloat() / 2
            require(otpViewItemRadius <= halfOfLineWidth) { "The itemRadius can not be greater than lineWidth when viewType is line" }
        } else if (viewType == VIEW_TYPE_RECTANGLE) {
            val halfOfItemWidth = otpViewItemWidth.toFloat() / 2
            require(otpViewItemRadius <= halfOfItemWidth) { "The itemRadius can not be greater than itemWidth" }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width: Int
        val height: Int
        val boxHeight = otpViewItemHeight
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize
        } else {
            val boxesWidth =
                (otpViewItemCount - 1) * otpViewItemSpacing + otpViewItemCount * otpViewItemWidth
            width = boxesWidth + ViewCompat.getPaddingEnd(this) + ViewCompat.getPaddingStart(this)
            if (otpViewItemSpacing == 0) {
                width -= (otpViewItemCount - 1) * lineWidth
            }
        }
        height =
            if (heightMode == MeasureSpec.EXACTLY) heightSize else boxHeight + paddingTop + paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (start != text.length) {
            moveSelectionToEnd()
        }
        if (text.length == otpViewItemCount && onOtpCompletionListener != null) {
            onOtpCompletionListener!!.onOtpCompleted(text.toString())
        } else if (onOtpCompletionListener != null) {
            onOtpCompletionListener!!.onOtpIncomplete()
        }
        makeBlink()
    }



    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (text != null && selEnd != text!!.length) {
            moveSelectionToEnd()
        }
    }

    private fun moveSelectionToEnd() {
        if (text != null) {
            setSelection(text!!.length)
        }
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (lineColors == null || lineColors!!.isStateful) {
//            updateColors();
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        updatePaints()
        drawOtpView(canvas)
        canvas.restore()
    }

    private fun updatePaints() {
        paint.color = currentLineColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = lineWidth.toFloat()
        getPaint().color = currentTextColor
    }

    private fun drawOtpView(canvas: Canvas) {
        val nextItemToFill: Int = if (rtlTextDirection) {
            otpViewItemCount - 1
        } else {
            if (text != null) {
                text!!.length
            } else {
                0
            }
        }
        for (i in 0 until otpViewItemCount) {
            val itemSelected = isFocused && nextItemToFill == i
            val itemFilled = i < nextItemToFill
            var itemState: IntArray? = null
            if (itemFilled) {
                itemState = FILLED_STATE
            } else if (itemSelected) {
                itemState = SELECTED_STATE
            }
            paint.color = itemState?.let {
                getLineColorForState(it)
            } ?: currentLineColor
            updateItemRectF(i)
            updateCenterPoint()
            canvas.save()
            if (viewType == VIEW_TYPE_RECTANGLE) {
                updateOtpViewBoxPath(i)
                canvas.clipPath(path)
            }
            drawItemBackground(canvas, itemState)
            canvas.restore()
            if (itemSelected) {
                drawCursor(canvas)
            }
            if (viewType == VIEW_TYPE_RECTANGLE) {
                drawOtpBox(canvas, i)
            } else if (viewType == VIEW_TYPE_LINE) {
                drawOtpLine(canvas, i)
            }
            if (DBG) {
                drawAnchorLine(canvas)
            }
            if (rtlTextDirection) {
                val reversedPosition = otpViewItemCount - i
                if (text!!.length >= reversedPosition) {
                    drawInput(canvas, i)
                } else if (!TextUtils.isEmpty(hint) && hint.length == otpViewItemCount) {
                    drawHint(canvas, i)
                }
            } else {
                if (text!!.length > i) {
                    drawInput(canvas, i)
                } else if (!TextUtils.isEmpty(hint) && hint.length == otpViewItemCount) {
                    drawHint(canvas, i)
                }
            }
        }
        if (isFocused
            && text != null && text!!.length != otpViewItemCount && viewType == VIEW_TYPE_RECTANGLE
        ) {
            val index = text!!.length
            updateItemRectF(index)
            updateCenterPoint()
            updateOtpViewBoxPath(index)
            paint.color = getLineColorForState(SELECTED_STATE)
            drawOtpBox(canvas, index)
        }
    }

    private fun drawInput(canvas: Canvas, i: Int) {
        //allows masking for all number keyboard
        if (maskingChar != null &&
            (isNumberInputType(inputType) ||
                    isPasswordInputType(inputType))
        ) {
            drawMaskingText(canvas, i, maskingChar!![0].toString())
        } else if (isPasswordInputType(inputType)) {
            drawCircle(canvas, i)
        } else {
            drawText(canvas, i)
        }
    }

    private fun getLineColorForState(states: IntArray): Int {
        return if (lineColors != null) lineColors!!.getColorForState(
            states,
            currentLineColor
        ) else currentLineColor
    }

    private fun drawItemBackground(canvas: Canvas, backgroundState: IntArray?) {
        if (itemBackground == null) {
            return
        }
        val delta = lineWidth.toFloat() / 2
        val left = (itemBorderRect.left - delta).roundToInt()
        val top = (itemBorderRect.top - delta).roundToInt()
        val right = (itemBorderRect.right + delta).roundToInt()
        val bottom = (itemBorderRect.bottom + delta).roundToInt()
        itemBackground!!.setBounds(left, top, right, bottom)
        itemBackground!!.state = backgroundState ?: drawableState
        itemBackground!!.draw(canvas)
    }

    private fun updateOtpViewBoxPath(i: Int) {
        var drawRightCorner = false
        var drawLeftCorner = false
        if (otpViewItemSpacing != 0) {
            drawRightCorner = true
            drawLeftCorner = drawRightCorner
        } else {
            if (i == 0 && i != otpViewItemCount - 1) {
                drawLeftCorner = true
            }
            if (i == otpViewItemCount - 1 && i != 0) {
                drawRightCorner = true
            }
        }
        updateRoundRectPath(
            itemBorderRect,
            otpViewItemRadius.toFloat(),
            otpViewItemRadius.toFloat(),
            drawLeftCorner,
            drawRightCorner
        )
    }

    private fun drawOtpBox(canvas: Canvas, i: Int) {
        if (text != null && hideLineWhenFilled && i < text!!.length) {
            return
        }
        canvas.drawPath(path, paint)
    }

    private fun drawOtpLine(canvas: Canvas, i: Int) {
        if (text != null && hideLineWhenFilled && i < text!!.length) {
            return
        }
        var drawLeft: Boolean
        var drawRight: Boolean
        drawRight = true
        drawLeft = drawRight
        if (otpViewItemSpacing == 0 && otpViewItemCount > 1) {
            when (i) {
                0 -> {
                    drawRight = false
                }
                otpViewItemCount - 1 -> {
                    drawLeft = false
                }
                else -> {
                    drawRight = false
                    drawLeft = drawRight
                }
            }
        }
        paint.style = Paint.Style.FILL
        paint.strokeWidth = lineWidth.toFloat() / 10
        val halfLineWidth = lineWidth.toFloat() / 2
        itemLineRect[itemBorderRect.left - halfLineWidth, itemBorderRect.bottom - halfLineWidth, itemBorderRect.right + halfLineWidth] =
            itemBorderRect.bottom + halfLineWidth
        updateRoundRectPath(
            itemLineRect,
            otpViewItemRadius.toFloat(),
            otpViewItemRadius.toFloat(),
            drawLeft,
            drawRight
        )
        canvas.drawPath(path, paint)
    }

    private fun drawCursor(canvas: Canvas) {
        if (drawCursor) {
            val cx = itemCenterPoint.x
            val cy = itemCenterPoint.y
            val y = cy - cursorHeight / 2
            val color = paint.color
            val width = paint.strokeWidth
            paint.color = cursorColor
            paint.strokeWidth = cursorWidth.toFloat()
            canvas.drawLine(cx, y, cx, y + cursorHeight, paint)
            paint.color = color
            paint.strokeWidth = width
        }
    }

    private fun updateRoundRectPath(rectF: RectF, rx: Float, ry: Float, l: Boolean, r: Boolean) {
        updateRoundRectPath(rectF, rx, ry, l, r, r, l)
    }

    private fun updateRoundRectPath(
        rectF: RectF, rx: Float, ry: Float,
        tl: Boolean, tr: Boolean, br: Boolean, bl: Boolean
    ) {
        path.reset()
        val l = rectF.left
        val t = rectF.top
        val r = rectF.right
        val b = rectF.bottom
        val w = r - l
        val h = b - t
        val lw = w - 2 * rx
        val lh = h - 2 * ry
        path.moveTo(l, t + ry)
        if (tl) {
            path.rQuadTo(0f, -ry, rx, -ry)
        } else {
            path.rLineTo(0f, -ry)
            path.rLineTo(rx, 0f)
        }
        path.rLineTo(lw, 0f)
        if (tr) {
            path.rQuadTo(rx, 0f, rx, ry)
        } else {
            path.rLineTo(rx, 0f)
            path.rLineTo(0f, ry)
        }
        path.rLineTo(0f, lh)
        if (br) {
            path.rQuadTo(0f, ry, -rx, ry)
        } else {
            path.rLineTo(0f, ry)
            path.rLineTo(-rx, 0f)
        }
        path.rLineTo(-lw, 0f)
        if (bl) {
            path.rQuadTo(-rx, 0f, -rx, -ry)
        } else {
            path.rLineTo(-rx, 0f)
            path.rLineTo(0f, -ry)
        }
        path.rLineTo(0f, -lh)
        path.close()
    }

    private fun updateItemRectF(i: Int) {
        val halfLineWidth = lineWidth.toFloat() / 2
        var left = (scrollX
                + ViewCompat.getPaddingStart(this)
                + i * (otpViewItemSpacing + otpViewItemWidth) + halfLineWidth)
        if (otpViewItemSpacing == 0 && i > 0) {
            left -= lineWidth * i
        }
        val right = left + otpViewItemWidth - lineWidth
        val top = scrollY + paddingTop + halfLineWidth
        val bottom = top + otpViewItemHeight - lineWidth
        itemBorderRect[left, top, right] = bottom
    }

    private fun drawText(canvas: Canvas, i: Int) {
        val paint = getPaintByIndex()
        paint.color = currentTextColor
        if (rtlTextDirection) {
            val reversedPosition = otpViewItemCount - i
            val reversedCharPosition: Int = if (text == null) {
                reversedPosition
            } else {
                reversedPosition - text!!.length
            }
            if (reversedCharPosition <= 0 && text != null) {
                drawTextAtBox(canvas, paint, text, abs(reversedCharPosition))
            }
        } else if (text != null) {
            drawTextAtBox(canvas, paint, text, i)
        }
    }

    private fun drawMaskingText(canvas: Canvas, i: Int, maskingChar: String) {
        val paint = getPaintByIndex()
        paint.color = currentTextColor
        if (rtlTextDirection) {
            val reversedPosition = otpViewItemCount - i
            val reversedCharPosition: Int = if (text == null) {
                reversedPosition
            } else {
                reversedPosition - text!!.length
            }
            if (reversedCharPosition <= 0 && text != null) {
                drawTextAtBox(
                    canvas, paint, text.toString().replace(".".toRegex(), maskingChar),
                    abs(reversedCharPosition)
                )
            }
        } else if (text != null) {
            drawTextAtBox(canvas, paint, text.toString().replace(".".toRegex(), maskingChar), i)
        }
    }

    private fun drawHint(canvas: Canvas, i: Int) {
        val paint = getPaintByIndex()
        paint.color = currentHintTextColor
        if (rtlTextDirection) {
            val reversedPosition = otpViewItemCount - i
            val reversedCharPosition = reversedPosition - hint.length
            if (reversedCharPosition <= 0) {
                drawTextAtBox(canvas, paint, hint, abs(reversedCharPosition))
            }
        } else {
            drawTextAtBox(canvas, paint, hint, i)
        }
    }

    private fun drawTextAtBox(canvas: Canvas, paint: Paint, text: CharSequence?, charAt: Int) {
        paint.getTextBounds(text.toString(), charAt, charAt + 1, textRect)
        val cx = itemCenterPoint.x
        val cy = itemCenterPoint.y
        val x = cx - abs(textRect.width().toFloat()) / 2 - textRect.left
        val y = cy + abs(textRect.height().toFloat()) / 2 - textRect.bottom
        canvas.drawText(text!!, charAt, charAt + 1, x, y, paint)
    }

    private fun drawCircle(canvas: Canvas, i: Int) {
        val paint = getPaintByIndex()
        val cx = itemCenterPoint.x
        val cy = itemCenterPoint.y
        if (rtlTextDirection) {
            val reversedItemPosition = otpViewItemCount - i
            val reversedCharPosition = reversedItemPosition - hint.length
            if (reversedCharPosition <= 0) {
                canvas.drawCircle(cx, cy, paint.textSize / 2, paint)
            }
        } else {
            canvas.drawCircle(cx, cy, paint.textSize / 2, paint)
        }
    }

    private fun getPaintByIndex(): Paint {
        return getPaint()
        /*if (getText() != null && isAnimationEnable && i == getText().length() - 1) {
//            animatorTextPaint.setColor(getPaint().getColor());
            return animatorTextPaint;
        } else {
            return getPaint();
        }*/
    }

    private fun drawAnchorLine(canvas: Canvas) {
        var cx = itemCenterPoint.x
        var cy = itemCenterPoint.y
        paint.strokeWidth = 1f
        cx -= paint.strokeWidth / 2
        cy -= paint.strokeWidth / 2
        path.reset()
        path.moveTo(cx, itemBorderRect.top)
        path.lineTo(cx, itemBorderRect.top + abs(itemBorderRect.height()))
        canvas.drawPath(path, paint)
        path.reset()
        path.moveTo(itemBorderRect.left, cy)
        path.lineTo(itemBorderRect.left + abs(itemBorderRect.width()), cy)
        canvas.drawPath(path, paint)
        path.reset()
        paint.strokeWidth = lineWidth.toFloat()
    }

    private fun updateCenterPoint() {
        val cx = itemBorderRect.left + abs(itemBorderRect.width()) / 2
        val cy = itemBorderRect.top + abs(itemBorderRect.height()) / 2
        itemCenterPoint[cx] = cy
    }

    override fun getDefaultMovementMethod(): MovementMethod {
        return instance!!
    }

    override fun setTextSize(size: Float) {
        super.setTextSize(size)
        updateCursorHeight()
    }

    override fun setTextSize(unit: Int, size: Float) {
        super.setTextSize(unit, size)
        updateCursorHeight()
    }

    fun setOtpCompletionListener(otpCompletionListener: OnOtpCompletionListener?) {
        onOtpCompletionListener = otpCompletionListener
    }

    override fun setCursorVisible(visible: Boolean) {
        if (isCursorVisible != visible) {
            isCursorVisible = visible
            invalidateCursor(isCursorVisible)
            makeBlink()
        }
    }

    override fun isCursorVisible(): Boolean {
        return isCursorVisible
    }

    override fun onScreenStateChanged(screenState: Int) {
        super.onScreenStateChanged(screenState)
        if (screenState == SCREEN_STATE_ON) {
            resumeBlink()
        } else if (screenState == SCREEN_STATE_OFF) {
            suspendBlink()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        resumeBlink()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        suspendBlink()
    }

    private fun shouldBlink(): Boolean {
        return isCursorVisible() && isFocused
    }

    private fun makeBlink() {
        if (shouldBlink()) {
            if (blink == null) {
                blink = Blink()
            }
            removeCallbacks(blink)
            drawCursor = false
            postDelayed(blink, BLINK.toLong())
        } else {
            if (blink != null) {
                removeCallbacks(blink)
            }
        }
    }

    private fun suspendBlink() {
        if (blink != null) {
            blink!!.cancel()
            invalidateCursor(false)
        }
    }

    private fun resumeBlink() {
        if (blink != null) {
            blink!!.unCancel()
            makeBlink()
        }
    }

    private fun invalidateCursor(showCursor: Boolean) {
        if (drawCursor != showCursor) {
            drawCursor = showCursor
            invalidate()
        }
    }

    private fun updateCursorHeight() {
        val delta = 2 * dpToPx()
        cursorHeight = if (otpViewItemHeight - textSize > delta) textSize + delta else textSize
    }

    private inner class Blink : Runnable {
        private var cancelled = false
        override fun run() {
            if (cancelled) {
                return
            }
            removeCallbacks(this)
            if (shouldBlink()) {
                invalidateCursor(!drawCursor)
                postDelayed(this, BLINK.toLong())
            }
        }

        fun cancel() {
            if (!cancelled) {
                removeCallbacks(this)
                cancelled = true
            }
        }

        fun unCancel() {
            cancelled = false
        }
    }

    //endregion
    private fun dpToPx(): Int {
        return (2.toFloat() * resources.displayMetrics.density + 0.5f).toInt()
    }

    companion object {
        private const val DBG = false
        private const val BLINK = 500
        private const val DEFAULT_COUNT = 4
        private val NO_FILTERS = arrayOfNulls<InputFilter>(0)
        private val SELECTED_STATE = intArrayOf(
            android.R.attr.state_selected
        )
        private val FILLED_STATE = intArrayOf(
            R.attr.state_filled
        )
        private const val VIEW_TYPE_RECTANGLE = 0
        private const val VIEW_TYPE_LINE = 1
        private fun isPasswordInputType(inputType: Int): Boolean {
            val variation =
                inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
            return (variation
                    == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) || (variation
                    == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD) || (variation
                    == EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD)
        }

        private fun isNumberInputType(inputType: Int): Boolean {
            return inputType == EditorInfo.TYPE_CLASS_NUMBER
        }
    }

    init {
        val res = resources
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        //        animatorTextPaint.set(getPaint());
        val theme = context.theme
        val typedArray = theme.obtainStyledAttributes(attrs, R.styleable.OtpView, defStyleAttr, 0)
        viewType = typedArray.getInt(R.styleable.OtpView_viewType, VIEW_TYPE_RECTANGLE)
        otpViewItemCount = typedArray.getInt(R.styleable.OtpView_itemCount, DEFAULT_COUNT)
        otpViewItemHeight = typedArray.getDimension(
            R.styleable.OtpView_itemHeight,
            res.getDimensionPixelSize(R.dimen.otp_view_item_size).toFloat()
        ).toInt()
        otpViewItemWidth = typedArray.getDimension(
            R.styleable.OtpView_itemWidth,
            res.getDimensionPixelSize(R.dimen.otp_view_item_size).toFloat()
        ).toInt()
        otpViewItemSpacing = typedArray.getDimensionPixelSize(
            R.styleable.OtpView_itemSpacing,
            res.getDimensionPixelSize(R.dimen.otp_view_item_spacing)
        )
        otpViewItemRadius = typedArray.getDimension(R.styleable.OtpView_itemRadius, 0f).toInt()
        lineWidth = typedArray.getDimension(
            R.styleable.OtpView_lineWidth,
            res.getDimensionPixelSize(R.dimen.otp_view_item_line_width).toFloat()
        ).toInt()
        lineColors = typedArray.getColorStateList(R.styleable.OtpView_lineColor)
        isCursorVisible = typedArray.getBoolean(R.styleable.OtpView_android_cursorVisible, true)
        cursorColor = typedArray.getColor(R.styleable.OtpView_cursorColor, currentTextColor)
        cursorWidth = typedArray.getDimensionPixelSize(
            R.styleable.OtpView_cursorWidth,
            res.getDimensionPixelSize(R.dimen.otp_view_cursor_width)
        )
        itemBackground = typedArray.getDrawable(R.styleable.OtpView_android_itemBackground)
        hideLineWhenFilled = typedArray.getBoolean(R.styleable.OtpView_hideLineWhenFilled, false)
        rtlTextDirection = typedArray.getBoolean(R.styleable.OtpView_rtlTextDirection, false)
        maskingChar = typedArray.getString(R.styleable.OtpView_maskingChar)
        typedArray.recycle()
        if (lineColors != null) {
            currentLineColor = lineColors!!.defaultColor
        }
        updateCursorHeight()
        checkItemRadius()
        setMaxLength(otpViewItemCount)
        paint.strokeWidth = lineWidth.toFloat()
        //        setupAnimator();
        super.setCursorVisible(false)
        setTextIsSelectable(true)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }
}