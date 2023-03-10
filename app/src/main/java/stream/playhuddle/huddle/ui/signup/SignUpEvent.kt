package stream.playhuddle.huddle.ui.signup

sealed class SignUpEvent {
    data class OnUsernameChange(val value: String) : SignUpEvent()
    data class OnAgeChange(val value: String) : SignUpEvent()
    data class OnLocationChange(val value: String) : SignUpEvent()
    data class OnInterestsChange(val value: String) : SignUpEvent()
    data class OnBioChange(val value: String) : SignUpEvent()
    data class ShowDialog(val value: Boolean) : SignUpEvent()

    data class OnEmailChange(val value: String) : SignUpEvent()
    data class OnPasswordChange(val value: String) : SignUpEvent()

    object OnSave : SignUpEvent()
    object StartSwiping : SignUpEvent()
}
