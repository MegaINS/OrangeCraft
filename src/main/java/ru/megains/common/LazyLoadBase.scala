package ru.megains.common

abstract class LazyLoadBase[T] {

    private var value: T = _
    private var isLoaded: Boolean = false

    def getValue: T = {
        if (!isLoaded) {
            isLoaded = true
            value = load
        }
        value
    }

    protected def load: T
}
