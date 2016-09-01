package nthu.hpclp.shared;

import java.io.Serializable;
import java.math.BigDecimal;

public class ParmEmitter implements Serializable {

	private static final long serialVersionUID = 5665716916993277316L;

	public static final int TITLE = 0;	
	public static final int KIND = 1;
	public static final int AREA = 2;
	public static final int STRENGTH = 3;
	public static final int SURFACE = 4;
	public static final int SERIAL = 5;
	public static final int CRITERON = 6;
	public static final int FACTOR_K = 7;
	public static final int FACTOR_P = 8;
	public static final int UNCERTAIN = 9;
	public static final int MAX_COLS = 10;
	
	private static final String DEFAULT_UNCERTAIN = "0.025";
	
	public String[] info;
	
	public ParmEmitter(String txt){
		setText(txt);
	}
	
	public void setText(String txt){
		info = txt.split("@");
	}
	
	@Override
	public String toString(){
		String txt = info[0];
		for(int i=1; i<MAX_COLS; i++){
			if(i>=info.length){
				txt = txt + "@";
			}else{
				txt = txt + "@" + info[i];
			}
		}
		return txt;
	}
	
	public String getTitle(){
		return info[TITLE];
	}
	public void setTitle(String val){
		info[TITLE] = val;
	}
	
	public String getKind(){
		return info[KIND];
	}
	public void setKind(String val){
		info[KIND] = val;
	}
	
	public String getArea(){
		return info[AREA];
	}
	public void setArea(String val){
		info[AREA] = val;
	}
	public String getAreaValue(){
		return Const.getValOrUnit(info[AREA],false);
	}
	public String getAreaUnit(){
		return Const.getValOrUnit(info[AREA],true);
	}
	
	public String getStrength(){
		return info[STRENGTH];
	}
	public String getStrengthValue(){
		return Const.getValOrUnit(info[STRENGTH],false);
	}
	public String getStrengthUnit(){
		return Const.getValOrUnit(info[STRENGTH],true);
	}
	public void setStrength(String txt){
		info[STRENGTH] = txt;
	}
	public String getStrengthNorm(){
		double strg = _get_strn_value();
		double area = Const.exchange_area(getAreaValue(),getAreaUnit(),"cm");
		double val = strg/area;
		BigDecimal vv = new BigDecimal(val);
		vv = vv.setScale(1,BigDecimal.ROUND_HALF_EVEN);//truncate!!!
		int prec = vv.precision();
		String unit = getStrengthUnit();		
		if(unit.equalsIgnoreCase("kBq")==true){		
			if(prec<=3){
				unit = "Bq";
			}else{
				vv = vv.movePointLeft(3);
			}
		}else if(unit.equalsIgnoreCase("MBq")==true){		
			if(prec<=3){
				unit = "Bq";
			}else{
				vv = vv.movePointLeft(6);
			}		
		}else if(unit.equalsIgnoreCase("GBq")==true){			
			if(prec<=3){
				unit = "Bq";
			}else{
				vv = vv.movePointLeft(9);
			}			
		}
		return vv.toString()+" "+unit+"/cm²";
	}
	public double _get_strn_value(){
		double v = 0.f;
		String val, unit;		
		val = getStrengthValue();		
		unit = getStrengthUnit();
		try{
			v = Double.valueOf(val);
		}catch(NumberFormatException e){
			return 0.f;
		}
		if(unit.equalsIgnoreCase("Bq")==true){		
			v = v*1.f;	
		}else if(unit.equalsIgnoreCase("kBq")==true){		
			v = v*1e3;
		}else if(unit.equalsIgnoreCase("MBq")==true){		
			v = v*1e6;			
		}else if(unit.equalsIgnoreCase("GBq")==true){			
			v = v*1e9;			
		}else{
			v = 0.f;
		}
		return v;
	}
	public String _get_strn_base_unit(){
		String unit = getStrengthUnit();
		char ss = unit.charAt(0);
		if(ss=='k' || ss=='M' || ss=='G'){
			unit = unit.substring(1);
		}
		return unit;
	}

	public String getActivityValue(String meaUnit){
		double act;
		String val, unit;
		val = getStrengthValue();
		unit = getStrengthUnit().toLowerCase();
		try{
			act = Double.valueOf(val);
		}catch(NumberFormatException e){
			return "0.";
		}
		double time = 0.f;
		if(meaUnit.equalsIgnoreCase("cps")==true){
			time = 1.f;
		}else if(meaUnit.equalsIgnoreCase("cpm")==true){
			time = 60.f;
		}
		if(unit.equalsIgnoreCase("Bq")==true){		
			act = act*time;	
		}else if(unit.equalsIgnoreCase("kBq")==true){		
			act = act*1e3*time;
		}else if(unit.equalsIgnoreCase("MBq")==true){		
			act = act*1e6*time;			
		}else if(unit.equalsIgnoreCase("GBq")==true){			
			act = act*1e9*time;			
		}else{
			act = 0.f;
		}
		try{
			val = String.valueOf(act);
		}catch(NumberFormatException e){
			val = "0.";
		}
		return val;
	}
	
