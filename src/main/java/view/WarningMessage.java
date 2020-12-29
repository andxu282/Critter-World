package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WarningMessage extends Main {
	static boolean selectAnotherFile = true;

	public static void display(String message) {
		Stage warning = new Stage();
		warning.initModality(Modality.APPLICATION_MODAL);
		VBox warningBox = new VBox();
		Text warningText = new Text(message);
		warningText.setFont(new Font("Verdana", 14));
		AnchorPane warningTextWrapper = formatWrapper(warningText, 5.0, 5.0, 5.0, 5.0);
		Button ok = new Button("Okay");
		AnchorPane okWrapper = formatWrapper(ok, 5.0, 5.0, 5.0, 5.0);
		okWrapper.setPrefSize(100.0, 50.0);

		ok.setOnAction(closeWarning -> {
			warning.close();
		});
		warningBox.getChildren().addAll(warningTextWrapper, okWrapper);

		Scene warningScene = new Scene(warningBox);
		warning.setScene(warningScene);
		warning.showAndWait();
	}

	public static boolean displayFileWarning(String message) {
		Stage warning = new Stage();
		warning.initModality(Modality.APPLICATION_MODAL);
		VBox warningBox = new VBox();
		Text warningText = new Text(message);
		warningText.setFont(new Font("Verdana", 14));
		AnchorPane warningTextWrapper = formatWrapper(warningText, 5.0, 5.0, 5.0, 5.0);

		HBox buttons = new HBox();
		Button no = new Button("No");
		AnchorPane noWrapper = formatWrapper(no, 5.0, 5.0, 5.0, 5.0);
		noWrapper.setPrefSize(100.0, 50.0);
		no.setOnAction(closeWarning -> {
			selectAnotherFile = false;
			warning.close();
		});
		Button yes = new Button("Yes");
		AnchorPane yesWrapper = formatWrapper(yes, 5.0, 5.0, 5.0, 5.0);
		yesWrapper.setPrefSize(100.0, 50.0);
		yes.setOnAction(closeWarning -> {
			selectAnotherFile = true;
			warning.close();
		});

		buttons.getChildren().addAll(yesWrapper, noWrapper);
		warningBox.getChildren().addAll(warningTextWrapper, buttons);
		Scene warningScene = new Scene(warningBox);
		warning.setScene(warningScene);
		warning.showAndWait();
		return selectAnotherFile;
	}
}
