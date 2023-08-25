@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package za.co.retrorabbit.gameofthrones.screens

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.co.retrorabbit.gameofthrones.composables.LoadingAnimation
import za.co.retrorabbit.gameofthrones.models.House
import za.co.retrorabbit.gameofthrones.models.HousesViewModel
import za.co.retrorabbit.gameofthrones.router.route
import za.co.retrorabbit.gameofthrones.services.RetrofitClient
import javax.inject.Inject

val housesData = HousesViewModel()

@HiltViewModel
class HousesGroupedViewModel @Inject constructor(): ViewModel() {
    private val _data = MutableLiveData(mapOf<Char, List<House>>())
    val data: LiveData<Map<Char, List<House>>> = _data

    fun onDataChange(data: Map<Char, List<House>>) {
        _data.value = data
    }
}

private fun getHouseDataGrouped(
    page: Int,
    model: HousesGroupedViewModel
) {
    var lastPage: Int = -1

    if (page != lastPage) {

        RetrofitClient.instance.getGameOfThronesService().getHouses(page)
            ?.enqueue(object : Callback<List<House>> {
                override fun onResponse(call: Call<List<House>>, response: Response<List<House>>) {
                    val data: List<House> = response.body() ?: emptyList()

                    if (lastPage == -1) {
                        val header = response.headers().get("link")?.split("; ", ",")

                        header?.indexOf("rel=\"last\"")?.let {
                            if (it != -1) {
                                header[it - 1].let { lastUrl ->
                                    Uri.parse(lastUrl).getQueryParameter("page")?.toInt()
                                        ?.let { lastPage = it }
                                }
                            }
                        }
                    }
                    housesData.onDataAdd(data)

                    housesData.data.value?.filterNot { it.name.isNullOrBlank() }
                        ?.groupBy { it.name!!.elementAt(6) }?.entries?.associate { it.key to it.value }
                        ?.let {
                            model.onDataChange(
                                it
                            )
                        }
                }

                override fun onFailure(call: Call<List<House>>, t: Throwable) {
                }
            })
    }
}

@Composable
fun HousesListScaffold(navController: NavHostController) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "GoT Houses",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = scrollBehavior
            )
        },
    ) { padding ->
        HousesList(navController, padding)
    }
}

fun LazyListState.isScrolledToEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

@Composable
fun HousesList(
    navController: NavController,
    padding: PaddingValues,
) {
    val viewModel = hiltViewModel<HousesGroupedViewModel>()
    val grouped by viewModel.data.observeAsState(mutableMapOf())

    var page by remember {
        mutableStateOf(1)
    }

    val scrollState = rememberLazyListState()

    // observer when reached end of list
    val endOfListReached by remember {
        derivedStateOf {
            scrollState.isScrolledToEnd()
        }
    }

    LaunchedEffect(page) {
        getHouseDataGrouped(
            page,
            viewModel
        )
    }

    // act when end of list reached
    LaunchedEffect(endOfListReached) {
        if (endOfListReached) {
            page += 1
        }
    }

    if (grouped.isEmpty()) {
        LoadingAnimation()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = padding,
        state = scrollState
    ) {
        grouped.forEach { (initial, houses) ->
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE1E1E1)),
                ) {
                    Text(
                        "$initial",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp,
                            ),
                    )
                }
            }

            items(houses.size) { house ->
                HouseItem(house = houses[house]) {
                    route(navController, houses[house].url)
                }
            }
        }
    }
}


@Composable
fun HouseItem(house: House, onClick: () -> Unit) {
    Column {
        ListItem(
            modifier = Modifier
                .clickable { onClick() }, headlineText = {
                Text(
                    text = house.name?.substring(6) ?: "",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }, supportingText = {
                Text(
                    text = house.region ?: "",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            }
        )
        Divider(color = Color(0xFFE1E1E1))
    }
}