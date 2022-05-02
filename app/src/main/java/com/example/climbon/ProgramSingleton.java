package com.example.climbon;

import android.opengl.GLES20;
import android.util.Log;

public class ProgramSingleton {

    private static int mProgram;
    private static ProgramSingleton program_singleton = null;

    private static String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private static String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private ProgramSingleton() {
        int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public static int getProgram() {
        if (program_singleton == null) {
            program_singleton = new ProgramSingleton();
        }
        program_singleton = new ProgramSingleton();
        GLES20.glLinkProgram(mProgram);
        Log.e("ProgramSingleton","Returning program...");
        return mProgram;
    }
}
