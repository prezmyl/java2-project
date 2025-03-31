package cz.vsb.fei.project.file;


import cz.vsb.fei.project.data.Score;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DbConnector implements ScoreStorageInterface {
    // mozna konstantni connection string (H2 database)
    // private static final String DB_URL = "jdbc:h2:file:./scoreDB";
    private static final String JDBC_CONECTIN_STRING = "jdbc:h2:file:./score";
    // mozna private meotda pro otevreni spojeni

    public DbConnector() {

    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_CONECTIN_STRING);
    }

    @Override
    public void init() {
        // vytvoreni tabulky pokud uz neexistuje
       try (Connection con = getConnection();
                Statement stmt = con.createStatement();) {
           stmt.executeUpdate("CREATE TABLE if not exists scores(nick VARCHAR(50) NOT NULL, points INT NOT NULL);");
       } catch (SQLException e) {
            log.error("Error connecting to database: {}", e.getMessage(), e);
       }

    }

    @Override
    public void insertScore(Score score) {
        //INSERT do tabulky
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO scores (nick, points) VALUES (?, ?)")) {
            ps.setString(1, score.getNick());
            ps.setInt(2, score.getPoints());
            ps.executeUpdate();
        }
        catch (SQLException e){
            log.error("Error inserting score into database: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<Score> getAll(){
        //SELECT *
        List<Score> scores = new ArrayList<>();
        try (Connection con = getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM scores")) {
            while (rs.next()) {
                String nick = rs.getString("nick");
                int points = rs.getInt("points");
                scores.add(new Score(nick, points));
            }

        } catch (SQLException e) {
            log.error("Error getting all scores from database: {}", e.getMessage(), e);
        }
        return scores;
    }

    @Override
    public List<Score> getTopScores(int number){
        // SELECT s LIMIT
        return new ArrayList<>();
    }

    @Override
    public Score getScoreByName(String name){
        // SELECT WHERE name ==

        return new Score();
    }
}
