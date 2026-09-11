package simulator.model;

import simulator.misc.Vector2D;

public class MovingBody extends Body {
	
	protected Vector2D vAceleracion;

	public MovingBody(String id,String gid, Vector2D vPosicion, Vector2D vVelocidad, double masa) {
		
		super(id, gid,vPosicion,vVelocidad, masa);
	}

	@Override
	protected void advance(double dt) {
		
	if ( masa == 0) {
		this.vAceleracion = new Vector2D();	
	}
	
	else {
		this.vAceleracion = vFuerza.scale(1/masa); 
	}	
	  this.vPosicion =  vPosicion.plus(vVelocidad.scale(dt).plus(this.vAceleracion.scale(0.5 *dt*dt)));
	  this.vVelocidad =vAceleracion.scale(dt).plus(vVelocidad);
	}
	
}
