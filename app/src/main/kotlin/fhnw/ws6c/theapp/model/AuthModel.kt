package fhnw.ws6c.theapp.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthModel(private val plantModel: PlantModel) : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val sharedPref: SharedPreferences = plantModel.context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    var email: TextFieldValue = TextFieldValue()

    fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    fun signInScreen() {
        plantModel.currentScreen = Screen.SIGNIN
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
        plantModel.currentScreen = Screen.SIGNUP
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

    companion object {
        private const val TAG = "AuthViewModel"
    }
}