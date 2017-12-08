package com.apusapps.opengllesson;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class TextureHelper {
    private  static  final String TAG="TextureHelper";

    /***
     * GLES20.glGenTextures(1,textureObjectIds,0);生成一个纹理，放入textureObjectIds中，同样地，如果生成成功，那么就会返回一个非零值
     然后我们将bitmap从raw中读进来，这个就不解释了
     GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureObjectIds[0]);的作用是将我们刚生成的纹理和OpenGL的2D纹理绑定，告诉OpenGL这是一个2D的纹理（贴图）
     GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR_MIPMAP_LINEAR);
     GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
     这两句话用来设置纹理过滤的方式，GL_TEXTURE_MIN_FILTER是指缩小时的过滤方式，GL_TEXTURE_MAG_FILTER则是放大的
     关于放大和缩小时的可用方式，参见下图：
     * @param context
     * @param resourceId
     * @return
     */
    public static int loadTexture(Context context, int resourceId){

        final int[] textureObjectIds=new int[1];
        GLES20.glGenTextures(1,textureObjectIds,0);
        if (textureObjectIds[0]==0){
            Log.d(TAG,"生成纹理对象失败");
            return 0;
        }

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;

        Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(),resourceId,options);

        if (bitmap==null){
            Log.d(TAG,"加载位图失败");
            GLES20.glDeleteTextures(1,textureObjectIds,0);
            return 0;
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureObjectIds[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);

        bitmap.recycle();

        //为与target相关联的纹理图像生成一组完整的mipmap
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);

        return textureObjectIds[0];
    }
}