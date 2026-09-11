package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws {

	private double g;


	public NewtonUniversalGravitation(double g) {

	if(g<=0) {
		throw new IllegalArgumentException("La constante gravitacional es menor que 0");
	}
	else {
		this.g = g;
	}

	}
	
	public void apply(List<Body> bs) {
		
		double f;
		Vector2D ye = new Vector2D();
		
		for (Body body : bs ) { //estoy haciendo un blucle anidado creo

			for(Body body1 : bs) {
				
				if(body.vPosicion.distanceTo(body1.vPosicion)!=0) {
					double dist = body.getPosition().distanceTo(body1.getPosition());
					f = (g*body.getMass()*body1.getMass())/(dist*dist);
//					ye=(body1.vPosicion.minus(body.vPosicion)).scale(1/body.vPosicion.distanceTo(body1.getPosition())).scale(f);
					ye = (body1.getPosition().minus(body.getPosition())).direction().scale(f);
				}else {
					ye= new Vector2D();
				}
				body.addForce(ye);
			}
		
		}


	}


	@Override
	public String toString() {
		return "NewtonUniversalGravitation with G "  + g;
	}



	}


