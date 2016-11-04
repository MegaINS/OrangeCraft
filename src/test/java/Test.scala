import java.io.File

import ru.megains.common.world.storage.AnvilSaveFormat

import scala.reflect.io.Directory

object Test extends App {


    val a = new AnvilSaveFormat(new Directory(new File("C:/OrangeCraft/", "saves")))

    println(getClass.getClassLoader.getResource("").getPath)
    a.getSavesArray.foreach(println(_))
}
