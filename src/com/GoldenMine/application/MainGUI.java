package com.GoldenMine.application;

import com.GoldenMine.Utility.Point;
import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.PaletteHandler;

/**
 * Created by ehe12 on 2018-08-07.
 */
public class MainGUI {
    Palette palette;

    /*
    (click)
    void Scene::ScreenPointToRay( int screenX, int screenY, Vec3& outRayOrigin, Vec3& outRayTarget ) const
{
    const float aspect = Screen::Height() / (float)Screen::Width();
    const float halfWidth = (float)(Screen::Width()) * 0.5f;
    const float halfHeight = (float)(Screen::Height()) * 0.5f;
    const float fov = activeCamera->FOV() * (MathUtil::pi / 180.0f);

    // Normalizes screen coordinates and scales them to the FOV.
    const float dx = std::tan( fov * 0.5f ) * (screenX / halfWidth - 1.0f) / aspect;
    const float dy = std::tan( fov * 0.5f ) * (screenY / halfHeight - 1.0f);

    Matrix44 invView;
    Matrix44::Invert( activeCamera->ViewMatrix(), invView );

    const float farp = activeCamera->FarClipPlane();

    outRayOrigin = activeCamera->GetPosition();
    outRayTarget = -Vec3( -dx * farp, dy * farp, farp );

    Matrix44::TransformPoint( outRayTarget, invView, &outRayTarget );
}

     */

    public MainGUI() {
        palette = new Palette("Serendivoca", 60, new Point(1400, 900), new PaletteHandler() {
            @Override
            public void onRenderStart() {

            }

            @Override
            public void onRenderFinish() {

            }

            @Override
            public void onFrameStart() {

            }

            @Override
            public void onFrameFinish() {

            }
        });

        palette.getWindow();
    }
}
