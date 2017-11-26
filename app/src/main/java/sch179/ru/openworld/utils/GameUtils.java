package sch179.ru.openworld.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GameUtils {

    private static AssetManager assetManager;
    private static Context context;

    public static void setContext(Context app_context) { context = app_context; }
    public static void setAssetManager(AssetManager assets) {
        assetManager = assets;
    }

    public static BufferedReader getReader(String fileName) throws IOException {
        return new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
    }

    public static String loadShaderSourse(String fileName) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = reader.readLine()) != null)
                shaderSource.append(line).append("\n");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return shaderSource.toString();
    }

    public static Bitmap loadBitmap(int bitmapLocation) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        return BitmapFactory.decodeResource(context.getResources(), bitmapLocation, options);
    }

    public static int loadTexture(int textureLocation) {
        int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        Log.d("loadTexture", ""+textureHandle[0]);
        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), textureLocation, options);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            bitmap.recycle();
        }
        if (textureHandle[0] == 0)
            throw new RuntimeException("Error loading texture.");
        return textureHandle[0];
    }

    public static FloatBuffer createFloatBuffer(int size) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(size * 4);
        buffer.order(ByteOrder.nativeOrder());
        return buffer.asFloatBuffer();
    }

    public static IntBuffer createIntBuffer(int numInts) {
        return ByteBuffer.allocateDirect(numInts * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
    }

    public static class Vector3f {

        public float x = 0, y = 0, z = 0;

        public Vector3f(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void normalize() {
            float c = (float) Math.sqrt(x * x + y * y + z * z);
            x /= c;
            y /= c;
            z /= c;
        }

    }

    public static class Vector2f {

        public float x = 0, y = 0;

        public Vector2f(float x, float y) {
            this.x = x;
            this.y = y;
        }

    }

}
