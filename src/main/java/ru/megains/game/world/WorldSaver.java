package ru.megains.game.world;


import ru.megains.engine.graph.RenderChunk;
import ru.megains.game.position.ChunkPosition;
import ru.megains.game.world.chunk.Chunk;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

public class WorldSaver {

    private static final String WORLD_DIRECTORY = "src/world/world.dat";
    public static boolean load(byte[] world){

        try {
            DataInputStream e = new DataInputStream(new GZIPInputStream(new FileInputStream(new File(WORLD_DIRECTORY))));
            e.readFully(world);

            e.close();
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;

        }
        return true;
    }

    public static void save(HashMap<Long, Chunk> world) {

        byte[] save = new byte[sizeWorld];

        ByteBuffer worldBuf = ByteBuffer.allocate(sizeWorld);



        for (int x =-2;x<2;++x){
            for (int y =-2;y<2;++y){
                for (int z =-2;z<2;++z){

                  //  world.get(getIndex(x,y,z)).save(worldBuf);

                }
            }
        }



    }
    public static int sizeWorld;

    public static boolean load(HashMap<Long, Chunk> chunks, int size,World world) {
        sizeWorld = size*8;
        byte[] load = new byte[sizeWorld];

         if(load(load)) {
             ByteBuffer worldBuf=  ByteBuffer.allocate(4096);

             int i=0;
             for (int x =-2;x<2;++x){
                 for (int y =-2;y<2;++y){
                     for (int z =-2;z<2;++z){
                        worldBuf.clear();
                         worldBuf.put(load,i,4096);

                         i+=4096;

                         chunks.put(getIndex(x,y,z),new Chunk(world,new ChunkPosition(x* RenderChunk.chunkSize,y* RenderChunk.chunkSize,z* RenderChunk.chunkSize),worldBuf.array()));


                     }
                 }
             }

         }else {
             byte[] wor = new byte[4096];
             for (int x = -2; x < 2  ; ++x) {
                 for (int y = -2; y < 2  ; ++y) {
                     for (int z = -2; z < 2 ; ++z) {

                         for(int i = 0;i<wor.length;i++){
                             if(y<0){
                                 wor[i] = 1;
//                                 float j = (float) Math.random();
//                                 if(j<0.3f){
//                                     wor[i] = 0;
//                                 }else if(j<0.6f){
//                                     wor[i] = 1;
//                                 }else {
//                                     wor[i] = 2;
//                                 }

                             }else {
                                 wor[i] = 0;
                             }

                         }



                         chunks.put(getIndex(x, y, z), new Chunk(world, new ChunkPosition(x * RenderChunk.chunkSize, y * RenderChunk.chunkSize, z * RenderChunk.chunkSize),wor ));


                     }
                 }
             }
         }

        return true;
    }
    public static long getIndex(long x, long y, long z){
       return   (x & 16777215l) << 40 |  (z & 16777215l) << 16  | (y & 65535L);
    }
}
