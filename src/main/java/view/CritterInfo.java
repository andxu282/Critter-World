package view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Critter;

public class CritterInfo extends Main {
	public static void display(HexMap hexMap, Critter critter, ImageView icon) {
		ImageView critterView = critter.getView();
		ImageView glowingCritterView = new ImageView(critterView.getImage());
		glowingCritterView.setX(critterView.getX());
		glowingCritterView.setY(critterView.getY());
		glowingCritterView.setFitHeight(critterView.getFitHeight());
		glowingCritterView.setFitWidth(critterView.getFitWidth());
		glowingCritterView.setRotate(critterView.getRotate());

		Color critterHue = ((Lighting) critterView.getEffect()).getLight().getColor();
		Lighting glowingCritterEffect = new Lighting();
		Light.Distant l = new Light.Distant();
		l.setColor(critterHue);
		glowingCritterEffect.setLight(l);
		
		//glowingCritterEffect.set(critterHue);
		//glowingCritterEffect.setInput(new Glow(0.7));
		glowingCritterView.setEffect(glowingCritterEffect);
		Timeline timeline = new Timeline();

		KeyValue transparent = new KeyValue(glowingCritterView.opacityProperty(), 0.0);
		KeyValue opaque = new KeyValue(glowingCritterView.opacityProperty(), 1.0);

		KeyFrame startFadeIn = new KeyFrame(Duration.ZERO, transparent);
		KeyFrame endFadeIn = new KeyFrame(Duration.millis(900), opaque);
		KeyFrame startFadeOut = new KeyFrame(Duration.millis(1100), opaque);
		KeyFrame endFadeOut = new KeyFrame(Duration.millis(2000), transparent);

		timeline.getKeyFrames().addAll(startFadeIn, endFadeIn, startFadeOut, endFadeOut);

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		hexMap.getChildren().add(glowingCritterView);

		Stage critterInfo = new Stage();
		critterInfo.initModality(Modality.APPLICATION_MODAL);
		VBox critterInfoBox = new VBox();
		Text species = new Text(critter.getSpecies());
		species.setFont(new Font("Verdana", 20));
		AnchorPane speciesWrapper = formatWrapper(species, 5.0, 5.0, 5.0, 5.0);
		critterInfoBox.getChildren().add(speciesWrapper);
		critterInfoBox.getChildren().add(icon);
		Text critterMem = new Text("Memory Length: " + critter.getMemPos(0) + "\nDefense: " + critter.getMemPos(1)
				+ "\nOffense: " + critter.getMemPos(2) + "\nSize: " + critter.getMemPos(3) + "\nEnergy: "
				+ critter.getMemPos(4) + "\nPass: " + critter.getMemPos(5) + "\nPosture: " + critter.getMemPos(6)
				+ "\nProgram: " + critter.getProgramString() + "\nLast Executed Rule: " + critter.getLastRuleString());
		critterMem.setFont(new Font("Verdana", 14));
		AnchorPane critterMemWrapper = formatWrapper(critterMem, 5.0, 5.0, 5.0, 5.0);
		critterInfoBox.getChildren().add(critterMemWrapper);

		Scene critterInfoScene = new Scene(critterInfoBox);
		critterInfo.setScene(critterInfoScene);
		critterInfo.show();
		critterInfo.setOnHiding(e -> hexMap.getChildren().remove(glowingCritterView));
	}
}
