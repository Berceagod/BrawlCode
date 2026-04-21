package problems;

public class DatabaseProblem extends Problem {
    private String schemaName;

    public DatabaseProblem(int id, String title, String difficulty, String schemaName) {
        super(id, title, difficulty);
        this.schemaName = schemaName;
    }

    @Override
    public String getProblemDetails() {
        return "SQL: " + title + " (" + difficulty + ") - Schema: " + schemaName;
    }
}
