package com.velocita.elite.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// ─────────────────────────────────────────────────────────────
//  VELOCITÀ ELITE — MAIN VIEW MODEL
//
//  Single shared ViewModel for the app.  Owns:
//    • Form state for Enrollment & Login screens
//    • Validation logic (Regex-based)
//    • The motorcycle catalogue (data layer stand-in)
//    • Concierge booking state
// ─────────────────────────────────────────────────────────────

// ── Data Models ───────────────────────────────────────────────

/**
 * Represents a single luxury motorcycle in the showroom.
 *
 * @param id           Unique identifier; used as navigation argument.
 * @param name         Model name (e.g. "Panigale V4 R").
 * @param brand        Manufacturer (e.g. "Ducati").
 * @param tagline      Short marketing line shown on the showroom card.
 * @param priceDisplay Formatted price string (e.g. "£ 38,500").
 * @param engineCC     Engine displacement in cubic centimetres.
 * @param horsePower   Peak power output.
 * @param topSpeedKph  Claimed top speed.
 * @param weightKg     Kerb weight in kilograms.
 * @param description  Longer description shown on the Config screen.
 * @param colorHex     Dominant brand/bike colour as a hex string (for gradient tint).
 */
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
    val colorHex    : String
)

/**
 * Holds a single form field's text value alongside its
 * optional error message.  null error means the field is valid
 * (or not yet validated).
 */
data class FieldState(
    val value : String = "",
    val error : String? = null
)

/**
 * Encapsulates the booking details submitted from the
 * Concierge screen.
 */
data class BookingState(
    val name         : FieldState = FieldState(),
    val phone        : FieldState = FieldState(),
    val serviceType  : String     = "Maintenance",   // "Maintenance" | "Private Viewing"
    val preferredDate: String     = "",
    val notes        : FieldState = FieldState(),
    val submitted    : Boolean    = false
)

// ─────────────────────────────────────────────────────────────
//  VIEW MODEL
// ─────────────────────────────────────────────────────────────

class MainViewModel : ViewModel() {

    // ── Regex Patterns ────────────────────────────────────────

    /** RFC 5322-compliant email validation pattern. */
    private val emailRegex   = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    /** Password: min 8 chars, at least one letter and one digit. */
    private val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")

    /** International phone: optional '+', 7–15 digits. */
    private val phoneRegex   = Regex("^\\+?[0-9]{7,15}$")

    // ── Enrollment Form State ─────────────────────────────────

    /** Full name field on the enrollment form. */
    var enrollName     by mutableStateOf(FieldState())
        private set

    /** Email field — shared across enrollment and login. */
    var enrollEmail    by mutableStateOf(FieldState())
        private set

    /** Password field — shared across enrollment and login. */
    var enrollPassword by mutableStateOf(FieldState())
        private set

    /** Phone number field (enrollment only). */
    var enrollPhone    by mutableStateOf(FieldState())
        private set

    /** True once all enrollment fields pass validation. */
    var enrollSuccess  by mutableStateOf(false)
        private set

    // ── Login Form State ──────────────────────────────────────

    /** Email field for the Owner Login screen. */
    var loginEmail    by mutableStateOf(FieldState())
        private set

    /** Password field for the Owner Login screen. */
    var loginPassword by mutableStateOf(FieldState())
        private set

    /** Set to true to trigger login-success side effects. */
    var loginSuccess  by mutableStateOf(false)
        private set

    // ── Concierge Booking State ───────────────────────────────

    /** Complete booking form state for the Concierge screen. */
    var booking by mutableStateOf(BookingState())
        private set

    // ─────────────────────────────────────────────────────────
    //  ENROLLMENT FIELD UPDATERS
    // ─────────────────────────────────────────────────────────

    /** Called on every keystroke in the Name field. */
    fun onEnrollNameChange(value: String) {
        enrollName = FieldState(value = value)
    }

    /** Called on every keystroke in the Enrollment Email field. */
    fun onEnrollEmailChange(value: String) {
        enrollEmail = FieldState(value = value)
    }

    /** Called on every keystroke in the Enrollment Password field. */
    fun onEnrollPasswordChange(value: String) {
        enrollPassword = FieldState(value = value)
    }

