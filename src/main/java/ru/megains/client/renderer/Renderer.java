package ru.megains.client.renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import ru.megains.client.OrangeCraft;
import ru.megains.client.renderer.graph.Camera;
import ru.megains.client.renderer.graph.ShaderProgram;
import ru.megains.client.renderer.graph.Transformation;
import ru.megains.client.renderer.world.RenderChunk;
import ru.megains.client.renderer.world.WorldRenderer;
import ru.megains.common.block.blockdata.BlockPos;
import ru.megains.common.managers.TextureManager;
import ru.megains.common.util.Utils;
import scala.collection.Iterator;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;


public class Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private static final int MAX_POINT_LIGHTS = 5;

    private static final int MAX_SPOT_LIGHTS = 5;

    public final Transformation transformation;
    public ShaderProgram hudShaderProgram;
    public TextureManager textureManager;

    //  private ShaderProgram skyBoxShaderProgram;
    public int a;
    private ShaderProgram sceneShaderProgram;
    private OrangeCraft oc;

    public WorldRenderer worldRenderer;
    Frustum frustum;

    public Renderer(OrangeCraft cubeGame) {
        oc = cubeGame;
        transformation = new Transformation();
    }

    public void init(TextureManager textureManager) throws Exception {
        this.textureManager = textureManager;

        //  setupSkyBoxShader();



        setupSceneShader();
        setupHudShader();


        //  GL11.glClearDepth(1.0D);
        //    GL11.glDepthFunc(GL_LEQUAL);


        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }


//    private void setupSkyBoxShader() throws Exception {
//        skyBoxShaderProgram = new ShaderProgram();
//        skyBoxShaderProgram.createVertexShader(Utils.loadResource("/shaders/sb_vertex.vs"));
//        skyBoxShaderProgram.createFragmentShader(Utils.loadResource("/shaders/sb_fragment.fs"));
//        skyBoxShaderProgram.link();
//
//        // Create uniforms for projection matrix
//        skyBoxShaderProgram.createUniform("projectionMatrix");
//        skyBoxShaderProgram.createUniform("modelViewMatrix");
//        skyBoxShaderProgram.createUniform("texture_sampler");
//        skyBoxShaderProgram.createUniform("ambientLight");
//    }

    public void render(Camera camera, WorldRenderer worldRenderer) {


        transformation.updateProjectionMatrix(FOV, 800, 600, Z_NEAR, Z_FAR, camera);
        transformation.updateViewMatrix(camera);
        Matrix4f projectionMatrix = transformation.getProjectionMatrix();
        Matrix4f viewMatrix = transformation.getViewMatrix();
        FloatBuffer _proj = projectionMatrix.get(BufferUtils.createFloatBuffer(16));
        projectionMatrix.mul(viewMatrix);
        FloatBuffer _modl = viewMatrix.get(BufferUtils.createFloatBuffer(16));

        frustum = Frustum.getFrustum(_proj, _modl);

        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);

        //  glViewport(0, 0, 800, 600);
//        if (Display.isResized()) {
//
//            Display.setResized(false);
//        }

        // Update projection and view atrices once per render cycle

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        if (oc.world() != null) {
            renderScene();
        }



        // renderSkyBox(window, camera, scene);

        renderGui();


        Display.update();

    }

    private void setupSceneShader() throws Exception {
        // Create shader
        sceneShaderProgram = new ShaderProgram();
        sceneShaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        sceneShaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
        sceneShaderProgram.link();


        sceneShaderProgram.createUniform("projectionMatrix");
        sceneShaderProgram.createUniform("modelViewMatrix");
    }

    private void setupHudShader() throws Exception {
        hudShaderProgram = new ShaderProgram();
        hudShaderProgram.createVertexShader(Utils.loadResource("/shaders/hud_vertex.vs"));
        hudShaderProgram.createFragmentShader(Utils.loadResource("/shaders/hud_fragment.fs"));
        hudShaderProgram.link();

        // Create uniforms for Ortographic-model projection matrix and base colour
        hudShaderProgram.createUniform("projectionMatrix");
        hudShaderProgram.createUniform("modelMatrix");
        hudShaderProgram.createUniform("colour");
        hudShaderProgram.createUniform("hasTexture");
    }


