package com.jibanez.kotlincameraxmlkit.factory

import android.content.Context
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView

interface AnalyzerFactory {
    fun createAnalyzerWithPreviewView(context: Context, controller: CameraController, previewView: PreviewView): MlKitAnalyzer
}