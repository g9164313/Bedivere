package narl.hpclp.shared;

import java.io.Serializable;
import java.util.Date;

public class ItemOwner extends ItemBase implements Serializable {

	private static final long serialVersionUID = -8999124501083199660L;

	public static final int INFO_OKEY = 0;		
	public static final int INFO_NAME = 1;
	public static final int INFO_ZIP = 2;
	public static final int INFO_ADDR = 3;
	public static final int INFO_DEPT = 4;
	public static final int INFO_PRSN = 5;	
	public static final int INFO_PHONE = 6;
	public static final int INFO_EMAIL = 7;
	public static final int INFO_CLASS = 8;//deprecate~~~~
	public static final int INFO_MEMO = 9;
	public static final int INFO_MAX_COL = 10;	
	
	public ItemOwner(){
		super(INFO_MAX_COL);
	}
	
	public ItemOwner(
		String oid,
		String[] info,
		Date stmp
	){
		super(INFO_MAX_COL);
		map(oid,info,stmp);
	}
	
	public String getKey(){
		return inf[INFO_OKEY];
	}
	
	public String getName(){
		return inf[INFO_NAME];
	}
	
	public String getZip(){
		return inf[INFO_ZIP];
	}
	
	public String getAddress(){
		return inf[INFO_ADDR];
	}
	
	public String getDepart(){
		return inf[INFO_DEPT];
	}
	
	public String getPerson(){
		return inf[INFO_PRSN];
	}
	
	public String getPhone(){
		return inf[INFO_PHONE];
	}
	
	public String getEmail(){
		return inf[INFO_EMAIL];
	}
	
	public String getMemo(){
		return inf[INFO_MEMO];
	}
}
