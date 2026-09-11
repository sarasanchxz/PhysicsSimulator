package simulator.model;

import simulator.misc.Vector2D;

public class StationaryBody extends Body{

	public StationaryBody(String id, String gid, Vector2D vPosicion, double masa) {
		super(id,gid,vPosicion, new Vector2D(), masa);
	}
	
	@Override
	protected void advance(double dt) {
		
	}
	
}
