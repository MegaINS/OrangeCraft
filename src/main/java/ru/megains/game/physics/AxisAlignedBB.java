package ru.megains.game.physics;


import org.joml.Vector3f;
import ru.megains.game.blockdata.BlockDirection;
import ru.megains.game.util.RayTraceResult;


public class AxisAlignedBB {
    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;

    public AxisAlignedBB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public AxisAlignedBB() {
        this(0, 0, 0, 0, 0, 0);
    }


    public void set(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public void set(AxisAlignedBB aabb) {
        this.minX = aabb.minX;
        this.minY = aabb.minY;
        this.minZ = aabb.minZ;
        this.maxX = aabb.maxX;
        this.maxY = aabb.maxY;
        this.maxZ = aabb.maxZ;
    }

    public AxisAlignedBB getCopy() {
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);

    }


    public AxisAlignedBB move(float x, float y, float z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }

    public AxisAlignedBB sum(float x, float y, float z) {
        return new AxisAlignedBB(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z);

    }


    public AxisAlignedBB expand(float x, float y, float z) {
        float x0 = minX;
        float y0 = minY;
        float z0 = minZ;
        float x1 = maxX;
        float y1 = maxY;
        float z1 = maxZ;
        if (x > 0.0) {
            x1 += x;
        } else {
            x0 += x;
        }
        if (y > 0.0) {
            y1 += y;
        } else {
            y0 += y;
        }
        if (z > 0.0) {
            z1 += z;
        } else {
            z0 += z;
        }
        return new AxisAlignedBB(x0, y0, z0, x1, y1, z1);
    }


    public boolean checkCollision(AxisAlignedBB aabb) {


        if (minY < aabb.getMaxY() && maxY > aabb.getMinY()) {
            if (minX < aabb.getMaxX() && maxX > aabb.getMinX()) {
                if (minZ < aabb.getMaxZ() && maxZ > aabb.getMinZ()) {
                    return true;
                }

            }
        }
        return false;
    }


    public float checkXcollision(AxisAlignedBB aabb, float x) {

        if (minY < aabb.getMaxY() && maxY > aabb.getMinY()) {
            if (minZ < aabb.getMaxZ() && maxZ > aabb.getMinZ()) {
                float max;
                if (x > 0.0 && minX >= aabb.getMaxX() - x) {
                    max = minX - aabb.getMaxX();
                    if (max < x) {
                        x = max;
                    }
                }
                if (x < 0.0 && maxX <= aabb.getMinX() - x) {
                    max = maxX - aabb.getMinX();
                    if (max > x) {
                        x = max;
                    }
                }
            }
        }
        return x;
    }

    public float checkYcollision(AxisAlignedBB aabb, float y) {
        if (minX < aabb.getMaxX() && maxX > aabb.getMinX()) {
            if (minZ < aabb.getMaxZ() && maxZ > aabb.getMinZ()) {
                float max;
                if (y > 0.0 && minY >= aabb.getMaxY()) {
                    max = minY - aabb.getMaxY();
                    if (max < y) {
                        y = max;
                    }
                }
                if (y < 0.0 && maxY <= aabb.getMinY()) {
                    max = maxY - aabb.getMinY();
                    if (max > y) {
                        y = max;
                    }
                }
            }
        }


        return y;
    }

    public float checkZcollision(AxisAlignedBB aabb, float z) {

        if (minY < aabb.getMaxY() && maxY > aabb.getMinY()) {
            if (minX < aabb.getMaxX() && maxX > aabb.getMinX()) {
                float max;
                if (z > 0.0 && minZ >= aabb.getMaxZ() - z) {
                    max = minZ - aabb.getMaxZ();
                    if (max < z) {
                        z = max;
                    }
                }
                if (z < 0.0 && maxZ <= aabb.getMinZ() - z) {
                    max = maxZ - aabb.getMinZ();
                    if (max > z) {
                        z = max;
                    }
                }


            }
        }
        return z;
    }

