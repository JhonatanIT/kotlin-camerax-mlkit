package com.jibanez.kotlincameraxmlkit.selfiesegmentation

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import com.jibanez.kotlincameraxmlkit.factory.AnalyzerFactory

class SelfieSegmentationStream : AnalyzerFactory {
    override fun createAnalyzerWithPreviewView(context: Context, previewView: PreviewView): MlKitAnalyzer {

        val options =
            SelfieSegmenterOptions.Builder()
                .setDetectorMode(SelfieSegmenterOptions.STREAM_MODE)
                .enableRawSizeMask()
                .build()

        val segmentationDetector = Segmentation.getClient(options)

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val analyzer = MlKitAnalyzer(
            listOf(segmentationDetector),
            CameraController.TAP_TO_FOCUS_NOT_STARTED,   // video is not smooth
            ContextCompat.getMainExecutor(context)
        ) { result: MlKitAnalyzer.Result?  ->
            val segmentationMask = result?.getValue(segmentationDetector)

            if (segmentationMask == null) {
                previewView.overlay.clear()
                return@MlKitAnalyzer
            }

            previewView.overlay.clear()

            val mask = segmentationMask.buffer
            val maskWidth = segmentationMask.width
            val maskHeight = segmentationMask.height

            // Use indices for clarity and to avoid potential issues with mutable loop variables.
            for (y in 0 until maskHeight) {
                for (x in 0 until maskWidth) {
                    // Calculate the index directly for better performance and clarity.
                    val index = y * maskWidth + x

                    // Check if the index is within bounds.
                    if (index < mask.capacity()) {
                        // Get the confidence of the (x, y) pixel in the mask being in the foreground.
                        val foregroundConfidence = mask.get(index)

                        // Process the foregroundConfidence here...
                        println("Confidence at ($x, $y): $foregroundConfidence")
                        //TODO paint the mask

                    } else {
                        println("Index out of bounds: $index")
                    }
                }
            }
        }

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
        return analyzer
    }
}