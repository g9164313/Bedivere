package narl.hpclp.shared;

import java.io.Serializable;
import java.util.Date;

public class ItemBase implements Serializable {

	private static final long serialVersionUID = 7096858623078746344L;

	public String id = "";
	public Date stmp = new Date();
	public Date last = new Date();
	public String[] inf;
	
	public ItemBase(int size){
		inf = new String[size];
		for(int i=0; i<size; i++){
			inf[i] = "????";
		}
	}
		
	public void map(String[] val){
		for(int i=0; i<inf.length; i++){
			if(i>=val.length){
				break;
			}
			if(val[i]==null){
				inf[i] = "";//some old data still keep the value null :-( 
			}else{
				inf[i] = val[i];
			}
		}
	}
	
	public void map(
		String id,
		String[] val
	){
		this.id = id;
		map(val);
	}
	
	public void map(
		String id,
		String[] val,
		Date stmp
	){
		this.id = id;
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
		this.id = id;
		this.stmp.setTime(stmp.getTime());
		this.last.setTime(last.getTime());
		map(val);		
	}	
}
