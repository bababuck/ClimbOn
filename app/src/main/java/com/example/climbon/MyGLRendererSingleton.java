package com.example.climbon;

import android.content.Context;
import android.util.Log;

public class MyGLRendererSingleton {

    private static MyGLRenderer renderer;
    private static MyGLRendererSingleton singleton;

    private MyGLRendererSingleton(Context context) {
        renderer = new MyGLRenderer(context);
    }

    public static MyGLRenderer getProgram(Context context) {
        if (renderer == null) {
            Log.e("RendererSingleton","Making new Renderer");
            singleton = new com.example.climbon.MyGLRendererSingleton(context);
        }
        Log.e("RendererSingleton","Returning Renderer");
        return renderer;
    }

    public static void clearProgram() {
        renderer = null;
    }
}