	private double _get_act_per_area(String meaUnit,String detArea){		
		double val = str2num(getActivityValue(meaUnit));
		return _get_val_per_area(val,detArea);
	}
	private double _get_val_per_area(double val,String detArea){
		double emit_area = str2num(getAreaValue());
		String area_unit = getAreaUnit();
		if(emit_area==0.f){
			return val;
		}
		double det_area = str2num(Const.getValOrUnit(detArea, false));
		String deta_unit = Const.getValOrUnit(detArea, true);
		if(deta_unit.equals(area_unit)!=true){		
			det_area = Const.convertArea(
				det_area,
				deta_unit,
				area_unit
			);
		}
		double ratio = det_area/emit_area;
		if(1.1<ratio){
			return -1.*val;//trick!!!!
		}else if(1<=ratio && ratio<=1.1){
			return val;
		}
		return (val*det_area)/emit_area;	
	}
	
	public String getSurface(){
		return info[SURFACE];
	}
	public String getSurfaceValue(){
		return Const.getValOrUnit(info[SURFACE],false);
	}
	public String getSurfaceUnit(){
		return Const.getValOrUnit(info[SURFACE],true);
	}
	public void setSurface(String txt){
		info[SURFACE] = txt;
	}
	
	/*private double _get_surf_per_area(String detArea){
		double val = str2num(getSurfaceValue());
		return _get_val_per_area(val,detArea);
	}*/

	public double eff_val = 1.;
	public String eff_unit = "cps";
	
	public double _prepare_strn_unit(String meaUnit,String detArea){		
		//emit_eff_val = _get_strn_value();
		eff_val = _get_act_per_area(meaUnit,detArea);
		eff_unit = _get_strn_base_unit();		
		return eff_val;
	}
	
	public double _prepare_surf_unit(String meaUnit,String detArea){
		
		eff_val = str2num(getSurfaceValue());
		eff_unit = getSurfaceUnit();

		double emitArea_val = str2num(getAreaValue());
		String emitArea_unit = getAreaUnit();

		double detArea_val = str2num(Const.getValOrUnit(detArea, false));
		String detArea_unit = Const.getValOrUnit(detArea, true);

		// step.1:check the time domain
		if(
			meaUnit.equals("cps")==true || 
			meaUnit.indexOf("sec")>=0
		){
			if(eff_unit.indexOf("min")>=0){
				eff_val = eff_val / 60.;
				eff_unit = eff_unit.replaceAll("min", "sec");
			}
		}else if(
			meaUnit.equals("cpm") == true || 
			meaUnit.indexOf("min") >= 0
		){
			if( eff_unit.indexOf("sec")>=0){
				eff_val = eff_val * 60.;
				eff_unit = eff_unit.replaceAll("sec", "min");
			}else if(eff_unit.indexOf("/s")>=0){
				eff_val = eff_val * 60.;
				eff_unit = eff_unit.replaceAll("/s", "/min");
			}
		}else if(meaUnit.indexOf("Bq")>=0){
			//'Bq' is equal to '1/sec'
			eff_val = _get_strn_value();
			eff_unit = "Bq";
		}else{
			return eff_val;
		}
		
		// step.2:check the area domain
		if(emitArea_val<=0 || detArea_val<=0){
			return eff_val;
		}
		if(
			meaUnit.equals("cps")==true || 
			meaUnit.equals("cpm")==true
		){
			if(meaUnit.length()==0){
				eff_val = eff_val / emitArea_val;
				eff_unit = Const.invertSlash(eff_unit);
				eff_unit = eff_unit + "/" + emitArea_unit;
				return eff_val;
			}
			emitArea_val = Const.convertArea(
				emitArea_val,
				emitArea_unit,
				detArea_unit
			);
			eff_val = (eff_val*detArea_val)/ emitArea_val;		
		}else{
			final String[] unit = {"cm","inch","mm"};
			for(int i=0; i<unit.length; i++){
				if(meaUnit.lastIndexOf(unit[i])>0){
					emitArea_val = Const.convertArea(
						emitArea_val,
						emitArea_unit,
						unit[i]
					);
					eff_val = eff_val / emitArea_val;
					eff_unit = eff_unit + "/"+unit[i]+"²";
					break;
				}
			}
		}
		return eff_val;
	}
	
	public String getSerial(){
		return info[SERIAL];
	}
	public void setSerial(String val){
		info[SERIAL] = val;
	}
	
	public String getCriterion(){
		return info[CRITERON];
	}
	public void setCriterion(String val){
		info[CRITERON] = val;
	}
	
	public String getFactorK(){
		return info[FACTOR_K];
	}
	public double getFactorKV(){
		double v = 0.f;
		try{
			v = Double.valueOf(info[FACTOR_K]);
		}catch(NumberFormatException e){
			v = 0.f;
		}
		return v;
	}
	public void setFactorK(String val){
		info[FACTOR_K] = val;
	}
	
	public String getFactorP(){
		return info[FACTOR_P];
	}
	public void setFactorP(String txt){
		info[FACTOR_P] = txt;
	}

	public String getUncertain(){
		return info[UNCERTAIN];
	}
	public double getUncertainVal(String range){
		return Double.valueOf(DEFAULT_UNCERTAIN);
	}
	public void setUncertain(String txt){
		info[UNCERTAIN] = txt;
	}
	
	private double str2num(String val){
		try{			
			if(val.length()==0){
				return -1;
			}
			return Double.valueOf(val);
		}catch(NumberFormatException e){
			return -2;
		}
	}
}
