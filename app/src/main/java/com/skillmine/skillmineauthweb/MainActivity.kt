package com.skillmine.skillmineauthweb

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skillmine.skillmineauthweb.AuthenticationConstants.BASE_URL
import com.skillmine.skillmineauthweb.AuthenticationConstants.CLIENT_ID
import com.skillmine.skillmineauthweb.AuthenticationConstants.REDIRECT_URL
import com.skillmine.webauthsdk.AuthenticationActivity

class MainActivity : AppCompatActivity() {
    private lateinit var loginButton: Button

    //Get the result from AuthenticationActivity
    private val authActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val accessToken = result.data?.getStringExtra("access_token")
                accessToken?.let {
                    //Open Dashboard Activity
                    startActivity(Intent(this@MainActivity, DashBoardActivity::class.java))
                    Log.v("ACCESS_TOKEN", accessToken)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loginButton = findViewById<Button>(R.id.bt_login)

        //Call WebAuth Library
        loginButton.setOnClickListener {
            val intent =
                AuthenticationActivity.createIntent(this, BASE_URL, CLIENT_ID, REDIRECT_URL)
            authActivityResultLauncher.launch(intent)

        }
    }
}


