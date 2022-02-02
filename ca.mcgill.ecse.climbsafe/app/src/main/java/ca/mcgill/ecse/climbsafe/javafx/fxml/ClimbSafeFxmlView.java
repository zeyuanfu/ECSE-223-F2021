package ca.mcgill.ecse.climbsafe.javafx.fxml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import ca.mcgill.ecse.climbsafe.application.*;


public class ClimbSafeFxmlView extends Application {

  public static final EventType<Event> REFRESH_EVENT = new EventType<>("REFRESH");
  private static ClimbSafeFxmlView instance;
  private List<Node> refreshableNodes = new ArrayList<>();

  @Override
  public void start(Stage primaryStage) {
    instance = this;
    try {
      var root = (Pane) FXMLLoader.load(getClass().getResource("MainPage.fxml"));

      var scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.setMinWidth(800);
      primaryStage.setMinHeight(600);
      primaryStage.setTitle("ClimbSafe by Team 15");
      Image icon = new Image("file:src/mountainIcon.png");
      primaryStage.getIcons().add(icon);

      primaryStage.show();
      refresh();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Register the node for receiving refresh events
  public void registerRefreshEvent(Node node) {
    refreshableNodes.add(node);
  }

  // Register multiple nodes for receiving refresh events
  public void registerRefreshEvent(Node... nodes) {
    for (var node: nodes) {
      refreshableNodes.add(node);
    }
  }

  // remove the node from receiving refresh events
  public void removeRefreshableNode(Node node) {
    refreshableNodes.remove(node);
  }

  // fire the refresh event to all registered nodes
  public void refresh() {
    for (Node node : refreshableNodes) {
      node.fireEvent(new Event(REFRESH_EVENT));
    }
  }

  public static ClimbSafeFxmlView getInstance() {
    return instance;
  }

  public static void makeText(String toastMsg) {
		
		Stage primaryStage = null;
		
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
	    	
	    	Stage toastStage=new Stage();
	        toastStage.initOwner(primaryStage);
	        toastStage.setResizable(false);
	        toastStage.initStyle(StageStyle.TRANSPARENT);
	        
	        toastStage.setY(screenBounds.getMaxY()/1.45);
	        toastStage.setX(screenBounds.getMaxX()/2.5);

	        Text text = new Text(toastMsg);
	        text.setFont(Font.font("Verdana", 10));
	        text.setFill(Color.WHITE);

	        StackPane root = new StackPane(text);
	        root.setStyle("-fx-background-radius: 10; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 5px;");
	        root.setOpacity(0);

	        Scene scene = new Scene(root);
	        scene.setFill(Color.TRANSPARENT);
	        toastStage.setScene(scene);
	        toastStage.show();

	        Timeline fadeInTimeline = new Timeline();
	        double fadeInDelay = 500;
			KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 1)); 
	        fadeInTimeline.getKeyFrames().add(fadeInKey1);   
	        fadeInTimeline.setOnFinished((ae) -> 
	        {
	            new Thread(() -> {
	                try
	                {
	                    long toastDelay = 1500;
						Thread.sleep(toastDelay);
	                }
	                catch (InterruptedException e)
	                {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	                   Timeline fadeOutTimeline = new Timeline();
	                    double fadeOutDelay = 500;
						KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 0)); 
	                    fadeOutTimeline.getKeyFrames().add(fadeOutKey1);   
	                    fadeOutTimeline.setOnFinished((aeb) -> toastStage.close()); 
	                    fadeOutTimeline.play();
	            }).start();
	        }); 
	        fadeInTimeline.play();
	    }

}
