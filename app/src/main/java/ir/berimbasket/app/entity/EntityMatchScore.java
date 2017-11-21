package ir.berimbasket.app.entity;

import android.content.Context;

import java.util.ArrayList;

import ir.berimbasket.app.R;

/**
 * Created by mohammad hosein on 4/23/2017.
 */

public class EntityMatchScore {

    private int id;
    private String date;
    private int homeScore;
    private int awayScore;
    private String homeLogo;
    private String awayLogo;
    private String homeName;
    private String awayName;

    public static ArrayList<EntityMatchScore> getRecyclerLandscape(Context context) {

        ArrayList<EntityMatchScore> matchScores = new ArrayList<>();
        for (int i = 0; i < 33; i++) {
            EntityMatchScore matchScore = new EntityMatchScore();
            matchScore.setAwayName(context.getString(R.string.entity_match_score_away_default));
            matchScore.setHomeName(context.getString(R.string.entity_match_score_home_default));
            matchScore.setAwayScore(1);
            matchScore.setHomeScore(3);
            matchScore.setDate(context.getString(R.string.entity_match_score_data_default));
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
