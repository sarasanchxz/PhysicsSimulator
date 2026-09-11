package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class BodiesGroup implements Iterable<Body>{

	private String id;
	private ForceLaws forceLaws;
	private List<Body> bodyList;
	List<Body> _bodiesRO; //NUEVO
	
	public BodiesGroup(String id, ForceLaws fuerzas) {//preguntar
		if(fuerzas == null ||id == null || id.trim().length() == 0) { 
			throw new IllegalArgumentException("Algún valor es null");
		}
		this.forceLaws = fuerzas;
		this.id = id;
		bodyList = new ArrayList<>();
		_bodiesRO = Collections.unmodifiableList(bodyList); //bodyList = _bodies
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setForceLaws(ForceLaws f) {
		if(f==null) 
			throw new IllegalArgumentException("Algún valor es null");
		
		this.forceLaws = f;
	}
	
	public void addBody(Body b) {
		if( b == null)
			throw new IllegalArgumentException("Ya tiene este identificador o body es null");
		
		if(this.bodyList.contains(b)) {
			throw new IllegalArgumentException("Ya tiene este identificador o body es null");
		}
		/*for(Body be: bodyList) {
			if(b.getId() == be.getId() ) {
				throw new IllegalArgumentException("Ya tiene este identificador o body es null");
			}
		}*/
		bodyList.add(b);
		
	}
	
	public void advance(double dt) {
		if(dt<=0)
			throw new IllegalArgumentException("Dt Negativo");
		
			
		for (Body body : bodyList ) { // no se porque no se puede hacer todo en un for????
			body.resetForce();		
		}
		
		forceLaws.apply(bodyList);
		
		for (Body body : bodyList ) {		
			body.advance(dt);		
		}
	}
	
	public JSONObject getState() {
		
		JSONObject Bodies  = new JSONObject();		
		Bodies.put("id", this.id);
		
		JSONArray ja = new JSONArray();
		
		for(Body body: bodyList) {
			
			ja.put(body.getState());
		}
		
		Bodies.put("bodies", ja);
		
		return Bodies;
	}
	
	public String toString() {
		return getState().toString();
	}
	
	//NUEVO
	
	public String getForceLawsInfo(){		
		return  forceLaws.toString();
	}
	
	@Override
	public Iterator<Body> iterator() {	 // no se aun que es un iterador ni que significa iterator<> pero es esto	
		return _bodiesRO.iterator();
	}
	
	//esta es casera
	
	public List<Body> getBodies(){
		return _bodiesRO;
	}

}

	