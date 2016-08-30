package narl.hpclp.shared;

import java.io.Serializable;

public class ItemParam implements Serializable {

	private static final long serialVersionUID = 3403904676123063921L;
		
	public static final int STA_IDLE = 0;
	public static final int STA_CREATE = 1;
	public static final int STA_UPDATE = 2;
	public static final int STA_DELETE = 3;
	
	private int sta = STA_IDLE;
	
	private String key="",val="";
	
	public ItemParam(){
	}
	
	public ItemParam(String k, String v){
		setKey(k);
		setVal(v);
	}
	
	public void set(ItemParam src){
		key = src.key;
		val = src.val;
		sta = src.sta;
	}
	
	public void setState(int sta){
		this.sta = sta;
		error = null;
	}
	
	public int getState(){
		return sta;
	}
	
	public void setKey(String txt){
		key = txt;
	}
	
	public String getKey(){
		return key;
	}
	
	public void setVal(String txt){
		val = txt;
	}
	
	public String getVal(){
		return val;
	}
	
	public void setVal(int cid,String txt){
		String[] v = val.split("@");
		if(0<=cid && cid<v.length){
			v[cid] = txt.trim();
			//combine data again!!!
			val = v[0];
			for(int i=1; i<v.length; i++){
				val = val+"@"+v[i];
			}
		}else{
			val = txt;
		}		
	}
	
	public String getVal(int cid){
		String[] v = val.split("@");
		if(0<=cid && cid<v.length){			
			return v[cid];
		}
		return "";
	}
	
	/**
	 * This variable keep error message, reset this when state is changed
	 */
	public String error = null;
	
	public String appendError(String txt){
		if(error==null){
			error = txt;
		}else{
			error = error + "\n" + txt;
		}
		return error;
	}
}
