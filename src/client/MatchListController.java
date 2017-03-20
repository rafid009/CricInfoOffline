package client;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import sharedResources.Match;

public class MatchListController {

    @FXML
    private ListView<Match> matchList;

    private ClientMain clientMain;

    @FXML
    void onClickAction(MouseEvent event) {
        if(clientMain.getMaxNumOfMatch() > clientMain.getCurrNumOfMatch()){
            Match subscribedMatch = matchList.getSelectionModel().getSelectedItem();
            System.out.println("got match");
            new MatchCommunicationThread(subscribedMatch, clientMain);
            clientMain.getMatches().remove(subscribedMatch);
            updateMatchList(clientMain.getMatches());
        }

    }

    public void alertMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.CLOSE);
        alert.show();
    }

    public void updateMatchList(ObservableList<Match> matches){
        matchList.setItems(matches);
        matchList.refresh();
    }

    public ListView<Match> getMatchList() {
        return matchList;
    }

    public void setMatchList(ListView<Match> matchList) {
        this.matchList = matchList;
    }

    public ClientMain getClientMain() {
        return clientMain;
    }

    public void setClientMain(ClientMain clientMain) {
        this.clientMain = clientMain;
    }
}
