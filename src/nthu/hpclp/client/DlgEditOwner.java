package nthu.hpclp.client;

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
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemOwner;

public class DlgEditOwner extends DlgBase<ItemOwner> {

	private static DlgEditOwnerUiBinder uiBinder = GWT.create(DlgEditOwnerUiBinder.class);

	interface DlgEditOwnerUiBinder extends UiBinder<Widget, DlgEditOwner> {
	}

	@UiField
	MaterialModal root;
	
	@UiField
	MaterialButton btnAction,btnCancel;

	@UiField
	MaterialTextBox boxKey,boxName,boxStmp;
	
	@UiField
	MaterialTextBox boxZip,boxAddr,boxPhon;
	
	@UiField
	MaterialTextBox boxDept,boxPern,boxMail,boxMemo;
	
	public DlgEditOwner() {
		initWidget(uiBinder.createAndBindUi(this));
		refxWidget(root,btnAction,btnCancel);
		chainBox(
			boxKey,boxName,boxStmp,
			boxZip,boxAddr,boxPhon,
			boxDept,boxPern,boxMail,
			boxMemo
		);
	}

	@Override
	public void onEventShow() {
	}

	@Override
	public void onEventHide() {
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
			public void onSuccess(String result) {
				boxKey.setText(result);
			}				
		});		
	}
	
	@UiHandler("boxStmp")
	public void onChangeMeet(ValueChangeEvent<String> event) {
		//check ths stamp is valid~~~~
		Main.text2stmp(boxStmp,target.stmp);
	}
	
	@Override
	void eventAppear(ItemOwner item) {
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
		
		btnAction.setText(add_or_edit(item.uuid));
	}

	@Override
	void takeAction(ClickEvent event) {
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
	void takeCancel(ClickEvent event) {
	}
}

