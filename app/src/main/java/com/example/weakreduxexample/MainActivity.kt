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
import kotlin.reflect.KClass

class MainActivity: AppCompatActivity(), Redux<MainActivity.SampleState, MainActivity.SampleAction> {
    private val inputUsername: TextInputLayout by lazy { findViewById(R.id.input_username) }
    private val inputPassword: TextInputLayout by lazy { findViewById(R.id.input_password) }
    private val button: Button by lazy { findViewById(R.id.button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Call the store dispatch() on UI interactions.
         */
        inputUsername.editText?.doAfterTextChanged { editable ->
            store.dispatch(SampleAction.SetUsername(username = editable.toString()))
        }
        inputPassword.editText?.doAfterTextChanged { editable ->
            store.dispatch(SampleAction.SetPassword(password = editable.toString()))
        }
    }

    /**
     * This block can be written someplace else, and separately.
     */
    data class SampleState(val usernameInput: String, val passwordInput: String): State
    sealed class SampleAction: Action {
        data class SetUsername(val username: String): SampleAction()
        data class SetPassword(val password: String): SampleAction()
    }
    private val reducer: (SampleState, SampleAction) -> SampleState = { oldState, action ->
        /** can also add a logger here */
        Log.d("my tag", "$action")
        when (action) {
            is SampleAction.SetUsername -> oldState.copy(usernameInput = action.username)
            is SampleAction.SetPassword -> oldState.copy(passwordInput = action.password)
        }
    }
    private val initialState = SampleState(usernameInput = "", passwordInput = "")

    /**
     * Initialize store with an initial state, a reducer, and the activity reference that implements
     * Redux (this).
     *
     * The reference is needed so update() is called after every call to dispatch().
     */
    override val store: Redux.Store<SampleState, SampleAction> = Redux.Store(initialState, reducer, this)

    /**
     * Called after every dispatch().
     *
     * Put View logic here. In general, don't call action dispatches here to avoid infinite loops.
     * The UI element most prone to infinite loops is EditText's afterTextChanged() callback.
     */
    override fun update(newState: SampleState) {
        /** some sample logic: enable button once both inputs are not empty (not checking for blanks, though) */
        button.isEnabled = newState.usernameInput != "" && newState.passwordInput != ""
    }


}