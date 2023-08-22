package za.co.retrorabbit.gameofthrones.extensions

fun String?.getId(): Int? = this?.split("/")?.last()?.toIntOrNull()
