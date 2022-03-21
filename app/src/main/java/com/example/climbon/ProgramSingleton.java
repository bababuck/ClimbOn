package com.example.climbon;

import android.opengl.GLES20;

public class ProgramSingleton {

    private static int mProgram;
    private static ProgramSingleton program_singleton = null;

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private ProgramSingleton() {
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
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
        return mProgram;
    }
}
