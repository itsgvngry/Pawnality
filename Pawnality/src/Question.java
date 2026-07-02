// ENCAPSULATION: all fields private, accessed only via public getters
// CONSTRUCTOR: takes all data at creation time
public class Question {

    private final String   questionText;
    private final String[] answers;       // ARRAY — 4 answer choices
    private final String[] playstyleKeys; // which playstyle each answer maps to

    public Question(String questionText, String[] answers, String[] playstyleKeys) {
        this.questionText  = questionText;
        this.answers       = answers;
        this.playstyleKeys = playstyleKeys;
    }

    public String   getQuestionText()        { return questionText; }
    public String[] getAnswers()             { return answers; }
    public String   getPlaystyleKey(int i)   { return playstyleKeys[i]; }
}
