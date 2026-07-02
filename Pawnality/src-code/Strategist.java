import java.awt.Color;

// INHERITANCE & POLYMORPHISM: Extends Playstyle.
// Represents the "Positional" archetype. Overrides all abstract methods to deliver 
// a calculated, methodical vibe (Magnus Carlsen style) when this result is shown.

public class Strategist extends Playstyle {

    public Strategist() {
        super("The Strategist", "Positional");
    }

    // Notice how the SAME method names are used, but the 
    // content is completely different. That's Polymorphism!
    @Override
    public String getDescription() {
        return "You think three steps ahead, plan carefully, and never rush into anything. "
             + "Patient and methodical, you outmanoeuvre opponents through superior "
             + "preparation. The long game is always your game.";
    }

    @Override public String getChessPiece()       { return "\u265B"; } // ♛
    @Override public Color  getColor()             { return new Color(60, 130, 210); }
    @Override public String getGrandmaster()       { return "Magnus Carlsen"; }
    @Override public String getGrandmasterTitle()  { return "The Mozart of Chess"; }
    @Override public String getGrandmasterStyle()  {
        return "A methodical genius who dismantles opponents through flawless "
             + "preparation, endgame technique, and relentless positional pressure.";
    }
}