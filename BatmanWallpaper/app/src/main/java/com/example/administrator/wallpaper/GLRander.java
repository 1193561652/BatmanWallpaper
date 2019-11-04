package com.example.administrator.wallpaper;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;

import com.example.administrator.batmanwallpaper.R;
import com.example.administrator.service.WorkingServiceConnection;
import com.example.administrator.service.WorkingServiceInterface;
import com.example.administrator.wallpaper.glObject.BackgroupModel;
import com.example.administrator.wallpaper.glObject.GeneralModel;

import java.util.concurrent.atomic.AtomicInteger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import glBase.DrawModel;
import glBase.ModelManager;
import glBase.ShaderProgram;
import glBase.loadModel.Geometry;
import glBase.tools.TextureHelper;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;


public class GLRander implements GLSurfaceView.Renderer, GLTouchHandle, WorkingServiceInterface {
    private Context context = null;
    private long globalStartTime = 0;

    private TestProgram testProgram = null;
    private Geometry tea = null;

    private float[] perspMatrix = new float[16];    //透视矩阵
    private float[] viewMatrix = new float[16];

    private float[] lightVec = new float[3];

    private int textureId = 0;

    private BackgroupModel plan = null;

    private ModelManager modelManager = new ModelManager();
    private float whRate = 1.0f;

    public static final int WS_WORKING = 0;
    public static final int WS_SAFE = 1;
    public static final int WS_ERROR = 2;
    private AtomicInteger mWSState = new AtomicInteger(WS_SAFE);

    public GLRander(Context context) {
        this.context = context;
        ShaderProgram.context = context;

        Matrix.setIdentityM(perspMatrix, 0);
        Matrix.setIdentityM(viewMatrix, 0);

        //this.bindWorkingService();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        //glDepthFunc(GL_LEQUAL);
        glEnable(GL_CULL_FACE);

        testProgram = new TestProgram(context, R.raw.simple_vertex, R.raw.simple_fragment);

        Matrix.setIdentityM(perspMatrix, 0);

        //tea = new Geometry("/sdcard/Download/tea.obj");
        //boolean bOpen = tea.loadModel(true, true);

        //textureId = TextureHelper.loadTexture(context, R.drawable.tea3);

        //plan = new BackgroupModel("/sdcard/Download/plan2.obj", "/sdcard/Download/plan2.mtl", modelManager);
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BatmanWallpaper/";
        plan = new BackgroupModel(rootPath + "plan2.obj", rootPath+ "plan2.mtl", modelManager);
        plan.setTextureTurn(true);
        plan.setLoopTexture(true);
        plan.setMaxAndCurrentAndBack();
        //plan.scalc(8.0f, 8.0f, 1.0f);
        //plan.moveBy(0f, 0.0f, -5.5f);
        globalStartTime = System.nanoTime();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        whRate = ((float) width / (float) height);
        //Matrix.perspectiveM(perspMatrix, 0, 90, ((float)width) / ((float)height), 5, 10);
        float aspecRatio = width > height?((float) width / (float) height):((float) height/(float) width);
        if(width > height) {
            Matrix.orthoM(perspMatrix, 0, -1.0f, 1.0f, -1.0f / aspecRatio, 1.0f / aspecRatio, -1.0f, 1.0f);
        } else {
            Matrix.orthoM(perspMatrix, 0, -1.0f / aspecRatio, 1.0f / aspecRatio, -1.0f, 1.0f, -1.0f, 1.0f);
        }
    }

