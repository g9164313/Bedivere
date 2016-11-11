package nthu.hpclp.client.setting;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;

import gwt.material.design.client.ui.MaterialToast;
import narl.itrc.client.DlgBase;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemParam;

public class ColUpdate extends Column<ItemParam,String> implements 
	FieldUpdater<ItemParam,String>
{
	private GrdParam grd = null;
	
	public ColUpdate(GrdParam root) {
		super(new ButtonCell());
		setFieldUpdater(this);
		grd = root;
	}
	
	public static DlgModifyValue dlgValue = new DlgModifyValue();
	public static DlgModifyService dlgService = new DlgModifyService();
	public static DlgModifyEmitter dlgEmitter = new DlgModifyEmitter();
	private DlgBase<ItemParam> dialog = null;
	
	@Override
	public void update(int index, ItemParam object, String value) {
		String key = object.getKey();
		if(key.startsWith("SERVICE")==true){
			dialog = dlgService;
		}else if(key.startsWith("EMITTER")==true){
			dialog = dlgEmitter;
		}else{
			dialog = dlgValue;
		}
		dialog.appear(object, eventAction, null);
	}

	private ClickHandler eventAction = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			ItemParam itm = dialog.getTarget();
			itm.setVal(dialog.toString());
			Main.rpc.accessParam(
				Const.CMD_UPDATE, 
				dialog.getTarget(), 
				eventUpdate
			);
		}
	};
	
	private final AsyncCallback<ItemParam> eventUpdate = new AsyncCallback<ItemParam>(){
		@Override
		public void onFailure(Throwable caught) {
			String msg = caught.getMessage();
			MaterialToast.fireToast("內部錯誤："+msg,3000);
			System.out.println(msg);
			grd.data2grid();
		}
		@Override
		public void onSuccess(ItemParam result) {
			ItemParam itm = dialog.getTarget();
			itm.setVal(dialog.toString());
			grd.data2grid();
			//When user switch to another panel, it must refresh all environments!!!  
		}
	};
	
	@Override
	public String getValue(ItemParam object) {
		return "修改";
	}
};
