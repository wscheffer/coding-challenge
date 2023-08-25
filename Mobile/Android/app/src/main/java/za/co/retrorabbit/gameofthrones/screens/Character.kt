@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
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
import za.co.retrorabbit.gameofthrones.models.Book
import za.co.retrorabbit.gameofthrones.models.BooksViewModel
import za.co.retrorabbit.gameofthrones.models.CharacterViewModel
import za.co.retrorabbit.gameofthrones.models.House
import za.co.retrorabbit.gameofthrones.models.HousesViewModel
import za.co.retrorabbit.gameofthrones.models.Person
import za.co.retrorabbit.gameofthrones.router.route
import za.co.retrorabbit.gameofthrones.services.RetrofitClient
import za.co.retrorabbit.gameofthrones.services.getData

private val personData = CharacterViewModel()
private val spouseData = CharacterViewModel()
private val motherData = CharacterViewModel()
private val fatherData = CharacterViewModel()
private val booksData = BooksViewModel()
private val allegiancesData = HousesViewModel()

fun getCharacter(id: Int) {

    val call = RetrofitClient.instance.getGameOfThronesService().getCharacter(id)
    call?.enqueue(object : Callback<Person> {
        override fun onResponse(call: Call<Person>, response: Response<Person>) {
            val person: Person = response.body() ?: Person()

            personData.onDataChange(person)

            getBooks(person.books.mapNotNull { it.getId() })

            if (!person.spouse.isNullOrBlank()) {
                person.spouse?.let { getSpouse(it.getId()) }
            }

            if (!person.mother.isNullOrBlank()) {
                person.mother?.let { getMother(it.getId()) }
            }

            if (!person.father.isNullOrBlank()) {
                person.father?.let { getFather(it.getId()) }
            }

            if (person.allegiances.isNotEmpty()) {
                getAllegiances(person.allegiances.mapNotNull { it.getId() })
            }
        }

        override fun onFailure(call: Call<Person>, t: Throwable) {
        }
    })
}

private fun getMother(id: Int?) {

    val call = id?.let { RetrofitClient.instance.getGameOfThronesService().getCharacter(it) }
    call?.enqueue(object : Callback<Person> {
        override fun onResponse(call: Call<Person>, response: Response<Person>) {
            val person: Person = response.body() ?: Person()

            motherData.onDataChange(person)
        }

        override fun onFailure(call: Call<Person>, t: Throwable) {
        }
    })
}

private fun getFather(id: Int?) {

    val call = id?.let { RetrofitClient.instance.getGameOfThronesService().getCharacter(it) }
    call?.enqueue(object : Callback<Person> {
        override fun onResponse(call: Call<Person>, response: Response<Person>) {
            val person: Person = response.body() ?: Person()

            fatherData.onDataChange(person)
        }

        override fun onFailure(call: Call<Person>, t: Throwable) {
        }
    })
}

private fun getSpouse(id: Int?) {

    val call = id?.let { RetrofitClient.instance.getGameOfThronesService().getCharacter(it) }
    call?.enqueue(object : Callback<Person> {
        override fun onResponse(call: Call<Person>, response: Response<Person>) {
            val person: Person = response.body() ?: Person()

            spouseData.onDataChange(person)
        }

        override fun onFailure(call: Call<Person>, t: Throwable) {
        }
    })
}

private fun getAllegiances(ids: List<Int>) {

    allegiancesData.clear()

    ids.forEach { id ->

        getData(
            RetrofitClient.instance.getGameOfThronesService().getHouse(id),
            allegiancesData,
            House()
        )
    }
}

private fun getBooks(ids: List<Int>) {

    booksData.clear()

    ids.forEach { id ->

        getData(
            RetrofitClient.instance.getGameOfThronesService().getBooks(id),
            booksData,
            Book()
        )
    }
}

