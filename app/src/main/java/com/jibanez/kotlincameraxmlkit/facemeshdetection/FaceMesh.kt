package com.jibanez.kotlincameraxmlkit.facemeshdetection

import android.content.Context
import android.graphics.Color
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.Triangle
import com.google.mlkit.vision.facemesh.FaceMeshDetection
import com.google.mlkit.vision.facemesh.FaceMeshDetectorOptions
import com.google.mlkit.vision.facemesh.FaceMeshPoint
import com.jibanez.kotlincameraxmlkit.drawable.BoundingRectDrawable
import com.jibanez.kotlincameraxmlkit.factory.AnalyzerFactory

class FaceMesh : AnalyzerFactory {
    override fun createAnalyzerWithPreviewView(context: Context, previewView: PreviewView): MlKitAnalyzer {

        val faceMeshDetector = FaceMeshDetection.getClient(
            FaceMeshDetectorOptions.Builder()
                .setUseCase(FaceMeshDetectorOptions.FACE_MESH)
                .build()
        )

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val analyzer = MlKitAnalyzer(
            listOf(faceMeshDetector),
            CameraController.IMAGE_CAPTURE,
            ContextCompat.getMainExecutor(context)
        ) { result: MlKitAnalyzer.Result?  ->
            val faceMeshs = result?.getValue(faceMeshDetector)
            if (faceMeshs == null) {
                previewView.overlay.clear()
                return@MlKitAnalyzer
            }

            previewView.overlay.clear()

            for (faceMesh in faceMeshs) {

                // Gets all points
                val faceMeshpoints = faceMesh.allPoints
                for (faceMeshpoint in faceMeshpoints) {
                    val index: Int = faceMeshpoint.index
                    val position = faceMeshpoint.position
                }

                // Gets triangle info
                val triangles: List<Triangle<FaceMeshPoint>> = faceMesh.allTriangles
                for (triangle in triangles) {
                    // 3 Points connecting to each other and representing a triangle area.
                    val connectedPoints = triangle.allPoints
                }

                val faceBoundingBoxDrawable = BoundingRectDrawable(faceMesh.boundingBox, Color.YELLOW)
                previewView.overlay.add(faceBoundingBoxDrawable)
            }
        }

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
        return analyzer
    }
}