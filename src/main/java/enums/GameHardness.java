package enums;

public enum GameHardness {
    EASY("LEVE 1", 10, 0.5, 1.5),
    MEDIUM("LEVEL 2", 5,1,1),
    HARD("LEVEL 3", 2, 1.5, .5);


    private String name;
    private int HP;
    private double gettingHitFactor;
    private double damageFactor;

    GameHardness(String name, int HP, double gettingHitFactor, double damageFactor) {
        this.name = name;
        this.HP = HP;
        this.gettingHitFactor = gettingHitFactor;
        this.damageFactor = damageFactor;
    }

    public String getName() {
        return name;
    }

    public int getHP() {
        return HP;
    }

    public double getGettingHitFactor() {
        return gettingHitFactor;
    }

    public double getDamageFactor() {
        return damageFactor;
    }
}
