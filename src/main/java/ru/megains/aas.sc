import java.io.{File, FileReader}

import scala.reflect.io.Path

//import scala.reflect.io.{File, Directory}

//val directory:Directory = Directory( File("resources"))
//for(a<- directory.list){
//  println(a)
//}
Path("test.txt").toAbsolute
new File("test.txt").getAbsolutePath
new FileReader(new File("/test.txt"))
