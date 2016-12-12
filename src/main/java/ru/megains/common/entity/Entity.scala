package ru.megains.common.entity


import org.joml.Vector3d
import ru.megains.common.physics.AxisAlignedBB
import ru.megains.common.util.{MathHelper, RayTraceResult}
import ru.megains.common.world.World
import ru.megains.nbt.NBTType
import ru.megains.nbt.tag.{NBTCompound, NBTList}

import scala.collection.mutable


abstract class Entity(var world: World, val height: Float, val wight: Float, val levelView: Float) {

    val body: AxisAlignedBB = new AxisAlignedBB()

    var posX: Double = .0
    var posY: Double = .0
    var posZ: Double = .0
    var prevPosX: Double = .0
    var prevPosY: Double = .0
    var prevPosZ: Double = .0

    var motionX: Double = .0
    var motionY: Double = .0
    var motionZ: Double = .0
    var goY: Float = 0.5f

    var rotationYaw: Float = 0
    var rotationPitch: Float = 0
    var prevRotationYaw: Float = .0f
    var prevRotationPitch: Float = .0f

    var speed: Float = 50
    var onGround: Boolean = false

    def setPosition(x: Double, y: Double, z: Double) {

        posX = x
        posY = y
        posZ = z
        val i = wight / 2
        body.set(x - i toFloat, y toFloat, z - i toFloat, x + i toFloat, y + height toFloat, z + i toFloat)
    }

    def setWorld(worldIn: World) {
        world = worldIn
    }

    def update()

    def isSneaking: Boolean = false

    def move(x: Double, y: Double, z: Double) {
        var x0: Float = x toFloat
        var z0: Float = z toFloat
        var y0: Float = y toFloat
        var x1: Float = x toFloat
        var z1: Float = z toFloat
        var y1: Float = y toFloat

        val bodyCopy: AxisAlignedBB = body.getCopy
        var aabbs: mutable.ArrayBuffer[AxisAlignedBB] = world.addBlocksInList(body.expand(x0, y0, z0))




        aabbs.foreach((aabb: AxisAlignedBB) => {
            y0 = aabb.checkYcollision(body, y0)
        })
        body.move(0, y0, 0)

        aabbs.foreach((aabb: AxisAlignedBB) => {
            x0 = aabb.checkXcollision(body, x0)
        })
        body.move(x0, 0, 0)

        aabbs.foreach((aabb: AxisAlignedBB) => {
            z0 = aabb.checkZcollision(body, z0)
        })
        body.move(0, 0, z0)

        onGround = y != y0 && y < 0.0F
        if (onGround && (Math.abs(x) > Math.abs(x0) || Math.abs(z) > Math.abs(z0))) {

            aabbs = world.addBlocksInList(bodyCopy.expand(x1, goY, z1))

            aabbs.foreach((aabb: AxisAlignedBB) => {
                y1 = aabb.checkYcollision(body, goY)
            })
            body.move(0, y1, 0)

            aabbs.foreach((aabb: AxisAlignedBB) => {
                x1 = aabb.checkXcollision(body, x1)
            })
            body.move(x1, 0, 0)

            aabbs.foreach((aabb: AxisAlignedBB) => {
                z1 = aabb.checkZcollision(body, z1)
            })
            body.move(0, 0, z1)

            if (Math.abs(x1) > Math.abs(x0) || Math.abs(z1) > Math.abs(z0)) {
                body.set(bodyCopy)
            }
        }
        if (x0 != x & x1 != x) {
            motionX = 0.0F
        }
        if (y0 != y) {
            motionY = 0.0F
        }
        if (z0 != z & z1 != z) {
            motionZ = 0.0F
        }

        posX = body.getCenterX
        posY = body.getMinY
        posZ = body.getCenterZ

    }

    def moveFlying(x: Float, z: Float, limit: Float) {
        var dist: Float = x * x + z * z
        if (dist >= 1.0E-4F) {
            dist = MathHelper.sqrt_float(dist)
            if (dist < 1.0F) {
                dist = 1.0F
            }
            dist = limit / dist * speed
            val x1 = dist * x
            val z1 = dist * z
            val f4: Float = MathHelper.sin(rotationYaw * Math.PI.toFloat / 180.0F)
            val f5: Float = MathHelper.cos(rotationYaw * Math.PI.toFloat / 180.0F)
            motionX += (x1 * f5 - z1 * f4)
            motionZ += (z1 * f5 + x1 * f4)
        }
    }

