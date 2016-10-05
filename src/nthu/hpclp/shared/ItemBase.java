package nthu.hpclp.shared;

import java.io.Serializable;
import java.util.Date;

public class ItemBase implements Serializable {

	private static final long serialVersionUID = 7096858623078746344L;

	/**
	 * When UUID is started with dollar($) sign, delete this item!!!<p>
	 * When UUID is a empty string, create a new one and insert it to database.<p>
	 * When UUID is valid, modify record in database.<p>
	 */
	public String uuid = "";
	
	public Date stmp = new Date();
		
	public Date last = new Date();
	
	public String[] info;
	
	public boolean death = false;
	
	public String error = "";
	
	public ItemBase(int size){
		info = new String[size];
		for(int i=0; i<size; i++){
			info[i] = "";
		}
	}
	
	public void copyTo(ItemBase dst){
		if(dst==null){
			return;
		}
		dst.uuid = this.uuid;
		dst.stmp.setTime(this.stmp.getTime());
		dst.last.setTime(this.last.getTime());
		for(int i=0; i<info.length; i++){
			dst.info[i] = this.info[i];
		}
	}
	
	public void map(String[] val){
		if(val==null){
			return;
		}
		for(int i=0; i<info.length; i++){
			if(i>=val.length){
				break;
			}
			if(val[i]==null){
				info[i] = "";//some old data still keep the value null :-( 
			}else{
				info[i] = val[i];
			}
		}
	}
	
	public void map(
		String id,
		String[] val
	){
		this.uuid = id;
		map(val);
	}
	
	public void map(
		String id,
		String[] val,
		Date stmp
	){
		this.uuid = id;
		long tick = stmp.getTime();
		this.stmp.setTime(tick);
		this.last.setTime(tick);
		map(val);		
	}
	
	public void map(
		String id,
		String[] val,
		Date stmp,
		Date last
	){
		this.uuid = id;
		this.stmp.setTime(stmp.getTime());
		this.last.setTime(last.getTime());
		map(val);		
	}
	
	public void setStmp(Date d){
		stmp.setTime(d.getTime());
	}
	
	public void setLast(Date d){
		stmp.setTime(d.getTime());
	}
}
