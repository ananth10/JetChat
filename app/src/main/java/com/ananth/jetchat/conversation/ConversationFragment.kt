package com.ananth.jetchat.conversation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ananth.jetchat.MainViewModel
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.ananth.jetchat.R
import com.ananth.jetchat.data.exampleUiState
import com.ananth.jetchat.ui.theme.JetchatTheme

class ConversationFragment : Fragment() {

    val mainViewModel: MainViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        setContent {
            JetchatTheme {
                ConversationContent(uiState = exampleUiState, navigateToProfile = { user ->
                    val bundle = bundleOf("userId" to user)
                    findNavController().navigate(R.id.nav_profile, bundle)

                },
                    onNavIconPressed = {
                        mainViewModel.openDrawer()
                    }
                )
            }
        }
    }
}