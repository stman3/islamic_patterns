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

public class Animation3 {

    // The window handle
    private long window;
    private float subtractValue = 0.0015f;
    private long startTime = System.currentTimeMillis();
    private float scalingDownFactor = 1f;
    private float translateYFactor = 0f;
    private float flippingDegree = 0f;
    private float countStars = 0;
    private boolean revLine = false;
    private float locTrin = 0.0f;
    HashMap<String, Float> outerLineData = new HashMap<String, Float>();
    HashMap<String, Float> innerTrinOneData = new HashMap<String, Float>();

    //-------------------------------------------------------------------------
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

    //-------------------------------------------------------------------------
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

    //-------------------------------------------------------------------------
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
            glPushMatrix();
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
            if (locTrin > 0.18f) {
                revLine = true;
            }
            if (locTrin <= 0.0f) {
                revLine = false;
            }
            if (revLine == true) {
                locTrin -= 0.001;
            } else {
                locTrin += 0.001;
            }
            glPopMatrix();

            //-------------------------------------------
            glLoadIdentity();
            glPushMatrix();
            glScaled(1.3, 1.3, 1);
            for (int i = 0; i <= 360;) {
                glRotatef(i, 0, 0, 1);
                drawInnerTrinTwo();
                i += 45;
            }
            updateFlippingDeg();
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

            for (int i = 0; i < countStars; i++) {
                float x = 0.09f;
                float y = 0.09f;
                float incrment = 0.05f;
                for (int j = 0; j < countStars; j++) {
                    if ((incrmentshiftx < -0.7 || incrmentshifty < -0.8) || (incrmentshiftx > 0.7 || incrmentshifty > 0.8)) {
                        float shiftx = 0.0f + incrmentshiftx;
                        float shifty = 0.0f + incrmentshifty;
                        drawBackground(x, y, incrment, shiftx, shifty);
                    }
                    incrmentshiftx += 0.283f;
                    System.out.println("Stars Background Translation for x");
                    printTransMatrix(incrmentshiftx,incrmentshifty);
                }

                incrmentshiftx = -0.845f;
                incrmentshifty += 0.283f;
                System.out.println("Stars Background Translation for y ");
                printTransMatrix(incrmentshiftx,incrmentshifty);

            }
            countStars += 0.01f;
            if (countStars > 7.0) {
                countStars = 0.0f;
            }
            glPopMatrix();

            //-------------------------------------------
            float incrmentshiftxRev = 0.85f;
            float incrmentshiftyRev = 0.85f;
            for (int i = 0; i < countStars; i++) {
                float x = 0.09f;
                float y = 0.09f;
                float incrment = 0.05f;

                for (int j = 0; j < countStars; j++) {
                    if ((incrmentshiftxRev < -0.7 || incrmentshiftyRev < -0.8) || (incrmentshiftxRev > 0.7 || incrmentshiftyRev > 0.8)) {
                        float shiftx = 0.0f + incrmentshiftxRev;
                        float shifty = 0.0f + incrmentshiftyRev;
                        drawBackground(x, y, incrment, shiftx, shifty);
                    }
                    incrmentshiftxRev -= 0.283f;
                }

                incrmentshiftxRev = 0.845f;
                incrmentshiftyRev -= 0.283f;

            }
            //------------------------------------
            glLoadIdentity();
            glPushMatrix();

            drawBackBox();

            glPopMatrix();

            glfwSwapBuffers(window); // swap the color buffers
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            // time to wait before starting  the animation!

