package research;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class ResearchScreeningSystem {
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "and", "are", "as", "at", "by", "for", "from", "in",
            "is", "of", "on", "or", "that", "the", "their", "this", "to", "with",
            "using", "we", "our", "new", "research", "study", "paper", "method"
    ));

    private final List<ResearchPaper> acceptedPapers = new ArrayList<>();
    private final double similarityThreshold;
    private final double noveltyThreshold;

    public ResearchScreeningSystem() { this(70.0, 40.0); }

    public ResearchScreeningSystem(double similarityThreshold, double noveltyThreshold) {
        if (similarityThreshold < 0 || similarityThreshold > 100
                || noveltyThreshold < 0 || noveltyThreshold > 100) {
            throw new IllegalArgumentException("thresholds must be between 0 and 100");
        }
        this.similarityThreshold = similarityThreshold;
        this.noveltyThreshold = noveltyThreshold;
    }

    public void addAcceptedPaper(ResearchPaper paper) {
        if (paper == null) throw new IllegalArgumentException("paper cannot be null");
        if (acceptedPapers.stream().noneMatch(existing -> existing.getId().equals(paper.getId()))) {
            acceptedPapers.add(paper);
        }
    }

    public List<ResearchPaper> getAcceptedPapers() {
        return Collections.unmodifiableList(acceptedPapers);
    }

    public double getSimilarityThreshold() { return similarityThreshold; }
    public double getNoveltyThreshold() { return noveltyThreshold; }

    public ScreeningResult screen(ResearchPaper submission) {
        List<SimilarityResult> ranked = rankSimilarPapers(submission);
        double similarity = ranked.isEmpty() ? 0.0 : ranked.get(0).getScore();
        Set<String> existingTerms = acceptedPapers.stream()
                .flatMap(paper -> tokenize(paper.searchableText()).stream())
                .collect(Collectors.toCollection(HashSet::new));
        Set<String> submissionTerms = tokenize(submission.searchableText());
        Set<String> newTerms = new LinkedHashSet<>(submissionTerms);
        newTerms.removeAll(existingTerms);
        newTerms.removeIf(term -> term.length() < 4);

        List<String> newKeywords = submission.getKeywords().stream()
                .map(String::toLowerCase)
                .filter(keyword -> keywordIsNew(keyword, existingTerms))
                .collect(Collectors.toList());
        List<String> newConcepts = extractConcepts(submission, existingTerms, newTerms);
        double keywordNovelty = submission.getKeywords().isEmpty()
                ? 0.0
                : 100.0 * newKeywords.size() / submission.getKeywords().size();
        double uniqueTermScore = submissionTerms.isEmpty()
                ? 0.0
                : 100.0 * Math.min(1.0, newTerms.size() / (double) Math.max(5, submissionTerms.size() / 2));
        double contributionScore = meaningfulContribution(submission) ? 100.0 : 0.0;
        double novelty = clamp(0.45 * keywordNovelty + 0.30 * uniqueTermScore
                + 0.25 * contributionScore);

        boolean meaningful = meaningfulContribution(submission) || !newConcepts.isEmpty()
                || submission.getAbstractText().trim().split("\\s+").length >= 18;
        boolean accepted = (similarity >= similarityThreshold && novelty >= noveltyThreshold)
                || (similarity < similarityThreshold && meaningful);
        String explanation = buildExplanation(similarity, novelty, accepted, meaningful,
                newConcepts, ranked.isEmpty());
        return new ScreeningResult(submission, similarity, novelty,
                accepted ? ScreeningResult.Decision.ACCEPT : ScreeningResult.Decision.REJECT,
                explanation, ranked, newKeywords, newConcepts);
    }

    public List<SimilarityResult> rankSimilarPapers(ResearchPaper submission) {
        List<SimilarityResult> results = new ArrayList<>();
        for (ResearchPaper existing : acceptedPapers) {
            results.add(compare(submission, existing));
        }
        Collections.sort(results);
        return results;
    }

    public SimilarityResult compare(ResearchPaper first, ResearchPaper second) {
        Set<String> firstTerms = tokenize(first.searchableText());
        Set<String> secondTerms = tokenize(second.searchableText());
        Set<String> intersection = new LinkedHashSet<>(firstTerms);
        intersection.retainAll(secondTerms);
        Set<String> union = new HashSet<>(firstTerms);
        union.addAll(secondTerms);
        double textJaccard = union.isEmpty() ? 0.0 : 100.0 * intersection.size() / union.size();

        Set<String> firstKeywords = new HashSet<>(first.getKeywords());
        Set<String> secondKeywords = new HashSet<>(second.getKeywords());
        Set<String> keywordUnion = new HashSet<>(firstKeywords);
        keywordUnion.addAll(secondKeywords);
        Set<String> keywordIntersection = new HashSet<>(firstKeywords);
        keywordIntersection.retainAll(secondKeywords);
        double keywordJaccard = keywordUnion.isEmpty()
                ? 0.0 : 100.0 * keywordIntersection.size() / keywordUnion.size();

        String firstTitle = normalize(first.getTitle());
        String secondTitle = normalize(second.getTitle());
        double titleLcs = firstTitle.isEmpty() || secondTitle.isEmpty()
                ? 0.0
                : 100.0 * M3_DPAlgorithms.lcsLength(firstTitle, secondTitle)
                / Math.max(firstTitle.length(), secondTitle.length());
        double score = clamp(0.50 * textJaccard + 0.30 * keywordJaccard + 0.20 * titleLcs);
        return new SimilarityResult(second, score, keywordJaccard, textJaccard,
                new ArrayList<>(intersection));
    }

    public void printReport(ScreeningResult result) {
        ResearchPaper paper = result.getSubmittedPaper();
        System.out.println("\nRESEARCH SCREENING REPORT");
        System.out.println("----------------------------------------");
        System.out.println("Paper Title: " + paper.getTitle());
        System.out.println("Author: " + paper.getAuthor());
        System.out.printf(Locale.US, "\nSimilarity Score: %.2f%%%n", result.getSimilarityScore());
        System.out.printf(Locale.US, "Novelty Score: %.2f%%%n", result.getNoveltyScore());
        System.out.println("\nMost Similar Papers:");
        if (result.getSimilarPapers().isEmpty()) {
            System.out.println("  No existing papers; this is the first submission.");
        } else {
            int rank = 1;
            for (SimilarityResult match : result.getSimilarPapers().subList(
                    0, Math.min(3, result.getSimilarPapers().size()))) {
                System.out.printf(Locale.US, "%d. %s [%s] - %.2f%%%n", rank++,
                        match.getPaper().getTitle(), match.getPaper().getId(), match.getScore());
            }
        }
        System.out.println("\nNew Keywords: " + displayList(result.getNewKeywords()));
        System.out.println("New Concepts: " + displayList(result.getNewConcepts()));
        System.out.println("\nDecision: " + result.getDecision());
        System.out.println("Explanation:");
        System.out.println(result.getExplanation());
        System.out.println("Status: " + (result.isAccepted() ? "Added to ResearchHub" : "Not Added"));
        System.out.println("----------------------------------------");
    }

    private List<String> extractConcepts(ResearchPaper paper, Set<String> existing, Set<String> newTerms) {
        LinkedHashSet<String> concepts = new LinkedHashSet<>();
        for (String keyword : paper.getKeywords()) {
            if (keywordIsNew(keyword, existing)) concepts.add(keyword.toLowerCase());
        }
        concepts.addAll(newTerms.stream().filter(term -> term.length() >= 7)
                .limit(5).collect(Collectors.toList()));
        if (!paper.getContributionDescription().isEmpty()) {
            String[] words = paper.getContributionDescription().toLowerCase().split("\\s+");
            if (words.length >= 3) concepts.add("described contribution");
        }
        return new ArrayList<>(concepts);
    }

    private boolean keywordIsNew(String keyword, Set<String> existingTerms) {
        Set<String> keywordTerms = tokenize(keyword);
        return !keywordTerms.isEmpty() && keywordTerms.stream().anyMatch(term -> !existingTerms.contains(term));
    }

    private boolean meaningfulContribution(ResearchPaper paper) {
        String contribution = paper.getContributionDescription();
        String lower = contribution.toLowerCase();
        return contribution.trim().split("\\s+").length >= 5
                || lower.contains("propose") || lower.contains("introduce")
                || lower.contains("novel") || lower.contains("improve")
                || lower.contains("framework") || lower.contains("dataset");
    }

    private String buildExplanation(double similarity, double novelty, boolean accepted,
                                    boolean meaningful, List<String> concepts, boolean noCorpus) {
        List<String> reasons = new ArrayList<>();
        if (noCorpus) reasons.add("No existing papers were available for comparison");
        else if (similarity >= similarityThreshold) reasons.add("Similarity detected with existing research");
        else reasons.add("Similarity is below the configured similarity threshold");
        if (!concepts.isEmpty()) reasons.add("New concepts identified: " + displayList(concepts));
        if (novelty >= noveltyThreshold) {
            reasons.add(String.format(Locale.US, "Novelty score is above the %.2f%% threshold", noveltyThreshold));
        } else {
            reasons.add(String.format(Locale.US, "Novelty score is below the %.2f%% threshold", noveltyThreshold));
        }
        if (meaningful) reasons.add("Meaningful contribution detected");
        else reasons.add("No meaningful contribution was detected");
        if (!accepted && similarity >= similarityThreshold) {
            reasons.add("High similarity combined with insufficient novelty");
        }
        return reasons.stream().map(reason -> "- " + reason).collect(Collectors.joining("\n"));
    }

    public static Set<String> tokenize(String text) {
        Set<String> terms = new LinkedHashSet<>();
        if (text == null) return terms;
        for (String raw : text.toLowerCase().replaceAll("[^a-z0-9-]", " ").split("\\s+")) {
            if (raw.length() >= 2 && !STOP_WORDS.contains(raw)) terms.add(raw);
        }
        return terms;
    }

    private static String normalize(String text) {
        return text.toLowerCase().replaceAll("[^a-z0-9]", "");
    }

    private static String displayList(List<String> values) {
        return values.isEmpty() ? "none detected" : String.join(", ", values);
    }

    private static double clamp(double value) {
        return Math.max(0.0, Math.min(100.0, value));
    }
}