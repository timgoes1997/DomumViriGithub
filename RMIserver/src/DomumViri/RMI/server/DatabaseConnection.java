/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.RMI.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import DomumViri.RMI.shared.GameServer;
import DomumViri.RMI.shared.Highscore;

/**
 *
 * @author Walter Kuijlaars & Jeroen Roovers
 */
public class DatabaseConnection {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://188.166.2.36/demo";

    //  Database credentials
    static final String USER = "proftaakuser";
    static final String PASS = "proftaak";

    public static boolean NewAccount(String iUsername, String iPassword) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean gelukt = false;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql;
            sql = "INSERT INTO Account(username, password, wins) VALUES (?, ?, 0)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, iUsername);
            stmt.setString(2, iPassword);

            int returnedvalue;
            returnedvalue = stmt.executeUpdate();
            if (returnedvalue == 1) {
                gelukt = true;
            }

            stmt.close();
            conn.close();
        } catch (SQLException se) {
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }
            return gelukt;
        }
    }

    public static boolean addServer(String pName, String pIP, int pPort) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean gelukt = false;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql;
            sql = "INSERT INTO Serverinstantie(IpAdressHost, Port, Naam) VALUES (?, ? , ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pIP);
            stmt.setString(2, String.valueOf(pPort));
            stmt.setString(3, pName);

            int returnedvalue;
            returnedvalue = stmt.executeUpdate();
            if (returnedvalue == 1) {
                gelukt = true;
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
            return gelukt;
        }
    }

    public static boolean deleteServer(String pIP) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean gelukt = false;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql;
            sql = "DELETE FROM `demo`.`Serverinstantie` WHERE `IpAdressHost`=?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pIP);

            int returnedvalue;
            returnedvalue = stmt.executeUpdate();
            if (returnedvalue == 1) {
                gelukt = true;
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
        } finally {
            try {
                if (conn != null) {
                    stmt.close();
                    conn.close();
                }
            } catch (SQLException e) {
            }
            return gelukt;
        }
    }

    public static boolean login(String iUsername, String iPassword) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean gelukt = false;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql;
            sql = "SELECT ID, Username, Password FROM Account WHERE Username=? AND Password =?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, iUsername);
            stmt.setString(2, iPassword);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                gelukt = true;
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
        } finally {
            try {
                if (conn != null) {
                    stmt.close();
                    conn.close();
                }
            } catch (SQLException e) {
            }
            return gelukt;
        }
    }

    public static boolean raiseHighscore(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean gelukt = false;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql;
            sql = "SELECT Wins FROM Account WHERE Username = ? ";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            int wins = -1;
            if (rs.next()) {
                wins = rs.getInt(1);
                wins++;
            }

            String update;
            update = "UPDATE Account SET Wins = ? WHERE Username = ? ";

            stmt = conn.prepareStatement(update);
            stmt.setInt(1, wins);
            stmt.setString(2, username);

            int returnedvalue;
            returnedvalue = stmt.executeUpdate();
            if (returnedvalue == 1) {
                gelukt = true;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
        } finally {
            try {
                if (conn != null) {
                    stmt.close();
                    conn.close();
                }
            } catch (SQLException e) {
            }
            return gelukt;
        }
    }

    public static List<Highscore> getHighscores() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Highscore> hiscores = null;
        try {
            Class.forName(JDBC_DRIVER);
            hiscores = new ArrayList<>();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected");

            String sql = "SELECT Username, Wins FROM Account ORDER BY Wins DESC";
            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                //Retrieve by column name
                String username = rs.getString("Username");
                int wins = rs.getInt("Wins");
                Highscore hiscore = new Highscore(username, wins);
                hiscores.add(hiscore);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        } finally {
            try {
                if (!conn.isClosed()) {
                    rs.close();
                    conn.close();
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return hiscores;
    }

    public static void addWin(String naam) {
        Connection conn = null;
        Statement stmt = null;
        int wins = 99999;
        List<GameServer> games = new ArrayList<GameServer>();
        try {
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Wins FROM Account WHERE Username = '" + naam + "'";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                wins = rs.getInt(1);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try

        wins++;

        conn = null;
        stmt = null;
        try {
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "INSERT INTO Account(Wins) VALUES ('" + wins + "') WHERE Username = '" + naam + "'";
            stmt.execute(sql);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
        } catch (Exception e) {
        } finally {
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }

    }

    public static List<GameServer> getGames() {
        Connection conn = null;
        Statement stmt = null;
        List<GameServer> games = new ArrayList<GameServer>();
        try {
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM Serverinstantie";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while (rs.next()) {
                int id = rs.getInt(1);
                String ip = rs.getString(2);
                String port = rs.getString(3);
                String naam = rs.getString(4);
                GameServer gameserver = new GameServer(ip, port, naam);
                games.add(gameserver);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return games;
    }
}
