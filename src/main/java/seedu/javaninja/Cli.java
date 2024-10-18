package seedu.javaninja;

import java.util.Scanner;

/* Manages input by user */
public class Cli {
    private QuizManager quizManager;

    public Cli() {
        quizManager = new QuizManager();
    }

    public void start() {
        System.out.println("Welcome to Java Ninja!");
        System.out.println("'view' to view topics");
        System.out.println("'select' to select a topic");
        System.out.println("'review' to review the past result");
        System.out.println("'help' for a list of commands");
        System.out.println("'quit' to exit the programme");
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("quit")) {
                break;
            }
            switch (input.toLowerCase()) {
            case "view":
                quizManager.printTopics();
                break;
            case "select":
                System.out.println("Enter topic name: ");
                if (scanner.hasNextLine()) {
                    String topicName = scanner.nextLine();
                    quizManager.selectTopic(topicName);
                } else {
                    System.out.println("Input error, file has no contents.");
                }
                break;
            case "review":
                System.out.println("Reviewing your past results:");
                String pastResults = quizManager.getPastResults();
                System.out.println(pastResults);
                break;
            case "help":
                printHelp();
                break;
            default:
                System.out.println("Invalid input. Type 'help' for a list of commands.");
            }
            input = scanner.nextLine();
        }
        System.out.println("Goodbye!");
    }

    // Help message
    private void printHelp() {
        System.out.println("List of available commands:");
        System.out.println("'view' - View available topics");
        System.out.println("'select' - Type 'select' and hit Enter. Then type a topic name (e.g.\"Loops\")");
        System.out.println("'review' - Review all your quiz results");
        System.out.println("'help' - Show this help message");
        System.out.println("'quit' - Exit the program");
    }
}
