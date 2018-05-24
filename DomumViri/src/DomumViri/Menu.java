package DomumViri;

import DomumViri.Database.DatabaseConnection;
import DomumViri.Main.Game;
import DomumViri.Managers.AudioManager;
import DomumViri.Entities.*;
import DomumViri.Main.GameMode;
import DomumViri.RMI.client.*;
import DomumViri.RMI.shared.*;
import DomumViri.SocketServer.SocketServer;
import DomumViri.User.Account;
import DomumViri.User.Team;
import DomumViri.VoicechatClient.Client_voice;
import DomumViri.VoicechatClient.client;
import DomumViri.VoicechatServer.Server_voice;
import com.mysql.jdbc.log.Log;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author walter
 */
public class Menu extends Application {

    private static final String LABELCONTENT = "label-content";
    private static final String RED = "#Listview_RED";
    private static final String BLUE = "#Listview_BLUE";
    private static final String DOMUS = "\\Layout\\Domus.css";
    private static final String GAMENAME = "Domum Viri";

    private Stage window;
    private Scene login, menu, singleplayer, highscore, settings, gameover, newaccount, lobby, hostgame, teams, direct, teamspeak; //exit
    private Game game;
    private Label lblLogin, lblUsername, lblPassword, lblMenu, lblWinnaar, lblVKills, lblVDeaths, lblNew, lblnewUsername, lblnewPassword;
    private Label lblHighscore1, lblHighscore2, lblHighscore3, lblHighscore4, lblHighscore5;
    private Button btnLogin, btnCreateNewAccount, btnContinu, btnGoToSinglePlayer, btnGoToMultiPlayer, btnGoToHighScore, btnGoToSettings, btnQuitGame, btnCreateAccount, btnBack, btnBack2;
    private TextField tbUsername, tbnewUsername;
    private PasswordField tbPassword, tbnewPassword;
    private boolean isHosting;
    ListView<GameServer> lvGames;

    private int sceneWidth;
    private int sceneLength;
    private int gameTime;
    private boolean fullscreen;
    private GameMode mode;
    private String spelerNaam;

    private Client_voice voiceClient;
    private Server_voice voiceServer;

    private Account account;

    private InetAddress ip;
    private int port;
    private SocketServer server;
    private Thread serverThread;

    private double decibel;

    private RMIclient rmiclient;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Verkrijgt het volume
     *
     * @return
     */
    public double getDecibel() {
        return decibel;
    }

    /**
     * Stelt het volume in.
     *
     * @param decibel
     */
    public void setDecibel(double decibel) {
        this.decibel = decibel;
    }

