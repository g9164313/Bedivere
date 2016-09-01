package nthu.hpclp.server;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemProdx;

class DSrcScribble extends FieldScribble implements JRDataSource {
	
	protected int mFmt = 0;
	protected int totalField = 0;

	private String mDetType = "";
	private String mDetArea = "";
	private String mMeaUnit = "";

	public DSrcScribble(ItemProdx p){
		prodx = p;
		emitt = prodx.getEmitter();
		mFmt = prodx.format;
		
		mDetType = prodx.tenur.getDetectType();
		mDetArea = prodx.tenur.getArea();
		mMeaUnit = prodx.getUnitMea();

		if(prodx.format==ItemProdx.FMT_F4){
			emitt._prepare_surf_unit(mMeaUnit,mDetArea);
		}else if(prodx.format==ItemProdx.FMT_F5){
			emitt._prepare_strn_unit(mMeaUnit,mDetArea);
		}
		
		_init_factor();
		_init_scribble();
	}
	
	private void _init_factor(){
		double temp, press;
		try {
			temp = Double.valueOf(prodx.getTemperature());
		} catch (NumberFormatException e) {
			temp = 22.f;
		}
		try {
			press = Double.valueOf(prodx.getPressure());
		} catch (NumberFormatException e) {
			press = 101.3f;
		}
		mFactor = ((273.2f + temp) / (295.2)) * (101.3 / press);
	}
	
	private void _init_scribble() {
		if (prodx.scribble == null) {
			return;
		}
		Object[] data = prodx.scribble.toArray();
		totalField = data.length;
		mListValues = new String[data.length][];
		for (int i = 0; i < mListValues.length; i++) {
			String values = (String) data[i];
			String[] sub_val = values.split("@");
			if (sub_val.length == 0) {
				mListValues[i] = null;
				continue;
			}
			mListValues[i] = new String[sub_val.length];
			for (int j = 0; j < mListValues[i].length; j++) {
				mListValues[i][j] = sub_val[j];
			}
		}
	}
	
	@Override
	public Object getFieldValue(JRField arg0) throws JRException {

		String name = arg0.getName();
		String ans = mListValues[mIndex][0];
		if(ans!=null){
			if (ans.contains(Const.SPECIAL_PREFIX) == true) {
				if (name.equals("field0") == true) {
					return ans;
				}
				return "";
			}
		}
		if (name.equals("field0") == true) {
			String vals = mListValues[mIndex][0];
			if (vals == null) {
				return " ";
			}
			return vals;
		}

		switch (mFmt) {
		case ItemProdx.FMT_F1V:
		case ItemProdx.FMT_F1W:
			return _field_f1(name);
		case ItemProdx.FMT_F2:
			if (mDetType.indexOf("游離腔")>=0) {
				return _field_f2_chamer(name);
			}
			return _field_fx(name,false);
		case ItemProdx.FMT_F3:
			return _field_fx(name,true);
		case ItemProdx.FMT_F4:
			return _field_f4(name);
		case ItemProdx.FMT_F5:
			return _field_f5(name);
		}
		return ans;
	}

	@Override
	public boolean next() throws JRException {
		mIndex++;
		if (mIndex < totalField) {
			calculating();
			return true;
		}
		return false;
	}
}


