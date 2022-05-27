package enums;

public enum EntityType {
    PLAYER("player"),
    BULLET("bullet"),
    BOMB("bomb"),
    MINI_BOSS("miniBoss"), BOSS("boss"), Egg("egg"), SHOOT_ICON("shootIcon"), BOSS2("boss2");

    public String name;

    EntityType(String name) {
        this.name = name;
    }
}
