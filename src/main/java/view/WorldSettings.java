package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WorldSettings extends Main {
	static boolean enableManna;
	static boolean enableForcedMutation;

	public static boolean[] display() {
		Stage worldSettings = new Stage();
		worldSettings.initModality(Modality.APPLICATION_MODAL);

		VBox worldSettingsVbox = new VBox();
		CheckBox enableMannaCheckBox = new CheckBox("Enable Manna (Possible random food drops after each turn)");
		AnchorPane mannaCheckBoxWrapper = formatWrapper(enableMannaCheckBox, 5.0, 5.0, 5.0, 5.0);
		CheckBox enableForcedMutationCheckBox = new CheckBox(
				"Enable Forced Mutation (Critters always mutate after each turn)");
		AnchorPane mutationCheckBoxWrapper = formatWrapper(enableForcedMutationCheckBox, 5.0, 5.0, 5.0, 5.0);
		Button submitSettings = new Button("Confirm World Settings");
		AnchorPane buttonWrapper = formatWrapper(submitSettings, 5.0, 5.0, 5.0, 5.0);

		worldSettingsVbox.getChildren().add(mannaCheckBoxWrapper);
		worldSettingsVbox.getChildren().add(mutationCheckBoxWrapper);
		worldSettingsVbox.getChildren().add(buttonWrapper);


		submitSettings.setOnAction(e -> {
			enableManna = enableMannaCheckBox.isSelected();
			enableForcedMutation = enableForcedMutationCheckBox.isSelected();
			worldSettings.close();
		});
		Scene worldSettingsScene = new Scene(worldSettingsVbox);
		worldSettings.setScene(worldSettingsScene);
		worldSettings.showAndWait();
		return new boolean[] { enableManna, enableForcedMutation };
	}
}
