package narl.hpclp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import narl.hpclp.shared.ItemTenur;

public class DlgItemTenur extends DlgItem {

	private static DlgItemTenurUiBinder uiBinder = GWT.create(DlgItemTenurUiBinder.class);

	interface DlgItemTenurUiBinder extends UiBinder<Widget, DlgItemTenur> {
	}

	@UiField
	MaterialModal root;
	
	@UiField
	MaterialButton btnAction;
	
	@UiField
	MaterialButton btnCancel;
	
	@UiField
	MaterialTextBox boxDevVendor,boxDevSerial,boxDevNumber;
		
	@UiField
	MaterialButton btnDetType;
	@UiField
	MaterialDropDown drpDetType;
	
	@UiField
	MaterialTextBox boxDetSerial,boxDetNumber;
	
	@UiField
	MaterialTextBox boxArea,boxFactor,boxSteer;

	
	public DlgItemTenur() {
		initWidget(uiBinder.createAndBindUi(this));
		refxWidget(root,btnAction,btnCancel);
	}

	public void initDetectType(){
		drpDetType.clear();
		String[] val = Main.param.detectType;
		for(int i=0; i<val.length; i++){
			drpDetType.add(new MaterialLabel(val[i]));
		}
	}
	
	private void setDetectType(String val){
		btnDetType.setText(val);
		for(Widget wid:drpDetType.getItems()){
			MaterialLabel txt = (MaterialLabel)wid;
			if(val.equalsIgnoreCase(txt.getText())==true){
				return;//we found the old one~~~
			}
		}		
		drpDetType.add(new MaterialLabel(val));
	}
	
	@UiHandler("drpDetType")
	void onDropdown(SelectionEvent<Widget> event){
		MaterialLabel txt = (MaterialLabel) event.getSelectedItem();
		btnDetType.setText(txt.getText());
	}
	
	private ItemTenur item = null;
	
	public DlgItem appear(ItemTenur itm,Runnable event){
		item = itm;
		//mapping variable to box~~~
		boxDevVendor.setText(item.getDeviceVendor());
		boxDevSerial.setText(item.getDeviceSerial());
		boxDevNumber.setText(item.getDeviceNumber());		
		setDetectType(itm.getDetectType());		
		boxDetSerial.setText(item.getDetectSerial());
		boxDetNumber.setText(item.getDetectNumber());
		boxArea.setText(item.getArea());
		boxFactor.setText(item.getFactor());
		boxSteer.setText(item.getSteer());
		return appear(itm.uuid,event);
	}

	@Override
	void takeAction(ClickEvent event) {
		//mapping box to variable~~~
		item.setDeviceVendor(boxDevVendor.getText());
		item.setDeviceSerial(boxDevSerial.getText());
		item.setDeviceNumber(boxDevNumber.getText());
		item.setDetectType(btnDetType.getText());
		item.setDetectSerial(boxDetSerial.getText());
		item.setDetectNumber(boxDetNumber.getText());
		item.setArea(boxArea.getText());
		item.setFactor(boxFactor.getText());
		item.setSteer(boxSteer.getText());
		Main.rpc.modifyTenur(item,eventModify);
	}
	
	private AsyncCallback<ItemTenur> eventModify = 
		new AsyncCallback<ItemTenur>()
	{
		@Override
		public void onFailure(Throwable caught) {
		}
		@Override
		public void onSuccess(ItemTenur result) {
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
