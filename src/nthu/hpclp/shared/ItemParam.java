package nthu.hpclp.shared;

import java.io.Serializable;

public class ItemParam implements Serializable {

	private static final long serialVersionUID = 3403904676123063921L;
		
	private String key="",val="";
	
	public ItemParam(){
	}

	/**
	 * Special constructor, the value item has separator 
	 * @param k - key
	 * @param col - column number
	 */
	public ItemParam(String k,int col){
		setKey(k);		
		String val = "新增項目";
		for(int i=1; i<col; i++){
			val = val + "@新增項目";
		}
	}
	
	public ItemParam(String k, String v){
		setKey(k);
		setVal(v);
	}
	
	public ItemParam(ItemParam src){
		copyFrom(src);
	}

	public void copyFrom(ItemParam src){
		key = src.key;
		val = src.val;
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
