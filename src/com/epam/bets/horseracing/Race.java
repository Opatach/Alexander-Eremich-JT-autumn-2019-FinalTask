/*
 * Race
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.horseracing;

/**
 * Race class.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class Race {
    /**
     * Race id
     */
    private int id;
    /**
     * Race name
     */
    private String name;
    /**
     * Race coefficient
     */
    private double coefficient;
    /**
     * Race firstWinner
     */
    private int firstPlace;
    /**
     * Race secondWinner
     */
    private int secondPlace;
    /**
     * Race thirdWinner
     */
    private int thirdPlace;

    /**
     * Create a new race.
     * @param name race name.
     * @param coefficient race coefficient.
     */
    public Race(String name, double coefficient) {
        id = -1;
        this.name = name;
        this.coefficient = coefficient;
        firstPlace = 0;
        secondPlace = 0;
        thirdPlace = 0;
    }

    /**
     * @return race id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id race id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return race name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name race name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return race coefficient
     */
    public double getCoefficient() {
        return coefficient;
    }

    /**
     * @param coefficient race coefficient
     */
    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    /**
     * @return race first winner
     */
    public int getFirstPlace() {
        return firstPlace;
    }

    /**
     * @param firstPlace race first winner
     */
    public void setFirstPlace(int firstPlace) {
        this.firstPlace = firstPlace;
    }

    /**
     * @return race second winner
     */
    public int getSecondPlace() {
        return secondPlace;
    }

    /**
     * @param secondPlace race second winner
     */
    public void setSecondPlace(int secondPlace) {
        this.secondPlace = secondPlace;
    }

    /**
     * @return race third winner
     */
    public int getThirdPlace() {
        return thirdPlace;
    }

    /**
     * @param thirdPlace race third winner
     */
    public void setThirdPlace(int thirdPlace) {
        this.thirdPlace = thirdPlace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Race race = (Race) o;
        return id == race.id &&
                Double.compare(race.coefficient, coefficient) == 0 &&
                firstPlace == race.firstPlace &&
                secondPlace == race.secondPlace &&
                thirdPlace == race.thirdPlace &&
                name.equals(race.name);
    }

    @Override
    public int hashCode() {
        return (int) (id * 71 + name.hashCode() + coefficient +
                firstPlace + secondPlace + thirdPlace);
    }

    @Override
    public String toString() {
        return "Race{" +
                "id=" + id +
                ", name='" + name +
                "', coefficient=" + coefficient +
                ", first winner=" + firstPlace +
                ", second winner=" + secondPlace +
                ", third winner=" + thirdPlace +
                '}';
    }
}
