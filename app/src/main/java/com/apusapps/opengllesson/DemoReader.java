package com.apusapps.opengllesson;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author by dingdegao
 *         time 2017/11/28 16:34
 *         function:
 */

public class DemoReader implements GLSurfaceView.Renderer {

    private Context context;
    private GLSurfaceView glView;
    private int programId;
    private int aPosition;
    private int aTexCoord;
    private int sTexture;
    private int aCenterPoint;
    private int textureId;

    private float vertix[] ={
            -1f,1f,
            -1f,-1f,
            1f,1f,
            1f,-1f
    };
    private FloatBuffer floatBuffer;

    private float textureFloat[] = {
            0,1,
            0,0,
            1,1,
            1,0,

    };
    private FloatBuffer textureBuffer;

    private short indexShort [] = {
            0,1,2,
            1,2,3
    };
    private ShortBuffer shortBuffer;

    private final float[] projectionMatrix=new float[16];
    private int uMatrixHandle;


    public DemoReader(Context context,GLSurfaceView glView) {
        this.context = context;
        this.glView = glView;
        floatBuffer = ByteBuffer.allocateDirect(vertix.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(vertix);
        floatBuffer.position(0);

        textureBuffer = ByteBuffer.allocateDirect(textureFloat.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(textureFloat);
        textureBuffer.position(0);


        shortBuffer = ByteBuffer.allocateDirect(indexShort.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer().put(indexShort);
        shortBuffer.position(0);

        shortBuffer = ByteBuffer.allocateDirect(indexShort.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer().put(indexShort);
        shortBuffer.position(0);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        /**
         * 设置变形区域
         */
        float ratio=glView.getWidth()>glView.getHeight()?
                (float)glView.getWidth()/glView.getHeight():
                (float)glView.getHeight()/glView.getWidth();
        if (glView.getWidth()>glView.getHeight()){
            Matrix.orthoM(projectionMatrix,0,-ratio,ratio,-1f,1f,-1f,1f);
        }else {
            Matrix.orthoM(projectionMatrix,0,-1f,1f,-ratio,ratio,-1f,1f);
        }

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(context.getResources(),R.drawable.bg,options);
//
//        float photoRatio=(float)glView.getHeight()/options.outHeight;
//        Matrix.orthoM(projectionMatrix,0,-1f,1f,-photoRatio,photoRatio,-1f,1f);

        textureId = TextureHelper.loadTexture(context, R.drawable.bg);
        String fragment_shader = ShaderUtils.readRawTextFile(context, R.raw.fragment_shader);
        String vertix_shader = ShaderUtils.readRawTextFile(context, R.raw.vertix_shader);
        programId = ShaderUtils.createProgram(vertix_shader, fragment_shader);

       uMatrixHandle=GLES20.glGetUniformLocation(programId,"uMatrix");

        aPosition = GLES20.glGetAttribLocation(programId, "aPosition");
        aTexCoord = GLES20.glGetAttribLocation(programId, "aTexCoord");
        sTexture = GLES20.glGetUniformLocation(programId, "sTexture");
        aCenterPoint = GLES20.glGetUniformLocation(programId, "aCenterPoint");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }


    float centerX = 0.5f,centerY = 0.5f;

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(programId);
        GLES20.glUniformMatrix4fv(uMatrixHandle,1,false,projectionMatrix,0);
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glVertexAttribPointer(aPosition,2,GLES20.GL_FLOAT,false,0,floatBuffer);
        GLES20.glUniform2f(aCenterPoint,centerX,centerY);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);

        GLES20.glEnableVertexAttribArray(aTexCoord);
        GLES20.glVertexAttribPointer(aTexCoord,2,GLES20.GL_FLOAT,false,0,textureBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
        GLES20.glUniform1i(sTexture,0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES,indexShort.length,GLES20.GL_UNSIGNED_SHORT,shortBuffer);
    }
}
