package com.jibanez.kotlincameraxmlkit.objectdetection

import android.content.Context
import android.graphics.Color
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.jibanez.kotlincameraxmlkit.drawable.BoundingRectDrawable
import com.jibanez.kotlincameraxmlkit.drawable.TextDrawable
import com.jibanez.kotlincameraxmlkit.factory.AnalyzerFactory

class ObjectDetectionStream : AnalyzerFactory {
    override fun createAnalyzerWithPreviewView(context: Context, previewView: PreviewView): MlKitAnalyzer {

        // Live detection and tracking
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
//            .enableMultipleObjects()
            .enableClassification()  // Optional
            .build()

        val objectDetector = ObjectDetection.getClient(options)

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val analyzer = MlKitAnalyzer(
            listOf(objectDetector),
            CameraController.IMAGE_CAPTURE,
            ContextCompat.getMainExecutor(context)
        ) { result: MlKitAnalyzer.Result?  ->
            val detectedObjects = result?.getValue(objectDetector)
            if (detectedObjects == null) {
                previewView.overlay.clear()
                return@MlKitAnalyzer
            }

            previewView.overlay.clear()

            for (detectedObject in detectedObjects) {
                val boundingBox = detectedObject.boundingBox
                previewView.overlay.add(BoundingRectDrawable(boundingBox, Color.YELLOW))

                val trackingId = detectedObject.trackingId
                previewView.overlay.add(TextDrawable("Tracking ID: $trackingId", boundingBox, 0))

                detectedObject.labels.forEachIndexed { index, label ->
                    val text = label.text
                    val confidence = label.confidence

                    // Create the text to display.
                    val displayText = "Label: $text, Confidence: $confidence"

                    // Add the TextDrawable to the overlay at the top left corner
                    previewView.overlay.add(TextDrawable(displayText, boundingBox, index+1))
                }
            }
        }

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
        return analyzer
    }
}