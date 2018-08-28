package com.GoldenMine.graphic.elements;

import com.GoldenMine.Utility.Point;
import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.camera.Camera;
import com.GoldenMine.graphic.util.Transformation;
import com.GoldenMine.graphic.util.Window;
import java.util.*;
import org.joml.Intersectionf;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Created by ehe12 on 2018-08-10.
 */
public class MousePicker {


    public Sprite getClickedObject(Palette palette, int mouseX, int mouseY) {
        List<Sprite> sprites = palette.getSprites();

        Point paletteSize = palette.getPaletteSize();

        float x = (2.0f * mouseX) / (float)paletteSize.getX() - 1.0f;
        float y = 1.0f - (2.0f * mouseY) / (float)paletteSize.getY();
        float z = 1.0f;

        Vector3f ray_nds = new Vector3f(x, y, z);
        Vector4f ray_clip = new Vector4f(x ,y, -1f , -1f);

        Vector4f ray_eye = ray_clip.mul(palette.getWindow().getProjectionMatrix().invert(new Matrix4f()));
        ray_eye = new Vector4f(ray_eye.x, ray_eye.y, -1f, 0f);

        Vector4f ray_wor_temp = ray_eye.mul(palette.getCamera().getViewMatrix().invert(new Matrix4f()), new Vector4f());
        Vector3f ray_wor = new Vector3f(ray_wor_temp.x, ray_wor_temp.y, ray_wor_temp.z).normalize();



        return null;
    }


    /*public Sprite get(Palette palette, int mouseX, int mouseY) {
        float posX = (float) (mouseX / (0.5f * palette.getPaletteSizeX()) - 1.0f);
        float posY = (float) (mouseY / (0.5f * palette.getPaletteSizeY()) - 1.0f);

        float aspectRatio = (float) (palette.getPaletteSizeX() / palette.getPaletteSizeY());

        float fov = palette.getWindow().getFOV();
        float halfFov = fov / 2f;
        float screenHalfHeight = (float) Math.tan(halfFov);
        float screenHalfWidth = screenHalfHeight * aspectRatio;

        Vector3f pixelPos3D = new Vector3f(posX * screenHalfWidth, posY * screenHalfHeight, 1.f);

        new Transformation().getWorldMatrix(palette.getCamera().getPosition(), palette.getCamera().getRotation(), 1).;


        Intersectionf.intersectRayTriangle()

        return null;
    }*/

    public static double getValue(float x1, float y1, float x2, float y2, float x) {
        return (y1 - y2)/(x1 - x2)*(x-x1)+y1;
    }

    public static boolean isInTriangle(float x1, float y1, float x2, float y2, float x3, float y3, float p1, float p2, float tolerant) {
         float a = (y1 - y2)/(x1 - x2);

         return true;
    }

    public static boolean isInTriangle(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f point, float tolerant) {
        return false;
    }

    public static void main(String[] args) {
        HashSet<String> set1 = new HashSet<String>();
        LinkedHashSet<String> set2 = new LinkedHashSet<String>();
        TreeSet<String> set3 = new TreeSet<String>();

        set1.add("E-Business");
        set1.add("Digital Contents");
        set1.add("Web Programming");
        set1.add("Hacking Defense");

        set2.add("E-Business");
        set2.add("Digital Contents");
        set2.add("Web Programming");
        set2.add("Hacking Defense");

        set3.add("E-Business");
        set3.add("Digital Contents");
        set3.add("Web Programming");
        set3.add("Hacking Defense");

        System.out.println(set1);
        System.out.println(set2);
        System.out.println(set3);

        //set1.retainAll(set2);
    }

    static Vector3f RayIntersectsTriangle(Vector3f rayOrigin, Vector3f rayVector, float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, float v3x, float v3y, float v3z) {
        final float EPSILON = 0.0000001F;
        Vector3f edge1, edge2, h, s, q;
        float a,f,u,v;
        edge1 = new Vector3f(v2x-v1x, v2y-v1y, v2z-v1z);
        edge2 = new Vector3f(v3x-v1x, v3y-v1y, v3z-v1z);
        h = rayVector.cross(edge2);
        a = edge1.dot(h);
        if (a > -EPSILON && a < EPSILON)
            return new Vector3f(-1, -1, -1);
        f = 1/a;
        s = new Vector3f(rayOrigin.x-v1x, rayOrigin.y-v1y, rayOrigin.z-v1z);
        //s = rayOrigin - vertex0;
        u = f * s.dot(h);
        if (u < 0.0 || u > 1.0)
            return new Vector3f(-1, -1, -1);
        q = s.cross(edge1);
        v = f * rayVector.dot(q);
        if (v < 0.0 || u + v > 1.0)
            return new Vector3f(-1, -1, -1);
        // At this stage we can compute t to find out where the intersection point is on the line.
        float t = f * edge2.dot(q);
        if (t > EPSILON) // ray intersection
        {
            //outIntersectionPoint = rayOrigin + rayVector * t;
            return new Vector3f(rayOrigin).add(new Vector3f(rayVector).mul(t));
        }
        else // This means that there is a line intersection but not a ray intersection.
            return new Vector3f(rayOrigin).add(new Vector3f(rayVector).mul(t));
    }

