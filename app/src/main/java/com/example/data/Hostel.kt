package com.example.data

data class Hostel(
    val id: String,
    val name: String,
    val address: String,
    val monthlyPrice: Int,
    val basicInfo: String
) {
    val formattedPrice: String
        get() = "₹${"%,d".format(monthlyPrice)}"
}
