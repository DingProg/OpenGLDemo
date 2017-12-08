package com.apusapps.opengllesson;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author by dingdegao
 *         time 2017/11/28 16:32
 *         function:
 */

public class DemoGlSurfaceView extends GLSurfaceView {


    private DemoReader renderer;

    public DemoGlSurfaceView(Context context) {
        super(context);
        init();
    }

    public DemoGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        renderer = new DemoReader(getContext(), this);
        setRenderer(renderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float centX = event.getX() / getWidth();
                float centY = event.getY() / getHeight();
                renderer.setCenterX(centX);
                renderer.setCenterY(centY);
                requestRender();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
