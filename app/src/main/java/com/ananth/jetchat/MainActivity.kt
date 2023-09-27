package com.ananth.jetchat

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.ananth.jetchat.components.JetchatDrawer
import com.ananth.jetchat.databinding.ContentMainBinding
import com.ananth.jetchat.ui.theme.JetchatTheme
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(
            ComposeView(this).apply {
                consumeWindowInsets = false
                setContent {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val drawerOpen by viewModel.drawerShouldBeOpened.collectAsStateWithLifecycle()

                    if (drawerOpen) {
                        // Open drawer and reset state in VM.
                        LaunchedEffect(Unit) {
                            try {
                                drawerState.open()
                            } finally {
                                viewModel.resetOpenDrawerAction()
                            }
                        }
                    }

                    //intercepts back navigation when the drawer is open
                    val scope = rememberCoroutineScope()
                    if (drawerState.isOpen) {
                        BackHandler {
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    }

                    JetchatDrawer(drawerState = drawerState, onProfileClicked = {
                        val bundle = bundleOf("userId" to it)
                        findNavController().navigate(R.id.nav_profile)
                        scope.launch {
                            drawerState.close()
                        }
                    }, onChatClicked = {
                        findNavController().popBackStack(R.id.nav_profile, false)
                        scope.launch {
                            drawerState.close()
                        }
                    }) {
                        AndroidViewBinding(ContentMainBinding::inflate)
                    }
                }
            }
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController().navigateUp() || super.onSupportNavigateUp()
    }

    private fun findNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }
}
