/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jibanez.kotlincameraxmlkit.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable

class TextDrawable(private val text: String, private val boundingBox: Rect, private val order: Int) : Drawable() {

    private val contentRectPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.YELLOW
        alpha = 255
    }

    private val contentTextPaint = Paint().apply {
        color = Color.DKGRAY
        alpha = 255
        textSize = 25F
    }

    private val contentPadding = 20
    private var textWidth = contentTextPaint.measureText(text).toInt()

    override fun draw(canvas: Canvas) {

        //(contentTextPaint.textSize.toInt() + contentPadding) * order -> to make the text appear in a list
        canvas.drawRect(
            Rect(
                boundingBox.left,
                boundingBox.bottom + contentPadding/2 + (contentTextPaint.textSize.toInt() + contentPadding) * order,
                boundingBox.left + textWidth + contentPadding*2,
                boundingBox.bottom + contentTextPaint.textSize.toInt() + contentPadding + (contentTextPaint.textSize.toInt() + contentPadding) * order) ,
            contentRectPaint
        )
        canvas.drawText(
            text,
            (boundingBox.left + contentPadding).toFloat(),
            (boundingBox.bottom + contentPadding*2 + (contentTextPaint.textSize.toInt() + contentPadding) * order).toFloat(),
            contentTextPaint
        )
    }

    override fun setAlpha(alpha: Int) {
        contentRectPaint.alpha = alpha
        contentTextPaint.alpha = alpha
    }

    override fun setColorFilter(colorFiter: ColorFilter?) {
        contentRectPaint.colorFilter = colorFilter
        contentTextPaint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}