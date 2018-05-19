package com.axonactive.myroom.validation

import android.content.Context
import android.view.View
import com.axonactive.myroom.R
import com.rengwuxian.materialedittext.MaterialEditText
import com.rengwuxian.materialedittext.validation.RegexpValidator

/**
 * Created by Phuong Nguyen on 5/14/2018.
 */
object Validator {

    fun validateEmpty(editText: MaterialEditText, parent : Context) {
    editText.onFocusChangeListener = (View.OnFocusChangeListener { _ , hasFocus ->
        if (!hasFocus && editText.text.trim().isBlank()) {
                editText.error = parent.resources.getString(R.string.field_cannot_empty)
            }
        })
    }

    fun validateRegex(editText: MaterialEditText, parent: Context, regex : String, errorMsg : String) {
        editText.onFocusChangeListener = (View.OnFocusChangeListener { _ , hasFocus ->
            if (!hasFocus) {
                if (editText.text.trim().isNotBlank()) {
                    editText.validateWith(RegexpValidator(errorMsg, regex))
                }
                else {
                    editText.error = parent.resources.getString(R.string.field_cannot_empty)
                }
            }
        })
    }

}