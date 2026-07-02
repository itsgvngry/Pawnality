import java.util.HashMap;
import java.util.Map;

// ============================================================
// RECURSION & EXCEPTION HANDLING
// This class controls the entire quiz flow. It holds the scores,
// moves through questions, and determines the final result.
// ============================================================
public class QuizEngine {

    private final Question[] questions; // ARRAY of all questions
    private int currentIndex = 0;
    
    // A Map (like a dictionary) to keep track of scores for each playstyle.
    private final Map<String, Integer> scores = new HashMap<>();
    private static final String[] PRIORITY = {"Tactician","Strategist","Wildcard","Fortress"};

    public QuizEngine(Question[] questions) {
        this.questions = questions;
        // Initialize all scores to 0 so we don't get null errors.
        for (String key : PRIORITY) {
            scores.put(key, 0);
        }
    }

    // Getters to help the UI know what to display.
    public Question getCurrentQuestion()  { return questions[currentIndex]; }
    public boolean  hasMoreQuestions()    { return currentIndex < questions.length; }
    public int      getCurrentIndex()     { return currentIndex; }
    public int      getTotalQuestions()   { return questions.length; }
    public int      getScoreFor(String k) { return scores.getOrDefault(k, 0); }

    // When a user picks an answer, we increment the score for that playstyle
    // and move to the next question.
    public void submitAnswer(String playstyleKey) {
        scores.put(playstyleKey, scores.get(playstyleKey) + 1);
        currentIndex++;
    }

    // ============================================================
    // PRESENTATION POINT: RECURSION 
    // Look here! Instead of using a boring 'for' or 'while' loop,
    // we use RECURSION to find the highest score. The method 
    // calls itself until it reaches the end of the array.
    // ============================================================
    public String calculateResult() {
        return findHighest(PRIORITY, 0, PRIORITY[0]);
    }

    private String findHighest(String[] keys, int index, String best) {
        // BASE CASE: If we've checked every key, return the winner!
        if (index == keys.length) return best; 
        
        String current = keys[index];
        // Check if the current key has a higher score than our current 'best'.
        if (scores.get(current) > scores.get(best)) best = current;
        
        // RECURSIVE CASE: Call the method again with the next index!
        return findHighest(keys, index + 1, best); 
    }

    // ============================================================
    // EXCEPTION HANDLING
    // This method actively checks if the user's name is valid.
    // If something is wrong, it THROWS an exception. We will catch
    // this in the MainFrame to show an error message to the user.
    // ============================================================
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

    // ============================================================
    // POLYMORPHISM
    // This is where the magic happens! Based on the result key, 
    // we return a specific subclass (Tactician, Strategist, etc.).
    // BUT we treat them all as a single 'Playstyle' type.
    // ============================================================
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