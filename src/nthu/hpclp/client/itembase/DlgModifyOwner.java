package nthu.hpclp.client.itembase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import narl.itrc.client.DlgBase;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemOwner;

public class DlgModifyOwner extends DlgBase<ItemOwner> {

	private static DlgModifyOwnerUiBinder uiBinder = GWT.create(DlgModifyOwnerUiBinder.class);

	interface DlgModifyOwnerUiBinder extends UiBinder<Widget, DlgModifyOwner> {
	}

	@UiField(provided=true) 
	MaterialModal root = _dlg_root;
	@UiField(provided=true)
	MaterialButton btnAction = _btn_action;
	@UiField(provided=true)
	MaterialButton btnCancel = _btn_cancel;

	@UiField
	MaterialTextBox boxKey,boxName,boxStmp;
	@UiField
	MaterialTextBox boxZip,boxAddr,boxPhon;
	@UiField
	MaterialTextBox boxDept,boxPern,boxMail,boxMemo;
	
	public DlgModifyOwner() {
		initWidget(uiBinder.createAndBindUi(this));
		chainBox(
			boxKey,boxName,boxStmp,
			boxZip,boxAddr,boxPhon,
			boxDept,boxPern,boxMail,
			boxMemo
		);
	}

	@UiHandler("boxKey")
	public void onChangeKey(ValueChangeEvent<String> event) {
		String val = event.getValue();		
		if(val.charAt(0)!='+'){
			return;
		}
		//plus sign is a special operation, we will ask server
		Main.rpc.genKey(
			Const.OWNER+val, new AsyncCallback<String>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("內部錯誤");
			}
			@Override
			public void onSuccess(String o_key) {
				boxKey.setText(o_key);
			}				
		});		
	}
	
	@UiHandler("boxStmp")
	public void onChangeMeet(ValueChangeEvent<String> event) {
		//check ths stamp is valid~~~~
		Main.text2stmp(boxStmp,target.stmp);
	}
	
	@Override
	public void eventAppear(ItemOwner item) {
		boxKey.setFocus(true);
		
		boxKey.setText(item.getKey());		
		boxName.setText(item.getName());
		boxStmp.setText(Main.fmtStmpLast.format(item.stmp));
		
		boxZip.setText(item.getZip());
		boxAddr.setText(item.getAddress());
		boxPhon.setText(item.getPhone());
		
		boxDept.setText(item.getDepartment());
		boxPern.setText(item.getPerson());
		boxMail.setText(item.getEMail());
		boxMemo.setText(item.getMemo());
		
		if(item.isNewone()==true){
			btnAction.setText("新增");
		}else if(item.isModify()==true){
			btnAction.setText("修改");
		}else if(item.isDelete()==true){
			btnAction.setText("刪除");
		}else{
			btnAction.setText("確認");
		}		
	}

	@Override
	public void takeAction(ClickEvent event) {
		ItemOwner item = target;
		//mapping box to variable~~~
		item.setKey(boxKey.getText());		
		item.setName(boxName.getText());
		
		item.setZip(boxZip.getText());
		item.setAddress(boxAddr.getText());
		item.setPhone(boxPhon.getText());
		
		item.setDepartment(boxDept.getText());
		item.setPerson(boxPern.getText());
		item.setEMail(boxMail.getText());
		item.setMemo(boxMemo.getText());
				
		Main.rpc.modifyOwner(
			item,new AsyncCallback<ItemOwner>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("無法更新資料");
			}
			@Override
			public void onSuccess(ItemOwner result) {
				if(result==null){
					MaterialToast.fireToast("不明原因錯誤");
					return;
				}
				target = result;
				MaterialToast.fireToast("已更新 "+result.getKey());
				dialog_done();
			}
		});
	}

	@Override
	public void takeCancel(ClickEvent event) {
	}
}

