/*
 * Bet
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.horseracing;

/**
 * Bet class.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class Bet {
    /**
     * Bet id
     */
    private int id;
    /**
     * Customer
     */
    private Customer customer;
    /**
     * Race
     */
    private Race race;
    /**
     * Bet type
     */
    private String type;
    /**
     * Horse number
     */
    private int horseNumber;
    /**
     * Bet amount
     */
    private int betAmount;

    /**
     * Create new bet
     * @param horseNumber horse number
     * @param betAmount bet amount
     */
    public Bet(int horseNumber, int betAmount) {
        id = -1;
        customer = null;
        race = null;
        type = "";
        this.betAmount = betAmount;
        this.horseNumber = horseNumber;
    }

    /**
     * @return bet id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id bet id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return bet customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer bet customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return bet race
     */
    public Race getRace() {
        return race;
    }

    /**
     * @param race bet race
     */
    public void setRace(Race race) {
        this.race = race;
    }

    /**
     * @return bet type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type bet type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return bet horse number
     */
    public int getHorseNumber() {
        return horseNumber;
    }

    /**
     * @param horseNumber bet horse number
     */
    public void setHorseNumber(int horseNumber) {
        this.horseNumber = horseNumber;
    }

    /**
     * @return bet amount
     */
    public int getBetAmount() {
        return betAmount;
    }

    /**
     * @param betAmount bet amount
     */
    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bet bet = (Bet) o;
        return id == bet.id &&
                betAmount == bet.betAmount &&
                customer.equals(bet.customer) &&
                race.equals(bet.race) &&
                type == bet.type;
    }

    @Override
    public int hashCode() {
        return id * 25 + betAmount;
    }

    @Override
    public String toString() {
        return "Bet{" +
                "id=" + id +
                ", customer=" + customer.getUsername() +
                " width ID " + customer.getId() +
                ", race=" + race.getName() +
                ", type=" + type +
                ", betAmount=" + betAmount +
                '}';
    }
}
