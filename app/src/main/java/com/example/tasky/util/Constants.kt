package com.example.tasky.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tasky.R

class Constants {

    companion object {

        const val BASE_URL = "https://tasky.pl-coding.com/"


        const val PREFERENCE_NAME = "tasky_preference"
        const val REFRESH_TOKEN_KEY = "refreshToken"
        const val ACCESS_TOKEN = "accessToken"
        const val ACCESS_TOKEN_EXPIRATION_STAMP = "accessTokenExpirationStamp"
        const val FULL_NAME = "fullName"
        const val USER_ID = "UserId"






        fun textSpan(text: String, start: Int, end: Int, color: Int): SpannableString {
            val spannableString = SpannableString(text)

            val colorSpan = ForegroundColorSpan(color)
            spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            return spannableString
        }

        fun makeLinks(context: Context, textView: TextView, text: String, phrase:String, phraseColor: Int, onClickAction: () -> Unit): SpannableString {
            val spannableString = SpannableString(text)

            val clickableSpan = object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = phraseColor
                    ds.isUnderlineText = false
                }
                override fun onClick(view: View) {
                    onClickAction
                }
            }
            val start = text.indexOf(phrase)
            val end = start + phrase.length

            spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            val colorSpan = ForegroundColorSpan(phraseColor)

            textView.text = spannableString
            textView.movementMethod = LinkMovementMethod.getInstance()
            textView.highlightColor = Color.TRANSPARENT

            return spannableString
        }

        fun makeLink(text: String, phrase:String, phraseColor: Int, listener: View.OnClickListener): SpannableString {
            val spannableString = SpannableString(text)

            val clickableSpan = object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = phraseColor
                    ds.isUnderlineText = false
                }
                override fun onClick(view: View) {
                    listener.onClick(view)
                }
            }
            val start = text.indexOf(phrase)
            val end = start.plus(phrase.length)

            spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            return spannableString
        }

        fun getProgressDialog(context: Context): Dialog {

            return Dialog(context).apply {
                setContentView(R.layout.progress_loader_layout)
                window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setCancelable(false)
            }
        }
    }
}