    static Vector3f RayIntersectsTriangle(Vector3f rayOrigin,
                               Vector3f rayVector,
                                  Vector3f vertex0,
                                  Vector3f vertex1,
                                  Vector3f vertex2)
    {
        final float EPSILON = 0.0000001F;
        Vector3f edge1, edge2, h, s, q;
        float a,f,u,v;
        edge1 = new Vector3f(vertex1.x-vertex0.x, vertex1.y-vertex0.y, vertex1.z-vertex0.z);
        edge2 = new Vector3f(vertex2.x-vertex0.x, vertex2.y-vertex0.y, vertex2.z-vertex0.z);
        h = rayVector.cross(edge2);
        a = edge1.dot(h);
        if (a > -EPSILON && a < EPSILON)
            return new Vector3f(-1, -1, -1);
        f = 1/a;
        s = new Vector3f(rayOrigin.x-vertex0.x, rayOrigin.y-vertex0.y, rayOrigin.z-vertex0.z);
        //s = rayOrigin - vertex0;
        u = f * s.dot(h);
        if (u < 0.0 || u > 1.0)
            return new Vector3f(-1, -1, -1);
        q = s.cross(edge1);
        v = f * rayVector.dot(q);
        if (v < 0.0 || u + v > 1.0)
            return new Vector3f(-1, -1, -1);
        // At this stage we can compute t to find out where the intersection point is on the line.
        float t = f * edge2.dot(q);
        if (t > EPSILON) // ray intersection
        {
            //outIntersectionPoint = rayOrigin + rayVector * t;
            return new Vector3f(rayOrigin).add(new Vector3f(rayVector).mul(t));
        }
        else // This means that there is a line intersection but not a ray intersection.
            return new Vector3f(rayOrigin).add(new Vector3f(rayVector).mul(t));
    }

