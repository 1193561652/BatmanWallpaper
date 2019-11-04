package com.example.administrator.wallpaper;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.administrator.service.WorkingServiceConnection;
import com.example.administrator.service.WorkingServiceInterface;

//GLWallpaperService
public class GLWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new GLEngine();
    }


    //GLEngine
    public class GLEngine extends Engine {
        private WallpaperGLSurfaceView glSurfaceView = null;
        private boolean rendererSet = false;
        private GLRander rander;
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this.getApplicationContext());

            ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

            final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

            rander = new GLRander(GLWallpaperService.this.getApplicationContext());
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setPreserveEGLContextOnPause(true);
            glSurfaceView.setRenderer(rander);

            rendererSet = true;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            glSurfaceView.onWallpaperDestroy();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
//            if(rendererSet) {
//                glSurfaceView.onResume();
//            } else {
//                glSurfaceView.onPause();
//            }
            if(rendererSet) {
                if (visible) {
                    glSurfaceView.onResume();
                } else {
                    glSurfaceView.onPause();
                }
            }
        }


        @Override
        public void onOffsetsChanged(final float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
            //Log.v("GLWallpaperService", "xOffset:" + xOffset + " yOffset:" + yOffset);

            glSurfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    rander.Roll(xOffset);
                }
            });
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            //glSurfaceView.onResume();
        }



        //WallpaperGLSurfaceView
        public class WallpaperGLSurfaceView extends GLSurfaceView {
            public WallpaperGLSurfaceView(Context context) {
                super(context);
            }

            @Override
            public SurfaceHolder getHolder() {
                return getSurfaceHolder();
            }

            public void onWallpaperDestroy() {
                super.onDetachedFromWindow();
            }
        }



    }
}
