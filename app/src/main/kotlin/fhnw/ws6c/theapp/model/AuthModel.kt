package fhnw.ws6c.theapp.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser

class AuthModel(private val plantModel: PlantModel) : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val sharedPref: SharedPreferences = plantModel.context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    var email: TextFieldValue = TextFieldValue()
    var errorMessage by mutableStateOf<String?>(null)

    fun createAccount(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage = "Bitte gib Email und Passwort ein."
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    errorMessage = getCustomErrorMessage(task.exception)
                    updateUI(null)
                }
            }
    }

    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage = "Bitte gib Email und Passwort ein."
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    errorMessage = getCustomErrorMessage(task.exception)
                    updateUI(null)
                }
            }
    }

    fun signInScreen() {
        plantModel.currentScreen = Screen.LOGIN
    }

    fun signUpScreen() {
        plantModel.currentScreen = Screen.SIGNUP
    }

    fun logout() {
        auth.signOut()
        with(sharedPref.edit()) {
            putBoolean("is_logged_in", false)
            apply()
        }
        clearErrorMessage()
        plantModel.currentScreen = Screen.LOGIN
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            with(sharedPref.edit()) {
                putBoolean("is_logged_in", true)
                apply()
                Log.d(TAG, "Loggedin:true")
            }
            plantModel.currentScreen = Screen.HOME
        } else {
            Log.w(TAG, "signInWithEmail:failure")
        }
    }

    private fun clearErrorMessage() {
        errorMessage = null
    }

    private fun getCustomErrorMessage(exception: Exception?): String {
        if (exception is FirebaseAuthException) {
            return when (exception.errorCode) {
                "ERROR_INVALID_EMAIL" -> "Die Email-Adresse ist ungültig."
                "ERROR_INVALID_CREDENTIAL" -> "Die Email oder das Passwort ist ungültig."
                "ERROR_USER_NOT_FOUND" -> "Es exisitiert kein Account mit dieser Email. Gehe zu Account erstellen."
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Diese Email wurde bereits verwendet. Gehe zum Login."
                "ERROR_WEAK_PASSWORD" -> "Das Passwort ist zu schwach. Bitte wähle ein stärkeres Passwort."
                else -> "Ein unerwarteter Fehler ist passiert. Bitte prüfe deine Eingaben und versuche es nochmals."
            }
        }
        return "Ein unerwarteter Fehler ist passiert. Bitte prüfe deine Eingaben und versuche es nochmals."
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}