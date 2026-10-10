package research;

import java.util.Objects;

public class Author {
    private final String id;
    private final String name;
    private final String affiliation;
    private final String email;

    public Author(String id, String name, String affiliation, String email) {
        this.id = require(id, "id");
        this.name = require(name, "name");
        this.affiliation = affiliation == null ? "" : affiliation.trim();
        this.email = email == null ? "" : email.trim();
    }

    private static String require(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " cannot be empty");
        }
        return value.trim();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAffiliation() { return affiliation; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return name + (affiliation.isEmpty() ? "" : " (" + affiliation + ")");
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Author && id.equals(((Author) other).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}