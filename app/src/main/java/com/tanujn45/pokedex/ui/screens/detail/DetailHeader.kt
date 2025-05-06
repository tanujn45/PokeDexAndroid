package com.tanujn45.pokedex.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tanujn45.pokedex.R
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonType
import com.tanujn45.pokedex.models.TypeSlot
import com.tanujn45.pokedex.models.bulbasaur
import com.tanujn45.pokedex.models.getSpriteUrl
import com.tanujn45.pokedex.ui.components.TypeBadge
import java.util.Locale

@Composable
fun PokemonDetailHeader(
    modifier: Modifier = Modifier,
    pokemon: PokemonDetail,
    onBack: () -> Unit = {},
    isPreview: Boolean = false
) {
    val pokemonType = PokemonType.fromString(pokemon.typeSlots.first().type.name) ?: return
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large), contentAlignment = Alignment.TopCenter
    ) {
        // Put the icon on the top left corner
        Box(modifier = Modifier.align(Alignment.TopStart)) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }

        Image(
            painter = painterResource(pokemonType.iconRes),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            alpha = 0.5f,
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier
                .padding(top = 64.dp, bottom = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PokemonImage(
                imageUrl = pokemon.getSpriteUrl(),
                isPreview = isPreview,
                modifier = Modifier.size(200.dp)
            )
            Text(
                pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun PokemonImage(modifier: Modifier = Modifier, imageUrl: String = "", isPreview: Boolean = false) {
    if (isPreview) {
        Image(
            painter = painterResource(id = R.drawable.bulbasaur_1),
            contentDescription = "pokemon image",
            modifier = modifier
        )
    } else {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(imageUrl)
                .crossfade(true).build(), contentDescription = "pokemon image", modifier = modifier
        )
    }
}

@Composable
fun PokemonTypeBadges(types: List<TypeSlot>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        types.mapNotNull { slot -> PokemonType.fromString(slot.type.name) }.forEach { type ->
            TypeBadge(type = type, modifier = Modifier.padding(horizontal = 4.dp))
        }
    }
}

@Preview(showBackground = true, heightDp = 500)
@Composable
fun PokemonDetailHeaderPreview() {
    PokemonDetailHeader(pokemon = bulbasaur, isPreview = true)
}