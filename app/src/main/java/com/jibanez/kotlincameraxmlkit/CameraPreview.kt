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
import com.jibanez.kotlincameraxmlkit.factory.BarcodeScannerFactory
import com.jibanez.kotlincameraxmlkit.factory.TextRecognitionFactory

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    context: Context,
    mlKitFeature: MLKitFeature = MLKitFeature.BARCODE_SCANNER,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val analyzerFactory: AnalyzerFactory = when (mlKitFeature) {
        MLKitFeature.BARCODE_SCANNER -> BarcodeScannerFactory()
        MLKitFeature.TEXT_RECOGNITION -> TextRecognitionFactory()
    }

    AndroidView(
        factory = {
            PreviewView(it).apply {
                val analyzer = analyzerFactory.createAnalyzerWithPreviewView(context, controller, this)

                //Implementing the ImageAnalysis analyzer
                controller.setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}

enum class MLKitFeature {
    BARCODE_SCANNER,
    TEXT_RECOGNITION
}

