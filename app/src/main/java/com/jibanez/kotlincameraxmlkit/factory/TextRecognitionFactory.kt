package com.jibanez.kotlincameraxmlkit.factory

import android.content.Context
import android.graphics.Color
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.jibanez.kotlincameraxmlkit.drawable.BoundingRectDrawable

class TextRecognitionFactory : AnalyzerFactory {
    override fun createAnalyzerWithPreviewView(context: Context, controller: CameraController, previewView: PreviewView): MlKitAnalyzer {

        //TODO explore Text Recognition options
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)  // Latin script library

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val analyzer = MlKitAnalyzer(
            listOf(recognizer),
            CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED,
            ContextCompat.getMainExecutor(context)
        ) { result: MlKitAnalyzer.Result?  ->
            val textResults = result?.getValue(recognizer)
            if (textResults == null) {
                previewView.overlay.clear()
                return@MlKitAnalyzer
            }

            // Handle text recognition results here
            val resultText = textResults.text
            previewView.overlay.clear()

            for (block in textResults.textBlocks) {
                val blockText = block.text
                val blockCornerPoints = block.cornerPoints
                val blockFrame = block.boundingBox

                //Draw a block frame
                val boundingRectDrawable = BoundingRectDrawable(blockFrame, Color.YELLOW)
                println(blockText)
                previewView.overlay.add(boundingRectDrawable)

                for (line in block.lines) {
                    val lineText = line.text
                    val lineCornerPoints = line.cornerPoints
                    val lineFrame = line.boundingBox
                    for (element in line.elements) {
                        val elementText = element.text
                        val elementCornerPoints = element.cornerPoints
                        val elementFrame = element.boundingBox
                    }
                }
            }
        }

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
        return analyzer
    }
}