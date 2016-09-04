package ru.megains.engine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private final Vector2d previousPos;

    public static final Vector2d currentPos= new Vector2d(0, 0);

    private final Vector2f displVec;

    private boolean inWindow = false;

    private boolean leftButtonPressed = false;

    private boolean rightButtonPressed = false;

    private GLFWCursorPosCallback cursorPosCallback;

    private GLFWCursorEnterCallback cursorEnterCallback;

    private GLFWMouseButtonCallback mouseButtonCallback;

    private GLFWScrollCallback scrollCallback;

    public int scroll = 0;


    MouseInput() {
        previousPos = new Vector2d(-1, -1);
        displVec = new Vector2f();
    }

    public void init(Window window) {


        //  glfwSetInputMode(window.getWindowHandle(),GLFW_CURSOR,GLFW_CURSOR_DISABLED);

        glfwSetCursorPosCallback(window.getWindowHandle(), cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                currentPos.x = xpos  ;
                currentPos.y = (ypos-600)*-1;
            }
        });
        glfwSetCursorEnterCallback(window.getWindowHandle(), cursorEnterCallback = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                inWindow = entered;
            }
        });
        glfwSetMouseButtonCallback(window.getWindowHandle(), mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
                rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;

            }
        });

        glfwSetScrollCallback(window.getWindowHandle(), scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                scroll = (int) yoffset *-1;
            }
        });


    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    void input(Window window) {

        displVec.x = 0;
        displVec.y = 0;
        if (inWindow && isMouseGrade) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;

        if (window.isKeyPressed(GLFW_KEY_M)) {
            mouseGrade(window.getWindowHandle());
        }
    }

    private boolean isMouseGrade = false;
    private int i = 0;

    private void mouseGrade(long window) {
        i++;
        if (i == 5) {
            i = 0;
            if (isMouseGrade) {

                glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            } else {
                glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            }
            isMouseGrade = !isMouseGrade;
        }
    }


    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
