package nthu.hpclp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import narl.itrc.client.DlgBase;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemTenur;

public class DlgEditTenur extends DlgBase<ItemTenur> {

	private static DlgEditTenurUiBinder uiBinder = GWT.create(DlgEditTenurUiBinder.class);

	interface DlgEditTenurUiBinder extends UiBinder<Widget, DlgEditTenur> {
	}

	@UiField(provided=true) 
	MaterialModal root = _dlg_root;
	@UiField(provided=true)
	MaterialButton btnAction = _btn_action;
	@UiField(provided=true)
	MaterialButton btnCancel = _btn_cancel;
	
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

	@UiField
	MaterialTextBox boxOKey;
	@UiField
	MaterialLabel txtOwner;
		
	public DlgEditTenur() {
		initWidget(uiBinder.createAndBindUi(this));
		chainBox(
			boxDevVendor,boxDevSerial,boxDevNumber,
			boxDetSerial,boxDetNumber,			
			boxArea,boxFactor,boxSteer,
			boxOKey,boxMemo
		);
	}

	@UiHandler("drpDetType")
	void onDropdown(SelectionEvent<Widget> event){
		MaterialLabel txt = (MaterialLabel) event.getSelectedItem();
		btnDetType.setText(txt.getText());
	}
	
	@UiHandler("boxArea")
	public void onChangeArea(ValueChangeEvent<String> event) {
		Const.makeSpecialCharacter(boxArea);
	}
	
	@UiHandler("boxSteer")
	public void onChangeSteer(ValueChangeEvent<String> event) {
		Const.makeSpecialCharacter(boxSteer);
	}
	
	@UiHandler("boxMeet")
	public void onChangeMeet(ValueChangeEvent<String> event) {
		Main.text2stmp(boxMeet,target.meet);
	}
	
	@UiHandler("boxLast")
	public void onChangeLast(ValueChangeEvent<String> event) {
		Main.text2stmp(boxLast, target.last);
	}
	
	@Override
	public void eventAppear(ItemTenur item) {
		boxDevVendor.setFocus(true);
		
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
		
		ItemOwner owner = item.getOwner();
		if(owner!=null){
			txtOwner.setText(owner.getKey()+" "+owner.getName());
		}else{
			txtOwner.setText("");
		}
		btnAction.setText(add_or_edit(item.uuid));
	}
	
	@Override
	public void takeAction(ClickEvent event) {
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
		item.setLast(Main.fmtStmpLast.parse(boxLast.getText()));		
		item.setMemo(boxMemo.getText());
		
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
				target = result;
				MaterialToast.fireToast("已更新 "+result.getKey());
				dialog_done();
			}
		};
		Main.rpc.modifyTenur(item,eventModify);
	}

	@Override
	public void takeCancel(ClickEvent event) {
	}
	
	@UiHandler("boxOKey")
    void onChangeKeyOwner(ValueChangeEvent<String> event){
		String txt = event.getValue().trim();
		String post = "WHERE "+
	    	"(info[1] SIMILAR TO '%"+txt+"%') OR "+
	    	"(info[2] SIMILAR TO '%"+txt+"%') OR "+
	    	"(info[4] SIMILAR TO '%"+txt+"%') OR "+
	    	"(info[6] SIMILAR TO '%"+txt+"%') ORDER BY last DESC ";
	    //Query feature???
	    final ClickHandler eventPick = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				ItemOwner item = Main.dlgPickOwner.getTarget();
				if(item==null){
					return;
				}
				target.setOwner(item);
				txtOwner.setText(item.getKey()+" "+item.getName());
				boxOKey.setText("");
			}
	    };
	    Main.dlgPickOwner.appear(post,eventPick);
	}
	
	public void initDetectType(){
		drpDetType.clear();
		//TODO:how to display??
		/*String[] val = Main.param.detectType;
		for(int i=0; i<val.length; i++){
			drpDetType.add(new MaterialLabel(val[i]));
		}*/
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


