package ru.megains.engine.graph;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import ru.megains.engine.Frustum;
import ru.megains.engine.Utils;
import ru.megains.engine.Window;
import ru.megains.engine.graph.light.DirectionalLight;
import ru.megains.engine.graph.light.PointLight;
import ru.megains.engine.graph.renderer.mesh.Mesh;
import ru.megains.engine.graph.renderer.texture.TextureManager;
import ru.megains.engine.graph.text.IHud;
import ru.megains.engine.graph.text.Text;
import ru.megains.game.CubeGame;
import ru.megains.game.blockdata.BlockWorldPos;
import ru.megains.game.util.BlockAndPos;
import ru.megains.game.world.WorldRenderer;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_COMPARE_MODE;
import static org.lwjgl.opengl.GL30.*;


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

    private ShadowMap shadowMap;

    private ShaderProgram depthShaderProgram;

    private ShaderProgram sceneShaderProgram;

   private PointLight pointLight;
    private DirectionalLight directionalLight;
    public ShaderProgram hudShaderProgram;
    public TextureManager textureManager;

  //  private ShaderProgram skyBoxShaderProgram;

    private final float specularPower;

    private CubeGame cubeGame;

    public Renderer(CubeGame cubeGame) {
        this.cubeGame = cubeGame;
        transformation = new Transformation();
        specularPower = 10f;
    }

    Text text ;
    public void init(Window window, TextureManager textureManager) throws Exception {
        this.textureManager = textureManager;
        shadowMap = new ShadowMap();

        setupDepthShader();
      //  setupSkyBoxShader();

        text = new Text("Hello");

        setupSceneShader();
        setupHudShader();

//        pointLight = new PointLight();
//        pointLight.position.set(8f, 8f, 8f, 1.0f);
//        pointLight.ambient.set(0.5f, 0.5f, 0.5f, 1.0f);
//        pointLight.diffuse.set(0.8f, 0.8f, 0.8f, 1.0f);
//        pointLight.specular.set(0.5f, 0.5f, 0.5f, 1.0f);
//        pointLight.attenuation.set(0.5f, 0.0f, 0.1f);


        directionalLight = new DirectionalLight();
        directionalLight.position.set(0, 500, 0, 1.0f);
        directionalLight.ambient.set(1.0f, 1.0f, 1.0f, 1.0f);
        directionalLight.diffuse.set(1.0f, 1.0f, 1.0f, 1.0f);
        directionalLight.specular.set(0.1f, 0.1f, 0.1f, 1.0f);

        cameraL = new Camera(new Vector3f(directionalLight.position.x/5,directionalLight.position.y/5,directionalLight.position.z/5),new Vector3f(60,75,0) );
      //  glBlendFunc(GL_ONE, GL_ONE);
      //  GL11.glClearDepth(1.0D);
    //    GL11.glDepthFunc(GL_LEQUAL);


        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
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

        renderDepthMap(window, camera, worldRenderer,frustum);


        glViewport(0, 0, window.getWidth(), window.getHeight());
        if (window.isResized()) {

            window.setResized(false);
        }

        // Update projection and view atrices once per render cycle
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);




        renderScene(window, camera, worldRenderer,frustum);




     //   renderSkyBox(window, camera, scene);

        renderHud(window, hud);


    }



    private void setupDepthShader() throws Exception {
        depthShaderProgram = new ShaderProgram();
        depthShaderProgram.createVertexShader(Utils.loadResource("/shaders/depth_vertex.vs"));
        depthShaderProgram.createFragmentShader(Utils.loadResource("/shaders/depth_fragment.fs"));
        depthShaderProgram.link();
        depthShaderProgram.createUniform("modelViewProjection");

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
        sceneShaderProgram.createVertexShader(Utils.loadResource("/shaders/vertexD.vs"));
        sceneShaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragmentD.fs"));
        sceneShaderProgram.link();


        sceneShaderProgram.createUniform("projectionMatrix");
        sceneShaderProgram.createUniform("modelViewMatrix");
        sceneShaderProgram.createUniform("normal");
        sceneShaderProgram.createUniform("lightMatrix");
        sceneShaderProgram.createDirectionalLightUniform("directionalLight");
        sceneShaderProgram.createUniform("viewPosition");
        sceneShaderProgram.createUniform("depthTexture");

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


    public Camera cameraL;

    Matrix4f lait;
    private void renderDepthMap(Window window, Camera cam/*, Scene scene*/, WorldRenderer worldRenderer, Frustum frustum) {

        // установим активный FBO
        glBindFramebuffer(GL_FRAMEBUFFER, shadowMap.getDepthMapFBO());

// размер вьюпорта должен совпадать с размером текстуры для хранения буфера глубины
        glViewport(0, 0, ShadowMap.SHADOW_MAP_WIDTH, ShadowMap.SHADOW_MAP_HEIGHT);

// отключаем вывод цвета
        glColorMask(false, false, false, false);

// включаем вывод буфера глубины
        glDepthMask(true);

// очищаем буфер глубины перед его заполнением
        glClear(GL_DEPTH_BUFFER_BIT);

// отключаем отображение внешних граней объекта, оставляя внутренние
        glCullFace(GL_FRONT);


        depthShaderProgram.bind();



        Matrix4f viewMatrix = transformation.updateViewMatrix(cameraL);




        Matrix4f projectionMatrix = transformation.updateOrthoProjectionMatrix(-150.0f, 150.0f, -150.0f, 150.0f, -150.0f, 150.0f);
        projectionMatrix.mul(viewMatrix);

        Matrix4f bias = new Matrix4f(
                0.5f, 0.0f, 0.0f, 0f,
                0.0f, 0.5f, 0.0f, 0f,
                0.0f, 0.0f, 0.5f, 0f,
                0.5f, 0.5f, 0.5f, 1.0f);
        lait = new Matrix4f(bias.mul(projectionMatrix));



        RenderChunk.clearRend();
        scala.collection.Iterable<RenderChunk> renderChunks = worldRenderer.renderChunks().values();
        scala.collection.Iterator<RenderChunk> iterable = renderChunks.iterator();
        RenderChunk renderChunk;
        while (iterable.hasNext()) {
            renderChunk = iterable.next();


            if (frustum.cubeInFrustum(renderChunk.getCube())) {
                Matrix4f modelViewProjection = transformation.buildChunkModelViewMatrix(renderChunk.chunk.position());

                depthShaderProgram.setUniform("modelViewProjection",new Matrix4f(projectionMatrix).mul(modelViewProjection) );

                renderChunk.render(0,depthShaderProgram);
            }

        }


        // Unbind
        depthShaderProgram.unbind();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glColorMask(true, true, true, true);
        glCullFace(GL_BACK);
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
        sceneShaderProgram.setUniform("viewPosition", camera.getPosition());
        sceneShaderProgram.setUniform("directionalLight", directionalLight);



        glActiveTexture(GL_TEXTURE0 + 1);
        glBindTexture(GL_TEXTURE_2D, shadowMap.id);

        sceneShaderProgram.setUniform("depthTexture", 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_REF_TO_TEXTURE);





        Matrix4f ordoViewMatrix = transformation.updateViewMatrix(cameraL);

        Matrix4f ordoProjectionMatrix = transformation.updateOrthoProjectionMatrix(-5.0f, 5.0f, -5.0f, 5.0f, -10.0f, 10.0f);
        ordoProjectionMatrix.mul(ordoViewMatrix);




        // Render each mesh with the associated game Items
            glEnable(GL_CULL_FACE);
            //   glEnable(GL_BLEND);

            Matrix4f modelViewMatrix;
            RenderChunk.clearRend();
            scala.collection.Iterable<RenderChunk> renderChunks = worldRenderer.renderChunks().values();


          //  sceneShaderProgram.setUniform("bias", bias);
            scala.collection.Iterator<RenderChunk> iterable = renderChunks.iterator();
            RenderChunk renderChunk;
            while (iterable.hasNext()) {
                renderChunk = iterable.next();


                if (frustum.cubeInFrustum(renderChunk.getCube())) {
                    modelViewMatrix = transformation.buildChunkModelViewMatrix(renderChunk.chunk.position());

                    sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                    sceneShaderProgram.setUniform("normal", new Matrix3f(modelViewMatrix.invert()).transpose());
                    sceneShaderProgram.setUniform("lightMatrix", lait);


                    renderChunk.render(0, depthShaderProgram);
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

            glDisable(GL_CULL_FACE);
            modelViewMatrix = transformation.buildTextModelViewMatrix(new BlockWorldPos(65, 65, 65));
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

            sceneShaderProgram.setUniform("normal", new Matrix3f(modelViewMatrix.invert()).transpose());
            text.getMesh().render(sceneShaderProgram,textureManager);

            BlockAndPos bp = CubeGame.megaGame.blockAndPos;

            if (bp != null) {
                modelViewMatrix = transformation.buildBlockModelViewMatrix(bp.pos());
                sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                worldRenderer.renderBlockBounds(sceneShaderProgram);
            }


        sceneShaderProgram.unbind();
    }


    private void renderLights(Matrix4f viewMatrix/*, SceneLight sceneLight*/) {

//        sceneShaderProgram.setUniform("ambientLight", sceneLight.getAmbientLight());
//        sceneShaderProgram.setUniform("specularPower", specularPower);
//
//        // Process Point Lights
//        PointLight[] pointLightList = sceneLight.getPointLightList();
//        int numLights = pointLightList != null ? pointLightList.length : 0;
//        for (int i = 0; i < numLights; i++) {
//            // Get a copy of the point light object and transform its position to view coordinates
//            PointLight currPointLight = new PointLight(pointLightList[i]);
//            Vector3f lightPos = currPointLight.getPosition();
//            Vector4f aux = new Vector4f(lightPos, 1);
//            aux.mul(viewMatrix);
//            lightPos.x = aux.x;
//            lightPos.y = aux.y;
//            lightPos.z = aux.z;
//            sceneShaderProgram.setUniform("pointLights", currPointLight, i);
//        }
//
//        // Process Spot Ligths
//        SpotLight[] spotLightList = sceneLight.getSpotLightList();
//        numLights = spotLightList != null ? spotLightList.length : 0;
//        for (int i = 0; i < numLights; i++) {
//            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
//            SpotLight currSpotLight = new SpotLight(spotLightList[i]);
//            Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
//            dir.mul(viewMatrix);
//            currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
//
//            Vector3f lightPos = currSpotLight.getPointLight().getPosition();
//            Vector4f aux = new Vector4f(lightPos, 1);
//            aux.mul(viewMatrix);
//            lightPos.x = aux.x;
//            lightPos.y = aux.y;
//            lightPos.z = aux.z;
//
//            sceneShaderProgram.setUniform("spotLights", currSpotLight, i);
//        }
//
//        // Get a copy of the directional light object and transform its position to view coordinates








//        float lightIntensity = 1.0f;
//        Vector3f lightDirection = new Vector3f(0, 1, 1);
//        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
//        directionalLight.setShadowPosMult(5);
//        directionalLight.setOrthoCords(-10.0f, 10.0f, -10.0f, 100.0f, -1.0f, 20.0f);
//
//        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
//        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
//        dir.mul(viewMatrix);
//        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
      //  sceneShaderProgram.setUniform("directionalLight", currDirLight);
    }



    private void renderHud(Window window, IHud hud) {
        if (hud != null) {
            glEnable(GL_BLEND);
            glDisable(GL_CULL_FACE);
            glDisable(GL_DEPTH_TEST);
            hudShaderProgram.bind();


            Matrix4f ortho = transformation.getOrtho2DProjectionMatrix(0, window.getWidth(),  0,window.getHeight());
            hudShaderProgram.setUniform("projectionMatrix", ortho);

            for (Text gameItem : hud.getGameItems().values()) {
                Mesh mesh = gameItem.getMesh();

                Matrix4f projModelMatrix = transformation.buildOrtoProjModelMatrix(gameItem);
                hudShaderProgram.setUniform("modelMatrix", projModelMatrix);
                mesh.render(hudShaderProgram,textureManager);
            }

            glEnable(GL_CULL_FACE);
            if(cubeGame.guiScreen!=null){
                cubeGame.guiScreen.render(this);
            }

            glEnable(GL_DEPTH_TEST);
            glDisable(GL_BLEND);

          //  glEnable(GL_DEPTH_TEST);


            hudShaderProgram.unbind();


        }
    }



    public void cleanup() {
        if (shadowMap != null) {
            shadowMap.cleanup();
        }
        if (depthShaderProgram != null) {
            depthShaderProgram.cleanup();
        }
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
