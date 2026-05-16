package com.velocita.elite.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// ─────────────────────────────────────────────────────────────
//  VELOCITÀ ELITE — MAIN VIEW MODEL
// ─────────────────────────────────────────────────────────────

data class BikeModel(
    val id          : Int,
    val name        : String,
    val brand       : String,
    val tagline     : String,
    val priceDisplay: String,
    val engineCC    : Int,
    val horsePower  : Int,
    val topSpeedKph : Int,
    val weightKg    : Int,
    val description : String,
    val colorHex    : String,
    val imageUrl    : String,
    val descriptionImageUrl: String
)

data class UserProfile(
    val name: String = "",
    val email: String = ""
)

data class FieldState(
    val value : String = "",
    val error : String? = null
)

data class BookingState(
    val name         : FieldState = FieldState(),
    val phone        : FieldState = FieldState(),
    val serviceType  : String     = "Maintenance",
    val preferredDate: String     = "",
    val notes        : FieldState = FieldState(),
    val submitted    : Boolean    = false
)

class MainViewModel : ViewModel() {

    private val emailRegex   = Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$")
    private val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d).{8,}\$")
    private val phoneRegex   = Regex("^\\+?[0-9]{7,15}\$")

    // --- User Profile State ---
    var currentUser by mutableStateOf<UserProfile?>(null)
        private set

    fun setUserProfile(name: String, email: String) {
        currentUser = UserProfile(name = name, email = email)
    }

    // --- Booking State ---
    var booking by mutableStateOf(BookingState())
        private set

    fun onBookingNameChange(value: String)  { booking = booking.copy(name  = FieldState(value)) }
    fun onBookingPhoneChange(value: String) { booking = booking.copy(phone = FieldState(value)) }
    fun onBookingNotesChange(value: String) { booking = booking.copy(notes = FieldState(value)) }
    fun onServiceTypeChange(type: String)   { booking = booking.copy(serviceType = type) }
    fun onDateChange(date: String)          { booking = booking.copy(preferredDate = date) }

    fun submitBooking(): Boolean {
        var valid = true
        booking = booking.copy(
            name = if (booking.name.value.trim().length < 2) { valid = false; booking.name.copy(error = "Please enter your name") } else booking.name.copy(error = null),
            phone = if (!phoneRegex.matches(booking.phone.value.trim())) { valid = false; booking.phone.copy(error = "Valid phone number required") } else booking.phone.copy(error = null)
        )
        if (valid) booking = booking.copy(submitted = true)
        return valid
    }

    fun resetBooking() { booking = BookingState() }

    // --- Catalogue Data ---
    val bikeList: List<BikeModel> = listOf(
        BikeModel(
            id           = 0,
            name         = "Panigale V4 R",
            brand        = "Ducati",
            tagline      = "The closest thing to a MotoGP machine",
            priceDisplay = "£ 38,500",
            engineCC     = 998,
            horsePower   = 240,
            topSpeedKph  = 299,
            weightKg     = 172,
            colorHex     = "#CC2200",
            imageUrl     = "https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?auto=format&fit=crop&q=80&w=1200",
            descriptionImageUrl = "https://images.ctfassets.net/x7j9qwvpvr5s/4mNBxO4ppjUmSUjiwpwxUL/8cb6e1766a299e27d4f1c56f8d7d5e30/Ducati-MY25-Panigale-V4-motore-accordion-414x434-03.jpg",
            description  = "The Panigale V4 R is Ducati's closest road-legal expression of superbike racing DNA. Its 998cc Desmosedici Stradale R engine delivers a stratospheric 240 hp."
        ),
        BikeModel(
            id           = 1,
            name         = "S 1000 RR",
            brand        = "BMW Motorrad",
            tagline      = "Bavarian precision redefined",
            priceDisplay = "£ 28,900",
            engineCC     = 999,
            horsePower   = 205,
            topSpeedKph  = 299,
            weightKg     = 197,
            colorHex     = "#0057A0",
            imageUrl     = "https://cdn.bikedekho.com/processedimages/bmw/2025-s-1000-rr/source/2025-s-1000-rr678b98fe42c4c.jpg?imwidth=412&impolicy=resize",
            descriptionImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQf0eZzZ6XmDk-R2w4ZSXf6hbXRRer2FhRoWg&s",
            description  = "The BMW S 1000 RR is the ultimate superbike. It features an inline four-cylinder engine with ShiftCam technology for incredible performance across the entire RPM range."
        ),
        BikeModel(
            id           = 2,
            name         = "RSV4 Factory",
            brand        = "Aprilia",
            tagline      = "Three decades of racing heritage",
            priceDisplay = "£ 26,200",
            engineCC     = 1099,
            horsePower   = 217,
            topSpeedKph  = 299,
            weightKg     = 184,
            colorHex     = "#B80000",
            imageUrl     = "https://www.webbikeworld.com/wp-content/uploads/2020/02/2020-aprilia-rsv4-rr-03-1000x588.jpg",
            descriptionImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5-IuM_2aTjxfAOZgU7QVpDFKXw5dYzOcAKQ&s",
            description  = "Aprilia's RSV4 Factory benefits from direct World Superbike Championship development with its compact 65° V4 engine."
        )
    )

    fun getBikeById(id: Int): BikeModel? = bikeList.find { it.id == id }
}
