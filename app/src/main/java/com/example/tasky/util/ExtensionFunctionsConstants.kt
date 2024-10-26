package com.example.tasky.util

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import com.example.tasky.R

object ExtensionFunctionsConstants {

    fun EditText.emailAddTextChange() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches()) {
                    this@emailAddTextChange.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_check,
                            resources.newTheme()
                        ),
                        null
                    )
                } else {
                    this@emailAddTextChange.error = "Email format is wrong"
                    this@emailAddTextChange.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                    )
                }
            }

        })
    }

    fun EditText.nameAddTextChange() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.isNotEmpty() == true) {
                    this@nameAddTextChange.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_check,
                            resources.newTheme()
                        ),
                        null
                    )
                } else {
                    this@nameAddTextChange.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                    )
                }
            }

        })
    }

}