    def rayTrace(blockReachDistance: Float, partialTicks: Float): RayTraceResult = {

        val vec3d = new Vector3d(posX, posY + levelView, posZ)
        val vec3d1: Vector3d = getLook(partialTicks).mul(blockReachDistance).add(vec3d)
        world.rayTraceBlocks(vec3d, vec3d1, false, false, true)
    }

    def getLook(partialTicks: Float): Vector3d = {
        if (partialTicks == 1.0F) {
            getVectorForRotation(rotationPitch, rotationYaw)
        }
        else {
            val f: Float = rotationPitch
            val f1: Float = rotationYaw
            getVectorForRotation(f, f1)
        }
    }

    def getVectorForRotation(pitch: Float, yaw: Float): Vector3d = {
        val f: Float = MathHelper.cos(-yaw * 0.017453292F - Math.PI.toFloat)
        val f1: Float = MathHelper.sin(-yaw * 0.017453292F - Math.PI.toFloat)
        val f2: Float = MathHelper.cos(-pitch * 0.017453292F)
        val f3: Float = MathHelper.sin(-pitch * 0.017453292F)
        new Vector3d(f1 * f2, f3, f * f2)
    }

    def setPositionAndRotation(x: Double, y: Double, z: Double, yaw: Float, pitchIn: Float) {
        posX = MathHelper.clamp_double(x, -3.0E7D, 3.0E7D)
        posY = y
        posZ = MathHelper.clamp_double(z, -3.0E7D, 3.0E7D)
        prevPosX = posX
        prevPosY = posY
        prevPosZ = posZ
        val pitch = MathHelper.clamp_float(pitchIn, -90.0F, 90.0F)
        rotationYaw = yaw
        rotationPitch = pitch
        prevRotationYaw = rotationYaw
        prevRotationPitch = rotationPitch
        val d0: Double = (prevRotationYaw - yaw).toDouble
        if (d0 < -180.0D) prevRotationYaw += 360.0F
        if (d0 >= 180.0D) prevRotationYaw -= 360.0F
        setPosition(posX, posY, posZ)
        setRotation(yaw, pitch)
    }

    protected def setRotation(yaw: Float, pitch: Float) {
        rotationYaw = yaw % 360.0F
        rotationPitch = pitch % 360.0F
    }

    def readFromNBT(compound: NBTCompound) {

        val listPos: NBTList = compound.getList("Pos")
        val listMotion: NBTList = compound.getList("Motion")
        val listRotation: NBTList = compound.getList("Rotation")
        motionX = listMotion.getDouble(0)
        motionY = listMotion.getDouble(1)
        motionZ = listMotion.getDouble(2)
        if (Math.abs(motionX) > 10.0D) motionX = 0.0D
        if (Math.abs(motionY) > 10.0D) motionY = 0.0D
        if (Math.abs(motionZ) > 10.0D) motionZ = 0.0D
        posX = listPos.getDouble(0)
        posY = listPos.getDouble(1)
        posZ = listPos.getDouble(2)

        prevPosX = posX
        prevPosY = posY
        prevPosZ = posZ
        rotationYaw = listRotation.getFloat(0)
        rotationPitch = listRotation.getFloat(1)
        prevRotationYaw = rotationYaw
        prevRotationPitch = rotationPitch

        onGround = compound.getBoolean("OnGround")

        setPosition(posX, posY, posZ)
        setRotation(rotationYaw, rotationPitch)

        //    lastTickPosX = posX
        //   lastTickPosY = posY
        //   lastTickPosZ = posZ
        //  setRotationYawHead(rotationYaw)
        //   setRenderYawOffset(rotationYaw)
        //   fallDistance = compound.getFloat("FallDistance")
        //    fire = compound.getShort("Fire")
        //   setAir(compound.getShort("Air"))
        //      if (compound.hasKey("Dimension")) dimension = compound.getInteger("Dimension")
        //      invulnerable = compound.getBoolean("Invulnerable")
        //     timeUntilPortal = compound.getInteger("PortalCooldown")
        //            if (compound.hasUniqueId("UUID")) {
        //                entityUniqueID = compound.getUniqueId("UUID")
        //                cachedUniqueIdString = entityUniqueID.toString
        //            }
        //   if (compound.hasKey("CustomName", 8)) setCustomNameTag(compound.getString("CustomName"))
        //            setAlwaysRenderNameTag(compound.getBoolean("CustomNameVisible"))
        //            cmdResultStats.readStatsFromNBT(compound)
        //            setSilent(compound.getBoolean("Silent"))
        //            func_189654_d(compound.getBoolean("NoGravity"))
        //            setGlowing(compound.getBoolean("Glowing"))
        //            if (compound.hasKey("ForgeData")) customEntityData = compound.getCompoundTag("ForgeData")
        //            if (capabilities != null && compound.hasKey("ForgeCaps")) capabilities.deserializeNBT(compound.getCompoundTag("ForgeCaps"))
        //            if (compound.hasKey("Tags", 9)) {
        //                tags.clear()
        //                val nbttaglist1: NBTTagList = compound.getTagList("Tags", 8)
        //                val i: Int = Math.min(nbttaglist1.tagCount, 1024)
        //                var j: Int = 0
        //                while (j < i) {
        //                    {
        //                        tags.add(nbttaglist1.getStringTagAt(j))
        //                    }
        //                    {
        //                        j += 1;
        //                        j
        //                    }
        //                }
        //            }
        //            readEntityFromNBT(compound)
        //            if (shouldSetPosAfterLoading) setPosition(posX, posY, posZ)
        //        } catch {
        //            case throwable: Throwable => {
        //                val crashreport: CrashReport = CrashReport.makeCrashReport(throwable, "Loading entity NBT")
        //                val crashreportcategory: CrashReportCategory = crashreport.makeCategory("Entity being loaded")
        //                addEntityCrashInfo(crashreportcategory)
        //                throw new ReportedException(crashreport)
        //            }
        //        }
    }

