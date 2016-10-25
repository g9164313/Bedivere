package nthu.hpclp.client.product;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialTextBox;
import narl.itrc.client.ExComposite;


public class PartAppx extends ExComposite {

	private static PartAppxUiBinder uiBinder = GWT.create(PartAppxUiBinder.class);

	interface PartAppxUiBinder extends UiBinder<Widget, PartAppx> {
	}

	public PartAppx() {
		initWidget(uiBinder.createAndBindUi(this));
		boxStmp.setText("999/99/99");
		boxName.setText("aaa");
		boxMemo.setText("bbb");
		chainBox(boxStmp,boxName,boxMemo);
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
