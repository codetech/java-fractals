import static java.lang.Math.cos;
import static java.lang.Math.random;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class PythagorasTree
{
	private GraphicsContext				graphicsContext;
	private ObjectProperty<Integer>		iterations			= new SimpleObjectProperty<> (this, "iterations");
	private ObjectProperty<Integer>		x					= new SimpleObjectProperty<> (this, "x");
	private ObjectProperty<Integer>		y					= new SimpleObjectProperty<> (this, "y");
	private DoubleProperty				initAngle			= new SimpleDoubleProperty (this, "initAngle");
	private ObjectProperty<Double>		halfAngle			= new SimpleObjectProperty<> (this, "halfAngle");
	private DoubleProperty				angleBias			= new SimpleDoubleProperty (this, "angleBias");
	private ObjectProperty<Double>		size				= new SimpleObjectProperty<> (this, "size");
	private ObjectProperty<Double>		sizeMultiplier		= new SimpleObjectProperty<> (this, "sizeMultiplier");
	private BooleanProperty				drawBackwards		= new SimpleBooleanProperty (this, "drawBackwards");
	private ObjectProperty<Integer>		delay				= new SimpleObjectProperty<> (this, "delay");
	// RGB, [0, 1) given but [0,1] allowed, A = 1.0;
	private Function<Integer, Paint>	iterationsToColor	= (iterations) -> Color.color (random (),
																	random (),
																	random ());

	private ExecutorService				threadPool			= Executors.newCachedThreadPool ();

	public PythagorasTree (GraphicsContext graphicsContext)
	{
		setGraphicsContext (graphicsContext);
	}

	public boolean anyValuesNull ()
	{
		if (getIterations () == null
				|| getX () == null
				|| getY () == null
				|| getHalfAngle () == null
				|| getSize () == null
				|| getSizeMultiplier () == null
				|| getDelay () == null)
		{
			return true;
		}

		return false;
	}

	public boolean drawTree ()
	{
		return drawTree (false);
	}

	public boolean drawTree (boolean drawBackwards)
	{
		if (anyValuesNull ())
		{
			return false;
		}

		Thread drawTree;

		if (isDrawBackwards ())
		{
			drawTree = new Thread (
					() -> drawTreeBackward (0, getX (), getY (), toRadians (getInitAngle ()), getSize ()));
		}
		else
		{
			drawTree = new Thread (
					() -> drawTreeForward (0, getX (), getY (), toRadians (getInitAngle ()), getSize ()));
		}

		threadPool.execute (drawTree);

		return true;

	}

	private void drawTreeForward (int iterations, double x, double y, double initAngle, double size)
	{
		if (iterations >= getIterations ())
		{
			return;
		}

		double xPrime = x + cos (initAngle) * size;
		double yPrime = y - sin (initAngle) * size;

		double angleBiasLeft = 1;
		double angleBiasRight = 1;

		if (getAngleBias () > 0)
		{
			angleBiasLeft = getAngleBias ();
			angleBiasRight = 1 / getAngleBias ();
		}
		else if (getAngleBias () < 0)
		{
			angleBiasLeft = 1 / getAngleBias ();
			angleBiasRight = getAngleBias ();
		}

		if (!Thread.currentThread ().isInterrupted ())
		{
			try
			{
				Thread.sleep (getDelay ());
			}
			catch (InterruptedException e)
			{
				Thread.currentThread ().interrupt ();
				return; 
			}

			graphicsContext.setStroke (iterationsToColor.apply (iterations));
			graphicsContext.strokeLine (x, y, xPrime, yPrime);

			drawTreeForward (iterations + 1,
					xPrime,
					yPrime,
					initAngle + toRadians (getHalfAngle ()) * angleBiasLeft,
					size * getSizeMultiplier ());
			drawTreeForward (iterations + 1,
					xPrime,
					yPrime,
					initAngle - toRadians (getHalfAngle ()) * angleBiasRight,
					size * getSizeMultiplier ());
		}

	}

	private void drawTreeBackward (int iterations, double x, double y, double initAngle, double size)
	{
		if (iterations >= getIterations ())
		{
			return;
		}

		double xPrime = x + cos (initAngle) * size;
		double yPrime = y - sin (initAngle) * size;

		double angleBiasLeft = 1;
		double angleBiasRight = 1;

		if (getAngleBias () > 0)
		{
			angleBiasLeft = getAngleBias ();
			angleBiasRight = 1 / getAngleBias ();
		}
		else if (getAngleBias () < 0)
		{
			angleBiasLeft = 1 / getAngleBias ();
			angleBiasRight = getAngleBias ();
		}

		if (!Thread.currentThread ().isInterrupted ())
		{
			try
			{
				Thread.sleep (getDelay ());
			}
			catch (InterruptedException e)
			{
				Thread.currentThread ().interrupt ();
				return;
			}
			drawTreeBackward (iterations + 1,
					xPrime,
					yPrime,
					initAngle + toRadians (getHalfAngle () * angleBiasLeft),
					size * getSizeMultiplier ());
			drawTreeBackward (iterations + 1,
					xPrime,
					yPrime,
					initAngle - toRadians (getHalfAngle () * angleBiasRight),
					size * getSizeMultiplier ());

			graphicsContext.setStroke (iterationsToColor.apply (iterations));
			graphicsContext.strokeLine (x, y, xPrime, yPrime);
		}
	}

	public GraphicsContext getGraphicsContext ()
	{
		return graphicsContext;
	}

	public void setGraphicsContext (GraphicsContext graphicsContext)
	{
		this.graphicsContext = graphicsContext;
	}

	public final ObjectProperty<Integer> iterationsProperty ()
	{
		return this.iterations;
	}

	public final java.lang.Integer getIterations ()
	{
		return this.iterationsProperty ().get ();
	}

	public final void setIterations (final java.lang.Integer iterations)
	{
		this.iterationsProperty ().set (iterations);
	}

	public final ObjectProperty<Integer> xProperty ()
	{
		return this.x;
	}

	public final java.lang.Integer getX ()
	{
		return this.xProperty ().get ();
	}

	public final void setX (final java.lang.Integer x)
	{
		this.xProperty ().set (x);
	}

	public final ObjectProperty<Integer> yProperty ()
	{
		return this.y;
	}

	public final java.lang.Integer getY ()
	{
		return this.yProperty ().get ();
	}

	public final void setY (final java.lang.Integer y)
	{
		this.yProperty ().set (y);
	}

	public final DoubleProperty initAngleProperty ()
	{
		return this.initAngle;
	}

	public final double getInitAngle ()
	{
		return this.initAngleProperty ().get ();
	}

	public final void setInitAngle (final double initAngle)
	{
		this.initAngleProperty ().set (initAngle);
	}

	public final ObjectProperty<Double> halfAngleProperty ()
	{
		return this.halfAngle;
	}

	public final java.lang.Double getHalfAngle ()
	{
		return this.halfAngleProperty ().get ();
	}

	public final void setHalfAngle (final java.lang.Double halfAngle)
	{
		this.halfAngleProperty ().set (halfAngle);
	}

	public final DoubleProperty angleBiasProperty ()
	{
		return this.angleBias;
	}

	public final double getAngleBias ()
	{
		return this.angleBiasProperty ().get ();
	}

	public final void setAngleBias (final double angleBias)
	{
		this.angleBiasProperty ().set (angleBias);
	}

	public final ObjectProperty<Double> sizeProperty ()
	{
		return this.size;
	}

	public final java.lang.Double getSize ()
	{
		return this.sizeProperty ().get ();
	}

	public final void setSize (final java.lang.Double size)
	{
		this.sizeProperty ().set (size);
	}

	public final ObjectProperty<Double> sizeMultiplierProperty ()
	{
		return this.sizeMultiplier;
	}

	public final java.lang.Double getSizeMultiplier ()
	{
		return this.sizeMultiplierProperty ().get ();
	}

	public final void setSizeMultiplier (final java.lang.Double sizeMultiplier)
	{
		this.sizeMultiplierProperty ().set (sizeMultiplier);
	}

	public Function<Integer, Paint> getIterationsToColor ()
	{
		return iterationsToColor;
	}

	public void setIterationsToColor (Function<Integer, Paint> iterationsToColor)
	{
		this.iterationsToColor = iterationsToColor;
	}

	public final BooleanProperty drawBackwardsProperty ()
	{
		return this.drawBackwards;
	}

	public final boolean isDrawBackwards ()
	{
		return this.drawBackwardsProperty ().get ();
	}

	public final void setDrawBackwards (final boolean drawBackwards)
	{
		this.drawBackwardsProperty ().set (drawBackwards);
	}

	public final ObjectProperty<Integer> delayProperty ()
	{
		return this.delay;
	}

	public final java.lang.Integer getDelay ()
	{
		return this.delayProperty ().get ();
	}

	public final void setDelay (final java.lang.Integer delay)
	{
		this.delayProperty ().set (delay);
	}

	public ExecutorService getThreadPool ()
	{
		return threadPool;
	}

	public void setThreadPool (ExecutorService threadPool)
	{
		this.threadPool = threadPool;
	}

}
