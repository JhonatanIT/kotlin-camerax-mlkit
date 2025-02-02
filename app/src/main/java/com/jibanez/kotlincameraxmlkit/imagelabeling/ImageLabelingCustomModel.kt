package com.jibanez.kotlincameraxmlkit.imagelabeling

import android.content.Context
import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import com.jibanez.kotlincameraxmlkit.drawable.TextDrawable
import com.jibanez.kotlincameraxmlkit.factory.AnalyzerFactory

/**
 * ImageLabeling with a custom model (Food classification model in this example)
 * https://www.kaggle.com/models?id=127,99,239,55,49,21,102,219,279,257,273,155,297,47,146,272,161,294&tfhub-redirect=true
 */
class ImageLabelingCustomModel : AnalyzerFactory {
    override fun createAnalyzerWithPreviewView(context: Context, previewView: PreviewView): MlKitAnalyzer {

        //TODO Train your own image classification model and replace the model file
        val localModel = LocalModel.Builder()
            .setAssetFilePath("1.tflite")       //food classification model
            // or .setAbsoluteFilePath(absolute file path to model file)
            // or .setUri(URI to model file)
            .build()

        val customImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
            .setConfidenceThreshold(0.1f)
            .setMaxResultCount(5)
            .build()
        val labelerDetector = ImageLabeling.getClient(customImageLabelerOptions)

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val analyzer = MlKitAnalyzer(
            listOf(labelerDetector),
            CameraController.IMAGE_CAPTURE,
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