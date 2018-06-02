package com.mcbc.shaktiman.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbOps {

    private static Connection con;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/shaktiman", "root", "1234");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to Database");
        }
    }

    public static void addUser(User user) {
        try {
            PreparedStatement ptsmt = con.prepareStatement("INSERT INTO users VALUES (?,?,?,?,?,?)");
            ptsmt.setString(1, user.getUserid());
            ptsmt.setString(2, user.getName());
            ptsmt.setString(3, user.getEmail());
            ptsmt.setInt(4, user.getGamesPlayed());
            ptsmt.setInt(5, user.getGamesWon());
            ptsmt.setString(6, user.getPassHash());
            ptsmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Userid already exists");
        }
    }

    public static boolean verifyLogin(String userid, String passHash) {
        try {
            PreparedStatement ptsmt = con.prepareStatement("SELECT * from users where userid=? and passHash=?");
            ptsmt.setString(1, userid);
            ptsmt.setString(2, passHash);
            ResultSet rs = ptsmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while getting user information");
        }
    }

    public static User getUser(String userid) {
        try {
            PreparedStatement ptsmt = con.prepareStatement("SELECT * from users where userid=?");
            ptsmt.setString(1, userid);
            ResultSet rs = ptsmt.executeQuery();
            if (!rs.next()) return null;
            return new User(userid, rs.getString("name"), rs.getString("email"), rs.getString("passHash"), rs.getInt("gamesPlayed"), rs.getInt("gamesWon"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while getting user information");
        }
    }

    public static void updateUser(User user) {
        try {
            PreparedStatement ptsmt = con.prepareStatement("UPDATE users set name=?,email=?,gamesPlayed=?,gamesWon=?,passHash=? where userid=?");
            ptsmt.setString(1, user.getName());
            ptsmt.setString(2, user.getEmail());
            ptsmt.setInt(3, user.getGamesPlayed());
            ptsmt.setInt(4, user.getGamesWon());
            ptsmt.setString(5, user.getPassHash());
            ptsmt.setString(6, user.getUserid());
            ptsmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while updating user information");
        }
    }


    public static void deleteUser(String userid) {
        try {
            PreparedStatement ptsmt = con.prepareStatement("DELETE from users where userid=?");
            ptsmt.setString(1, userid);
            ptsmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting user");
        }
    }
}
