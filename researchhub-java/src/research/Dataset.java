package research;

public class Dataset {
    private final String id;
    private final String name;
    private final String description;
    private final int recordCount;

    public Dataset(String id, String name, String description, int recordCount) {
        if (recordCount < 0) throw new IllegalArgumentException("recordCount cannot be negative");
        this.id = id;
        this.name = name;
        this.description = description;
        this.recordCount = recordCount;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getRecordCount() { return recordCount; }

    @Override
    public String toString() {
        return id + " | " + name + " | " + recordCount + " records";
    }
}