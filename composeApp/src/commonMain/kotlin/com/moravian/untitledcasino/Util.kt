package com.moravian.untitledcasino

fun formatWithCommas(number: String): String {
    val result = StringBuilder()

    var count = 0
    for (i in number.length - 1 downTo 0) {
        result.append(number[i])
        count++

        if (count % 3 == 0 && i != 0) {
            result.append(',')
        }
    }

    return result.reverse().toString()
}

fun formatPrice(number: Int): String {
    val stringNum = number.toString().padStart(3, '0')
    return "$${formatWithCommas(stringNum.dropLast(2))}.${stringNum.takeLast(2)}"
}
