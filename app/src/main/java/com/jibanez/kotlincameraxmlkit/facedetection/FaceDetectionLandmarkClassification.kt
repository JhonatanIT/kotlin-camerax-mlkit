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
import com.google.mlkit.vision.face.FaceLandmark
import com.jibanez.kotlincameraxmlkit.drawable.PointsDrawable
import com.jibanez.kotlincameraxmlkit.drawable.BoundingRectDrawable
import com.jibanez.kotlincameraxmlkit.drawable.TextDrawable
import com.jibanez.kotlincameraxmlkit.factory.AnalyzerFactory

class FaceDetectionLandmarkClassification : AnalyzerFactory {
    override fun createAnalyzerWithPreviewView(context: Context, previewView: PreviewView): MlKitAnalyzer {

        // High-accuracy landmark detection and face classification
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.5f)
            .build()

        val detector = FaceDetection.getClient(highAccuracyOpts)

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

//                val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
//                val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                // If landmark detection was enabled (mouth, ears, eyes, cheeks, and nose available):
                val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                leftEar?.let {
                    val leftEarDrawable = PointsDrawable(listOf(leftEar.position), Color.BLUE)
                    previewView.overlay.add(leftEarDrawable)
                }

                // If classification was enabled:
                if (face.smilingProbability != null) {
                    val smileProb = face.smilingProbability
                    previewView.overlay.add(TextDrawable("Smiling: $smileProb", boundingBox, 0))
                }
                if (face.leftEyeOpenProbability != null) {
                    val leftEyeOpenProb = face.leftEyeOpenProbability
                    previewView.overlay.add(TextDrawable("Left Eye Open: $leftEyeOpenProb", boundingBox, 1))

                }
                if (face.rightEyeOpenProbability != null) {
                    val rightEyeOpenProb = face.rightEyeOpenProbability
                    previewView.overlay.add(TextDrawable("Right Eye Open: $rightEyeOpenProb", boundingBox,2 ))
                }

                val faceBoundingBoxDrawable = BoundingRectDrawable(boundingBox, Color.YELLOW)
                previewView.overlay.add(faceBoundingBoxDrawable)
            }
        }

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
        return analyzer
    }
}