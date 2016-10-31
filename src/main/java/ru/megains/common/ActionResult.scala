package ru.megains.common

import ru.megains.common.EnumActionResult.EnumActionResult


class ActionResult[T](val `type`: EnumActionResult, val result: T) {

}
