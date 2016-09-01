package nthu.hpclp.shared;

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
		Date stmp,
		Date last
	){
		super(INFO_MAX_COL);
		map(oid,info,stmp,last);
	}
	
	public void copyTo(ItemOwner dst){
		super.copyTo(dst);
	}
	
	public String getFullName(){
		return info[INFO_OKEY]+" "+info[INFO_NAME];
	}
	
	public String getKey(){
		return info[INFO_OKEY];
	}
	public void setKey(String txt){
		info[INFO_OKEY] = txt;
	}
	
	public String getName(){
		return info[INFO_NAME];
	}
	public void setName(String txt){
		info[INFO_NAME] = txt;
	}
	
	public String getZip(){
		return info[INFO_ZIP];
	}
	public void setZip(String txt){
		info[INFO_ZIP] = txt;
	}
	
	public String getAddress(){
		return info[INFO_ADDR];
	}
	public void setAddress(String txt){
		info[INFO_ADDR] = txt;
	}
	
	public String getDepartment(){
		return info[INFO_DEPT];
	}
	public void setDepartment(String txt){
		info[INFO_DEPT] = txt;
	}
	
	public String getPerson(){
		return info[INFO_PRSN];
	}
	public void setPerson(String txt){
		info[INFO_PRSN] = txt;
	}
	
	public String getPhone(){
		return info[INFO_PHONE];
	}
	public void setPhone(String txt){
		info[INFO_PHONE] = txt;
	}
	
	public String getEMail(){
		return info[INFO_EMAIL];
	}
	public void setEMail(String txt){
		info[INFO_EMAIL] = txt;
	}
	
	public String getMemo(){
		return info[INFO_MEMO];
	}
	public void setMemo(String txt){
		info[INFO_MEMO] = txt;
	}
}
