package com.example.weakredux

interface Redux <S, A> where S: State, A: Action {
    val store: Store<S, A>
    fun update(newState: S)

    class Store<S, A>(initialState: S,
                      private val reducer: (S, A) -> S,
                      private val implementer: Redux<S, A>) where S: State, A: Action {
        private var state = initialState

        fun dispatch(vararg actions: A) {
            actions.forEach { action ->
                this.state = reducer(this.state, action)
            }

            implementer.update(this.state)
        }

        fun getState() = state
    }
}

interface State
interface Action
