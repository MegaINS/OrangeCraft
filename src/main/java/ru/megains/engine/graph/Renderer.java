package ru.megains.engine.graph;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import ru.megains.engine.Frustum;
import ru.megains.engine.Utils;
import ru.megains.engine.Window;
import ru.megains.engine.graph.renderer.mesh.Mesh;
import ru.megains.engine.graph.renderer.texture.TextureManager;
import ru.megains.engine.graph.text.IHud;
import ru.megains.engine.graph.text.Text;
import ru.megains.game.OrangeCraft;
import ru.megains.game.blockdata.BlockWorldPos;
import ru.megains.game.util.BlockAndPos;

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


    private ShaderProgram sceneShaderProgram;


    public ShaderProgram hudShaderProgram;
    public TextureManager textureManager;

    //  private ShaderProgram skyBoxShaderProgram;

    private final float specularPower;

    private OrangeCraft cubeGame;


    public Renderer(OrangeCraft cubeGame) {
        this.cubeGame = cubeGame;
        transformation = new Transformation();
        specularPower = 10f;
    }

    Text text;

    public void init(Window window, TextureManager textureManager) throws Exception {
        this.textureManager = textureManager;


        //  setupSkyBoxShader();

        text = new Text("Hello");

        setupSceneShader();
        setupHudShader();


        //  GL11.glClearDepth(1.0D);
        //    GL11.glDepthFunc(GL_LEQUAL);


        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }


    public void render(Window window, Camera camera, IHud hud, WorldRenderer worldRenderer) {


        transformation.updateProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR, camera);
        transformation.updateViewMatrix(camera);
        Matrix4f projectionMatrix = transformation.getProjectionMatrix();
        Matrix4f viewMatrix = transformation.getViewMatrix();
        FloatBuffer _proj = projectionMatrix.get(BufferUtils.createFloatBuffer(16));
        projectionMatrix.mul(viewMatrix);
        FloatBuffer _modl = viewMatrix.get(BufferUtils.createFloatBuffer(16));

        Frustum frustum = Frustum.getFrustum(_proj, _modl);

        glEnable(GL_CULL_FACE);


        glViewport(0, 0, window.getWidth(), window.getHeight());
        if (window.isResized()) {

            window.setResized(false);
        }

        // Update projection and view atrices once per render cycle
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        renderScene(window, camera, worldRenderer, frustum);


        //   renderSkyBox(window, camera, scene);

        renderHud(window, hud);


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

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
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


    public int a;

    public void renderScene(Window window, Camera camera,/*, Scene scene*/WorldRenderer worldRenderer, Frustum frustum) {
        sceneShaderProgram.bind();


        Matrix4f projectionMatrix = transformation.getProjectionMatrix();
        sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Render each mesh with the associated game Items
        glEnable(GL_CULL_FACE);
        //   glEnable(GL_BLEND);

        Matrix4f modelViewMatrix;
        RenderChunk.clearRend();
        scala.collection.Iterable<RenderChunk> renderChunks = worldRenderer.renderChunks().values();


        scala.collection.Iterator<RenderChunk> iterable = renderChunks.iterator();
        RenderChunk renderChunk;
        while (iterable.hasNext()) {
            renderChunk = iterable.next();


            if (frustum.cubeInFrustum(renderChunk.getCube())) {
                modelViewMatrix = transformation.buildChunkModelViewMatrix(renderChunk.chunk.position());

                sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

                renderChunk.render(0, sceneShaderProgram);
            }

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

        worldRenderer.renderEntitiesItem(frustum, transformation, sceneShaderProgram);


        glDisable(GL_CULL_FACE);
        modelViewMatrix = transformation.buildTextModelViewMatrix(new BlockWorldPos(65, 65, 65));
        sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

        text.getMesh().render(sceneShaderProgram, textureManager);

        BlockAndPos bp = OrangeCraft.megaGame.blockAndPos;

        if (bp != null) {
            modelViewMatrix = transformation.buildBlockModelViewMatrix(bp.pos());
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            worldRenderer.renderBlockBounds(sceneShaderProgram);
        }


        sceneShaderProgram.unbind();
    }


    private void renderHud(Window window, IHud hud) {
        if (hud != null) {
            glEnable(GL_BLEND);
            glDisable(GL_CULL_FACE);
            glDisable(GL_DEPTH_TEST);
            hudShaderProgram.bind();


            Matrix4f ortho = transformation.getOrtho2DProjectionMatrix(0, window.getWidth(), 0, window.getHeight());
            hudShaderProgram.setUniform("projectionMatrix", ortho);

            for (Text gameItem : hud.getGameItems().values()) {
                Mesh mesh = gameItem.getMesh();

                Matrix4f projModelMatrix = transformation.buildOrtoProjModelMatrix(gameItem);
                hudShaderProgram.setUniform("modelMatrix", projModelMatrix);
                mesh.render(hudShaderProgram, textureManager);
            }

            glEnable(GL_CULL_FACE);

            cubeGame.guiManager.render();

            if (cubeGame.guiScreen != null) {
                cubeGame.guiScreen.render(this);
            }

            glEnable(GL_DEPTH_TEST);
            glDisable(GL_BLEND);

            //  glEnable(GL_DEPTH_TEST);


            hudShaderProgram.unbind();


        }
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
