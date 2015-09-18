package de.leifaktor.robbie.api.controllers.collision;

import de.leifaktor.robbie.api.controllers.CreatureController;

import java.util.Collection;
import java.util.List;

/**
 * This interface describes what to do when collisions between Entities
 * are dectected.
 */
public interface CollisionDetector {

    /**
     * Called whenever a collision detection shall be run.
     *
     * <p>This method should usually be called every Tick.
     * <p>Implementation need to notify the colliding Entities
     * about that.
     *
     * @param collidables the collection of all collidables to check for
     *                    happened collisions
     */
    void doDetection(Collection<Collidable> collidables);
}
