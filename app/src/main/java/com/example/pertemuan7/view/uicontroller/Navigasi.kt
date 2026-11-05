package com.example.pertemuan7.view.uicontroller

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pertemuan7.model.DataJK.JenisK
import com.example.pertemuan7.view.FormSiswa
import com.example.pertemuan7.view.TampilSiswa
import com.example.pertemuan7.viewmodel.SiswaViewModel

enum class Navigasi {
    Formulir,
    Detail
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiswaApp(
    //edit 1 : parameter viewModel
    modifier: Modifier,
    viewModel: SiswaViewModel = viewModel(),
    navController : NavHostController = rememberNavController()
){
    Scaffold { isiRuang->
        //edit 2 : tambahkan variabel uiState
        val uiState = viewModel.statusUI.collectAsState()
        NavHost(
            navController = navController,
            startDestination = Navigasi.Formulir.name,

            modifier = Modifier.padding(isiRuang)){
            composable(route = Navigasi.Formulir.name){
                //edit 3 : tambahkan variable konteks
                val konteks = LocalContext.current
                FormSiswa(
                    //edit 4 : parameter pilihanJK dan onSubmitButtonClicked
                    pilihanJK = JenisK.map { id -> konteks.resources.getString(id) },
                    onSubmitButtonClicked = {
                        viewModel.setSiswa(it)
                        navController.navigate(Navigasi.Detail.name)
                    }
                )
            }
            composable(route = Navigasi.Detail.name){
                TampilSiswa(
                    //edit 5 : parameter statusUiSiswa
                    statusUiSiswa = uiState.value,
                    onBackButtonClicked = { cancelAndBackToFormulir(navController) }
                )
            }
        }
    }
}

private fun cancelAndBackToFormulir(
    navController: NavHostController
) {
    navController.popBackStack(Navigasi.Formulir.name, inclusive = false)
}