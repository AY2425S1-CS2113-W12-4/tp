package seedu.javaninja;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

class QuizManagerTest {

    private QuizManager quizManager;
    private final String RESULTS_FILE_PATH = "data/results.txt";

    @BeforeEach
    public void setUp() {
        File file = new File(RESULTS_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        quizManager = new QuizManager();
        quizManager.addTopic(new Topic("Default Topic"));
    }

    @Test
    public void selectTopic_invalidTopicName_displaysError() {
        // Prepare a scanner with simulated user input
        ByteArrayInputStream input = new ByteArrayInputStream("InvalidTopicName\n".getBytes());
        Scanner scanner = new Scanner(input);

        quizManager.selectTopic("InvalidTopicName", scanner);

        scanner.close();
    }

    @Test
    public void getPastResults_withResults_returnsCorrectResults() {
        // Add a topic with a question
        Topic topic = new Topic("Java Basics");
        topic.addQuestion(new Mcq("What is Java?", "a",
                List.of("a) A programming language", "b) A type of coffee", "c) A car brand")));

        quizManager.addTopic(topic);

        ByteArrayInputStream input = new ByteArrayInputStream("b\n".getBytes());
        Scanner scanner = new Scanner(input);

        quizManager.startQuiz(topic, scanner);

        String expectedResult = "Score: 0%, Comment: Better luck next time!\n";
        assertEquals(expectedResult, quizManager.getPastResults());

        scanner.close();
    }

    @Test
    public void saveResults_savesToFileCorrectly() throws IOException {
        // Add a topic and question
        Topic topic = new Topic("Java Basics");
        topic.addQuestion(new Mcq("What is Java?", "a",
                List.of("a) A programming language", "b) A type of coffee", "c) A car brand")));

        quizManager.addTopic(topic);

        ByteArrayInputStream input = new ByteArrayInputStream("b\n".getBytes());
        Scanner scanner = new Scanner(input);

        quizManager.startQuiz(topic, scanner);

        String savedResults = Files.readString(Path.of(RESULTS_FILE_PATH));
        String expectedSavedResults = "Score: 0%, Comment: Better luck next time!\n";
        assertEquals(expectedSavedResults, savedResults);

        scanner.close();
    }

    @Test
    public void loadResultsFromFile_correctlyLoadsResults() throws IOException {
        String previousResult = "Score: 80%, Comment: Good job!\n";
        Files.writeString(Path.of(RESULTS_FILE_PATH), previousResult);

        quizManager = new QuizManager();

        String loadedResults = quizManager.getPastResults();
        assertEquals(previousResult, loadedResults);
    }
}
