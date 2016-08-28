package narl.hpclp.shared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Hub for enviroment parameter.<p>
 * @author qq
 *
 */
public class ItemParam implements Serializable {

	private static final long serialVersionUID = -672433658255729566L;

	public ItemParam(){		
	}
	
	public static class TxtPair{
		public String key;
		public String val;
		public TxtPair(String k, String v){
			key = k;
			val = v;
		}
	};
	
	public ArrayList<TxtPair> prodxDetType = new ArrayList<TxtPair>();
	public ArrayList<TxtPair> prodxRadUnit = new ArrayList<TxtPair>();
	public ArrayList<TxtPair> prodxEmitter = new ArrayList<TxtPair>();
	
	public ArrayList<TxtPair> accntService = new ArrayList<TxtPair>();
	
	public ArrayList<TxtPair> otherRestDay = new ArrayList<TxtPair>();

	public void gather(ArrayList<TxtPair> lst,String key,String val){
		lst.add(new TxtPair(key,val));
	}
	
	/**
	 * Server can put error message here~~~
	 */
	public String error = "";
	
	public ItemParam appendError(String msg){
		error = error + msg +"\n";
		return this;
	}
}
