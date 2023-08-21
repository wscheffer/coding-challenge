@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package za.co.retrorabbit.gameofthrones.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import za.co.retrorabbit.gameofthrones.models.Person
import za.co.retrorabbit.gameofthrones.router.route
import za.co.retrorabbit.gameofthrones.services.RetrofitClient

class CharacterViewModel : ViewModel() {
    private val _person = MutableLiveData(Person())
    var person: LiveData<Person> = _person

    fun onDataChange(data: Person) {
        _person.value = data
    }
}

class BooksViewModel : ViewModel() {
    private val _books = MutableLiveData(emptyList<Book>())
    var books: LiveData<List<Book>> = _books

    fun onDataAdd(data: Book) {
        _books.value = (_books.value)?.plus(data)
    }
}

class CharactersViewModel : ViewModel() {
    private val _characters = MutableLiveData(emptyList<Person>())
    var characters: LiveData<List<Person>> = _characters

    fun onDataAdd(data: Person) {
        _characters.value = (_characters.value)?.plus(data)
    }
}

val personData = CharacterViewModel()
val spouseData = CharacterViewModel()
val motherData = CharacterViewModel()
val fatherData = CharacterViewModel()
val booksData = BooksViewModel()
val allegiancesData = CharactersViewModel()

private fun getCharacter(id: Int) {

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

            if (person.allegiances.isEmpty()) {
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

    ids.forEach { id ->
        val call = RetrofitClient.instance.getGameOfThronesService().getCharacter(id)
        call?.enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                val data: Person? = response.body()

                data?.let { allegiancesData.onDataAdd(it) }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
            }
        })
    }
}

private fun getBooks(ids: List<Int>) {

    if(booksData.books.value?.isEmpty() == true) {
        ids.forEach { id ->
            val call = RetrofitClient.instance.getGameOfThronesService().getBooks(id)
            call?.enqueue(object : Callback<Book> {
                override fun onResponse(call: Call<Book>, response: Response<Book>) {
                    val data: Book? = response.body()

                    data?.let { booksData.onDataAdd(it) }
                }

                override fun onFailure(call: Call<Book>, t: Throwable) {
                }
            })
        }
    }
}

@Composable
fun CharacterScaffold(id: Int, navController: NavHostController) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    getCharacter(id)
    val person by personData.person.observeAsState(Person())
    val books by booksData.books.observeAsState(emptyList())
    val allegiances by allegiancesData.characters.observeAsState(emptyList())
    val spouse by spouseData.person.observeAsState(Person())
    val father by motherData.person.observeAsState(Person())
    val mother by fatherData.person.observeAsState(Person())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = "${person.name} (${person.url?.getId()})",
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                if (allegiances.isNotEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = "Allegiances",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .paddingFromBaseline(top = 40.dp, bottom = 8.dp)
                                .padding(horizontal = 16.dp)
                        )
                    }
                    items(allegiances.size) {
                        IconTile(title = allegiances[it].name ?: "", icon = Icons.Rounded.Create)
                    }
                }
                if (books.isNotEmpty()) {

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
                                .height(220.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            rows = GridCells.Fixed(2)
                        ) {
                            items(books.size) {
                                Box(
                                    modifier = Modifier
                                        .width(150.dp)
                                ) {
                                    IconTile(title = books[it].name ?: "",
                                        icon = Icons.Rounded.AccountBox,
                                        click = {
                                            route(navController, books[it].url)
                                        })
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
                }
            }
        }
    }
}
