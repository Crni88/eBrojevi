package com.example.ebrojevi.ui.loading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ebrojevi.ui.components.eNumbersLoader

@Composable
fun LoadingScreenRoot(
    viewModel: LoadingScreenViewModel = viewModel(),
    navController: NavHostController,
    queryString: String
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoadingScreenScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun LoadingScreenScreen(
    state: LoadingScreenState,
    onAction: (LoadingScreenAction) -> Unit,
) {
    eNumbersLoader(state.displayedText)
}
