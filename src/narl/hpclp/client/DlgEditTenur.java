package narl.hpclp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
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

public class DlgEditTenur extends DlgBase<ItemTenur> {

	private static DlgEditTenurUiBinder uiBinder = GWT.create(DlgEditTenurUiBinder.class);

	interface DlgEditTenurUiBinder extends UiBinder<Widget, DlgEditTenur> {
	}

	@UiField
	MaterialModal root;
	
	@UiField
	MaterialButton btnAction,btnCancel;
	
	@UiField
	MaterialTextBox boxDevVendor,boxDevSerial,boxDevNumber;
		
	@UiField
	MaterialButton btnDetType;
	@UiField
	MaterialDropDown drpDetType;
	
	@UiField
	MaterialTextBox boxDetSerial,boxDetNumber;
	
	@UiField
	MaterialTextBox boxMeet,boxLast,boxMemo;
	
	@UiField
	MaterialTextBox boxArea,boxFactor,boxSteer;

	public DlgEditTenur() {
		initWidget(uiBinder.createAndBindUi(this));
		refxWidget(root,btnAction,btnCancel);
	}

	@UiHandler("drpDetType")
	void onDropdown(SelectionEvent<Widget> event){
		MaterialLabel txt = (MaterialLabel) event.getSelectedItem();
		btnDetType.setText(txt.getText());
	}
	
	@UiHandler("boxArea")
	public void onChangeArea(ValueChangeEvent<String> event) {
		makeSpecialCharacter(boxArea);
	}
	
	@UiHandler("boxSteer")
	public void onChangeSteer(ValueChangeEvent<String> event) {
		makeSpecialCharacter(boxSteer);
	}
	
	@UiHandler("boxMeet")
	public void onChangeMeet(ValueChangeEvent<String> event) {
		ItemTenur item = target;
		if(Main.text2stmp(boxMeet, item.meet)==true){
			boxLast.setFocus(true);
		}
	}
	
	@UiHandler("boxLast")
	public void onChangeLast(ValueChangeEvent<String> event) {
		ItemTenur item = target;
		if(Main.text2stmp(boxLast, item.last)==true){
			boxMemo.setFocus(true);
		}
	}
	
	@UiHandler("boxMemo")
	public void onChangeMemo(ValueChangeEvent<String> event) {
		boxMemo.setFocus(false);//end of chain~~~
	}

	@Override
	void eventAppear(ItemTenur item) {
		boxDevVendor.setText(item.getDeviceVendor());
		boxDevSerial.setText(item.getDeviceSerial());
		boxDevNumber.setText(item.getDeviceNumber());		
		
		setDetectType(item.getDetectType());		
		boxDetSerial.setText(item.getDetectSerial());
		boxDetNumber.setText(item.getDetectNumber());
		
		boxArea.setText(item.getArea());
		boxFactor.setText(item.getFactor());
		boxSteer.setText(item.getSteer());
		
		boxMeet.setText(Main.fmtStmpLast.format(item.meet));
		boxLast.setText(Main.fmtStmpLast.format(item.last));
		boxMemo.setText(item.getMemo());
	}
	
	@Override
	void takeAction(ClickEvent event) {
		ItemTenur item = target;
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
		
		item.setMeet(Main.fmtStmpLast.parse(boxMeet.getText()));
		//item.setStmp(Main.fmtStmpLast.parse(boxMeet.getText()));//future~~~~	
		item.setLast(Main.fmtStmpLast.parse(boxLast.getText()));		
		item.setMemo(boxMemo.getText());
		
		btnAction.setText(add_or_edit(item.uuid));
		
		final AsyncCallback<ItemTenur> eventModify = 
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
				//TODO:result.copyTo(item);
				root.closeModal();
			}
		};
		Main.rpc.modifyTenur(item,eventModify);
	}

	@Override
	void takeCancel(ClickEvent event) {
	}
	
	
	
	private void makeSpecialCharacter(MaterialTextBox box){
		String txt = box.getText();
		txt = txt.replace("^2","²").replace("^3","³");
		box.setText(txt);
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

}
