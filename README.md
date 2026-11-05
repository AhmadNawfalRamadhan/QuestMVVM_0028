# 🎓 Form Pendaftaran Siswa (MVVM)

Aplikasi Android sederhana berbasis **Jetpack Compose** yang menerapkan arsitektur **MVVM (Model-View-ViewModel)**. Pengguna mengisi data siswa (nama, jenis kelamin, alamat) pada halaman formulir, lalu data ditampilkan di halaman detail.

---

## 📸 Tampilan Aplikasi

| Halaman Form | Halaman Hasil (Data Siswa) |
|:---:|:---:|
| <img width="397" height="869" alt="Screenshot 2026-11-05 215730" src="https://github.com/user-attachments/assets/013cfac6-9c97-4159-9d1e-237e13328038" /> | <img width="398" height="871" alt="Screenshot 2026-11-05 215740" src="https://github.com/user-attachments/assets/e7335bce-09c7-429a-b2b3-d1411f0f2074" /> |
| Input nama, jenis kelamin, dan alamat | Menampilkan data yang telah diinput |

> 📝 Simpan screenshot di folder `screenshots/` dengan nama `form.png` dan `hasil.png`.

---

## ✨ Fitur

- Input **Nama Lengkap** dengan `OutlinedTextField`
- Pilihan **Jenis Kelamin** menggunakan `RadioButton` (Laki-laki / Perempuan)
- Input **Alamat**
- Tombol **Submit** (aktif setelah alamat terisi)
- Navigasi antar halaman dengan **Navigation Compose**
- Penyimpanan state dengan **ViewModel + StateFlow**
- Tanpa hardcode pada resource: teks memakai `strings.xml`, ukuran memakai `dimens.xml`
- Tombol **Back** pada halaman detail untuk kembali ke form

---

## 🏗️ Arsitektur MVVM

```
┌──────────┐   event (data form)   ┌───────────────┐   update state   ┌──────────┐
│   View   │ ────────────────────▶ │   ViewModel   │ ───────────────▶ │  Model   │
│ (Compose)│ ◀──────────────────── │ (StateFlow)   │ ◀─────────────── │ (Siswa)  │
└──────────┘    collectAsState     └───────────────┘                  └──────────┘
```

| Layer | Komponen | Peran |
|---|---|---|
| **Model** | `Siswa`, `DataJK` | Struktur data siswa dan daftar pilihan jenis kelamin |
| **View** | `FormSiswa`, `TampilSiswa`, `Navigasi` | Tampilan UI dan navigasi |
| **ViewModel** | `SiswaViewModel` | Menyimpan dan mengelola state data siswa |

---

## 📁 Struktur Folder

```
com.example.myarsitekturmvvm
├── model
│   ├── DataJK.kt
│   └── Siswa.kt
├── ui.theme
├── view
│   ├── uicontroller
│   │   └── Navigasi.kt
│   ├── FormSiswa.kt
│   └── TampilSiswa.kt
├── viewmodel
│   └── SiswaViewModel.kt
└── MainActivity.kt
```

---

## 🔍 Penjelasan Kode

### Model

**`Siswa.kt`** adalah data class yang menyimpan data siswa.

```kotlin
data class Siswa(
    val nama: String = "",
    val gender: String = "",
    val alamat: String = ""
)
```

**`DataJK.kt`** berisi daftar pilihan jenis kelamin yang merujuk ke resource string.

```kotlin
object DataJK {
    val JenisK = listOf(
        R.string.laki,
        R.string.wanita
    )
}
```

### ViewModel

**`SiswaViewModel.kt`** menyimpan state UI dengan `MutableStateFlow` dan mengeksposnya sebagai `StateFlow` (read-only) agar tidak bisa diubah langsung dari View.

```kotlin
class SiswaViewModel : ViewModel() {
    private val _statusUI = MutableStateFlow(Siswa())
    val statusUI: StateFlow<Siswa> = _statusUI.asStateFlow()

    fun setSiswa(ls: MutableList<String>) {
        _statusUI.update { statusSaatIni ->
            statusSaatIni.copy(nama = ls[0], gender = ls[1], alamat = ls[2])
        }
    }
}
```

### View

| File | Fungsi |
|---|---|
| `FormSiswa.kt` | Halaman input: `OutlinedTextField` untuk nama & alamat, `RadioButton` untuk jenis kelamin, dan tombol Submit. State lokal memakai `rememberSaveable` dan `remember`. |
| `TampilSiswa.kt` | Halaman hasil: menampilkan data dalam bentuk daftar `Pair(label, nilai)` beserta tombol Back. |
| `Navigasi.kt` | `enum class Navigasi { Formulir, Detail }` dan composable `SiswaApp` yang berisi `NavHost`. |

### Alur Navigasi

```
Formulir ──(Submit)──▶ Detail
   ▲                      │
   └──────(Back)──────────┘
```

1. Pengguna mengisi form lalu menekan **Submit**.
2. `viewModel.setSiswa(it)` menyimpan data ke state.
3. `navController.navigate(Navigasi.Detail.name)` membuka halaman detail.
4. Halaman detail membaca data dari `uiState.value`.
5. Tombol **Back** memanggil `popBackStack` kembali ke form.

---

## 🧩 Resource

**`strings.xml`**

```xml
<resources>
    <string name="app_name">Form Pendaftaran</string>
    <string name="laki">Laki-laki</string>
    <string name="wanita">Perempuan</string>
    <string name="submit">Submit</string>
    <string name="back">Back</string>
    <string name="nama">Nama Lengkap</string>
    <string name="alamat">Alamat</string>
    <string name="gender">Jenis Kelamin</string>
    <string name="detail">Data Siswa</string>
</resources>
```

**`dimens.xml`**

```xml
<resources>
    <dimen name="padding_small">10dp</dimen>
    <dimen name="padding_medium">16dp</dimen>
    <dimen name="thickness_divider">1dp</dimen>
</resources>
```

---

## 📦 Dependencies

Selain dependency bawaan Compose, proyek ini menambahkan:

```kotlin
implementation(libs.lifecycle.viewmodel.compose)
implementation(libs.lifecycle.runtime.compose)
implementation(libs.navigation.compose)
```

| Library | Kegunaan |
|---|---|
| `lifecycle-viewmodel-compose` | Memakai `viewModel()` di composable |
| `lifecycle-runtime-compose` | Memakai `collectAsState()` dari `StateFlow` |
| `navigation-compose` | Navigasi antar halaman (`NavHost`) |
| `material3` | Komponen UI Material Design 3 |

---

## 🚀 Cara Menjalankan

1. **Clone** repository ini:
   ```bash
   git clone https://github.com/AhmadNawfalRamadhan/QuestMVVM_0028.git
   ```
2. Buka folder proyek dengan **Android Studio**.
3. Tunggu proses **Gradle Sync** selesai.
4. Pilih emulator atau hubungkan perangkat Android.
5. Klik **Run ▶️**.

### Kebutuhan

- Android Studio (versi terbaru disarankan)
- JDK 11 atau lebih baru
- Min SDK sesuai konfigurasi di `build.gradle.kts`

---

## 🛠️ Teknologi

- **Kotlin**
- **Jetpack Compose** + **Material 3**
- **ViewModel** + **StateFlow**
- **Navigation Compose**
