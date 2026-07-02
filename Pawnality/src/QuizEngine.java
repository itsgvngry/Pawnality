import java.util.HashMap;
import java.util.Map;

// Core quiz logic — scoring, recursion, exception handling
public class QuizEngine {

    private final Question[] questions; // ARRAY of all questions
    private int currentIndex = 0;
    private final Map<String, Integer> scores = new HashMap<>();
    private static final String[] PRIORITY = {"Tactician","Strategist","Wildcard","Fortress"};

    public QuizEngine(Question[] questions) {
        this.questions = questions;
        for (String key : PRIORITY) {
            scores.put(key, 0);
        }
    }

    public Question getCurrentQuestion()  { return questions[currentIndex]; }
    public boolean  hasMoreQuestions()    { return currentIndex < questions.length; }
    public int      getCurrentIndex()     { return currentIndex; }
    public int      getTotalQuestions()   { return questions.length; }
    public int      getScoreFor(String k) { return scores.getOrDefault(k, 0); }

    public void submitAnswer(String playstyleKey) {
        scores.put(playstyleKey, scores.get(playstyleKey) + 1);
        currentIndex++;
    }

    // RECURSION: recursively walks the priority array to find the highest score
    // instead of using a standard loop — satisfies the recursion requirement
    public String calculateResult() {
        return findHighest(PRIORITY, 0, PRIORITY[0]);
    }

    private String findHighest(String[] keys, int index, String best) {
        if (index == keys.length) return best; // base case
        String current = keys[index];
        if (scores.get(current) > scores.get(best)) best = current;
        return findHighest(keys, index + 1, best); // recursive call
    }

    // EXCEPTION HANDLING: validates user name input and throws
    // if it fails, caught in MainFrame's welcome screen listener
    public static void validateName(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter your name to begin.");
        }
        if (name.trim().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters.");
        }
        if (!name.trim().matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Name can only contain letters and spaces.");
        }
    }

    // POLYMORPHISM: returns the correct Playstyle subclass based on the result key
    // Each subclass overrides getDescription(), getColor(), etc. differently
    public static Playstyle createPlaystyle(String key) {
        switch (key) {
            case "Tactician":  return new Tactician();
            case "Strategist": return new Strategist();
            case "Wildcard":   return new Wildcard();
            case "Fortress":   return new Fortress();
            default: throw new IllegalArgumentException("Unknown playstyle: " + key);
        }
    }
}
