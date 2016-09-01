package nthu.hpclp.server;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ParmEmitter;

public class FieldScribble {
	
	protected int mIndex = -1;		
	protected String[][] mListValues = null;
	
	protected ItemProdx prodx;
	protected ParmEmitter emitt;
		
	protected double mFactor = 1.f;

	public FieldScribble(){		
	}
	
	protected String _field_f1(String name) {
		return "no support";
	}

	protected Object _field_f2_chamer(String name) {
		
		double round = xxx_all*mea_avg*mFactor;
		
		if (name.equals("field1") == true) {
			
			// 參考值
			if(ref_cnt==1.f){
				return formatStrNum(mListValues[mIndex][1],dec,fac);
			}
			return formatStrNum(
				Formular._trim_num_by(2,round,ref_avg),
				dec,fac
			);
			
		} else if (name.equals("field2") == true) {
			
			// 不確定度
			return "none";
			
		} else if(name.equals("field3")==true){
			
			// 器示值
			return formatStrNum(
				Formular._trim_num_by(2,round,mea_avg),
				dec,fac
			);
			
		} else if(name.equals("field4")==true){
			
			// 實際值
			return formatStrNum(
				Formular._trim_num_by(2,round,mea_avg*mFactor),
				dec,fac
			);
			
		} else if (name.equals("field5") == true) {
			
			// 相對平均實驗標準差
			return formatAvgDev(xxx_inst);
			
		} else if (name.equals("field6") == true) {
			
			// 校正因子
			return formatStrNum(
				Formular.getFactorOrResponse(ref_avg,mea_avg*mFactor,false),
				dec,fac
			);
			
		} else if (name.equals("field7") == true) {
			
			// 相對擴充不確定度
			return formatXXX(xxx_all);
		}
		return " ";
	}

	protected Object _field_fx(String name, boolean is_response) {
		double round = xxx_all*mea_avg;
		if (name.equals("field1") == true) {
			
			// 參考值			
			if(ref_cnt==1.f){
				return formatStrNum(mListValues[mIndex][1],dec,fac);
			}
			return formatStrNum(
				Formular._trim_num_by(2,round,ref_avg),
				dec,fac
			);
			
		} else if (name.equals("field2") == true) {
			
			// 不確定度
			return "none";
			
		} else if (name.equals("field3") == true) {
			
			// 器示值
			return formatStrNum(
				Formular._trim_num_by(2,round,mea_avg),
				dec,fac
			);
			
		} else if (name.equals("field4") == true) {
			
			// 相對平均實驗標準差
			return formatAvgDev(xxx_inst);
			
		} else if (name.equals("field5") == true) {
			
			// 反應值 or 校正因子
			return formatStrNum(
				Formular.getFactorOrResponse(ref_avg,mea_avg,is_response),
				dec,fac
			);	
			
		} else if (name.equals("field6") == true) {
			
			// 相對擴充不確定度
			return formatXXX(xxx_all);
		}
		return "";
	}

	protected Object _field_f4(String name) {
		double round = xxx_all*mea_avg;	
		if (name.equals("field1") == true) {
			
			// 涵蓋射源表面發射率
			return formatStrNum(
				Formular._trim_num_by(2,round,emitt.eff_val),
				dec,fac
			);
			
		} else if (name.equals("field3") == true) {
			
			// 平均器示值
			return formatStrNum(
				Formular._trim_num_by(2,round,mea_avg),
				dec,fac
			);
			
		} else if (name.equals("field4") == true) {
			
			// 相對平均實驗標準差
			return formatAvgDev(xxx_inst);
			
		} else if (name.equals("field5") == true) {
			
			// 平均背景值
			round = xxx_all*ref_avg;
			if(ref_cnt==1.f){
				return formatStrNum(mListValues[mIndex][1],dec,fac);
			}
			return formatStrNum(
				Formular._trim_num_by(2,round,ref_avg),
				dec,fac
			);
			
		} else if (name.equals("field7") == true) {
			
			// 表面偵測效率
			return Formular._effect_by_round(						
				ref_avg,
				mea_avg,			
				emitt.eff_val
			);
		} else if (name.equals("field8") == true) {
			
			// 相對擴充不確定度
			return formatXXX(xxx_all);
		}
		return "";
	}

