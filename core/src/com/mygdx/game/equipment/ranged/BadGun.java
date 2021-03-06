package com.mygdx.game.equipment.ranged;

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

public class BadGun extends RangedWeapon {

    private final static String name = "Enemy Gun";
    private final static int clipSize = 6;
    private final static float shootCd = 3.0f;
    private final static float shootDelay = 0;
    private final static float reloadTime = 0.5f;
    private final static int reloadAmount = 6;
    private final static float baseDamage = 1.0f;
    private final static float recoil = 0.0f;
    private final static float knockback = 15.5f;
    private final static float projectileSpeed = 6.0f;
    private final static int projectileWidth = 22;
    private final static int projectileHeight = 22;
    private final static float lifespan = 10.5f;

    private final static int projDura = 1;

    private final static HitboxFactory onShoot = new HitboxFactory() {

        @Override
        public Hitbox makeHitbox(final Schmuck user, PlayState state, Vector2 startVelocity, float x, float y, short filter,
                                 World world, OrthographicCamera camera,
                                 RayHandler rays) {

            Hitbox proj = new HitboxImage(state, x, y, projectileWidth, projectileHeight, lifespan, projDura, 0, startVelocity,
                    filter, true, world, camera, rays, user, "orb_red");

            proj.setUserData(new HitboxData(state, world, proj) {

                public void onHit(UserData fixB) {
                    if (fixB != null) {
                    	fixB.receiveDamage(baseDamage, this.hbox.getBody().getLinearVelocity().nor().scl(knockback), 
								user.getBodyData(), true, DamageTypes.TESTTYPE1);
                    }
                    super.onHit(fixB);
                }
            });

            return null;
        }

    };

    public BadGun(Schmuck user) {
        super(user, name, clipSize, reloadTime, recoil, projectileSpeed, shootCd, shootDelay, reloadAmount, onShoot);
    }

}
