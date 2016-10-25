package nthu.hpclp.server;

import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ItemTenur;
import nthu.hpclp.shared.ParmEmitter;

public class DSrcProduct implements JRDataSource {

	private int idx = -1;
	
	public static ArrayList<ItemProdx> lst = new ArrayList<ItemProdx>();
	
	public DSrcProduct(){
	}
	
	@Override
	public Object getFieldValue(JRField arg0) throws JRException {
		ItemProdx prodx = lst.get(idx);		
		ItemTenur tenur = prodx.getTenur();
		String name = arg0.getName();
		Object res = common_field(prodx,name);			
		if(res!=null){
			return res;
		}
		switch(prodx.format){
		case ItemProdx.FMT_F2:
			if(tenur.isChamber()==true){
				res = field_chamber(prodx,name);				
			}else{
				res = field_other(prodx,name);
			}
			if(res!=null){
				return res;
			}
			break;
		case ItemProdx.FMT_F3:// γ反應報告
			res = field_gamma(prodx,name);
			if(res!=null){
				return res;
			}
			break;
		case ItemProdx.FMT_F4:// 污染效率報告
			res = field_effect(prodx,name);
			if(res!=null){
				return res;
			}
			break;
		case ItemProdx.FMT_F5:// 活度報告
			res = field_activity(prodx,name);
			if(res!=null){
				return res;
			}
			break;
		}
		if(name.equals("scribble_source")==true){
			return new DSrcScribble(prodx);				
		}
		return null;
	}

	@Override
	public boolean next() throws JRException {
		idx++;
		if(idx<lst.size()){
			return true;
		}
		return false;
	}

