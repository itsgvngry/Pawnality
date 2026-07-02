import java.awt.Color;

// INHERITANCE & POLYMORPHISM: Extends Playstyle.
// Represents the "Aggressive" archetype. Overrides all abstract methods to inject 
// impulsive, attack-driven personality (Mikhail Tal style) into the quiz result.

public class Tactician extends Playstyle {

    public Tactician() {
        // Calling the parent constructor to set the name and trait.
        super("The Tactician", "Aggressive");
    }

    // Each @Override below provides a UNIQUE implementation
    // for the Tactician playstyle.
    @Override
    public String getDescription() {
        return "You act on instinct and go for the win even when the risk is high. "
             + "Bold, decisive, and relentless — waiting feels like losing to you. "
             + "You strike first and figure out the details later.";
    }

    @Override public String getChessPiece()       { return "\u265F"; } // ♟
    @Override public Color  getColor()             { return new Color(210, 65, 65); }
    @Override public String getGrandmaster()       { return "Mikhail Tal"; }
    @Override public String getGrandmasterTitle()  { return "The Magician from Riga"; }
    @Override public String getGrandmasterStyle()  {
        return "Famous for wild sacrifices and brilliant attacks that completely "
             + "bewildered even the strongest opponents in the world.";
    }
}