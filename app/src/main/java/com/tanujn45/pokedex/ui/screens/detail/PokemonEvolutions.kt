package com.tanujn45.pokedex.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.South
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.tanujn45.pokedex.models.EvolutionNode
import com.tanujn45.pokedex.models.PokemonDetail
import com.tanujn45.pokedex.models.PokemonType
import com.tanujn45.pokedex.models.getSpriteUrl
import com.tanujn45.pokedex.ui.components.TypeBadge

@Composable
fun PokemonEvolutions(
    modifier: Modifier = Modifier,
    root: EvolutionNode?,
    onPokemonSelected: (String) -> Unit,
) {
    if (root == null) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No evolution data.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Evolution Chain",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        val levels = remember(root) {
            mutableMapOf<Int, MutableList<EvolutionNode>>().apply {
                fun traverse(node: EvolutionNode, depth: Int) {
                    getOrPut(depth) { mutableListOf() } += node
                    node.children.forEach { traverse(it, depth + 1) }
                }
                traverse(root, 0)
            }
        }

        levels.toSortedMap().forEach { (index, nodes) ->
            LazyRow(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                items(nodes) { node ->
                    Column {
                        EvolutionNodeGroup(node, onPokemonSelected = onPokemonSelected)
                    }
                }
            }
            if (index < levels.size - 1) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        imageVector = Icons.Default.South,
                        contentDescription = "Has children",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun EvolutionNodeGroup(
    node: EvolutionNode, modifier: Modifier = Modifier, onPokemonSelected: (String) -> Unit
) {
    Column(
        modifier = modifier.padding(end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(12.dp)
        ) {
            node.details.forEach { detail ->
                EvolutionNodeItem(detail = detail, onPokemonSelected = onPokemonSelected)
            }
        }
    }
}

@Composable
private fun EvolutionNodeItem(
    modifier: Modifier = Modifier, detail: PokemonDetail, onPokemonSelected: (String) -> Unit
) {
    Column(
        modifier = modifier.clickable { onPokemonSelected(detail.id.toString()) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            model = detail.getSpriteUrl(),
            contentDescription = detail.name,
            modifier = Modifier.size(128.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = detail.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            detail.typeSlots.forEach { type ->
                PokemonType.fromString(type.type.name)?.let {
                    TypeBadge(
                        type = it, hideText = true, modifier = Modifier.padding(horizontal = 2.dp)
                    )
                }
            }
        }
    }
}
