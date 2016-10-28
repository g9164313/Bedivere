package nthu.hpclp.client.product;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialTextBox;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;

public class PartAppx extends ExComposite {

	private static PartAppxUiBinder uiBinder = GWT.create(PartAppxUiBinder.class);

	interface PartAppxUiBinder extends UiBinder<Widget, PartAppx> {
	}

	public PartAppx() {
		initWidget(uiBinder.createAndBindUi(this));
		boxStmp.setText(Main.date2tw(new Date()));
		boxName.setText("");
		boxMemo.setText("");
		appendCombo(
			boxName,
			new String[]{"d04925","w03099","w11803"}
		);
		appendCombo(
			boxMemo,
			new String[]{"EFFI","ACTI","~500","D.R./EFFI","x100(fail.)"}
		);
		chainLocalBox(boxStmp,boxName,boxMemo);
	}

	@UiField
	MaterialTextBox boxStmp,boxName,boxMemo;
	 
	public String getDate(){
		return boxStmp.getText();
	}
	
	public String getName(){
		return boxName.getText();
	}
	
	public String getMemo(){
		return boxMemo.getText();
	}

	@Override
	public void onEventShow() {
	}

	@Override
	public void onEventHide() {
	}	
}
