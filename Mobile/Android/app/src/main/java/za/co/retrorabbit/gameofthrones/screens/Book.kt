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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.co.retrorabbit.gameofthrones.composables.IconTile
import za.co.retrorabbit.gameofthrones.composables.LoadingAnimation
import za.co.retrorabbit.gameofthrones.composables.MultilineLabel
import za.co.retrorabbit.gameofthrones.extensions.getId
import za.co.retrorabbit.gameofthrones.models.Book
import za.co.retrorabbit.gameofthrones.models.BookViewModel
import za.co.retrorabbit.gameofthrones.models.CharactersViewModel
import za.co.retrorabbit.gameofthrones.models.DataViewModel
import za.co.retrorabbit.gameofthrones.models.Person
import za.co.retrorabbit.gameofthrones.router.route
import za.co.retrorabbit.gameofthrones.services.RetrofitClient
import za.co.retrorabbit.gameofthrones.services.getData


private val bookData = BookViewModel()
private val charactersData = CharactersViewModel()
private val charactersPOVData = CharactersViewModel()

private fun getCharacters(ids: List<Int>) {

    ids.forEach { id ->
        getData(
            RetrofitClient.instance.getGameOfThronesService().getCharacter(id),
            charactersData,
            Person()
        )
    }
}

private fun getCharactersPOV(ids: List<Int>) {

    ids.forEach { id ->
        val call = RetrofitClient.instance.getGameOfThronesService().getCharacter(id)
        call?.enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                val data: Person? = response.body()

                data?.let { charactersPOVData.onDataAdd(it) }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
            }
        })
    }
}

private fun getBook(id: Int) {

    if (bookData.data.value?.url.isNullOrBlank()) {
        val call = RetrofitClient.instance.getGameOfThronesService().getBooks(id)
        call?.enqueue(object : Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                val data: Book = response.body() ?: Book()

                bookData.onDataChange(data)

                if (data.characters.isNotEmpty()) {
                    getCharacters(data.characters.filter { it.isNotBlank() }
                        .mapNotNull { it.getId() })

                }

                if (data.povCharacters.isNotEmpty()) {
                    getCharactersPOV(data.povCharacters.filter { it.isNotBlank() }
                        .mapNotNull { it.getId() })
                }
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
            }
        })
    }
}

@Composable
fun BookScaffold(id: Int, navController: NavHostController) {
    getBook(id)
    val book by bookData.data.observeAsState(Book())
    val characters by charactersData.data.observeAsState(emptyList())
    val charactersPOV by charactersPOVData.data.observeAsState(emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = book.name ?: "No name",
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
                        bookData.clear()
                        charactersData.clear()
                        charactersPOVData.clear()

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
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                            .height(220.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        rows = GridCells.Fixed(2)
                    ) {
                        if (characters.isNotEmpty()) {
                            items(characters.size) {
                                Box(
                                    modifier = Modifier
                                        .width(150.dp)
                                ) {
                                    IconTile(
                                        title = characters[it].name ?: "",
                                        icon = Icons.Rounded.Face,
                                        click = {
                                            route(navController, characters[it].url)
                                        })
                                }
                            }
                        } else {
                            items(bookData.data.value?.characters?.size ?: 0) {
                                Box(
                                    modifier = Modifier
                                        .width(150.dp)
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
                            .height(220.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        rows = GridCells.Fixed(2)
                    ) {
                        if (charactersPOV.isNotEmpty()) {
                            items(charactersPOV.size) {
                                Box(
                                    modifier = Modifier
                                        .width(150.dp)
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
                                Box(
                                    modifier = Modifier
                                        .width(150.dp)
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