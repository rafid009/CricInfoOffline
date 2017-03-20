package sharedResources;

import util.NetworkUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ASUS on 3/12/2017.
 */
public class Match implements Serializable{
    private String team1;
    private String team2;
    private int matchId;
    private int socketNo;
    private String winner;
    private int team1Score=0;
    private int team2Score=0;
    private ArrayList<Student> subscribers=new ArrayList<>();
    public ArrayList<ArrayList<Integer>> scores1 = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> scores2 = new ArrayList<>();

    public Match(String team1, String team2, int matchId, int socketNo) {
        this.team1 = team1;
        this.team2 = team2;
        this.matchId = matchId;
        this.socketNo = socketNo;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getSocketNo() {
        return socketNo;
    }

    public void setSocketNo(int socketNo) {
        this.socketNo = socketNo;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(int team1Score) {
        this.team1Score = team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(int team2Score) {
        this.team2Score = team2Score;
    }

    public ArrayList<ArrayList<Integer>> getScores1() {
        return scores1;
    }

    public void setScores1(ArrayList<ArrayList<Integer>> scores1) {
        this.scores1 = scores1;
    }

    public ArrayList<ArrayList<Integer>> getScores2() {
        return scores2;
    }

    public void setScores2(ArrayList<ArrayList<Integer>> scores2) {
        this.scores2 = scores2;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public ArrayList<Student> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(ArrayList<Student> subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public String toString() {
        return team1 +" VS " + team2;
    }
}
