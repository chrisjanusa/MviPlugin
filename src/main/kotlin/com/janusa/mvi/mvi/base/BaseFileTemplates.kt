package com.janusa.mvi.mvi.base

import com.janusa.mvi.mvi.helpers.FileTemplate

object MVIScreenTemplate : FileTemplate(
    title = "MVIScreen",
    content = "import androidx.compose.runtime.Composable\n" +
            "import androidx.compose.runtime.LaunchedEffect\n" +
            "import androidx.compose.ui.platform.LocalContext\n" +
            "import com.ramcosta.composedestinations.navigation.DestinationsNavigator\n" +
            "\n" +
            "@Composable\n" +
            "fun <State> MVIScreen(\n" +
            "    navigator: DestinationsNavigator,\n" +
            "    viewModel: MVIViewModel<State>,\n" +
            "    initialAction: BaseAction<State>? = null,\n" +
            "    content: @Composable ((State) -> Unit),\n" +
            ") {\n" +
            "    val context = LocalContext.current\n" +
            "    LaunchedEffect(key1 = true) {\n" +
            "        initialAction?.let { viewModel.performAction(initialAction) }\n" +
            "        viewModel.eventFlow.collect { event ->\n" +
            "            event.performEvent(context, navigator)\n" +
            "        }\n" +
            "    }\n" +
            "    content(viewModel.state.value)\n" +
            "}"
)

object BaseActionTemplate : FileTemplate(
    title = "BaseAction",
    content = "interface BaseAction<S> {\n" +
            "    suspend fun performAction(\n" +
            "        currentState: S,\n" +
            "        sendUpdater: (BaseUpdater<S>) -> Unit,\n" +
            "        sendEvent: (BaseEvent) -> Unit\n" +
            "    )\n" +
            "}"
)

object BaseEventTemplate : FileTemplate(
    title = "BaseEvent",
    content = "import android.content.Context\n" +
            "import com.ramcosta.composedestinations.navigation.DestinationsNavigator\n" +
            "\n" +
            "interface BaseEvent {\n" +
            "    fun performEvent(context: Context, navigator: DestinationsNavigator)\n" +
            "}"
)

object BaseUpdaterTemplate : FileTemplate(
    title = "BaseUpdater",
    content = "interface BaseUpdater<S> {\n" +
            "    fun performUpdate(prevState: S) : S\n" +
            "}"
)

object MVIViewModelTemplate : FileTemplate(
    title = "MVIViewModel",
    content = "import androidx.compose.runtime.MutableState\n" +
            "import androidx.compose.runtime.State\n" +
            "import androidx.compose.runtime.mutableStateOf\n" +
            "import androidx.lifecycle.ViewModel\n" +
            "import androidx.lifecycle.viewModelScope\n" +
            "import kotlinx.coroutines.flow.MutableSharedFlow\n" +
            "import kotlinx.coroutines.flow.SharedFlow\n" +
            "import kotlinx.coroutines.launch\n" +
            "\n" +
            "abstract class MVIViewModel<S>(initialState: S) : ViewModel() {\n" +
            "    private val _state: MutableState<S> = mutableStateOf(initialState)\n" +
            "    val state: State<S> = _state\n" +
            "    private val updateFlow = MutableSharedFlow<BaseUpdater<S>>()\n" +
            "    private val _eventFlow = MutableSharedFlow<BaseEvent>()\n" +
            "    val eventFlow: SharedFlow<BaseEvent> = _eventFlow\n" +
            "\n" +
            "    init {\n" +
            "        viewModelScope.launch {\n" +
            "            updateFlow.collect { updater ->\n" +
            "                _state.value = updater.performUpdate(state.value)\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private fun sendUpdate(update: BaseUpdater<S>) {\n" +
            "        viewModelScope.launch {\n" +
            "            updateFlow.emit(update)\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private fun sendEvent(event: BaseEvent) {\n" +
            "        viewModelScope.launch {\n" +
            "            _eventFlow.emit(event)\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    val performAction : (BaseAction<S>) -> Unit = { action ->\n" +
            "        viewModelScope.launch {\n" +
            "            action.performAction(state.value, ::sendUpdate, ::sendEvent)\n" +
            "        }\n" +
            "    }\n" +
            "}"
)