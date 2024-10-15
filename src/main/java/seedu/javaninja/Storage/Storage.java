package seedu.javaninja.Storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
        File file = new File(filePath);
        File directory = file.getParentFile();

        // Check if the directory exists, if not, create it
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Loads quiz results from the file.
     * @return A list of past quiz results loaded from the file.
     * @throws IOException If there is an error reading the file.
     */
    public List<String> loadResults() throws IOException {
        List<String> results = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return results;  // Return an empty list if the file doesn't exist
        }

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            results.add(scanner.nextLine());
        }
        scanner.close();
        return results;
    }

    /**
     * Saves the quiz results to the file.
     * @param results The list of past results to save.
     * @throws IOException If there is an error writing to the file.
     */
    public void saveResults(List<String> results) throws IOException {
        File file = new File(filePath);
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }

        FileWriter writer = new FileWriter(filePath);
        for (String result : results) {
            writer.write(result + "\n");
        }
        writer.close();
    }
}
