package com.jibanez.kotlincameraxmlkit.factory

import android.content.Context
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.jibanez.kotlincameraxmlkit.qr.QrCodeDrawable
import com.jibanez.kotlincameraxmlkit.qr.QrCodeViewModel

class BarcodeScannerFactory : AnalyzerFactory {
    override fun createAnalyzerWithPreviewView(context: Context, controller: CameraController, previewView: PreviewView): MlKitAnalyzer {

        //TODO Explore Barcode Scanner options (FORMAT_QR_CODE)
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE)
            .build()
        val barcodeScanner = BarcodeScanning.getClient(options)

        return MlKitAnalyzer(
            listOf(barcodeScanner),
            CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED,
            ContextCompat.getMainExecutor(context)
        ) { result: MlKitAnalyzer.Result? ->
            val barcodeResults = result?.getValue(barcodeScanner)
            if (barcodeResults.isNullOrEmpty()) {
                previewView.overlay.clear()
                previewView.setOnTouchListener { _, _ -> false } //no-op
                return@MlKitAnalyzer
            }

            val qrCodeViewModel = QrCodeViewModel(barcodeResults[0])
            val qrCodeDrawable = QrCodeDrawable(qrCodeViewModel)

            previewView.setOnTouchListener(qrCodeViewModel.qrCodeTouchCallback)
            previewView.overlay.clear()
            previewView.overlay.add(qrCodeDrawable)
        }
    }
}