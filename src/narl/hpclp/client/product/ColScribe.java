package narl.hpclp.client.product;

import narl.hpclp.shared.FormulaSet;
import narl.hpclp.shared.ItemProdx;

import com.google.gwt.user.cellview.client.TextColumn;

class ColScribe extends TextColumn<String>{
	
	public final static int MODE_NONE = -1;
	public final static int MODE_AVG = 0;
	public final static int MODE_DEV = 1;
	public final static int MODE_AVGDEV = 2;
	public final static int MODE_UNCX= 3;
	public final static int MODE_FACT= 4;
	
	private int idx;
	private int mod = MODE_NONE;
	
	public ColScribe(int i){
		idx = i;
	}
	public ColScribe(int i,int m){
		idx = i;
		mod = m;
	}
	
	@Override
	public String getValue(String vals) {
		ItemProdx itm = PanMain.curProdx;
		if(itm==null){
			return "";
		}
		
		String[] val = vals.split("@");
		if(mod==MODE_NONE){
			if(idx>=val.length){
				return "---";
			}
			return val[idx];
		}
		if(val[0].indexOf("調整")>=0){
			return "---";
		}

		switch(mod){
		case MODE_AVG:				
			return FormulaSet.avg(val[idx],0,0.);
		case MODE_DEV:
			return FormulaSet.dev(val[idx],0,0.);
		case MODE_AVGDEV:
			String v1 = FormulaSet.avg(val[idx],0,0.);
			String v2 = FormulaSet.dev(val[idx],0,0.);
			if(v2.length()==0){
				return v1;
			}
			return v1+"±"+v2;
		case MODE_UNCX:
			return FormulaSet.xxx(val[idx],0,0.);
		case MODE_FACT:
			switch(PanMain.curProdx.format){
			case ItemProdx.FMT_F1V:
			case ItemProdx.FMT_F1W:
			case ItemProdx.FMT_F2:
				return FormulaSet.getFactor(val[1],val[2]);
			case ItemProdx.FMT_F3:
				return FormulaSet.getResponse(val[1],val[2]);
			case ItemProdx.FMT_F4:
				return FormulaSet.getEffect(
					val[1],val[2], 
					itm.getEmitter()._prepare_surf_unit(
						itm.getUnitMea(), 
						itm.tenur.getArea()
					)
				);
			case ItemProdx.FMT_F5:
				return FormulaSet.getEffect(
					val[1],val[2], 
					itm.getEmitter()._prepare_strn_unit(
						itm.getUnitMea(), 
						itm.tenur.getArea()
					)
				);
			}
			return "fmt?";			
		}
		return "???";
	}
};
