package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CritterSettings extends Main {
	static int count = 1;
	static String addChoiceString = "Place any number of critters randomly";
	static int c;
	static int r;
	static int index0 = 0;
	static int index1 = 0;
	static int index2 = 0;

	public static int[] display() {

		Stage critterSettings = new Stage();
		critterSettings.initModality(Modality.APPLICATION_MODAL);
		VBox critterSettingsBox = new VBox();
		ChoiceBox<String> addChoice = new ChoiceBox<>();
		addChoice.getItems().add("Place any number of critters randomly");
		addChoice.getItems().add("Place one critter at a certain tile");
		addChoice.setValue("Place any number of critters randomly");
		addChoice.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
			addChoiceString = newValue;
		});
		AnchorPane addChoiceWrapper = formatWrapper(addChoice, 5.0, 5.0, 5.0, 5.0);
		Button submitChoice = new Button("Submit");
		submitChoice.setOnAction(e -> {
			if (addChoiceString.equals("Place any number of critters randomly")) {
				Stage addChoiceStage = new Stage();
				HBox hbox = new HBox();
				addChoiceStage.initModality(Modality.APPLICATION_MODAL);
				Text label = new Text("Number of Critters: ");
				label.setFont(new Font("Verdana", 14));
				AnchorPane labelWrapper = formatWrapper(label, 5.0, 5.0, 5.0, 5.0);

				TextArea numCritters = new TextArea();
				numCritters.setPrefSize(100.0, 30.0);
				AnchorPane numCrittersWrapper = formatWrapper(numCritters, 5.0, 5.0, 5.0, 5.0);
				numCritters.setOnKeyPressed(event -> {
					if (event.getCode() == KeyCode.ENTER) {
						try {
							count = Integer
									.parseInt(numCritters.getText().substring(0, numCritters.getText().length() - 1));
							if (count <= 0) {
								throw new NumberFormatException();
							}
						} catch (NumberFormatException exception) {
							WarningMessage
									.display("Warning: Number was not formatted correctly, using default of 1 critter");
							count = 1;
						}
						addChoice.setValue("Place any number of critters randomly");
						addChoiceStage.close();
						critterSettings.close();
					}
				});
				hbox.getChildren().add(labelWrapper);
				hbox.getChildren().add(numCrittersWrapper);

				Scene addChoiceScene = new Scene(hbox);
				addChoiceStage.setScene(addChoiceScene);
				addChoiceStage.showAndWait();
				index0 = 1;
				index1 = count;
			} else {
				Stage addChoiceStage = new Stage();
				HBox hbox = new HBox();
				Text columnLabel = new Text("c: ");
				columnLabel.setFont(new Font("Verdana", 14));
				AnchorPane columnLabelWrapper = formatWrapper(columnLabel, 5.0, 5.0, 5.0, 5.0);
				TextArea column = new TextArea();
				column.setPrefSize(100.0, 30.0);
				AnchorPane columnWrapper = formatWrapper(column, 5.0, 5.0, 5.0, 5.0);

				Text rowLabel = new Text("r: ");
				rowLabel.setFont(new Font("Verdana", 14));
				AnchorPane rowLabelWrapper = formatWrapper(rowLabel, 5.0, 5.0, 5.0, 5.0);
				TextArea row = new TextArea();
				row.setPrefSize(100.0, 30.0);
				AnchorPane rowWrapper = formatWrapper(row, 5.0, 5.0, 5.0, 5.0);

				column.setOnKeyPressed(event -> {
					if (event.getCode() == KeyCode.ENTER) {
						try {
							c = Integer.parseInt(column.getText().substring(0, column.getText().length() - 1));
							if (c < 0) {
								throw new NumberFormatException();
							}
							r = Integer.parseInt(row.getText().substring(0, row.getText().length()));
							if (r < 0) {
								throw new NumberFormatException();
							}
						} catch (NumberFormatException exception) {
							WarningMessage.display(
									"Warning: Number was not formatted correctly, using default of index of 0 for c and r.");
							c = 0;
							r = 0;
						}
						addChoice.setValue("Place any number of critters randomly");
						addChoiceStage.close();
						critterSettings.close();
					}
				});


				row.setOnKeyPressed(event -> {
					if (event.getCode() == KeyCode.ENTER) {
						try {
							c = Integer.parseInt(column.getText().substring(0, column.getText().length()));
							if (c < 0) {
								throw new NumberFormatException();
							}

							r = Integer.parseInt(row.getText().substring(0, row.getText().length() - 1));
							if (r < 0) {
								throw new NumberFormatException();
							}
						} catch (NumberFormatException exception) {
							WarningMessage.display(
									"Warning: Number was not formatted correctly, using default of index of 0 for c and r.");
							c = 0;
							r = 0;
						}
						addChoice.setValue("Place any number of critters randomly");
						addChoiceStage.close();
						critterSettings.close();
					}
				});

				hbox.getChildren().add(columnLabelWrapper);
				hbox.getChildren().add(columnWrapper);
				hbox.getChildren().add(rowLabelWrapper);
				hbox.getChildren().add(rowWrapper);

				Scene addChoiceScene = new Scene(hbox);
				addChoiceStage.setScene(addChoiceScene);
				addChoiceStage.showAndWait();
				index0 = 0;
				index1 = c;
				index2 = r;
			}
		});
		AnchorPane submitChoiceWrapper = formatWrapper(submitChoice, 5.0, 5.0, 5.0, 5.0);
		critterSettingsBox.getChildren().add(addChoiceWrapper);
		critterSettingsBox.getChildren().add(submitChoiceWrapper);

		Scene critterSettingsScene = new Scene(critterSettingsBox);
		critterSettings.setScene(critterSettingsScene);
		critterSettings.showAndWait();

		return new int[] { index0, index1, index2 };
	}
}