@Composable
fun CharacterScaffold(id: Int, navController: NavHostController) {


    LaunchedEffect(id) {
        getCharacter(id)
    }

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val person by personData.data.observeAsState(Person())
    val books by booksData.data.observeAsState(emptyList())
    val allegiances by allegiancesData.data.observeAsState(emptyList())
    val spouse by spouseData.data.observeAsState(Person())
    val father by motherData.data.observeAsState(Person())
    val mother by fatherData.data.observeAsState(Person())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(horizontal = SCAFFOLD_PADDING),
                        text = person.name ?: "",
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
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { padding ->
        if (person.url?.isEmpty() != false) {
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
                        title = "Titles",
                        body = person.titles.joinToString { it }
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    MultilineLabel(
                        title = "Aliases",
                        body = person.aliases.joinToString { it }
                    )
                }
                item {
                    MultilineLabel(
                        title = "Gender",
                        body = person.gender
                    )
                }
                item {
                    MultilineLabel(
                        title = "Culture",
                        body = person.culture
                    )
                }
                item {
                    MultilineLabel(
                        title = "Born",
                        body = person.born
                    )
                }
                item {
                    MultilineLabel(
                        title = "Died",
                        body = person.died
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Allegiances",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        modifier = Modifier
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
                        if (allegiances.isNotEmpty()) {
                            items(allegiances.size) {
                                Box(
                                    modifier = Modifier
                                        .width(gridCellWidth())
                                ) {
                                    IconTile(
                                        title = allegiances[it].name ?: "",
                                        icon = Icons.Rounded.Create
                                    )
                                }
                            }
                        } else {
                            item {
                                Card(
                                    modifier = Modifier
                                        .height(GRID_HEIGHT)
                                        .width(gridCellWidth())
                                ) {
                                    IconTile(
                                        title = "No allegiances",
                                        icon = Icons.Filled.Face,
                                    )
                                }
                            }
                        }
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Books",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
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
                        if (books.isNotEmpty()) {
                            items(books.size) {
                                Box(
                                    modifier = Modifier
                                        .width(gridCellWidth())
                                ) {
                                    IconTile(title = books[it].name ?: "",
                                        icon = Icons.Rounded.AccountBox,
                                        click = {
                                            route(navController, books[it].url)
                                        })
                                }
                            }
                        } else if ((booksData.data.value?.size ?: 0) > 0) {
                            items(booksData.data.value?.size ?: 0) {
                                Box(
                                    modifier = Modifier
                                        .height(GRID_HEIGHT)
                                        .width(gridCellWidth())
                                ) {
                                    LoadingAnimation()
                                }
                            }
                        } else {
                            item {
                                Card(
                                    modifier = Modifier
                                        .height(GRID_HEIGHT)
                                        .width(gridCellWidth())
                                ) {
                                    IconTile(
                                        title = "No books",
                                        icon = Icons.Filled.Face,
                                    )
                                }
                            }
                        }
                    }
                }
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Other",
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
                    )
                    {
                        if (!spouse.url.isNullOrBlank()) {
                            spouse.url?.let { url ->
                                if (url.isNotBlank()) {
                                    item {
                                        Box {
                                            IconTile(
                                                title = "Spouse\n(${spouse.name})",
                                                icon = Icons.Filled.Face,
                                                click = {
                                                    route(navController, url)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            item {
                                Card(
                                    modifier = Modifier
                                        .width(gridCellWidth())
                                ) {
                                    IconTile(
                                        title = "No Spouse",
                                        icon = Icons.Filled.Face,
                                    )
                                }
                            }
                        }
                        if (!father.url.isNullOrBlank()) {
                            item {
                                Box {
                                    IconTile(
                                        title = "Father\n(${father.name})",
                                        icon = Icons.Filled.Face,
                                        click = {
                                            route(navController, father.url)
                                        }
                                    )
                                }
                            }
                        } else {
                            item {
                                Box(
                                    modifier = Modifier
                                        .width(gridCellWidth())
                                ) {
                                    IconTile(
                                        title = "No Father",
                                        icon = Icons.Filled.Face,
                                    )
                                }
                            }
                        }
                        if (!mother.url.isNullOrBlank()) {
                            item {
                                Box {
                                    IconTile(
                                        title = "Mother\n(${mother.name})",
                                        icon = Icons.Filled.Face,
                                        click = {
                                            route(navController, mother.url)
                                        }
                                    )
                                }
                            }
                        } else {
                            item {
                                Box(
                                    modifier = Modifier
                                        .width(gridCellWidth())
                                ) {
                                    IconTile(
                                        title = "No Mother",
                                        icon = Icons.Filled.Face,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
