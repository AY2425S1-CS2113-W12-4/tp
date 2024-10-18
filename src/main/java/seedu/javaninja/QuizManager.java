package seedu.javaninja;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class QuizManager {
    private static final String FILE_PATH = "./data/Questions.txt";
    private static final Logger logger = Logger.getLogger(QuizManager.class.getName());
    private List<Topic> topics;
    private Quiz currentQuiz;
    private List<String> pastResults;
    private Storage storage;

    public QuizManager() {
        this.topics = new ArrayList<>();
        this.pastResults = new ArrayList<>();
        this.storage = new Storage("data/results.txt");
        loadTopicsFromFile();
        loadResultsFromFile();
    }

    private void loadTopicsFromFile() {
        File file = new File(FILE_PATH);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseTopic(line);
            }
        } catch (IOException e) {
            logger.severe("Error reading file: " + e.getMessage());
        } catch (NoSuchElementException e) {

            File directory = new File("./data");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File newFile = new File("./data/Questions.txt");
            try {
                newFile.createNewFile();
                logger.info("Created new file: Questions.txt");
            } catch (IOException ioException) {
                logger.severe("Error creating new file: " + ioException.getMessage());
            }
        }
    }

    private Topic getOrCreateTopic(String topicName) {
        for (Topic topic : topics) {
            if (topic.getName().equalsIgnoreCase(topicName)) {
                return topic;
            }
        }
        Topic newTopic = new Topic(topicName);
        topics.add(newTopic);
        return newTopic;
    }

    public void parseTopic(String line) {
        String[] parts = line.split("\\|");

        assert parts.length >= 4 : "Invalid line format, expected at least 4 parts";

        String topicName = parts[0].trim();
        String questionType = parts[1].trim();
        String questionText = parts[2].trim();
        String correctAnswer = parts[3].trim();

        Topic topic = getOrCreateTopic(topicName);
        switch (questionType) {
        case "Mcq":
            List<String> options = new ArrayList<>();
            for (int i = 4; i < parts.length; i++) {
                options.add(parts[i]);
            }
            topic.addQuestion(new Mcq(questionText, correctAnswer, options));
            break;
        default:
            logger.warning("Invalid question type: " + questionType);
        }
    }

    public void selectTopic(String topicName) {
        for (Topic topic : topics) {
            if (topic.getName().equals(topicName)) {
                startQuiz(topic);
                return;
            }
        }
        logger.warning("No such topic: " + topicName);
    }

    public void startQuiz(Topic topic) {
        currentQuiz = new Quiz(topic);
        currentQuiz.start();
        int score = currentQuiz.getScore();
        String comment = generateComment(score);
        addPastResult(score, comment);
        saveResultsToFile();
    }

    public void printTopics() {
        logger.info("Listing all available topics.");
        for (Topic topic : topics) {
            System.out.println(topic.getName());
        }
    }

    public void addTopic(Topic topic) {
        assert topic != null : "Topic should not be null";
        logger.info("Adding topic: " + topic.getName());
        topics.add(topic);
    }

    public int getTopicsCount() {
        return topics.size();
    }

    public void removeTopic(Topic topic) {
        assert topic != null : "Topic should not be null";
        logger.info("Removing topic: " + topic.getName());
        topics.remove(topic);
    }

    private void addPastResult(int score, String comment) {
        pastResults.add("Score: " + score + "%, Comment: " + comment);
    }

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

    private void saveResultsToFile() {
        try {
            storage.saveResults(pastResults);
        } catch (IOException e) {
            logger.severe("Error saving results to file: " + e.getMessage());
        }
    }

    private void loadResultsFromFile() {
        try {
            pastResults = storage.loadResults();
        } catch (IOException e) {
            logger.warning("No past results found.");
        }
    }
}
