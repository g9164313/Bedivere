package narl.hpclp.server;

import java.math.BigDecimal;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class Formular {

	public static SummaryStatistics getStat(String txt){
		SummaryStatistics stat = new SummaryStatistics();
		if(txt.length()==0){
			return stat;
		}
		String[] val = txt.split(",");
		if(val.length==0){
			return stat;
		}
		for(int i=0; i<val.length; i++){			
			if(val[i]==null){
				continue;
			}			
			if(val[i].length()==0){
				continue;
			}
			try{			
				stat.addValue(Double.valueOf(val[i]));	
			}catch(NumberFormatException e){
				System.err.print("fail to parse "+val[i]);
				continue;
			}
		}
		return stat;
	}
	
	public static String _trim_num_by(
		int estimate,
		double round,
		double value		
	){	
		BigDecimal val = new BigDecimal(value);
		if(round==0.f){
			val.movePointRight(3);
			val = val.divide(
				BigDecimal.ONE, 
				0, 
				BigDecimal.ROUND_HALF_UP
			);
			val.movePointLeft(3);
			return val.toString();
		}
		int pos = _first_nonzero_in_fraction(round);
		if(estimate<=0){
			return "estimate??";
		}		
		pos = pos + estimate;
		if(pos>1){
			pos = pos -1;
		}
		val = val.movePointRight(pos);
		
		val = _round_up_even(val);
			
		String res = val.movePointRight(-1*pos).toString();
		if(res.indexOf('.')<0){
			res = res + '.';
		}
		return res;
	}
	
	public static int _first_nonzero_in_fraction(double val){		
		if(val==0.f){
			return 0;
		}
		BigDecimal vv = new BigDecimal(val);
		BigDecimal digi = null;
		int pos = 0;
		if(val<1.f){
			do{
				vv = vv.movePointRight(1);				
				digi = vv.divide(
					BigDecimal.ONE, 
					0, 
					BigDecimal.ROUND_DOWN
				);
				pos++;
			}while(digi.equals(BigDecimal.ZERO)==true);	
		}else{
			do{
				vv = vv.movePointLeft(1);				
				digi = vv.divide(
					BigDecimal.ONE, 
					0, 
					BigDecimal.ROUND_DOWN
				);
				pos--;
			}while(digi.equals(BigDecimal.ZERO)==false);			
		}
		return pos;
	}
	
	private static BigDecimal _round_up_even(BigDecimal val){		
		//find the digital number for checking whether we got a number lower than 5
		int digi = val.subtract(val.divideToIntegralValue(BigDecimal.ONE))
			.movePointRight(1)
			.divideToIntegralValue(BigDecimal.ONE)
			.intValue();
		
		if(digi<5){
			val = val.divide(
				BigDecimal.ONE, 
				0, 
				BigDecimal.ROUND_DOWN
			);
		}else if(digi==5){
			digi = val.divide(
				BigDecimal.ONE, 
				0, 
				BigDecimal.ROUND_DOWN
			).intValue();
			if(digi%2==0){
				val = val.divide(
					BigDecimal.ONE, 
					0, 
					BigDecimal.ROUND_DOWN
				);
			}else{
				val = val.divide(
					BigDecimal.ONE, 
					0, 
					BigDecimal.ROUND_HALF_UP
				);
			}
		}else if(5<digi){
			val = val.divide(
				BigDecimal.ONE, 
				0, 
				BigDecimal.ROUND_HALF_UP
			);
		}
		return val;
	}
	
	public static String _effect_by_round(	
		double bkg,
		double mea,		
		double factory
	){
		if(factory==0.f){
			return "inf.";
		}
		factory = (mea-bkg)/factory;
		return _match_percent(3,factory);
	}
	
	public static String _match_percent(
		int keep,
		double value
	){
		if(value==0.f){
			return "0";
		}
		BigDecimal val = new BigDecimal(value);
		int pos = _first_nonzero_in_fraction(value);
		if(pos<0){
			pos = pos + keep;
			val = val.movePointRight(pos);
			val = _round_up_even(val);
			val = val.movePointLeft(pos);
			return val.toString();
		}		
		pos = pos + keep - 1;
		val = val.movePointRight(pos);
		val = _round_up_even(val);
		val = val.movePointLeft(pos-2);
		return val.toString()+'%';
	}

	private static BigDecimal _round_up_skip(BigDecimal val){
		String tmp =val.toString();
		int pos = tmp.indexOf('.');
		char digi;
		if(pos<0){
			pos = tmp.length();
		}
		digi = tmp.charAt(pos+1);
		if(digi!='0'){
			val = val.divide(
				BigDecimal.ONE, 
				0, 
				BigDecimal.ROUND_UP
			);
		}else{
			val = val.divide(
				BigDecimal.ONE, 
				0, 
				BigDecimal.ROUND_DOWN
			);
		}
		return val;
	}
	
	public static String _match_percent_up(
		int keep,
		double value
	){
		if(value==0.f){
			return "0";
		}
		BigDecimal val = new BigDecimal(value);
		int pos = _first_nonzero_in_fraction(value);
		if(pos<0){
			pos = pos + keep;
			val = val.movePointRight(pos);
			val = _round_up_skip(val);
			val = val.movePointLeft(pos);
			return val.toString();
		}		
		pos = pos + keep - 1;
		val = val.movePointRight(pos);
		val = _round_up_skip(val);
		val = val.movePointLeft(pos-2);
		return val.toString()+'%';
	}
	
	public static String getFactorOrResponse(
		double mRef,
		double mMea,
		boolean is_response
	){
		double ans;	
		if(is_response==true){
			//反應
			if(mRef==0.f){
				return "inf.";
			}
			ans = mMea/mRef;
			//return _match_percent(3,ans);
		}else{
			//校正因子
			if(mMea==0.f){
				return "inf.";
			}
			ans = mRef/mMea;			
		}
		BigDecimal val = new BigDecimal(ans);
		int keep = 2;
		val = val.movePointRight(keep);
		val = _round_up_even(val);
		val = val.movePointLeft(keep);
		return val.toString();
	}
}
