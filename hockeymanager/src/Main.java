import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import javax.swing.JOptionPane;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Main extends Application {
	
	/*
	 * Awards
	 * Edit Player(Position)
	 * Power Rankings
	 * # of Cups Won list, other franchise stats
	 * Top 100 F/D/G List
	 * Draft Records
	 */
	
	//Splits date keeping into actual date & displayed date
	static Calendar gameCalendar = Calendar.getInstance();
	static Calendar displayCalendar = Calendar.getInstance();
	static Locale locale = Locale.getDefault();

	//NO BUILT IN ORDINAL INDICATORS IN CALENDAR
	static String[] suffixes =
		//  0     1     2     3     4     5     6     7     8     9
		{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				//10    11    12    13    14    15    16    17    18    19
				"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
				//20    21    22    23    24    25    26    27    28    29
				"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				//30    31
				"th", "st" };

	//All the players
	static Skater[] Skaters = new Skater[((Team.TeamNames.length + Team.MinorTeams.length + Team.EuropeanTeams.length + Team.ProspectTeams.length) * Team.rosterLimit) + 564];
	static Goalie[] Goalies = new Goalie[((Team.TeamNames.length + Team.MinorTeams.length + Team.EuropeanTeams.length + Team.ProspectTeams.length) * (Team.rosterGlimit-1)) + 68];

	//Main League
	static Team[] Teams = new Team[Team.TeamNames.length];
	static Team UserTeam;

	//Other Leagues
	static Team[] MinorTeams = new Team[Team.MinorTeams.length];
	static Team[] EuropeanTeams = new Team[Team.EuropeanTeams.length];
	static Team[] ProspectTeams = new Team[Team.ProspectTeams.length];

	//All Leagues
	static Team[][] AllTeams = {Teams, EuropeanTeams, MinorTeams, ProspectTeams};
	
	//Various counters
	static int currentTeam = 0, currentT = 0, currentL = 0, currentP = 0, currentS = 0;
	
	//Seasons
	static Season MainSeasons;
	static Season EuroSeasons;
	static Season MinorSeasons;
	static Season ProspectSeasons;

	Button exitGame = new Button("Exit");
	Button[] days = new Button[7];
	static Button simGame = new Button("Sim Game");
	Button backToHub = new Button("Back");
	
	//TODO Edit Team Stanings to be tabs on hub that switch between EConf & WConf
	static TableView<Team> teamStandings = new TableView<>();
	static TableView<Skater> skaterLeaders = new TableView<>();
	static TableView<Goalie> goalieLeaders = new TableView<>();

	static Label date = new Label();
	static Label season = new Label();
	static Label seasonType = new Label();
	static Label month = new Label();
	static Label userTeamLabel = new Label();
	static Label userTeamRecord = new Label();
	Label[] dayText = new Label[7];
	static Label news = new Label();

	HBox week = new HBox();
	HBox weekText = new HBox();
	
	//Screen Resolution
	static int windowWidth= 1280, windowHeight= 720;
	
	static Stage mainWindow = new Stage();

	GridPane playerStatsLayout = new GridPane();
	Scene playerStats = new Scene(playerStatsLayout,windowWidth,windowHeight);

	static ObservableList<Team> teamList = FXCollections.observableArrayList();
	static ObservableList<Skater> skaterList = FXCollections.observableArrayList();
	static ObservableList<Goalie> goalieList = FXCollections.observableArrayList();

	static ObservableList<Skater> skaterCopy =  skaterList;
	static ObservableList<Team> teamCopy = teamList;
	static ObservableList<Goalie> goalieCopy = goalieList;

	//Team Class Column Variables
	static TableColumn<Team, String> teamNames = new TableColumn<>("Team");
	static TableColumn<Team, Integer> teamConf = new TableColumn<>("Conference");
	static TableColumn<Team, Integer> teamWins = new TableColumn<>("Wins");
	static TableColumn<Team, Integer> teamLoss = new TableColumn<>("Losses");
	static TableColumn<Team, Integer> teamOTL = new TableColumn<>("OTL");
	static TableColumn<Team, Integer> teamPoints = new TableColumn<>("Points");

	//Player Class Column Variables
	static TableColumn<Skater, String> skaterName = new TableColumn<>("Name");
	static TableColumn<Skater, String> skaterPosition = new TableColumn<>("Position");
	static TableColumn<Skater, String> skaterTeam = new TableColumn<>("Team");
	static TableColumn<Skater, String> skaterAge = new TableColumn<>("Age");
	static TableColumn<Goalie, String> goalieName = new TableColumn<>("Name");
	static TableColumn<Goalie, String> goalieTeam = new TableColumn<>("Team");
	static TableColumn<Goalie, String> goalieAge = new TableColumn<>("Age");
	static TableColumn<Goalie, String> goaliePosition = new TableColumn<>("Position");
	
	//Skater Class Column Variables
	static TableColumn<Skater, String> skaterGamesPlayed = new TableColumn<>("GP");
	static TableColumn<Skater, String> skaterGoals = new TableColumn<>("G");
	static TableColumn<Skater, String> skaterAssists = new TableColumn<>("A");
	static TableColumn<Skater, String> skaterPoints = new TableColumn<>("P");
	static TableColumn<Skater, String> skaterPIM = new TableColumn<>("PIM");
	
	//Goalie Class Column Variables
	static TableColumn<Goalie, String> goalieGamesPlayed = new TableColumn<>("GP");
	static TableColumn<Goalie, String> goalieWins = new TableColumn<>("W");
	static TableColumn<Goalie, String> goalieLosses = new TableColumn<>("L");
	static TableColumn<Goalie, String> goalieOTL = new TableColumn<>("OTL");
	static TableColumn<Goalie, String> goalieSO = new TableColumn<>("SO");
	static TableColumn<Goalie, String> goalieGoals = new TableColumn<>("G");
	static TableColumn<Goalie, String> goalieAssists = new TableColumn<>("A");
	static TableColumn<Goalie, String> goaliePoints = new TableColumn<>("P");
	static TableColumn<Goalie, String> goaliePIM = new TableColumn<>("PIM");

	public static void main(String[] args) {
		
		date.setStyle("-fx-font-size: 2em; ");
		season.setStyle("-fx-font-size: 2em; ");
		seasonType.setStyle("-fx-font-size: 2em; ");
		
		//Team Class Values
		teamNames.setCellValueFactory(new PropertyValueFactory<>("nameAbbrv"));
		teamConf.setCellValueFactory(new PropertyValueFactory<>("Conference"));
		teamWins.setCellValueFactory(new PropertyValueFactory<>("Wins"));
		teamLoss.setCellValueFactory(new PropertyValueFactory<>("Losses"));
		teamOTL.setCellValueFactory(new PropertyValueFactory<>("OTLosses"));
		teamPoints.setCellValueFactory(new PropertyValueFactory<>("Points"));
		
		//Player Class Values
		skaterName.setCellValueFactory(new PropertyValueFactory<>("shortName"));
		skaterPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
		skaterTeam.setCellValueFactory(new PropertyValueFactory<>("teamAbrrv"));
		skaterAge.setCellValueFactory(new PropertyValueFactory<>("age"));
		goalieName.setCellValueFactory(new PropertyValueFactory<>("shortName"));
		goalieTeam.setCellValueFactory(new PropertyValueFactory<>("teamAbrrv"));
		goalieAge.setCellValueFactory(new PropertyValueFactory<>("age"));
		goaliePosition.setCellValueFactory(new PropertyValueFactory<>("position"));
		
		//Skater Class Values
		skaterGoals.setCellValueFactory(new PropertyValueFactory<>("goals"));
		skaterAssists.setCellValueFactory(new PropertyValueFactory<>("assists"));
		skaterPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
		skaterGamesPlayed.setCellValueFactory(new PropertyValueFactory<>("gamesPlayed"));
		skaterPIM.setCellValueFactory(new PropertyValueFactory<>("PIM"));
		
		//Goalie Class Values
		goalieGamesPlayed.setCellValueFactory(new PropertyValueFactory<>("gamesPlayed"));
		goalieWins.setCellValueFactory(new PropertyValueFactory<>("wins"));
		goalieLosses.setCellValueFactory(new PropertyValueFactory<>("losses"));
		goalieOTL.setCellValueFactory(new PropertyValueFactory<>("OTL"));
		goalieSO.setCellValueFactory(new PropertyValueFactory<>("shutouts"));
		goalieGoals.setCellValueFactory(new PropertyValueFactory<>("goals"));
		goalieAssists.setCellValueFactory(new PropertyValueFactory<>("assists"));
		goaliePoints.setCellValueFactory(new PropertyValueFactory<>("points"));
		goaliePIM.setCellValueFactory(new PropertyValueFactory<>("PIM"));
		
		launch(args);

	}

	public void start(Stage window) {

		VBox mainMenuOptions = new VBox(20);
		Button newGame = new Button("New Game");
		Button loadGame = new Button("Load Game");
		Button settingsGame = new Button("Settings");
		Scene mainMenu = new Scene(mainMenuOptions, windowWidth, windowHeight);

		mainWindow = window;

		//TODO Change Name
		window.setTitle("Hockey Manager v0.0.1");
		window.setScene(mainMenu);
		window.centerOnScreen();
		window.setResizable(false);
		window.show();
		mainMenuOptions.getChildren().addAll(newGame, loadGame, settingsGame, exitGame);
		mainMenuOptions.setAlignment(Pos.CENTER);

		//Exit Buttons
		window.setOnCloseRequest(e -> System.exit(0));
		exitGame.setOnAction(e -> System.exit(0));

		newGame.setOnAction(e -> newGame());

	}

	public void newGame() {
		
		populate();

		Button  nextTeam = new Button("Next");
		Button  previousTeam = new Button("Previous");
		Button[] teamOptions = new Button[Teams.length];

		Label[] teamStats = new Label[Teams.length];

		HBox selectTeamOptions = new HBox(10);
		VBox teamInfo = new VBox(50);
		
		Scene teamSelect = new Scene(selectTeamOptions, windowWidth, windowHeight);

		//Set start date for game September 1st, Current Year
		gameCalendar.set(gameCalendar.get(Calendar.YEAR), Calendar.SEPTEMBER, 1, 0, 0, 0);
		
		//Top Info for playerhub
		season.setText(gameCalendar.get(Calendar.YEAR) + "-" + (gameCalendar.get(Calendar.YEAR)+1));
		seasonType.setText("Preseason");
		date.setText(gameCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale) + " " + gameCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale) 
		+ " " + gameCalendar.get(Calendar.DAY_OF_MONTH) + suffixes[gameCalendar.get(Calendar.DATE)] + " "	+ gameCalendar.get(Calendar.YEAR));
		month.setText(gameCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale));

		//Create buttons so user can select team
		for (int x = 0; x < teamOptions.length; x++) {

			final int temp = x;

			teamOptions[x] = new Button("Select " + Teams[x].name);

			teamStats[x] = new Label(Teams[x].name + "\nOffensive Rating: " + Teams[x].OffensiveRating + "\nDefensive Rating: " + Teams[x].DeffensiveRating + "\nGoaltender Rating: " + Teams[x].GoaltenderRating);

			teamOptions[x].setOnAction(e -> {

				UserTeam = Teams[temp];

				userTeamLabel.setText(UserTeam.name);
				userTeamLabel.setStyle("-fx-font-size: 2em; ");
				
				userTeamRecord.setText(UserTeam.Wins + " - " + UserTeam.Losses + " - " + UserTeam.OTLosses);
				userTeamRecord.setStyle("-fx-font-size: 2em; ");

				MainSeasons = new Season(0);
				EuroSeasons = new Season(1);
				MinorSeasons = new Season(2);
				ProspectSeasons = new Season(3);

				setUpHub();

			});

		}

		teamStats[currentTeam].setPrefWidth(300);
		teamStats[currentTeam].setAlignment(Pos.CENTER);

		teamInfo.setAlignment(Pos.CENTER);
		teamInfo.getChildren().addAll(teamStats[currentTeam], teamOptions[currentTeam]);

		selectTeamOptions.getChildren().addAll(previousTeam, teamInfo, nextTeam);
		selectTeamOptions.setAlignment(Pos.CENTER);

		nextTeam.setOnAction(e-> {

			currentTeam = nextOption(currentTeam, Teams.length);

			teamInfo.getChildren().setAll(teamStats[currentTeam], teamOptions[currentTeam]);
			teamInfo.setAlignment(Pos.CENTER);

			teamStats[currentTeam].setPrefWidth(300);
			teamStats[currentTeam].setAlignment(Pos.CENTER);


		});

		previousTeam.setOnAction(e-> {

			currentTeam = previousOption(currentTeam, Teams.length);

			teamInfo.getChildren().setAll(teamStats[currentTeam], teamOptions[currentTeam]);
			teamInfo.setAlignment(Pos.CENTER);

			teamStats[currentTeam].setPrefWidth(300);
			teamStats[currentTeam].setAlignment(Pos.CENTER);

		});

		mainWindow.setScene(teamSelect);

	}


	@SuppressWarnings({ "unchecked" })
	public void setUpHub() {
		
		setCalendarButtons();

		teamStandings.setItems(teamList);
		teamStandings.getColumns().setAll(teamNames, teamConf, teamWins, teamLoss, teamOTL, teamPoints);

		goalieLeaders.setItems(goalieList);
		goalieLeaders.getColumns().setAll(goalieName, goalieTeam, goalieGamesPlayed, goalieWins, goalieLosses, goalieOTL, goalieSO);
		
		skaterLeaders.setItems(skaterList);
		skaterLeaders.getColumns().setAll(skaterName, skaterTeam, skaterPosition, skaterGamesPlayed, skaterGoals, skaterAssists, skaterPoints, skaterPIM);

		skaterLeaders.getSortOrder().clear();
		goalieLeaders.getSortOrder().clear();
		
		getcurrentSeason();
		resetLists();

		//Filter Skaters
		for (int x = 0; x < skaterCopy.size(); x++) {

			if (skaterCopy.get(x).league != Team.League[0]) {

				skaterLeaders.getItems().remove(x);

				x--;

			}

		}

		//Filter Goalies
		for (int x = 0; x < goalieCopy.size(); x++) {

			if (goalieCopy.get(x).league != Team.League[0]) {

				goalieLeaders.getItems().remove(x);

				x--;

			}

		}
		
		
		Button viewNext = new Button("View Next Week");
		Button viewPrev = new Button("View Previous Week");
		
		Button roster = new Button("Edit Roster");
		Button editLines = new Button ("Edit Lines");
		Button playerStats = new Button("View Stats");
		Button contracts = new Button("Contracts");
		Button freeAgents = new Button("Free Agents");
		Button trade = new Button("Trade Players");
		Button draftRankings = new Button("Draft Rankings");
		Button injuries = new Button("Injuries");
		Button teamStrategies = new Button("Team Strategies");
		Button teamReports = new Button("Team Reports");
		Button save = new Button("Save");
		
		Button[] options = {simGame, roster, editLines, playerStats, contracts, freeAgents, trade, draftRankings, injuries, teamStrategies, teamReports, save, exitGame};
		
		for (int x = 0; x < options.length; x++) {
			
			options[x].setMaxWidth(Double.MAX_VALUE);
			
		}
		
		
		simGame.setDisable(true);

		VBox hubOptions = new VBox();
		HBox hubInfo = new HBox();
		ScrollPane hubNews = new ScrollPane(); //Change
		
		GridPane hubDisplay = new GridPane();

		teamStandings.setMinWidth(windowWidth*.22);
		skaterLeaders.setMinWidth(windowWidth*.22);
		goalieLeaders.setMinWidth(windowWidth*.22);
		hubNews.setMinWidth(windowWidth*.22);
		hubOptions.setMinWidth(windowWidth*.12);	
		
		hubNews.setContent(news);

		hubOptions.getChildren().addAll(options);		
		
		hubInfo.getChildren().addAll(hubOptions, hubNews, teamStandings, skaterLeaders, goalieLeaders);
		
		

		//TODO Add UserTeam's Opponent Name + Record, constantly show next game
		HBox top = new HBox(20);
		top.getChildren().addAll(userTeamLabel,userTeamRecord, season, seasonType);
		
		HBox weekSelector = new HBox(20);
		weekSelector.getChildren().addAll(viewPrev, month, viewNext);
		weekSelector.setAlignment(Pos.CENTER);
		weekSelector.setPadding(new Insets(0,0,20,0));

		//Item, Column, Row
		hubDisplay.add(top, 0, 0);
		hubDisplay.add(date, 0, 1);
		hubDisplay.add(weekSelector, 0, 2);
		hubDisplay.add(weekText, 0, 3);
		hubDisplay.add(week, 0, 4);
		hubDisplay.add(hubInfo, 0, 5);

		week.setAlignment(Pos.CENTER);
		weekText.setAlignment(Pos.CENTER);
		hubInfo.setAlignment(Pos.CENTER);

		Scene playerHub = new Scene(hubDisplay, windowWidth, windowHeight);
		mainWindow.setScene(playerHub);

		simGame.setOnAction(e -> {

			MainSeasons.userTeamPlayGames();

			userTeamRecord.setText(UserTeam.Wins + " - " + UserTeam.Losses + " - " + UserTeam.OTLosses);
			
			refreshTables();

			simGame.setDisable(true);

		});

		//View Next Week
		viewNext.setOnAction(e -> {

			displayCalendar.add(Calendar.DATE, 1);

			updateCalendarButtons();

		});

		//View Previous Week
		viewPrev.setOnAction(e -> {

			displayCalendar.add(Calendar.DATE, -13);

			updateCalendarButtons();

		});	

		playerStats.setOnAction(e -> getAllPlayerStats());

		editLines.setOnAction(e -> editLines());

		roster.setOnAction(e -> editRoster());

		backToHub.setOnAction(e -> setUpHub());
		
		contracts.setOnAction(e -> contracts());
		
		freeAgents.setOnAction(e -> freeAgents());

	}
	
	void getcurrentSeason() {
		
		//regular season + 1/2 off season
		if (Main.gameCalendar.get(Calendar.MONTH) == Calendar.AUGUST ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.SEPTEMBER ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.OCTOBER ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.NOVEMBER ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.DECEMBER ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.JANUARY ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.FEBRUARY ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.MARCH
				) {

			currentS = 0;

		}

		//playoffs + 1/2 offseason
		else {

			currentS = 1;

		}

		
		
	}
	
	@SuppressWarnings("unchecked")
	void freeAgents() {
		
		//TODO Don't use player class fix and stuff

		String[] positions = {"All Skaters", "Centers", "Left Wings", "Right Wings", "Defencemen", "Goaltenders"};

		
		Button nextPosition = new Button("Next");
		Button previousPosition = new Button("Previous");
		
		Label currentPosition = new Label(positions[currentP]);
		
		HBox positionLayout = new HBox(20);

		positionLayout.getChildren().addAll(previousPosition, currentPosition, nextPosition);
		
		TableView<Player> freeAgentsTable = new TableView<>();
		
		TableColumn<Player, String> name = new TableColumn<>("Name");
		name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
		
		TableColumn<Player, String> position = new TableColumn<>("P");
		position.setCellValueFactory(new PropertyValueFactory<>("position"));
		
		TableColumn<Player, String> age = new TableColumn<>("Age");
		age.setCellValueFactory(new PropertyValueFactory<>("age"));
		
		TableColumn<Player, String> team = new TableColumn<>("Test");
		team.setCellValueFactory(new PropertyValueFactory<>("team"));
		
		freeAgentsTable.getColumns().setAll(name, position, age, team);
		
		ObservableList<Player> FreeAgents = FXCollections.observableArrayList();
		ObservableList<Player> FreeAgentsCOPY = FreeAgents;
		
		FreeAgents.addAll(Skaters);
		FreeAgents.addAll(Goalies);
		
		freeAgentsTable.setItems(FreeAgents);
		
		
		for (int x = 0; x < FreeAgentsCOPY.size(); x++) {

			if (FreeAgentsCOPY.get(x).team.equals(null) == false) {

				freeAgentsTable.getItems().remove(x);

				x--;

			}

		}
		
		VBox freeAgentLayout = new VBox();
		freeAgentLayout.getChildren().setAll(backToHub,positionLayout, freeAgentsTable);
		
		Scene freeAgentsScene = new Scene(freeAgentLayout, windowWidth, windowHeight);
		
		mainWindow.setScene(freeAgentsScene);

		freeAgentsTable.addEventFilter(MouseEvent.MOUSE_CLICKED, e-> {
			
			//Prevents players from being clickable in hub
			if (mainWindow.getScene() == playerStats && skaterLeaders.getSelectionModel().getSelectedItem() != null) {
				
				skaterPlayerSceen((Skater) freeAgentsTable.getSelectionModel().getSelectedItem());

				freeAgentsTable.getSelectionModel().clearSelection();

			}
			
		});
		
		nextPosition.setOnAction(e ->  {

			FreeAgents.clear();
			FreeAgents.addAll(Skaters);
			FreeAgents.addAll(Goalies);

			currentP = nextOption(currentP,positions.length);

			if (currentP != 0) {

				for (int x = 0; x < FreeAgentsCOPY.size(); x++) {

					if (FreeAgentsCOPY.get(x).position != Player.positionType[currentP-1]) {

						freeAgentsTable.getItems().remove(x);

						x--;

					}

				}
			}

			freeAgentsTable.refresh();
			
			currentPosition.setText(positions[currentP]);

		});

		previousPosition.setOnAction(e -> {

			FreeAgents.clear();
			FreeAgents.addAll(Skaters);
			FreeAgents.addAll(Goalies);

			currentP = previousOption(currentP,positions.length);

			if (currentP != 0) {

				for (int x = 0; x < FreeAgentsCOPY.size(); x++) {

					if (FreeAgentsCOPY.get(x).position != Player.positionType[currentP-1]) {

						freeAgentsTable.getItems().remove(x);

						x--;

					}

				}

			}

			freeAgentsTable.refresh();
			
			currentPosition.setText(positions[currentP]);

		});
		
	}

	@SuppressWarnings("unchecked")
	public void editRoster() {
		
		int team = 0;
		final int temp = team;

		TableView<Skater> MainTable = new TableView<>();
		TableView<Skater> MinorTable = new TableView<>();
		TableView<Goalie> MainGoalieTable = new TableView<>();
		TableView<Goalie> MinorGoalieTable = new TableView<>();

		ObservableList<Skater> MainList = FXCollections.observableArrayList();
		ObservableList<Skater> MinorList = FXCollections.observableArrayList();
		ObservableList<Goalie> MainGoalieList = FXCollections.observableArrayList();
		ObservableList<Goalie> MinorGoalieList = FXCollections.observableArrayList();

		MainList.setAll(Skaters);
		MinorList.setAll(Skaters);
		MainGoalieList.setAll(Goalies);
		MinorGoalieList.setAll(Goalies);

		TableColumn<Skater, String> MinorName = new TableColumn<>("Name");
		TableColumn<Skater, String> MinorPosition = new TableColumn<>("Position");
		TableColumn<Skater, String> MinorAge = new TableColumn<>("Age");
		MinorName.setCellValueFactory(new PropertyValueFactory<>("shortName"));
		MinorPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
		MinorAge.setCellValueFactory(new PropertyValueFactory<>("age"));

		TableColumn<Goalie, String> MinorGoalieName = new TableColumn<>("Name");
		TableColumn<Goalie, String> MinorGoalieAge = new TableColumn<>("Age");
		TableColumn<Goalie, String> MainGoaliePosition = new TableColumn<>("Position");
		TableColumn<Goalie, String> MinorGoaliePosition = new TableColumn<>("Position");

		MinorGoalieName.setCellValueFactory(new PropertyValueFactory<>("shortName"));
		MinorGoalieAge.setCellValueFactory(new PropertyValueFactory<>("age"));
		MainGoaliePosition.setCellValueFactory(new PropertyValueFactory<>("position"));
		MinorGoaliePosition.setCellValueFactory(new PropertyValueFactory<>("position"));

		ObservableList<Skater> MainCopy = MainList;
		ObservableList<Skater> MinorCopy = MinorList;

		ObservableList<Goalie> MainGoalieCopy = MainGoalieList;
		ObservableList<Goalie> MinorGoalieCopy = MinorGoalieList;

		MainTable.setItems(MainList);
		MainTable.getColumns().setAll(skaterName, skaterPosition, skaterAge);
		MinorTable.setItems(MinorList);
		MinorTable.getColumns().setAll(MinorName, MinorPosition, MinorAge);

		MainGoalieTable.setItems(MainGoalieList);
		MainGoalieTable.getColumns().setAll(goalieName,MainGoaliePosition, goalieAge);
		MinorGoalieTable.setItems(MinorGoalieList);
		MinorGoalieTable.getColumns().setAll(MinorGoalieName, MinorGoaliePosition, MinorGoalieAge);

		HBox Main = new HBox();
		HBox Minor = new HBox();

		Main.getChildren().addAll(MainTable, MainGoalieTable);
		Minor.getChildren().addAll(MinorTable, MinorGoalieTable);

		VBox rosterLayout = new VBox(25);
		rosterLayout.getChildren().addAll(new Label("Main Roster"),Main,new Label("Minor Roster"), Minor, backToHub);
		Scene editRoster = new Scene(rosterLayout,windowWidth,windowHeight);

		//Get User Team Skaters
		for (int x = 0; x < MainCopy.size(); x++) {

			if (MainCopy.get(x).team != UserTeam) {

				MainTable.getItems().remove(x);

				x--;

			}

		}

		//Get User Team Goalies
		for (int x = 0; x < MainGoalieCopy.size(); x++) {

			if (MainGoalieCopy.get(x).team != UserTeam) {

				MainGoalieTable.getItems().remove(x);

				x--;

			}

		}

		//Get index of User Team, to find it's linked minor team
		for (int x = 0; x< Teams.length; x++) {
			if (Teams[x] == UserTeam) {

				team = x;

				break;

			}
		}

		//Get User Team Minor Skaters
		for (int x = 0; x < MinorCopy.size(); x++) {

			if (MinorCopy.get(x).team != MinorTeams[team] || MinorCopy.get(x).signedToMainTeam != true) {

				MinorTable.getItems().remove(x);

				x--;

			}

		}

		//Get User Team Minor Goalies
		for (int x = 0; x < MinorGoalieCopy.size(); x++) {

			if (MinorGoalieCopy.get(x).team != MinorTeams[team]) {

				MinorGoalieTable.getItems().remove(x);

				x--;

			}

		}

		MainTable.setOnDragDetected(e -> {

			//Dummy content to initiate drag and drop
			Dragboard db = MainTable.startDragAndDrop(TransferMode.ANY);
			ClipboardContent content = new ClipboardContent();
			content.putString(MainTable.getSelectionModel().getSelectedItem().toString());
			db.setContent(content);

			e.consume();

		});


		MinorTable.setOnDragDetected(e -> {

			//Dummy content to initiate drag and drop
			Dragboard db = MinorTable.startDragAndDrop(TransferMode.ANY);
			ClipboardContent content = new ClipboardContent();
			content.putString(MinorTable.getSelectionModel().getSelectedItem().toString());
			db.setContent(content);

			e.consume();

		});


		MainTable.setOnDragOver(e -> e.acceptTransferModes(TransferMode.MOVE) );

		MainTable.setOnDragDropped(e -> { 

			if (MainList.size()+1  + MainGoalieList.size() > 23) {

				JOptionPane.showMessageDialog(null, "Cannot have more than 23 skaters on roster.", "Ultimate Hockey Manager" , JOptionPane.DEFAULT_OPTION);
				
			}

			else {

				MainTable.getItems().add(MinorTable.getSelectionModel().getSelectedItem());

				MinorTable.getSelectionModel().getSelectedItem().team = Teams[temp];
				MinorTable.getSelectionModel().getSelectedItem().league = Team.League[0];

				MinorTable.getItems().remove(MinorTable.getSelectionModel().getSelectedItem());

				UserTeam.setRoster();
				
			}

			e.setDropCompleted(true);		

			e.consume();

		});

		MinorTable.setOnDragOver(e -> e.acceptTransferModes(TransferMode.MOVE) );

		MinorTable.setOnDragDropped(e -> { 

			if (MainList.size()-1  + MainGoalieList.size() < 20) {

				JOptionPane.showMessageDialog(null, "Cannot have below 18 skaters on roster.", "Ultimate Hockey Manager" , JOptionPane.DEFAULT_OPTION);

			}

			else {

				MinorTable.getItems().add(MainTable.getSelectionModel().getSelectedItem());

				MainTable.getSelectionModel().getSelectedItem().team = MinorTeams[temp];
				MainTable.getSelectionModel().getSelectedItem().league = Team.League[2];

				MainTable.getItems().remove(MainTable.getSelectionModel().getSelectedItem());

				UserTeam.setRoster();
				

			}

			e.setDropCompleted(true);

			e.consume();

		});

		MainGoalieTable.setOnDragDetected(e -> {

			//Dummy content to initiate drag and drop
			Dragboard db = MainGoalieTable.startDragAndDrop(TransferMode.ANY);
			ClipboardContent content = new ClipboardContent();
			content.putString(MainGoalieTable.getSelectionModel().getSelectedItem().toString());
			db.setContent(content);

			e.consume();

		});

		MinorGoalieTable.setOnDragDetected(e -> {

			//Dummy content to initiate drag and drop
			Dragboard db = MinorGoalieTable.startDragAndDrop(TransferMode.ANY);
			ClipboardContent content = new ClipboardContent();
			content.putString(MinorGoalieTable.getSelectionModel().getSelectedItem().toString());
			db.setContent(content);

			e.consume();

		});

		MainGoalieTable.setOnDragOver(e -> e.acceptTransferModes(TransferMode.MOVE));

		MainGoalieTable.setOnDragDropped(e -> { 

			if (MainList.size() + MainGoalieList.size()+1 > 23) {

				JOptionPane.showMessageDialog(null, "Cannot have more than 23 skaters on roster.", "Ultimate Hockey Manager" , JOptionPane.DEFAULT_OPTION);

			}

			else {

				MainGoalieTable.getItems().add(MinorGoalieTable.getSelectionModel().getSelectedItem());

				MinorGoalieTable.getSelectionModel().getSelectedItem().team = Teams[temp];
				MinorGoalieTable.getSelectionModel().getSelectedItem().league = Team.League[0];

				MinorGoalieTable.getItems().remove(MinorGoalieTable.getSelectionModel().getSelectedItem());

				UserTeam.setRoster();
				

			}

			e.setDropCompleted(true);

			e.consume();

		});

		MinorGoalieTable.setOnDragOver(e -> e.acceptTransferModes(TransferMode.MOVE));

		MinorGoalieTable.setOnDragDropped(e -> { 

			if (MainGoalieList.size()-1 < 2) {

				JOptionPane.showMessageDialog(null, "Cannot have less than 2 goalies.", "Ultimate Hockey Manager" , JOptionPane.DEFAULT_OPTION);

			}

			else {

				MinorGoalieTable.getItems().add(MainGoalieTable.getSelectionModel().getSelectedItem());

				MainGoalieTable.getSelectionModel().getSelectedItem().team = MinorTeams[temp];
				MainGoalieTable.getSelectionModel().getSelectedItem().league = Team.League[2];

				MainGoalieTable.getItems().remove(MainGoalieTable.getSelectionModel().getSelectedItem());

				UserTeam.setRoster();
				

			}

			e.setDropCompleted(true);

			e.consume();

		});
		
		mainWindow.setScene(editRoster);

	}

	@SuppressWarnings("unchecked")
	public void editLines() {	
		
		//TODO columns to add player ranking, handiness
		
		TableView<Skater> MainTable = new TableView<>();
		TableView<Goalie> MainGoalieTable = new TableView<>();

		ObservableList<Skater> MainList = FXCollections.observableArrayList();
		ObservableList<Goalie> MainGoalieList = FXCollections.observableArrayList();

		MainList.setAll(Skaters);
		MainGoalieList.setAll(Goalies);

		ObservableList<Skater> MainCopy = MainList;
		ObservableList<Goalie> GoalieCopy = MainGoalieList;
		
		MainTable.setItems(MainList);
		MainTable.getColumns().setAll(skaterName, skaterPosition, skaterAge);
		
		MainGoalieTable.setItems(MainGoalieList);
		MainGoalieTable.getColumns().setAll(goalieName, goaliePosition, goalieAge);
		
		Label[] LinesF = new Label[12];
		Label[] LinesD = new Label[6];
		Label[][] LinePP = new Label[4][5];
		Label[][] LinePK = new Label[4][4];
		Label[][] LineOT = new Label[4][3];
		Label[] Tandem = new Label[2];
		
		Label[] Titles = {new Label("Offensive Lines"), new Label ("Deffensive Lines"), new Label("Powerplay Units"), new Label("4 Man Powerplay Units"), new Label("Penalty Kill Units"), 
				new Label("3 Man Penalty Kill Units"), new Label("Overtime Lines"), new Label("Tandem")};
		
		for (int x = 0; x < Titles.length; x++) {
			
			Titles[x].setStyle("-fx-font-weight: bold;");
			
		}
		
		for (int x = 0; x < LinesF.length; x++) {
			
			final int temp = x;
			
			LinesF[x] = new Label(UserTeam.LinesF[x].shortName + " " + UserTeam.LinesF[x].position);
	
			LinesF[temp].setOnDragOver(e -> e.acceptTransferModes(TransferMode.MOVE));
			
			LinesF[temp].setOnDragDropped(e -> {
				
				LinesF[temp].setText(MainTable.getSelectionModel().getSelectedItem().shortName + " " + MainTable.getSelectionModel().getSelectedItem().position );
				
				UserTeam.LinesF[temp] = MainTable.getSelectionModel().getSelectedItem();
				
				MainTable.getSelectionModel().clearSelection();
				
				for (int y = 0; y < LinesF.length; y++) {
					
					if (temp == y) {

						continue;

					}

					else if (UserTeam.LinesF[temp].equals(UserTeam.LinesF[y]) == true) {

						LinesF[y].setText("EMPTY");

					}

				}

				for (int y = 0; y < LinesD.length; y++) {

				if (UserTeam.LinesF[temp].equals(UserTeam.LinesD[y]) == true) {

						LinesD[y].setText("EMPTY");
						
					}

				}
				
				e.setDropCompleted(true);		

				e.consume();
				
			});
			
		}

		for (int x = 0; x < LinesD.length; x++) {

			final int temp = x;
			
			LinesD[x] = new Label(UserTeam.LinesD[x].shortName + " " + UserTeam.LinesD[x].position);
			
			LinesD[temp].setOnDragOver(e -> e.acceptTransferModes(TransferMode.MOVE));

			LinesD[temp].setOnDragDropped(e -> {

				LinesD[temp].setText(MainTable.getSelectionModel().getSelectedItem().shortName + " " + MainTable.getSelectionModel().getSelectedItem().position );

				UserTeam.LinesD[temp] = MainTable.getSelectionModel().getSelectedItem();

				MainTable.getSelectionModel().clearSelection();

				for (int y = 0; y < LinesF.length; y++) {

					if (UserTeam.LinesD[temp].equals(UserTeam.LinesF[y]) == true) {

						LinesF[y].setText("EMPTY");

					}

				}

				for (int y = 0; y < LinesD.length; y++) {

					if (temp == y) {

						continue;

					}

					else if (UserTeam.LinesD[temp].equals(UserTeam.LinesD[y]) == true) {

						LinesD[y].setText("EMPTY");

					}

				}

				e.setDropCompleted(true);		

				e.consume();

			});

		}
		
		for (int x = 0; x < Tandem.length; x++) {

			final int temp = x;
			
			Tandem[x] = new Label(UserTeam.Tandem[x].shortName + " " + UserTeam.Tandem[x].position);

			Tandem[temp].setOnDragOver(e -> e.acceptTransferModes(TransferMode.MOVE) );

			Tandem[temp].setOnDragDropped(e -> {

				Tandem[temp].setText(MainGoalieTable.getSelectionModel().getSelectedItem().shortName + " " + MainGoalieTable.getSelectionModel().getSelectedItem().position );

				UserTeam.Tandem[temp] = MainGoalieTable.getSelectionModel().getSelectedItem();

				MainGoalieTable.getSelectionModel().clearSelection();
				
				e.setDropCompleted(true);		

				e.consume();

			});

		}
		
		for (int x = 0; x < LinePP.length; x++) {
			
			for (int y = 0; y < LinePP[x].length; y++) {
			
				//LinePP[x][y] = new Label(UserTeam.LinePP[x][y].shortName + " " + UserTeam.LinePP[x][y].position);
				
			}
			
		}

		for (int x = 0; x < LinePK.length; x++) {

			for (int y = 0; y < LinePK[x].length; y++) {

				//LinePP[x][y] = new Label(UserTeam.LinePP[x][y].shortName + " " + UserTeam.LinePP[x][y].position);

			}

		}

		for (int x = 0; x < LineOT.length; x++) {

			for (int y = 0; y < LineOT[x].length; y++) {

				//LinePP[x][y] = new Label(UserTeam.LinePP[x][y].shortName + " " + UserTeam.LinePP[x][y].position);

			}

		}

		GridPane Lines = new GridPane();
		Lines.setHgap(60);
		Lines.setVgap(2);
		Lines.setAlignment(Pos.TOP_CENTER);
		
		BorderPane linesLayout = new BorderPane();
		linesLayout.setLeft(MainTable);
		linesLayout.setTop(backToHub);
		linesLayout.setRight(MainGoalieTable);
		linesLayout.setCenter(Lines);
		Scene editLines = new Scene(linesLayout,windowWidth,windowHeight);

		int count = 0, count2 = 0;;
		
		//text, x, y
		//Offensive Lines
		Lines.add(Titles[0], 0, count);
		
		count++;

		//Offensive Lines
		for (int x = 0; x < 4; x++) {

			Lines.add(LinesF[count2], 0, count);
			Lines.add(LinesF[count2+1], 1, count);
			Lines.add(LinesF[count2+2], 2, count);

			count2+=3;
			
			count++;

		}
		
		//Deffensive Lines
		Lines.add(Titles[1], 0, count);
		
		count++;
		
		count2= 0;
		
		//Deffensive Lines
		for (int x = 0; x < 3; x++) {

			Lines.add(LinesD[count2], 0, count);
			Lines.add(LinesD[count2+1], 1, count);

			count2+=2;
			
			count++;

		}
		
		//Goalies
		Lines.add(Titles[7], 0, count);
		count++;
		Lines.add(Tandem[0], 0, count);
		count++;
		Lines.add(Tandem[1], 0, count);
		count++;
		
		//Powerplay
		Lines.add(Titles[2], 0, count);

		count++;

		count2= 0;
		
		//Powerplay
		for (int x = 0; x < 4; x++) {
			
			if (x < 2) {
				
				/*
				Lines.add(LinePP[x][count2], 0, count);
				Lines.add(LinePP[x][count2+1], 1, count);
				Lines.add(LinePP[x][count2+2], 2, count);
				*/
				
				//test
				Lines.add(new Label("EMPTY"), 0, count);
				Lines.add(new Label("EMPTY"), 1, count);
				Lines.add(new Label("EMPTY"), 2, count);
				
				count++;
				/*
				Lines.add(LinePP[x][count2+3], 0, count);
				Lines.add(LinePP[x][count2+4], 1, count);
				*/
				
				Lines.add(new Label("EMPTY"), 0, count);
				Lines.add(new Label("EMPTY"), 1, count);
				
				count2+=5;
				
				count++;
				
				
			}
			
			else {
				
				if (x == 2) {
					
					Lines.add(Titles[3], 0, count);
					count++;
					
				}
				
				
				/*
				Lines.add(LinePP[x][count2], 0, count);
				Lines.add(LinePP[x][count2+1], 1, count);
				Lines.add(LinePP[x][count2+2], 2, count);
				*/
				
				//test
				Lines.add(new Label("EMPTY"), 0, count);
				Lines.add(new Label("EMPTY"), 1, count);
				
				count++;
				/*
				Lines.add(LinePP[x][count2+3], 0, count);
				Lines.add(LinePP[x][count2+4], 1, count);
				*/
				
				Lines.add(new Label("EMPTY"), 0, count);
				Lines.add(new Label("EMPTY"), 1, count);
				
				count2+=4;
				
				count++;
				
			}
			
		}
		
		//Penalty Kill
		Lines.add(Titles[4], 0, count);

		count++;

		count2= 0;

		//Penalty Kill
		for (int x = 0; x < 4; x++) {

			if (x < 2) {

				/*
				Lines.add(LinePP[x][count2], 0, count);
				Lines.add(LinePP[x][count2+1], 1, count);
				Lines.add(LinePP[x][count2+2], 2, count);
				 */

				//test
				Lines.add(new Label("EMPTY"), 0, count);
				Lines.add(new Label("EMPTY"), 1, count);

				count++;
				/*
				Lines.add(LinePP[x][count2+3], 0, count);
				Lines.add(LinePP[x][count2+4], 1, count);
				 */

				Lines.add(new Label("EMPTY"), 0, count);
				Lines.add(new Label("EMPTY"), 1, count);

				count2+=4;

				count++;


			}

			else {
				

				if (x == 2) {
					
					Lines.add(Titles[5], 0, count);
					count++;
					
				}
				
				
				/*
				Lines.add(LinePP[x][count2], 0, count);
				Lines.add(LinePP[x][count2+1], 1, count);
				Lines.add(LinePP[x][count2+2], 2, count);
				 */

				//test
				Lines.add(new Label("EMPTY"), 0, count);
				Lines.add(new Label("EMPTY"), 1, count);
				Lines.add(new Label("EMPTY"), 2, count);


				count2+=4;

				count++;



			}

		}
		

		//OT Lines
		Lines.add(Titles[6], 0, count);

		count++;

		count2= 0;

		//OT Lines
		for (int x = 0; x < 4; x++) {

			/*
				Lines.add(LinePP[x][count2], 0, count);
				Lines.add(LinePP[x][count2+1], 1, count);
				Lines.add(LinePP[x][count2+2], 2, count);
			 */

			//test
			Lines.add(new Label("EMPTY"), 0, count);
			Lines.add(new Label("EMPTY"), 1, count);
			Lines.add(new Label("EMPTY"), 2, count);


			count2+=4;

			count++;


		}


		
		//Get UserTeam Skaters
		for (int x = 0; x < MainCopy.size(); x++) {

			if (MainCopy.get(x).team != UserTeam) {

				MainTable.getItems().remove(x);

				x--;

			}

		}
		
		//Get UserTeam Goalies
		for (int x = 0; x < GoalieCopy.size(); x++) {

			if (GoalieCopy.get(x).team != UserTeam) {

				MainGoalieTable.getItems().remove(x);

				x--;

			}

		}
		
		MainTable.setOnDragDetected(e -> {

			//Dummy content to initiate drag and drop
			Dragboard db = MainTable.startDragAndDrop(TransferMode.ANY);
			ClipboardContent content = new ClipboardContent();
			content.putString(MainTable.getSelectionModel().getSelectedItem().toString());
			db.setContent(content);

			e.consume();

		});
		
		MainGoalieTable.setOnDragDetected(e -> {

			//Dummy content to initiate drag and drop
			Dragboard db = MainGoalieTable.startDragAndDrop(TransferMode.ANY);
			ClipboardContent content = new ClipboardContent();
			content.putString(MainGoalieTable.getSelectionModel().getSelectedItem().toString());
			db.setContent(content);

			e.consume();

		});
		

		mainWindow.setScene(editLines);

	}

	public void resetLists() {

		teamList.setAll(Teams);
		skaterList.setAll(Skaters);
		goalieList.setAll(Goalies);

		teamCopy = teamList;
		skaterCopy = skaterList;
		goalieCopy = goalieList;

		refreshTables();

	}

	@SuppressWarnings("unchecked")
	public void getAllPlayerStats() {

		playerStatsLayout.getChildren().clear();

		skaterLeaders.getSortOrder().clear();
		goalieLeaders.getSortOrder().clear();

		skaterLeaders.setPrefSize(windowWidth, windowHeight);
		goalieLeaders.setPrefSize(windowWidth, windowHeight);

		String[] positions = {"All Skaters", "Centers", "Left Wings", "Right Wings", "Defencemen", "Goaltenders"};
		String[] seasonTypes = {"Regular Season", "Playoffs"};

		Button nextPosition = new Button("Next");
		Button previousPosition = new Button("Previous");

		Button nextLeague = new Button("Next");
		Button previousLeague= new Button("Previous");

		Button nextTeam = new Button("Next");
		Button previousTeam = new Button("Previous");

		Button changeSeasonType = new Button("");

		if (currentS == 0) {

			changeSeasonType.setText(seasonTypes[1]);

		}

		else if (currentS == 1) {

			changeSeasonType.setText(seasonTypes[0]);

		}
		
		Label currentLeague = new Label(Team.League[currentL]);
		Label currentTeam = new Label(Teams[currentT].name);
		Label currentPosition = new Label(positions[currentP]);

		HBox leagues = new HBox(20);
		HBox teams = new HBox(20);
		HBox position = new HBox(20);

		leagues.getChildren().addAll(previousLeague, currentLeague, nextLeague);
		teams.getChildren().addAll(previousTeam,currentTeam,nextTeam);
		position.getChildren().addAll(previousPosition, currentPosition, nextPosition);

		skaterLeaders.getColumns().setAll(skaterName, skaterAge, skaterPosition, skaterGamesPlayed, skaterGoals, skaterAssists, skaterPoints, skaterPIM);

		goalieLeaders.getColumns().setAll(goalieName, goalieAge, goaliePosition, goalieGamesPlayed, goalieWins, goalieLosses, goalieOTL, goalieSO, goalieGoals, goalieAssists, goaliePoints, goaliePIM);
		
		playerStatsLayout.add(leagues, 0, 0);
		playerStatsLayout.add(teams, 0, 1);
		playerStatsLayout.add(position, 0, 2);
		playerStatsLayout.add(changeSeasonType, 0, 3);
		playerStatsLayout.add(backToHub, 0, 4);

		//If returning from an indivdual's stats page, keeps the same order that was there
		if (currentP != 5 ) {

			playerStatsLayout.add(skaterLeaders, 0, 5);

		}
		
		else {
			
			playerStatsLayout.add(goalieLeaders, 0, 5);
			
		}
		
		updateTeamSelect();

		refreshTables();

		mainWindow.setScene(playerStats);

		skaterLeaders.addEventFilter(MouseEvent.MOUSE_CLICKED, e-> {

			//Prevents players from being clickable in hub
			if (mainWindow.getScene() == playerStats && skaterLeaders.getSelectionModel().getSelectedItem() != null) {

				skaterPlayerSceen(skaterLeaders.getSelectionModel().getSelectedItem());

				skaterLeaders.getSelectionModel().clearSelection();

			}
			
		});

		goalieLeaders.addEventFilter(MouseEvent.MOUSE_CLICKED, e-> {

			//Prevents players from being clickable in hub
			if (mainWindow.getScene() == playerStats && goalieLeaders.getSelectionModel().getSelectedItem() != null) {

				goaliePlayerSceen(goalieLeaders.getSelectionModel().getSelectedItem());
				
				goalieLeaders.getSelectionModel().clearSelection();
				
			}

		});		

		nextPosition.setOnAction(e ->  {

			resetLists();

			currentP = nextOption(currentP,positions.length);

			updateLeagueSelect();

			updatePositionSelect();

			updateTeamSelect();

			refreshTables();

			currentPosition.setText(positions[currentP]);

		});

		previousPosition.setOnAction(e -> {

			resetLists();

			currentP = previousOption(currentP,positions.length);

			updateLeagueSelect();

			updatePositionSelect();

			updateTeamSelect();

			refreshTables();

			currentPosition.setText(positions[currentP]);

		});

		nextTeam.setOnAction(e -> {

			resetLists();

			currentT = nextOption(currentT, AllTeams[currentL].length);

			updateLeagueSelect();

			updatePositionSelect();

			updateTeamSelect();

			currentTeam.setText(AllTeams[currentL][currentT].name);

		});

		previousTeam.setOnAction(e -> {

			resetLists();

			currentT = previousOption(currentT, AllTeams[currentL].length);

			updateLeagueSelect();

			updatePositionSelect();

			updateTeamSelect();

			currentTeam.setText(AllTeams[currentL][currentT].name);

		});

		nextLeague.setOnAction(e -> {

			currentT = 0;

			resetLists();

			currentL =  nextOption(currentL,Team.League.length-1);

			updateLeagueSelect();

			updatePositionSelect();

			updateTeamSelect();

			refreshTables();

			currentTeam.setText(AllTeams[currentL][currentT].name);
			currentLeague.setText(Team.League[currentL]);

		});

		previousLeague.setOnAction(e -> {

			currentT = 0;

			resetLists();

			currentL =	previousOption(currentL,Team.League.length-1);

			updateLeagueSelect();

			updatePositionSelect();

			updateTeamSelect();

			refreshTables();

			currentTeam.setText(AllTeams[currentL][currentT].name);
			currentLeague.setText(Team.League[currentL]);

		});

		changeSeasonType.setOnAction(e -> {

			resetLists();

			currentS = nextOption(currentS, seasonTypes.length );

			updateLeagueSelect();

			updatePositionSelect();

			updateTeamSelect();

			refreshTables();

			if (currentS == 0) {

				changeSeasonType.setText(seasonTypes[1]);
			}

			else if (currentS == 1) {

				changeSeasonType.setText(seasonTypes[0]);
			}

		});

	}

	@SuppressWarnings("unchecked")
	void contracts() {
		
		TableView<Player> contracts = new TableView<>();
		
		TableColumn<Player, String> name = new TableColumn<>("Name");
		TableColumn<Player, String> position = new TableColumn<>("Position");
		TableColumn<Player, String> age = new TableColumn<>("Age");
		TableColumn<Player, String> contractAmount = new TableColumn<>("$");
		TableColumn<Player, String> contractLength = new TableColumn<>("Years");
		
		name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
		position.setCellValueFactory(new PropertyValueFactory<>("position"));
		age.setCellValueFactory(new PropertyValueFactory<>("age"));
		contractAmount.setCellValueFactory(new PropertyValueFactory<>("contractA"));
		contractLength.setCellValueFactory(new PropertyValueFactory<>("contractL"));
		
		ObservableList<Player> UserTeamList = FXCollections.observableArrayList();
		
		UserTeamList.addAll(Skaters);
		UserTeamList.addAll(Goalies);

		ObservableList<Player> Copy = UserTeamList;
		
		contracts.setItems(UserTeamList);
		contracts.getColumns().setAll(name, position, age, contractAmount, contractLength);
		
		for (int x = 0; x < Copy.size(); x++) {
			
			if (Copy.get(x).team != UserTeam) {

				contracts.getItems().remove(x);

				x--;

			}
			
		}
		
		VBox contractsLayout = new VBox();
		
		contractsLayout.getChildren().setAll(backToHub, contracts);
		
		Scene contractsScene = new Scene(contractsLayout, windowWidth, windowHeight);
		
		mainWindow.setScene(contractsScene);
		
		
	}

	@SuppressWarnings("unchecked")
	void skaterPlayerSceen(Skater player) {

		//TODO Have list of stats move down one after to accommodate room for new stats
		//TODO Add other player Information
		
		//TODO Test Print out ratings + attributes
		System.out.println("Rating" + " " + player.rating);
		
		VBox playerScreenLayout = new VBox();

		Button backToStats = new Button("Back");

		if (player.seasonStats[1][0] == null || player.seasonStats[1][0] == season.getText() ) {
			
			player.seasonStats[1][0] = season.getText();
			player.seasonStats[1][1] = player.team.getName();
			player.seasonStats[1][2] = String.valueOf(player.gamesPlayed[0]);
			player.seasonStats[1][3] = String.valueOf(player.goals[0]);
			player.seasonStats[1][4] = String.valueOf(player.assists[0]);
			player.seasonStats[1][5] = String.valueOf(player.points[0]);
			player.seasonStats[1][6] = String.valueOf(player.PIM[0]);

		}
		
		if (player.playoffStats[1][0] == null || player.playoffStats[1][0] == season.getText() ) {
			
			player.playoffStats[1][0] = season.getText();
			player.playoffStats[1][1] = player.team.getName();
			player.playoffStats[1][2] = String.valueOf(player.gamesPlayed[1]);
			player.playoffStats[1][3] = String.valueOf(player.goals[1]);
			player.playoffStats[1][4] = String.valueOf(player.assists[1]);
			player.playoffStats[1][5] = String.valueOf(player.points[1]);
			player.playoffStats[1][6] = String.valueOf(player.PIM[1]);

		}

		Scene playerScreen = new Scene(playerScreenLayout, windowWidth, windowHeight);

		ObservableList<String[]> playerStatsList = FXCollections.observableArrayList();
		ObservableList<String[]> playerPlayoffStatsList = FXCollections.observableArrayList();

		playerStatsList.addAll(Arrays.asList(player.seasonStats));
		playerStatsList.remove(0);//remove titles from data
		
		playerPlayoffStatsList.addAll(Arrays.asList(player.playoffStats));
		playerPlayoffStatsList.remove(0);//remove titles from data

		TableView<String[]> table = new TableView<>();

		for (int i = 0; i < player.seasonStats[0].length; i++) {


			@SuppressWarnings("rawtypes")
			TableColumn tc = new TableColumn(player.seasonStats[0][i]);

			final int colNo = i;
			tc.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {

				@Override
				public ObservableValue<String> call(CellDataFeatures<String[], String> p) {

					return new SimpleStringProperty((p.getValue()[colNo]));

				}

			});

			table.getColumns().add(tc);
		}

		table.setItems(playerStatsList);
		

		TableView<String[]> tablePlayoffs = new TableView<>();

		for (int i = 0; i < player.playoffStats[0].length; i++) {

			@SuppressWarnings("rawtypes")
			TableColumn tcPlayoffs = new TableColumn(player.playoffStats[0][i]);

			final int colNo = i;
			tcPlayoffs.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {

				@Override
				public ObservableValue<String> call(CellDataFeatures<String[], String> p) {

					return new SimpleStringProperty((p.getValue()[colNo]));

				}

			});

			tablePlayoffs.getColumns().add(tcPlayoffs);
		}

		tablePlayoffs.setItems(playerPlayoffStatsList);
		
		HBox tables = new HBox();
		tables.getChildren().addAll(table, tablePlayoffs);

		playerScreenLayout.getChildren().setAll(backToStats, new Label(player.fullName), tables);

		mainWindow.setScene(playerScreen);

		backToStats.setOnAction(e -> getAllPlayerStats());
		

	}


	@SuppressWarnings("unchecked")
	void goaliePlayerSceen(Goalie player) {

		//TODO Have list of stats move down one after to accommodate room for new stats
		//TODO Add other player Information
		
		VBox playerScreenLayout = new VBox();

		Button backToStats = new Button("Back");

		if (player.seasonStats[1][0] == null || player.seasonStats[1][0] == season.getText()) {

			player.seasonStats[1][0] = season.getText();
			player.seasonStats[1][1] = player.team.getName();
			player.seasonStats[1][2] = String.valueOf(player.gamesPlayed[0]);
			player.seasonStats[1][3] = String.valueOf(player.wins[0]);
			player.seasonStats[1][4] = String.valueOf(player.losses[0]);
			player.seasonStats[1][5] = String.valueOf(player.OTL[0]);
			player.seasonStats[1][6] = String.valueOf(player.shutouts[0]);
			player.seasonStats[1][9] = String.valueOf(player.goals[0]);
			player.seasonStats[1][10] = String.valueOf(player.assists[0]);
			player.seasonStats[1][11] = String.valueOf(player.points[0]);
			player.seasonStats[1][14] = String.valueOf(player.PIM[0]);
			
		}
		

		if (player.playoffStats[1][0] == null || player.playoffStats[1][0] == season.getText() ) {

			player.playoffStats[1][0] = season.getText();
			player.playoffStats[1][1] = player.team.getName();
			player.playoffStats[1][2] = String.valueOf(player.gamesPlayed[1]);
			player.playoffStats[1][3] = String.valueOf(player.wins[1]);
			player.playoffStats[1][4] = String.valueOf(player.losses[1]);
			player.playoffStats[1][5] = String.valueOf(player.OTL[1]);
			player.playoffStats[1][6] = String.valueOf(player.shutouts[1]);
			player.playoffStats[1][9] = String.valueOf(player.goals[1]);
			player.playoffStats[1][10] = String.valueOf(player.assists[1]);
			player.playoffStats[1][11] = String.valueOf(player.points[1]);
			player.playoffStats[1][14] = String.valueOf(player.PIM[1]);
			
		}

		Scene playerScreen = new Scene(playerScreenLayout, windowWidth, windowHeight);

		ObservableList<String[]> playerStatsList = FXCollections.observableArrayList();
		ObservableList<String[]> playerPlayoffStatsList = FXCollections.observableArrayList();

		playerStatsList.addAll(Arrays.asList(player.seasonStats));
		playerStatsList.remove(0);//remove titles from data
		
		playerPlayoffStatsList.addAll(Arrays.asList(player.playoffStats));
		playerPlayoffStatsList.remove(0);//remove titles from data

		TableView<String[]> table = new TableView<>();

		for (int i = 0; i < player.seasonStats[0].length; i++) {


			@SuppressWarnings("rawtypes")
			TableColumn tc = new TableColumn(player.seasonStats[0][i]);

			final int colNo = i;
			tc.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {

				@Override
				public ObservableValue<String> call(CellDataFeatures<String[], String> p) {

					return new SimpleStringProperty((p.getValue()[colNo]));

				}

			});

			table.getColumns().add(tc);
		}

		table.setItems(playerStatsList);


		TableView<String[]> tablePlayoffs = new TableView<>();

		for (int i = 0; i < player.playoffStats[0].length; i++) {


			@SuppressWarnings("rawtypes")
			TableColumn tc = new TableColumn(player.playoffStats[0][i]);

			final int colNo = i;
			tc.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {

				@Override
				public ObservableValue<String> call(CellDataFeatures<String[], String> p) {

					return new SimpleStringProperty((p.getValue()[colNo]));

				}

			});

			tablePlayoffs.getColumns().add(tc);
		}

		tablePlayoffs.setItems(playerPlayoffStatsList);
		
		HBox tables = new HBox();
		tables.getChildren().addAll(table, tablePlayoffs);

		playerScreenLayout.getChildren().setAll(backToStats, new Label(player.fullName), tables);

		mainWindow.setScene(playerScreen);

		backToStats.setOnAction(e -> getAllPlayerStats());

	}
	

	public void updateLeagueSelect() {

		for (int x = 0; x < skaterCopy.size(); x++) {

			if (skaterCopy.get(x).league != AllTeams[currentL][currentT].league) {

				skaterLeaders.getItems().remove(x);

				x--;

			}

		}

		for (int x = 0; x < goalieCopy.size(); x++) {

			if (goalieCopy.get(x).league != AllTeams[currentL][currentT].league) {

				goalieLeaders.getItems().remove(x);

				x--;

			}

		}

	}

	public void updatePositionSelect() { 

		//Goalies
		if (currentP == 5) {

			if (playerStatsLayout.getChildren().contains(goalieLeaders) == false) {

				playerStatsLayout.getChildren().remove(skaterLeaders);

				playerStatsLayout.add(goalieLeaders, 0, 5);

			}

		}

		//All Skaters
		else if (currentP == 0) {

			if (playerStatsLayout.getChildren().contains(skaterLeaders) == false) {

				playerStatsLayout.getChildren().remove(goalieLeaders);

				playerStatsLayout.add(skaterLeaders, 0, 5);
			}

		}

		else {

			//Defence
			if (currentP == 4) {

				if (playerStatsLayout.getChildren().contains(skaterLeaders) == false) {

					playerStatsLayout.getChildren().remove(goalieLeaders);

					playerStatsLayout.add(skaterLeaders, 0, 5);

				}



			}


			for (int x = 0; x < skaterCopy.size(); x++) {

				if (skaterCopy.get(x).position != Skater.positionType[currentP-1]) {

					skaterLeaders.getItems().remove(x);

					x--;

				}

			}

		}

	}


	public void updateTeamSelect() {

		for (int x = 0; x < skaterCopy.size(); x++) {

			if (skaterCopy.get(x).team != AllTeams[currentL][currentT]) {

				skaterLeaders.getItems().remove(x);

				x--;

			}

		}

		for (int x = 0; x < goalieCopy.size(); x++) {

			if (goalieCopy.get(x).team != AllTeams[currentL][currentT]) {

				goalieLeaders.getItems().remove(x);

				x--;

			}

		}

		refreshTables();

	}


	public int nextOption(int number, int length) {


		if (number == (length-1)) {

			number = 0;

		}

		else {

			number++;

		}

		return number;

	}

	public int previousOption(int number, int length) {

		if (number == 0) {

			number = length-1;

		}

		else {

			number--;

		}

		return number;

	}

	public static void refreshTables() {

		//Refresh
		teamStandings.refresh();
		skaterLeaders.refresh();
		goalieLeaders.refresh();

	}


	public void setCalendarButtons() {

		//reset
		displayCalendar.set(gameCalendar.get(Calendar.YEAR), gameCalendar.get(Calendar.MONTH), gameCalendar.get(Calendar.DATE), 0, 0, 0);

		//create buttons, setup layout and allow user to sim days
		for (int x = 0; x < days.length; x++) {

			final int temp = x;

			days[x] = new Button();
			dayText[x] = new Label();
			days[x].setMinSize((windowWidth/(days.length)), (windowWidth/(days.length)));
			dayText[x].setPadding(new Insets(0,0,0,(windowWidth/days.length)/2.8));
			dayText[x].setPrefWidth(windowWidth/days.length);

			//Sim Day to date displayed on button
			days[x].setOnAction(e -> {

				//Check to see if day# on calendar is within month or not
				for (int y = 0; y < days.length; y++) {

					//Prevent search from going out of bounds
					if ((temp+y) > days.length-1) {

						break;

					}

					//if the next day is less than the day of the button clicked, it must be in the previous month
					if (Integer.parseInt(days[temp+y].getText()) < Integer.parseInt(days[temp].getText())) {

						displayCalendar.add(Calendar.MONTH, -1);

						break;

					}
				}


				//Sets up comparable from button clicked
				displayCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(days[temp].getText()));


				if (simGame.isDisabled() == false) {

					simGame.fire();

				}


				while (displayCalendar.get(Calendar.DAY_OF_YEAR) != gameCalendar.get(Calendar.DAY_OF_YEAR)) {

					//THings that need to update over the passage of time go here
					//TODO Have method that removes players with 0 gp or sometin
					
					gameCalendar.add(Calendar.DATE, 1);

					getcurrentSeason();
					
					//Start next season
					if (gameCalendar.get(Calendar.MONTH) == Calendar.AUGUST && gameCalendar.get(Calendar.DATE) == 20 ) {

						//TODO This should be where everything resets
						
						MainSeasons = new Season(0);
						EuroSeasons = new Season(1);
						MinorSeasons = new Season(2);
						ProspectSeasons = new Season(3);

					}

					MainSeasons.main();
					EuroSeasons.main();
					MinorSeasons.main();
					ProspectSeasons.main();


					if (displayCalendar.get(Calendar.DAY_OF_YEAR) != gameCalendar.get(Calendar.DAY_OF_YEAR) ) {

						MainSeasons.userTeamPlayGames();

					}

				}

				updateCalendarButtons();
				
				userTeamRecord.setText(UserTeam.Wins + " - " + UserTeam.Losses + " - " + UserTeam.OTLosses);
				
				refreshTables();

			});

		}

		updateCalendarButtons();
		
		refreshTables();

	}
	

	public void updateCalendarButtons() {

		//reset
		weekText.getChildren().clear();
		week.getChildren().clear();
		simGame.setDisable(true);

		for (int x = 0; x < days.length; x++) {

			//Reset to default style
			days[x].setStyle("");

			if (x == 0 ) {

				dayText[0].setText(displayCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale));

				//Current Days
				if (displayCalendar.get(Calendar.DAY_OF_YEAR) == gameCalendar.get(Calendar.DAY_OF_YEAR)) {

					days[0].setText(String.valueOf(gameCalendar.get(Calendar.DATE)));
					days[0].setTooltip(new Tooltip("Current Day: " + displayCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale) + " " + String.valueOf(displayCalendar.get(Calendar.DATE) + suffixes[displayCalendar.get(Calendar.DATE)])));	
					days[0].setDisable(true);

				}

				else  {

					//Past Days
					if (displayCalendar.before(gameCalendar) == true) {

						days[0].setText(String.valueOf(displayCalendar.get(Calendar.DATE)));
						days[0].setDisable(true);
					}

					//Future Days
					else {

						days[0].setText(String.valueOf(displayCalendar.get(Calendar.DATE)));
						days[0].setTooltip(new Tooltip("Sim to " + displayCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale) + " " + String.valueOf(displayCalendar.get(Calendar.DATE) + suffixes[displayCalendar.get(Calendar.DATE)])));
						days[0].setDisable(false);

					}
				}

			}

			else {

				displayCalendar.add(Calendar.DATE, 1);

				dayText[x].setText(displayCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale));

				days[x].setText(String.valueOf(displayCalendar.get(Calendar.DATE)));

				days[x].setTooltip(new Tooltip("Sim to " + displayCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale)  + " " + String.valueOf(displayCalendar.get(Calendar.DATE)) + suffixes[displayCalendar.get(Calendar.DATE)]));

				//Past Days
				if (displayCalendar.before(gameCalendar) == true) {

					days[x].setDisable(true);

				}

				//Future Days
				else {

					days[x].setDisable(false);

				}

			}

			for (int z = 0; z < MainSeasons.TeamMatchup.length; z++) {

				if (displayCalendar.get(Calendar.DAY_OF_YEAR) == MainSeasons.scheduleCalendar[z].get(Calendar.DAY_OF_YEAR)) {

					if (displayCalendar.get(Calendar.DAY_OF_YEAR) == gameCalendar.get(Calendar.DAY_OF_YEAR) &&  (
							(UserTeam == MainSeasons.TeamMatchup[z][0] ) || UserTeam == MainSeasons.TeamMatchup[z][1] 
							) ) {
						
						simGame.setDisable(false);

					}

					//User Home Game
					if (UserTeam == MainSeasons.TeamMatchup[z][0] ) {


						days[x].setStyle("-fx-background-color: #17AEA3; ");

						break;
					}

					//User Away Game
					else if (UserTeam == MainSeasons.TeamMatchup[z][1] ) {

						days[x].setStyle("-fx-background-color: #C74F4F; ");

						break;

					}

				}

			}

		}

		for (int x = 0; x < days.length; x++) {

			days[x].setAlignment(Pos.BOTTOM_LEFT);
			days[x].setStyle(days[x].getStyle() + "-fx-font-size: 2em; ");
			week.getChildren().add(days[x]);
			weekText.getChildren().add(dayText[x]);

		}
		
		month.setText(displayCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale));

	}

	public void save() {


	}

	public void load() {


	}

	//Fill teams with randomly generated skaters and goalies
	public static void populate() {

		int count = 0, count2 = 0;

		//Create Teams
		for (int x = 0; x < AllTeams.length; x++) {

			for (int y = 0; y < AllTeams[x].length; y++) {

				AllTeams[x][y] = new Team(y,count);

			}

			count++;

		}

		count = 0;

		//Skaters
		for (int x = 0; x < AllTeams.length; x++) {

			for (int y =0; y < AllTeams[x].length; y++) {

				for (int a = 0; a < Team.rosterLimit; a++  ) {

					//Centers
					if (a <= 3) {

						Skaters[count] = new Skater(0,y,count2);

						count++;

					}

					//Left Wings
					else if (a > 3 && a <= 7) {

						Skaters[count] = new Skater(1,y,count2);

						count++;

					}

					//Right Wings
					else if (a > 7 && a <= 11) {

						Skaters[count] = new Skater(2,y,count2);

						count++;


					}

					//Defensemen
					else if (a > 11 && a <= 17) {

						Skaters[count] = new Skater(3,y,count2);

						count++;

					}

					else {

						Skaters[count] = new Skater((int)(Math.random()*Skater.positionType.length-1),y,count2);

						count++;

					}
				}

			}

			count2++;

		}

		//Free Agents Skaters
		while (count < Skaters.length) {

			Skaters[count] = new Skater((int)(Math.random()*Skater.positionType.length-1),0,4);

			count++;

		}

		count = 0;
		count2 = 0;

		//Goalies
		for (int x = 0; x < AllTeams.length; x++) {

			for (int y =0; y < AllTeams[x].length; y++) {

				for (int a = 0; a < Team.rosterGlimit-1; a++  ) {

					Goalies[count] = new Goalie(y,count2);
					
					count++;

				}

			}

			count2++;

		}

		//Free Agents Goalies
		while (count < Goalies.length) {

			Goalies[count] = new Goalie(0,4);

			count++;

		}

		count = 0;
		count2 = 0;

		//Set Team Rosters
		for (int x = 0; x < AllTeams.length; x++) {

			for (int y =0; y < AllTeams[x].length; y++) {

				AllTeams[x][y].setRoster();

			}

			count++;

		}

	}
	
	

}



