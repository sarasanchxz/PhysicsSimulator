package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardFixedPoint implements ForceLaws{
	private double g;
	private Vector2D c;
	
	
	public MovingTowardFixedPoint(Vector2D c, double g) {
		if(c== null || g <0)
			throw new IllegalArgumentException("Algo fue mal"); 
		
		this.c = new Vector2D(c); 
		this.g = g;
	}
	
	@Override
	public void apply(List<Body> bs) {
		Vector2D f2;
		for (Body body : bs ) { //estoy haciendo un blucle anidado creo
			double masa = body.getMass();
			f2 = (c.minus(body.vPosicion).direction()).scale(g*masa);
			body.vFuerza.plus(f2);
		}
	}

	@Override
	public String toString() {
		return "MovingTowardFixedPoint [g=" + g + ", c=" + c + "]";
	}	
	
}
