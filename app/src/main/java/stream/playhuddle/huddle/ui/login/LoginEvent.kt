package stream.playhuddle.huddle.ui.login

sealed class LoginEvent {
    data class OnEmailChange(val email: String) : LoginEvent()
    data class OnPasswordChange(val password: String) : LoginEvent()
    object OnLogin : LoginEvent()
    object ClearMessage: LoginEvent()
}
