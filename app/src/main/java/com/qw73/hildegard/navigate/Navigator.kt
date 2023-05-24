package com.qw73.hildegard.navigate

import android.view.View

enum class Screen {
    AUTHORIZATION,
    MAIN
}

interface Navigator {
    fun navigateToAuthorization(screen: Screen, view: View, animationKey: String)
    fun navigateToMain()
}