//    private void renderSkyBox(Window window, Camera camera, Scene scene) {
//        SkyBox skyBox = scene.getSkyBox();
//        if (skyBox != null) {
//            skyBoxShaderProgram.bind();
//
//            skyBoxShaderProgram.setUniform("texture_sampler", 0);
//
//            Matrix4f projectionMatrix = transformation.getProjectionMatrix();
//            skyBoxShaderProgram.setUniform("projectionMatrix", projectionMatrix);
//            Matrix4f viewMatrix = transformation.getViewMatrix();
//            viewMatrix.m30 = 0;
//            viewMatrix.m31 = 0;
//            viewMatrix.m32 = 0;
//            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(skyBox, viewMatrix);
//            skyBoxShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
//            skyBoxShaderProgram.setUniform("ambientLight", scene.getSceneLight().getSkyBoxLight());
//
//            scene.getSkyBox().getMesh().render();
//
//            skyBoxShaderProgram.unbind();
//        }
//    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }




    private void renderScene() {
        sceneShaderProgram.bind();


        Matrix4f projectionMatrix = transformation.getProjectionMatrix();
        sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Render each mesh with the associated game Items
        glEnable(GL_CULL_FACE);
        //   glEnable(GL_BLEND);

        Matrix4f modelViewMatrix;
        RenderChunk.clearRend();

        //   Iterable<RenderChunk> renderChunks = worldRenderer.renderChunks().values();


        // Iterator<RenderChunk> iterable = renderChunks.iterator();
        Iterator<RenderChunk> iterable = worldRenderer.getRenderChunks(oc.player(), frustum).iterator();
        RenderChunk renderChunk;
        while (iterable.hasNext()) {
            renderChunk = iterable.next();
            //  if (frustum.cubeInFrustum(renderChunk.cube())) {
            modelViewMatrix = transformation.buildChunkModelViewMatrix(renderChunk.chunk().position());
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            renderChunk.render(0);
            //  }

        }


        // GL11.glDepthMask(true);
        // glDisable(GL_BLEND);

//       for(Entity entity: worldRenderer.world.mobEntity)  {
//
//           if ( frustum.cubeInFrustum(entity.body())){
//               modelViewMatrix = transformation.buildEntityModelViewMatrix(entity, viewMatrix);
//
//               sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
//               sceneShaderProgram.setUniform("normal", new Matrix3f(modelViewMatrix.invert()).transpose());
//
//               worldRenderer.renderEntity(entity);
//           }
//       }


//       //

//     //   glDisable(GL_DEPTH_TEST);
//

        worldRenderer.renderEntitiesItem(frustum, transformation);


        glDisable(GL_CULL_FACE);
        //  modelViewMatrix = transformation.buildTextModelViewMatrix(new BlockWorldPos(65, 65, 65));
        //  sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

        //  text.getMesh().render(sceneShaderProgram, textureManager);

        BlockPos bp = oc.blockSelectPosition();

        if (bp != null) {
            modelViewMatrix = transformation.buildBlockModelViewMatrix(bp);
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            worldRenderer.renderBlockMouseOver();
        }


        sceneShaderProgram.unbind();
    }


    private void renderGui() {

        glEnable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);

        hudShaderProgram.bind();
        Matrix4f ortho = transformation.getOrtho2DProjectionMatrix(0, 800, 0, 600);
        hudShaderProgram.setUniform("projectionMatrix", ortho);


        oc.guiManager().draw(Mouse.getX(), Mouse.getY());

        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glDisable(GL_CULL_FACE);
        hudShaderProgram.unbind();

    }


    public void cleanup() {

//        if (skyBoxShaderProgram != null) {
//            skyBoxShaderProgram.cleanup();
//        }
        if (sceneShaderProgram != null) {
            sceneShaderProgram.cleanup();
        }
        if (hudShaderProgram != null) {
            hudShaderProgram.cleanup();
        }
    }

}
