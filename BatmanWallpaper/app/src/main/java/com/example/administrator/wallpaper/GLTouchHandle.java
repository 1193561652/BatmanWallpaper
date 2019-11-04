package com.example.administrator.wallpaper;

import android.view.MotionEvent;

public interface GLTouchHandle {

    public void TouchDown(MotionEvent event, float normalX, float normalY);
    public void TouchMove(MotionEvent event, float normalX, float normalY);
    public void TouchUp(MotionEvent event, float normalX, float normalY);
    public void Roll(float offset);
}
