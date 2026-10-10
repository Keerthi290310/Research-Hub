package research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScreeningResult {
    public enum Decision { ACCEPT, REJECT }

    private final ResearchPaper submittedPaper;
    private final double similarityScore;
    private final double noveltyScore;
    private final Decision decision;
    private final String explanation;
    private final List<SimilarityResult> similarPapers;
    private final List<String> newKeywords;
    private final List<String> newConcepts;

    public ScreeningResult(ResearchPaper submittedPaper, double similarityScore,
                           double noveltyScore, Decision decision, String explanation,
                           List<SimilarityResult> similarPapers, List<String> newKeywords,
                           List<String> newConcepts) {
        this.submittedPaper = submittedPaper;
        this.similarityScore = similarityScore;
        this.noveltyScore = noveltyScore;
        this.decision = decision;
        this.explanation = explanation;
        this.similarPapers = Collections.unmodifiableList(new ArrayList<>(similarPapers));
        this.newKeywords = Collections.unmodifiableList(new ArrayList<>(newKeywords));
        this.newConcepts = Collections.unmodifiableList(new ArrayList<>(newConcepts));
    }

    public ResearchPaper getSubmittedPaper() { return submittedPaper; }
    public double getSimilarityScore() { return similarityScore; }
    public double getNoveltyScore() { return noveltyScore; }
    public Decision getDecision() { return decision; }
    public String getExplanation() { return explanation; }
    public List<SimilarityResult> getSimilarPapers() { return similarPapers; }
    public List<String> getNewKeywords() { return newKeywords; }
    public List<String> getNewConcepts() { return newConcepts; }

    public boolean isAccepted() { return decision == Decision.ACCEPT; }
}