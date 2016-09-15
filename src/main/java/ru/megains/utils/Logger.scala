package ru.megains.utils

import org.apache.logging.log4j.LogManager

trait Logger[T] {

    val log = LogManager.getLogger(asInstanceOf[T])


}
