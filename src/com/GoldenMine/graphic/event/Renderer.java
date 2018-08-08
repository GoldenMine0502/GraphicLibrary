package com.GoldenMine.graphic.event;

/**
 * Created by ehe12 on 2018-08-06.
 */
public interface Renderer {
    void onFrameStart();
    void onFrameFinish();

    void onRenderStart();
    void onRenderFinish();
}
