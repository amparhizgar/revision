package com.amirhparhizgar.revision.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.ui.common.TaskBottomNav
import com.amirhparhizgar.revision.ui.screen.*
import com.amirhparhizgar.revision.ui.theme.RevisionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RevisionTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    val backstackEntry by navController.currentBackStackEntryAsState()
                    val startDest = todoScreen.destination
                    val showBottomNav =
                        backstackEntry?.destination?.route in bottomTabList.map { it.destination }
                    Scaffold(
                        bottomBar = {
                            if (showBottomNav) {
                                TaskBottomNav(
                                    list = bottomTabList,
                                    selected = tabIndexFromRout(backstackEntry?.destination?.route),
                                    onSelect = {
                                        navController.navigate(
                                            bottomTabList[it].destination,
                                            navOptions = tabNavigateOptions(navController.graph.startDestinationId)
                                        )
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding),
                            startDestination = startDest
                        ) {
                            composable(todoScreen.destination) {
                                TodoScreen(goSingleScreen = {
                                    goSingleTaskScreen(it, navController)
                                })
                            }
                            composable(
                                singleTaskScreen.destination + "?id={id}",
                                arguments = listOf(navArgument("id") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                })
                            ) {
                                SingleTaskScreen(onBack = {
                                    navController.popBackStack()
                                })
                            }
                            composable(
                                taskScreen.destination
                            ) {
                                TasksScreen(goSingleScreen = {
                                    goSingleTaskScreen(it, navController)
                                })
                            }
                            composable(profileScreen.destination) {
                                ProfileScreen(goSettings = { navController.navigate(settingScreen.destination) })
                            }
                            composable(settingScreen.destination) {
                                SettingScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun goSingleTaskScreen(
        task: Task?,
        navController: NavHostController
    ) {
        if (task == null)
            navController.navigate(singleTaskScreen.destination)
        else
            navController.navigate(singleTaskScreen.destination + "?id=" + task.id)
    }

    private fun tabIndexFromRout(rout: String?): Int {
        bottomTabList.forEachIndexed { index, tab ->
            if (tab.destination == rout)
                return index
        }
        return 0
    }

    private fun tabNavigateOptions(startDestId: Int) =
        NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(true)
            .setPopUpTo(startDestId, inclusive = false, saveState = true).build()
}
