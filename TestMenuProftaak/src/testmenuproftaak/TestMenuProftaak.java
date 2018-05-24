package testmenuproftaak;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author walter
 */
public class TestMenuProftaak extends Application{

    Stage window;
    Scene menu, singleplayer, highscore, settings; //exit
    
    int SceneWidth;
    int SceneLength;
    
    int Decibel;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SceneWidth = 960;
        SceneLength = 540;
        Decibel = 100;
        
        window = primaryStage;
        primaryStage.setTitle("Domum Viri");
        
        //Label S1.1
        Label lblMenu = new Label("Menu:");
        
        //Button S1.1
        Button btnGoToSinglePlayer = new Button("Singleplayer");
        btnGoToSinglePlayer.setPrefWidth(300);
        btnGoToSinglePlayer.setOnAction(e -> {
            window.setScene(singleplayer);
            window.setWidth(SceneWidth);
            window.setHeight(SceneLength);
                });
        
        //Button S1.2
        Button btnGoToMultiPlayer = new Button("Multiplayer");
        btnGoToMultiPlayer.setPrefWidth(300);
        btnGoToMultiPlayer.setOnAction(e -> MessageBox.show("DomusMessage", "Dit gedeelte is nog niet geimplementeerd!"));
        
        //Button S1.3
        Button btnGoToHighScore = new Button("Highscore");
        btnGoToHighScore.setPrefWidth(300);
        btnGoToHighScore.setOnAction(e -> {
            window.setScene(highscore);
            window.setWidth(SceneWidth);
            window.setHeight(SceneLength);
                });
        
        //Button S1.4
        Button btnGoToSettings = new Button("Settings");
        btnGoToSettings.setPrefWidth(300);
        btnGoToSettings.setOnAction(e -> {
            window.setScene(settings);
            window.setWidth(SceneWidth);
            window.setHeight(SceneLength);
                });
        
        //Button S1.5
        Button btnQuitGame = new Button("Quit");
        btnQuitGame.setPrefWidth(300);
        btnQuitGame.setOnAction(e -> System.exit(0));
        
        //Scene 1 MENU
        VBox MenuLayout = new VBox(20);
        MenuLayout.getChildren().addAll(lblMenu, btnGoToSinglePlayer, /*btnGoToMultiPlayer,*/ btnGoToHighScore, btnGoToSettings, btnQuitGame);
        menu = new Scene(MenuLayout, SceneWidth, SceneLength); //960-540 & 1280-720
        
        //Button S2.1
        Button btnExitSinglePlayer = new Button("Back");
        btnExitSinglePlayer.setOnAction(e -> {
            window.setScene(menu);
            window.setWidth(SceneWidth);
            window.setHeight(SceneLength);
                });
        
        //Scene 2 SINGLEPLAYER 
        VBox SinglePlayerLayout = new VBox();
        SinglePlayerLayout.getChildren().add(btnExitSinglePlayer);
        singleplayer = new Scene(SinglePlayerLayout, SceneWidth, SceneLength);
        
        //Label S4.0
        Label lblLeeg0 = new Label("");
        lblLeeg0.setId("label-content");
        Label lblLeeg1 = new Label("");
        Label lblLeeg2 = new Label("");
        
        //Label S4.1
        Label lblTitelHighscore = new Label("Highscore:");
        
        //Label S4.2
        Label lblHighscore = new Label("<score>");
        lblHighscore.setId("label-content");
        
        //Button S4.1
        Button btnExitHighscore = new Button("Back");
        btnExitHighscore.setPrefWidth(300);
        btnExitHighscore.setOnAction(e -> {
            window.setScene(menu);
            window.setWidth(SceneWidth);
            window.setHeight(SceneLength);
                });
        
        //Scene 4 Highscore 
        VBox HighscoreLayout = new VBox();
        HighscoreLayout.getChildren().addAll(lblTitelHighscore, lblLeeg0, lblHighscore, lblLeeg1, lblLeeg2, btnExitHighscore);
        highscore = new Scene(HighscoreLayout, SceneWidth, SceneLength);
        
