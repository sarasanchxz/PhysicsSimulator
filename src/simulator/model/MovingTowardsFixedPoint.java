package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws{
	 double g = 9.8;
	 Vector2D c;
	
	 public MovingTowardsFixedPoint() {
		 
	 }
	public MovingTowardsFixedPoint(Vector2D c, double g) {
		if(c== null || g <= 0)
			throw new IllegalArgumentException("Algo fue mal"); 
		
		this.c = c; 
		this.g = g;
	}
	
	@Override
	public void apply(List<Body> bs) {
		Vector2D ye = new Vector2D();
		for (Body body : bs ) { 
			if(c.distanceTo(body.vPosicion)!=0) {
				ye = c.minus(body.vPosicion).direction().scale(body.masa*g);
				body.vFuerza = body.vFuerza.plus(ye);
			}
		}
	}

	@Override
	public String toString() {
		
		return "Moving towards "+ c+" with constant acceleration "+ g;
	}	
	
}
