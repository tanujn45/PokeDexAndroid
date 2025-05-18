package com.tanujn45.pokedex.ui.screens.favorites

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tanujn45.pokedex.ui.components.FloatingBottomBar
import com.tanujn45.pokedex.ui.components.Preloader
import com.tanujn45.pokedex.ui.screens.search.SearchList
import com.tanujn45.pokedex.viewModel.FavoritesUiState
import com.tanujn45.pokedex.viewModel.FavoritesViewModel

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onPokemonSelected: (String) -> Unit,
    viewModel: FavoritesViewModel = viewModel(),
    navController: NavController,
    bottomPadding: Dp
) {
    val uiState by viewModel.favoritesUiState.collectAsState()

    val listState = rememberLazyListState()
    val barHeight = 56.dp + bottomPadding
    val barHeightPx = with(LocalDensity.current) { barHeight.toPx() }

    val scrollOffset = remember { mutableFloatStateOf(0f) }
    val nestedConn = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset, source: NestedScrollSource
            ): Offset {
                scrollOffset.floatValue =
                    (scrollOffset.floatValue - available.y).coerceIn(0f, barHeightPx)
                return Offset.Zero
            }
        }
    }
    val animatedOffsetDp by animateDpAsState(
        targetValue = with(LocalDensity.current) { scrollOffset.floatValue.toDp() },
        animationSpec = tween(durationMillis = 100)
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column {
            Text(
                text = "Favorites",
                modifier = Modifier.padding(vertical = 16.dp),
                style = MaterialTheme.typography.titleLarge
            )

            when (uiState) {
                is FavoritesUiState.Loading -> {
                    Preloader(color = Color.Red)
                }

                is FavoritesUiState.Success -> {
                    SearchList(
                        pokemonList = (uiState as FavoritesUiState.Success).results,
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(nestedConn),
                        onClick = onPokemonSelected,
                        listState = listState, safeBottomPadding = bottomPadding
                    )
                }

                is FavoritesUiState.Empty -> {
                    Text(text = "No Pokemon favorites yet")
                }

                is FavoritesUiState.Error -> {
                    Text(text = "Error: ${(uiState as FavoritesUiState.Error).message}")
                }
            }
        }
        FloatingBottomBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = animatedOffsetDp - bottomPadding)
        )
    }
}