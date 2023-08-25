package za.co.retrorabbit.gameofthrones.extensions

fun String?.getId(): Int? = this?.split("/")?.last()?.toIntOrNull()

fun String?.ifNullOrBank(defaultValue: () -> String) : String =
    if (this.isNullOrBlank()) defaultValue() else this