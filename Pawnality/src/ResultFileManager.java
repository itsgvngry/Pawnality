import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// FILE HANDLING: saves and loads quiz results from a text file
public class ResultFileManager {

    private static final String FILE = "pawnality_results.txt";
    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Save a new result entry — appends so history builds up over time
    public static void saveResult(String userName, String playstyle) {
        String timestamp = LocalDateTime.now().format(FMT);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write("[" + timestamp + "] " + userName + " => " + playstyle);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Could not save result: " + e.getMessage());
        }
    }

    // Load all past results from file
    public static List<String> loadHistory() {
        List<String> history = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return history;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) history.add(line);
            }
        } catch (IOException e) {
            System.out.println("Could not load history: " + e.getMessage());
        }
        return history;
    }
}
