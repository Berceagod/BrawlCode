package problems;

public class AlgorithmicProblem extends Problem {
    private double timeLimit;

    public AlgorithmicProblem(int id, String title, String difficulty, double timeLimit) {
        super(id, title, difficulty);
        this.timeLimit = timeLimit;
    }

    @Override
    public String getProblemDetails() {
        return "Algorithmic Problem: " + title + " (" + difficulty + ") - Time Limit: " + timeLimit + "s";
    }
}
