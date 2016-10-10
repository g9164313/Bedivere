package nthu.hpclp.shared;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;

/**
 * Hub for enviroment parameter.<p>
 * @author qq
 *
 */
public class ParamHub implements Serializable {

	private static final long serialVersionUID = -6683462103610450373L;

	public ParamHub(){		
	}
	
	public ArrayList<ItemParam> prodxDetType = new ArrayList<ItemParam>();
	public ArrayList<ItemParam> prodxRadUnit = new ArrayList<ItemParam>();
	public ArrayList<ItemParam> prodxEmitter = new ArrayList<ItemParam>();
	public ArrayList<ItemParam> accntService = new ArrayList<ItemParam>();
	public ArrayList<ItemParam> otherRestDay = new ArrayList<ItemParam>();

	public void gather(ArrayList<ItemParam> lst,String key,String val){
		lst.add(new ItemParam(key,val));
	}
	
	/**
	 * Server can put error message here~~~
	 */
	public String error = null;
	
	public ParamHub appendError(String msg){
		if(error==null){
			error = msg;
		}else{
			error = error + msg +"\n";
		}
		GWT.log(msg);
		System.out.println(msg);
		return this;
	}
}