	private Object common_field(ItemProdx prodx,String name){
		ItemOwner owner = prodx.getOwner();
		ItemTenur tenur = prodx.getTenur();
		ParmEmitter emitter = prodx.getEmitter();
		if(name.equals("agreement0")){ return owner.getName(); }// 單位名稱
		if(name.equals("agreement1")){ return owner.getKey(); }// 單位代號
		if(name.equals("agreement2")){ return owner.getAddress(); }// 單位地址
		if(name.equals("expert0")  ){ return prodx.getKey(); }// 報告編號
		if(name.equals("expert1")  ){ return UtilsMisc.date2tw_d(prodx.stmp); }// 校正日期	
		if(name.equals("material0")){ return tenur.getDeviceVendor(); }//儀器廠牌
		if(name.equals("material1")){ return tenur.getDeviceSerial(); }//儀器型號
		if(name.equals("material2")){ return tenur.getDeviceNumber(); }//儀器序號
		if(name.equals("material3")){ return tenur.getDetectType(); }//偵檢器
		if(name.equals("material4")){ return tenur.getDetectSerial().trim(); }//偵檢器型號
		if(name.equals("material5")){ return tenur.getDetectNumber().trim(); }//偵檢器序號
		if(name.equals("material6")){ return tenur.getArea(); }
		if(name.equals("expert5")  ){ return prodx.getUnitRef(); }
		if(name.equals("expert5.1")){ return prodx.getUnitMea(); }
		if(name.equals("expert6")  ){ return prodx.getDistance(); }	
		if(name.equals("expert7")  ){ return emitter.getKind(); }//射源名稱		
		if(name.equals("expert8")  ){ return emitter.getArea(); }//射源面積
		if(name.equals("expert9")  ){ return emitter.getStrength(); }//射源活度
		if(name.equals("expert10") ){
			//表面發射率
			String mea_unit = prodx.getUnitMea();
			if(mea_unit.indexOf("Bq")>=0){
				return emitter.getStrengthNorm();
			}
			return emitter.getSurface(); 
		}
		if(name.equals("expert11") ){ return emitter.getCriterion(); }//射源保證書		
		if(name.equals("expert12") ){ return emitter.getFactorK(); }//射源的涵蓋因子
		if(name.equals("expert13") ){ return emitter.getFactorP(); }//射源的涵蓋機率
		if(name.equals("expert14") ){ return emitter.getSerial(); }//射源序號			
		if(name.equals("expert2")  ){ return prodx.getTemperature()+"  ℃"; }
		if(name.equals("expert3")  ){ return prodx.getPressure()+"  kPa"; }
		if(name.equals("expert4")  ){ return prodx.getHumidity()+"  %RH";	}		
		if(name.equals("print_day")){ return UtilsMisc.twToday(); }
		if(name.equals("use_logo") ){ return prodx.useLogo; }
		return null;
	}
	private String field_chamber(ItemProdx prodx,String name){
		ItemTenur tenur = prodx.getTenur();
		if(name.equals("title0")  ){ return "校正刻度"; }		
		if(name.equals("title1")  ){ return "參考值"; }
		if(name.equals("title1.1")){ return prodx.getUnitRef(); }		
		//if(name.equals("title2")){ return "不確定度"); }
		if(name.equals("title3")  ){ return "平均器示值"; }
		if(name.equals("title3.1")){ return prodx.getUnitMea(); }
		if(name.equals("title4")  ){ return "實際值"; }
		if(name.equals("title4.1")){ return prodx.getUnitMea(); }
		if(name.equals("title5")  ){ return "實驗平均值"; }
		if(name.equals("title5.1")){ return "相對不確定度"; }
		if(name.equals("title6")  ){ return "校正因子"; }
		if(name.equals("title7")  ){ return "相對"; }
		if(name.equals("title7.1")){ return "擴充不確定度"; }
		if(name.equals("expert15")){ return tenur.getFactor(); }//方向校正因子
		return null;
	}
	private String field_other(ItemProdx prodx,String name){
		if(name.equals("title0")  ){ return "校正刻度"; }
		if(name.equals("title1")  ){ return "參考值"; }
		if(name.equals("title1.1")){ return prodx.getUnitRef(); }
		//if(name.equals("title2")){ return "不確定度"; }
		if(name.equals("title3")  ){ return "平均器示值"; }
		if(name.equals("title3.1")){ return prodx.getUnitMea(); }
		if(name.equals("title4")  ){ return "實驗平均值"; }
		if(name.equals("title4.1")){ return "相對不確定度"; }
		if(name.equals("title5")  ){ return "校正因子";	}
		if(name.equals("title6")  ){ return "相對"; }
		if(name.equals("title6.1")){ return "擴充不確定度"; }
		return null;
	}
	private String field_gamma(ItemProdx prodx,String name){
		ItemTenur tenur = prodx.getTenur();
		if(name.equals("title0")  ){ return "校正刻度"; }
		if(name.equals("title1")  ){ return "參考值"; }
		if(name.equals("title1.1")){ return prodx.getUnitRef(); }
		//if(name.equals("title2")){ return "不確定度"; }
		if(name.equals("title3")  ){ return "平均器示值"; }
		if(name.equals("title3.1")){ return prodx.getUnitMea(); }
		if(name.equals("title4")  ){ return "實驗平均值"; }
		if(name.equals("title4.1")){ return "相對不確定度"; }
		if(name.equals("title5")  ){ return "反應"; }
		if(name.equals("title5.1")){
			return Const.insertSlash(
				prodx.getUnitMea(),
				prodx.getUnitRef()
			);
		}
		if(name.equals("title6")  ){ return "相對"; }
		if(name.equals("title6.1")){ return "擴充不確定度"; }
		if(name.equals("expert15")){ return tenur.getSteer(); }//廠商建議值
		return null;
	}
	private String field_effect(ItemProdx prodx,String name){	
		if(name.equals("title0")){ return "校正刻度"; }
		if(name.equals("titleEx")){ return "射源表面發射率"; }
		if(name.equals("title1")){
			if(prodx.getUnitMea().indexOf("Bq")>=0){
				return "射源單位面積活度";
			}
			return "含蓋射源表面發射率";
		}
		if(name.equals("title1.1")){ 
			return prodx.getEmitter().eff_unit;
		}
		//if(name.equals("title2")){ return "不確定度"); }
		if(name.equals("title3")){ return "平均器示值"; }
		if(name.equals("title3.1")){ return prodx.getUnitMea(); }
		if(name.equals("title4")  ){ return "實驗平均值"; }
		if(name.equals("title4.1")){ return "相對不確定度"; }
		if(name.equals("title5")){ return "平均背景值"; }
		if(name.equals("title5.1")){ return prodx.getUnitMea(); }
		//if(name.equals("title6")){ return "實驗標準差"; }
		if(name.equals("title7")){ return "表面偵測效率"; }
		if(name.equals("title7.1")){ 
			return Const.insertSlash(
				prodx.getUnitMea(),
				prodx.getEmitter().eff_unit
			); 
		}			
		if(name.equals("title8")  ){ return "相對"; }
		if(name.equals("title8.1")){ return "擴充不確定度"; }
		return null;
	}
	private String field_activity(ItemProdx prodx,String name){
		ParmEmitter emitter = prodx.getEmitter();
		String act_unit = null;
		String mea_unit = prodx.getUnitMea();			
		if(name.equals("title0")){ return "校正刻度"; }
		if(name.equals("title1")){
			if(emitter.eff_val<0){
				return "活度(涵蓋過大!!)";
			}
			return "校正射源活度";
		}			
		if(mea_unit.equals("cpm")==true){
			act_unit = "dpm";
		}else if(mea_unit.equals("cps")==true){
			act_unit = emitter.getStrengthUnit();
		}else {
			act_unit = "???";
		}			
		if(name.equals("title1.1")){ return act_unit; }			
		//if(name.equals("title2")){ return "不確定度"; }
		if(name.equals("title3")){ return "平均器示值"; }
		if(name.equals("title3.1")){ return mea_unit; }
		if(name.equals("title4")  ){ return "實驗平均值"; }
		if(name.equals("title4.1")){ return "相對不確定度"; }
		if(name.equals("title5")){ return "平均背景值"; }
		if(name.equals("title5.1")){ return mea_unit; }
		//if(name.equals("title6")){ return "實驗標準差"; }
		if(name.equals("title7")){ return "活度偵測效率"; }
		if(name.equals("title7.1")){ return 
			Const.insertSlash(
				mea_unit,
				act_unit
			);
		}
		if(name.equals("title8")  ){ return "相對"; }
		if(name.equals("title8.1")){ return "擴充不確定度"; }
		return null;
	}
}


