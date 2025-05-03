package com.tanujn45.pokedex.ui.screens.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonType
import com.tanujn45.pokedex.models.bulbasaur

enum class DetailTab(val title: String) {
    Overview("Overview"), Stats("Stats"), Moves("Moves"), Evolution("Evolution"), Abilities("Abilities")
}

@Composable
fun DetailTabContent(modifier: Modifier = Modifier, pokemon: PokemonDetail) {
    var selectedTab by rememberSaveable { mutableStateOf(DetailTab.Overview) }
    val type: PokemonType? = PokemonType.fromString(pokemon.typeSlots.first().type.name)
    val tabs = DetailTab.entries.toTypedArray()

    Column(modifier = modifier) {
        Spacer(Modifier.height(24.dp))

        ScrollableTabRow(
            selectedTabIndex = selectedTab.ordinal,
            modifier = Modifier.fillMaxWidth(),
            edgePadding = 0.dp,
            indicator = {},
            divider = {}) {
            tabs.forEach { tab ->
                val isSelected = tab == selectedTab
                // Animate the background color between your two states
                val targetColor = if (isSelected) type?.color?.copy(alpha = 0.7f)
                                ?: MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant

                val backgroundColor by animateColorAsState(targetColor)
                Tab(
                    selected = isSelected,
                    onClick = { selectedTab = tab },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(targetColor)
                        .animateContentSize()
                ) {
                    Text(
                        text = tab.title.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        when (selectedTab) {
            DetailTab.Overview -> PokemonOverview(pokemon = pokemon)
            DetailTab.Stats -> PokemonStats(pokemon = pokemon, type)
            DetailTab.Moves -> {}
            DetailTab.Evolution -> {}
            DetailTab.Abilities -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailTabContentPreview() {
    DetailTabContent(pokemon = bulbasaur)
}
