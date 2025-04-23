package com.example.loomi.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loomi.R
import com.example.loomi.data.model.OnBoarding


class OnBoardingViewModel : ViewModel() {

    private val _onBoardingItems = MutableLiveData<List<OnBoarding>>()
    val onBoardingItems: LiveData<List<OnBoarding>> = _onBoardingItems

    init {
        loadOnBoardingData()
    }

    private fun loadOnBoardingData() {
        _onBoardingItems.value = listOf(
            OnBoarding("Selamat datang di Loomi", "Belajar jadi lebih mudah dan cepat dengan metode microlearning", R.drawable.onboarding),
            OnBoarding("Belajar Sesuai Waktu Kamu", "Setiap materi disusun dalam potongan kecil sehingga bisa dipelajari kapan saja dan di mana saja", R.drawable.onboarding),
            OnBoarding("Pantau Kemajuan Belajarmu", "Lacak setiap kemajuan yang sudah dicapai, dan raih pencapaian untuk tiap materi yang diselesaikan", R.drawable.onboarding),
            OnBoarding("Dapatkan Pengingat untuk Terus Belajar", "Aktifkan notifikasi supaya kamu nggak ketinggalan materi-materi baru.", R.drawable.onboarding),
            OnBoarding("Siap untuk Mulai?", "Jelajahi materi dan mulai perjalanan belajarmu sekarang!", R.drawable.onboarding)
        )
    }
}