    /** Called on every keystroke in the Phone field. */
    fun onEnrollPhoneChange(value: String) {
        enrollPhone = FieldState(value = value)
    }

    /**
     * Validates all enrollment fields.
     * Writes inline error strings back into each [FieldState] on failure.
     * @return true if every field is valid and form may be submitted.
     */
    fun validateEnrollment(): Boolean {
        var valid = true

        // ── Validate Name ──
        enrollName = if (enrollName.value.trim().length < 2) {
            valid = false
            enrollName.copy(error = "Please enter your full name")
        } else {
            enrollName.copy(error = null)
        }

        // ── Validate Email ──
        enrollEmail = if (!emailRegex.matches(enrollEmail.value.trim())) {
            valid = false
            enrollEmail.copy(error = "Enter a valid email address")
        } else {
            enrollEmail.copy(error = null)
        }

        // ── Validate Password ──
        enrollPassword = if (!passwordRegex.matches(enrollPassword.value)) {
            valid = false
            enrollPassword.copy(error = "Min 8 characters with a letter and digit")
        } else {
            enrollPassword.copy(error = null)
        }

        // ── Validate Phone ──
        enrollPhone = if (!phoneRegex.matches(enrollPhone.value.trim())) {
            valid = false
            enrollPhone.copy(error = "Enter a valid phone number (7–15 digits)")
        } else {
            enrollPhone.copy(error = null)
        }

        if (valid) enrollSuccess = true
        return valid
    }

    // ─────────────────────────────────────────────────────────
    //  LOGIN FIELD UPDATERS
    // ─────────────────────────────────────────────────────────

    /** Called on every keystroke in the Login Email field. */
    fun onLoginEmailChange(value: String) {
        // Real-time: clear previous error as the user types
        loginEmail = FieldState(value = value)
    }

    /** Called on every keystroke in the Login Password field. */
    fun onLoginPasswordChange(value: String) {
        loginPassword = FieldState(value = value)
    }

    /**
     * Validates the login fields with real-time inline feedback.
     * @return true if credentials pass local format validation.
     */
    fun validateLogin(): Boolean {
        var valid = true

        loginEmail = if (!emailRegex.matches(loginEmail.value.trim())) {
            valid = false
            loginEmail.copy(error = "Invalid email address")
        } else {
            loginEmail.copy(error = null)
        }

        loginPassword = if (loginPassword.value.length < 8) {
            valid = false
            loginPassword.copy(error = "Password must be at least 8 characters")
        } else {
            loginPassword.copy(error = null)
        }

        if (valid) loginSuccess = true
        return valid
    }

    // ─────────────────────────────────────────────────────────
    //  CONCIERGE BOOKING UPDATERS
    // ─────────────────────────────────────────────────────────

    fun onBookingNameChange(value: String)  { booking = booking.copy(name  = FieldState(value)) }
    fun onBookingPhoneChange(value: String) { booking = booking.copy(phone = FieldState(value)) }
    fun onBookingNotesChange(value: String) { booking = booking.copy(notes = FieldState(value)) }
    fun onServiceTypeChange(type: String)   { booking = booking.copy(serviceType = type) }
    fun onDateChange(date: String)          { booking = booking.copy(preferredDate = date) }

    /**
     * Validates and submits the concierge booking form.
     * @return true if the booking was accepted.
     */
    fun submitBooking(): Boolean {
        var valid = true

        booking = booking.copy(
            name = if (booking.name.value.trim().length < 2) {
                valid = false
                booking.name.copy(error = "Please enter your name")
            } else booking.name.copy(error = null),

            phone = if (!phoneRegex.matches(booking.phone.value.trim())) {
                valid = false
                booking.phone.copy(error = "Valid phone number required")
            } else booking.phone.copy(error = null)
        )

        if (valid) booking = booking.copy(submitted = true)
        return valid
    }

    /** Resets the booking form (e.g. after a confirmed submission). */
    fun resetBooking() {
        booking = BookingState()
    }

    // ─────────────────────────────────────────────────────────
    //  MOTORCYCLE CATALOGUE  (static data — replace with
    //  a repository / API call in production)
    // ─────────────────────────────────────────────────────────

