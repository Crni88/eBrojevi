package com.example.ebrojevi.ui.numberDetailsScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NumbersDetailsRoot(
    viewModel: NumbersDetailsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NumbersDetailsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun NumbersDetailsScreen(
    state: NumbersDetailsState,
    onAction: (NumbersDetailsAction) -> Unit,
) {

}