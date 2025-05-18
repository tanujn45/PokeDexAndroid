package com.tanujn45.pokedex.ui.screens.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.tanujn45.pokedex.models.PokemonSummary
import com.tanujn45.pokedex.models.PokemonType
import com.tanujn45.pokedex.ui.components.FloatingBottomBar
import com.tanujn45.pokedex.ui.components.Preloader
import com.tanujn45.pokedex.ui.components.TypeBadge
import com.tanujn45.pokedex.ui.theme.PokemonColorMap
import com.tanujn45.pokedex.viewModel.SearchUiState
import com.tanujn45.pokedex.viewModel.SearchViewModel

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onPokemonSelected: (String) -> Unit,
    viewModel: SearchViewModel = viewModel(),
    navController: NavController,
    bottomPadding: Dp
) {
    val uiState by viewModel.searchUiState.collectAsState()

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
            SearchBar(onQueryChanged = viewModel::onQueryChanged)
            when (uiState) {
                is SearchUiState.Idle -> {
                    SearchList(
                        pokemonList = (uiState as SearchUiState.Idle).results,
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(nestedConn),
                        onClick = onPokemonSelected,
                        listState = listState, safeBottomPadding = bottomPadding
                    )
                }

                is SearchUiState.Loading -> {
                    Preloader(color = Color.Red)
                }

                is SearchUiState.Success -> {
                    SearchList(
                        pokemonList = (uiState as SearchUiState.Success).results,
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(nestedConn),
                        onClick = onPokemonSelected,
                        listState = listState, safeBottomPadding = bottomPadding
                    )
                }

                is SearchUiState.Empty -> {
                    Text(text = "No pokemon found")
                }

                is SearchUiState.Error -> {
                    Text(text = "Error: ${(uiState as SearchUiState.Error).message}")
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

@Composable
fun SearchList(
    modifier: Modifier = Modifier,
    pokemonList: List<PokemonSummary>,
    onClick: (String) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    safeBottomPadding: Dp = 16.dp
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = listState,
        contentPadding =
            PaddingValues(bottom = safeBottomPadding)
    ) {
        items(pokemonList) { pokemon ->
            Log.d("Pokemon", "Pokemon: $pokemon")
            SearchListItem(
                id = pokemon.id,
                name = pokemon.name,
                types = pokemon.typeNames,
                spriteUrl = pokemon.spriteUrl,
                onClick = { onClick(pokemon.name) },
                backgroundColor = pokemon.speciesColor
            )
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, onQueryChanged: (String) -> Unit) {
    var searchText by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        value = searchText,
        onValueChange = { text ->
            searchText = text
            onQueryChanged(searchText)
        },
        maxLines = 1,
        label = { Text("Search Pokemon") },
        leadingIcon = {
            Image(
                imageVector = Icons.Default.Search, contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = {
                    searchText = ""
                    onQueryChanged(searchText)
                }) {
                    Image(
                        imageVector = Icons.Default.Close, contentDescription = "Clear text"
                    )
                }
            }
        },
        shape = MaterialTheme.shapes.medium,
    )
}


@Composable
fun SearchListItem(
    modifier: Modifier = Modifier,
    id: Int,
    name: String,
    types: List<String>,
    spriteUrl: String,
    backgroundColor: String,
    onClick: () -> Unit
) {
    val background: Color = PokemonColorMap[backgroundColor] ?: Color.LightGray
    Box(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(listOf(background.copy(alpha = 0.8f), background)),
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation(),
            colors = CardDefaults.cardColors().copy(
                containerColor = Color.Transparent
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "#${id}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Log.d("Types", "Types: $types")
                    Row {
                        types.forEach { type ->
                            PokemonType.fromString(type)?.let {
                                TypeBadge(
                                    type = it, modifier = Modifier.padding(end = 4.dp)
                                )
                            }
                        }
                    }
                }
                AsyncImage(
                    model = spriteUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .padding(end = 16.dp)
                )
            }
        }
    }
}
