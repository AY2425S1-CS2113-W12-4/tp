package seedu.javaninja;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/* Manages input by user */
public class Cli {
    private QuizManager quizManager;
    private Scanner scanner;

    public Cli() {
        quizManager = new QuizManager();
        scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        System.out.println("Welcome to Java Ninja!");
        System.out.println("'view' to view topics");
        System.out.println("'select [topic]' to select a topic");
        System.out.println("'review' to review past results");
        System.out.println("'help' for a list of commands");
        System.out.println("'quit' to exit the program");

        try {
            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    System.out.println("No input detected. Exiting.");
                    break;
                }

                String input = scanner.nextLine().trim();
                String command = processCommand(input);

                if (command.equals("quit")) {
                    break;
                }

                switch (command) {
                case "view":
                    quizManager.printTopics();
                    break;
                case "select":
                    selectTopic(input);
                    break;
                case "review":
                    System.out.println("Reviewing your past results:");
                    System.out.println(quizManager.getPastResults());
                    break;
                case "help":
                    printHelp();
                    break;
                case "add":
                    quizManager.addQuestionByUser(input);
                    break;
                default:
                    System.out.println("Invalid input. Type 'help' for a list of commands.");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Error: No input found. Exiting.");
        } finally {
            System.out.println("Goodbye!");
            scanner.close();
        }
    }

    private void selectTopic(String input) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) {
            System.out.println("Please specify a topic to select.");
        } else {
            quizManager.selectTopic(parts[1], scanner);
        }
    }

    public String processCommand(String input) {
        return input.split(" ")[0].toLowerCase();
    }

    // Help message
    private void printHelp() {
        System.out.println("List of available commands:");
        System.out.println("'view' - View available topics");
        System.out.println("'select [topic]' - Start a quiz on a specific topic");
        System.out.println("'review' - Review all your quiz results");
        System.out.println("'help' - Show this help message");
        System.out.println("'quit' - Exit the program");
    }
}
