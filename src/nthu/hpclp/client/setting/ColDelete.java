package nthu.hpclp.client.setting;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;

import gwt.material.design.client.ui.MaterialToast;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemParam;

public class ColDelete extends Column<ItemParam,String> implements 
	FieldUpdater<ItemParam,String>
{
	private GrdParam grd = null;
	
	public ColDelete(GrdParam root) {
		super(new ButtonCell());
		setFieldUpdater(this);
		grd = root;
	}
	
	@Override
	public void update(final int index, ItemParam object, String value) {
		final AsyncCallback<ItemParam> event = new AsyncCallback<ItemParam>(){
			@Override
			public void onFailure(Throwable caught) {
				String msg = caught.getMessage();
				MaterialToast.fireToast("內部錯誤："+msg,3000);
				System.out.println(msg);
			}
			@Override
			public void onSuccess(ItemParam result) {
				grd.datum.remove(index);
				grd.data2grid();
			}
		};
		//how to get user confirm???
		Main.rpc.accessParam(Const.CMD_DELETE, object, event);
	}
	
	@Override
	public String getValue(ItemParam object) {
		return "刪除";
	}
};