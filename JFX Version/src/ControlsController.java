import static java.lang.Math.random;

import java.util.function.UnaryOperator;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class ControlsController
{
	private Main main;
	@FXML private ToggleGroup toggleGroup = new ToggleGroup ();

	@FXML private TextField	iterations;
	@FXML private TextField	x, y;
	@FXML private Slider	angle;
	@FXML private TextField	halfAngle;
	@FXML private Slider	angleBias;
	@FXML private TextField	size, sizeMultiplier;
	@FXML private RadioButton blackColor, randomColor, customColor;
	@FXML private ColorPicker colorPicker;
	@FXML private CheckBox drawBackward;
	@FXML private TextField delay;

	@FXML
	private void initialize ()
	{
		blackColor.setToggleGroup (toggleGroup);
		randomColor.setToggleGroup (toggleGroup);
		customColor.setToggleGroup (toggleGroup);

		StringConverter<Double> angleConverter = new StringConverter<Double> ()
		{
			public Double fromString (String string)
			{
				return Double.parseDouble (string.substring (0,
						string.contains ("°") ? string.length () - 1 : string.length ()));
			}

			public String toString (Double object)
			{
				if (object == null)
				{
					return "0.0°";
				}
				return object.toString () + "°";
			}
		};

		StringConverter<Double> doubleConverter = new DoubleStringConverter ();

		UnaryOperator<TextFormatter.Change> doubleFilter = (change) ->
		{
			String text = change.getText ();
			for (int i = 0; i < text.length (); i++)
			{
				if (!Character.isDigit (text.charAt (i)) && !(text.charAt (i) == '.'))
				{
					return null;
				}
			}
			return change;
		};

		StringConverter<Integer> intConverter = new IntegerStringConverter ();

		UnaryOperator<TextFormatter.Change> intFilter = (change ->
		{
			String text = change.getText ();
			for (int i = 0; i < text.length (); i++)
			{
				if (!Character.isDigit (text.charAt (i)))
				{
					return null;
				}
			}
			return change;
		});
		
		iterations.setTextFormatter (new TextFormatter<Integer> (intConverter, new Integer (5), intFilter));
		x.setTextFormatter (new TextFormatter<Integer> (intConverter, null, intFilter));
		y.setTextFormatter (new TextFormatter<Integer> (intConverter, null, intFilter));
		halfAngle.setTextFormatter (new TextFormatter<Double> (angleConverter, new Double (20), doubleFilter));
		size.setTextFormatter (new TextFormatter<Double> (doubleConverter, new Double (100), doubleFilter));
		sizeMultiplier.setTextFormatter (new TextFormatter<Double> (doubleConverter, new Double (0.75), doubleFilter));
		delay.setTextFormatter (new TextFormatter<Integer> (intConverter, new Integer (0), intFilter));
	}

	@SuppressWarnings("unchecked")
	public void bindToPythagorasTree (PythagorasTree tree)
	{
		tree.iterationsProperty ().bindBidirectional ((ObjectProperty<Integer>) iterations.getTextFormatter ().valueProperty ());
		tree.xProperty ().bindBidirectional ((ObjectProperty<Integer>) x.getTextFormatter ().valueProperty ());
		tree.yProperty ().bindBidirectional ((ObjectProperty<Integer>) y.getTextFormatter ().valueProperty ());
		tree.initAngleProperty ().bindBidirectional (angle.valueProperty ());
		tree.halfAngleProperty ().bindBidirectional ((ObjectProperty<Double>) halfAngle.getTextFormatter ().valueProperty ());
		tree.angleBiasProperty ().bindBidirectional (angleBias.valueProperty ());
		tree.sizeProperty ().bindBidirectional ((ObjectProperty<Double>) size.getTextFormatter ().valueProperty ());
		tree.sizeMultiplierProperty ().bindBidirectional ((ObjectProperty<Double>) sizeMultiplier.getTextFormatter ().valueProperty ());
		tree.drawBackwardsProperty ().bindBidirectional (drawBackward.selectedProperty ());
		tree.delayProperty ().bindBidirectional ((ObjectProperty<Integer>) delay.getTextFormatter ().valueProperty ());
		
		toggleGroup.selectedToggleProperty ().addListener (new ChangeListener<Toggle> ()
		{

			@Override
			public void changed (ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue)
			{
				if(newValue == blackColor)
				{
					tree.setIterationsToColor ((i) -> Color.BLACK);
				}
				else if (newValue == randomColor)
				{
					tree.setIterationsToColor ( (i) -> Color.color (random (), random (), random ()));
				}
				else if(newValue == customColor)
				{
					tree.setIterationsToColor ((i) -> colorPicker.getValue ());
				}

			}
		});
		
		colorPicker.setOnShowing ((e) -> toggleGroup.selectToggle (customColor));
	}

	@FXML
	private void drawPythagorasTree ()
	{
		 main.drawTree ();
	}

	@FXML
	private void reDrawPythagorasTree ()
	{
		main.clear();
		main.drawTree();
	}

	@FXML
	private void clear ()
	{ 
		main.clear();
	}

	public Main getMain ()
	{
		return main;
	}

	public void setMain (Main main)
	{
		this.main = main;
	}

	public TextField getIterations ()
	{
		return iterations;
	}

	public void setIterations (TextField iterations)
	{
		this.iterations = iterations;
	}

	public TextField getX ()
	{
		return x;
	}

	public void setX (TextField x)
	{
		this.x = x;
	}

	public TextField getY ()
	{
		return y;
	}

	public void setY (TextField y)
	{
		this.y = y;
	}

	public Slider getAngle ()
	{
		return angle;
	}

	public void setAngle (Slider angle)
	{
		this.angle = angle;
	}

	public TextField getHalfAngle ()
	{
		return halfAngle;
	}

	public void setHalfAngle (TextField halfAngle)
	{
		this.halfAngle = halfAngle;
	}

	public Slider getAngleBias ()
	{
		return angleBias;
	}

	public void setAngleBias (Slider angleBias)
	{
		this.angleBias = angleBias;
	}

	public TextField getSize ()
	{
		return size;
	}

	public void setSize (TextField size)
	{
		this.size = size;
	}

	public TextField getSizeMultiplier ()
	{
		return sizeMultiplier;
	}

	public void setSizeMultiplier (TextField sizeMultiplier)
	{
		this.sizeMultiplier = sizeMultiplier;
	}

	public RadioButton getBlackColor ()
	{
		return blackColor;
	}

	public void setBlackColor (RadioButton blackColor)
	{
		this.blackColor = blackColor;
	}

	public RadioButton getRandomColor ()
	{
		return randomColor;
	}

	public void setRandomColor (RadioButton randomColor)
	{
		this.randomColor = randomColor;
	}

	public RadioButton getCustomColor ()
	{
		return customColor;
	}

	public void setCustomColor (RadioButton customColor)
	{
		this.customColor = customColor;
	}

	public ColorPicker getColorPicker ()
	{
		return colorPicker;
	}

	public void setColorPicker (ColorPicker colorPicker)
	{
		this.colorPicker = colorPicker;
	}
}
