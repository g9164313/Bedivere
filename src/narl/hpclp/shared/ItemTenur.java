package narl.hpclp.shared;

import java.io.Serializable;
import java.util.Date;

public class ItemTenur extends ItemBase implements Serializable {

	private static final long serialVersionUID = -3588414162261404996L;

	public static final int INFO_TKEY = 0;	
	public static final int INFO_DEV_VENDOR = 1;
	public static final int INFO_DEV_SERIAL = 2;
	public static final int INFO_DEV_NUMBER = 3;
	public static final int INFO_DET_TYPE = 4;
	public static final int INFO_DET_SERIAL = 5;
	public static final int INFO_DET_NUMBER = 6;
	public static final int INFO_AREA = 7;	
	public static final int INFO_FACTOR = 8;
	public static final int INFO_STEER = 9;	
	public static final int INFO_MEMO = 10;
	public static final int INFO_MAX_COL = 11;
	
	public Date meet = new Date();
	
	public ItemBase owner = null;
	
	public ItemTenur(){
		super(INFO_MAX_COL);
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
		return inf[INFO_TKEY];
	}	
	public void setKey(String val){
		inf[INFO_TKEY]=val;
	}
	
	public String getDeviceVendor(){
		return inf[INFO_DEV_VENDOR];
	}	
	public void setDeviceVendor(String val){
		inf[INFO_DEV_VENDOR]=val;
		genkey();
	}	
	
	public String getDeviceSerial(){		
		return inf[INFO_DEV_SERIAL];
	}	
	public void setDeviceSerial(String val){
		inf[INFO_DEV_SERIAL]=val;
		genkey();
	}	
	
	public String getDeviceNumber(){
		return inf[INFO_DEV_NUMBER];
	}	
	public void setDeviceNumber(String val){
		inf[INFO_DEV_NUMBER]=val;
		genkey();
	}
		
	public String getDetectType(){
		return inf[INFO_DET_TYPE];
	}
	public void setDetectType(String val){
		inf[INFO_DET_TYPE]=val;
	}
	
	public String getDetectSerial(){
		return inf[INFO_DET_SERIAL];
	}
	public void setDetectSerial(String val){
		inf[INFO_DET_SERIAL]=val;
		genkey();
	}
	
	public String getDetectNumber(){
		return inf[INFO_DET_NUMBER];
	}
	public void setDetectNumber(String val){
		inf[INFO_DET_NUMBER]=val;
		genkey();
	}
	
	public String getArea(){
		return inf[INFO_AREA];
	}
	public void setArea(String val){
		inf[INFO_AREA]=val;
	}
	
	public String getFactor(){
		return inf[INFO_FACTOR];
	}
	public void setFactor(String val){
		inf[INFO_FACTOR]=val;
	}
	
	public String getSteer(){
		return inf[INFO_STEER];
	}
	public void setSteer(String val){
		inf[INFO_STEER]=val;
	}

	public String getMemo(){
		return inf[INFO_MEMO];
	}
	public void setMemo(String val){
		inf[INFO_MEMO]=val;
	}
	
	public boolean isChamber(){
		int pos = getDetectType().indexOf("游離腔");
		if(pos<0){
			return false;
		}
		return true;
	}
	
	private void genkey(){
		inf[INFO_TKEY] = genKeyPattern(
			inf[INFO_DEV_SERIAL], 
			inf[INFO_DEV_NUMBER],			
			inf[INFO_DET_SERIAL],
			inf[INFO_DET_NUMBER],
			inf[INFO_DEV_VENDOR]
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
	
	private static String trimTypo(String txt){
		txt = txt.toLowerCase();
		txt = txt.replaceAll("\\s","");
		txt = txt.replaceAll(",","");
		txt = txt.replaceAll("－","");//strange character!!
		return txt;
	}	
}
