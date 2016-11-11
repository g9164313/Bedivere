package nthu.hpclp.client.setting;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;

import gwt.material.design.client.ui.MaterialToast;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemParam;

public class ColInsert extends Column<ItemParam,String> implements 
	FieldUpdater<ItemParam,String>
{
	private GrdParam grd = null;
	
	public ColInsert(GrdParam root) {
		super(new ButtonCell());
		setFieldUpdater(this);
		grd = root;
		init_prefix();		
	}
	
	private String prefix = "";
	
	private void init_prefix(){
		if(grd.datum.isEmpty()==true){
			return;
		}
		prefix = grd.datum.get(0).getKey();
		int pos = prefix.indexOf('_');
		if(pos>=0){
			prefix = prefix.substring(0, pos+1);
		}
	}
	
	private String gen_key_val(){
		int beg = prefix.length();
		int maxVal = 0;
		for(ItemParam itm:grd.datum){
			String txt = itm.getKey().substring(beg);
			try{
				int val = Integer.valueOf(txt);
				if(val>=maxVal){
					maxVal = val + 1;
				}
			}catch(NumberFormatException e){
				continue;
			}
		}		
		return prefix+maxVal;
	}

	@Override
	public void update(int index, ItemParam object, String value) {
		ItemParam itm = new ItemParam(object);
		itm.setKey(gen_key_val());
		Main.rpc.accessParam(Const.CMD_INSERT, itm, eventInsert);
	}
	
	private final AsyncCallback<ItemParam> eventInsert = new AsyncCallback<ItemParam>(){
		@Override
		public void onFailure(Throwable caught) {
			String msg = caught.getMessage();
			MaterialToast.fireToast("內部錯誤："+msg,3000);
			System.out.println(msg);
			grd.data2grid();
		}
		@Override
		public void onSuccess(ItemParam result) {
			grd.datum.add(0,result);
			grd.data2grid();
			//When user switch to another panel, it must refresh all environments!!! 
		}
	};

	@Override
	public String getValue(ItemParam object) {
		return "新增";
	}
}
