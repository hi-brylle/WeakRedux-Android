package com.example.weakredux

interface Redux <S, A> where S: State, A: Action {
    val store: Store<S>
    val reducer: (S, A) -> S
    fun update(newState: S)

    fun dispatch(vararg actions: A) {
        actions.forEach { action ->
            store.state = reducer(store.state, action)
        }
        update(store.state)
    }

    class Store<S>(initialState: S) where S: State {
        internal var state = initialState
    }
}

interface State
interface Action
