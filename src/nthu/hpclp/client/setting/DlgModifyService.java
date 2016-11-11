package nthu.hpclp.client.setting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialTextBox;
import narl.itrc.client.DlgBase;
import nthu.hpclp.shared.ItemParam;

public class DlgModifyService extends DlgBase<ItemParam> {

	private static DlgModifyServiceUiBinder uiBinder = GWT.create(DlgModifyServiceUiBinder.class);

	interface DlgModifyServiceUiBinder extends UiBinder<Widget, DlgModifyService> {
	}

	public DlgModifyService() {
		initWidget(uiBinder.createAndBindUi(this));
		chainBox(boxName,boxCurrency);
	}

	@UiField(provided=true) 
	MaterialModal root = _dlg_root;
	@UiField(provided=true)
	MaterialButton btnAction = _btn_action;
	@UiField(provided=true)
	MaterialButton btnCancel = _btn_cancel;
	
	@UiField
	MaterialTextBox boxName,boxCurrency;
		
	@Override
	public String toString(){
		return boxName.getText()+"@"+boxCurrency.getText();
	}
	
	@Override
	public void eventAppear(ItemParam item) {
		String[] val = item.getVal().split("@");
		String txt = "0";
		switch(val.length){
		default:
		case 2:
			boxName.setText(val[0]);
			txt = val[1];
			//txt = NumberFormat.getCurrencyFormat().format(Integer.valueOf(val[1]));
			break;
		case 1:
			boxName.setText(val[0]);
			break;
		case 0:
			boxName.setText("???");
			break;
		}
		boxCurrency.setText(txt);
	}

	@Override
	public void takeAction(ClickEvent event) {
	}

	@Override
	public void takeCancel(ClickEvent event) {
	}
}
