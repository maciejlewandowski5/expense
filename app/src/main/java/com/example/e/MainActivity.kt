package com.example.e

import android.app.Application
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.e.ui.theme.ETheme
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import java.time.LocalDateTime

@HiltAndroidApp
class ExpenseApplication : Application()

@AndroidEntryPoint
@ExperimentalFoundationApi
@ExperimentalMaterialApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContent {
            val navController = rememberNavController()
            ETheme {
                Navigation.NavigationComponent(
                    navController,
                    ::showDatePicker,
                    stringResource(id = R.string.payyOff)
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