	protected Object _field_f5(String name) {
		double round = xxx_all*mea_avg;
		if (name.equals("field1")==true){
			
			// 射源活度
			//round = xxx_all*mea_avg;
			return formatStrNum(
				Formular._trim_num_by(2,ref_dev,Math.abs(emitt.eff_val)),
				dec,fac
			);	
			
		} else if(name.equals("field3")==true){
			
			// 平均器示值
			round = xxx_all*mea_avg;
			return formatStrNum(
				Formular._trim_num_by(2,round,mea_avg),
				dec,fac
			);
			
		} else if(name.equals("field4")==true){
			
			// 相對平均實驗標準差
			return formatAvgDev(xxx_inst);
			
		} else if(name.equals("field5")==true){
			
			// 平均背景值
			if(ref_cnt==1.f){
				return formatStrNum(mListValues[mIndex][1],dec,fac);
			}
			return formatStrNum(
				Formular._trim_num_by(2,ref_dev,ref_avg),
				dec,fac
			);
			
		} else if(name.equals("field7")==true){
			
			// 活度偵測效率
			return Formular._effect_by_round(
				ref_avg,
				mea_avg,			
				Math.abs(emitt.eff_val)
			);
			
		} else if (name.equals("field8") == true) {
			
			// 相對擴充不確定度
			return formatXXX(xxx_all);
		}
		return "";
	}
	//-------------------------------//
	
	private double ref_cnt,ref_avg,ref_dev;	
	private double mea_cnt,mea_avg,mea_dev;
	
	private double xxx_inst;//the uncertain of instrument
	private double xxx_emit;//the uncertain of radiation
	private double xxx_all;//the uncertain of instrument and radiation
	
	protected void calculating(){	
		int size = mListValues[mIndex].length;
		if(size<3){
			//reset all values~~
			ref_cnt = ref_avg = ref_dev = 1.;
			mea_cnt = mea_avg = mea_dev = 1.;
			xxx_inst = xxx_emit = xxx_all = 0.;
			return;
		}
		
		SummaryStatistics stat;
		
		stat = Formular.getStat(mListValues[mIndex][1]);
		ref_cnt = stat.getN();
		ref_avg = stat.getMean();
		ref_dev = Math.sqrt(stat.getVariance());
		
		stat = Formular.getStat(mListValues[mIndex][2]);	
		mea_cnt = stat.getN();
		mea_avg = stat.getMean();
		mea_dev = Math.sqrt(stat.getVariance());
		
		xxx_inst = (mea_dev/Math.sqrt(mea_cnt))/mea_avg;
		xxx_emit = emitt.getUncertainVal(mListValues[mIndex][2]);	
		
		xxx_all = Math.sqrt(xxx_inst*xxx_inst + xxx_emit*xxx_emit);
		xxx_all = xxx_all * emitt.getFactorKV();
	}
	//-------------------------------//
	
	private static final int dec = 5;//the text space of number
	private static final int fac = 3;//the text space of number
	
	private static String formatAvgDev(double val){
		return formatStrNum(
			Formular._match_percent_up(2,val),
			dec,fac
		);
	}
	
	private static String formatXXX(double val){
		return formatStrNum(
			Formular._match_percent_up(2,val),
			dec,fac
		);
	}
	
	private static String formatStrNum(
		String txt,
		int deciNum, int fracNum
	){
		String deci = "";
		String frac = "";
		//step.1 find the point~~~
		int pos = txt.indexOf('.');
		if(pos<0){
			deci = txt.substring(0, txt.length());			
		}else{
			deci = txt.substring(0, pos);
			frac = txt.substring(pos+1, txt.length());
		}
		//padding the decimal
		if(deciNum>deci.length()){
			deciNum = deciNum-deci.length();
			for(int i=0; i<deciNum; i++){
				deci = ' '+deci;
			}
		}
		if(pos<0){
			return deci;
		}
		//padding the fraction
		if(fracNum>frac.length()){
			fracNum = fracNum-frac.length();
			for(int i=0; i<fracNum; i++){
				frac = frac+' ';
			}
		}
		return deci+"."+frac;
	}
}
