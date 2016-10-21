package nthu.hpclp.shared;

import java.io.Serializable;
import java.util.Date;

public class ItemTenur extends ItemBase implements Serializable {

	private static final long serialVersionUID = -3588414162261404996L;

	public static final int INFO_TKEY = 0;	
	public static final int INFO_DEV_VENDOR = 1;
	public static final int INFO_DEV_SERIAL = 2;//型號
	public static final int INFO_DEV_NUMBER = 3;//序號
	public static final int INFO_DET_TYPE = 4;
	public static final int INFO_DET_SERIAL = 5;//型號
	public static final int INFO_DET_NUMBER = 6;//序號
	public static final int INFO_AREA = 7;	
	public static final int INFO_FACTOR = 8;
	public static final int INFO_STEER = 9;	
	public static final int INFO_MEMO = 10;
	public static final int INFO_MAX_COL = 11;
	
	public Date meet = new Date();
	
	public ItemOwner owner = null;
	
	private static final String DEF_DET_TYPE = "游離腔";
	
	public ItemTenur(){
		super(INFO_MAX_COL);
		info[INFO_DET_TYPE] = DEF_DET_TYPE;
	}

	public ItemTenur(ItemOwner own){
		super(INFO_MAX_COL);
		info[INFO_DET_TYPE] = DEF_DET_TYPE;
		owner = own;		
	}
	
	public ItemTenur(
		String id,
		String[] info,
		Date stmp,
		Date last
	){
		super(INFO_MAX_COL);
		map(id,info,stmp,last);
		this.meet.setTime(stmp.getTime());
	}
	
	public ItemTenur(
		String id,
		String[] info,
		Date stmp,
		Date last,
		Date meet
	){
		super(INFO_MAX_COL);
		map(id,info,stmp,last);
		this.meet.setTime(meet.getTime());
	}
	
	public String getKey(){
		return info[INFO_TKEY];
	}	
	public void setKey(String val){
		info[INFO_TKEY]=val;
	}
	
	public void copyTo(ItemTenur dst){
		super.copyTo(dst);
		if(this.owner==null){
			this.owner.copyTo(dst.owner);
		}
		dst.meet.setTime(this.meet.getTime());
	}
	
	public void setMeet(Date d){
		meet.setTime(d.getTime());
	}
	
	
	public String getName(){		
		return info[INFO_DEV_SERIAL]+" "+info[INFO_DEV_NUMBER];
	}
	
	
	public String getDeviceVendor(){
		return info[INFO_DEV_VENDOR];
	}	
	public void setDeviceVendor(String val){
		info[INFO_DEV_VENDOR]=val;
		genkey();
	}	
	
	public String getDeviceSerial(){		
		return info[INFO_DEV_SERIAL];
	}	
	public void setDeviceSerial(String val){
		info[INFO_DEV_SERIAL]=val;
		genkey();
	}

	public String getDeviceNumber(){
		return info[INFO_DEV_NUMBER];
	}	
	public void setDeviceNumber(String val){
		info[INFO_DEV_NUMBER]=val;
		genkey();
	}
		
	public String getDetectType(){
		return info[INFO_DET_TYPE];
	}
	public void setDetectType(String val){
		info[INFO_DET_TYPE]=val;
	}
	
	public String getDetectSerial(){
		return info[INFO_DET_SERIAL];
	}
	public void setDetectSerial(String val){
		info[INFO_DET_SERIAL]=val;
		genkey();
	}
	
	public String getDetectNumber(){
		return info[INFO_DET_NUMBER];
	}
	public void setDetectNumber(String val){
		info[INFO_DET_NUMBER]=val;
		genkey();
	}
	
	public String getArea(){
		return info[INFO_AREA];
	}
	public void setArea(String val){
		info[INFO_AREA]=val;
	}
	
	public String getFactor(){
		return info[INFO_FACTOR];
	}
	public void setFactor(String val){
		info[INFO_FACTOR]=val;
	}
	
	public String getSteer(){
		return info[INFO_STEER];
	}
	public void setSteer(String val){
		info[INFO_STEER]=val;
	}

	public String getMemo(){
		return info[INFO_MEMO];
	}
	public void setMemo(String val){
		info[INFO_MEMO]=val;
	}
	
	public boolean isChamber(){
		int pos = getDetectType().indexOf("游離腔");
		if(pos<0){
			return false;
		}
		return true;
	}
	
	private void genkey(){
		info[INFO_TKEY] = genKeyPattern(
			info[INFO_DEV_SERIAL], 
			info[INFO_DEV_NUMBER],			
			info[INFO_DET_SERIAL],
			info[INFO_DET_NUMBER],
			info[INFO_DEV_VENDOR]
		);
	}
	
	//use serial and number to generate pattern~~~
	public static String genKeyPattern(String... vals){
		int cnt = 0;
		String pattern = "";
		for(int i=0; i<vals.length; i++){
			String aaa = vals[i];
			if(aaa==null){
				continue;
			}
			aaa = trimTypo(aaa);
			if(aaa.length()==0){
				continue;
			}		
			pattern = pattern+aaa+"_";
			cnt++;
			if(cnt>=2){
				break;
			}
		}
		int len = pattern.length();
		if(len==0){
			pattern = RandUtil.uuid(7, 10);
		}else{
			pattern = pattern.substring(0,len-1);
		}	
		return pattern;
	}
	
	public static String trimTypo(String txt){
		return txt.trim().toLowerCase()
			.replaceAll("\\s","")
			.replaceAll(",","")
			.replaceAll("-","")
			.replaceAll("＋","");		
	}	
}
