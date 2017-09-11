package ir.berimbasket.app.Entity;

import java.util.ArrayList;

/**
 * Created by mohammad hosein on 4/23/2017.
 */

public class EntityMatchScore {

    private String scoreStatus;
    private int homeScore;
    private int awayScore;
    private String homeLogo;
    private String awayLogo;
    private String homeName;
    private String awayName;

    public static ArrayList<EntityMatchScore> getRecyclerLandscape() {

        ArrayList<EntityMatchScore> matchScores = new ArrayList<>();
        for (int i = 0; i < 33; i++) {
            EntityMatchScore matchScore = new EntityMatchScore();
            matchScore.setAwayName("استقلال");
            matchScore.setHomeName("پرسپولیس");
            matchScore.setAwayScore(1);
            matchScore.setHomeScore(3);
            matchScore.setScoreStatus("پایان");
            matchScores.add(matchScore);
        }

        return matchScores;
    }

    public String getHomeLogo() {
        return homeLogo;
    }

    public void setHomeLogo(String homeLogo) {
        this.homeLogo = homeLogo;
    }

    public String getAwayLogo() {
        return awayLogo;
    }

    public void setAwayLogo(String awayLogo) {
        this.awayLogo = awayLogo;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public String getScoreStatus() {
        return scoreStatus;
    }

    public void setScoreStatus(String scoreStatus) {
        this.scoreStatus = scoreStatus;
    }
}
