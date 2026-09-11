package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import simulator.model.BodiesGroup;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator implements Observable<SimulatorObserver> {

	private double dt;
	private ForceLaws forceLaws;
	private Map<String,BodiesGroup> maps; // =_groups ??
	private List<String> BodyStr;
	private double currentTime;
	private List<SimulatorObserver> ObserverList; //lista de observadores
	private Map<String, BodiesGroup> _groupsRO;
	
	
	public PhysicsSimulator(ForceLaws forceLaws,double dt) {
		if( dt<0 || forceLaws == null) 
		{
			throw new IllegalArgumentException("Datos incorrectos");
		}
		else 
		{
		this.dt = dt;
		this.forceLaws = forceLaws;
		this.maps = new HashMap<>();
		this.BodyStr = new ArrayList<>();
		this.ObserverList = new ArrayList<>();
		this.currentTime = 0;
		this._groupsRO = Collections.unmodifiableMap(maps); //esto es loq ue le paso a los observadores no maps
		
		}
	}
	
	public void advance() {
		/*for(Map.Entry<String,BodiesGroup> entry : maps.entrySet()) {
			entry.advance(dt);
		}*/
		
		//Solucion de una herramienta cochina(ni idea de si está bien)
		for (BodiesGroup bodiesGroup : maps.values()) {
		   bodiesGroup.advance(dt);
		}
		
		this.currentTime+=dt;
		
		for (SimulatorObserver observer : ObserverList) {  //nuevo			
			observer.onAdvance(_groupsRO, currentTime);;	
		}
		
		
	}
	
	public void addGroup(String id) {
		boolean existe = maps.containsKey(id);
		if(existe)
			throw new IllegalArgumentException("Existe ya un grupo con ese id");
		else {
			BodiesGroup bd = new BodiesGroup(id,this.forceLaws);
			maps.put(id, bd);
			BodyStr.add(id); // añado el string a una lista que he creado
			
			for (SimulatorObserver observer : ObserverList) {  //nuevo			
				observer.onGroupAdded(_groupsRO, bd);	
			}
		}
	}
	
	public void addBody(Body b) {
		boolean existe = maps.containsKey(b.getgId());
		if(!existe) {
			throw new IllegalArgumentException("Existe ya un grupo con ese id");
		}
		else {
			
			maps.get(b.getgId()).addBody(b);
			
			for (SimulatorObserver observer : ObserverList) {  //nuevo no se si va dentro o fuera del else		
				observer.onBodyAdded(_groupsRO, b);	
			}
			
		}
	}
	
	public void setForceLaws(String id, ForceLaws fl) {
		boolean existe = maps.containsKey(id);
		if(!existe) { //solo funciona poniendolo con ! pero no tiene sentido
			throw new IllegalArgumentException("existe un grupo con ese id");
			
		}
		else {
			maps.get(id).setForceLaws(fl);
			
			for (SimulatorObserver observer : ObserverList) {  //nuevo				
				observer.onForceLawsChanged(maps.get(id));	//NO SE SI ESTO ESTA BIEN 
			}
		}
	}
	
	public JSONObject getState() {
		
		JSONObject ret = new JSONObject();
		
		ret.put("time", this.currentTime);
		
		JSONArray ja = new JSONArray();
		
		for (String str : BodyStr) {
			ja.put(maps.get(str).getState());
		}
		
		ret.put("groups", ja);
		
		return ret;
	}
	
	public String toString() {
		return  getState().toString();
	}
	
	/*public void setDeltaTime(double dt) {
		this.dt=dt;
	}*/
	
	//cosas nuevas XXXXXXXXXXXXXXXXXXXXXXXXXXX
	
	public void reset() {
		
		maps.clear();
		BodyStr.clear();
		currentTime = 0.0;
		
		for (SimulatorObserver observer : ObserverList) {  //nuevo			
			observer.onReset(_groupsRO, currentTime, dt);		
		}
	}
	
	public void setDeltaTime(double dt) {
		
		if (dt< 0)  {			
			throw new IllegalArgumentException (" dt no es natural");			
		}		
		else {
		currentTime = dt;	
		
		for (SimulatorObserver observer : ObserverList) {  //nuevo				
			observer.onDeltaTimeChanged(dt);	
		  }
		}
	}
	
	
	//METODOS NUEVOS AL AGREGARLE LA INTERFAZ 

	@Override
	public void addObserver(SimulatorObserver o) {
		
		if (!ObserverList.contains(o)) {			
			ObserverList.add(o);
		}	
		
		o.onRegister(_groupsRO, currentTime, dt);
	}

	@Override
	public void removeObserver(SimulatorObserver o) {
		
		int index = ObserverList.indexOf(o);
		if (index != -1) {
		ObserverList.remove(index);// esto ya desplaza tras eliminar	
		}
		
	}
	
	
	
	
	
	
	
}