    def writeToNBT(compound: NBTCompound): Unit = {

        val listPos: NBTList = compound.createList("Pos", NBTType.EnumNBTDouble)
        listPos.setValue(posX)
        listPos.setValue(posY)
        listPos.setValue(posZ)

        val listMotion: NBTList = compound.createList("Motion", NBTType.EnumNBTDouble)
        listMotion.setValue(motionX)
        listMotion.setValue(motionY)
        listMotion.setValue(motionZ)

        val listRotation: NBTList = compound.createList("Rotation", NBTType.EnumNBTFloat)
        listRotation.setValue(rotationYaw)
        listRotation.setValue(rotationPitch)

        compound.setValue("OnGround", onGround)

        //   compound.setFloat("FallDistance", this.fallDistance)
        //   compound.setShort("Fire", this.fire.toShort)
        // compound.setShort("Air", this.getAir.toShort)

        //   compound.setInteger("Dimension", this.dimension)
        //  compound.setBoolean("Invulnerable", this.invulnerable)
        //  compound.setInteger("PortalCooldown", this.timeUntilPortal)
        //   compound.setUniqueId("UUID", this.getUniqueID)
        //  if (this.getCustomNameTag != null && !this.getCustomNameTag.isEmpty) p_189511_1_.setString("CustomName", this.getCustomNameTag)
        //  if (this.getAlwaysRenderNameTag) p_189511_1_.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag)
        //   this.cmdResultStats.writeStatsToNBT(p_189511_1_)
        //  if (this.isSilent) p_189511_1_.setBoolean("Silent", this.isSilent)
        //  if (this.func_189652_ae) p_189511_1_.setBoolean("NoGravity", this.func_189652_ae)
        // if (this.glowing) p_189511_1_.setBoolean("Glowing", this.glowing)
        //        if (this.tags.size > 0) {
        //            val nbttaglist = new NBTTagList
        //            import scala.collection.JavaConversions._
        //            for (s <- this.tags) {
        //                nbttaglist.appendTag(new NBTTagString(s))
        //            }
        //            p_189511_1_.setTag("Tags", nbttaglist)
        //        }
        //  if (customEntityData != null) p_189511_1_.setTag("ForgeData", customEntityData)
        // if (this.capabilities != null) p_189511_1_.setTag("ForgeCaps", this.capabilities.serializeNBT)
        //writeEntityToNBT(p_189511_1_)
        //        if (this.isBeingRidden) {
        //            val nbttaglist1 = new NBTTagList
        //            import scala.collection.JavaConversions._
        //            for (entity <- this.getPassengers) {
        //                val nbttagcompound = new NBTTagCompound
        //                if (entity.writeToNBTAtomically(nbttagcompound)) nbttaglist1.appendTag(nbttagcompound)
        //            }
        //            if (!nbttaglist1.hasNoTags) p_189511_1_.setTag("Passengers", nbttaglist1)
        //        }
    }
}
