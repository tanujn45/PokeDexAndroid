package com.tanujn45.pokedex.ui.screens.detail

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonSpecies
import com.tanujn45.pokedex.models.PokemonType
import com.tanujn45.pokedex.models.bulbasaur
import com.tanujn45.pokedex.models.bulbasaurSpecies
import com.tanujn45.pokedex.models.femaleFraction
import com.tanujn45.pokedex.models.getEnglishFlavorText
import com.tanujn45.pokedex.models.getEnglishGenus
import java.util.Locale
import kotlin.math.roundToInt


@Composable
fun PokemonOverview(
    modifier: Modifier = Modifier, pokemon: PokemonDetail, species: PokemonSpecies
) {
    val type = PokemonType.fromString(pokemon.typeSlots.first().type.name) ?: return
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FlavorTextBlock(text = species.getEnglishFlavorText())

        Spacer(Modifier.height(8.dp))
        CryButton(cryUrl = pokemon.cries.latest, name = pokemon.name, color = type.color)

        Spacer(modifier = Modifier.height(8.dp))
        BasicInfoBlock(pokemon = pokemon, species = species)

        Spacer(modifier = Modifier.height(8.dp))
        GenderRatioLine(femalePct = species.femaleFraction())
    }
}

@Composable
fun FlavorTextBlock(text: String?, modifier: Modifier = Modifier) {
    text?.let {
        Text(
            text = "\"${it.trim()}\"",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier
        )
    }
}

@Composable
fun BasicInfoBlock(pokemon: PokemonDetail, species: PokemonSpecies, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
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
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()
        ) {
            PokemonInfoChip(
                name = "Base Exp",
                value = "${pokemon.baseExperience}",
                modifier = Modifier.weight(1f)
            )
            PokemonInfoChip(
                name = "Genus", value = species.getEnglishGenus().replaceFirstChar {
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
fun PokemonOverviewPreview() {
    PokemonOverview(pokemon = bulbasaur, species = bulbasaurSpecies)
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

@Composable
fun GenderRatioLine(femalePct: Float?, modifier: Modifier = Modifier) {
    if (femalePct == null) {
        Text("Genderless", style = MaterialTheme.typography.bodySmall)
    } else {
        val malePct = 1f - femalePct
        Log.d("GenderRatioLine", "femalePct: $femalePct, malePct: $malePct")
        Column(modifier = modifier) {
            Text(
                text = "Gender Ratio".uppercase(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                // Female segment (pink)
                if (femalePct > 0) {
                    Box(
                        Modifier
                            .weight(femalePct)
                            .fillMaxHeight()
                            .background(Color(0xFFE5A9B8))
                    )
                }
                // Male segment (blue)
                if (malePct > 0) {
                    Box(
                        Modifier
                            .weight(malePct)
                            .fillMaxHeight()
                            .background(Color(0xFF7EA6E0))
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Female, contentDescription = "Female",
//                    tint = Color(0xFFE91E63),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${(femalePct * 100).roundToInt()}%",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Male, contentDescription = "Male",
//                    tint = Color(0xFF2196F3),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${(malePct * 100).roundToInt()}%",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun CryButton(
    cryUrl: String?,
    name: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primaryContainer
) {
    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(cryUrl) {
        onDispose {
            player?.release()
            player = null
        }
    }

    Button(
        onClick = {
            cryUrl?.let { url ->
                // Release previous player if exists
                player?.release()

                try {
                    // Create new player
                    player = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA).build()
                        )
                        setDataSource(url)
                        prepareAsync()

                        setOnPreparedListener {
                            it.start()
                            isPlaying = true
                        }

                        setOnCompletionListener {
                            isPlaying = false
                            it.release()
                            player = null
                        }

                        setOnErrorListener { mp, _, _ ->
                            isPlaying = false
                            mp.release()
                            player = null
                            true
                        }
                    }
                } catch (e: Exception) {
                    player = null
                }
            }
        },
        modifier = modifier.height(48.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.7f),
            contentColor = Color.Black,
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
            contentDescription = "Play cry",
            tint = if (isPlaying) color
            else Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "Play ${name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}'s Cry",
            style = MaterialTheme.typography.labelLarge
        )
    }
}
