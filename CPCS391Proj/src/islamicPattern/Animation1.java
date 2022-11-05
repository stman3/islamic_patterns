package islamicPattern;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;
import java.util.HashMap;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Animation1 {

    // The window handle
    private long window;
    private float subtractValue = 0.0015f;
    private float scalingDownFactor = 1f;
    private float translateYFactor = 0f;
    private float rotationgDegree = 0f;
    private boolean reverseAnimation = false;
    HashMap<String, Float> outerLineData = new HashMap<String, Float>();
    HashMap<String, Float> innerTrinOneData = new HashMap<String, Float>();

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        initOuterLineData();
        initInnerTrinOneData();
        loop();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(600, 600, "IslamicPattern!", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        // Set the clear color
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            //-------------------------------------------
            glLoadIdentity();
            glScaled(1.3, 1.3, 1);
            float x1 = outerLineData.get("x1");
            float y1 = outerLineData.get("y1");
            float x2 = outerLineData.get("x2");
            float y2 = outerLineData.get("y2");
            float x3 = outerLineData.get("x3");
            float y3 = outerLineData.get("y3");
            float degree = outerLineData.get("degree");
            for (int i = 0; i <= 8; i++) {
                glRotatef(i * degree, 0, 0, 1);
                drawOuterLine(x1, y1, x2, y2, x3, y3);
            }
            glPopMatrix();

            //-------------------------------------------
            glLoadIdentity();
            glPushMatrix();
            glScaled(1.3, 1.3, 1);
            scaleDownAndTranslate();
            for (int i = 0; i <= 360;) {
                glRotatef(i, 0, 0, 1);
                drawInnerTrinTwo();
                i += 45;
            }
            glPopMatrix();
            //-------------------------------------------
            glLoadIdentity();
            glPushMatrix();
            glScaled(1.3, 1.3, 1);
            scaleDownAndTranslate();
            x1 = innerTrinOneData.get("x1");
            y1 = innerTrinOneData.get("y1");
            x2 = innerTrinOneData.get("x2");
            y2 = innerTrinOneData.get("y2");
            x3 = innerTrinOneData.get("x3");
            y3 = innerTrinOneData.get("y3");
            float x4 = innerTrinOneData.get("x4");
            float y4 = innerTrinOneData.get("y4");
            for (int i = 0; i <= 360;) {
                glRotatef(i, 0, 0, 1);
                drawInnerTrinOne(0.7f, 0, 0, x1, y1, x2, y2, x3, y3, x4, y4);
                i += 45;
            }
            glPopMatrix();
            //-------------------------------------------
            glLoadIdentity();
            glPushMatrix();
            glScaled(1.3, 1.3, 1);
            scaleDownAndTranslate();
            for (int i = 0; i <= 360;) {
                glRotatef(i, 0, 0, 1);
                drawDots();
                i += 45;
            }
            glPopMatrix();
            //-------------------------------------------

            float incrmentshiftx = -0.85f;
            float incrmentshifty = -0.85f;
            glLoadIdentity();
            glPushMatrix();

            for (int i = 0; i < 7; i++) {
                float x = 0.09f;
                float y = 0.09f;
                float incrment = 0.05f;

                for (int j = 0; j < 7; j++) {
                    if ((incrmentshiftx < -0.7 || incrmentshifty < -0.8) || (incrmentshiftx > 0.7 || incrmentshifty > 0.8)) {
                        float shiftx = 0.0f + incrmentshiftx;
                        float shifty = 0.0f + incrmentshifty;
                        drawBackground(x, y, incrment, shiftx, shifty);
                    }
                    incrmentshiftx += 0.283f;

                }

                incrmentshiftx = -0.845f;
                incrmentshifty += 0.283f;

            }
            glPopMatrix();
            //-------------------------------------------

            glLoadIdentity();
            glPushMatrix();

            drawBackBox();

            glPopMatrix();

            glfwSwapBuffers(window); // swap the color buffers
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            updateValues();
        }
    }

    private void updateValues() {
        updateOuterLineData();
        updateInnerTrinOneData();
        updateTranslateYFactor();
        updateScalingDownFactor();
        updateRotationgDegree();
        checkForReverse();
    }

    private void checkForReverse() {
        if (scalingDownFactor <= 0.7f && translateYFactor >= 0.3f && outerLineData.get("degree") <= 0 && outerLineData.get("y1") <= -0.52f && outerLineData.get("y2") <= 0.05f) {
            reverseAnimation = false;
        } else if (scalingDownFactor >= 1f && translateYFactor <= 0f && outerLineData.get("degree") >= 45 && outerLineData.get("y1") >= 0.04f && outerLineData.get("y2") >= 0.12f && outerLineData.get("y3") >= 0.24f) {
            reverseAnimation = false;
        }
    }

    private void updateRotationgDegree() {
        rotationgDegree++;
    }

    private void updateScalingDownFactor() {
        if (!reverseAnimation) {
            scalingDownFactor = scalingDownFactor > 0.7f ? scalingDownFactor - 0.009f : scalingDownFactor;
        } else {
            scalingDownFactor = scalingDownFactor <= 1f ? scalingDownFactor + 0.009f : scalingDownFactor;
        }
    }

    private void updateTranslateYFactor() {
        if (!reverseAnimation) {
            translateYFactor = translateYFactor < 0.3f ? translateYFactor + 0.009f : translateYFactor;
        } else {
            translateYFactor = translateYFactor >= 0f ? translateYFactor - 0.009f : translateYFactor;
        }
    }

    private void scaleDownAndTranslate() {
        glScalef(scalingDownFactor, scalingDownFactor, 1f);
        glTranslatef(0, translateYFactor, 0);

    }

    //----------------------------------------
    private void updateOuterLineData() {
        float[] oldFirstVertex = new float[]{outerLineData.get("x1"), outerLineData.get("y1"), 1};
        float[] oldSecondVertex = new float[]{outerLineData.get("x2"), outerLineData.get("y2"), 1};
        float[] oldThirdVertex = new float[]{outerLineData.get("x3"), outerLineData.get("y3"), 1};
        float oldDegree = outerLineData.get("degree");
        outerLineData.forEach((key, v) -> {
            if (key.equals("x1") || key.equals("x2") || key.equals("x3")) {
                if (!reverseAnimation) {
                    outerLineData.put(key, v.floatValue() > 0f ? v - subtractValue : v);
                } else if (reverseAnimation && key.equals("x1")) {
                    outerLineData.put(key, v.floatValue() < 0.36f ? v + subtractValue : v);
                } else if (reverseAnimation && key.equals("x2")) {
                    outerLineData.put(key, v.floatValue() < 0.28f ? v + subtractValue : v);
                } else if (reverseAnimation && key.equals("x3")) {
                    outerLineData.put(key, v.floatValue() < 0.28f ? v + subtractValue : v);
                }
            } else if (key.equals("y1")) {
                if (!reverseAnimation) {
                    outerLineData.put(key, v.floatValue() > -0.52f ? v - subtractValue : v);
                } else {
                    outerLineData.put(key, v.floatValue() < 0.04f ? v + subtractValue : v);
                }
            } else if (key.equals("y2") || key.equals("y3")) {
                if (!reverseAnimation) {
                    outerLineData.put(key, v.floatValue() > 0.05f ? v - subtractValue : v);
                } else if (reverseAnimation && key.equals("y2")) {
                    outerLineData.put(key, v.floatValue() < 0.12f ? v + subtractValue : v);
                } else if (key.equals("y3")) {
                    outerLineData.put(key, v.floatValue() < 0.24f ? v + subtractValue : v);
                }
            } else if (key.equals("degree")) {
                if (!reverseAnimation) {
                    outerLineData.put(key, v.floatValue() > 0 ? v - 0.2f : 0);
                } else {
                    outerLineData.put(key, v.floatValue() < 45 ? v + 0.2f : 0);
                }
            }
        });
        float[] newFirstVertex = new float[]{outerLineData.get("x1"), outerLineData.get("y1"), 1};
        float[] newSecondVertex = new float[]{outerLineData.get("x2"), outerLineData.get("y2"), 1};
        float[] newThirdVertex = new float[]{outerLineData.get("x3"), outerLineData.get("y3"), 1};
        float newDegree = outerLineData.get("degree");
        printMatrix(constructTranslationRoatationMatrix(newFirstVertex[0] - oldFirstVertex[0], newFirstVertex[1] - oldFirstVertex[1], 0, newDegree - oldDegree), "outer lines x1, y1 translate & z rotation matrix");
        printMatrix(constructTranslationRoatationMatrix(newSecondVertex[0] - oldSecondVertex[0], newSecondVertex[1] - oldSecondVertex[1], 0, newDegree - oldDegree), "outer lines x2, y2 translate & z rotation matrix");
        printMatrix(constructTranslationRoatationMatrix(newThirdVertex[0] - oldSecondVertex[0], newThirdVertex[1] - oldThirdVertex[1], 0, newDegree - oldDegree), "outer lines x3, y3 translate & z rotation matrix");
    }

    private void updateInnerTrinOneData() {
        float[] oldFourthVertex = new float[]{innerTrinOneData.get("x4"), innerTrinOneData.get("y4"), 1};

        innerTrinOneData.forEach((key, v) -> {
            if (key.equals("x4")) {
                if (!reverseAnimation) {
                    innerTrinOneData.put(key, v.floatValue() > -0.05f ? v - subtractValue : v);
                } else {
                    innerTrinOneData.put(key, v.floatValue() <= 0.22f ? v + subtractValue : v);
                }
            }
        });
        float[] newFourthVertex = new float[]{innerTrinOneData.get("x4"), innerTrinOneData.get("y4"), 1};
        printMatrix(constructTranslationMatrix(oldFourthVertex[0] - newFourthVertex[0], 0f, 0f), "first Inner triangle x4 translate matrix");
    }

    //----------------------------------------------------------------------------------------
    private void drawOuterLine(float x1, float y1, float x2, float y2, float x3, float y3) {
        // create firs pattern ( outer)
        glLineWidth(4);
        glPushMatrix();
        glBegin(GL_LINE_STRIP);
        {
            glColor3d(0.89, 0.68, 0.29);
            glVertex3f(x1, y1, 0f);
            glVertex3f(x2, y2, 0f);
            glVertex3f(x3, y3, 0f);

        }
        glEnd();
        glPopMatrix();
    }
    //----------------------------------------------------------------------------------------

    private void drawInnerTrinOne(float red, float green, float blue, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        glPushMatrix();
        if (y1 > -0.18f && y3 < 0.18f) {
            glRotatef(0, 0, 0, 360);
        }
        glBegin(GL_POLYGON);
        {
            glColor3d(red, green, blue);
            //glColor3d(0.99, 0.78, 0.39);
            glVertex3f(x1, y1, 0f);
            glVertex3f(x2, y2, 0f);
            glVertex3f(x3, y3, 0f);
            glVertex3f(x4, y4, 0f);

        }
        glBegin(GL_LINE_LOOP);
        {
            glColor3d(0.2, 0, 0);
            glVertex3f(x1, y1, 0f);
            glVertex3f(x2, y2, 0f);
            glVertex3f(x3, y3, 0f);
            glVertex3f(x4, y4, 0f);

        }
        glEnd();
        glPopMatrix();
    }
    //----------------------------------------------------------------------------------------

    private void drawInnerTrinTwo() {
        glPushMatrix();
        glRotatef(rotationgDegree, 0, 0, 1);
        glBegin(GL_POLYGON);
        {

            glColor3d(0.89, 0.78, 0.39);
            glVertex3f(0.01f, 0f, 0f);
            glVertex3f(0.22f, -0.08f, 0f);
            glVertex3f(0.2f, 0f, 0f);
            glVertex3f(0.22f, 0.08f, 0f);

        }
        glBegin(GL_LINE_LOOP);
        {
            glColor3d(0.79, 0.58, 0.19);
            glVertex3f(0.01f, 0f, 0f);
            glVertex3f(0.22f, -0.08f, 0f);
            glVertex3f(0.2f, 0f, 0f);
            glVertex3f(0.22f, 0.08f, 0f);

        }
        glEnd();
        glPopMatrix();
    }
    //----------------------------------------------------------------------------------------

    private void drawDots() {
        glPushMatrix();
        glBegin(GL_POLYGON);
        {

            glColor3d(0.79, 0.58, 0.19);
            glVertex3f(0.24f, 0.087f, 0f);
            glVertex3f(0.25f, 0.097f, 0f);
            glVertex3f(0.24f, 0.107f, 0f);
            glVertex3f(0.23f, 0.097f, 0f);

        }
        glBegin(GL_LINE_LOOP);
        {

            glColor3d(0.89, 0.78, 0.39);
            glVertex3f(0.24f, 0.087f, 0f);
            glVertex3f(0.25f, 0.097f, 0f);
            glVertex3f(0.24f, 0.107f, 0f);
            glVertex3f(0.23f, 0.097f, 0f);

        }
        glEnd();
        glPopMatrix();
    }
    //----------------------------------------------------------------------------------------

    private void drawBackground(float x, float y, float incrment, float shiftx, float shifty) {
        glLineWidth(3f);
        glColor3d(0.79, 0.68, 0.29);
        glBegin(GL_LINE_STRIP);
        {

            glVertex3f(-x + shiftx, y + shifty, 0f);
            //up
            glVertex3f(-x / 2 + shiftx, y + shifty, 0f);
            glVertex3f(0 + shiftx, y + incrment + shifty, 0f);
            glVertex3f(x / 2 + shiftx, y + shifty, 0f);
            glVertex3f(x + shiftx, y + shifty, 0f);
            //mid right
            glVertex3f(x + shiftx, y / 2 + shifty, 0f);
            glVertex3f(x + incrment + shiftx, 0 + shifty, 0f);
            glVertex3f(x + shiftx, -y / 2 + shifty, 0f);

            glVertex3f(+x + shiftx, -y + shifty, 0f);
            //down
            glVertex3f(+x / 2 + shiftx, -y + shifty, 0f);
            glVertex3f(0 + shiftx, -(y + incrment) + shifty, 0f);
            glVertex3f(-x / 2 + shiftx, -y + shifty, 0f);

            glVertex3f(-x + shiftx, -y + shifty, 0f);
            //mid left
            glVertex3f(-x + shiftx, -y / 2 + shifty, 0f);
            glVertex3f(-(x + incrment) + shiftx, 0.0f + shifty, 0f);
            glVertex3f(-x + shiftx, y / 2 + shifty, 0f);
            glVertex3f(-x + shiftx, y + shifty, 0f);
        }

        glEnd();

    }

    //----------------------------------------------------------------------------------------
    private void drawBackBox() {
        glLineWidth(4f);
        glBegin(GL_LINE_LOOP);
        {

            glColor3d(0.79, 0.58, 0.19);
            glVertex3f(0.675f, -0.675f, 0f);
            glVertex3f(0.675f, 0.675f, 0f);
            glColor3d(0.39, 0.28, 0.09);
            glVertex3f(-0.675f, 0.675f, 0f);
            glVertex3f(-0.675f, -0.675f, 0f);

        }

        glEnd();
    }

    public static void main(String[] args) {
        new Animation1().run();
    }

    private void initOuterLineData() {
        outerLineData.put("x1", 0.36f);
        outerLineData.put("y1", 0.04f);
        outerLineData.put("x2", 0.28f);
        outerLineData.put("y2", 0.12f);
        outerLineData.put("x3", 0.28f);
        outerLineData.put("y3", 0.24f);
        outerLineData.put("degree", 45f);
    }

    private void initInnerTrinOneData() {
        innerTrinOneData.put("x1", 0.25f);
        innerTrinOneData.put("y1", -0.08f);
        innerTrinOneData.put("x2", 0.32f);
        innerTrinOneData.put("y2", 0f);
        innerTrinOneData.put("x3", 0.25f);
        innerTrinOneData.put("y3", 0.08f);
        innerTrinOneData.put("x4", 0.22f);
        innerTrinOneData.put("y4", 0.0f);
        innerTrinOneData.put("degree", 45f);
    }

    private void printMatrix(float[] matrix, String label) {
        System.out.println("---------------------------------------");
        System.out.println(label);
        System.out.println("---------------------------------------");
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                System.out.printf("%7.4f%c", matrix[row + col * 4], col == 3 ? '\n' : ' ');
            }
        }
    }

    private float[] constructTranslationRoatationMatrix(float tx, float ty, float tz, float degree) {
        return new float[]{(float) Math.cos(degree), (float) Math.cos(degree), 0, 0, (float) -Math.sin(degree), (float) Math.cos(degree), 0, 0, 0, 0, 0, 1f, tx, ty, tz, 1f};
    }

    private float[] constructTranslationMatrix(float tx, float ty, float tz) {
        return new float[]{1f, 0, 0, 0, 0, 1f, 0, 0, 0, 0, 0, 1f, tx, ty, tz, 1f};
    }
}
