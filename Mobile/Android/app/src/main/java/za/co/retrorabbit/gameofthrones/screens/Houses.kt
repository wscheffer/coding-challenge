@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package za.co.retrorabbit.gameofthrones.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.co.retrorabbit.gameofthrones.composables.LoadingAnimation
import za.co.retrorabbit.gameofthrones.models.House
import za.co.retrorabbit.gameofthrones.models.HousesViewModel
import za.co.retrorabbit.gameofthrones.router.route
import za.co.retrorabbit.gameofthrones.services.RetrofitClient


val housesData = HousesViewModel()
private val housesGroupedData = HousesGroupedViewModel()

private class HousesGroupedViewModel : ViewModel() {
    private val _data = MutableLiveData(mapOf<Char, List<House>>())
    var data: LiveData<Map<Char, List<House>>> = _data

    fun onDataChange(data: Map<Char, List<House>>) {
        _data.value = data
    }

    fun clear() {
        _data.value = emptyMap()
    }
}

private fun getDataGrouped(endpoint: Call<List<House>>?, model: HousesGroupedViewModel) {

    endpoint?.enqueue(object : Callback<List<House>> {
        override fun onResponse(call: Call<List<House>>, response: Response<List<House>>) {
            val data: List<House> = response.body() ?: emptyList()

            housesData.onDataChange(data)

            model.clear()
            model.onDataChange(data.filterNot { it.name.isNullOrBlank() }
                .groupBy { it.name!!.elementAt(6) }.entries.associate { it.key to it.value }
            )
        }

        override fun onFailure(call: Call<List<House>>, t: Throwable) {
        }
    })
}

@Composable
fun HouseListScaffold(navController: NavHostController) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val grouped by housesGroupedData.data.observeAsState(mutableMapOf())

    if (housesData.data.value?.isEmpty() != false) {
        getDataGrouped(
            RetrofitClient.instance.getGameOfThronesService().getHouses(),
            housesGroupedData
        )
    }

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
        HousesList(grouped, navController, padding)
    }
}

@Composable
fun HousesList(
    grouped: Map<Char, List<House>>,
    navController: NavController,
    padding: PaddingValues,
) {
    if (grouped.isEmpty()) {
        LoadingAnimation()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = padding
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