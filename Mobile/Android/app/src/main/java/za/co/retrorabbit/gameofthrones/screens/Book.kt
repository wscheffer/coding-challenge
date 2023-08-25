@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package za.co.retrorabbit.gameofthrones.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.co.retrorabbit.gameofthrones.composables.GRID_HEIGHT
import za.co.retrorabbit.gameofthrones.composables.GRID_SPACING
import za.co.retrorabbit.gameofthrones.composables.IconTile
import za.co.retrorabbit.gameofthrones.composables.LoadingAnimation
import za.co.retrorabbit.gameofthrones.composables.MultilineLabel
import za.co.retrorabbit.gameofthrones.composables.SCAFFOLD_PADDING
import za.co.retrorabbit.gameofthrones.composables.gridCellWidth
import za.co.retrorabbit.gameofthrones.extensions.getId
import za.co.retrorabbit.gameofthrones.extensions.ifNullOrBank
import za.co.retrorabbit.gameofthrones.models.Book
import za.co.retrorabbit.gameofthrones.models.BookViewModel
import za.co.retrorabbit.gameofthrones.models.CharactersViewModel
import za.co.retrorabbit.gameofthrones.models.Person
import za.co.retrorabbit.gameofthrones.router.route
import za.co.retrorabbit.gameofthrones.services.RetrofitClient
import za.co.retrorabbit.gameofthrones.services.getData


private val bookData = BookViewModel()
private val charactersData = CharactersViewModel()
private val charactersPOVData = CharactersViewModel()

private val activeCalls = mutableMapOf<Int, Call<Person>>()
private val activePOVCalls = mutableMapOf<Int, Call<Person>>()

private fun getCharacters(ids: List<Int>) {

    ids.forEach { id ->
        getData(
            RetrofitClient.instance.getGameOfThronesService().getCharacter(id)?.also {
                activeCalls[id] = it;
            },
            charactersData,
            Person()
        ) {
            activeCalls.remove(id)
        }
    }
}

private fun getCharactersPOV(ids: List<Int>) {

    ids.forEach { id ->
        getData(
            RetrofitClient.instance.getGameOfThronesService().getCharacter(id)?.also {
                activePOVCalls[id] = it;
            },
            charactersPOVData,
            Person()
        ) {
            activePOVCalls.remove(id)
        }
    }
}

private fun getBook(id: Int) {

    val call = RetrofitClient.instance.getGameOfThronesService().getBooks(id)
    call?.enqueue(object : Callback<Book> {
        override fun onResponse(call: Call<Book>, response: Response<Book>) {
            val data: Book = response.body() ?: Book()

            bookData.onDataChange(data)

//            if (data.characters.isNotEmpty()) {
                getCharacters(data.characters.filter { it.isNotBlank() }
                    .mapNotNull { it.getId() })

//            }

//            if (data.povCharacters.isNotEmpty()) {
                getCharactersPOV(data.povCharacters.filter { it.isNotBlank() }
                    .mapNotNull { it.getId() })
//            }
        }

        override fun onFailure(call: Call<Book>, t: Throwable) {
        }
    })
}

@Composable
fun BookScaffold(id: Int, navController: NavHostController) {

    val book by bookData.data.observeAsState(Book())
    val characters by charactersData.data.observeAsState(emptyList())
    val charactersPOV by charactersPOVData.data.observeAsState(emptyList())

    LaunchedEffect(id) {
        getBook(id)
    }

    DisposableEffect(bookData) {
        onDispose {
            activeCalls.forEach {
                it.value.cancel()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(horizontal = SCAFFOLD_PADDING),
                        text = book.name ?: "Loading ...",
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
    ) { padding ->
        if (book.url?.isEmpty() != false) {
            LoadingAnimation()
        } else {

            LazyVerticalGrid(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(GRID_SPACING),
                horizontalArrangement = Arrangement.spacedBy(GRID_SPACING),
                columns = GridCells.Fixed(2)
            ) {
                item(span = { GridItemSpan(2) }) {
                    MultilineLabel(
                        title = "Authors",
                        body = book.authors
                    )
                }
                item {
                    MultilineLabel(
                        title = "ISBN",
                        body = book.isbn
                    )
                }
                item {
                    MultilineLabel(
                        title = "Number of pages",
                        body = "${book.numberOfPages}"
                    )
                }
                item {
                    MultilineLabel(
                        title = "Publisher",
                        body = book.publisher
                    )
                }
                item {
                    MultilineLabel(
                        title = "Country",
                        body = book.country
                    )
                }
                item {
                    MultilineLabel(
                        title = "Media type",
                        body = book.mediaType
                    )
                }
                item {
                    MultilineLabel(
                        title = "Released",
                        body = book.releasedFormatted
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Characters",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    LazyHorizontalGrid(
                        modifier = Modifier
                            .height(GRID_HEIGHT)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(GRID_SPACING),
                        horizontalArrangement = Arrangement.spacedBy(GRID_SPACING),
                        rows = GridCells.Fixed(1)
                    ) {
                        if (characters.isNotEmpty()) {
                            items(characters.takeLast(10).size) {
                                Box(
                                    modifier = Modifier
                                        .width(gridCellWidth())
                                ) {
                                    IconTile(
                                        title = characters[it].name.ifNullOrBank { "No Name" },
                                        icon = Icons.Rounded.Face,
                                        click = {
                                            route(navController, characters[it].url)
                                        })
                                }
                            }
                        } else {
                            items(bookData.data.value?.characters?.size ?: 0) {
                                Card(
                                    modifier = Modifier
                                        .width(gridCellWidth())
                                ) {
                                    LoadingAnimation()
                                }
                            }
                        }
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "POV-chapters",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    LazyHorizontalGrid(
                        modifier = Modifier
                            .height(GRID_HEIGHT)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(GRID_SPACING),
                        horizontalArrangement = Arrangement.spacedBy(GRID_SPACING),
                        rows = GridCells.Fixed(1)
                    ) {
                        if (charactersPOV.isNotEmpty()) {
                            items(charactersPOV.size) {
                                Box(
                                    modifier = Modifier
                                        .width(gridCellWidth())
                                ) {
                                    IconTile(
                                        title = charactersPOV[it].name ?: "",
                                        icon = Icons.Rounded.Face,
                                        click = {
                                            route(navController, charactersPOV[it].url)
                                        })
                                }
                            }
                        } else {
                            items(bookData.data.value?.povCharacters?.size ?: 0) {
                                Card(
                                    modifier = Modifier
                                        .width(gridCellWidth())
                                ) {
                                    LoadingAnimation()
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}