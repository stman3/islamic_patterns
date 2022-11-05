package islamicPattern;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Animation2 {

    // The window handle
    private long window;
    private float translate = 0.0f;
    private float innerRotateDegree = 0.0f;
    private float outerRotateDegree = 0.0f;
    private int phase = 1;
    private float subtractValue = 0.0009f;
    private float scalingDownFactor = 1f;
    private float scaleUpFactor = 1f;
    private float flippingDegree = 0f;
    HashMap<String, Float> outerLineData = new HashMap<String, Float>();
    HashMap<String, Float> innerTrinOneData = new HashMap<String, Float>();
    HashMap<String, Float> innerTrinTwoData = new HashMap<String, Float>();
    HashMap<String, Float> dotsData = new HashMap<String, Float>();

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        initOuterLineData();
        initInnerTrinOneData();
        initInnerTrinTwoData();
        initDotsData();
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
        try (MemoryStack stack = stackPush()) {
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
            glRotatef(-outerRotateDegree, 0, 0, 1);

            if (phase == 3) {
                glTranslatef(translate, 0, 0);
            }
            for (int i = 0; i <= 8; i++) {
                glRotatef(i * degree, 0, 0, 1);
                drawOuterLine(x1, y1, x2, y2, x3, y3);
            }
            glPopMatrix();

            //-------------------------------------------
            glLoadIdentity();
            glPushMatrix();
            glScaled(1.3, 1.3, 1);
            glRotatef(-innerRotateDegree, 0, 0, 1);
            glScalef(scalingDownFactor, scalingDownFactor, 0);
            float[] matrix = new float[16];
            glGetFloatv(GL_MODELVIEW_MATRIX, matrix);
            printMatrix(matrix, "Scaling and rotation of second inner triangle");
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

            x1 = innerTrinOneData.get("x1");
            y1 = innerTrinOneData.get("y1");
            x2 = innerTrinOneData.get("x2");
            y2 = innerTrinOneData.get("y2");
            x3 = innerTrinOneData.get("x3");
            y3 = innerTrinOneData.get("y3");
            float x4 = innerTrinOneData.get("x4");
            float y4 = innerTrinOneData.get("y4");
            degree = innerTrinOneData.get("degree");
            glRotatef(innerRotateDegree, 0, 0, 1);

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
            x1 = dotsData.get("x1");
            y1 = dotsData.get("y1");
            x2 = dotsData.get("x2");
            y2 = dotsData.get("y2");
            x3 = dotsData.get("x3");
            y3 = dotsData.get("y3");
            x4 = dotsData.get("x4");
            y4 = dotsData.get("y4");
            glScalef(scaleUpFactor, scaleUpFactor, 0);
            glRotatef(-outerRotateDegree, 0, 0, 1);

            for (int i = 0; i <= 360;) {
                glRotatef(i, 0, 0, 1);
                drawDots(x1, y1, x2, y2, x3, y3, x4, y4);
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
                        //    drawBackground(x, y, incrment, shiftx, shifty);
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
        checkPhase();
        if (phase == 1) {
            updateOuterLineData();
            //updateDotsData();
            scaleUpFactor = scaleUpFactor < 1.6f ? scaleUpFactor + 0.0009f : scaleUpFactor;
            outerRotateDegree += 0.2f;

        } else if (phase == 2) {
            updateInnerTrinOneData();
            updateInnerTrinTwoData();
            scalingDownFactor = scalingDownFactor > 0.0f ? scalingDownFactor - 0.004f : scalingDownFactor;
            innerRotateDegree += 0.2f;
        } else if (phase == 3) {
            shuriken();
        } else if (phase == 4) {
            phase4();
        }

    }

    private void checkPhase() {
        if (outerLineData.get("x2") > 0.48f && outerLineData.get("y1") < -0.03229f & phase == 1) {
            phase++;
        } else if (innerTrinTwoData.get("x4") < 0.015f && phase == 2) {
            phase++;
        } else if (outerRotateDegree > 390.0f) {
            phase++;
        }
    }

    //----------------------------------------
    private void updateOuterLineData() {

        outerLineData.forEach((key, v) -> {
            if (key.equals("x2")) {
                outerLineData.put(key, v.floatValue() < 0.48f ? v + subtractValue : v);
            } else if (key.equals("y1")) {
                outerLineData.put(key, v.floatValue() > -0.03229f ? v - subtractValue : v);

            }
        });

    }

    private void updateInnerTrinOneData() {

        innerTrinOneData.forEach((key, v) -> {
            if (key.equals("x4")) {
                innerTrinOneData.put(key, v.floatValue() > 0.015f ? v - subtractValue : v);
            }

        });

    }

    private void updateInnerTrinTwoData() {

        innerTrinTwoData.forEach((key, v) -> {
            if (key.equals("x4")) {
                innerTrinTwoData.put(key, v.floatValue() > 0.015f ? v - subtractValue : v);
            }

        });

    }

    private void shuriken() {
        outerRotateDegree += 2.0f;
        innerRotateDegree -= 2.0f;
    }

    private void phase4() {
        if (outerRotateDegree > -45f) {
            reverseInnerTrinOneData();
            reverseInnerTrinTwoData();
            scalingDownFactor = scalingDownFactor <= 1f ? scalingDownFactor + 0.0049f : scalingDownFactor;
            outerRotateDegree -= 2.0f;
            innerRotateDegree += 2.0f;
        } else if (outerLineData.get("x2") <= 0.29f) {
            phase = 1;
        } else {

            revrseOuterLineData();
            scaleUpFactor = scaleUpFactor < 0.3f ? scaleUpFactor + 0.0009f : scaleUpFactor;

        }
    }

    private void reverseInnerTrinOneData() {
        float[] oldFourthVertex = new float[]{innerTrinOneData.get("x4"), innerTrinOneData.get("y4"), 1};

        innerTrinOneData.forEach((key, v) -> {
            if (key.equals("x4")) {
                innerTrinOneData.put(key, v.floatValue() <= 0.25f ? v + subtractValue : v);
            }

        });
        float[] newFourthVertex = new float[]{innerTrinOneData.get("x4"), innerTrinOneData.get("y4"), 1};
        printMatrix(constructTranslationMatrix(oldFourthVertex[0] - newFourthVertex[0], 0f, 0f), "first Inner triangle x4 translate matrix");

    }

    private void reverseInnerTrinTwoData() {
        float[] oldFourthVertex = new float[]{innerTrinTwoData.get("x4"), innerTrinTwoData.get("y4"), 1};

        innerTrinTwoData.forEach((key, v) -> {
            if (key.equals("x4")) {
                innerTrinTwoData.put(key, v.floatValue() <= 0.25f ? v + subtractValue : v);
            }

        });
        float[] newFourthVertex = new float[]{innerTrinTwoData.get("x4"), innerTrinTwoData.get("y4"), 1};
        printMatrix(constructTranslationMatrix(newFourthVertex[0] - oldFourthVertex[0], 0f, 0f), "second Inner triangle x4 translate matrix");

    }

    private void revrseOuterLineData() {
        float[] oldFirstVertex = new float[]{outerLineData.get("x1"), outerLineData.get("y1"), 1};
        float[] oldSecondVertex = new float[]{outerLineData.get("x2"), outerLineData.get("y2"), 1};
        float oldDegree = outerLineData.get("degree");
        outerLineData.forEach((key, v) -> {
            if (key.equals("x2")) {
                outerLineData.put(key, v.floatValue() >= 0.29f ? v - subtractValue : v);
            } else if (key.equals("y1")) {
                outerLineData.put(key, v.floatValue() <= 0.04f ? v + subtractValue : v);

            }

        });
        float[] newFirstVertex = new float[]{outerLineData.get("x1"), outerLineData.get("y1"), 1};
        float[] newSecondVertex = new float[]{outerLineData.get("x2"), outerLineData.get("y2"), 1};
        float newDegree = outerLineData.get("degree");
        printMatrix(constructTranslationRoatationMatrix(newFirstVertex[0] - oldFirstVertex[0], newFirstVertex[1] - oldFirstVertex[1], 0, newDegree - oldDegree), "outer lines x1, y1 translate & z rotation matrix");
        printMatrix(constructTranslationRoatationMatrix(newSecondVertex[0] - oldSecondVertex[0], newSecondVertex[1] - oldSecondVertex[1], 0, newDegree - oldDegree), "outer lines x2, y2 translate & z rotation matrix");

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
        glRotatef(flippingDegree, 0, 0, 1);
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

    private void drawDots(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        glPushMatrix();
        glBegin(GL_POLYGON);
        {

            glColor3d(0.79, 0.68, 0.29);
            glVertex3f(x1, y1, 0f);
            glVertex3f(x2, y2, 0f);
            glVertex3f(x3, y3, 0f);
            glVertex3f(x4, y4, 0f);

        }
        glBegin(GL_LINE_LOOP);
        {

            glColor3d(0.79, 0.68, 0.29);
            glVertex3f(x1, y1, 0f);
            glVertex3f(x2, y2, 0f);
            glVertex3f(x3, y3, 0f);
            glVertex3f(x4, y4, 0f);
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
        new Animation2().run();
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

    private void initInnerTrinTwoData() {
//        glColor3d(0.89, 0.78, 0.39);
//            glVertex3f(0.01f, 0f, 0f);
//            glVertex3f(0.22f, -0.08f, 0f);
//            glVertex3f(0.2f, 0f, 0f);
//            glVertex3f(0.22f, 0.08f, 0f);
        innerTrinTwoData.put("x1", 0.1f);
        innerTrinTwoData.put("y1", 0.0f);
        innerTrinTwoData.put("x2", 0.22f);
        innerTrinTwoData.put("y2", -0.8f);
        innerTrinTwoData.put("x3", 0.2f);
        innerTrinTwoData.put("y3", 0.0f);
        innerTrinTwoData.put("x4", 0.22f);
        innerTrinTwoData.put("y4", 0.08f);
        innerTrinTwoData.put("degree", 45f);
    }

    private void initDotsData() {
//         glColor3d(0.79, 0.58, 0.19);
//            glVertex3f(0.24f, 0.087f, 0f);
//            glVertex3f(0.25f, 0.097f, 0f);
//            glVertex3f(0.24f, 0.107f, 0f);
//            glVertex3f(0.23f, 0.097f, 0f);
        dotsData.put("x1", 0.24f);
        dotsData.put("y1", 0.087f);
        dotsData.put("x2", 0.25f);
        dotsData.put("y2", 0.097f);
        dotsData.put("x3", 0.24f);
        dotsData.put("y3", 0.107f);
        dotsData.put("x4", 0.23f);
        dotsData.put("y4", 0.097f);

    }
}
