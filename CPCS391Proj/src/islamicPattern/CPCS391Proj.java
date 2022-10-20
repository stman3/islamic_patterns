package islamicPattern;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class CPCS391Proj {

    // The window handle
    private long window;
    private int b = 1;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
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
            glPushMatrix();
            glScaled(1.3, 1.3, 1);
            for (int i = 0; i <= 360;) {
                drawOuterLine();
                glRotatef(i, 0, 0, 1);
                i += 45;
            }
            glPopMatrix();
            //-------------------------------------------
            glLoadIdentity();
            glPushMatrix();
            glScaled(1.3, 1.3, 1);
            for (int i = 0; i <= 360;) {
                drawInnerTrinOne();
                glRotatef(i, 0, 0, 1);
                i += 45;
            }
            glPopMatrix();
            //-------------------------------------------
            glLoadIdentity();
            glPushMatrix();
            glScaled(1.3, 1.3, 1);
            for (int i = 0; i <= 360;) {
                drawInnerTrinTwo();
                glRotatef(i, 0, 0, 1);
                i += 45;
            }
            glPopMatrix();
            //-------------------------------------------
            glLoadIdentity();
            glPushMatrix();
            glScaled(1.3, 1.3, 1);
            for (int i = 0; i <= 360;) {
                drawDots();
                glRotatef(i, 0, 0, 1);
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
            glScalef(0.5f, 0.5f, 1.0f);
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
        }
    }

    //----------------------------------------------------------------------------------------
    private void drawOuterLine() {
        // create firs pattern ( outer)
        glLineWidth(4);
        glBegin(GL_LINE_STRIP);
        {
            glColor3d(0.89, 0.68, 0.29);
            glVertex3f(0.36f, 0.04f, 0f);
            glVertex3f(0.28f, 0.12f, 0f);
            glVertex3f(0.28f, 0.24f, 0f);
        }
        glEnd();
    }
    //----------------------------------------------------------------------------------------

    private void drawInnerTrinOne() {

        glBegin(GL_POLYGON);
        {
            glColor3d(0.7, 0.0, 0.0);
            //glColor3d(0.99, 0.78, 0.39);
            glVertex3f(0.25f, -0.08f, 0f);
            glVertex3f(0.32f, 0f, 0f);
            glVertex3f(0.25f, 0.08f, 0f);
            glVertex3f(0.22f, 0f, 0f);
        }
        glBegin(GL_LINE_LOOP);
        {
            glColor3d(0.2, 0.0, 0.0);
            // glColor3d(0.79, 0.58, 0.19);

            glVertex3f(0.25f, -0.08f, 0f);
            glVertex3f(0.32f, 0f, 0f);
            glVertex3f(0.25f, 0.08f, 0f);
            glVertex3f(0.22f, 0f, 0f);

        }

        glEnd();
    }
    //----------------------------------------------------------------------------------------

    private void drawInnerTrinTwo() {
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
    }
    //----------------------------------------------------------------------------------------

    private void drawDots() {
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
            glVertex3f(-0.675f, 0.67f, 0f);
            glVertex3f(-0.675f, -0.675f, 0f);

        }

        glEnd();
    }

    public static void main(String[] args) {
        new CPCS391Proj().run();

    }
}
