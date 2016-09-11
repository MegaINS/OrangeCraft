package ru.megains.game.util;


import org.joml.Vector3f;
import ru.megains.game.block.Block;
import ru.megains.game.blockdata.BlockDirection;
import ru.megains.game.blockdata.BlockWorldPos;

public class RayTraceResult {
    /**
     * Used to determine what sub-segment is hit
     */
    public int subHit = -1;

    /**
     * Used to add extra hit info
     */
    public Object hitInfo = null;
    /**
     * What type of ray trace hit was this? 0 = block, 1 = entity
     */
    public RayTraceResult.Type typeOfHit;
    public BlockDirection sideHit;
    /**
     * The vector position of the hit
     */
    public Vector3f hitVec;
    public Block block;
    private BlockWorldPos blockPos;

    /**
     * The hit entity
     */
    // public Entity entityHit;
    public RayTraceResult(Vector3f hitVecIn, BlockDirection sideHitIn, BlockWorldPos blockPosIn, Block block) {
        this(RayTraceResult.Type.BLOCK, hitVecIn, sideHitIn, blockPosIn, block);
    }

    public RayTraceResult(Vector3f hitVecIn, BlockDirection sideHitIn) {
        this.sideHit = sideHitIn;
        this.hitVec = hitVecIn;
        //   this(RayTraceResult.Type.BLOCK, hitVecIn, sideHitIn, null,null);
    }

//    public RayTraceResult(Entity entityIn)
//    {
//        this(entityIn, new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ));
//    }

    public RayTraceResult(RayTraceResult.Type typeIn, Vector3f hitVecIn, BlockDirection sideHitIn, BlockWorldPos blockPosIn, Block block) {
        this.typeOfHit = typeIn;
        this.blockPos = blockPosIn;
        this.sideHit = sideHitIn;
        this.hitVec = new Vector3f(Math.abs((hitVecIn.x % 1 + 1f) % 1), Math.abs((hitVecIn.y % 1 + 1f) % 1), Math.abs((hitVecIn.z % 1 + 1f) % 1));

        if (sideHit == BlockDirection.UP() && hitVec.y == 0.0) {
            hitVec.add(0, 1, 0);
        }
        if (sideHit == BlockDirection.EAST() && hitVec.x == 0.0) {
            hitVec.add(1, 0, 0);
        }
        if (sideHit == BlockDirection.SOUTH() && hitVec.z == 0.0) {
            hitVec.add(0, 0, 1);
        }

        this.block = block;
    }

//    public RayTraceResult(Entity entityHitIn, Vec3d hitVecIn)
//    {
//        this.typeOfHit = RayTraceResult.Type.ENTITY;
//        this.entityHit = entityHitIn;
//        this.hitVec = hitVecIn;
//    }

    public BlockWorldPos getBlockWorldPos() {
        return this.blockPos;
    }


    public static enum Type {
        MISS,
        BLOCK,
        ENTITY;
    }
}