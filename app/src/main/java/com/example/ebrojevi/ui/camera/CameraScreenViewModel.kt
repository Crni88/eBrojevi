package com.example.ebrojevi.ui.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CameraScreenViewModel() : ViewModel() {
    private val _state = MutableStateFlow(CameraScreenState())
    val state: StateFlow<CameraScreenState> = _state

    private val textRecognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun processImage(bitmap: Bitmap) {
        _state.update { it.copy(isLoading = true, displayedText = "Processing image...") }
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val input = InputImage.fromBitmap(bitmap, /* rotationDegrees= */ 0)
                val result = textRecognizer.process(input).await()
                _state.update {
                    it.copy(
                        displayedText = "Extracting text...",
                        recognizedText = result.text,
                        lastBitmap = bitmap,
                        navigateToLoadingScreen = true,
                        queryString = getQueryString(result.text)
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        displayedText = "Unable to extract text...",
                        recognizedText = "Error: ${e.localizedMessage}",
                        lastBitmap = bitmap
                    )
                }
            }
        }
    }

    private fun getQueryString(inputValue: String): String {
        val pattern = Regex("""\bE\d{3}[a-zA-Z]?\b""")
        return pattern.findAll(inputValue)
            .joinToString("/") { it.value }
    }

    fun resetNavigationState() {
        _state.update {
            it.copy(
                navigateToLoadingScreen = false,
                isLoading = false,
                recognizedText = "",
            )
        }
    }
}