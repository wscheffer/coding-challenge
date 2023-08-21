@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material.icons.rounded.Face
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
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.co.retrorabbit.gameofthrones.composables.IconTile
import za.co.retrorabbit.gameofthrones.composables.MultilineLabel
import za.co.retrorabbit.gameofthrones.extensions.getId
import za.co.retrorabbit.gameofthrones.models.House
import za.co.retrorabbit.gameofthrones.models.Person
import za.co.retrorabbit.gameofthrones.router.route
import za.co.retrorabbit.gameofthrones.services.RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseDetailScaffold(housesData: House?, navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
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
                        text = housesData?.name
                            ?: "Game of Thrones",
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
        DetailsScreen(padding, housesData, navController)
    }
}

val currentLordData = CharacterViewModel()
val founderData = CharacterViewModel()
val heirData = CharacterViewModel()
val membersData = CharactersViewModel()

private fun getCharacter(id: Int?) {

    val call = id?.let { RetrofitClient.instance.getGameOfThronesService().getCharacter(it) }
    call?.enqueue(object : Callback<Person> {
        override fun onResponse(call: Call<Person>, response: Response<Person>) {
            val person: Person = response.body() ?: Person()

            currentLordData.onDataChange(person)
        }

        override fun onFailure(call: Call<Person>, t: Throwable) {
        }
    })
}

private fun getFounder(id: Int?) {

    val call = id?.let { RetrofitClient.instance.getGameOfThronesService().getCharacter(it) }
    call?.enqueue(object : Callback<Person> {
        override fun onResponse(call: Call<Person>, response: Response<Person>) {
            val person: Person = response.body() ?: Person()

            founderData.onDataChange(person)
        }

        override fun onFailure(call: Call<Person>, t: Throwable) {
        }
    })
}

private fun getHeir(id: Int?) {

    val call = id?.let { RetrofitClient.instance.getGameOfThronesService().getCharacter(it) }
    call?.enqueue(object : Callback<Person> {
        override fun onResponse(call: Call<Person>, response: Response<Person>) {
            val person: Person = response.body() ?: Person()

            heirData.onDataChange(person)
        }

        override fun onFailure(call: Call<Person>, t: Throwable) {
        }
    })
}

private fun getMembers(ids: List<Int>) {

    ids.forEach { id ->
        val call = RetrofitClient.instance.getGameOfThronesService().getCharacter(id)
        call?.enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                val data: Person? = response.body()

                data?.let { membersData.onDataAdd(it) }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
            }
        })
    }
}

@Composable
fun DetailsScreen(
    padding: PaddingValues,
    housesData: House?,
    navController: NavHostController
) {
    housesData?.let {
        getCharacter(housesData.currentLord.getId())
        getFounder(housesData.founder.getId())
        getHeir(housesData.heir.getId())
        getMembers(housesData.swornMembers.filter { it.isNotBlank() }.mapNotNull { it.getId() })
    }
    val currentLord by currentLordData.person.observeAsState(Person())
    val founder by founderData.person.observeAsState(Person())
    val heir by heirData.person.observeAsState(Person())
    val members by membersData.characters.observeAsState(emptyList())

    housesData?.let {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            columns = GridCells.Fixed(2)
        ) {
            item(span = { GridItemSpan(2) }) {
                MultilineLabel(
                    title = "Region",
                    body = it.region,
                )
            }
            item(span = { GridItemSpan(2) }) {
                MultilineLabel(
                    title = "Coat of arms",
                    body = it.coatOfArms,
                )
            }
            item(span = { GridItemSpan(2) }) {
                MultilineLabel(
                    title = "Words",
                    body = it.words,
                )
            }
            if (it.titles.isNotEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    MultilineLabel(
                        title = "Titles",
                        body = it.titles.joinToString { it }
                    )
                }
            }
            if (it.seats.isNotEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    MultilineLabel(
                        title = "Seats",
                        body = it.seats.joinToString { it }
                    )
                }
            }
            if (members.isNotEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Sworn Members",
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
                        items(members.size) {
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                            ) {
                                IconTile(
                                    title = members[it].name ?: "",
                                    icon = Icons.Rounded.Face,
                                    click = {
                                        route(navController, members[it].url)
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
            item(span = { GridItemSpan(2) }) {
                LazyHorizontalGrid(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    rows = GridCells.Fixed(1)
                ) {
                    currentLord.url?.let { url ->
                        if (url.isNotBlank()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .width(150.dp)
                                ) {
                                    IconTile(
                                        title = "Current Lord\n(${currentLord.name})",
                                        icon = Icons.Filled.Face,
                                        click = {
                                            route(navController, url)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    heir.url?.let { url ->
                        if (url.isNotBlank()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .width(150.dp)
                                ) {
                                    IconTile(
                                        title = "Heir\n(${heir.name})",
                                        icon = Icons.Filled.Face,
                                        click = {
                                            route(navController, url)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    founder.url?.let { url ->
                        item {
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                            ) {
                                IconTile(
                                    title = "Founder\n(${founder.name})",
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
        }
    }
}