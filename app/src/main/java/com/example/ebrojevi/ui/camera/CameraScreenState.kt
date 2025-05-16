package com.example.ebrojevi.ui.camera

import android.graphics.Bitmap

data class CameraScreenState(
    val recognizedText: String = "",
    val isLoading: Boolean = false,
    val displayedText: String = "",
    val lastBitmap: Bitmap? = null,
    val navigateToLoadingScreen: Boolean = false,
    val queryString: String = "",
)
