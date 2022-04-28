package com.example.e.destinations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.e.login.session.AccessToken
import com.example.e.source.SwitchSourceCard
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDateTime

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalSerializationApi
object Navigation {
    @Composable
    fun NavigationComponent(
        navController: NavHostController,
        showDatePicker: ((LocalDateTime) -> Unit) -> Unit,
        payOffText: String,
        accessToken: LiveData<AccessToken?>,
        sourceRemote: LiveData<Boolean>
    ) {
        fun NavGraphBuilder.composableWithAuthorization(
            destination: Destination,
            arguments: List<NamedNavArgument> = emptyList(),
            deepLinks: List<NavDeepLink> = emptyList(),
        ) {
            composable(destination.route, arguments, deepLinks) {
                val token by accessToken.observeAsState()
                val isSourceRemote by sourceRemote.observeAsState(false)
                if (isNavigateToLoginScreenRequired(
                        token,
                        destination.route,
                        isSourceRemote
                    )
                ) {
                    LaunchedEffect(
                        key1 = Unit,
                        block = { navController.navigate(Destination.LOGIN) }
                    )
                }
                destination.Content(it, navController, showDatePicker, payOffText)
            }
        }

        NavHost(
            navController = navController,
            startDestination = Destination.HOME.route
        ) {
            Destination.values().forEach { composableWithAuthorization(it) }
        }
    }

    private fun isNavigateToLoginScreenRequired(
        token: AccessToken?,
        route: String,
        isSourceRemote: Boolean
    ) = token == null && route != Destination.LOGIN.route && isSourceRemote

    fun NavHostController.navigateHome(): () -> Unit =
        { this.navigate(Destination.HOME) }

    private fun NavHostController.navigate(destination: Destination) {
        navigate(destination.route)
    }
}
