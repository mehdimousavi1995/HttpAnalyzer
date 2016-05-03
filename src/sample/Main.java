package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main extends Application {
    private String temp;
    private URL urlContainer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initUI(primaryStage);
    }

    private void initUI(Stage stage) {
        GridPane root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.setPadding(new Insets(5));
        ColumnConstraints cons1 = new ColumnConstraints();
        cons1.setHgrow(Priority.NEVER);
        root.getColumnConstraints().add(cons1);
        ColumnConstraints cons2 = new ColumnConstraints();
        cons2.setHgrow(Priority.ALWAYS);
        root.getColumnConstraints().addAll(cons1, cons2);
        RowConstraints rcons1 = new RowConstraints();
        rcons1.setVgrow(Priority.NEVER);
        RowConstraints rcons2 = new RowConstraints();
        rcons2.setVgrow(Priority.ALWAYS);
        root.getRowConstraints().addAll(rcons1, rcons2);
        Label lbl = new Label("URL :");
        TextField urlField = new TextField();
        ListView<StringBuilder> view = new ListView<>();
        Button analyzeBtn = new Button("Analyze");
        Button closeBtn = new Button("Close");

        closeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) closeBtn.getScene().getWindow();
                stage.close();
            }
        });
        urlField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                temp = urlField.getText().toString();
            }
        });
        analyzeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    urlContainer = new URL(temp);
                    HttpAnalyzer h = new HttpAnalyzer(urlContainer);
                    ObservableList<StringBuilder> items = FXCollections.observableArrayList(
                            h.getServerInfo(), h.getServersMethods(), h.getCookieInfo(),
                            h.getCacheInfo(), h.getAuthenticationInfo(), h.getStatusCode(),
                            new StringBuilder("Q7 : \n    this program supports persistent connections "));
                    view.setItems(items);
                } catch (MalformedURLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Error Dialog");
                    alert.setContentText("Ooops, there was an error!");
                    alert.showAndWait();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Error Dialog");
                    alert.setContentText("Ooops, there was an error!");
                    alert.showAndWait();
                }

            }
        });
        GridPane.setHalignment(analyzeBtn, HPos.RIGHT);
        root.add(lbl, 0, 0);
        root.add(urlField, 1, 0, 3, 1);
        root.add(view, 0, 1, 4, 2);
        root.add(analyzeBtn, 2, 3);
        root.add(closeBtn, 3, 3);
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Http Analyzer");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
