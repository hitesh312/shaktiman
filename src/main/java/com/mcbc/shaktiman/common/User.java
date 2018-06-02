package com.mcbc.shaktiman.common;

import java.util.Objects;

public class User {

    private String userid;
    private String name;
    private String email;
    private String passHash;
    private int gamesPlayed = 0;
    private int gamesWon = 0;

    public User(String userid, String name, String email, String passHash, int gamesPlayed, int gamesWon) {
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.passHash = passHash;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
    }

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return gamesPlayed == user.gamesPlayed &&
                gamesWon == user.gamesWon &&
                Objects.equals(userid, user.userid) &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(passHash, user.passHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, name, email, passHash, gamesPlayed, gamesWon);
    }

    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }
}
