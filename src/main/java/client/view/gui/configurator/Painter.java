package client.view.gui.configurator;

import client.view.gui.LoaderResources;
import client.view.gui.MatchCityPane;
import controller.Player;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.scene.control.gauge.linear.elements.Segment;
import model.*;
import model.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * This class is used to paint and repaint the GameBoard when the server send the updates
 */
public class Painter {

	private static final int numCols = 5;
	private static final int numRows = 11;

	private GridPane region1, region2, region3;

	private String css;

	private Pane linesPane;

	private ArrayList<Line> links;

	private List<SingleLink> linksBetweenCities;

	private CitiesListener citiesListener;

	private StackPane stackPane;

	private List<MatchCityPane> matchCitiesPanes;

	private ArrayList<RegionRunLater> regionRunLaters;

	private SimpleMetroArcGauge victoryPointsIndicators;

	private SimpleMetroArcGauge nobilityPointsIndicators;
	private SimpleMetroArcGauge numberOfAssistants;
	private SimpleMetroArcGauge numberOfEmporium;

	private SimpleMetroArcGauge coinsIndicators;

	private ArrayList<Tile> unusedPermitTile;

	private BoardController boardController;


	public Painter(StackPane stackPane, GridPane region1, GridPane region2, GridPane region3, Pane linesPane,
			CitiesListener citiesListener, BoardController boardController) {
		this.boardController = boardController;
		victoryPointsIndicators = new SimpleMetroArcGauge();
		nobilityPointsIndicators = new SimpleMetroArcGauge();
		coinsIndicators = new SimpleMetroArcGauge();
		numberOfAssistants = new SimpleMetroArcGauge();
		numberOfEmporium = new SimpleMetroArcGauge();

		victoryPointsIndicators.setMaxValue(100);
		victoryPointsIndicators.setMinValue(0);

		nobilityPointsIndicators.setMaxValue(20);
		nobilityPointsIndicators.setMinValue(0);

		coinsIndicators.setMaxValue(20);
		coinsIndicators.setMinValue(0);

		numberOfAssistants.setMaxValue(100);
		numberOfAssistants.setMinValue(0);

		numberOfEmporium.setMaxValue(10);
		numberOfEmporium.setMinValue(0);

		coinsIndicators.getStyleClass().add("colorscheme-indicator");
		for (int i = 0; i < 10; i++) {
			Segment lSegment = new PercentSegment(coinsIndicators, i * 10.0, (i + 1) * 10.0);
			coinsIndicators.segments().add(lSegment);
		}

		nobilityPointsIndicators.getStyleClass().add("colorscheme-indicator");
		for (int i = 0; i < 10; i++) {
			Segment lSegment = new PercentSegment(nobilityPointsIndicators, i * 10.0, (i + 1) * 10.0);
			nobilityPointsIndicators.segments().add(lSegment);
		}

		victoryPointsIndicators.getStyleClass().add("colorscheme-indicator");
		for (int i = 0; i < 10; i++) {
			Segment lSegment = new PercentSegment(victoryPointsIndicators, i * 10.0, (i + 1) * 10.0);
			victoryPointsIndicators.segments().add(lSegment);
		}
		numberOfAssistants.getStyleClass().add("colorscheme-indicator");
		for (int i = 0; i < 10; i++) {
			Segment lSegment = new PercentSegment(numberOfAssistants, i * 10.0, (i + 1) * 10.0);
			numberOfAssistants.segments().add(lSegment);
		}

		numberOfEmporium.getStyleClass().add("colorscheme-indicator-emporium");
		for (int i = 0; i < 10; i++) {
			Segment lSegment = new PercentSegment(numberOfEmporium, i * 10.0, (i + 1) * 10.0);
			numberOfEmporium.segments().add(lSegment);
		}
		this.stackPane = stackPane;
		this.citiesListener = citiesListener;
		links = new ArrayList<>();
		linksBetweenCities = new ArrayList<>();
		matchCitiesPanes = new ArrayList<>();
		css = LoaderResources.loadPath("/configurator/style.css");
		this.linesPane = linesPane;
		this.region1 = region1;
		this.region2 = region2;
		this.region3 = region3;
		addCells();

	}

