package com.tanujn45.pokedex.ui.screens.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.bulbasaur
import java.util.Locale


@Composable
fun PokemonDetailBasicInfo(modifier: Modifier = Modifier, pokemon: PokemonDetail) {
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()
        ) {
            PokemonInfoChip(
                name = "Height", value = "${pokemon.height} cm", modifier = Modifier.weight(1f)
            )
            PokemonInfoChip(
                name = "Weight", value = "${pokemon.weight} kg", modifier = Modifier.weight(1f)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()
        ) {
            PokemonInfoChip(
                name = "Base Exp",
                value = "${pokemon.baseExperience}",
                modifier = Modifier.weight(1f)
            )
            PokemonInfoChip(
                name = "Species", value = pokemon.species.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }, modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonDetailBasicInfoPreview() {
    PokemonDetailBasicInfo(pokemon = bulbasaur)
}

@Composable
fun PokemonInfoChip(
    modifier: Modifier = Modifier, name: String, value: String, icon: ImageVector? = null,
) {
    Column(modifier = modifier) {
        Row {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null)
            }
            Text(
                text = name.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 8.dp, vertical = 10.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 128)
@Composable
fun PokemonInfoChipPreview() {
    PokemonInfoChip(name = "Height", value = "7 cm", modifier = Modifier.padding(16.dp))
}

