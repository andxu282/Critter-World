package view;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import model.Critter;


public class DrawCritter {
		
		public Canvas draw(Critter c, double w, int maxSize, HashMap<Integer, ArrayList<Double>> colors) {
			double maxRadius = 1.3*w;
			double rotation = 38.5;
			int orientation = c.getDirection();
			int scaleDown = (int)(maxSize/c.getMemory()[3]);
			double radius = maxRadius - (5* scaleDown);
			if(radius < 10) {
				radius = 10;
			}	
			Canvas canvas = new Canvas(radius, radius);
			GraphicsContext gc = canvas.getGraphicsContext2D();
			gc.setFill(javafx.scene.paint.Color.BLACK);
			gc.fillOval(0, 0, radius, radius);
			if(!colors.containsKey(c.getMemory()[6])) {
				double r = Math.random();
				double g = Math.random();
				double b = Math.random();
				ArrayList<Double> scheme = new ArrayList<Double>();
				scheme.add(r);
				scheme.add(g);
				scheme.add(b);
				colors.put(c.getMemory()[6], scheme);
				gc.setFill(javafx.scene.paint.Color.color(r, g, b));
			}
			else {
				ArrayList<Double> scheme = colors.get(c.getMemory()[6]);
				gc.setFill(javafx.scene.paint.Color.color(scheme.get(0), scheme.get(1), scheme.get(2)));			
			}
			gc.fillOval(0, 0, radius/2, radius/2);
			canvas.setRotate(rotation+(orientation*60));
			return canvas;	
		}
		
		public ImageView draw22(Critter c, HashMap<String, ArrayList<Double>> colors) {
		
			ImageView i = new ImageView("view/CritterIcon.png");		
			Color hue = new Color(0, 0, 0, 0);
			Light.Distant color = new Light.Distant(45,45,hue);
			if(!colors.containsKey(c.getSpecies())) {
				double r = Math.random();
				double g = Math.random();
				double b = Math.random();
				ArrayList<Double> scheme = new ArrayList<Double>();
				scheme.add(r);
				scheme.add(g);
				scheme.add(b);
				colors.put(c.getSpecies(), scheme);
				color.setColor(Color.color(r,g,b));
			}
			else {
				ArrayList<Double> scheme = colors.get(c.getSpecies());
				color.setColor((Color.color(scheme.get(0), scheme.get(1), scheme.get(2))));
			}
			Lighting lighting = new Lighting();
			lighting.setDiffuseConstant(1.0);
		    lighting.setSpecularConstant(0.0);
		    lighting.setSpecularExponent(0.0);
		    lighting.setSurfaceScale(0.0);
		    lighting.setLight(color);
		    i.setEffect(lighting);
		    int size = c.getMemory()[3];
		    if(size <10) {
		    	i.setFitHeight(55 + size*5);
		    	i.setFitWidth(55 + size*5);
		    }
		    else {
		    	i.setFitHeight(100);
		    	i.setFitWidth(100);
		    }
		    
		    return i;	
		}
	
	}

