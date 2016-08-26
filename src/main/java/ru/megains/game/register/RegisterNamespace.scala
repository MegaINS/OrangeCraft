package ru.megains.game.register






import scala.collection.mutable


class RegisterNamespace[T]{

    private val idObject: mutable.HashMap[Int,T] = new mutable.HashMap[Int,T]
    private val objectId: mutable.HashMap[T,Int] = new mutable.HashMap[T,Int]
    private val nameObject: mutable.HashMap[String,T] = new mutable.HashMap[String,T]


    def registerObject(id: Int,name:String, Object: T) = {
        idObject += id -> Object
        objectId += Object -> id
        nameObject += name ->Object
    }

    def getObjects = idObject.values

    def getObject(id: Int): T = idObject(id)

    def getObject(name: String): T = nameObject(name)

    def getIdByObject(Object: T): Int = {
        if(objectId.contains(Object)){
            objectId.get(Object).get
        }else{
            -1
        }
    }

    def contains(id: Int): Boolean = {
        idObject.contains(id)
    }

    def contains(Object: T): Boolean = {
        objectId.contains(Object)
    }

    def contains(name: String): Boolean = {
        nameObject.contains(name)
    }
    
    
    
}