package narl.hpclp.shared;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

public class ItemParam implements Serializable {

	private static final long serialVersionUID = -672433658255729566L;

	public ItemParam(){		
	}
	
	public String[] prodxUnit;
	public String[] detectType;
	public String[] accntService;
	public String[] prodxEmitter;
			
	private ArrayList<String> tmp = new ArrayList<String>();
	
	public void gather(String val){
		tmp.add(val);
	}
	
	public void mapping(String name){
		if(name.equalsIgnoreCase("prodxUnit")==true){
			prodxUnit = tmp.toArray(new String[0]);
		}else if(name.equalsIgnoreCase("detectType")==true){
			detectType = tmp.toArray(new String[0]);
		}else if(name.equalsIgnoreCase("accntService")==true){
			accntService = tmp.toArray(new String[0]);
		}else if(name.equalsIgnoreCase("prodxEmitter")==true){
			prodxEmitter = tmp.toArray(new String[0]);
		}
		tmp.clear();
	}
	
	public String error = null;
	
	public ItemParam appendError(String txt){
		if(error==null){
			error = txt;
		}else{
			error = error+"\n========\n"+txt;
		}
		return this;
	}
}
