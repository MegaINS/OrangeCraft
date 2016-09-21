package ru.megains.renderer.graph;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_COMPARE_MODE;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;


public class ShadowMap {

    public static final int SHADOW_MAP_WIDTH = 4096;

    public static final int SHADOW_MAP_HEIGHT = 4096;

    private final int depthMapFBO;

    public int id;
    //  private final Texture depthMap;

    public ShadowMap() throws Exception {


        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // установим параметры "оборачиваниея" текстуры - отсутствие оборачивания
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        // необходимо для использования depth-текстуры как shadow map
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_REF_TO_TEXTURE);

        // соаздем "пустую" текстуру под depth-данные
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);

        // проверим на наличие ошибок
        //   OPENGL_CHECK_FOR_ERRORS();


        depthMapFBO = glGenFramebuffers();


// создаем FBO для рендера глубины в текстуру

// делаем созданный FBO текущим
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);

// отключаем вывод цвета в текущий FBO
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);

// указываем для текущего FBO текстуру, куда следует производить рендер глубины
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, id, 0);

// проверим текущий FBO на корректность
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new Exception("Could not create FrameBuffer");
        }

// возвращаем FBO по-умолчанию
        glBindFramebuffer(GL_FRAMEBUFFER, 0);


//        // Create a FBO to render the depth map
//        depthMapFBO = glGenFramebuffers();
//
//        // Create the depth map texture
//        depthMap = new Texture(SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT, GL_DEPTH_COMPONENT);
//
//        // Attach the the depth map texture to the FBO
//        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
//        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap.getId(), 0);
//        // Set only depth
//        glDrawBuffer(GL_NONE);
//        glReadBuffer(GL_NONE);
//
//        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
//            throw new Exception("Could not create FrameBuffer");
//        }
//
//        // Unbind
//        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }


    // public Texture getDepthMapTexture() {
//        return depthMap;
//    }
//
    public int getDepthMapFBO() {
        return depthMapFBO;
    }

    //
    public void cleanup() {
        glDeleteFramebuffers(depthMapFBO);
        //  depthMap.cleanup();
    }
}
