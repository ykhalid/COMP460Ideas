package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * These utils describe various tools for moving the camera.
 */
public class CameraStyles {

    public static void lockOnTarget(Camera camera, Vector2 target) {
        Vector3 position = camera.position;
        position.x = target.x;
        position.y = target.y;
        camera.position.set(position);
        camera.update();
    }

    public static void lerpToTarget(Camera camera, Vector2 target) {
        // a + (b - a) * lerp factor
        Vector3 position = camera.position;
        position.x = camera.position.x + (target.x - camera.position.x) * .1f;
        position.y = camera.position.y + (target.y - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();
    }
    
    int meep = 0;
    
    public static void lerpToPlayerAngle(OrthographicCamera camera, Vector2 target, float angle) {
    	// a + (b - a) * lerp factor
        Vector3 position = camera.position;
        float camAngle = -(float)Math.atan2(camera.up.x, camera.up.y) * MathUtils.radiansToDegrees + 180;
        
        float targetAngle = angle * MathUtils.radiansToDegrees;

		while(targetAngle <= 0) {
			targetAngle += 360;
		   }
		while(targetAngle > 360) {
			targetAngle -= 360;
		}
		
//		camera.up.set(0, 1, 0);
//	    camera.direction.set(0, 0, -1);
        camera.rotate((camAngle - targetAngle) + 180);

		position.x = camera.position.x + (target.x - camera.position.x) * .1f;
        position.y = camera.position.y + (target.y + Gdx.graphics.getHeight() / 2 - 200 - camera.position.y) * .1f;
        
 //       camAngle = camAngle + (targetAngle * MathUtils.radiansToDegrees - camAngle) * .1f;
        
        camera.position.set(position);
 //       camera.rotateAround(new Vector3(target.x, target.y, 0), new Vector3(0, 0, 1), 0.1f);      
  //      camera.up.set(0, 1, 0);
    //    camera.direction.set(0, 0, -1);
        camera.update();
    }

    public static void lockAverageBetweenTargets(Camera camera, Vector2 targetA, Vector2 targetB) {
        Vector3 position = camera.position;
        position.x = (targetA.x + targetB.x) / 2;
        position.y = (targetA.y + targetB.y) / 2;
        camera.position.set(position);
        camera.update();
    }

    public static void lerpAverageBetweenTargets(Camera camera, Vector2 targetA, Vector2 targetB) {
        float avgX = (targetA.x + targetB.x) / 2;
        float avgY = (targetA.y + targetB.y) / 2;

        Vector3 position = camera.position;
        position.x = camera.position.x + (avgX - camera.position.x) * .1f;
        position.y = camera.position.y + (avgY - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();
    }

    public static boolean searchFocalPoints(OrthographicCamera camera, Array<Vector2> focalPoints, Vector2 target, float threshold) {
        for(Vector2 point : focalPoints) {
            if(target.dst(point) < threshold) {
                float newZoom = (target.dst(point) / threshold) + .2f;
                camera.zoom = camera.zoom + ((newZoom > 1? 1 : newZoom) - camera.zoom) * .1f;
                CameraStyles.lerpToTarget(camera, point);
                return true;
            }
        }
        return false;
    }

    public static void shake(Camera camera, Vector2 displacement, float strength) {
        Vector3 position = camera.position;
        position.x += displacement.x * strength;
        position.y += displacement.y * strength;
        camera.position.set(position);
        camera.update();
    }
}