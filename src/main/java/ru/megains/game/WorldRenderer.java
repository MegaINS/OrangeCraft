//package ru.megains.game;
//
//
//import ru.megains.engine.graph.Material;
//import ru.megains.engine.graph.RenderChunk;
//import ru.megains.engine.graph.Texture;
//import ru.megains.renderer.texture.TextureLoader;
//import ru.megains.game.blockdata.BlockPos;
//import ru.megains.game.entity.Entity;
//import ru.megains.game.world.World;
//import ru.megains.game.world.chunk.Chunk;
//import ru.megains.game.world.chunk.ChunkVoid;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class WorldRenderer {
//
//    public World world;
//
//    public HashMap<Long,RenderChunk> renderChunks;
//
//    public WorldRenderer(World world) {
//        this.world = world;
//
//
//    }
//
//    public HashMap<Long, RenderChunk> getRenderChunks() {
//        return renderChunks;
//    }
//
//    public void init(){
//
//
//
//        RenderChunk.material = new Material(new Texture("/textures/grassblock.png"));
//        RenderChunk.material.texture.setId(TextureLoader.blockTexture());
//        RenderChunk.material.ambient.set(0.5f, 0.5f, 0.5f, 1.0f);
//        RenderChunk.material.diffuse.set(0.8f, 0.8f, 0.8f, 1.0f);
//        RenderChunk.material.specular.set(0.1f, 0.1f, 0.1f, 1.0f);
//        RenderChunk.material.emission.set(0f, 0f, 0f, 1.0f);
//        RenderChunk.material.shininess = 20.0f;
//
//        renderChunks = new HashMap<>();
//
//
//        for(Map.Entry<Object,Chunk> i: world.chunks().seq()){
//           renderChunks.put((Long) i.getKey(),new RenderChunk(i.getValue()));
//        }
//
//
//    }
//
//
//
//
//
//    public void cleanUp(){
//        renderChunks.values().forEach(RenderChunk::cleanUp);
//    }
//
//    public void reRender(BlockPos pos) {
//
//
//        int x = pos.x()>>4;
//        int y = pos.y()>>4;
//        int z = pos.z()>>4;
//        getRenderChunkRen(x,y,z).reRender();
//        getRenderChunkRen(x+1,y,z).reRender();
//        getRenderChunkRen(x-1,y,z).reRender();
//        getRenderChunkRen(x,y+1,z).reRender();
//        getRenderChunkRen(x,y-1,z).reRender();
//        getRenderChunkRen(x,y,z+1).reRender();
//        getRenderChunkRen(x,y,z-1).reRender();
//
//    }
//
//    public RenderChunk getRenderChunkRen(int x, int y, int z){
//
//        long i =(x & 16777215l) << 40 | (z & 16777215l) << 16 | (y & 65535L);
//        return renderChunks.containsKey(i)?renderChunks.get(i):voidRender;
//
//    }
//    public static RenderChunk voidRender = new RenderChunk(new ChunkVoid());
//
//
//
//    public void renderEntity(Entity entity) {
//
////        if(entity.mesh==null){
////            createEntityMesh(entity);
////        }
////
////        entity.mesh.render();
//
//    }
//    public void createEntityMesh(Entity entity){
//
////        AxisAlignedBB AABB = entity.cube;
////        float minX = AABB.getMinX();
////        float minY = AABB.getMinY();
////        float minZ = AABB.getMinZ();
////        float maxX = AABB.getMaxX();
////        float maxY = AABB.getMaxY();
////        float maxZ = AABB.getMaxZ();
////
////
////
////
////        Material material = new Material(new Texture(TextureLoader.blockTexture()) );
////
////        MeshMaker$ mm =MeshMaker$.MODULE$;
////        mm.startMakeTriangles();
////        mm.setCurrentIndex();
////
////        mm.addVertexWithUV(minX, minY, minZ, 0, 1);
////        mm.addVertexWithUV(minX, maxY, minZ, 0, 0);
////        mm.addVertexWithUV(maxX, maxY, minZ, 1, 0);
////        mm.addVertexWithUV(maxX, minY, minZ, 1, 1);
////        mm.addIndex(0);
////        mm.addIndex( 1 );
////        mm.addIndex( 2);
////        mm.addIndex(0);
////        mm.addIndex(2);
////        mm.addIndex(3);
////
////        entity.mesh = mm.makeMesh();
////        entity.mesh.setMaterial(material);
//
//
//    }
//
//    public void update() {
//        for(RenderChunk chunk:renderChunks.values()){
//            chunk.update();
//
//        }
//    }
//}