    /** Returns the full list of bikes shown in the Showroom. */
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
            description  = "The Panigale V4 R is Ducati's closest road-legal expression " +
                    "of superbike racing DNA. Its 998cc Desmosedici Stradale R engine, " +
                    "based directly on the GP machine, delivers a stratospheric 240 hp " +
                    "before optional racing exhaust fitment.  Carbon fibre bodywork, " +
                    "electronically adjustable Öhlins suspension and Brembo Stylema " +
                    "monobloc callipers place this firmly in the realm of the extraordinary."
        ),
        BikeModel(
            id           = 1,
            name         = "S 1000 RR M",
            brand        = "BMW Motorrad",
            tagline      = "Bavarian precision redefined",
            priceDisplay = "£ 28,900",
            engineCC     = 999,
            horsePower   = 212,
            topSpeedKph  = 299,
            weightKg     = 193,
            colorHex     = "#0057A0",
            description  = "BMW's M 1000 RR is the product of uncompromising German " +
                    "engineering.  The M variant's titanium connecting rods and carbon " +
                    "fibre winglets — generating 11 kg of downforce at top speed — " +
                    "illustrate how far production superbikes have come.  ShiftCam " +
                    "variable valve timing delivers torque at every rev range."
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
            description  = "Aprilia's RSV4 Factory benefits from direct World Superbike " +
                    "Championship development.  The 65° V4 engine's compact dimensions " +
                    "allow ideal mass centralisation while its 217 hp output challenges " +
                    "machines costing twice as much.  APRC electronics suite rivals any " +
                    "competitor with cornering ABS, launch control, and eight-level " +
                    "traction control."
        ),
        BikeModel(
            id           = 3,
            name         = "CBR 1000RR-R SP",
            brand        = "Honda",
            tagline      = "Total control — every corner",
            priceDisplay = "£ 29,500",
            engineCC     = 999,
            horsePower   = 217,
            topSpeedKph  = 299,
            weightKg     = 179,
            colorHex     = "#CC3300",
            description  = "Honda's Fireblade SP was designed in parallel with their " +
                    "RC213V MotoGP machine, sharing the same engine concept philosophy. " +
                    "Öhlins NPX 25mm forks, rear Smart EC 2.0 electronics and " +
                    "Brembo Stylema callipers produce a bike that is simultaneously " +
                    "track-weapon and GT touring companion."
        ),
        BikeModel(
            id           = 4,
            name         = "Ninja ZX-10R KRT",
            brand        = "Kawasaki",
            tagline      = "Born in the World Superbike paddock",
            priceDisplay = "£ 22,800",
            engineCC     = 998,
            horsePower   = 203,
            topSpeedKph  = 299,
            weightKg     = 207,
            colorHex     = "#2D8C00",
            description  = "The KRT Edition Ninja ZX-10R wears the colours of Kawasaki " +
                    "Racing Team — five-time World Superbike champions.  Its 998cc " +
                    "inline-four breathes through a new finger-follower valve train " +
                    "lifted directly from the championship bike, rev-matching on " +
                    "downshifts and offering full Öhlins electronic suspension."
        ),
        BikeModel(
            id           = 5,
            name         = "YZF-R1M",
            brand        = "Yamaha",
            tagline      = "Electronics you once only dreamed of",
            priceDisplay = "£ 25,600",
            engineCC     = 998,
            horsePower   = 200,
            topSpeedKph  = 299,
            weightKg     = 202,
            colorHex     = "#0033AA",
            description  = "Yamaha's R1M is the electronics flagship.  Semi-active Öhlins " +
                    "Electronic Racing Suspension (ERS) with satellite-sensor IMU makes " +
                    "real-time damping adjustments mid-corner.  Carbon fibre body panels " +
                    "and the crossplane crank's linear power delivery make the R1M one " +
                    "of the most rewarding superbikes to ride quickly."
        ),
    )

    /**
     * Looks up a [BikeModel] by its [id].
     * Returns null if the id is out of range (defensive guard).
     */
    fun getBikeById(id: Int): BikeModel? = bikeList.getOrNull(id)
}
