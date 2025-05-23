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
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PointF
import android.graphics.drawable.Drawable

class PointsDrawable(private val points: List<PointF>?, private val color: Int) : Drawable() {
    private val pointListPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        alpha = 200
        strokeWidth = 12F
    }

    override fun draw(canvas: Canvas) {

        if (points?.isNotEmpty() == true) {

            pointListPaint.color = color
            canvas.drawPoints(convertPointFListToFloatArray(points), pointListPaint)
//            points.forEach { point ->
//                canvas.drawPoint(point.x, point.y, pointListPaint)
//            }
        }
    }

    override fun setAlpha(alpha: Int) {
        pointListPaint.alpha = alpha
    }

    override fun setColorFilter(colorFiter: ColorFilter?) {
        pointListPaint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    private fun convertPointFListToFloatArray(points: List<PointF>): FloatArray {
        return points.flatMap { listOf(it.x, it.y) }.toFloatArray()
    }
}