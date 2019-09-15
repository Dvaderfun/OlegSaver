package com.quarnuts.olegsaver.model;

public class HospitalisationAct {
    private long id;
    private String problem;
    private String difficulty;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public HospitalisationAct(long id, String problem, String difficulty) {
        this.id = id;
        this.problem = problem;
        this.difficulty = difficulty;
    }
}