        //Label S5.1
        Label lblTitelSettings = new Label("Settings:");
        
        //Label S5.2
        Label lblResolutie = new Label("Kies resolutie:");
        lblResolutie.setId("label-content");
        
        //Label leeg
        Label lblLeegheid1 = new Label("");
        lblLeegheid1.setId("label-content");
        Label lblLeegheid2 = new Label("");
        lblLeegheid2.setId("label-content");
        Label lblLeegheid4 = new Label("");
        lblLeegheid4.setId("label-content");
        
        //DropDownMenu S5.1
        ObservableList<String> options = 
        FXCollections.observableArrayList(
            "960-540",
            "1280-720"
        );
        ComboBox comboBox = new ComboBox(options);
        comboBox.setPrefWidth(300);
        comboBox.getSelectionModel().select(0);
        comboBox.setId("combobox");
        
        //Label S5.3
        Label lblGeluid = new Label("Kies geluidsterkte:");
        lblGeluid.setId("label-content");
        
        //DropDownMenu S5.2
        ObservableList<String> optionsGeluid = 
        FXCollections.observableArrayList(
            "100%",
            "75%",
            "50%",
            "25%",
            "Off"
        );
        ComboBox comboBoxGeluid = new ComboBox(optionsGeluid);
        comboBoxGeluid.setPrefWidth(300);
        comboBoxGeluid.getSelectionModel().select(0);
        comboBoxGeluid.setId("combobox");
        
        //Button S5.1
        Button btnSaveSettings = new Button("Save");
        btnSaveSettings.setPrefWidth(300);
        btnSaveSettings.setOnAction(e -> 
        {
            int Id = comboBox.getSelectionModel().getSelectedIndex();
            if (Id == 0)
            {
                SceneWidth = 960;
                SceneLength = 540;
                window.setWidth(SceneWidth);
                window.setHeight(SceneLength);
            }
            else
            {
                SceneWidth = 1280;
                SceneLength = 720;
                window.setWidth(SceneWidth);
                window.setHeight(SceneLength);
                //window.setScene(menu);
            }
            int ID = comboBoxGeluid.getSelectionModel().getSelectedIndex();
            switch (ID) {
                case 0:
                    Decibel = 100;
                    break;
                case 1:
                    Decibel = 75;
                    break;
                case 2:
                    Decibel = 50;
                    break;
                case 3:
                    Decibel = 25;
                    break;
                default:
                    Decibel = 0;
                    break;
            }

        });
        
        //Button S5.2
        Button btnExitSettings = new Button("Back");
        btnExitSettings.setPrefWidth(300);
        btnExitSettings.setOnAction(e -> {
            window.setScene(menu);
            window.setWidth(SceneWidth);
            window.setHeight(SceneLength);
                });
        
        //Scene 5 SETTINGS 
        VBox SettingsLayout = new VBox();
        SettingsLayout.getChildren().addAll(lblTitelSettings, lblResolutie, comboBox, lblLeegheid1, lblGeluid, comboBoxGeluid, lblLeegheid4, btnSaveSettings, lblLeegheid2, btnExitSettings);
        settings = new Scene(SettingsLayout, SceneWidth, SceneLength);
        
        //CSS
        menu.getStylesheets().add(TestMenuProftaak.class.getResource("Domus.css").toExternalForm());
        highscore.getStylesheets().add(TestMenuProftaak.class.getResource("Domus.css").toExternalForm());
        settings.getStylesheets().add(TestMenuProftaak.class.getResource("Domus.css").toExternalForm());

        //Display scene 1 at first
        window.setScene(menu);
        window.setWidth(SceneWidth);
        window.setHeight(SceneLength);
        window.setTitle("Domum Viri");
        window.show();
    }


}
