package com.tanujn45.pokedex.ui.screens.region

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.tanujn45.pokedex.models.regionStarters
import com.tanujn45.pokedex.viewModel.RegionDetailViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegionDetailScreen(
    regionName: String,
    navController: NavController,
    viewModel: RegionDetailViewModel = hiltViewModel()
) {

    LaunchedEffect(regionName) {
        viewModel.loadRegion(regionName)
    }

    val detail by viewModel.regionDetail.collectAsState()
    val entries by viewModel.pokedexEntries.collectAsState()
    val flavor by viewModel.flavorText.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        detail?.let { reg ->
            // … your banner and chips code unchanged …

            Spacer(Modifier.height(24.dp))

            // ─── Featured Starters Carousel ─────────────
            Text(
                "Featured Pokémon",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // entries is now List<PokedexEntry>
                val starters = regionStarters[reg.name]?.mapNotNull { starterName ->
                    // find the matching PokedexEntry
                    entries.find { it.pokemonSpecies.name == starterName }
                } ?: entries.take(3)  // fallback if we didn’t map

                items(starters) { entry ->
                    val id = entry.entryNumber
                    val name = entry.pokemonSpecies.name

                    Card(
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .size(96.dp)
                            .clickable { navController.navigate("detail/$name") }
                    ) {
                        AsyncImage(
                            model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
                            contentDescription = name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))


            // ─── Locations Grid ─────────────────────
            Text(
                "Locations",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                items(reg.locations) { area ->
                    Card(
                        onClick = { /* navigate deeper if desired */ },
                        shape = MaterialTheme.shapes.small,
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.height(100.dp)
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                area.name.replace('-', ' ').replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ─── Fun Fact / Flavor Text ─────────────
            Text(
                "Did you know?",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = flavor,
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}
