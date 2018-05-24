package DomumViri.Level;

public enum SpawnPointType {
    Player,
    Item;

    public static SpawnPointType fromInteger(int x) {
        switch (x) {
            case 0:
                return Player;
            case 1:
                return Item;
            default:
                return null;
        }
    }
};
