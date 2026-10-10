package research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimilarityResult implements Comparable<SimilarityResult> {
    private final ResearchPaper paper;
    private final double score;
    private final double keywordScore;
    private final double textScore;
    private final List<String> matchedTerms;

    public SimilarityResult(ResearchPaper paper, double score, double keywordScore,
                            double textScore, List<String> matchedTerms) {
        this.paper = paper;
        this.score = score;
        this.keywordScore = keywordScore;
        this.textScore = textScore;
        this.matchedTerms = Collections.unmodifiableList(new ArrayList<>(matchedTerms));
    }

    public ResearchPaper getPaper() { return paper; }
    public double getScore() { return score; }
    public double getKeywordScore() { return keywordScore; }
    public double getTextScore() { return textScore; }
    public List<String> getMatchedTerms() { return matchedTerms; }

    @Override
    public int compareTo(SimilarityResult other) {
        return Double.compare(other.score, score);
    }
}