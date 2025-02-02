package com.jibanez.kotlincameraxmlkit.imagelabeling

import android.content.Context
import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.jibanez.kotlincameraxmlkit.drawable.TextDrawable
import com.jibanez.kotlincameraxmlkit.factory.AnalyzerFactory

class ImageLabelingDefault : AnalyzerFactory {
    override fun createAnalyzerWithPreviewView(context: Context, previewView: PreviewView): MlKitAnalyzer {

        // To use default options:
        val labelerDetector = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val analyzer = MlKitAnalyzer(
            listOf(labelerDetector),
            CameraController.IMAGE_ANALYSIS,
            ContextCompat.getMainExecutor(context)
        ) { result: MlKitAnalyzer.Result?  ->
            val labels = result?.getValue(labelerDetector)
            if (labels == null) {
                previewView.overlay.clear()
                return@MlKitAnalyzer
            }

            previewView.overlay.clear()

            labels.forEachIndexed { index, label ->
                val text = label.text
                val confidence = label.confidence

                // Create the text to display.
                val displayText = "Label: $text, Confidence: $confidence"

                // Add the TextDrawable to the overlay at the top left corner
                previewView.overlay.add(TextDrawable(displayText, Rect(0, 0, 0, 0), index))
            }
        }

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
        return analyzer
    }
}