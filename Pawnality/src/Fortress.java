import java.awt.Color;

public class Fortress extends Playstyle {

    public Fortress() {
        super("The Fortress", "Defensive");
    }

    @Override
    public String getDescription() {
        return "You are steady, cautious, and completely unshakeable under pressure. "
             + "You protect what matters, avoid unnecessary risks, and win "
             + "by outlasting everyone else. You don't lose — you simply refuse to.";
    }

    @Override public String getChessPiece()       { return "\u265C"; } // ♜
    @Override public Color  getColor()             { return new Color(60, 180, 100); }
    @Override public String getGrandmaster()       { return "Anatoly Karpov"; }
    @Override public String getGrandmasterTitle()  { return "The Python"; }
    @Override public String getGrandmasterStyle()  {
        return "Karpov slowly squeezed opponents into submission with suffocating "
             + "positional play, leaving no room to breathe until victory was inevitable.";
    }
}
