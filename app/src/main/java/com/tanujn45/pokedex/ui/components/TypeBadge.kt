package com.tanujn45.pokedex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tanujn45.pokedex.models.PokemonType

@Composable
fun TypeBadge(
    modifier: Modifier = Modifier,
    type: PokemonType,
    hideText: Boolean = false
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(type.color)
            .padding(end = if (!hideText) 8.dp else 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = type.iconRes),
            contentDescription = "${type.displayName} icon",
            modifier = Modifier.size(28.dp)
        )
        if (!hideText) {
            Text(
                text = type.displayName,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
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

@Preview(showBackground = true)
@Composable
fun TypeBadgePreviewWithoutText() {
    TypeBadge(
        type = PokemonType.Electric,
        hideText = true,
        modifier = Modifier,
    )
}
