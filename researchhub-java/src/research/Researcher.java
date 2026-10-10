package research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Researcher {
    private final String id;
    private final String name;
    private final String email;
    private final List<String> expertise;

    public Researcher(String id, String name, String email, List<String> expertise) {
        this.id = id;
        this.name = name;
        this.email = email == null ? "" : email;
        this.expertise = expertise == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(expertise));
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getExpertise() { return expertise; }

    @Override
    public String toString() { return id + " | " + name + " | " + expertise; }
}