    @Override
    public void start(Stage primaryStage) {
        //ImageIcon img = new ImageIcon("C:\\Users\\walter\\Desktop\\School\\jaar2\\PTS3\\Opdracht\JCCD3\DomumViri\build\classes\Images\Domum_Icon.png");

        rmiclient = new RMIclient(RMIclient.HOSTNAME, RMIclient.PORTNUMBER);
        sceneWidth = 960;
        sceneLength = 540;
        fullscreen = false;

        gameTime = 300;
        decibel = 0;

        try {
            ip = InetAddress.getLocalHost();
            port = 1337;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }

        AudioManager.play(decibel);

        window = primaryStage;
        window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent we) {
                if (game != null) {
                    game.forceStop();
                }
                if (server != null) {
                    server.stopServer();
                }
                if (serverThread != null) {
                    serverThread.interrupt();
                }
                if (voiceClient != null) {
                    voiceClient.stopVoiceClient();
                }
                if (voiceServer != null) {
                    voiceServer.stopVoiceServer();
                }
                System.exit(0);
            }
        });
        primaryStage.setTitle(GAMENAME);

        //Labels S0.1
        lblLogin = new Label("Log in:");
        lblUsername = new Label("Username:");
        lblUsername.setId(LABELCONTENT);
        lblPassword = new Label("Password:");
        lblPassword.setId(LABELCONTENT);

        //Textfields S0.1
        tbUsername = new TextField("");
        tbUsername.setMaxWidth(600);
        tbPassword = new PasswordField();
        tbPassword.setMaxWidth(600);

        //Button S0.1
        btnLogin = new Button("Inloggen");
        btnLogin.setPrefWidth(300);
        btnLogin.setOnAction(e -> {
            //DatabaseConnection.test();
            boolean gelukt = false;
            //gelukt = DatabaseConnection.login(tbUsername.getText(), tbPassword.getText());
            gelukt = true;//rmiclient.login(tbUsername.getText(), tbPassword.getText());
            if (gelukt) {
                account = new Account(tbUsername.getText().toLowerCase());
                window.setScene(menu);
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Inlogfout");
                a.setHeaderText("");
                a.setContentText("De naam of het wachtwoord is verkeerd ingevoerd.");
                a.showAndWait();
            }

        });

        //Button S0.2
        btnCreateNewAccount = new Button("Nieuw account");
        btnCreateNewAccount.setPrefWidth(300);
        btnCreateNewAccount.setOnAction(e -> {
            window.setScene(newaccount);
        });

        //Scene 0 LOGIN
        VBox loginLayout = new VBox(20);
        loginLayout.getChildren().addAll(lblLogin, lblUsername, tbUsername, lblPassword, tbPassword, btnLogin, btnCreateNewAccount);
        login = new Scene(loginLayout, sceneWidth, sceneLength); //960-540 & 1280-720

        //Labels TEAMS
        Label lblPlayers = new Label("Teams:");
        //Label lblPlayers = new Label("Spelers:");
        //lblPlayers.setId(LABELCONTENT);
        //ListView<String> lvPlayers = new ListView<>();
        //lvPlayers.setPrefWidth(50);
        //lvPlayers.setPrefHeight(150);
        //ObservableList<String> data = FXCollections.observableArrayList();
        //lvPlayers.setItems(data);

        //Buttons TEAMS      
        spelerNaam = "testSpeler";
        Button btnTeamRed = new Button("Team red");
        btnTeamRed.setPrefWidth(300);
        btnTeamRed.setOnAction(e -> {
            //data.set(0, spelerNaam + " -> RED");
            mode = GameMode.MPTeams;
            account.setTeam(Team.Red);
            newGame(mode, ip, port);
        });

        Button btnTeamBlue = new Button("Team blue");
        btnTeamBlue.setPrefWidth(300);
        btnTeamBlue.setOnAction(e -> {
            //data.set(0, spelerNaam + " -> BLUE");
            mode = GameMode.MPTeams;
            account.setTeam(Team.Blue);
            newGame(mode, ip, port);
        });

        Button btnStart = new Button("Start");
        btnStart.setPrefWidth(300);
        btnStart.setOnAction(e -> {
            mode = GameMode.MPFFA;
            newGame(mode, ip, port);
        });

        Button btnBack3 = new Button("Terug");
        btnBack3.setPrefWidth(300);
        btnBack3.setOnAction(e -> {
            window.setScene(lobby);
        });

        //Scene TEAMS
        VBox TeamLayout = new VBox(20);
        TeamLayout.getChildren().addAll(lblPlayers, btnTeamRed, btnTeamBlue, btnStart, btnBack3);
        teams = new Scene(TeamLayout, sceneWidth, sceneLength); //960-540 & 1280-720

        //Labels S0,5.1
        lblNew = new Label("Maak nieuw account:");
        lblnewUsername = new Label("Username:");
        lblnewUsername.setId(LABELCONTENT);
        lblnewPassword = new Label("Password:");
        lblnewPassword.setId(LABELCONTENT);

        //Textfields S0,5.1
        tbnewUsername = new TextField("");
        tbnewUsername.setMaxWidth(600);
        tbnewPassword = new PasswordField();
        tbnewPassword.setMaxWidth(600);

        //Buttons
        btnCreateAccount = new Button("Crëer account");
        btnCreateAccount.setPrefWidth(300);
        btnCreateAccount.setOnAction(e -> {
            if (rmiclient.register(tbnewUsername.getText(), tbnewPassword.getText())) {
                window.setScene(login);
            }
            //DatabaseConnection.NewAccount(tbnewUsername.getText(), tbnewPassword.getText());

        });

        btnBack = new Button("Terug");
        btnBack.setPrefWidth(300);
        btnBack.setOnAction(e -> {
            window.setScene(login);
        });

        btnBack2 = new Button("Terug");
        btnBack2.setPrefWidth(300);
        btnBack2.setOnAction(e -> {
            window.setScene(login);
        });

        //Scene NEWACCOUNT
        VBox newAccountLayout = new VBox(20);
        newAccountLayout.getChildren().addAll(lblNew, lblnewUsername, tbnewUsername, lblnewPassword, tbnewPassword, btnCreateAccount, btnBack2);
        newaccount = new Scene(newAccountLayout, sceneWidth, sceneLength); //960-540 & 1280-720

        //Labels HOSTGAME
        Label lblHost = new Label("Host game");
        Label lblNaam = new Label("Naam game:");
        lblNaam.setId(LABELCONTENT);
        TextField tbNaam = new TextField();
        tbNaam.setPrefWidth(50);
        Label lblTeams = new Label("Teams on:");
        lblTeams.setId(LABELCONTENT);
        CheckBox cbTeams = new CheckBox();

        //Buttons HOSTGAME
        Button btnCreateGame = new Button("Create");
        btnCreateGame.setPrefWidth(300);
        btnCreateGame.setOnAction(e -> {
            serverThread = new Thread(() -> {
                try {
                    if (cbTeams.isSelected()) {
                        server = new SocketServer(GameMode.MPTeams);
                    } else {
                        server = new SocketServer(GameMode.MPFFA);
                    }
                } catch (Exception ex) {
                    if (server != null) {
                        server.stopServer();
                    }
                }
            });
            serverThread.start();
            isHosting = false;
            window.setScene(teams);
        });
        Button btnBackFromHost = new Button("back");
        btnBackFromHost.setPrefWidth(300);
        btnBackFromHost.setOnAction(e -> {
            window.setScene(lobby);
        });

        //Scene HOSTGAME
        VBox HostLayout = new VBox(20);
        HostLayout.getChildren().addAll(lblHost, lblNaam, tbNaam, lblTeams, cbTeams, btnCreateGame, btnBackFromHost);
        hostgame = new Scene(HostLayout, sceneWidth, sceneLength); //960-540 & 1280-720

        //Scene direct
        Label lblDirect = new Label("Connect");
        Label lblIp = new Label("IpAdress:");
        lblIp.setId(LABELCONTENT);
        TextField tbIp = new TextField();
        tbIp.setPrefWidth(50);
        Label lblPort = new Label("Port:");
        lblPort.setId(LABELCONTENT);
        TextField tbPort = new TextField();
        tbPort.setPrefWidth(50);

        Button btnJoinDirect = new Button("Connect");
        btnJoinDirect.setPrefWidth(300);
        btnJoinDirect.setOnAction(e -> {
            try {
                if (tbIp.getText() != null && tbPort.getText() != null) {
                    mode = GameMode.MP;
                    ip = InetAddress.getByName(tbIp.getText());
                    port = Integer.parseInt(tbPort.getText());
                    newGame(mode, ip, port);
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        VBox DirectLayout = new VBox(20);
        DirectLayout.getChildren().addAll(lblDirect, lblIp, tbIp, lblPort, tbPort, btnJoinDirect, btnBackFromHost);
        direct = new Scene(DirectLayout, sceneWidth, sceneLength); //960-540 & 1280-720

        //Labels LOBBY
        Label lblServers = new Label("Join a game:");

        //Listview LOBBY
        lvGames = new ListView<>();
        lvGames.setPrefWidth(20);
        lvGames.setPrefHeight(180);
        //ObservableList<String> items = FXCollections.observableArrayList("Test");
        //lvGames.setItems(items);

        //Buttons LOBBY
        Button btnConnect = new Button("Connect");
        btnConnect.setPrefWidth(300);
        btnConnect.setOnAction(e -> {
            try {
                GameServer g = lvGames.getSelectionModel().getSelectedItem();
                System.out.println("Connecting to server:");
                System.out.println(g.getName());
                System.out.println(g.getIPaddress() + ":" + g.getPort());
                ip = InetAddress.getByName(g.getIPaddress());
                port = Integer.parseInt(g.getPort());

                window.setScene(teams);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
            window.setScene(teams);
        });
        Button btnDirect = new Button("Direct");
        btnDirect.setPrefWidth(300);
        btnDirect.setOnAction(e -> {
            window.setScene(direct);
        });
        Button btnHostGame = new Button("Host game");
        btnHostGame.setPrefWidth(300);
        btnHostGame.setOnAction(e -> {
            window.setScene(hostgame);
        });
        Button btnBackFromLobby = new Button("Back");
        btnBackFromLobby.setPrefWidth(300);
        btnBackFromLobby.setOnAction(e -> {
            window.setScene(menu);
        });

        //Scene LOBBY
        VBox LobbyLayout = new VBox(20);
        LobbyLayout.getChildren().addAll(lblServers, lvGames, btnConnect, btnDirect, btnHostGame, btnBackFromLobby);
        lobby = new Scene(LobbyLayout, sceneWidth, sceneLength); //960-540 & 1280-720

        //Label S1.1
        lblMenu = new Label(GAMENAME);

        //Button S1.0 TOEVOEGEN AAN EEN NIEUWE SCENE
        btnContinu = new Button("Continue");
        btnContinu.setPrefWidth(300);
        btnContinu.setOnAction(e -> {
            getGame();
        });

        //Button S1.1
        btnGoToSinglePlayer = new Button("Singleplayer");
        btnGoToSinglePlayer.setPrefWidth(300);
        btnGoToSinglePlayer.setOnAction(e -> {
            mode = GameMode.SinglePlayer;
            newGame(mode, ip, port);
        });

        //Button S1.2
        btnGoToMultiPlayer = new Button("Multiplayer");
        btnGoToMultiPlayer.setPrefWidth(300);
        btnGoToMultiPlayer.setOnAction(e -> {
            window.setScene(lobby);
            List<GameServer> games = rmiclient.getAllGameServers();//DatabaseConnection.getGames();
            ObservableList<GameServer> olist = FXCollections.observableArrayList();
            olist.addAll(games);
            lvGames.setItems(olist);
//            ObservableList<String> gameNames = FXCollections.observableArrayList();
//
//            List<String> hoi = new ArrayList<String>();
//            for (GameServer game : games) {
//                gameNames.add(game.getName());
//            }
//            lvGames.setItems(gameNames);
//            //newGame(true);
        });

        //Button S1.3
        btnGoToHighScore = new Button("Highscore");
        btnGoToHighScore.setPrefWidth(300);
        btnGoToHighScore.setOnAction(e -> {
            window.setScene(highscore);
            List<Highscore> Highscores = rmiclient.getAllHighscores();//DatabaseConnection.getHighscore();
            List<String> hoi = new ArrayList<>();
            for (Highscore h : Highscores) {
                hoi.add(h.getUsername() + " - " + h.getWins() + " wins");
            }
            lblHighscore1.setText("1st ->" + hoi.get(0));
            lblHighscore2.setText("2st ->" + hoi.get(1));
            lblHighscore3.setText("3st ->" + hoi.get(2));
            lblHighscore4.setText("4st ->" + hoi.get(3));
            lblHighscore5.setText("5st ->" + hoi.get(4));
            //window.setFullScreen(fullscreen);
            //window.setWidth(sceneWidth);
            //window.setHeight(sceneLength);
        });

        //Button S1.4
        btnGoToSettings = new Button("Settings");
        btnGoToSettings.setPrefWidth(300);
        btnGoToSettings.setOnAction(e -> {
            window.setScene(settings);
            //window.setFullScreen(fullscreen);
            //window.setWidth(sceneWidth);
            //window.setHeight(sceneLength);
        });

        //Button S1.5
        btnQuitGame = new Button("Quit");
        btnQuitGame.setPrefWidth(300);
        btnQuitGame.setOnAction(e -> System.exit(0));

        //Scene 1 MENU        
        VBox menuLayout = new VBox(20);
        menuLayout.getChildren().addAll(lblMenu, btnGoToSinglePlayer, btnGoToMultiPlayer, btnGoToHighScore, btnGoToSettings, btnBack, btnQuitGame);
        menu = new Scene(menuLayout, sceneWidth, sceneLength); //960-540 & 1280-720

        //Button S2.1
        Button btnExitSinglePlayer = new Button("Back");
        btnExitSinglePlayer.setOnAction(e -> {
            window.setScene(menu);
            window.setFullScreen(fullscreen);
            window.setWidth(sceneWidth);
            window.setHeight(sceneLength);
        });

        //Scene 2 SINGLEPLAYER 
        VBox singlePlayerLayout = new VBox();
        singlePlayerLayout.getChildren().add(btnExitSinglePlayer);
        singleplayer = new Scene(singlePlayerLayout, sceneWidth, sceneLength);

        //Label S4.0
        Label lblLeeg0 = new Label("");
        lblLeeg0.setId(LABELCONTENT);
        Label lblLeeg1 = new Label("");

        //Label S4.1
        Label lblTitelHighscore = new Label("Highscore:");

        //Label S4.2
        lblHighscore1 = new Label("<score>");
        lblHighscore1.setId(LABELCONTENT);
        lblHighscore2 = new Label("<score>");
        lblHighscore2.setId(LABELCONTENT);
        lblHighscore3 = new Label("<score>");
        lblHighscore3.setId(LABELCONTENT);
        lblHighscore4 = new Label("<score>");
        lblHighscore4.setId(LABELCONTENT);
        lblHighscore5 = new Label("<score>");
        lblHighscore5.setId(LABELCONTENT);

        //Button S4.1
        Button btnExitHighscore = new Button("Back");
        btnExitHighscore.setPrefWidth(300);
        btnExitHighscore.setOnAction(e -> {
            window.setScene(menu);
            window.setFullScreen(fullscreen);
            window.setWidth(sceneWidth);
            window.setHeight(sceneLength);
        });

        //Scene 4 Highscore 
        VBox highscoreLayout = new VBox();
        highscoreLayout.getChildren().addAll(lblTitelHighscore, lblLeeg0, lblHighscore1, lblHighscore2, lblHighscore3, lblHighscore4, lblHighscore5, lblLeeg1, btnExitHighscore);
        highscore = new Scene(highscoreLayout, sceneWidth, sceneLength);

        //Scene 4.5 Teamspeak
        Label lblTeamspeak = new Label("Teamspeak:");
        Button btnHostTeamSpeak = new Button("Host teamspeak");
        btnHostTeamSpeak.setPrefWidth(300);
        btnHostTeamSpeak.setOnAction(e
                -> {
            if (voiceServer != null) {
                voiceServer = new Server_voice();
                voiceServer.startVoiceServer();

            }
        }
        );
        TextField tbIpSpeak = new TextField("");
        tbIpSpeak.setMaxWidth(600);
        Button btnClientTeamspeak = new Button("Join teamspeak");
        btnClientTeamspeak.setPrefWidth(300);
        btnClientTeamspeak.setOnAction(e
                -> {
            voiceClient = new Client_voice();
            voiceClient.startVoiceClient(tbIpSpeak.getText());
        }
        );
        Button btnBackToSettings = new Button("Terug");
        btnBackToSettings.setPrefWidth(300);
        btnBackToSettings.setOnAction(e
                -> {
            window.setScene(settings);
        }
        );
        VBox teamSpeakLayout = new VBox();
        teamSpeakLayout.getChildren().addAll(lblTeamspeak, btnHostTeamSpeak, tbIpSpeak, btnClientTeamspeak, btnBackToSettings);
        teamspeak = new Scene(teamSpeakLayout, sceneWidth, sceneLength);

        //Label S5.1
        Label lblTitelSettings = new Label("Settings:");

        //Label S5.2
        Label lblResolutie = new Label("Kies resolutie:");
        lblResolutie.setId(LABELCONTENT);

        //Label leeg
        Label lblLeegheid1 = new Label("");
        lblLeegheid1.setId(LABELCONTENT);
        Label lblLeegheid2 = new Label("");
        lblLeegheid2.setId(LABELCONTENT);
        Label lblLeegheid4 = new Label("");
        lblLeegheid4.setId(LABELCONTENT);

        //DropDownMenu S5.1
        ObservableList<String> options
                = FXCollections.observableArrayList(
                        "960-540",
                        "1230-540",
                        "1280-720",
                        "1280-1024",
                        "1920-800",
                        getScreenRes()
                );
        ComboBox comboBox = new ComboBox(options);
        comboBox.setPrefWidth(300);
        comboBox.getSelectionModel().select(0);
        comboBox.setId("combobox");

        //Label S5.3
        Label lblGeluid = new Label("Kies geluidsterkte:");
        lblGeluid.setId(LABELCONTENT);

        //DropDownMenu S5.2
        ObservableList<String> optionsGeluid
                = FXCollections.observableArrayList(
                        "100%",
                        "75%",
                        "50%",
                        "25%",
                        "10%",
                        "Off"
                );
        ComboBox comboBoxGeluid = new ComboBox(optionsGeluid);
        comboBoxGeluid.setPrefWidth(300);
        comboBoxGeluid.getSelectionModel().select(0);
        comboBoxGeluid.setId("combobox");

        //Button S5.1
        Button btnSaveSettings = new Button("Save");
        btnSaveSettings.setPrefWidth(300);
        btnSaveSettings.setOnAction(e
                -> {
            getScreenSizes(comboBox);
            getGeluidWaardes(comboBoxGeluid);
        }
        );

        //Button S5.1
        Button btnTeamspeak = new Button("Teamspeak");
        btnTeamspeak.setPrefWidth(300);
        btnTeamspeak.setOnAction(e
                -> {
            window.setScene(teamspeak);
        }
        );

        //Button S5.2
        Button btnExitSettings = new Button("Back");

        btnExitSettings.setPrefWidth(
                300);
        btnExitSettings.setOnAction(e
                -> {
            window.setScene(menu);
            window.setWidth(sceneWidth);
            window.setHeight(sceneLength);
            window.setFullScreen(fullscreen);
        }
        );

        //Scene 5 SETTINGS 
        VBox settingsLayout = new VBox();
        settingsLayout.getChildren().addAll(lblTitelSettings, lblResolutie, comboBox, lblLeegheid1, lblGeluid, comboBoxGeluid, btnTeamspeak, btnSaveSettings, btnExitSettings);
        settings = new Scene(settingsLayout, sceneWidth, sceneLength);

        //Label S6.0
        Label lblLeger = new Label("");
        Label lblLeger2 = new Label("");

        //Image S6.1
        Label lblGameOver = new Label("GAME OVER");
        lblGameOver.setId("lblGameOver");

        //Image S6.1
        lblWinnaar = new Label("<Winnaar>");
        lblWinnaar.setId(LABELCONTENT);

        //Label S6.2
        lblVKills = new Label("<Kills>");
        lblVKills.setId(LABELCONTENT);

        //Label S6.3
        lblVDeaths = new Label("<Deaths>");
        lblVDeaths.setId(LABELCONTENT);

        //Button S6.4
        Button btnExitVictory = new Button("Menu");
        btnExitVictory.setPrefWidth(300);
        btnExitVictory.setOnAction(e -> {
            window.setScene(menu);
            window.setFullScreen(fullscreen);
            window.setWidth(sceneWidth);
            window.setHeight(sceneLength);
        });

        //Scene 6 GameOver 
        VBox gameOverLayout = new VBox();
        gameOverLayout.getChildren().addAll(lblGameOver, lblLeger, lblWinnaar, lblVKills, lblVDeaths, lblLeger2, btnExitVictory);
        gameover = new Scene(gameOverLayout, sceneWidth, sceneLength);

        //CSS
        menu.getStylesheets()
                .add(Menu.class
                        .getResource(
                                DOMUS).toExternalForm());
        highscore.getStylesheets()
                .add(Menu.class
                        .getResource(
                                DOMUS).toExternalForm());
        settings.getStylesheets()
                .add(Menu.class
                        .getResource(
                                DOMUS).toExternalForm());
        gameover.getStylesheets()
                .add(Menu.class
                        .getResource(
                                DOMUS).toExternalForm());
        login.getStylesheets()
                .add(Menu.class
                        .getResource(
                                DOMUS).toExternalForm());
        newaccount.getStylesheets()
                .add(Menu.class
                        .getResource(
                                DOMUS).toExternalForm());
        lobby.getStylesheets()
                .add(Menu.class
                        .getResource(
                                DOMUS).toExternalForm());

        hostgame.getStylesheets()
                .add(Menu.class
                        .getResource(
                                DOMUS).toExternalForm());
        teams.getStylesheets()
                .add(Menu.class
                        .getResource(
                                DOMUS).toExternalForm());

        direct.getStylesheets()
                .add(Menu.class
                        .getResource(
                                DOMUS).toExternalForm());

        teamspeak.getStylesheets()
                .add(Menu.class
                        .getResource(
                                DOMUS).toExternalForm());

        //Display scene 1 at first
        window.setScene(login);

        window.setWidth(sceneWidth);

        window.setHeight(sceneLength);

        window.setTitle(GAMENAME);
        window.setResizable(
                false);
        window.centerOnScreen();

        window.show();

    }

    /**
     * Verkrijgt het scherm resolutie.
     *
     * @return
     */
    private String getScreenRes() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
        int width = (int) primaryScreenBounds.getWidth();
        int height = (int) primaryScreenBounds.getHeight();
        return width + "-" + height;
    }

    /**
     * Zet de schermresolutie voor de combobox
     *
     * @param comboBox De combobox waar de scherm resoluties in moeten staan.
     */
    private void getScreenSizes(ComboBox comboBox) {
        int id = comboBox.getSelectionModel().getSelectedIndex();
        switch (id) {
            case 0:
                setSceneSettings(false, 960, 540);
                break;
            case 1:
                setSceneSettings(false, 1230, 540);
                break;
            case 2:
                setSceneSettings(false, 1280, 720);
                break;
            case 3:
                setSceneSettings(false, 1280, 1024);
                break;
            case 4:
                setSceneSettings(false, 1920, 800);
                break;
            default:
                fullscreen = true;
                sceneWidth = (int) Screen.getPrimary().getBounds().getWidth();
                sceneLength = (int) Screen.getPrimary().getBounds().getHeight();
                setWindow();
                break;
        }
    }

    /**
     * Stelt het scherm in
     *
     * @param fullscreen fullscreenmode
     * @param sceneWidth schermbreedte
     * @param sceneLength schermlengte
     */
    private void setSceneSettings(boolean fullscreen, int sceneWidth, int sceneLength) {
        this.fullscreen = fullscreen;
        this.sceneWidth = sceneWidth;
        this.sceneLength = sceneLength;
        setWindow();
    }

    /**
     * Zet de geluidswaardes van de combobox geluid
     *
     * @param comboBoxGeluid de combobox waar de geluidwaardes in komen te
     * zitten.
     */
    private void getGeluidWaardes(ComboBox comboBoxGeluid) {
        int geluidID = comboBoxGeluid.getSelectionModel().getSelectedIndex();
        switch (geluidID) {
            case 0:
                setGeluid(1);
                break;
            case 1:
                setGeluid(0.75);
                break;
            case 2:
                setGeluid(0.5);
                break;
            case 3:
                setGeluid(0.25);
                break;
            case 4:
                setGeluid(0.1);
                break;
            default:
                setGeluid(0);
                break;
        }
    }

    /**
     * Stelt het geluid in
     *
     * @param decibel Hoe hard het geluid moet zijn.
     */
    private void setGeluid(double decibel) {
        this.decibel = decibel;
        AudioManager.setVolume(decibel);
    }

    /**
     * Verkrijgt de game die bezig is of maakt een nieuwe game aan als die al
     * voorbij is.
     */
    private void getGame() {
        if (singleplayer != null && game != null && game.isPlaying()) {
            int renderWidth = (int) ((double) 720 * ((double) sceneWidth / (double) sceneLength));
            game.setSize(sceneWidth, sceneLength);
            singleplayer = game.continueScene(renderWidth, 720, fullscreen);
            game.start(this, singleplayer);
        } else {
            newGame(mode, ip, port);
        }
    }

    /**
     * Maakt een nieuwe game aan.
     */
    private void newGame(GameMode mode, InetAddress ip, int port) {
        int renderWidth = (int) ((double) 720 * ((double) sceneWidth / (double) sceneLength));
        if (mode == GameMode.SinglePlayer) {
            game = new Game(window, gameTime, sceneWidth, sceneLength, fullscreen, account);
        } else if (ip != null && port > 1024 && port < 49151) {
            game = new Game(window, gameTime, sceneWidth, sceneLength, fullscreen, account, ip, port);
        }
        singleplayer = game.generateScene(renderWidth, 720, fullscreen);
        game.start(this, singleplayer);
    }

    /**
     * Zet het menu naar pauze mode.
     */
    public void changeMenuToPaused(boolean resetGameWorld) {
        VBox menuLayout = new VBox(20);
        menuLayout.getChildren().addAll(lblMenu, btnContinu, btnGoToSinglePlayer, btnGoToMultiPlayer, btnGoToHighScore, btnGoToSettings, btnQuitGame);
        menu = new Scene(menuLayout, sceneWidth, sceneLength); //960-540 & 1280-720

        menu.getStylesheets()
                .add(Menu.class
                        .getResource(
                                DOMUS).toExternalForm());
        if (resetGameWorld) {
            game = null;
        }
        window.setScene(menu);
        window.setFullScreen(fullscreen);
    }

    /**
     * Zet de scherm instellingen vast.
     */
    public void setWindow() {
        window.setWidth(sceneWidth);
        window.setHeight(sceneLength);
        window.setFullScreen(fullscreen);
        window.centerOnScreen();
    }

    /**
     * Weergeeft het game over menu
     *
     * @param bot De bot
     * @param player De speler
     */
    public void gameOver(Bot bot, Player player) {
        window.setScene(gameover);
        if (voiceClient != null) {
            voiceClient.stopVoiceClient();
        }
        if (bot.getKills() > player.getKills()) {
            lblWinnaar.setText("Winnaar: ROBOT");
        } else if (player.getKills() > bot.getKills()) {
            lblWinnaar.setText("Winnaar: " + player.getAccount().getUserName());
        } else {
            lblWinnaar.setText("DRAW");
        }
        lblVKills.setText("Kills: " + player.getKills());
        lblVDeaths.setText("Deaths: " + player.getDeaths());
    }

    private String getIpAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ipAddress = inetAddress.getHostAddress();
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }
}
