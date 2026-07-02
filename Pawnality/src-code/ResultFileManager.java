import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// ============================================================
// FILE HANDLING (I/O)
// This class is dedicated to reading and writing our quiz history
// to a physical text file on the computer's hard drive.
// ============================================================
public class ResultFileManager {

    private static final String FILE = "pawnality_results.txt";
    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ============================================================
    // WRITING TO A FILE
    // We use BufferedWriter for efficiency. Notice the 'true' in 
    // FileWriter? That means we are APPENDING to the file, 
    // so we don't overwrite old history!
    // ============================================================
    public static void saveResult(String userName, String playstyle) {
        String timestamp = LocalDateTime.now().format(FMT);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write("[" + timestamp + "] " + userName + " => " + playstyle);
            bw.newLine();
        } catch (IOException e) {
            // If something goes wrong (e.g., disk full), we catch it here.
            System.out.println("Could not save result: " + e.getMessage());
        }
    }

    // ============================================================
    // READING FROM A FILE
    // We use BufferedReader to read the file line by line.
    // We check if the file exists first to avoid a crash!
    // ============================================================
    public static List<String> loadHistory() {
        List<String> history = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return history; // No file? Return an empty list.
        
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