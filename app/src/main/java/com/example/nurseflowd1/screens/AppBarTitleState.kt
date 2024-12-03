package com.example.nurseflowd1.screens

sealed class AppBarTitleState {
    data class DisplayTitle(val display : String) : AppBarTitleState()
    data class DisplayTitleWithBack(val display : String) : AppBarTitleState()
    object NoTopAppBar : AppBarTitleState()
}
sealed class AppBarColorState {
    object NurseDashColors : AppBarColorState()
    object DefaultColors : AppBarColorState()
}
sealed class NavigationIconState {
    object None : NavigationIconState()
    object DefaultBack : NavigationIconState()
}
// Move the logo to right
