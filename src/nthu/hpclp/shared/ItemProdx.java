package nthu.hpclp.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ItemProdx extends ItemBase implements Serializable {

	private static final long serialVersionUID = -2882728634967952556L;

	public static final int INFO_PKEY = 0;
	public static final int INFO_AMBIENCE = 1;	
	public static final int INFO_DISTANCE = 2;
	public static final int INFO_UNIT_REF = 3;
	public static final int INFO_UNIT_MEA = 4;
	public static final int INFO_REV1 = 5;
	public static final int INFO_EMITTER = 6;
	public static final int INFO_MEMO = 7;
	public static final int INFO_MAX_COL = 8;

	//the experiment format
	public static final int FMT_F1V = 0;
	public static final int FMT_F1W = 1;
	public static final int FMT_F2 = 2;
	public static final int FMT_F3 = 3;//γ反應報告
	public static final int FMT_F4 = 4;//污染報告
	public static final int FMT_F5 = 5;//活度報告
	public int format = FMT_F2;//default!!!
	
	public boolean useLogo = true;
	
	public ItemOwner owner = null;
	
	public ItemTenur tenur = null;
	
	public ArrayList<String> scribble = new ArrayList<String>();
	
	public ItemProdx() {
		super(INFO_MAX_COL);
	}
	
	public ItemProdx(
		String oid,
		String[] info,
		Date stmp,
		Date last
	){
		super(INFO_MAX_COL);
		map(oid,info,stmp,last);
	}
	
	public String getKey(){
		return info[INFO_PKEY];
	}
	public void setKey(String val){
		info[INFO_PKEY]=val;
	}
	
	public String getFormat(){
		return fmt2txt(format);
	}
	public void setFormat(String fmt){
		setFormat(txt2fmt(fmt));
	}
	public void setFormat(int fmt){
		format = fmt;
	}

	public String getTemperature(){
		return get_col(info[INFO_AMBIENCE],0);
	}
	public String getPressure(){
		return get_col(info[INFO_AMBIENCE],1);	
	}
	public String getHumidity(){
		return get_col(info[INFO_AMBIENCE],2);
	}
	private String get_col(String txt,int idx){
		String[] col = txt.split("@");
		if(idx>=col.length){
			return "";
		}
		return col[idx];
	}
	
	public void setAmbience(
		String temp,
		String press,
		String humid
	){		
		String[] tmp = info[INFO_AMBIENCE].split("@");
		String[] col;
		switch(tmp.length){
		case 0:
			col = new String[]{"","",""};
			break;
		case 1:
			col = new String[]{tmp[0],"",""};
			break;
		case 2:
			col = new String[]{tmp[0],tmp[1],""};
			break;
		default:
			col = tmp;
			break;
		}
		if(temp !=null){ col[0] = temp.trim(); }
		if(press!=null){ col[1] = press.trim(); }
		if(humid!=null){ col[2] = humid.trim(); }	
		info[INFO_AMBIENCE] = col[0] + "@" + col[1] + "@" + col[2];
	}
	/*public void setAmbience(
		Double temp,
		Double press,
		Double humid
	){
		String[] col = info[INFO_AMBIENCE].split("@");
		if(temp !=null){ col[0] = temp.toString(); }
		if(press!=null){ col[1] = press.toString(); }
		if(humid!=null){ col[2] = humid.toString(); }
		info[INFO_AMBIENCE] = col[0] + "@" + col[1] + "@" + col[2];
	}*/
	
	public ParmEmitter getEmitter(){
		return new ParmEmitter(info[INFO_EMITTER]);
	}
	public String getEmitterTxt(){
		return info[INFO_EMITTER];
	}
	public void setEmitter(ParmEmitter parm){
		info[INFO_EMITTER] = parm.toString();
	}
	public void setEmitter(String val){
		info[INFO_EMITTER] = val;
	}
	
	public String getUnitRef(){
		return info[INFO_UNIT_REF].trim();
	}
	public void setUnitRef(String val){
		info[INFO_UNIT_REF]=val;
	}
	
	public String getUnitMea(){
		return info[INFO_UNIT_MEA].trim();
	}
	public void setUnitMea(String val){
		info[INFO_UNIT_MEA]=val;
	}
	
	public String getDistance(){
		return info[INFO_DISTANCE];
	}
	public void setDistance(String val){
		info[INFO_DISTANCE]=val;
	}
		
	public String getMemo(){
		return info[INFO_MEMO];
	}
	public void setMemo(String val){		
		info[INFO_MEMO]=val;		
	}
	
	public static final String TXT_FMT_F1V = "F1V";
	public static final String TXT_FMT_F1W = "F1W";
	public static final String TXT_FMT_F2 = "F2";
	public static final String TXT_FMT_F3 = "γ反應報告";
	public static final String TXT_FMT_F4 = "效率報告";
	public static final String TXT_FMT_F5 = "活度報告";

	public static int txt2fmt(String txt){
		int fmt = FMT_F2;//default~~~
		if(txt.equalsIgnoreCase(TXT_FMT_F1V)==true){
			fmt = FMT_F1V;
		}else if(txt.equalsIgnoreCase(TXT_FMT_F1W)==true){
			fmt = FMT_F1V;
		}else if(txt.equalsIgnoreCase(TXT_FMT_F2)==true){
			fmt = FMT_F2;
		}else if(txt.equalsIgnoreCase(TXT_FMT_F3)==true){
			fmt = FMT_F3;
		}else if(txt.equalsIgnoreCase(TXT_FMT_F4)==true){
			fmt = FMT_F4;
		}else if(txt.equalsIgnoreCase(TXT_FMT_F5)==true){
			fmt = FMT_F5;
		}
		return fmt;
	}
	public static String fmt2txt(int fmt){
		switch(fmt){
		case FMT_F1V:
			return TXT_FMT_F1V;
		case FMT_F1W:
			return TXT_FMT_F1W;
		case FMT_F2:
			return TXT_FMT_F2;
		case FMT_F3:
			return TXT_FMT_F3;
		case FMT_F4:
			return TXT_FMT_F4;
		case FMT_F5:
			return TXT_FMT_F5;
		}
		return TXT_FMT_F2;//default~~~
	} 
}
