package research;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ResearchHub {
    private final ResearchScreeningSystem screeningSystem = new ResearchScreeningSystem();
    private final Scanner scanner = new Scanner(System.in);
    private final AtomicInteger paperSequence = new AtomicInteger(4);
    private ScreeningResult lastScreening;

    public static void main(String[] args) {
        ResearchHub hub = new ResearchHub();
        hub.seedSamplePapers();
        hub.runMenu();
    }

    private void seedSamplePapers() {
        Author maya = new Author("A001", "Dr. Maya Chen", "Northbridge AI Lab", "maya@example.org");
        Author omar = new Author("A002", "Dr. Omar Ndlovu", "Cape Research Institute", "omar@example.org");
        Author priya = new Author("A003", "Dr. Priya Shah", "Open Science University", "priya@example.org");
        screeningSystem.addAcceptedPaper(new ResearchPaper("P001",
                "Graph Neural Networks for Scientific Discovery",
                "This study uses graph neural networks to model relationships between scientific entities and rank likely research links.",
                Arrays.asList("graph neural networks", "scientific discovery", "link prediction"),
                maya, 92, "We introduce a graph representation for scientific relationships."));
        screeningSystem.addAcceptedPaper(new ResearchPaper("P002",
                "AI Research Methods for Climate Data",
                "We evaluate machine learning methods for climate datasets, with attention to reproducible experiments, uncertainty, and data quality.",
                Arrays.asList("machine learning", "climate data", "uncertainty", "reproducibility"),
                omar, 89, "We compare established models on a curated climate benchmark."));
        screeningSystem.addAcceptedPaper(new ResearchPaper("P003",
                "Data Mining in Science",
                "A survey of data mining pipelines for discovering patterns in large scientific datasets and organizing evidence.",
                Arrays.asList("data mining", "scientific datasets", "pattern discovery"),
                priya, 86, "We organize common data mining strategies for research teams."));
    }

    private void runMenu() {
        System.out.println("Welcome to ResearchHub - intelligent paper screening.");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": viewPapers(); break;
                    case "2": submitPaper(); break;
                    case "3": showSimilarityAnalysis(); break;
                    case "4": showNoveltyAnalysis(); break;
                    case "5": showRanking(); break;
                    case "6": showReport(); break;
                    case "7": M1_StringAlgorithms.run(); break;
                    case "8": M2_SuffixStructures.run(); break;
                    case "9": M3_DPAlgorithms.run(); break;
                    case "10": M4_FlowAlgorithms.run(); break;
                    case "11": M5_NPAlgorithms.run(); break;
                    case "12": M6_RandomizedParallelAlgorithms.run(); break;
                    case "13": runCompleteScreeningDemo(); break;
                    case "14": runAllModules(); break;
                    case "0": running = false; break;
                    default: System.out.println("Please choose a number from 0 to 14.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Input error: " + exception.getMessage());
            } catch (Exception exception) {
                System.out.println("Operation could not be completed safely: " + exception.getMessage());
            }
        }
        System.out.println("Goodbye from ResearchHub.");
    }

    private void printMenu() {
        System.out.println("\n========================================");
        System.out.println("             RESEARCHHUB");
        System.out.println("========================================");
        System.out.println("1. View Research Papers");
        System.out.println("2. Submit New Research Paper");
        System.out.println("3. Similarity Analysis");
        System.out.println("4. Novelty Analysis");
        System.out.println("5. Similar Paper Ranking");
        System.out.println("6. Research Screening Report");
        System.out.println("7. M1 - String Algorithms");
        System.out.println("8. M2 - Suffix Structures");
        System.out.println("9. M3 - Dynamic Programming");
        System.out.println("10. M4 - Network Flow");
        System.out.println("11. M5 - NP Completeness");
        System.out.println("12. M6 - Randomized & Parallel");
        System.out.println("13. Run Complete Screening Demo");
        System.out.println("14. Run All DSA Modules");
        System.out.println("0. Exit");
        System.out.println("========================================");
        System.out.print("Select an option: ");
    }

    private void viewPapers() {
        System.out.println("\nACCEPTED RESEARCH PAPERS");
        for (ResearchPaper paper : screeningSystem.getAcceptedPapers()) {
            System.out.println(paper);
            System.out.println("  Abstract: " + paper.getAbstractText());
            System.out.println("  Keywords: " + String.join(", ", paper.getKeywords()));
        }
    }

    private void submitPaper() {
        System.out.println("\nSUBMIT NEW RESEARCH PAPER");
        String title = askRequired("Paper title: ");
        String abstractText = askRequired("Abstract: ");
        String keywordText = askRequired("Keywords (comma separated): ");
        String authorName = askRequired("Author name: ");
        String contribution = askRequired("Contribution description: ");
        Author author = new Author("SUB-" + paperSequence.get(), authorName,
                "Independent Researcher", "");
        List<String> keywords = Arrays.stream(keywordText.split(","))
                .map(String::trim).filter(value -> !value.isEmpty()).collect(Collectors.toList());
        ResearchPaper paper = new ResearchPaper("P" + String.format("%03d", paperSequence.getAndIncrement()),
                title, abstractText, keywords, author, 0.0, contribution);
        lastScreening = screeningSystem.screen(paper);
        screeningSystem.printReport(lastScreening);
        if (lastScreening.isAccepted()) screeningSystem.addAcceptedPaper(paper);
    }

    private String askRequired(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("This field is required.");
        }
    }

    private void showSimilarityAnalysis() {
        if (lastScreening == null) {
            System.out.println("Submit a paper first; similarity analysis is attached to the latest submission.");
            return;
        }
        System.out.printf(Locale.US, "Latest similarity score: %.2f%%%n", lastScreening.getSimilarityScore());
        for (SimilarityResult result : lastScreening.getSimilarPapers()) {
            System.out.printf(Locale.US, "%s -> %.2f%% (%d matched terms)%n",
                    result.getPaper().getTitle(), result.getScore(), result.getMatchedTerms().size());
        }
    }

    private void showNoveltyAnalysis() {
        if (lastScreening == null) {
            System.out.println("Submit a paper first; novelty analysis is attached to the latest submission.");
            return;
        }
        System.out.printf(Locale.US, "Latest novelty score: %.2f%%%n", lastScreening.getNoveltyScore());
        System.out.println("New keywords: " + lastScreening.getNewKeywords());
        System.out.println("New concepts: " + lastScreening.getNewConcepts());
    }

    private void showRanking() {
        if (lastScreening == null) {
            System.out.println("Submit a paper first to create a similarity ranking.");
            return;
        }
        int rank = 1;
        for (SimilarityResult result : lastScreening.getSimilarPapers()) {
            System.out.printf(Locale.US, "%d. %s [%s] %.2f%%%n", rank++,
                    result.getPaper().getTitle(), result.getPaper().getId(), result.getScore());
        }
    }

    private void showReport() {
        if (lastScreening == null) System.out.println("No screening report exists yet.");
        else screeningSystem.printReport(lastScreening);
    }

    private void runCompleteScreeningDemo() {
        Author author = new Author("DEMO", "Dr. Asha Rao", "Future Systems Lab", "");
        ResearchPaper submission = new ResearchPaper("DEMO-001",
                "Graph Neural Networks for Climate Risk Forecasting",
                "We propose an uncertainty calibrated graph neural network for climate risk forecasting. "
                        + "The approach combines scientific relationship graphs with regional climate observations "
                        + "and reports confidence intervals for every forecast.",
                Arrays.asList("graph neural networks", "climate risk", "uncertainty calibration",
                        "regional forecasting"),
                author, 0.0,
                "We introduce uncertainty calibration and regional climate risk forecasting "
                        + "to make graph predictions more actionable.");
        lastScreening = screeningSystem.screen(submission);
        screeningSystem.printReport(lastScreening);
        if (lastScreening.isAccepted()) {
            screeningSystem.addAcceptedPaper(submission);
            System.out.println("Demo result: the paper passed screening and is now searchable.");
        } else {
            System.out.println("Demo result: the paper was not added.");
        }
    }

    private void runAllModules() {
        M1_StringAlgorithms.run();
        M2_SuffixStructures.run();
        M3_DPAlgorithms.run();
        M4_FlowAlgorithms.run();
        M5_NPAlgorithms.run();
        M6_RandomizedParallelAlgorithms.run();
    }
}