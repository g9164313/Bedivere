package nthu.hpclp.shared;

import java.math.BigDecimal;

public final class FormulaSet {

	private static double[] lst2val(String lst){
		String[] val = lst.split(",");
		try{
			//check again~~~~
			for(int i=0; i<val.length; i++){
				Double.valueOf(val[i]);
			}
		}catch(NumberFormatException e){
			return null;
		}
		double[] res = new double[val.length];
		for(int i=0; i<val.length; i++){
			res[i] = Double.valueOf(val[i]);
		}
		return res;
	}

	public static String avg(String lst,int round,double esti) {
		double[] val = lst2val(lst);
		if(val==null){
			return "無資料";
		}
		if(val.length==1){
			return lst;
		}
		return trimVal(_avg(val),round,esti);
	}

	private static double _avg(double[] val) {
		double sum=0.;
		for(int i=0; i<val.length; i++){
			sum = sum + val[i];
		}
		return sum/val.length;
	}
	
	public static double _var(double[] val){
		double m = _avg(val);
		double n = val.length;
		double sum = 0.;
		for(int i=0; i<val.length; i++){
			sum = sum + (val[i]-m)*(val[i]-m);
		}			
		return Math.sqrt(sum/(n-1));
	}
	
	public static String dev(String lst,int round,double esti) {
		double[] val = lst2val(lst);
		if(val==null){
			return "無資料";
		}
		if(val.length<=1){
			return "---";
		}
		return trimVal(_var(val),round,esti);
	}
	
	public static String AvgDev(String lst) {
		double[] val = lst2val(lst);
		if(val==null){
			return "無資料";
		}
		double avg,dev;
		String txt;
		if(val.length==1){
			avg = _avg(val);
			txt = trimVal(avg,0,0.);//default is 3 digital
		}else{
			avg = _avg(val);
			dev = _var(val);
			//default is 3 digital
			txt = trimVal(avg,0,0.)+" ±"+trimVal(dev,0,0.);
		}
		return txt;
	}
		
	public static String xxx(String lst,int round,double esti) {
		double[] val = lst2val(lst);
		if(val==null){
			return "無資料";
		}
		if(val.length<=1){
			return "---";
		}
		double avg = _avg(val);
		double dev = _var(val);
		double xxx = dev/(avg*Math.sqrt(val.length));
		return trimVal(xxx,round,esti); 
	}

	public static String getFactor(
		String refLst,
		String meaLst
	){
		double[] ref = lst2val(refLst);
		if(ref==null){
			return "ERROR-ref";
		}
		double _ref = _avg(ref);
		
		double[] mea = lst2val(meaLst);
		if(mea==null){
			return "ERROR-mea";
		}
		double _mea = _avg(mea);
		
		double ans;//校正因子
		if(_mea==0.f){
			return "inf.";
		}
		ans = _ref/_mea;

		final int keep = 2;
		BigDecimal val = new BigDecimal(ans);
		val = val.movePointRight(keep);
		val = _round_up_even(val);
		val = val.movePointLeft(keep);
		return val.toString();
	}

	public static String getResponse(
		String refLst,
		String meaLst
	){
		double[] ref = lst2val(refLst);
		if(ref==null){
			return "ERROR-ref";
		}
		double _ref = _avg(ref);
		
		double[] mea = lst2val(meaLst);
		if(mea==null){
			return "ERROR-mea";
		}
		double _mea = _avg(mea);
			
		double ans;//反應
		if(_ref==0.f){
			return "inf.";
		}
		ans = _mea/_ref;
	
		final int keep = 2;
		BigDecimal val = new BigDecimal(ans);
		val = val.movePointRight(keep);
		val = _round_up_even(val);
		val = val.movePointLeft(keep);
		return val.toString();
	}
	
	public static String getEffect(	
		String bkgLst,
		String meaLst,		
		double factory
	){
		double[] bkg = lst2val(bkgLst);
		if(bkg==null){
			return "ERROR-bkg";
		}
		double _bkg = _avg(bkg);
		
		double[] mea = lst2val(meaLst);
		if(mea==null){
			return "ERROR-mea";
		}
		double _mea = _avg(mea);
		
		if(factory==0.f){
			return "inf.";
		}
		factory=(_mea-_bkg)/factory;
		return _match_percent(3,factory);
	}

	private static String _match_percent(
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
	
	public static String trimVal(double value,int round,double esti) {
		BigDecimal val = new BigDecimal(value);
		if (esti==0.f) {
			if(round<=0){
				round = 3;//default;
			}
			val = val.movePointRight(round);
			val = val.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP);
			val = val.movePointLeft(round);
			return val.toString();
		}
		
		int pos = _first_nonzero_in_fraction(esti);
		pos = pos + round;
		if (pos > 1) {
			pos = pos - 1;
		}
		val = val.movePointRight(pos);

		val = _round_up_even(val);

		String res = val.movePointRight(-1 * pos).toString();
		if (res.indexOf('.') < 0) {
			res = res + '.';
		}
		return res;
	}
	
	public static  int _first_nonzero_in_fraction(double val) {
		BigDecimal vv = new BigDecimal(val);
		BigDecimal dg;
		int pos = 0;
		if (val < 1.f) {
			do {
				vv = vv.movePointRight(1);
				dg = vv.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN);
				pos++;
			} while (dg.equals(BigDecimal.ZERO) == true);
		} else {
			do {
				vv = vv.movePointLeft(1);
				dg = vv.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN);
				pos--;
			} while (dg.equals(BigDecimal.ZERO) == false);
		}
		return pos;
	}

	private static  BigDecimal _round_up_even(BigDecimal val) {
		// find the digital number for checking whether we got a number lower
		// than 5
		int digi = val.subtract(val.divideToIntegralValue(BigDecimal.ONE))
			.movePointRight(1)
			.divideToIntegralValue(BigDecimal.ONE).intValue();

		if (digi < 5) {
			val = val.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN);
		} else if (digi == 5) {
			digi = val.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN).intValue();
			if (digi%2==0) {
				val = val.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN);
			} else {
				val = val.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP);
			}
		} else if (5 < digi) {
			val = val.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP);
		}
		return val;
	}	
}