    public RayTraceResult calculateIntercept(Vector3f vecA, Vector3f vecB) {
        Vector3f vec3d = this.func_186671_a(this.minX, vecA, vecB);
        BlockDirection enumfacing = BlockDirection.WEST();
        Vector3f vec3d1 = this.func_186671_a(this.maxX, vecA, vecB);

        if (vec3d1 != null && this.func_186661_a(vecA, vec3d, vec3d1)) {
            vec3d = vec3d1;
            enumfacing = BlockDirection.EAST();
        }

        vec3d1 = this.func_186663_b(this.minY, vecA, vecB);

        if (vec3d1 != null && this.func_186661_a(vecA, vec3d, vec3d1)) {
            vec3d = vec3d1;
            enumfacing = BlockDirection.DOWN();
        }

        vec3d1 = this.func_186663_b(this.maxY, vecA, vecB);

        if (vec3d1 != null && this.func_186661_a(vecA, vec3d, vec3d1)) {
            vec3d = vec3d1;
            enumfacing = BlockDirection.UP();
        }


        vec3d1 = this.func_186665_c(this.minZ, vecA, vecB);

        if (vec3d1 != null && this.func_186661_a(vecA, vec3d, vec3d1)) {
            vec3d = vec3d1;
            enumfacing = BlockDirection.NORTH();
        }

        vec3d1 = this.func_186665_c(this.maxZ, vecA, vecB);

        if (vec3d1 != null && this.func_186661_a(vecA, vec3d, vec3d1)) {
            vec3d = vec3d1;
            enumfacing = BlockDirection.SOUTH();
        }


        return vec3d == null ? null : new RayTraceResult(vec3d, enumfacing);
    }

    boolean func_186661_a(Vector3f p_186661_1_, Vector3f p_186661_2_, Vector3f p_186661_3_) {
        return p_186661_2_ == null || p_186661_1_.distanceSquared(p_186661_3_) < p_186661_1_.distanceSquared(p_186661_2_);
    }

    Vector3f func_186671_a(float p_186671_1_, Vector3f p_186671_3_, Vector3f p_186671_4_) {
        Vector3f vec3d = p_186671_3_.getIntermediateWithXValue(p_186671_4_, p_186671_1_);
        return vec3d != null && this.intersectsWithYZ(vec3d) ? vec3d : null;
    }

    Vector3f func_186663_b(float p_186663_1_, Vector3f p_186663_3_, Vector3f p_186663_4_) {
        Vector3f vec3d = p_186663_3_.getIntermediateWithYValue(p_186663_4_, p_186663_1_);
        return vec3d != null && this.intersectsWithXZ(vec3d) ? vec3d : null;
    }

    Vector3f func_186665_c(float p_186665_1_, Vector3f p_186665_3_, Vector3f p_186665_4_) {
        Vector3f vec3d = p_186665_3_.getIntermediateWithZValue(p_186665_4_, p_186665_1_);
        return vec3d != null && this.intersectsWithXY(vec3d) ? vec3d : null;
    }

    public boolean intersectsWithYZ(Vector3f vec) {
        return vec.y >= this.minY && vec.y <= this.maxY && vec.z >= this.minZ && vec.z <= this.maxZ;
    }

    public boolean intersectsWithXZ(Vector3f vec) {
        return vec.x >= this.minX && vec.x <= this.maxX && vec.z >= this.minZ && vec.z <= this.maxZ;
    }

    public boolean intersectsWithXY(Vector3f vec) {
        return vec.x >= this.minX && vec.x <= this.maxX && vec.y >= this.minY && vec.y <= this.maxY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMinZ() {
        return minZ;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMaxZ() {
        return maxZ;
    }

    public float getCenterX() {
        return (maxX + minX) / 2;
    }

    public float getCenterZ() {
        return (maxZ + minZ) / 2;
    }
}
