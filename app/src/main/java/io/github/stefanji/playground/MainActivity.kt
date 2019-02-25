package io.github.stefanji.playground

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    private lateinit var hostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        hostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            Toast.makeText(this, "nav to: ${destination.label}", Toast.LENGTH_SHORT).show()
        }

        // Test navigate
        window.decorView.postDelayed({
            hostFragment.navController.navigate(R.id.nav_second_fragment)
        }, 6000)
    }
}