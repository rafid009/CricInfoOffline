package client;/**
 * Created by ASUS on 3/11/2017.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sharedResources.Match;
import util.NetworkUtil;

import java.util.ArrayList;
import java.util.Hashtable;

import static java.lang.System.exit;

public class ClientMain extends Application {
    private Stage stage;
    private Scene clientFirstScene;
    private Scene clientMatchList;
    private ClientController clientFirstController;
    private MatchListController matchListController;
    private ObservableList<Match> matches = FXCollections.observableArrayList();
    private int clientId;
    private Hashtable<String, NetworkUtil> subscribedMatches = new Hashtable<>();
    private int maxNumOfMatch;
    private int currNumOfMatch=0;
    private ArrayList<Integer>subscribed = new ArrayList<>();


    public ArrayList<Integer> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(ArrayList<Integer> subscribed) {
        this.subscribed = subscribed;
    }




    public int getCurrNumOfMatch() {
        return currNumOfMatch;
    }

    public void setCurrNumOfMatch(int currNumOfMatch) {
        this.currNumOfMatch = currNumOfMatch;
    }

    public int getMaxNumOfMatch() {
        return maxNumOfMatch;
    }

    public void setMaxNumOfMatch(int maxNumOfMatch) {
        this.maxNumOfMatch = maxNumOfMatch;
    }

    public Hashtable<String, NetworkUtil> getSubscribedMatches() {
        return subscribedMatches;
    }

    public void setSubscribedMatches(Hashtable<String, NetworkUtil> subscribedMatches) {
        this.subscribedMatches = subscribedMatches;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
        exit(1);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Student");
        stage = primaryStage;
        configureFirstScene();
        configureMatchScene();
        stage.setScene(clientFirstScene);
        stage.show();

    }

    private void configureFirstScene(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("client.fxml"));
            Parent root = loader.load();
            clientFirstController = loader.getController();
            clientFirstController.setClientMain(this);
            clientFirstScene = new Scene(root, 600, 400);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void configureMatchScene(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("matchlist.fxml"));
            Parent root = loader.load();
            matchListController = loader.getController();
            matchListController.setClientMain(this);
            clientMatchList = new Scene(root, 600, 400);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showFirstScene(){
        stage.setScene(clientFirstScene);
    }

    public void showMatchList(){
        matchListController.getMatchList().setItems(matches);
        stage.setTitle(Integer.toString(clientId));
        stage.setScene(clientMatchList);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getClientFirstScene() {
        return clientFirstScene;
    }

    public void setClientFirstScene(Scene clientFirstScene) {
        this.clientFirstScene = clientFirstScene;
    }

    public Scene getClientMatchList() {
        return clientMatchList;
    }

    public void setClientMatchList(Scene clientMatchList) {
        this.clientMatchList = clientMatchList;
    }

    public ClientController getClientFirstController() {
        return clientFirstController;
    }

    public void setClientFirstController(ClientController clientFirstController) {
        this.clientFirstController = clientFirstController;
    }

    public MatchListController getMatchListController() {
        return matchListController;
    }

    public void setMatchListController(MatchListController matchListController) {
        this.matchListController = matchListController;
    }

    public ObservableList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ObservableList<Match> matches) {
        this.matches = matches;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void showNotValidId(String msg){
        matchListController.alertMessage(msg);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
