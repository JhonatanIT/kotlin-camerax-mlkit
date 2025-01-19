package com.jibanez.kotlincameraxmlkit.facedetection

import android.content.Context
import android.graphics.Color
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.jibanez.kotlincameraxmlkit.drawable.BoundingRectDrawable
import com.jibanez.kotlincameraxmlkit.drawable.TextDrawable
import com.jibanez.kotlincameraxmlkit.factory.AnalyzerFactory

class FaceDetectionTracking : AnalyzerFactory {
    override fun createAnalyzerWithPreviewView(context: Context, previewView: PreviewView): MlKitAnalyzer {

        val trakkingFace = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .enableTracking()
            .build()

        val detector = FaceDetection.getClient(trakkingFace)

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val analyzer = MlKitAnalyzer(
            listOf(detector),
            CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED,
            ContextCompat.getMainExecutor(context)
        ) { result: MlKitAnalyzer.Result?  ->
            val faces = result?.getValue(detector)
            if (faces == null) {
                previewView.overlay.clear()
                return@MlKitAnalyzer
            }

            previewView.overlay.clear()

            for (face in faces) {

                val boundingBox = face.boundingBox

                // If face tracking was enabled:
                if (face.trackingId != null) {
                    val id = face.trackingId
                    previewView.overlay.add(TextDrawable("Face ID: $id", boundingBox, 0))
                }

                val faceBoundingBoxDrawable = BoundingRectDrawable(boundingBox, Color.YELLOW)
                previewView.overlay.add(faceBoundingBoxDrawable)
            }
        }

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
        return analyzer
    }
}