    private long gloabTime = System.nanoTime();
    private long lastReqTime = 0;
    private long lastDraw = 0;
    //private int fps = 30;
    @Override
    public void onDrawFrame(GL10 gl) {
        gloabTime = System.nanoTime();
//        if(gloabTime - lastReqTime >= (2L * 60L * 1000L * 1000L * 1000L)) {
//            String needWorking = wsConnection.needWorking();
//            if(needWorking.equals("true"))
//                mNeedWorking = true;
//            else
//                mNeedWorking = false;
//            lastReqTime = gloabTime;
//        }

//        if(lastDraw > gloabTime && lastDraw - gloabTime <= 20 * 1000 * 1000) {
//            long dec = 20 * 1000 * 1000 - (lastDraw - gloabTime);
//            for(; dec >= 1000 * 1000; dec -= 1000 * 1000) {
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        lastDraw = gloabTime;

        long dec = gloabTime - lastDraw;
        if(dec <= 1000L * 1000L * 30L) {
            try {
                Thread.sleep(1000L * 1000L * 30L - dec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(gloabTime - lastReqTime >= 5L * 1000L * 1000L * 1000L) {
            if(wsConnection != null) {
                String needWorking = wsConnection.needWorking();
                if(needWorking.equals("true")) {
                    mWSState.getAndSet(WS_WORKING);
                } else if(needWorking.equals("false")) {
                    mWSState.getAndSet(WS_SAFE);
                } else if(needWorking.equals("error_call")) {
                    bindWorkingService();
                }else {
                    mWSState.getAndSet(WS_ERROR);
                }
            }
            lastReqTime = gloabTime;
        }

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        //glEnable(GL_DEPTH_TEST);

//        long currTime = System.nanoTime();
//        float x = (currTime - gloabTime) / 1000000000.0f;
//        x *= 0.2f;
//        if(x >= 1.0f)
//            x = 0.0f;
//
//        //mytea.drawModel(perspMatrix, viewMatrix);
//        float[] textureMatrix = new float[16];
//        Matrix.setIdentityM(textureMatrix, 0);
//        Matrix.translateM(textureMatrix, 0, x, 0.0f, 0.0f);
//        plan.setTextureMatrix(textureMatrix);

        int state = mWSState.get();
        plan.setBKImage(state);

        plan.drawModel(perspMatrix, viewMatrix);


//        testProgram.useProgram();
//        testProgram.setUnifrom(perspMatrix, viewMatrix, textureId);
//        testProgram.setAttribut(tea.getVerticeBuff(), tea.getNormalBuff(), tea.getTextureBuff());
//        //glDrawArrays(GL_TRIANGLES, 0, 3);
//
////        ShortBuffer indexBuff = ShortBuffer.allocate(3).put(new short[] {
////                1, 2, 3
////        });
//
////        ByteBuffer indexBuff = ByteBuffer.allocate(3).put(new byte[] {
////                1, 2, 3
////        });
//        //indexBuff.position(0);
//        glDrawElements(GL_TRIANGLES, tea.getIndexCount(), GL_UNSIGNED_SHORT, tea.getIndexBuff());
//        //glDrawElements(GL_LINE_STRIP, tea.getIndexCount(), GL_UNSIGNED_SHORT, tea.getIndexBuff());
//        //glDrawElements(GL_LINE_STRIP, tea.getIndexCount(), GL_UNSIGNED_SHORT, tea.getIndexBuff());
    }


    private float lastX = 0.0f;
    private float lastY = 0.0f;
    @Override
    public void TouchDown(MotionEvent event, float normalX, float normalY) {
        lastX = normalX;
        lastY = normalY;
    }

    @Override
    public void TouchMove(MotionEvent event, float normalX, float normalY) {
        //viewMatrix
        //Matrix.setIdentityM(viewMatrix, 0);
        Matrix.translateM(viewMatrix, 0, normalX - lastX, -(normalY - lastY), 0.0f);

        lastX = normalX;
        lastY = normalY;
    }

    @Override
    public void TouchUp(MotionEvent event, float normalX, float normalY) {

    }

    @Override
    public void Roll(float offset) {
        plan.rollTo(offset, whRate);
    }



    WorkingServiceConnection wsConnection = null;
    @SuppressLint("NewApi")
    private void bindWorkingService() {
        if(wsConnection == null) {
            wsConnection = new WorkingServiceConnection(this);
        }
        Intent startIntent = new Intent();
        ComponentName componentName = new ComponentName("com.example.testlua", "com.example.testlua.WorkingService");
        startIntent.setComponent(componentName);
        context.startForegroundService(startIntent);
        boolean bSec = context.bindService(startIntent, wsConnection, BIND_AUTO_CREATE);
        //Log.v("MainActivity", "" + bSec);
    }


    private void reqNeedWorking() {
        if(wsConnection != null) {
            String needWorking = wsConnection.needWorking();

            if(needWorking.equals("true")) {
                mWSState.getAndSet(WS_WORKING);
            } else if(needWorking.equals("false")) {
                mWSState.getAndSet(WS_SAFE);
            } else {
                mWSState.getAndSet(WS_ERROR);
            }
        }
    }

    @Override
    public void setNeedWorking(final String needWorking) {
        if(needWorking.equals("true")) {
            mWSState.getAndSet(WS_WORKING);
        } else if(needWorking.equals("false")) {
            mWSState.getAndSet(WS_SAFE);
        } else {
            mWSState.getAndSet(WS_ERROR);
        }
    }

    @Override
    public void onDisconnect() {
        //bindWorkingService();
    }
}
