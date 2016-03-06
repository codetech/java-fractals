
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application
{	
	public static GraphicsContext g;
	public static PythagorasTree tree;
	
	@Override
	public void start (Stage stage) throws Exception
	{
		FXMLLoader fxmlLoader = new FXMLLoader (getClass ().getResource ("Controls.fxml"));
		Parent controlRoot = fxmlLoader.load ();
		ControlsController controller = fxmlLoader.<ControlsController>getController ();
		controller.setMain (this);
		Stage controlStage = new Stage ();
		controlStage.setTitle ("Controls");
		controlStage.setScene (new Scene (controlRoot));
		
		Pane root = new Pane ();
		Canvas canvas = new Canvas (800, 600);
		g = canvas.getGraphicsContext2D ();
			
		root.getChildren ().add (canvas);
		
		tree = new PythagorasTree (g);
		controller.bindToPythagorasTree (tree);
		
		stage.setWidth (800);
		stage.setHeight (600);
		stage.setResizable (false);
		stage.setTitle ("Pythagoras Tree");
		stage.setScene (new Scene (root,800, 600));
		
		tree.setX ((int) (stage.getWidth() / 2));
		tree.setY ((int) (stage.getHeight() - 100));
		
		stage.show ();
		controlStage.show ();
	}

	public static void main (String[] args)
	{
		launch (args);
	}

	public void drawTree ()
	{
		tree.drawTree();
	}

	public void clear ()
	{
		tree.getThreadPool ().shutdownNow ();
		tree.setThreadPool (Executors.newCachedThreadPool());
		g.clearRect (0, 0, g.getCanvas ().getWidth (), g.getCanvas ().getHeight ());
	}
}