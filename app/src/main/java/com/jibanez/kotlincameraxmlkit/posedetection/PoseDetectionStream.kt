package com.jibanez.kotlincameraxmlkit.posedetection

import android.content.Context
import android.graphics.Color
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.jibanez.kotlincameraxmlkit.drawable.LinesDrawablePointsNullable
import com.jibanez.kotlincameraxmlkit.drawable.PointsDrawable
import com.jibanez.kotlincameraxmlkit.factory.AnalyzerFactory

class PoseDetectionStream : AnalyzerFactory {
    override fun createAnalyzerWithPreviewView(context: Context, previewView: PreviewView): MlKitAnalyzer {

        // Base pose detector with streaming frames, when depending on the pose-detection sdk
        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()

        val poseDetector = PoseDetection.getClient(options)

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val analyzer = MlKitAnalyzer(
            listOf(poseDetector),
            CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED,
            ContextCompat.getMainExecutor(context)
        ) { result: MlKitAnalyzer.Result?  ->
            val pose = result?.getValue(poseDetector)
            if (pose == null) {
                previewView.overlay.clear()
                return@MlKitAnalyzer
            }

            previewView.overlay.clear()

            // Get all PoseLandmarks. If no person was detected, the list will be empty
            val allPoseLandmarks = pose.allPoseLandmarks.map { it.position }
            val allPoseLandmarksPoints = PointsDrawable(allPoseLandmarks, Color.RED)
            previewView.overlay.add(allPoseLandmarksPoints)


            // Or get specific PoseLandmarks individually. These will all be null if no person was detected

            // Ears, eyes, and nose
            val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)?.position
            val rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER)?.position
            val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)?.position
            val rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER)?.position
            val nose = pose.getPoseLandmark(PoseLandmark.NOSE)?.position
            val leftEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER)?.position
            val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)?.position
            val leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER)?.position
            val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)?.position

            val earsEyesNosePoints = LinesDrawablePointsNullable(listOf(rightEar, rightEyeOuter, rightEye, rightEyeInner, nose, leftEyeInner, leftEye, leftEyeOuter, leftEar), Color.WHITE)
            previewView.overlay.add(earsEyesNosePoints)

            // Mouth
            val rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)?.position
            val leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)?.position

            val mouthPoints = LinesDrawablePointsNullable(listOf(rightMouth, leftMouth), Color.WHITE)
            previewView.overlay.add(mouthPoints)

            // Right hand
            val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)?.position
            val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)?.position
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)?.position
            val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)?.position

            val rightHandPoints = LinesDrawablePointsNullable(listOf(rightPinky, rightIndex, rightWrist, rightThumb), Color.WHITE)
            previewView.overlay.add(rightHandPoints)

            // Left hand
            val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)?.position
            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)?.position
            val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)?.position
            val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)?.position

            val leftHandPoints = LinesDrawablePointsNullable(listOf(leftThumb, leftWrist, leftIndex, leftPinky), Color.WHITE)
            previewView.overlay.add(leftHandPoints)

            // Arms and shoulders
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)?.position
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)?.position
            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)?.position
            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)?.position

            val armsShouldersPoints = LinesDrawablePointsNullable(listOf(rightPinky, rightWrist, rightElbow, rightShoulder, leftShoulder, leftElbow, leftWrist), Color.WHITE)
            previewView.overlay.add(armsShouldersPoints)

            //Body
            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)?.position
            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)?.position

            val bodyPoints = LinesDrawablePointsNullable(listOf(rightShoulder, rightHip, leftHip, leftShoulder), Color.WHITE)
            previewView.overlay.add(bodyPoints)

            //right leg
            val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)?.position
            val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)?.position
            val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)?.position
            val rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)?.position

            val rightLegPoints = LinesDrawablePointsNullable(listOf(rightHip, rightKnee, rightAnkle, rightHeel, rightFootIndex, rightAnkle), Color.WHITE)
            previewView.overlay.add(rightLegPoints)

            //left leg
            val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)?.position
            val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)?.position
            val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)?.position
            val leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)?.position

            val leftLegPoints = LinesDrawablePointsNullable(listOf(leftHip, leftKnee, leftAnkle, leftHeel, leftFootIndex, leftAnkle), Color.WHITE)
            previewView.overlay.add(leftLegPoints)

        }

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
        return analyzer
    }
}