package view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import controller.ConsoleController;
import controller.ControllerFactory;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Critter;
import model.Tile;
import model.TileType;
import model.World;

public class Main extends Application {
	private ConsoleController controller = (ConsoleController) ControllerFactory.getConsoleController();
	private World world;
	private final static double w = 60;
	private final static double h = w * Math.sqrt(3.0);

	int count;
	boolean play_B = true;
	AnchorPane runSimulation;
	int actual_rate = 30;
	HBox mainArea;
	VBox entireUI;

	private final static Image critterIcon = new Image("view/CritterIcon.png");
	private final static Image fishIcon = new Image("view/FishIcon.png");
	private final static Image rockIcon0 = new Image("view/RockIcon0.png");
	private final static Image rockIcon1 = new Image("view/RockIcon1.png");
	private final static Image rockIcon2 = new Image("view/RockIcon2.png");
	private final static Image hourglassIcon = new Image("view/HourglassIcon.png");
	private final static Image globeIcon = new Image("view/GlobeIcon.png");
	private final static Image playButtonIcon = new Image("view/PlayButtonIcon.png");
	private final static Image diceIcon = new Image("view/DiceIcon.png");
	private HashMap<String, ArrayList<Double>> colors = new HashMap<String, ArrayList<Double>>();


	public static void main(String[] args) {
		Application.launch(args);
	}

