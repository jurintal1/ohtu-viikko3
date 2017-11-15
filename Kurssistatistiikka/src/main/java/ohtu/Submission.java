package ohtu;

import java.util.List;

public class Submission {
    private int week;
    private int hours;
    private Integer[] exercises;

    public int getHours() {
        return hours;
    }

    public Integer[] getExercises() {
        return exercises;
    }
    

    public void setWeek(int week) {
        this.week = week;
    }

    public int getWeek() {
        return week;
    }

    @Override
    public String toString() {
        return "viikko  "+ week + "\n" +
                "   tehtyjä tehtäviä yhteensä: " + exercises.length + 
                ", aikaa kului " + hours + " tuntia" + 
                " , tehdyt tehtävät: " + exercises.toString();
        
    }
    
}