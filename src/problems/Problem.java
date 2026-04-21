package problems;

public abstract class Problem {
    protected int id;
    protected String title;
    protected String difficulty;

    public Problem(int id, String title, String difficulty) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDifficulty() { return difficulty; }

    public abstract String getProblemDetails();
}
