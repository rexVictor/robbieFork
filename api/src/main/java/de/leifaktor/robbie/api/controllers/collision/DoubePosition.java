package de.leifaktor.robbie.api.controllers.collision;

/**
 * Created by rex on 9/18/15.
 */
public class DoubePosition {

    private final double x;

    private final double y;

    public DoubePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the square of the euclidean distance between this and other.
     *
     * <p>The square is returned because of performance reasons.
     *
     * @param other the DoublePosition to calculate the distance to
     * @return {@code (this.x - other.x)^2 + (this.y - other.y)^2}
     * @throws NullPointerException if other is null
     */
    public double distanceTo(DoubePosition other) {
        double xDiff = x - other.x;
        double yDiff = y - other.y;
        return xDiff * xDiff + yDiff * yDiff;
    }
}
