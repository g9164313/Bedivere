package narl.hpclp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import narl.hpclp.shared.ItemOwner;

public class DlgItemOwner extends DlgItem {

	private static DlgItemOwnerUiBinder uiBinder = GWT.create(DlgItemOwnerUiBinder.class);

	interface DlgItemOwnerUiBinder extends UiBinder<Widget, DlgItemOwner> {
	}

	@UiField
	MaterialModal root;
	
	@UiField
	MaterialButton btnAction,btnCancel;

	@UiField
	MaterialTextBox boxKey,boxName;
	
	@UiField
	MaterialTextBox boxZip,boxAddr,boxPhon;
	
	@UiField
	MaterialTextBox boxDept,boxPern,boxMail,boxMemo;
	
	public DlgItemOwner() {
		initWidget(uiBinder.createAndBindUi(this));
		refxWidget(root,btnAction,btnCancel);		
	}

	private ItemOwner item = null;

	public DlgItem appear(ItemOwner itm,Runnable event){
		item = itm;
		//mapping variable to box~~~
		boxKey.setText(item.getKey());
		boxZip.setText(item.getZip());
		boxName.setText(item.getName());		
		boxAddr.setText(item.getAddress());
		boxPhon.setText(item.getPhone());
		boxDept.setText(item.getDepartment());
		boxPern.setText(item.getPerson());
		boxMail.setText(item.getEMail());
		boxMemo.setText(item.getMemo());
		return appear(itm.uuid,event);
	}

	@Override
	void takeAction(ClickEvent event) {
		//mapping box to variable~~~
		item.setKey(boxKey.getText());
		item.setZip(boxZip.getText());
		item.setName(boxName.getText());		
		item.setAddress(boxAddr.getText());
		item.setPhone(boxPhon.getText());
		item.setDepartment(boxDept.getText());
		item.setPerson(boxPern.getText());
		item.setEMail(boxMail.getText());
		item.setMemo(boxMemo.getText());
		Main.rpc.modifyOwner(item,eventModify);
	}
	
	private AsyncCallback<ItemOwner> eventModify = 
		new AsyncCallback<ItemOwner>()
	{
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
			MaterialToast.fireToast("已更新 "+result.getKey());
			result.copyTo(item);
			dlgRoot.closeModal();
			handleClose();
		}
	};
}
