package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;

public abstract class Body {
	
	protected String id;
	protected String gid;
	protected Vector2D vPosicion;
	protected Vector2D vFuerza;
	protected Vector2D vVelocidad;
	protected double masa;

	public Body(String id, String gid, Vector2D vPosicion, Vector2D vVelocidad, double masa) throws IllegalArgumentException {
		
		
	if (id == null ||  gid == null || vPosicion == null || vVelocidad == null || masa <= 0.0 || id.trim().length()==0 || gid.trim().length()==0) 
		throw new IllegalArgumentException("Algún valor es null");
					
	this.id = id;
	this.gid = gid;
	this.vPosicion = new Vector2D(vPosicion);
	this.vVelocidad = new Vector2D(vVelocidad);
	this.vFuerza = new Vector2D();
	this.masa = masa;
	
	}
	
	public String getId() {
		
		return this.id;
		
	}
	
	public String getgId() {
		
		return this.gid;
		
	}
	
	public Vector2D getVelocity() {
		
		return this.vVelocidad;
		
	}
	
	public Vector2D getPosition() {
		 
		return this.vPosicion;
		
	}
	
	public Vector2D getForce() {
		
		return this.vFuerza;
		
	}
	
	public double getMass() {
		
		return this.masa;
		
	}
	
	public void addForce(Vector2D f) {
		
		this.vFuerza = this.vFuerza.plus(f);
		
	}
	
	public void resetForce() {
		
		this.vFuerza = vFuerza.minus(vFuerza);
		
	}
	
	public JSONObject getState() {
		
		//JSONObject jBody  = new JSONObject();
		JSONObject jDatos = new JSONObject();
		
		//jBody.put("type", "mv_body");
		//jBody.put("data", jDatos);
		//jDatos.put("gid", gid);
		
		JSONArray ja = new JSONArray();
		
		ja.put(vPosicion.getX());
		ja.put(vPosicion.getY());
		
		JSONArray ja1 = new JSONArray();
		
		ja1.put(vVelocidad.getX());
		ja1.put(vVelocidad.getY());
		
		JSONArray ja2 = new JSONArray();
		
		ja2.put(vFuerza.getX());
		ja2.put(vFuerza.getY());
		
		jDatos.put("p",ja);
		jDatos.put("v", ja1);
		jDatos.put("f", this.vFuerza.asJSONArray());
		jDatos.put("id", id);
		jDatos.put("m", masa);
		
		
		return jDatos;
		
	}
	
	public String toString() {
	
		return getState().toString();
		
	}
	
	protected abstract void advance(double dt);
		
}
