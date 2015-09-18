package de.leifaktor.robbie.api.controllers.collision;

/**
 * Created by rex on 9/18/15.
 */
public interface CollisionHandlerProvider {

    public <V,W> CollisionHandler<? super V, ? super W>
    getHandlerFor(V c1, W c2);
}
