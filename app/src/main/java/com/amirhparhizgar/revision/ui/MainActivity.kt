package com.amirhparhizgar.revision.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.amirhparhizgar.revision.model.Task
import com.amirhparhizgar.revision.model.ThemeMode
import com.amirhparhizgar.revision.service.setting.SettingStore
import com.amirhparhizgar.revision.ui.common.SheetPack
import com.amirhparhizgar.revision.ui.common.TaskBottomNav
import com.amirhparhizgar.revision.ui.screen.*
import com.amirhparhizgar.revision.ui.theme.RevisionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var settingStore: SettingStore

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemDarkTheme = isSystemInDarkTheme() // couldn't put it in map!
            val appDarkTheme = settingStore.themeSetting.flow.map {
                when (it) {
                    ThemeMode.LIGHT.id -> false
                    ThemeMode.DARK.id -> true
                    else -> systemDarkTheme // Automatic
                }
            }.collectAsState(initial = systemDarkTheme)

            RevisionTheme(darkTheme = appDarkTheme.value) {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    val backstackEntry by navController.currentBackStackEntryAsState()
                    val startDest = todoScreen.destination
                    val showBottomNav =
                        backstackEntry?.destination?.route in bottomTabList.map { it.destination }
                    val emptyLambda: @Composable ColumnScope.() -> Unit =
                        {
                            Spacer(
                                modifier = Modifier
                                    .height(5.dp)
                                    .width(5.dp)
                            )
                        }
                    val sheetPack: SheetPack =
                        remember {
                            SheetPack(MutableStateFlow(emptyLambda), ModalBottomSheetState(
                                initialValue = ModalBottomSheetValue.Hidden,
                                animationSpec = SwipeableDefaults.AnimationSpec,
                                confirmStateChange = { true }
                            ))
                        }
                    val sheetContent = sheetPack.flow.collectAsState()
                    ModalBottomSheetLayout(
                        sheetState = sheetPack.state,
                        sheetShape = MaterialTheme.shapes.large.copy(
                            bottomEnd = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp)
                        ),
                        sheetContent = sheetContent.value
                    ) {
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
                                    }, sheetPack)
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
                                    }, sheetPack)
                                }
                                composable(profileScreen.destination) {
                                    ProfileScreen(goSettings = {
                                        navController.navigate(
                                            settingScreen.destination
                                        )
                                    })
                                }
                                composable(settingScreen.destination) {
                                    SettingScreen(onBackPressed = navController::popBackStack)
                                }
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
