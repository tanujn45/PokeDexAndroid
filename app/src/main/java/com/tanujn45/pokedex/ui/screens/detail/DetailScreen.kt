package com.tanujn45.pokedex.ui.screens.detail


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonType
import com.tanujn45.pokedex.models.bulbasaur
import com.tanujn45.pokedex.viewModel.PokeUiState
import com.tanujn45.pokedex.viewModel.PokeViewModel

@Composable
fun PokemonDetailScreen(
    modifier: Modifier = Modifier, viewModel: PokeViewModel = viewModel()
) {
    when (val pokeUiState = viewModel.pokeUiState) {
        is PokeUiState.Loading -> Text("Loading...")
        is PokeUiState.Success -> {
            val pokemon = pokeUiState.pokemon
            PokemonDetailContent(pokemon = pokemon, modifier = modifier)
        }

        is PokeUiState.Error -> Text("Error loading Pokemon details")
    }
}

@Composable
fun PokemonDetailContent(
    modifier: Modifier = Modifier, pokemon: PokemonDetail, isPreview: Boolean = false
) {
    val type: PokemonType? = PokemonType.fromString(pokemon.typeSlots.first().type.name)
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        PokemonDetailHeader(pokemon = pokemon, isPreview = isPreview)
        PokemonTypeBadges(pokemon.typeSlots)
        PokemonDetailBasicInfo(pokemon = pokemon, modifier = Modifier.padding(vertical = 16.dp))
//        Text("Some random text about the pokemon, theres is more of that text and I am not kidding I am having fun making this project")
//
//        Text(
//            "Abilities: ${pokemon.abilitySlots.joinToString { it.ability.name }}",
//            style = MaterialTheme.typography.bodyMedium
//        )
        pokemon.statSlots.forEach { slot ->
            PokemonStatItem(
                statName = slot.stat.name,
                statValue = slot.baseStat,
                modifier = Modifier.padding(vertical = 4.dp),
                color = type?.color ?: MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonDetailPreview() {
    PokemonDetailContent(pokemon = bulbasaur, isPreview = true)
}

@Composable
fun PokemonStatItem(
    modifier: Modifier = Modifier,
    statName: String,
    statValue: Int,
    maxStat: Int = 255,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(2f)
                .padding(end = 8.dp)
        )

        LinearProgressIndicator(
            progress = { statValue / maxStat.toFloat() },
            modifier = Modifier
                .weight(5f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            strokeCap = StrokeCap.Round, drawStopIndicator = {}
        )

        Spacer(Modifier.width(8.dp))
    }
}