            float secondsPassed = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime);
            if (secondsPassed < 5) {
                continue;
            }

        }
    }

    //-------------------------------------------------------------------------
    private void drawOuterLine(float x1, float y1, float x2, float y2, float x3, float y3) {
        // create first pattern ( outer )
        glLineWidth(4);
        glPushMatrix();
        glRotatef(flippingDegree, 0, 1, 1);
        System.out.println("Rotating Arrow Lines");
        printMatrix();
        glBegin(GL_LINE_STRIP);
        {
            glColor3d(0.89, 0.68, 0.29);
            glVertex3f(x1 + locTrin, y1, 0f);
            glVertex3f(x2 + locTrin, y2, 0f);
            glVertex3f(x3 + locTrin, y3, 0f);

        }
        glEnd();
        System.out.println("Translation Manually not using gl");
        printTransMatrix(locTrin,locTrin);
        glPopMatrix();
    }
    //-------------------------------------------------------------------------

    private void drawInnerTrinOne(float red, float green, float blue, float x1, float y1, float x2,
            float y2, float x3, float y3, float x4, float y4) {
        glPushMatrix();
        if (y1 > -0.18f && y3 < 0.18f) {
            glRotatef(0, 0, 0, 360);
        }
        // x and y 
        glRotatef(flippingDegree, 1, 1, 0);
        glBegin(GL_POLYGON);
        {
            glColor3d(red, green, blue);
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

    //-------------------------------------------------------------------------
    private void drawInnerTrinTwo() {
        glPushMatrix();
        float xRef = 1f;
        float yRef = 1f;
        glRotatef(flippingDegree, 1, 1, 0);
        System.out.println("Rotating Inner polygons first and Second");
        printMatrix();
        glBegin(GL_POLYGON);
        {

            glColor3d(0.89, 0.78, 0.39);
            glVertex3f(0.01f * xRef, 0f * yRef, 0f);
            glVertex3f(0.22f * xRef, -0.08f * yRef, 0f);
            glVertex3f(0.2f * xRef, 0f * yRef, 0f);
            glVertex3f(0.22f * xRef, 0.08f * yRef, 0f);

        }
        glBegin(GL_LINE_LOOP);
        {
            glColor3d(0.79, 0.58, 0.19);
            glVertex3f(0.01f * xRef, 0f * yRef, 0f);
            glVertex3f(0.22f * xRef, -0.08f * yRef, 0f);
            glVertex3f(0.2f * xRef, 0f * yRef, 0f);
            glVertex3f(0.22f * xRef, 0.08f * yRef, 0f);

        }
        glEnd();
        glPopMatrix();
    }

    //-------------------------------------------------------------------------
    private void drawDots() {
        glPushMatrix();

        glRotatef(flippingDegree, 1, 1, 0);
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

    //-------------------------------------------------------------------------
    private void drawBackground(float x, float y, float incrment, float shiftx, float shifty) {
        glLineWidth(3f);
        glColor3d(0.79, 0.68, 0.29);
        glPushMatrix();
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
        glPopMatrix();

    }

    //-------------------------------------------------------------------------
    private void drawBackBox() {
        glLineWidth(4f);
        glPushMatrix();
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
        glPopMatrix();
    }

    //-------------------------------------------------------------------------
    private void updateFlippingDeg() {
        flippingDegree += 0.55;
    }

    //-------------------------------------------------------------------------
    private void printMatrix() {
        System.out.println("---------------------------------------");
        double[] matrix = new double[16];
        glGetDoublev(GL_MODELVIEW_MATRIX, matrix);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                System.out.printf("%7.3f%c", matrix[row + col * 4], col == 3 ? '\n' : ' ');
            }
        }
        System.out.println("---------------------------------------");
    }

    //------------------------------------------------------------------------
    private void printTransMatrix(float tx, float ty) {
        System.out.println("---------------------------------------");
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if ((row == 0) && col == 3) {
                    System.out.printf("%7.4f%c", tx, col == 3 ? '\n' : ' ');
                } else if (row == 1 && col == 3) {
                    System.out.printf("%7.4f%c", ty, col == 3 ? '\n' : ' ');

                } else if (row == col) {
                    System.out.printf("%7.4f%c", 1f, col == 3 ? '\n' : ' ');
                } else {
                    System.out.printf("%7.4f%c", 0f, col == 3 ? '\n' : ' ');
                }
            }
        }
        System.out.println("---------------------------------------");
    }
    //------------------------------------------------------------------------

    //-------------------------------------------------------------------------         
    private void initOuterLineData() {
        outerLineData.put("x1", 0.36f);
        outerLineData.put("y1", 0.04f);
        outerLineData.put("x2", 0.28f);
        outerLineData.put("y2", 0.12f);
        outerLineData.put("x3", 0.28f);
        outerLineData.put("y3", 0.24f);
        outerLineData.put("degree", 45f);
    }

    //-------------------------------------------------------------------------
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

    //-------------------------------------------------------------------------
    public static void main(String[] args) {
        new Animation3().run();
    }
}
