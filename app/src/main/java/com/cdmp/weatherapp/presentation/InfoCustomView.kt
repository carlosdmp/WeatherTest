package com.cdmp.weatherapp.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.cdmp.weatherapp.R

class InfoCustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context)
            .inflate(R.layout.custom_info, this, true)
        attrs?.let { loadAttrs(context, it) }
    }

    private fun loadAttrs(context: Context, attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomInfo, 0, 0)
        findViewById<TextView>(R.id.tv_info_header).text = attributes.getString(R.styleable.CustomInfo_header)
        findViewById<ImageView>(R.id.iv_logo).run {
            setImageDrawable(
                ContextCompat.getDrawable(
                    context, attributes.getResourceId(R.styleable.CustomInfo_image, R.drawable.ic_temperature)
                )
            )
        }
        attributes.recycle()
    }

    fun setValue(text: String) {
        findViewById<TextView>(R.id.tv_info).text = text
    }
}