package com.GoldenMine.graphic.camera;

import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.util.Window;
import com.GoldenMine.graphic.elements.Sprite;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MouseBoxSelectionDetector extends CameraBoxSelectionDetector {

    private final Matrix4f invProjectionMatrix;
    
    private final Matrix4f invViewMatrix;

    private final Vector3f mouseDir;
    
    private final Vector4f tmpVec;

    public MouseBoxSelectionDetector() {
        super();
        invProjectionMatrix = new Matrix4f();
        invViewMatrix = new Matrix4f();
        mouseDir = new Vector3f();
        tmpVec = new Vector4f();
    }
    
    public Sprite selectGameItem(Palette palette, int mousePosX, int mousePosY) {
        Window window = palette.getWindow();
        Camera camera = palette.getCamera();

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

        return selectGameItem(palette, camera.getPosition(), mouseDir);
    }
    /*

    public void test(Palette palette) {
        while (Mouse.next()) {
            if (Mouse.getEventButton() == 1) {
                if (!Mouse.getEventButtonState()) {
                    Camera.get().generateViewMatrix();

                    float screenSpaceX = ((2 * Mouse.getX() / 800f) - 1.0f);// * Camera.get().getAspectRatio();
                    float screenSpaceY = ((2 * Mouse.getY() / 600f) - 1.0f);
                    float displacementRate = (float) Math.tan(Math.toRadians(palette.getWindow().getFOV() / 2));

                    screenSpaceX *= displacementRate;
                    screenSpaceY *= displacementRate;

                    Vector4f cameraSpaceNear = new Vector4f((float) (screenSpaceX * palette.getWindow().getZNEAR()), (float) (screenSpaceY * Camera.get().getNear()), (float) (-Camera.get().getNear()), 1);
                    Vector4f cameraSpaceFar = new Vector4f((float) (screenSpaceX * palette.getWindow().getZFAR()), (float) (screenSpaceY * Camera.get().getFar()), (float) (-Camera.get().getFar()), 1);

                    Matrix4f tmpView = new Matrix4f(palette.getCamera().getViewMatrix());

                    Matrix4f invertedViewMatrix = (Matrix4f) tmpView.invert();

                    Vector4f worldSpaceNear = new Vector4f();
                    //Matrix4f.transform(invertedViewMatrix, cameraSpaceNear, worldSpaceNear);
                    invertedViewMatrix.transform(cameraSpaceNear, worldSpaceNear);

                    Vector4f worldSpaceFar = new Vector4f();
                    //Matrix4f.transform(invertedViewMatrix, cameraSpaceFar, worldSpaceFar);
                    invertedViewMatrix.transform(cameraSpaceFar,worldSpaceFar);

                    Vector3f rayPosition = new Vector3f(worldSpaceNear.x, worldSpaceNear.y, worldSpaceNear.z);
                    Vector3f rayDirection = new Vector3f(worldSpaceFar.x - worldSpaceNear.x, worldSpaceFar.y - worldSpaceNear.y, worldSpaceFar.z - worldSpaceNear.z);

                    rayDirection = new Vector3f(rayDirection).rotateX(-palette.getCamera().getRotation().x).rotateY(-palette.getCamera().getRotation().y).rotateZ(-palette.getCamera().getRotation().z);

                    rayDirection.normalize();

                    rayPosition.z = -rayPosition.z;
                    // TODO: origin is a little bit off.... probably has to do with the Camera.get().getActualEyePosition....
                    Ray clickRay = new Ray(rayPosition, rayDirection);

                    // Debugg to se where the rays shoot from/at:
                    //
                    //this.worldModel.addDrawableThing(new StageObject("smallGrassPlate", new Vector(clickRay.getOrigin().x, clickRay.getOrigin().y,clickRay.getOrigin().z), new Vector(), new Vector(0.3f, 0.3f, 0.3f)));
                    //                for(float f = Camera.get().getNear(); f < Camera.get().getFar(); f += 1f) {
                    //                    this.worldModel.addDrawableThing(new StageObject("cube", new Vector(clickRay.setDistance(f).x, clickRay.getCurrentPosition().y,clickRay.getCurrentPosition().z), new Vector(), new Vector(0.3f, 0.3f, 0.3f)));
                    //                }
                    float largestEnteringValue, smallestExitingValue, temp1, temp2, closestEnteringValue = Camera.get().getFar() + 0.1f;
                    Drawable closestDrawableHit = null;

                    for (Drawable d : this.worldModel.getDrawableThings()) {
                        // Calcualte AABB for each object... needs to be moved later...
                        firstVertex = true;
                        for (Surface surface : d.getSurfaces()) {
                            for (Vertex v : surface.getVertices()) {
                                worldPosition.x = (v.x + d.getPosition().x) * d.getScale().x;
                                worldPosition.y = (v.y + d.getPosition().y) * d.getScale().y;
                                worldPosition.z = (v.z + d.getPosition().z) * d.getScale().z;
                                worldPosition = worldPosition.rotate(d.getRotation());
                                if (firstVertex) {
                                    maxX = worldPosition.x;
                                    maxY = worldPosition.y;
                                    maxZ = worldPosition.z;
                                    minX = worldPosition.x;
                                    minY = worldPosition.y;
                                    minZ = worldPosition.z;
                                    firstVertex = false;
                                } else {
                                    if (worldPosition.x > maxX) {
                                        maxX = worldPosition.x;
                                    }
                                    if (worldPosition.x < minX) {
                                        minX = worldPosition.x;
                                    }
                                    if (worldPosition.y > maxY) {
                                        maxY = worldPosition.y;
                                    }
                                    if (worldPosition.y < minY) {
                                        minY = worldPosition.y;
                                    }
                                    if (worldPosition.z > maxZ) {
                                        maxZ = worldPosition.z;
                                    }
                                    if (worldPosition.z < minZ) {
                                        minZ = worldPosition.z;
                                    }
                                }
                            }
                        }

                        // ray/slabs intersection test...


                        // clickRay.getOrigin().x + clickRay.getDirection().x * f = minX
                        // clickRay.getOrigin().x - minX = -clickRay.getDirection().x * f
                        // clickRay.getOrigin().x/-clickRay.getDirection().x - minX/-clickRay.getDirection().x = f
                        // -clickRay.getOrigin().x/clickRay.getDirection().x + minX/clickRay.getDirection().x = f
                        // (minX - clickRay.getOrigin.x) / clickRay.getDirection().x = f

                        // TODO: Fix something better here...
                        if (clickRay.getDirection().x == 0 || clickRay.getDirection().y == 0) {
                            System.out.println("Nu stötte vi på 0 i division!");
                            System.exit(1);
                        }
                        temp1 = (minX - clickRay.getOrigin().x) / clickRay.getDirection().x;
                        temp2 = (maxX - clickRay.getOrigin().x) / clickRay.getDirection().x;

                        if (temp2 > temp1) {
                            largestEnteringValue = temp1;
                            smallestExitingValue = temp2;
                        } else {
                            largestEnteringValue = temp2;
                            smallestExitingValue = temp1;
                        }

                        temp1 = (minY - clickRay.getOrigin().y) / clickRay.getDirection().y;
                        temp2 = (maxY - clickRay.getOrigin().y) / clickRay.getDirection().y;
                        if (temp2 > temp1) {
                            if (largestEnteringValue < temp1) {
                                largestEnteringValue = temp1;
                            }
                            if (smallestExitingValue > temp2) {
                                smallestExitingValue = temp2;
                            }
                        } else {
                            if (largestEnteringValue < temp2) {
                                largestEnteringValue = temp2;
                            }
                            if (smallestExitingValue > temp1) {
                                smallestExitingValue = temp1;
                            }
                        }

                        temp1 = (minZ - clickRay.getOrigin().z) / clickRay.getDirection().z;
                        temp2 = (maxZ - clickRay.getOrigin().z) / clickRay.getDirection().z;
                        if (temp2 > temp1) {
                            if (largestEnteringValue < temp1) {
                                largestEnteringValue = temp1;
                            }
                            if (smallestExitingValue > temp2) {
                                smallestExitingValue = temp2;
                            }
                        } else {
                            if (largestEnteringValue < temp2) {
                                largestEnteringValue = temp2;
                            }
                            if (smallestExitingValue > temp1) {
                                smallestExitingValue = temp1;
                            }
                        }

                        if (largestEnteringValue <= smallestExitingValue) {
                            if (largestEnteringValue < closestEnteringValue) {
                                closestEnteringValue = largestEnteringValue;
                                closestDrawableHit = d;
                            }
                        }

                    }
                    if (closestDrawableHit != null) {
                        this.worldModel.removeDrawableThing(closestDrawableHit);
                    }
                }
            }
        }
    }
    */
    /*


     */
}
