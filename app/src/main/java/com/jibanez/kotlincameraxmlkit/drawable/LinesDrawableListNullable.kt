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
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PointF
import android.graphics.drawable.Drawable

class LinesDrawableListNullable(private val points: List<PointF>?, private val color: Int) : Drawable() {
    private val pointListPaint = Paint().apply {
        style = Paint.Style.STROKE
        alpha = 200
        strokeWidth = 3F
    }

    override fun draw(canvas: Canvas) {

        if (points?.isNotEmpty() == true) {

            pointListPaint.color = color
            val path = Path()

            if (points.isNotEmpty()) {
                path.moveTo(points[0].x, points[0].y)
            }

            for (point in points) {
                path.lineTo(point.x, point.y)
            }
            canvas.drawPath(path, pointListPaint)
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

}