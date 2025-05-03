package com.tanujn45.pokedex.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tanujn45.pokedex.models.MoveSlot


@Composable
fun PokemonMoves(
    moves: List<MoveSlot>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        // Add gutter between columns
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(moves) { move ->
            PokemonMoveItem(move)
        }
    }
}

@Composable
private fun PokemonMoveItem(
    move: MoveSlot,
    modifier: Modifier = Modifier
) {
    // Only use the first learn detail
    val detail = move.versionGroupDetails.firstOrNull()
    val learnLabel = when (detail?.moveLearnMethod?.name) {
        "level-up" -> "Lv ${detail.levelLearnedAt}"
        "machine" -> "TM"
        "tutor" -> "Tutor"
        else -> detail?.moveLearnMethod?.name
            ?.replace('-', ' ')
            ?.replaceFirstChar { it.uppercase() }
            ?: ""
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Learn-method label
            Text(
                text = learnLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(48.dp)
            )

            // Move name
            Text(
                text = move.move.name
                    .replace('-', ' ')
                    .replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
