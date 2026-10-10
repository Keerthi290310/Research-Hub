package research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class ResearchPaper {
    private final String id;
    private final String title;
    private final String abstractText;
    private final List<String> keywords;
    private final Author author;
    private final double relevanceScore;
    private final String contributionDescription;

    public ResearchPaper(String id, String title, String abstractText,
                         List<String> keywords, Author author) {
        this(id, title, abstractText, keywords, author, 0.0, "");
    }

    public ResearchPaper(String id, String title, String abstractText,
                         List<String> keywords, Author author,
                         double relevanceScore, String contributionDescription) {
        this.id = require(id, "id");
        this.title = require(title, "title");
        this.abstractText = require(abstractText, "abstractText");
        if (author == null) {
            throw new IllegalArgumentException("author cannot be null");
        }
        this.author = author;
        this.keywords = normalizeKeywords(keywords);
        this.relevanceScore = Math.max(0.0, Math.min(100.0, relevanceScore));
        this.contributionDescription = contributionDescription == null
                ? "" : contributionDescription.trim();
    }

    private static String require(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " cannot be empty");
        }
        return value.trim();
    }

    private static List<String> normalizeKeywords(List<String> values) {
        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        if (values != null) {
            for (String value : values) {
                if (value != null && !value.trim().isEmpty()) {
                    normalized.add(value.trim().toLowerCase());
                }
            }
        }
        return Collections.unmodifiableList(new ArrayList<>(normalized));
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAbstractText() { return abstractText; }
    public List<String> getKeywords() { return keywords; }
    public Author getAuthor() { return author; }
    public double getRelevanceScore() { return relevanceScore; }
    public String getContributionDescription() { return contributionDescription; }

    public String searchableText() {
        return title + " " + abstractText + " " + String.join(" ", keywords)
                + " " + contributionDescription;
    }

    @Override
    public String toString() {
        return id + " | " + title + " | " + author.getName();
    }
}