import java.awt.Color;

// INHERITANCE: Abstract parent class — all 4 playstyles extend this
// POLYMORPHISM: Each subclass overrides all abstract methods differently
public abstract class Playstyle {

    protected String name;
    protected String trait;

    public Playstyle(String name, String trait) {
        this.name  = name;
        this.trait = trait;
    }

    // Concrete getters
    public String getName()  { return name; }
    public String getTrait() { return trait; }

    // Abstract methods — each subclass must implement these (POLYMORPHISM)
    public abstract String getDescription();
    public abstract String getChessPiece();
    public abstract Color  getColor();
    public abstract String getGrandmaster();
    public abstract String getGrandmasterTitle();
    public abstract String getGrandmasterStyle();
}