	// /Users/andrew/eclipse-workspace/ajx8-ezj4-ic254-critterworld/src/test/resources/A5files
	@Override
	public void start(Stage primaryStage) throws Exception {
		controller.newWorld();
//		controller.loadWorld(
//				"/Users/andrew/eclipse-workspace/ajx8-ezj4-ic254-critterworld/src/test/resources/critter_action_test_files/forward_test.txt",
//				false, false);

		world = (World) controller.getReadOnlyWorld();
		VBox entireUI = initializeEntireUI(Color.GRAY, Color.ALICEBLUE, primaryStage);
		Scene scene = new Scene(entireUI);
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

	public void pressPlay() {
		double currenttime;
		double currenttime2;
		while (!play_B) {
			currenttime = System.nanoTime();
			if (actual_rate == 0) {
			} else if (actual_rate <= 30) {
				while (System.nanoTime() - currenttime < 1.0 / actual_rate * 1000000000) {
				}
				currenttime = System.nanoTime();

			} else {
				while (System.nanoTime() - currenttime < 1.0 / 30.0 * 1000000000) {
					currenttime = System.nanoTime();
					currenttime2 = System.nanoTime();
					while (System.nanoTime() - currenttime2 < 1.0 / actual_rate * 1000000000) {
					}
					currenttime2 = System.nanoTime();
					controller.advanceTime(1);
				}
				world = (World) controller.getReadOnlyWorld();
			}
		}
	}

	public void pressPause() {
		play_B = true;
	}



	private static AnchorPane formatButton(ImageView icon) {
		icon.setFitHeight(60);
		icon.setFitWidth(60);
		AnchorPane anchorPane = new AnchorPane();
		Button button = new Button();
		button.setGraphic(icon);
		anchorPane.setPrefSize(75, 75);
		anchorPane.getChildren().add(button);
		AnchorPane.setBottomAnchor(button, 15.0);
		AnchorPane.setTopAnchor(button, 15.0);
		AnchorPane.setRightAnchor(button, 15.0);
		AnchorPane.setLeftAnchor(button, 15.0);
		return anchorPane;
	}


	private File loadInFile(String title, Stage stage) {
		FileChooser fileChooser = null;
		File file = null;
		// ensures that a file must be chosen
		while (file == null) {
			fileChooser = new FileChooser();
			fileChooser.setTitle(title);
			file = fileChooser.showOpenDialog(stage);
			// if the file hasn't been chosen yet, file will be null, and a warning will be
			// printed
			if (file == null) {
				// asks if the user wants to try again, or if they really meant to exit
				boolean selectAnotherFile = WarningMessage
						.displayFileWarning("No file was selected, would you like to select another file?");
				if (!selectAnotherFile) {
					break;
				}
			}
		}
		return file;
	}

	private HBox initializetoolBar(Color toolBarColor, Stage stage) {
		BackgroundFill toolBarBackgroundFill = new BackgroundFill(toolBarColor, CornerRadii.EMPTY, Insets.EMPTY);
		Background toolBarBackground = new Background(toolBarBackgroundFill);
		HBox toolBar = new HBox();
		Region spacer1 = new Region();
		spacer1.setPrefWidth(400);

		// working
		AnchorPane newWorld = formatButton(new ImageView(diceIcon));
		Button newWorldButton = (Button) newWorld.getChildren().get(0);
		newWorldButton.setOnAction(e -> {
			controller.newWorld();
			world = (World) controller.getReadOnlyWorld();
			VBox entireUI = initializeEntireUI(Color.GRAY, Color.ALICEBLUE, stage);
			stage.setScene(new Scene(entireUI));
			stage.sizeToScene();
			stage.setMaximized(true);
			stage.show();
		});

		// working
		AnchorPane loadWorld = formatButton(new ImageView(globeIcon));
		Button loadWorldButton = (Button) loadWorld.getChildren().get(0);
		loadWorldButton.setOnAction(e -> {
			File file = loadInFile("Load in World", stage);
			// Open up world settings to allow manna and/or forced mutations
			if (file != null) {
				boolean[] settings = WorldSettings.display();
				String filename = file.getAbsolutePath();
				controller.loadWorld(filename, settings[0], settings[1]);
				world = (World) controller.getReadOnlyWorld();
				VBox entireUI = initializeEntireUI(Color.GRAY, Color.ALICEBLUE, stage);
				HBox mainArea = (HBox) entireUI.getChildren().get(1);
				ScrollPane scrollArea = (ScrollPane) mainArea.getChildren().get(0);
				HexMap hexMap = (HexMap) scrollArea.getContent();
				initializeCritterView(hexMap);
				stage.setScene(new Scene(entireUI));
				stage.sizeToScene();
				stage.setMaximized(true);
				stage.show();
			}
		});

		// working
		AnchorPane loadCritters = formatButton(new ImageView(critterIcon));
		Button loadCrittersButton = (Button) loadCritters.getChildren().get(0);
		loadCrittersButton.setOnAction(e -> {
			File file = loadInFile("Load in Critter", stage);
			// load in the critters and update the world
			if (file != null) {
				int[] values = CritterSettings.display();
				int placeRandomly = values[0];
				String filename = file.getAbsolutePath();
				if (placeRandomly == 1) {
					int count = values[1];
					controller.loadCritters(filename, count);
				} else {
					int c = values[1];
					int r = values[2];
					controller.loadCritterOnSpecificTile(filename, c, r);
				}
				world = (World) controller.getReadOnlyWorld();
				VBox entireUI = (VBox) stage.getScene().getRoot();
				HBox mainArea = (HBox) entireUI.getChildren().get(1);
				ScrollPane scrollArea = (ScrollPane) mainArea.getChildren().get(0);
				HexMap hexMap = (HexMap) scrollArea.getContent();
				initializeCritterView(hexMap);
			}
		});

		AnchorPane advanceTime = formatButton(new ImageView(hourglassIcon));
		Button advanceTimeButton = (Button) advanceTime.getChildren().get(0);
		advanceTimeButton.setOnAction(e -> {
			ArrayList<Critter> previousCritterStates = new ArrayList<Critter>();
			for (int i = 0; i < world.getNumberOfAliveCritters(); i++) {
				Critter currentCritter = world.getCritters().get(i);
				Critter crit = currentCritter.clone();
				crit.setNextState(currentCritter);
				previousCritterStates.add(crit);
			}
			controller.advanceTime(1);
			world = (World) controller.getReadOnlyWorld();
			VBox entireUI = (VBox) stage.getScene().getRoot();
			HBox mainArea = (HBox) entireUI.getChildren().get(1);
			ScrollPane scrollArea = (ScrollPane) mainArea.getChildren().get(0);
			HexMap hexMap = (HexMap) scrollArea.getContent();
			updateCritterView(hexMap, 1000, previousCritterStates);
			if (this.world.getEnableManna()) {
				updateFoodView(hexMap, 1000);
			}
		});

		AnchorPane runSimulation = formatButton(new ImageView(playButtonIcon));
		Button runSimulationButton = (Button) runSimulation.getChildren().get(0);
		
		runSimulationButton.setOnAction(e -> {
			Runnable a = new Runnable() {
				public void run() {
					double currenttime;
					double currenttime2;
					ArrayList<Critter> previousCritterStates = new ArrayList<Critter>();
					while (!play_B) {
						currenttime = System.currentTimeMillis();
						if (actual_rate == 0) {
						} else if (actual_rate <= 30) {
							while (System.currentTimeMillis() - currenttime < 1.0 / actual_rate * 1000) {
							}
							previousCritterStates = new ArrayList<Critter>();
							for (int i = 0; i < world.getNumberOfAliveCritters(); i++) {
								Critter currentCritter = world.getCritters().get(i);
								Critter crit = currentCritter.clone();
								crit.setNextState(currentCritter);
								previousCritterStates.add(crit);
							}
							controller.advanceTime(1);
							world = (World) controller.getReadOnlyWorld();
							VBox entireUI = (VBox) stage.getScene().getRoot();
							HBox mainArea = (HBox) entireUI.getChildren().get(1);
							ScrollPane scrollArea = (ScrollPane) mainArea.getChildren().get(0);
							HexMap hexMap = (HexMap) scrollArea.getContent();

							updateCritterView(hexMap, (int) (System.currentTimeMillis() - currenttime)/((int)((world.getNumberOfAliveCritters()+actual_rate)/5)+1), previousCritterStates);

							if (world.getEnableManna()) {
								updateFoodView(hexMap, (int) (System.currentTimeMillis() - currenttime));
							}
							currenttime = System.currentTimeMillis();

						} else {
							int counter = 0;
							currenttime2 = System.currentTimeMillis();
							while (counter <= (int) Math.round(actual_rate / 30.0)) {
								currenttime = System.currentTimeMillis();

								while (System.currentTimeMillis() - currenttime < 1.0 / actual_rate * 1000) {
								}
								previousCritterStates = new ArrayList<Critter>();
								for (int i = 0; i < world.getNumberOfAliveCritters(); i++) {
									Critter currentCritter = world.getCritters().get(i);
									Critter crit = currentCritter.clone();
									crit.setNextState(currentCritter);
									previousCritterStates.add(crit);
								}
								currenttime = System.currentTimeMillis();
								controller.advanceTime(1);
								counter++;
							}
							world = (World) controller.getReadOnlyWorld();
							VBox entireUI = (VBox) stage.getScene().getRoot();
							HBox mainArea = (HBox) entireUI.getChildren().get(1);
							ScrollPane scrollArea = (ScrollPane) mainArea.getChildren().get(0);
							HexMap hexMap = (HexMap) scrollArea.getContent();

							updateCritterView(hexMap, (int) (System.currentTimeMillis() - currenttime)/((int)((world.getNumberOfAliveCritters()+actual_rate)/5)+1), previousCritterStates);

							if (world.getEnableManna()) {
								updateFoodView(hexMap, (int) (System.currentTimeMillis() - currenttime2));
							}
							//System.out.println(world.getSteps());
						}
					}
				}
			};
			Runnable b = new Runnable() {
				public void run() {
					pressPause();
				}
			};

			Thread a1 = new Thread(a);
			Thread b1 = new Thread(b);

			if (play_B) {

				ImageView icon = new ImageView(new Image("view/pausebuttonicon.png"));
				icon.setFitHeight(60);
				icon.setFitWidth(60);
				runSimulationButton.setGraphic(icon);
				play_B = false;
				a1.start();

			} else {

				ImageView icon = new ImageView(new Image("view/PlayButtonIcon.png"));
				icon.setFitHeight(60);
				icon.setFitWidth(60);
				runSimulationButton.setGraphic(icon);
				b1.start();
			}
		});
		
	

		HBox simulationRateBox = new HBox();
		Text simulationRateLabel = new Text("Enter Simulation Rate: ");
		AnchorPane simulationRateWrapper = new AnchorPane();
		TextArea simulationRate = new TextArea();
		simulationRateWrapper.setPrefSize(150, 75);
		simulationRateWrapper.getChildren().add(simulationRate);
		AnchorPane.setBottomAnchor(simulationRate, 25.0);
		AnchorPane.setTopAnchor(simulationRate, 25.0);
		AnchorPane.setRightAnchor(simulationRate, 10.0);
		AnchorPane.setLeftAnchor(simulationRate, 10.0);
		Text simulationRateUnits = new Text("updates/second");

		AnchorPane setRate = formatButton(new ImageView(new Image("view/checkmarkicon.png")));
		Button setRateButton = (Button) setRate.getChildren().get(0);
		setRateButton.setOnAction(e -> {
			try {
				int rate = Integer.parseInt(simulationRate.getText());
				if (rate >= 0 && rate <= 100) {
					actual_rate = rate;
				}
				else {
					WarningMessage.display("Enter a positive integer between 0 and 100");
				}
			} catch (NumberFormatException e1) {
				WarningMessage.display("Enter a positive integer between 0 and 100");
			}
		});


		simulationRateBox.getChildren().add(simulationRateLabel);
		simulationRateBox.getChildren().add(simulationRateWrapper);
		simulationRateBox.getChildren().add(simulationRateUnits);

		toolBar.getChildren().add(newWorld);
		toolBar.getChildren().add(loadWorld);
		toolBar.getChildren().add(loadCritters);
		toolBar.getChildren().add(advanceTime);
		toolBar.getChildren().add(runSimulation);
		toolBar.getChildren().add(spacer1);
		toolBar.getChildren().add(setRate);
		toolBar.getChildren().add(simulationRateBox);
		toolBar.setBackground(toolBarBackground);
		return toolBar;
	}

	protected static AnchorPane formatWrapper(Node node, double top, double left, double bottom, double right) {
		AnchorPane wrapper = new AnchorPane();
		wrapper.getChildren().add(node);
		AnchorPane.setBottomAnchor(node, bottom);
		AnchorPane.setTopAnchor(node, top);
		AnchorPane.setRightAnchor(node, right);
		AnchorPane.setLeftAnchor(node, left);
		return wrapper;
	}

	private HBox formatLegendDescription(ImageView legendIcon, String name) {
		HBox legendDescription = new HBox();
		legendIcon.setFitHeight(80);
		legendIcon.setFitWidth(80);
		Text label = new Text(name);
		label.setFont(new Font("Verdana", 24));
		AnchorPane labelBox = formatWrapper(label, 25.0, 10.0, 20.0, 10.0);
		legendDescription.getChildren().add(legendIcon);
		legendDescription.getChildren().add(labelBox);

		return legendDescription;
	}

	private VBox initializeLegend() {
		VBox legend = new VBox();
		HBox rockLegend = formatLegendDescription(new ImageView(rockIcon0), "Rock");
		HBox foodLegend = formatLegendDescription(new ImageView(fishIcon), "Food");
		HBox critterLegend = formatLegendDescription(new ImageView(critterIcon), "Critter");
		legend.getChildren().add(rockLegend);
		legend.getChildren().add(foodLegend);
		legend.getChildren().add(critterLegend);
		return legend;
	}

	private VBox initializeSideBar(Color sideBarColor) {
		BackgroundFill sideBarBackgroundFill = new BackgroundFill(sideBarColor, CornerRadii.EMPTY, Insets.EMPTY);
		Background sideBarBackground = new Background(sideBarBackgroundFill);
		VBox sideBar = new VBox();
		VBox legend = initializeLegend();
		sideBar.getChildren().add(legend);
		sideBar.setBackground(sideBarBackground);
		return sideBar;
	}

	private HBox initializeMainArea(Color mainAreaBackgroundColor) {
		AnchorPane tileMap = new HexMap(this.world);
		ScrollPane map = new ScrollPane(tileMap);
		map.setMaxSize(1270, 770);
		map.setMinSize(1270, 770);
		BackgroundFill mainAreaBackgroundFill = new BackgroundFill(mainAreaBackgroundColor, CornerRadii.EMPTY,
				Insets.EMPTY);
		Background mainAreaBackground = new Background(mainAreaBackgroundFill);
		HBox mainArea = new HBox();
		VBox sideBar = initializeSideBar(Color.LIGHTGRAY);
		mainArea.getChildren().add(map);
		mainArea.getChildren().add(sideBar);
		mainArea.setBackground(mainAreaBackground);
		return mainArea;
	}

	protected class HexMap extends AnchorPane {
		HexMap(World world) {
			int columnCount = world.getDimensions()[0];
			int rowCount = world.getDimensions()[1];
			int tilesPerRow = columnCount / 2;
			int xStartOffset = 30;
			int yStartOffset = 30;
			if (columnCount % 2 == 0) {
				for (int i = 0; i < rowCount; i++) {
					if (i % 2 == 0) {
						for (int j = 0; j < tilesPerRow; j++) {
							Tile tile = world.getTileRectangular(i, j);
							double xCoord = j * 3 * w + (i % 2) + xStartOffset + 1.5 * w;
							double yCoord = i * h / 2.0 + yStartOffset;
							StackPane hex = hexCreator(xCoord, yCoord, tile);
							this.getChildren().add(hex);
						}
					} else {
						for (int j = 0; j < tilesPerRow; j++) {
							Tile tile = world.getTileRectangular(i, j);
							double xCoord = j * 3 * w + (i % 2) + xStartOffset;
							double yCoord = i * h / 2.0 + yStartOffset;
							StackPane hex = hexCreator(xCoord, yCoord, tile);
							this.getChildren().add(hex);
						}
					}
				}
			} else {
				for (int i = 0; i < rowCount; i++) {
					if (i % 2 == 0) {
						for (int j = 0; j < tilesPerRow + 1; j++) {
							Tile tile = world.getTileRectangular(i, j);
							double xCoord = j * 3 * w + (i % 2) + xStartOffset;
							double yCoord = i * h / 2.0 + yStartOffset;
							StackPane hex = hexCreator(xCoord, yCoord, tile);
							this.getChildren().add(hex);
						}
					} else {
						for (int j = 0; j < tilesPerRow; j++) {
							Tile tile = world.getTileRectangular(i, j);
							double xCoord = j * 3 * w + (i % 2) + xStartOffset + 1.5 * w;
							double yCoord = i * h / 2.0 + yStartOffset;
							StackPane hex = hexCreator(xCoord, yCoord, tile);
							this.getChildren().add(hex);
						}
					}
				}
			}
		}
	}

	private void updateFoodView(HexMap hexMap, int time) {
		int columnCount = this.world.getDimensions()[0];
		int rowCount = this.world.getDimensions()[1];
		for (int i = 0; i < this.world.getNewFoodCoordinates().size(); i++) {
			Integer[] coords = this.world.getNewFoodCoordinates().get(i);
			int x = coords[0];
			int y = coords[1];
			int index = indexFinderRectangular(hexMap, x, y, columnCount, rowCount);
			double xCoord = coordinateFinder(hexMap, columnCount, rowCount, index)[0];
			double yCoord = coordinateFinder(hexMap, columnCount, rowCount, index)[1];
			StackPane currentTile = (StackPane) hexMap.getChildren()
					.get(index);
			StackPane foodTile = hexCreator(xCoord, yCoord, new Tile(200));
			changeHexTile(currentTile, foodTile, hexMap, time);
		}
		this.world.clearNewFoodCoordinates();
	}

	private void updateEmptyView(HexMap hexMap, int time) {
		int columnCount = this.world.getDimensions()[0];
		int rowCount = this.world.getDimensions()[1];
		for (int i = 0; i < this.world.getNewEmptyCoordinates().size(); i++) {
			Integer[] coords = this.world.getNewEmptyCoordinates().get(i);
			int x = coords[0];
			int y = coords[1];
			int index = indexFinderRectangular(hexMap, x, y, columnCount, rowCount);
			double xCoord = coordinateFinder(hexMap, columnCount, rowCount, index)[0];
			double yCoord = coordinateFinder(hexMap, columnCount, rowCount, index)[1];
			StackPane currentTile = (StackPane) hexMap.getChildren().get(index);
			StackPane foodTile = hexCreator(xCoord, yCoord, new Tile(0));
			changeHexTile(currentTile, foodTile, hexMap, time);
		}
		this.world.clearNewEmptyCoordinates();
	}

	private int indexFinderRectangular(HexMap hexMap, int x, int y, int columnCount, int rowCount) {
		int index = 0;
		int tilesPerRow = columnCount / 2;
		if (columnCount % 2 == 0) {
			for (int i = 0; i < x; i++) {
				if (i % 2 == 0) {
					for (int j = 0; j < tilesPerRow; j++) {
						index++;
					}
				} else {
					for (int j = 0; j < tilesPerRow; j++) {
						index++;
					}
				}
			}
		} else {
			for (int i = 0; i < x; i++) {
				if (i % 2 == 0) {
					for (int j = 0; j < tilesPerRow + 1; j++) {
						index++;
					}
				} else {
					for (int j = 0; j < tilesPerRow; j++) {
						index++;
					}
				}
			}
		}
		index += y;
		return index;
	}

	private int indexFinder(HexMap hexMap, int c, int r, int columnCount, int rowCount) {
		int index = 0;
		int x = this.world.convertToRectangular(c, r)[0];
		int y = this.world.convertToRectangular(c, r)[1];
		int tilesPerRow = columnCount / 2;
		if (columnCount % 2 == 0) {
			for (int i = 0; i < x; i++) {
				if (i % 2 == 0) {
					for (int j = 0; j < tilesPerRow; j++) {
						index++;
					}
				} else {
					for (int j = 0; j < tilesPerRow; j++) {
						index++;
					}
				}
			}
		} else {
			for (int i = 0; i < x; i++) {
				if (i % 2 == 0) {
					for (int j = 0; j < tilesPerRow + 1; j++) {
						index++;
					}
				} else {
					for (int j = 0; j < tilesPerRow; j++) {
						index++;
					}
				}
			}
		}
		index += y;
		return index;
	}

	private double[] coordinateFinder(HexMap hexMap, int columnCount, int rowCount, int index) {
		return new double[] {
				((Hex) ((StackPane) hexMap.getChildren().get(index)).getChildren().get(0)).x,
				((Hex) ((StackPane) hexMap.getChildren().get(index)).getChildren().get(0)).y };
	}

	private void moveCritter(Critter critter, int time, boolean forward, Critter previousCritterState) {
		if (previousCritterState.getC() == critter.getC() && previousCritterState.getR() == critter.getR()) {
			return;
		}
		ImageView critterView = critter.getView();
		double x = critterView.getX() + critterView.getFitWidth() / 2;
		double y = critterView.getY() + critterView.getFitHeight() / 2;
		double newX = x;
		double newY = y;
		int direction = critter.getDirection();
		if (!forward) {
			direction += 3;
			direction %= 6;
		}

		switch (direction) {
		case 0:
			newY -= h;
			break;
		case 1:
			newX += 1.5 * w;
			newY -= h / 2;
			break;
		case 2:
			newX += 1.5 * w;
			newY += h / 2;
			break;
		case 3:
			newX = x;
			newY += h;
			break;
		case 4:
			newX -= 1.5 * w;
			newY += h / 2;
			break;
		case 5:
			newX -= 1.5 * w;
			newY -= h / 2;
			break;
		default:
			newX = 0;
			newY = 0;
			break;
		}
		Line path0 = new Line(x, y, newX, newY);
		PathTransition pathTransition0 = new PathTransition(Duration.millis(time), path0, critter.getView());
		pathTransition0.play();

		// set the position
		critterView.setX(newX - critterView.getFitWidth() / 2);
		critterView.setY(newY - critterView.getFitHeight() / 2);
	}
	
	private void growCritter(Critter critter, Critter previous, int time, int size) {
	if(!(previous.getMemory()[3] == critter.getMemory()[3])) {
	    if(size <10) {
	    	FadeTransition fadeOut = new FadeTransition(Duration.millis(time), critter.getView());
			fadeOut.setFromValue(1.0);
			fadeOut.setToValue(0.0);
			fadeOut.play();
	    	double x = critter.getView().getX();
	    	double y = critter.getView().getY();
	    	critter.getView().setFitHeight(55 + size*5);
	    	critter.getView().setFitWidth(55 + size*5);
	    	critter.getView().setX(x-2.5);
	    	critter.getView().setY(y-2.5);
		    FadeTransition fadeIn = new FadeTransition(Duration.millis(time), critter.getView());
		    fadeIn.setFromValue(0.0);
		    fadeIn.setToValue(1.0);
		    fadeIn.play();
	    }
	}
	}

	private void changeHexTile(StackPane currentTile, StackPane finalTile, HexMap hexMap, int time) {
		FadeTransition fadeOut = new FadeTransition(Duration.millis(time), currentTile);
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		fadeOut.play();
		Platform.runLater(
				  () -> {
				    hexMap.getChildren().add(finalTile);
				  }
				);
		FadeTransition fadeIn = new FadeTransition(Duration.millis(time), finalTile);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		fadeIn.play();

	}

	private void initializeCritterView(HexMap hexMap) {
		ArrayList<Critter> critterList = this.world.getCritters();
		DrawCritter d = new DrawCritter();
		int columnCount = this.world.getDimensions()[0];
		int rowCount = this.world.getDimensions()[1];
		for (int n = 0; n < critterList.size(); n++) {
			Critter critter = critterList.get(n);
			if (critter.getView() != null) {
				continue;
			}
			//ColorAdjust colorAdjust = new ColorAdjust();
			//colorAdjust.setHue(-0.5);
			ImageView critterView = d.draw22(critter,colors);
			//ImageView critterView = new ImageView(critterIcon);
			//critterView.setEffect(colorAdjust);
			ImageView critterInfoView = d.draw22(critter,colors);
			//ImageView critterInfoView = new ImageView(critterIcon);
			//critterInfoView.setEffect(colorAdjust);
			int c = critter.getC();
			int r = critter.getR();
			int index = indexFinder(hexMap, c, r, columnCount, rowCount);
			double xCoord = coordinateFinder(hexMap, columnCount, rowCount, index)[0];
			double yCoord = coordinateFinder(hexMap, columnCount, rowCount, index)[1];
			// set the position
			critterView.setX(xCoord + 60 - (critterView.getFitWidth())/2);
			critterView.setY(yCoord + 60-(critterView.getFitHeight()/2));
			//critterView.setX(xCoord + (critterView.getFitWidth() / 2));
			//critterView.setY(yCoord + (critterView.getFitHeight() / 2));
			critterView.setRotate(critter.getDirection() * 60.0);
			// displays critter information
			critterView.setOnMouseClicked(lambda -> CritterInfo.display(hexMap, critter, critterInfoView));
			critter.setView(critterView);
			Platform.runLater(
					  () -> {
					    hexMap.getChildren().add(critterView);
					  }
					);
			FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), critterView);
			fadeIn.setFromValue(0.0);
			fadeIn.setToValue(1.0);
			fadeIn.play();
		}
	}

	private void updateCritterView(HexMap hexMap, int time, ArrayList<Critter> previousCritterStates) {
		for (int n = 0; n < previousCritterStates.size(); n++) {
			Critter previousCritterState = previousCritterStates.get(n);
			Critter critter = previousCritterState.getNextState();
			if (critter.getMemPos(4) <= 0) {
				int columnCount = this.world.getDimensions()[0];
				int rowCount = this.world.getDimensions()[1];
				int c = previousCritterState.getC();
				int r = previousCritterState.getR();
				int size = critter.getMemPos(3);
				int index = indexFinder(hexMap, c, r, columnCount, rowCount);
				double x = coordinateFinder(hexMap, columnCount, rowCount, index)[0];
				double y = coordinateFinder(hexMap, columnCount, rowCount, index)[1];
				StackPane currentTile = (StackPane) hexMap.getChildren()
						.get(indexFinder(hexMap, c, r, columnCount, rowCount));
				StackPane foodTile = hexCreator(x, y, new Tile(size * 200));
				changeHexTile(currentTile, foodTile, hexMap, time);
				Platform.runLater(
						  () -> {
						    hexMap.getChildren().remove(critter.getView());
						  }
						);
				FadeTransition fadeOut = new FadeTransition(Duration.millis(time), critter.getView());
				fadeOut.setFromValue(1.0);
				fadeOut.setToValue(0.0);
				fadeOut.play();
			}
			try {
				switch (critter.getLastAction()) {
				case FORWARD:
					moveCritter(critter, time, true, previousCritterState);
					break;
				case BACKWARD:
					moveCritter(critter, time, false, previousCritterState);
					break;
				case LEFT:
					RotateTransition left = new RotateTransition(Duration.millis(time), critter.getView());
					left.setToAngle(critter.getView().getRotate() - 60);
					left.play();
					critter.getView().setRotate(critter.getView().getRotate() - 60);
					break;
				case RIGHT:
					RotateTransition right = new RotateTransition(Duration.millis(time), critter.getView());
					right.setToAngle(critter.getView().getRotate() + 60);
					right.play();
					critter.getView().setRotate(critter.getView().getRotate() + 60);
					break;
				case BUD:
					initializeCritterView(hexMap);
					break;
				case MATE:
					initializeCritterView(hexMap);
					break;
				case EAT:
					updateEmptyView(hexMap, time);
					break;
				case SERVE:
					updateFoodView(hexMap, time);
					break;
				case GROW:
					growCritter(critter, previousCritterState, time, critter.getMemory()[3]);
					break;
				default:
					break;
				}
			} catch (NullPointerException e) {

			}
		}
	}

	private StackPane hexCreator(double x, double y, Tile tile) {
		Stop[] stops;
		StackPane hexTile = new StackPane();
		ImageView file = null;
		TileType t = tile.getTileType();
		switch (t) {
		case ROCK: // gray
			Random r1 = new Random();
			int choice1 = r1.nextInt(3);
			if (choice1 == 0) {
				file = new ImageView(rockIcon0);
				stops = new Stop[] { new Stop(0, new Color(0.25, 0.25, 0.25, 1)),
						new Stop(1, new Color(0.5, 0.5, 0.5, 1)) };
			} else if (choice1 == 1) {
				file = new ImageView(rockIcon1);
				stops = new Stop[] { new Stop(0, new Color(0.25, 0.25, 0.25, 1)),
						new Stop(1, new Color(0.5, 0.5, 0.5, 1)) };
			} else {
				file = new ImageView(rockIcon2);
				stops = new Stop[] { new Stop(0, new Color(0.25, 0.25, 0.25, 1)),
						new Stop(1, new Color(0.5, 0.5, 0.5, 1)) };
			}
			break;
		case FOOD: // blue
			file = new ImageView(fishIcon);
			stops = new Stop[] { new Stop(0, new Color(0, 0.07, 0.27, 1)),
						new Stop(1, new Color(0, 0.25, 0.55, 1)) };
			break;
		default: // green
			stops = new Stop[] { new Stop(0, new Color(0.027, 0.18, 0.027, 1)),
					new Stop(1, new Color(0.15, 0.37, 0.15, 1)) };
			break;
		}
		try {
			file.setFitHeight(60);
			file.setFitWidth(60);
			Polygon hex = new Hex(x, y, stops);
			hexTile.getChildren().add(hex);
			hexTile.getChildren().add(file);
		} catch (NullPointerException e) {
			Polygon hex = new Hex(x, y, stops);
			hexTile.getChildren().add(hex);
		}

		hexTile.setLayoutX(x);
		hexTile.setLayoutY(y);
		return hexTile;
	}

	class Hex extends Polygon {
		double x;
		double y;

		Hex(double x, double y, Stop[] stops) {
			this.x = x;
			this.y = y;
			LinearGradient gradient = new LinearGradient(0, 1, 1, 1, true, CycleMethod.NO_CYCLE, stops);
			// creates the polygon using the corner coordinates
			getPoints().addAll(x - w / 2.0, y + h / 2.0, x + w / 2.0, y + h / 2.0, x + w, y, x + w / 2.0, y - h / 2.0,
					x - w / 2.0, y - h / 2.0, x - w, y);

			// set up the visuals and a click listener for the tile
			setFill(gradient);
			setStrokeWidth(1);
			setStroke(Color.WHITE);
		}
	}
	
	private VBox initializeEntireUI(Color toolBarBackgroundColor, Color mainAreaBackgroundColor, Stage stage) {
		VBox entireUI = new VBox();
		mainArea = initializeMainArea(mainAreaBackgroundColor);
		HBox toolBar = initializetoolBar(toolBarBackgroundColor, stage);
		entireUI.getChildren().add(toolBar);
		entireUI.getChildren().add(mainArea);
		return entireUI;
	}
}