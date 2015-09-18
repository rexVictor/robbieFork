package de.leifaktor.robbie.api.controllers.collision;

/**
 * Created by rex on 9/18/15.
 */
public interface CollisionHandler<V extends Collidable,W extends Collidable>{

    public void collisionHappened(V c1, W c2);

    public boolean canHandle(Collidable c1, Collidable c2);
}
