import java.awt.Color;

// INHERITANCE: extends Playstyle
// POLYMORPHISM: overrides all abstract methods with Tactician-specific behaviour
public class Tactician extends Playstyle {

    public Tactician() {
        super("The Tactician", "Aggressive");
    }

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
