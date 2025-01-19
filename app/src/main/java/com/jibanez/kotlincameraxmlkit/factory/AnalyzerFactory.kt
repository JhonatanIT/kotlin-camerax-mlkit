package com.jibanez.kotlincameraxmlkit.factory

import android.content.Context
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.PreviewView

interface AnalyzerFactory {
    fun createAnalyzerWithPreviewView(context: Context, previewView: PreviewView): MlKitAnalyzer
}

enum class MLKitFeature {
    BARCODE_SCANNER,
    TEXT_RECOGNITION,
    FACE_DETECTION_CONTOUR,
    FACE_DETECTION_LANDMARK_CLASSIFICATION,
    FACE_DETECTION_TRACKING
}