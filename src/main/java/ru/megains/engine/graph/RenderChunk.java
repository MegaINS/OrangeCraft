package ru.megains.engine.graph;


import ru.megains.engine.graph.renderer.mesh.Mesh;
import ru.megains.engine.graph.renderer.mesh.MeshMaker;
import ru.megains.engine.graph.renderer.texture.TextureManager;
import ru.megains.game.blockdata.BlockWorldPos;
import ru.megains.game.multiblock.AMultiBlock;
import ru.megains.game.physics.AxisAlignedBB;
import ru.megains.game.world.World;
import ru.megains.game.world.chunk.Chunk;

public class RenderChunk {

    private static int chunkSize = 16;
    private static int rend = 0;
    public static int chunkRender = 0;
    public static int chunkUpdate = 0;
    private boolean isReRender = true;
    public final Chunk chunk;
    private AxisAlignedBB cube;
    private int blockRender;
    private Mesh[] meshes = new Mesh[2];
    private World world;
    private int minX;
    private int minY;
    private int minZ;
    private TextureManager textureManager;


    public RenderChunk(Chunk chunk, TextureManager textureManager) {
        this.textureManager = textureManager;
        this.chunk = chunk;
        this.world = chunk.world();
        minX = chunk.position().minX();
        minY = chunk.position().minY();
        minZ = chunk.position().minZ();
        cube = new AxisAlignedBB(minX, minY, minZ, chunk.position().maxX(), chunk.position().maxY(), chunk.position().maxZ());
    }


    public void render(int layer, ShaderProgram shaderProgram) {
        if (isReRender) {
            if (rend < 1) {
                rend++;
                blockRender = 0;
                cleanUp();
                reRenderChunk(0);
                isReRender = false;
                chunkUpdate++;
            }
        }
        renderChunk(layer, shaderProgram);
    }

    private void renderChunk(int layer, ShaderProgram shaderProgram) {

        if (blockRender != 0) {
            if (meshes[layer] != null) {
                meshes[layer].render(shaderProgram, textureManager);
                chunkRender++;
            }

        }
    }

    private void reRenderChunk(int layer) {


        MeshMaker.startMakeTriangles();
        MeshMaker.setTexture(TextureManager.locationBlockTexture());
        BlockWorldPos blockPos;
        BlockWorldPos renderPos;
        AMultiBlock block;


        for (int x = 0; x < chunkSize; ++x) {
            for (int y = 0; y < chunkSize; ++y) {
                for (int z = 0; z < chunkSize; ++z) {
                    blockPos = new BlockWorldPos(x + minX, y + minY, z + minZ);
                    renderPos = new BlockWorldPos(x, y, z);
                    if (!world.isAirBlock(blockPos)) {
                        block = chunk.getBlockWorldCord(blockPos);
                        blockRender += block.renderBlocks(world, blockPos, renderPos);
                    }
                }
            }
        }

        meshes[layer] = MeshMaker.makeMesh();
    }


    public void cleanUp() {
        for (Mesh mesh : meshes) {
            if (mesh != null) {
                mesh.cleanUp();
            }
        }

    }


    public void reRender() {

        isReRender = true;
    }

    static void clearRend() {
        rend = 0;
    }

    AxisAlignedBB getCube() {
        return cube;
    }


}
