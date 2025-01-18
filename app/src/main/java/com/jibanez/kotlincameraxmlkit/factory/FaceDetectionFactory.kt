package com.jibanez.kotlincameraxmlkit.factory

import android.content.Context
import android.graphics.Color
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.jibanez.kotlincameraxmlkit.drawable.PointListDrawable
import com.jibanez.kotlincameraxmlkit.drawable.BoundingRectDrawable

class FaceDetectionFactory : AnalyzerFactory {
    override fun createAnalyzerWithPreviewView(context: Context, controller: CameraController, previewView: PreviewView): MlKitAnalyzer {

        //TODO explore Face Detection options

        // High-accuracy landmark detection and face classification
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        // Real-time contour detection
        val realTimeOpts = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()

        val detector = FaceDetection.getClient(realTimeOpts)

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

//                // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
//                // nose available):
//                val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
//                leftEar?.let {
//                    val leftEarPos = leftEar.position
//                }

                // If contour detection was enabled:
                val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

                val leftEyeContourDrawable = PointListDrawable(leftEyeContour, Color.BLUE)
                val upperLipBottomContourDrawable = PointListDrawable(upperLipBottomContour, Color.RED)

                previewView.overlay.add(leftEyeContourDrawable)
                previewView.overlay.add(upperLipBottomContourDrawable)

//                // If classification was enabled:
//                if (face.smilingProbability != null) {
//                    val smileProb = face.smilingProbability
//                }
//                if (face.rightEyeOpenProbability != null) {
//                    val rightEyeOpenProb = face.rightEyeOpenProbability
//                }
//
//                // If face tracking was enabled:
//                if (face.trackingId != null) {
//                    val id = face.trackingId
//                }
                //Draw a block frame
                val boundingRectDrawable = BoundingRectDrawable(boundingBox, Color.CYAN)
                previewView.overlay.add(boundingRectDrawable)
            }
        }

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
        return analyzer
    }
}