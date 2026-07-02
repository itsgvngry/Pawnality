import java.awt.Color;

// INHERITANCE & POLYMORPHISM: Extends Playstyle.
// Represents the "Creative" archetype. Overrides all abstract methods to provide 
// unexpected, revolutionary personality (Bobby Fischer style) to the final result.

public class Wildcard extends Playstyle {

    public Wildcard() {
        super("The Wildcard", "Creative");
    }

    @Override
    public String getDescription() {
        return "You thrive on surprise and originality. Nobody can predict your next move — "
             + "including sometimes yourself. When others follow the rules, "
             + "you rewrite them entirely and find a way that nobody else considered.";
    }

    @Override public String getChessPiece()       { return "\u265E"; } // ♞
    @Override public Color  getColor()             { return new Color(155, 90, 210); }
    @Override public String getGrandmaster()       { return "Bobby Fischer"; }
    @Override public String getGrandmasterTitle()  { return "The Iconoclast"; }
    @Override public String getGrandmasterStyle()  {
        return "Revolutionary and unpredictable, Fischer completely redefined chess "
             + "with ideas and moves nobody had ever seen before.";
    }
}