package client.view.gui.configurator;

import client.view.gui.LoaderResources;
import client.view.gui.MatchCityPane;
import controller.Player;
import javafx.application.Platform;
import javafx.scene.AccessibleRole;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import model.*;
import model.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.swing.text.Segment;

/**
 * Created by pietro on 05/07/16.
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

	private SimpleMetroArcGauge coinsIndicators;



	public Painter(StackPane stackPane, GridPane region1, GridPane region2, GridPane region3, Pane linesPane,
			CitiesListener citiesListener) {


		victoryPointsIndicators=new SimpleMetroArcGauge();
		nobilityPointsIndicators=new SimpleMetroArcGauge();
		coinsIndicators=new SimpleMetroArcGauge();

		victoryPointsIndicators.setMaxValue(100);
		victoryPointsIndicators.setMinValue(0);

		nobilityPointsIndicators.setMaxValue(20);
		nobilityPointsIndicators.setMinValue(0);
		coinsIndicators.setMaxValue(20);
		coinsIndicators.setMinValue(0);
		
		
		
		



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
		});
		matchCitiesPanes.clear();
		links.clear();

		regionRunLaters = new ArrayList<RegionRunLater>();
		List<City> regionOne = regions[0].getCities();
		List<City> regionTwo = regions[1].getCities();
		List<City> regionThree = regions[2].getCities();
		fillGrid(regionOne, region1);
		fillGrid(regionTwo, region2);
		fillGrid(regionThree, region3);

		Platform.runLater(() -> {
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
		});

		addLinks(regionOne);
		addLinks(regionTwo);
		addLinks(regionThree);

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

					citiesListener.setFirstLink(matchFirstCityPane.getPane(), city);
					citiesListener.setAutomaticSecondLink(matchSecondCityPane.getPane(), connectedCity);
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
		regionRunLaters.add(new RegionRunLater(pane, colIndex, rowIndex, region));
		matchCitiesPanes.add(new MatchCityPane(city, pane));
	}

	public void createLine(Pane firstLink, Pane secondLink, City city1, City city2) {
		double x1, y1, x2, y2;
		Parent parentFirstLink = firstLink.getParent();
		Parent parentSecondLink = secondLink.getParent();




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
			Line line = new Line(firstLink.getLayoutX() + x1, firstLink.getLayoutY() + y1, secondLink.getLayoutX() + x2,
					secondLink.getLayoutY() + y2);
			line.setStrokeWidth(10);
			links.add(line);
			linksBetweenCities.add(new SingleLink(city1, city2, line));
			line.setOnMouseClicked(event -> {
				citiesListener.removeLink(linesPane, line);
			});
			line.getStyleClass().add("line");
			linesPane.getChildren().add(line);
		});
	}

	public List<SingleLink> getLinksBetweenCities() {
		return this.linksBetweenCities;
	}

	public void repaintPlayerStatus(Player player, GridPane indicatorPane) {
		int coins=player.getCoins();
		int nobilityTrack=player.getPositionInNobilityTrack();
		int victoryPoints=player.getVictoryPoints();


		coinsIndicators.setValue(coins);
		nobilityPointsIndicators.setValue(nobilityTrack);
		victoryPointsIndicators.setValue(victoryPoints);

		Platform.runLater(()->{
			indicatorPane.add(coinsIndicators,0,1);
			indicatorPane.add(nobilityPointsIndicators,1,1);
			indicatorPane.add(victoryPointsIndicators,2,1);
		});



	}

	public void repaintCouncils(Region[] regions, Council kingCouncil,  GridPane councillors,GridPane kingCouncilPane) {

		Council council1=regions[0].getCouncil();
		Council council2=regions[1].getCouncil();
		Council council3=regions[2].getCouncil();
		Council king=kingCouncil;



		Queue<Councillor> councillors1=council1.getCouncillors();
		Queue<Councillor> councillors2=council2.getCouncillors();
		Queue<Councillor> councillors3=council3.getCouncillors();
		Queue<Councillor> kingCouncillors=king.getCouncillors();

		Platform.runLater(()->{
			councillors.getChildren().clear();
			setCouncillors(councillors,councillors1,1);
			setCouncillors(councillors,councillors2,6);
			setCouncillors(councillors,councillors3,11);
			setKingCouncillors(kingCouncilPane,kingCouncillors,1);

		});
	}

	private void setKingCouncillors(GridPane kingCouncilPane, Queue<Councillor> kingCouncillors, int startCol) {
		int i=startCol;
		for(Councillor kingCouncillor:kingCouncillors){
			Pane pane = new Pane();
			pane.getStylesheets().add(css);
			switch (kingCouncillor.getColor()){
				case "PINK":
					pane.getStyleClass().add("kingPing");
					kingCouncilPane.add(pane,i,1);
					break;
				case "PURPLE":
					pane.getStyleClass().add("kingPurple");
					kingCouncilPane.add(pane,i,1);
					break;
				case "BLACK":
					pane.getStyleClass().add("kingBlack");
					kingCouncilPane.add(pane,i,1);
					break;
				case "BLUE":
					pane.getStyleClass().add("kingBlue");
					kingCouncilPane.add(pane,i,1);
					break;
				case "WHITE":
					pane.getStyleClass().add("kingWhite");
					kingCouncilPane.add(pane,i,1);
					break;
				case "ORANGE":
					pane.getStyleClass().add("kingOrange");
					kingCouncilPane.add(pane,i,1);
					break;
			}
			i++;
		}
	}

	private void setCouncillors(GridPane councillors, Queue<Councillor> councillors1, int startCol) {

		int i=startCol;
		for(Councillor councillor:councillors1){
			Pane pane = new Pane();
			switch (councillor.getColor()){
				case "PINK":
					pane.getStyleClass().add("PinkCouncillor");
					councillors.add(pane,i,0);
					break;
				case "PURPLE":
					pane.getStyleClass().add("PurpleCouncillor");
					councillors.add(pane,i,0);
					break;
				case "BLACK":
					pane.getStyleClass().add("BlackCouncillor");
					councillors.add(pane,i,0);
					break;
				case "BLUE":
					pane.getStyleClass().add("BlueCouncillor");
					councillors.add(pane,i,0);
					break;
				case "WHITE":
					pane.getStyleClass().add("WhiteCouncillor");
					councillors.add(pane,i,0);
					break;
				case "ORANGE":
					pane.getStyleClass().add("OrangeCouncillor");
					councillors.add(pane,i,0);
					break;
			}
			i++;
		}
	}
}
