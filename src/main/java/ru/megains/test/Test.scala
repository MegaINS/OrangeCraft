package ru.megains.test

;

object Test extends App {

    // args.foreach(println(_))
    println(getClass.getClassLoader.getResource("").getPath)

}
