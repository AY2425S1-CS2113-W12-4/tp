package seedu.javaninja;

import seedu.javaninja.Storage.Storage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizManager {
    private List<Topic> topics;
    private Quiz currentQuiz;
    private List<String> pastResults;
    private Storage storage; // Use Storage for saving and loading results

    public QuizManager() {
        this.topics = new ArrayList<>();
        this.pastResults = new ArrayList<>();
        this.storage = new Storage("data/results.txt"); // Define the file path for results
        loadTopics();
        loadResultsFromFile(); // Load results at the start
    }

    private void loadTopics() {
        Topic loopsTopic = new Topic("Loops");
        loopsTopic.addQuestion(new Mcq(
                "Which of the following is a loop structure in Java?",
                "b",
                List.of("a) if-else", "b) for", "c) switch", "d) try-catch")));
        topics.add(loopsTopic);
    }

    public void selectTopic(String topicName) {
        for (Topic topic : topics) {
            if (topic.getName().equals(topicName)) {
                startQuiz(topic);
                return;
            }
        }
        System.out.println("There is no such topic");
    }

    public void startQuiz(Topic topic) {
        currentQuiz = new Quiz(topic);
        currentQuiz.start();
        int score = currentQuiz.getScore();
        String comment = generateComment(score);
        addPastResult(score, comment);
        saveResultsToFile();  // Save results after the quiz
    }

    public void printTopics() {
        System.out.println("Available Topics: ");
        for (Topic topic : topics) {
            System.out.println(topic.getName());
        }
    }

    // Adds a new topic to the list of topics
    public void addTopic(Topic topic) {
        topics.add(topic);
    }

    // Returns the current number of topics
    public int getTopicsCount() {
        return topics.size();
    }

    // Removes an existing topic from the list
    public void removeTopic(Topic topic) {
        topics.remove(topic);
    }

    // Add past results (score and comment)
    private void addPastResult(int score, String comment) {
        pastResults.add("Score: " + score + "%, Comment: " + comment);
    }

    // Generates a comment based on the quiz score
    private String generateComment(int score) {
        if (score >= 90) {
            return "Excellent!";
        } else if (score >= 70) {
            return "Good job!";
        } else if (score >= 50) {
            return "Needs improvement.";
        } else {
            return "Better luck next time!";
        }
    }

    // Review past results
    public String getPastResults() {
        if (pastResults.isEmpty()) {
            return "No past results available. You haven't completed any quizzes yet.";
        }

        StringBuilder results = new StringBuilder();
        for (String result : pastResults) {
            results.append(result).append("\n");
        }
        return results.toString();
    }

    // Save the past results to file
    private void saveResultsToFile() {
        try {
            storage.saveResults(pastResults);
        } catch (IOException e) {
            System.out.println("Error saving results to file.");
        }
    }

    // Load past results from file
    private void loadResultsFromFile() {
        try {
            pastResults = storage.loadResults();
        } catch (IOException e) {
            System.out.println("No past results found.");
        }
    }
}
