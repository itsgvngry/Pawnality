// ============================================================
// ENCAPSULATION
// Look closely at these fields. They are all 'private' and 'final'.
// We hide the internal data and only expose them via public getters.
// This keeps our data safe from unwanted changes!
// ============================================================
public class Question {

    private final String   questionText;
    private final String[] answers;       // ARRAY: Stores 4 choices
    private final String[] playstyleKeys; // ARRAY: Maps each answer to a playstyle

    // Constructor - we set the data ONCE when the object is created.
    public Question(String questionText, String[] answers, String[] playstyleKeys) {
        this.questionText  = questionText;
        this.answers       = answers;
        this.playstyleKeys = playstyleKeys;
    }

    // Public Getters - the only way to access the private data.
    public String   getQuestionText()        { return questionText; }
    public String[] getAnswers()             { return answers; }
    public String   getPlaystyleKey(int i)   { return playstyleKeys[i]; }
}