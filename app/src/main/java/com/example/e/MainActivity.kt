package com.example.e

import android.app.Application
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.e.destinations.Navigation
import com.example.e.login.session.TokenRepository
import com.example.e.sharedpreferences.Preferences
import com.example.e.ui.theme.ETheme
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.serialization.ExperimentalSerializationApi
import java.time.LocalDateTime
import javax.inject.Inject

@HiltAndroidApp
class ExpenseApplication : Application()

@AndroidEntryPoint
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalSerializationApi
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var tokenRepository: TokenRepository

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
            param(FirebaseAnalytics.Param.LOCATION_ID, Settings.Secure.ANDROID_ID)
        }
        setContent {
            val navController = rememberNavController()
            ETheme {
                Navigation.NavigationComponent(
                    navController,
                    ::showDatePicker,
                    stringResource(id = R.string.payyOff),
                    tokenRepository.accessToken,
                    preferences.isSourceRemote()

                )
            }
        }
    }

    private fun showDatePicker(onDateSet: (LocalDateTime) -> Unit) {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        val timePicker = MaterialTimePicker.Builder().build()
        datePicker.addOnPositiveButtonClickListener { date ->
            timePicker.addOnPositiveButtonClickListener {
                onDateSet(
                    date.toLocalDateTime().withHour(timePicker.hour)
                        .withMinute(timePicker.minute)
                )
            }
            timePicker.show(supportFragmentManager, datePicker.toString())
        }
        datePicker.show(supportFragmentManager, datePicker.toString())
    }
}
