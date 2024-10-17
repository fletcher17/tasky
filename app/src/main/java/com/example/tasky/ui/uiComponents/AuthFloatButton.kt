package com.example.tasky.ui.uiComponents

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.tasky.databinding.AuthFloatButtonLayoutBinding

class AuthFloatButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding =
        AuthFloatButtonLayoutBinding.inflate(LayoutInflater.from(context), this, true)
}