package edu.nd.pmcburne.hello

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import edu.nd.pmcburne.hello.ui.theme.MyApplicationTheme
import com.google.android.gms.maps.CameraUpdateFactory

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            "Welcome to the Counter App!",
            modifier = Modifier.padding(16.dp)
        )
        Counter(viewModel, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        val singapore = LatLng(1.35, 103.87)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewMainScreen() {
    MyApplicationTheme {
        MainScreen(viewModel = MainViewModel())
    }
}

@Composable
fun Counter(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val counterValue = uiState.counterValue
    Row(modifier = modifier) {
        Text("Value: $counterValue", modifier = Modifier.padding(end = 8.dp))
        Button(
            onClick = { viewModel.incrementCounter() }
        ) { Text("+") }
        Button(
            onClick = { viewModel.decrementCounter() },
            enabled = viewModel.isDecrementEnabled,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text("-")
        }
        Button(
            onClick = { viewModel.resetCounter() },
            enabled = viewModel.isResetEnabled
        ) {
            Text("Reset")
        }
    }
}

@Preview(name = "Light Mode Counter", showBackground = true)
@Preview(name = "Dark Mode Counter", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CounterPreview() {
    MyApplicationTheme {
        Counter(viewModel = MainViewModel(0))
    }
}