	private void addCells() {
		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConstraints = new ColumnConstraints();
			colConstraints.setHgrow(Priority.SOMETIMES);
			region1.getColumnConstraints().add(colConstraints);
			region2.getColumnConstraints().add(colConstraints);
			region3.getColumnConstraints().add(colConstraints);
		}

		for (int i = 0; i < numRows; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setVgrow(Priority.SOMETIMES);
			region1.getRowConstraints().add(rowConstraints);
			region2.getRowConstraints().add(rowConstraints);
			region3.getRowConstraints().add(rowConstraints);
		}

	}

	public void repaint(Region[] regions) {
		Platform.runLater(() -> {
			linesPane.getChildren().clear();
			region1.getChildren().clear();
			region2.getChildren().clear();
			region3.getChildren().clear();

			matchCitiesPanes.clear();
			links.clear();

			regionRunLaters = new ArrayList<RegionRunLater>();
			List<City> regionOne = regions[0].getCities();
			List<City> regionTwo = regions[1].getCities();
			List<City> regionThree = regions[2].getCities();
			fillGrid(regionOne, region1);
			fillGrid(regionTwo, region2);
			fillGrid(regionThree, region3);

			for (RegionRunLater regionRunLater : regionRunLaters) {
				if (regionRunLater.getRegion().equals(region1))
					region1.add(regionRunLater.getPane(), regionRunLater.getColIndex(), regionRunLater.getRowIndex());
				else if (regionRunLater.getRegion().equals(region2))
					region2.add(regionRunLater.getPane(), regionRunLater.getColIndex(), regionRunLater.getRowIndex());
				else if (regionRunLater.getRegion().equals(region3))
					region3.add(regionRunLater.getPane(), regionRunLater.getColIndex(), regionRunLater.getRowIndex());
			}

			region1.setPickOnBounds(false);
			region2.setPickOnBounds(false);
			region3.setPickOnBounds(false);

			addLinks(regionOne);
			addLinks(regionTwo);
			addLinks(regionThree);
		});

	}

	private void addLinks(List<City> region) {
		List<City> connectedCities;
		MatchCityPane matchSecondCityPane = null;
		MatchCityPane matchFirstCityPane = null;

		for (City city : region) {
			for (MatchCityPane cityPane : matchCitiesPanes) {
				if (city.equals(cityPane.getCity())) {
					matchFirstCityPane = cityPane;
				}
			}
			connectedCities = city.getConnectedCities();
			if (matchFirstCityPane != null)
				for (City connectedCity : connectedCities) {
					for (MatchCityPane cityPane : matchCitiesPanes) {
						if (connectedCity.equals(cityPane.getCity())) {
							matchSecondCityPane = cityPane;
						}
					}
					if (matchSecondCityPane != null) {
						citiesListener.setFirstLink(matchFirstCityPane.getPane(), city);
						citiesListener.setAutomaticSecondLink(matchSecondCityPane.getPane(), connectedCity);
					}
				}
		}
	}

	private void fillGrid(List<City> region, GridPane regionGrid) {
		int k = 0;
		int j;
		for (int i = 1; i < numRows; i++) {
			if (i % 2 != 0)
				j = 1;
			else
				j = 3;
			if (k < region.size()) {
				addCity(regionGrid, region.get(k), i, j);
				k++;
			}
		}
	}

	private void addCity(GridPane region, City city, int rowIndex, int colIndex) {
		Pane pane = new Pane();
		pane.getStylesheets().add(css);
		if (!city.getKingIsHere())
			pane.getStyleClass().add(city.getColor() + "Castle");
		else
			pane.getStyleClass().add("PurpleCastle");
		pane.getStyleClass().add("city");

		pane.setOnMouseClicked(event -> {
			citiesListener.cityClicked(pane, city);
		});
		pane.setOnMouseEntered(event -> {
			citiesListener.cityEntered(city);
		});
		pane.setOnMouseExited(event -> {
			citiesListener.cityExited(city);
		});
		regionRunLaters.add(new RegionRunLater(pane, colIndex, rowIndex, region));
		matchCitiesPanes.add(new MatchCityPane(city, pane));
	}

	public void createLine(Pane firstLink, Pane secondLink, City city1, City city2) {
		double x1, y1, x2, y2;
		Parent parentFirstLink = firstLink.getParent();
		Parent parentSecondLink = secondLink.getParent();

		if (parentFirstLink == null || parentSecondLink == null) {
			return;
		}

		if (parentFirstLink.equals(region1)) {
			x1 = 50;
			y1 = 30;
		} else if (parentFirstLink.equals(region2)) {
			x1 = 460;
			y1 = 30;
		} else {
			x1 = 850;
			y1 = 20;
		}

		if (parentSecondLink.equals(region1)) {
			x2 = 50;
			y2 = 30;
		} else if (parentSecondLink.equals(region2)) {
			x2 = 460;
			y2 = 30;
		} else {
			x2 = 850;
			y2 = 20;
		}

		Platform.runLater(() -> {
			double xOne;
			double yOne;
			double xTwo;
			double yTwo;
			xOne = firstLink.getLayoutX();
			yOne = firstLink.getLayoutY();
			xTwo = secondLink.getLayoutX();
			yTwo = secondLink.getLayoutY();

			if (xOne == 0 || yOne == 0 || xTwo == 0 || yTwo == 0)
				boardController.repaintBoard();
			Line line = new Line(xOne + x1, yOne + y1, xTwo + x2, yTwo + y2);
			line.setStrokeWidth(10);
			links.add(line);
			linksBetweenCities.add(new SingleLink(city1, city2, line));
			line.setOnMouseClicked(event -> {
				citiesListener.removeLink(line);
			});
			line.getStyleClass().add("line");
			linesPane.getChildren().add(line);
		});
	}

	public List<SingleLink> getLinksBetweenCities() {
		return this.linksBetweenCities;
	}

	public void repaintPlayerStatus(Player player, GridPane indicatorPane, GridPane topIndicatorPane) {
		int coins = player.getCoins();
		int nobilityTrack = player.getPositionInNobilityTrack();
		int victoryPoints = player.getVictoryPoints();
		int numberOfAssistants = player.getNumberOfAssistants();
		int numberOfEmporium = player.getNumberOfEmporium();

		coinsIndicators.setValue(coins);
		nobilityPointsIndicators.setValue(nobilityTrack);
		victoryPointsIndicators.setValue(victoryPoints);
		this.numberOfAssistants.setValue(numberOfAssistants);
		this.numberOfEmporium.setValue(numberOfEmporium);

		Platform.runLater(() -> {
			indicatorPane.getChildren().clear();
			topIndicatorPane.getChildren().clear();
			VBox coinsBox = new VBox();
			VBox victoryBox = new VBox();
			VBox nobilityBox = new VBox();
			VBox assistantBox = new VBox();
			VBox emporiumBox = new VBox();
			coinsBox.setAlignment(Pos.BOTTOM_CENTER);
			victoryBox.setAlignment(Pos.BOTTOM_CENTER);
			nobilityBox.setAlignment(Pos.BOTTOM_CENTER);
			assistantBox.setAlignment(Pos.BOTTOM_CENTER);
			emporiumBox.setAlignment(Pos.BOTTOM_CENTER);
			Label coinsLabel = new Label("COINS");
			Label nobilityLabel = new Label("NOBILITY TRACK");
			Label victoryLabel = new Label("VICTORY POINTS");
			Label assistantsLabel = new Label("ASSISTANTS");
			Label emporiumLabel = new Label("EMPORIUM");

			coinsBox.getChildren().add(coinsIndicators);
			coinsBox.getChildren().add(coinsLabel);
			indicatorPane.add(coinsBox, 0, 1);

			victoryBox.getChildren().add(victoryPointsIndicators);
			victoryBox.getChildren().add(victoryLabel);
			indicatorPane.add(victoryBox, 1, 1);

			nobilityBox.getChildren().add(nobilityPointsIndicators);
			nobilityBox.getChildren().add(nobilityLabel);
			indicatorPane.add(nobilityBox, 2, 1);

			assistantBox.getChildren().add(this.numberOfAssistants);
			assistantBox.getChildren().add(assistantsLabel);
			topIndicatorPane.add(assistantBox, 0, 0);

			emporiumBox.getChildren().add(this.numberOfEmporium);
			emporiumBox.getChildren().add(emporiumLabel);
			topIndicatorPane.add(emporiumBox, 1, 0);

		});

	}

	public void repaintCouncils(Region[] regions, Council kingCouncil, GridPane councillors, GridPane kingCouncilPane) {

		Council council1 = regions[0].getCouncil();
		Council council2 = regions[1].getCouncil();
		Council council3 = regions[2].getCouncil();
		Council king = kingCouncil;

		Queue<Councillor> councillors1 = council1.getCouncillors();
		Queue<Councillor> councillors2 = council2.getCouncillors();
		Queue<Councillor> councillors3 = council3.getCouncillors();
		Queue<Councillor> kingCouncillors = king.getCouncillors();

		Platform.runLater(() -> {
			councillors.getChildren().clear();
			setCouncillors(councillors, councillors1, 1);
			setCouncillors(councillors, councillors2, 6);
			setCouncillors(councillors, councillors3, 11);
			kingCouncilPane.setPickOnBounds(true);
			setKingCouncillors(kingCouncilPane, kingCouncillors, 1);

		});
	}

	private void setKingCouncillors(GridPane kingCouncilPane, Queue<Councillor> kingCouncillors, int startCol) {
		int i = startCol;
		for (Councillor kingCouncillor : kingCouncillors) {
			Pane pane = new Pane();
			pane.getStylesheets().add(css);
			switch (kingCouncillor.getColor()) {
			case "PINK":
				pane.getStyleClass().add("kingPing");
				kingCouncilPane.add(pane, i, 1);
				break;
			case "PURPLE":
				pane.getStyleClass().add("kingPurple");
				kingCouncilPane.add(pane, i, 1);
				break;
			case "BLACK":
				pane.getStyleClass().add("kingBlack");
				kingCouncilPane.add(pane, i, 1);
				break;
			case "BLUE":
				pane.getStyleClass().add("kingBlue");
				kingCouncilPane.add(pane, i, 1);
				break;
			case "WHITE":
				pane.getStyleClass().add("kingWhite");
				kingCouncilPane.add(pane, i, 1);
				break;
			case "ORANGE":
				pane.getStyleClass().add("kingOrange");
				kingCouncilPane.add(pane, i, 1);
				break;
			}
			Pane councillorPane = (Pane) kingCouncilPane.getChildren().get(kingCouncilPane.getChildren().indexOf(pane));
			double x = councillorPane.getLayoutX();
			double y = councillorPane.getLayoutY();
			Path path = new Path();
			path.getElements().add(new MoveTo(x, y + 100));
			PathTransition pathTransition = new PathTransition();
			pathTransition.setDuration(Duration.millis(1000));
			pathTransition.setPath(path);
			pathTransition.setNode(councillorPane);
			pathTransition.setAutoReverse(true);
			pane.setOnMouseEntered(event -> {
				pathTransition.play();
			});
			i++;
		}
	}

	private void setCouncillors(GridPane councillors, Queue<Councillor> councillors1, int startCol) {

		int i = startCol;
		for (Councillor councillor : councillors1) {
			Pane pane = new Pane();
			switch (councillor.getColor()) {
			case "PINK":
				pane.getStyleClass().add("PinkCouncillor");
				councillors.add(pane, i, 0);
				break;
			case "PURPLE":
				pane.getStyleClass().add("PurpleCouncillor");
				councillors.add(pane, i, 0);
				break;
			case "BLACK":
				pane.getStyleClass().add("BlackCouncillor");
				councillors.add(pane, i, 0);
				break;
			case "BLUE":
				pane.getStyleClass().add("BlueCouncillor");
				councillors.add(pane, i, 0);
				break;
			case "WHITE":
				pane.getStyleClass().add("WhiteCouncillor");
				councillors.add(pane, i, 0);
				break;
			case "ORANGE":
				pane.getStyleClass().add("OrangeCouncillor");
				councillors.add(pane, i, 0);
				break;
			}
			i++;
			Pane councillorPane = (Pane) councillors.getChildren().get(councillors.getChildren().indexOf(pane));
			double x = councillorPane.getLayoutX();
			double y = councillorPane.getLayoutY();
			Path path = new Path();
			path.getElements().add(new MoveTo(x, y + 100));
			PathTransition pathTransition = new PathTransition();
			pathTransition.setDuration(Duration.millis(1000));
			pathTransition.setPath(path);
			pathTransition.setNode(councillorPane);
			pathTransition.setAutoReverse(true);
			pane.setOnMouseEntered(event -> {
				pathTransition.play();
			});
		}
	}

	public void repaintTile(GridPane permitTileSlot, Region[] regions) {
		Platform.runLater(() -> {
			permitTileSlot.getChildren().clear();
			ArrayList<PermitTile> allPermitTile = new ArrayList<PermitTile>();
			Region tempRegionCoast = regions[0];
			Region tempRegionHills = regions[1];
			Region tempRegionMountains = regions[2];
			PermitTile coastSlot1 = (PermitTile) tempRegionCoast.getDeck().getUnconveredPermitTile1();
			PermitTile coastSlot2 = (PermitTile) tempRegionCoast.getDeck().getUnconveredPermitTile2();
			PermitTile hillsSlot1 = (PermitTile) tempRegionHills.getDeck().getUnconveredPermitTile1();
			PermitTile hillsSlot2 = (PermitTile) tempRegionHills.getDeck().getUnconveredPermitTile2();
			PermitTile mountainsSlot1 = (PermitTile) tempRegionMountains.getDeck().getUnconveredPermitTile1();
			PermitTile mountainsSlot2 = (PermitTile) tempRegionMountains.getDeck().getUnconveredPermitTile2();
			allPermitTile.add(coastSlot1);
			allPermitTile.add(coastSlot2);
			allPermitTile.add(hillsSlot1);
			allPermitTile.add(hillsSlot2);
			allPermitTile.add(mountainsSlot1);
			allPermitTile.add(mountainsSlot2);

			css = LoaderResources.loadPath("/configurator/style.css");
			int idCard;
			List<City> cardCity;
			ArrayList<String> cardBonus;

			int colPosition = 0;

			for (int i = 0; i < allPermitTile.size(); i++) {
				idCard = allPermitTile.get(i).getId();
				cardCity = allPermitTile.get(i).getCities();
				cardBonus = allPermitTile.get(i).getBonus();
				Pane pane = new Pane();
				pane.getStylesheets().add(css);
				pane.getStyleClass().add("permitTile");
				Label id = new Label("ID: " + idCard);
				id.getStylesheets().add(css);
				id.getStyleClass().add("idSlot");

				permitTileSlot.add(pane, colPosition, 0);
				permitTileSlot.add(id, colPosition, 0);

				String city = "\nCity:";
				for (int k = 0; k < cardCity.size(); k++) {
					city += cardCity.get(k).getName().charAt(0) + ",";
				}
				Label cityName = new Label(city);
				cityName.getStylesheets().add(css);
				cityName.getStyleClass().add("cityPermitTileSlot");
				permitTileSlot.add(cityName, colPosition, 0);

				String bonus = "\n\nBonus:\n";
				for (int k = 0; k < cardBonus.size(); k++) {
					bonus += cardBonus.get(k) + "\n";
				}
				Label cityBonus = new Label(bonus);
				cityBonus.getStylesheets().add(css);
				cityBonus.getStyleClass().add("bonusSlot");
				permitTileSlot.add(cityBonus, colPosition, 0);
				colPosition += 2;
			}
		});
	}
}
