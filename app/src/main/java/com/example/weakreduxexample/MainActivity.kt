package com.example.weakreduxexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import com.example.weakredux.Action
import com.example.weakredux.Redux
import com.example.weakredux.State
import com.google.android.material.textfield.TextInputLayout

class MainActivity: AppCompatActivity(), Redux<MainActivity.SampleState, MainActivity.SampleAction> {
    private val inputUsername: TextInputLayout by lazy { findViewById(R.id.input_username) }
    private val inputPassword: TextInputLayout by lazy { findViewById(R.id.input_password) }
    private val button: Button by lazy { findViewById(R.id.button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Call the dispatch()'es on UI interactions */
        inputUsername.editText?.doAfterTextChanged { editable ->
            this.dispatch(SampleAction.SetUsername(username = editable.toString()))
        }
        inputPassword.editText?.doAfterTextChanged { editable ->
            this.dispatch(SampleAction.SetPassword(password = editable.toString()))
        }
    }

    /** These can be written someplace else */
    data class SampleState(val usernameInput: String, val passwordInput: String): State
    sealed class SampleAction: Action {
        data class SetUsername(val username: String): SampleAction()
        data class SetPassword(val password: String): SampleAction()
    }

    private val initialState = SampleState(usernameInput = "", passwordInput = "")
    override val store: Redux.Store<SampleState> = Redux.Store(initialState)

    /**
     * Define the reducer.
     * It's commonly just shallow copies of members of (a presumably flat) State
     */
    override val reducer: (SampleState, SampleAction) -> SampleState = { oldState, action ->
        // can also add a logger here
        Log.d("my tag", "$action")
        when (action) {
            is SampleAction.SetUsername -> oldState.copy(usernameInput = action.username)
            is SampleAction.SetPassword -> oldState.copy(passwordInput = action.password)
        }
    }

    /**
     * Put View logic here. Avoid infinite loops by dispatching Actions inside
     */
    override fun update(newState: SampleState) {
        // some sample logic: enable button once both inputs are not empty (not checking for blanks, though)
        button.isEnabled = newState.usernameInput != "" && newState.passwordInput != ""
    }

}