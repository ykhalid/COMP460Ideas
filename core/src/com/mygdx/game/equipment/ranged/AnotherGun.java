package com.mygdx.game.equipment.ranged;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.Hitbox;
import com.mygdx.game.entities.HitboxImage;
import com.mygdx.game.entities.Schmuck;
import com.mygdx.game.entities.userdata.HitboxData;
import com.mygdx.game.entities.userdata.UserData;
import com.mygdx.game.equipment.RangedWeapon;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.status.DamageTypes;
import com.mygdx.game.util.HitboxFactory;

import box2dLight.RayHandler;

public class AnotherGun extends RangedWeapon {

	private final static String name = "Another Gun";
	private final static int clipSize = 2;
	private final static float shootCd = 0.15f;
	private final static float shootDelay = 0.0f;
	private final static float reloadTime = 0.6f;
	private final static int reloadAmount = 2;
	private final static float baseDamage = 11.0f;
	private final static float recoil = 3.0f;
	private final static float knockback = 2.0f;
	private final static float projectileSpeed = 20.0f;
	private final static int projectileWidth = 10;
	private final static int projectileHeight = 10;
	private final static float lifespan = 0.5f;

	private final static int projDura = 2;
	
	private final static int numProj = 10;
	private final static int spread = 10;
	
	private final static HitboxFactory onShoot = new HitboxFactory() {

		@Override
		public Hitbox makeHitbox(final Schmuck user, PlayState state, Vector2 startVelocity, float x, float y, short filter,
				World world, OrthographicCamera camera,
				RayHandler rays) {
			
			for (int i = 0; i < numProj; i++) {
				
				float newDegrees = (float) (startVelocity.angle() + (ThreadLocalRandom.current().nextInt(-spread, spread + 1)));
				
				Hitbox proj = new HitboxImage(state, x, y, projectileWidth, projectileHeight, lifespan, projDura, 0, startVelocity.setAngle(newDegrees),
						filter, true, world, camera, rays, user, "orb_yellow");
				proj.setUserData(new HitboxData(state, world, proj) {
					
					public void onHit(UserData fixB) {
						if (fixB != null) {
							fixB.receiveDamage(baseDamage, this.hbox.getBody().getLinearVelocity().nor().scl(knockback), 
								user.getBodyData(), true, DamageTypes.TESTTYPE1);
						}
						super.onHit(fixB);
					}
				});		
			}
			return null;
		}
		
	};
	
	public AnotherGun(Schmuck user) {
		super(user, name, clipSize, reloadTime, recoil, projectileSpeed, shootCd, shootDelay, reloadAmount, onShoot);
	}

}
