package com.tanujn45.pokedex.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tanujn45.pokedex.R
import com.tanujn45.pokedex.models.MockPokemon
import com.tanujn45.pokedex.models.PokemonType
import com.tanujn45.pokedex.models.mockPokemonList
import com.tanujn45.pokedex.ui.components.TypeBadge
import com.tanujn45.pokedex.ui.theme.PokeDexTheme

@Composable
fun SearchScreen(modifier: Modifier) {
    var searchText by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            maxLines = 1,
            label = { Text("Search Pokemon") },
            leadingIcon = {
                Image(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = {
                        searchText = ""
                    }) {
                        Image(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear text"
                        )
                    }
                }
            },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
        SearchList(pokemonList = mockPokemonList, modifier = Modifier)
    }
}

@Composable
fun SearchList(
    modifier: Modifier = Modifier,
    pokemonList: List<MockPokemon>,
) {
    LazyColumn(modifier = modifier) {
        items(pokemonList) { mockPokemon ->
            SearchListItem(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                mockPokemon = mockPokemon,
                onClick = {}
            )
        }
    }
}

@Composable
fun SearchListItem(
    modifier: Modifier = Modifier, mockPokemon: MockPokemon, onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }, elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = mockPokemon.imageResourceId),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .padding(end = 16.dp)
            )

            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "#${mockPokemon.id}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = mockPokemon.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.size(4.dp))
                Row {
                    mockPokemon.types.forEach { type ->
                        TypeBadge(
                            type = type,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    PokeDexTheme {
        SearchScreen(modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun SearchListPreview() {
    PokeDexTheme {
        SearchList(
            pokemonList = mockPokemonList
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchListItemPreview() {
    PokeDexTheme {
        SearchListItem(
            mockPokemon = MockPokemon(
                id = 25,
                name = "Pikachu",
                types = listOf(PokemonType.Electric),
                imageResourceId = R.drawable.pikachu_25,
            ),
            onClick = {})
    }
}