package com.smallworldfs.moneytransferapp.utils.widget

import android.content.Context
import android.graphics.Typeface
import android.text.InputFilter
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSingleActionErrorDialog
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

class StyledInputEditText : TextInputEditText {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @Suppress("UNUSED_PARAMETER")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs) {
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
            t.recycle()
        }
        this.typeface = Typefaces.get(context, customFont)

        val nonLatinCharactersInputFilter = arrayOf(
            InputFilter { source, _, _, _, _, _ ->
                val isLatin = "\\p{Latin}+"
                val digits = "[0-9 ]+"
                val symbols = "[:?!@#$%^&*(),.;\\-_'’+/=\"€£¥₩\\n]"
                for (i in source.indices) {
                    if (!source[i].toString().matches(isLatin.toRegex()) && !source[i].toString().matches(digits.toRegex()) && !source[i].toString().matches(symbols.toRegex())) {
                        getContext().showSingleActionErrorDialog(getContext().getString(R.string.an_error_has_occurred), getContext().getString(R.string.incorrect_language), null)
                        return@InputFilter STRING_EMPTY
                    }
                }
                null
            }
        )
        filters = nonLatinCharactersInputFilter
    }
}
