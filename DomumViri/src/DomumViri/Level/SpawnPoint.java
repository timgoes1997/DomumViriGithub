package DomumViri.Level;

public class SpawnPoint {

    private float x;
    private float y;
    private SpawnPointType type;
    
    /**
     *
     * @param x
     * @param y
     * @param type
     */
    public SpawnPoint(float x, float y, SpawnPointType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public SpawnPointType getType() {
        return type;
    }
}
