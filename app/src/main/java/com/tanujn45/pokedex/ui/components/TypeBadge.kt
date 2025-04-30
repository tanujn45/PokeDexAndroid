package com.tanujn45.pokedex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tanujn45.pokedex.model.PokemonType

@Composable
fun TypeBadge(type: PokemonType, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraSmall)
                .background(type.color)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = type.displayName,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TypeBadgePreview() {
    TypeBadge(
        type = PokemonType.Electric,
        modifier = Modifier,
    )
}