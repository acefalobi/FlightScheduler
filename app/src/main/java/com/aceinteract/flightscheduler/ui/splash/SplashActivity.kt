package com.aceinteract.flightscheduler.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.aceinteract.flightscheduler.BuildConfig
import com.aceinteract.flightscheduler.MainActivity
import com.aceinteract.flightscheduler.data.remote.repository.AuthRemoteRepository
import com.aceinteract.flightscheduler.util.StorageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * Activity that shows splash screen and handles routing to other activities whe opened
 */
class SplashActivity : AppCompatActivity() {

    private val storageUtil: StorageUtil by inject()

    private val authRemoteRepository: AuthRemoteRepository by inject()

    /**
     * Runs splash screen scheduler
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateAccessToken()
    }

    /**
     * Updates access token in local storage from remote repository
     */
    private fun updateAccessToken() {
        GlobalScope.launch {
            val accessToken = authRemoteRepository.getAccessToken(
                BuildConfig.LUFTHANSA_CLIENT_ID,
                BuildConfig.LUFTHANSA_CLIENT_SECRET
            )
            launch(Dispatchers.Main) {
                if (accessToken != null) {
                    storageUtil.accessToken = accessToken
                }
                if (accessToken != null || storageUtil.accessToken.isNotEmpty()) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
                else AlertDialog.Builder(this@SplashActivity)
                    .setTitle("Connection Error")
                    .setMessage(
                        "Error connecting to server. Check your internet connection and try again later."
                    )
                    .setPositiveButton("OK") { _, _ ->
                        finish()
                    }
                    .setOnDismissListener {
                        finish()
                    }.show()
            }
        }
    }

}
