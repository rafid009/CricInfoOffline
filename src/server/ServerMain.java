package server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import sharedResources.Student;
import util.NetworkUtil;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

import static java.lang.System.exit;

public class ServerMain extends Application {

    private Stage stage;
    private Scene firstScene;
    private Scene createMatchScene;
    private ServerController firstPageController;
    private CreateMatchController createMatchController;

    private ServerMainThread serverMainThread;

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
        exit(1);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Server");
        stage = primaryStage;
        configureFirstPage();
        configureCreateMatch();
        stage.setScene(firstScene);
        stage.show();
    }

    private void configureFirstPage(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("server.fxml"));
            Parent root = loader.load();
            firstPageController = loader.getController();
            firstPageController.setServerMain(this);
            firstScene = new Scene(root, 600, 400);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void alertAboutIp(ServerMainThread serverMainThread,NetworkUtil nc, Student student){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("IP User isuues.");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            serverMainThread.getClientList().add(student);
            new ReadThreadServer(serverMainThread,nc,student);
            nc.write("ready");

        } else {
            // ... user chose CANCEL or closed the dialog
            nc.write("no");
        }
    }



    private void configureCreateMatch(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("createMatch.fxml"));
            Parent root = loader.load();
            createMatchController = loader.getController();
            createMatchController.setServerMain(this);
            createMatchScene = new Scene(root, 600, 400);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showFirstPage(){stage.setScene(firstScene);}
    public void showCreateMatchPage(){
        createMatchController.initialize();
        stage.setScene(createMatchScene);}

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getFirstScene() {
        return firstScene;
    }

    public void setFirstScene(Scene firstScene) {
        this.firstScene = firstScene;
    }

    public Scene getCreateMatchScene() {
        return createMatchScene;
    }

    public void setCreateMatchScene(Scene createMatchScene) {
        this.createMatchScene = createMatchScene;
    }

    public ServerController getFirstPageContrller() {
        return firstPageController;
    }

    public void setFirstPageContrller(ServerController firstPageController) {
        this.firstPageController = firstPageController;
    }

    public CreateMatchController getCreateMatchController() {
        return createMatchController;
    }

    public void setCreateMatchController(CreateMatchController createMatchController) {
        this.createMatchController = createMatchController;
    }

    public ServerMainThread getServerMainThread() {
        return serverMainThread;
    }

    public void setServerMainThread(ServerMainThread serverMainThread) {
        this.serverMainThread = serverMainThread;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
