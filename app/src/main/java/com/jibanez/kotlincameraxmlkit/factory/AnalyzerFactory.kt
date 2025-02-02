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
    FACE_DETECTION_TRACKING,
    FACE_MESH_DETECTION_BOUNDING_BOX,   //Beta phase: No working properly
    FACE_MESH_DETECTION,                //Beta phase: No working properly
    POSE_DETECTION_STREAM,
    POSE_DETECTION_SINGLE_IMAGE,
    SELF_SEGMENTATION_STREAM,           //Beta phase: No working properly
    IMAGE_LABELING_DEFAULT,
    IMAGE_LABELING_CUSTOM_MODEL,
    OBJECT_DETECTION_STREAM,
    OBJECT_DETECTION_SINGLE_IMAGE,      //TODO OBJECT_DETECTION_CUSTOM_MODEL
}