package com.example.ebrojevi.di

import com.example.ebrojevi.ui.camera.CameraScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { CameraScreenViewModel() }
}
