package com.smallworldfs.moneytransferapp.utils.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class StyledButton : AppCompatButton {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @Suppress("UNUSED_PARAMETER")
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs) {
        var customFont = Typefaces.NORMAL
        val attrsArray = intArrayOf(android.R.attr.textStyle)
        if (attrs != null) {
            val t = context.obtainStyledAttributes(attrs, attrsArray)

            // Cambia la fuente si tiene estilo
            if (t.getInt(0, 0) == Typeface.BOLD) {
                customFont = Typefaces.BOLD
            } else if (t.getInt(0, 0) == Typeface.ITALIC) {
                customFont = Typefaces.SEMIBOLD
            }
            // Set custom font
            this.typeface = Typefaces.get(context, customFont)
            t.recycle()
        }
    }
}
