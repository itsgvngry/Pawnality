import java.awt.Color;

// ============================================================
// ABSTRACTION & INHERITANCE
// This is the PARENT class. It acts as a template for all 
// playstyles. We don't instantiate this directly, but we force
// every child class to implement its own unique version of these methods.
// ============================================================
public abstract class Playstyle {

    protected String name;
    protected String trait;

    public Playstyle(String name, String trait) {
        this.name  = name;
        this.trait = trait;
    }

    // These are concrete getters shared by all children.
    public String getName()  { return name; }
    public String getTrait() { return trait; }

    // ============================================================
    // ABSTRACT METHODS (The "Contract")
    // Every subclass MUST override these. This is what makes 
    // Polymorphism possible later on!
    // ============================================================
    public abstract String getDescription();
    public abstract String getChessPiece();
    public abstract Color  getColor();
    public abstract String getGrandmaster();
    public abstract String getGrandmasterTitle();
    public abstract String getGrandmasterStyle();
}