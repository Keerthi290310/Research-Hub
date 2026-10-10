package research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Project {
    private final String id;
    private final String name;
    private final String description;
    private final List<Researcher> researchers = new ArrayList<>();
    private final List<Dataset> datasets = new ArrayList<>();

    public Project(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void addResearcher(Researcher researcher) { researchers.add(researcher); }
    public void addDataset(Dataset dataset) { datasets.add(dataset); }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Researcher> getResearchers() { return Collections.unmodifiableList(researchers); }
    public List<Dataset> getDatasets() { return Collections.unmodifiableList(datasets); }

    @Override
    public String toString() {
        return id + " | " + name + " | researchers=" + researchers.size()
                + ", datasets=" + datasets.size();
    }
}