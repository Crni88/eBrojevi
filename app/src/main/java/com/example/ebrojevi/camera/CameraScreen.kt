package com.example.ebrojevi.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.OptIn
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.TimeUnit

//TODO This needs to be further optimized and refactored. This is only a prototype!
@Composable
fun CameraScreenRoot(
    viewModel: CameraScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CameraScreen(
        state = state,
        onImageCaptured = viewModel::processImage
    )
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraScreen(
    state: CameraScreenState,
    onImageCaptured: (Bitmap) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor = ContextCompat.getMainExecutor(context)
    val coroutineScope = rememberCoroutineScope()
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var cameraInfo by remember { mutableStateOf<CameraInfo?>(null) }

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    var torchEnabled by remember { mutableStateOf(false) }
    var zoomRatio by remember { mutableFloatStateOf(1f) }
    var focusOffset by remember { mutableStateOf<Offset?>(null) }
    var focusAlpha by remember { mutableFloatStateOf(1f) }
    val animatedAlpha by animateFloatAsState(
        targetValue = focusAlpha,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )

    val previewUseCase = remember { Preview.Builder().build() }
    val imageCaptureUseCase = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    LaunchedEffect(hasPermission) {
        if (!hasPermission) return@LaunchedEffect
        val cameraProvider = ProcessCameraProvider.getInstance(context).await()
        cameraProvider.unbindAll()

        val camera = cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            previewUseCase,
            imageCaptureUseCase
        )
        cameraControl = camera.cameraControl
        cameraInfo = camera.cameraInfo
        previewUseCase.surfaceProvider = previewView.surfaceProvider
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(250.dp))
            Spacer(modifier = Modifier.height(50.dp))
            Text(state.displayedText)
        } else {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(650.dp)
            ) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier
                        .matchParentSize()
                        .pointerInput(Unit) {
                            detectTransformGestures { _, _, zoomChange, _ ->
                                val newZoom = (zoomRatio * zoomChange).coerceIn(
                                    cameraInfo?.zoomState?.value?.minZoomRatio ?: 1f,
                                    cameraInfo?.zoomState?.value?.maxZoomRatio ?: 1f
                                )
                                zoomRatio = newZoom
                                cameraControl?.setZoomRatio(newZoom)
                            }
                        }
                )

                focusOffset?.let { offset ->
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawCircle(
                            Color.White.copy(alpha = animatedAlpha),
                            radius = 40f,
                            center = offset
                        )
                    }
                }
                // Torch toggle button
                IconButton(
                    onClick = {
                        torchEnabled = !torchEnabled
                        cameraControl?.enableTorch(torchEnabled)
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = if (torchEnabled) Icons.Default.FlashlightOff else Icons.Default.FlashlightOn,
                        contentDescription = "Toggle torch"
                    )
                }

                previewView.post {
                    previewView.setOnTouchListener { view, event ->
                        if (event.action == MotionEvent.ACTION_UP) {
                            view.performClick()
                            val point = previewView.meteringPointFactory
                                .createPoint(event.x, event.y)
                            val action = FocusMeteringAction.Builder(
                                point,
                                FocusMeteringAction.FLAG_AF or FocusMeteringAction.FLAG_AE
                            )
                                .setAutoCancelDuration(5, TimeUnit.SECONDS)
                                .build()
                            cameraControl?.startFocusAndMetering(action)

                            focusOffset = Offset(event.x, event.y)
                            focusAlpha = 1f
                            coroutineScope.launch {
                                delay(600)
                                focusAlpha = 0f
                                delay(600)
                                focusOffset = null
                            }
                        }
                        true
                    }
                }
            }

            Spacer(Modifier.height(50.dp))

            Button(onClick = {
                imageCaptureUseCase.takePicture(
                    mainExecutor,
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(imageProxy: ImageProxy) {
                            val bmp = imageProxy.toBitmap()
                            imageProxy.close()
                            onImageCaptured(bmp)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e(
                                "CameraCapture",
                                "Image capture failed: ${exception.message}",
                                exception
                            )
                        }
                    }
                )
            }) {
                Text("SNAP!")
            }
        }
    }
}