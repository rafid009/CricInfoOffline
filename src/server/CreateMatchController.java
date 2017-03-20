package server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sharedResources.Match;
import sharedResources.Student;
import util.NetworkUtil;

public class CreateMatchController {

    @FXML
    private TextField team1;

    @FXML
    private TextField team2;

    @FXML
    private Button createMatch;

    private ServerMain serverMain;

    @FXML
    public void initialize(){
        team1.clear();
        team2.clear();
    }

    @FXML
    void onCreateTeamAction(ActionEvent event) {
        int matchId = serverMain.getServerMainThread().getMatchId()+1;
        serverMain.getServerMainThread().setMatchId(matchId);
        int socketId = serverMain.getServerMainThread().getMatchSocketNo();
        Match match = new Match(team1.getText(), team2.getText(), matchId, socketId );
        socketId+=1;
        serverMain.getServerMainThread().setMatchSocketNo(socketId);
        MatchThread matchThread = new MatchThread(serverMain.getServerMainThread(), serverMain, match);
        serverMain.getServerMainThread().getMatchThreadTable().put(Integer.toString(match.getMatchId()), matchThread);
        serverMain.getServerMainThread().getMatches().add(match);
        System.out.println("team created "+team1.getText()+" vs "+team2.getText());
        if(!serverMain.getServerMainThread().getClientList().isEmpty()){
            for (Student student : serverMain.getServerMainThread().getClientList()){
                NetworkUtil nc = student.getNc();
                String msg = "3::"+match.getMatchId()+"::"+match.getTeam1()+"::"+match.getTeam2()+"::"+match.getSocketNo();//flag to update match list;


                nc.write(msg);
            }
        }
        serverMain.showCreateMatchPage();

    }

    public TextField getTeam1() {
        return team1;
    }

    public void setTeam1(TextField team1) {
        this.team1 = team1;
    }

    public TextField getTeam2() {
        return team2;
    }

    public void setTeam2(TextField team2) {
        this.team2 = team2;
    }

    public Button getCreateMatch() {
        return createMatch;
    }

    public void setCreateMatch(Button createMatch) {
        this.createMatch = createMatch;
    }

    public ServerMain getServerMain() {
        return serverMain;
    }

    public void setServerMain(ServerMain serverMain) {
        this.serverMain = serverMain;
    }
}
