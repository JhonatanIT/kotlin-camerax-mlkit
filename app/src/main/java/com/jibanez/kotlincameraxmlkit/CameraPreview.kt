package com.jibanez.kotlincameraxmlkit

import android.content.Context
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.jibanez.kotlincameraxmlkit.factory.AnalyzerFactory
import com.jibanez.kotlincameraxmlkit.barcodescanning.QrBarcodeScanner
import com.jibanez.kotlincameraxmlkit.facedetection.FaceDetectionContour
import com.jibanez.kotlincameraxmlkit.facedetection.FaceDetectionLandmarkClassification
import com.jibanez.kotlincameraxmlkit.facedetection.FaceDetectionTracking
import com.jibanez.kotlincameraxmlkit.facemeshdetection.FaceMesh
import com.jibanez.kotlincameraxmlkit.facemeshdetection.FaceMeshBoundingBox
import com.jibanez.kotlincameraxmlkit.factory.MLKitFeature
import com.jibanez.kotlincameraxmlkit.posedetection.PoseDetectionSingleImage
import com.jibanez.kotlincameraxmlkit.posedetection.PoseDetectionStream
import com.jibanez.kotlincameraxmlkit.selfiesegmentation.SelfieSegmentationStream
import com.jibanez.kotlincameraxmlkit.textrecognition.TextRecognition

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    context: Context,
    mlKitFeature: MLKitFeature = MLKitFeature.BARCODE_SCANNER,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val analyzerFactory: AnalyzerFactory = when (mlKitFeature) {
        MLKitFeature.BARCODE_SCANNER -> QrBarcodeScanner()
        MLKitFeature.TEXT_RECOGNITION -> TextRecognition()
        MLKitFeature.FACE_DETECTION_CONTOUR -> FaceDetectionContour()
        MLKitFeature.FACE_DETECTION_LANDMARK_CLASSIFICATION -> FaceDetectionLandmarkClassification()
        MLKitFeature.FACE_DETECTION_TRACKING -> FaceDetectionTracking()
        MLKitFeature.FACE_MESH_DETECTION_BOUNDING_BOX -> FaceMeshBoundingBox()
        MLKitFeature.FACE_MESH_DETECTION -> FaceMesh()
        MLKitFeature.POSE_DETECTION_STREAM -> PoseDetectionStream()
        MLKitFeature.POSE_DETECTION_SINGLE_IMAGE -> PoseDetectionSingleImage()
        MLKitFeature.SELF_SEGMENTATION_STREAM -> SelfieSegmentationStream()
    }

    AndroidView(
        factory = {
            PreviewView(it).apply {
                val analyzer = analyzerFactory.createAnalyzerWithPreviewView(context, this)

                //Implementing the ImageAnalysis analyzer
                controller.setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}

