package com.jibanez.kotlincameraxmlkit.facedetection

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
import com.jibanez.kotlincameraxmlkit.drawable.PointsDrawable
import com.jibanez.kotlincameraxmlkit.drawable.BoundingRectDrawable
import com.jibanez.kotlincameraxmlkit.drawable.LinesDrawableListNullable
import com.jibanez.kotlincameraxmlkit.factory.AnalyzerFactory

class FaceDetectionContour : AnalyzerFactory {
    override fun createAnalyzerWithPreviewView(context: Context, previewView: PreviewView): MlKitAnalyzer {

        // Real-time contour detection
        val realTimeOpts = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setMinFaceSize(0.05f)
            .build()

        val detector = FaceDetection.getClient(realTimeOpts)

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val analyzer = MlKitAnalyzer(
            listOf(detector),
            CameraController.IMAGE_CAPTURE,
            ContextCompat.getMainExecutor(context)
        ) { result: MlKitAnalyzer.Result?  ->
            val faces = result?.getValue(detector)
            if (faces == null) {
                previewView.overlay.clear()
                return@MlKitAnalyzer
            }

            previewView.overlay.clear()

            //Just detect the counter points of the most prominent face
            for (face in faces) {

                //face contours
                val faceContoursPoints = PointsDrawable(face.getContour(FaceContour.FACE)?.points, Color.WHITE)
                val faceContoursLines = LinesDrawableListNullable(face.getContour(FaceContour.FACE)?.points, Color.WHITE)

                //Eye contours
                val leftEye = LinesDrawableListNullable(face.getContour(FaceContour.LEFT_EYE)?.points, Color.BLUE)
                val leftEyebrowTop = LinesDrawableListNullable(face.getContour(FaceContour.LEFT_EYEBROW_TOP)?.points, Color.CYAN)
                val leftEyebrowBottom = LinesDrawableListNullable(face.getContour(FaceContour.LEFT_EYEBROW_BOTTOM)?.points, Color.CYAN)
                val rightEye = LinesDrawableListNullable(face.getContour(FaceContour.RIGHT_EYE)?.points, Color.BLUE)
                val rightEyebrowTop = LinesDrawableListNullable(face.getContour(FaceContour.RIGHT_EYEBROW_TOP)?.points, Color.CYAN)
                val rightEyebrowBottom = LinesDrawableListNullable(face.getContour(FaceContour.RIGHT_EYEBROW_BOTTOM)?.points, Color.CYAN)

                //Cheek contours
                val leftCheek = PointsDrawable(face.getContour(FaceContour.LEFT_CHEEK)?.points, Color.GREEN)
                val rightCheek = PointsDrawable(face.getContour(FaceContour.RIGHT_CHEEK)?.points, Color.GREEN)

                //Nose contours
                val noseBridge = LinesDrawableListNullable(face.getContour(FaceContour.NOSE_BRIDGE)?.points, Color.MAGENTA)
                val noseBottom = LinesDrawableListNullable(face.getContour(FaceContour.NOSE_BOTTOM)?.points, Color.MAGENTA)

                //Lip contours
                val upperLipTop = LinesDrawableListNullable(face.getContour(FaceContour.UPPER_LIP_TOP)?.points, Color.RED)
                val upperLipBottom = LinesDrawableListNullable(face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points, Color.RED)
                val lowerLipTop = LinesDrawableListNullable(face.getContour(FaceContour.LOWER_LIP_TOP)?.points, Color.RED)
                val lowerLipBottom = LinesDrawableListNullable(face.getContour(FaceContour.LOWER_LIP_BOTTOM)?.points, Color.RED)

                previewView.overlay.add(faceContoursPoints)
                previewView.overlay.add(faceContoursLines)

                previewView.overlay.add(leftEye)
                previewView.overlay.add(leftEyebrowTop)
                previewView.overlay.add(leftEyebrowBottom)
                previewView.overlay.add(rightEye)
                previewView.overlay.add(rightEyebrowTop)
                previewView.overlay.add(rightEyebrowBottom)

                previewView.overlay.add(leftCheek)
                previewView.overlay.add(rightCheek)

                previewView.overlay.add(noseBridge)
                previewView.overlay.add(noseBottom)

                previewView.overlay.add(upperLipTop)
                previewView.overlay.add(upperLipBottom)
                previewView.overlay.add(lowerLipTop)
                previewView.overlay.add(lowerLipBottom)

                val faceBoundingBoxDrawable = BoundingRectDrawable(face.boundingBox, Color.YELLOW)
                previewView.overlay.add(faceBoundingBoxDrawable)
            }
        }

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
        return analyzer
    }
}