    public static Sprite selectGameItem(Palette palette, List<Sprite> gameItems, int mousePosX, int mousePosY, Camera camera) {
        Window window = palette.getWindow();

        Matrix4f invProjectionMatrix = new Matrix4f();
        Vector4f tmpVec = new Vector4f();
        Matrix4f invViewMatrix = new Matrix4f();
        Vector3f mouseDir = new Vector3f();

        // Transform mouse coordinates into normalized spaze [-1, 1]
        int wdwWitdh = window.getPaletteSize().getXInt();
        int wdwHeight = window.getPaletteSize().getYInt();

        float x = (float)(2 * mousePosX) / (float)wdwWitdh - 1.0f;
        float y = 1.0f - (float)(2 * mousePosY) / (float)wdwHeight;
        float z = -1.0f;

        invProjectionMatrix.set(window.getProjectionMatrix());
        invProjectionMatrix.invert();

        tmpVec.set(x, y, z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;

        Matrix4f viewMatrix = camera.getViewMatrix();
        invViewMatrix.set(viewMatrix);
        invViewMatrix.invert();
        tmpVec.mul(invViewMatrix);

        mouseDir.set(tmpVec.x, tmpVec.y, tmpVec.z);

        //new ArrayList().

        Sprite selected = null;

        //return selectGameItem(gameItems, camera.getPosition(), mouseDir);
        for(int i = 0; i < gameItems.size(); i++) {
            Sprite sprite = gameItems.get(i);

            float[] positions = sprite.getObjectElement().getPositions();
            int[] indices = sprite.getObjectElement().getIndices();

            for(int j = 0; j < indices.length; j+=3) {
                int index1 = indices[j];
                int index2 = indices[j+1];
                int index3 = indices[j+2];

                Vector3f vec = RayIntersectsTriangle(palette.getCamera().getPosition(), mouseDir,
                        positions[index1], positions[index1+1], positions[index1+2],
                        positions[index2], positions[index2+1], positions[index2+2],
                        positions[index3], positions[index3+1], positions[index3+2]);

                if(vec.x!=-1 && vec.y!=-1 && vec.z!=-1) {
                    selected = sprite;
                }
            }

        }

        return selected;
    }

    public static Sprite get(Palette palette, int mousePosX, int mousePosY, float ep) {
        Matrix4f invProjectionMatrix = new Matrix4f();
        Vector4f tmpVec = new Vector4f();
        Matrix4f invViewMatrix = new Matrix4f();
        Vector3f mouseDir = new Vector3f();

        List<Sprite> sprites = palette.getSprites();

        Camera camera = palette.getCamera();
        Vector3f cameraPos = camera.getPosition();
        //Vector3f cameraDir = camera.getViewMatrix().positiveZ(new Vector3f()).negate();


        int wdwWitdh = palette.getPaletteSize().getXInt();
        int wdwHeight = palette.getPaletteSize().getYInt();

        float x = (float) (2 * mousePosX) / (float) wdwWitdh - 1.0f;
        float y = 1.0f - (float) (2 * mousePosY) / (float) wdwHeight;
        float z = -1.0f;

        invProjectionMatrix.set(palette.getWindow().getProjectionMatrix());
        invProjectionMatrix.invert();

        tmpVec.set(x, y, z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;

        Matrix4f viewMatrix = camera.getViewMatrix();
        invViewMatrix.set(viewMatrix);
        invViewMatrix.invert();
        tmpVec.mul(invViewMatrix);

        mouseDir.set(tmpVec.x, tmpVec.y, tmpVec.z);
        //new Transformation().getModelViewMatrix()
        //System.out.println(mouseDir);

        System.out.println(mouseDir);

        Sprite candidateSprite = null;
        float minDirection = Float.MAX_VALUE;

        int index = 0;

        for (Sprite sprite : sprites) {
            index++;
            ObjectElement element = sprite.getObjectElement();

            float[] position = element.getPositions();
            int[] indices = element.getIndices();

            //System.out.println("====" + index + "====");

            //System.out.println(mouseDir);

            switch (element.getRenderObjectType()) {
                case TRIANGLE:
                    for (int i = 0; i < indices.length; i += 3) {

                        int index1 = indices[i];
                        int index2 = indices[i + 1];
                        int index3 = indices[i + 2];

                        //System.out.println("indices: " + index1 + ", " + index2 + ", " + index3);

                        float vertex1x = position[3 * index1];
                        float vertex2x = position[3 * index1 + 1];
                        float vertex3x = position[3 * index1 + 2];
                        float vertex4x = position[3 * index2];
                        float vertex5x = position[3 * index2 + 1];
                        float vertex6x = position[3 * index2 + 2];
                        float vertex7x = position[3 * index3];
                        float vertex8x = position[3 * index3 + 1];
                        float vertex9x = position[3 * index3 + 2];

                        float result = Intersectionf.intersectRayTriangle(
                                cameraPos.x, cameraPos.y, cameraPos.z,
                                mouseDir.x, mouseDir.y, mouseDir.z,
                                vertex1x, vertex2x, vertex3x,
                                vertex4x, vertex5x, vertex6x,
                                vertex7x, vertex8x, vertex9x,
                                0.00001f);

                        //System.out.println(index1 + ": " + vertex1x + ", " + vertex2x + ", " + vertex3x);
                        //System.out.println(index2 + ": " + vertex4x + ", " + vertex5x + ", " + vertex6x);
                        //System.out.println(index3 + ": " + vertex7x + ", " + vertex8x + ", " + vertex9x);


                        System.out.println("value: " + result);
                        /*float result = Intersectionf.intersectRayTriangle(
                                cameraPos.x, cameraPos.y, cameraPos.z,
                                cameraDir.x, cameraDir.y, cameraDir.z,
                                position[i*9  ], position[i*9+1], position[i*9+2],
                                position[i*9+3], position[i*9+4], position[i*9+5],
                                position[i*9+6], position[i*9+7], position[i*9+8], ep);*/
                        if (result > -1.0f) {
                            if (result < minDirection) {
                                candidateSprite = sprite;
                            }
                        }
                    }

                    break;
            }


        }

        System.out.println(candidateSprite!=null);
        return candidateSprite;
    }

    /*

    public Ray getPickRay(Palette palette, float mouseX, float mouseY) {
        List<Sprite> sprites = palette.getSprites();

        Point paletteSize = palette.getPaletteSize();

        float screenSpaceX = ((2f * mouseX) / (float)paletteSize.getX() - 1f) * aspectRatio;
        float screenSpaceY = ((-2f * mouseY) / (float)paletteSize.getY() + 1);

        float viewRatio = (float) Math.tan(palette.getWindow().getFOV() / 2f);



        screenSpaceX *= viewRatio;
        screenSpaceY *= viewRatio;

        Vector3f cameraSpaceNear = new Vector3f(screenSpaceX * zNear, screenSpaceY * zNear, -zNear);
        Vector3f cameraSpaceFar = new Vector3f(screenSpaceX * zFar, screenSpaceY * zFar, -zFar);

        Matrix4f tmpView = new Matrix4f(viewMatrix);
        Matrix4f invView = tmpView.invert();

        Vector3f worldSpaceNear = new Vector3f();
        Matrix4f.transform(invView, cameraSpaceNear, worldSpaceNear);

        Vector3f worldSpaceFar = new Vector3f();
        Matrix4f.transform(invView, cameraSpaceFar, worldSpaceFar);

        return ray.set(
                new Vector3f(worldSpaceNear.x, worldSpaceNear.y, worldSpaceNear.z),
                new Vector3f(
                        worldSpaceFar.x - worldSpaceNear.x,
                        worldSpaceFar.y - worldSpaceNear.y,
                        worldSpaceFar.z - worldSpaceNear.z
                ).normalize()
        );
    }
    */
}
