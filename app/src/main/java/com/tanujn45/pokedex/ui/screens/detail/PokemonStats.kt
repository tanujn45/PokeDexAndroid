package com.tanujn45.pokedex.ui.screens.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonType

@Composable
fun PokemonStats(pokemon: PokemonDetail, type: PokemonType?) {
    pokemon.statSlots.forEach { slot ->
        PokemonStatItem(
            statName = slot.stat.name,
            statValue = slot.baseStat,
            modifier = Modifier.padding(vertical = 4.dp),
            color = type?.color ?: MaterialTheme.colorScheme.primary
        )
    }
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
