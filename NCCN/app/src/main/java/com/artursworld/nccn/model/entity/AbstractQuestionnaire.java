package com.artursworld.nccn.model.entity;


public class AbstractQuestionnaire {

    private String name;
    private int progressInPercent;

    public AbstractQuestionnaire(String name, int progressInPercent){
        this.name = name;
        this.progressInPercent = progressInPercent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgressInPercent() {
        return progressInPercent;
    }

    public void setProgressInPercent(int progressInPercent) {
        this.progressInPercent = progressInPercent;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(name);
        s.append(": ");
        s.append(progressInPercent + "%");
        return s.toString();
    }
}
