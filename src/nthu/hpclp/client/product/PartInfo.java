package nthu.hpclp.client.product;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialFloatBox;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialTextBox;
import narl.itrc.client.ExComposite;

public class PartInfo extends ExComposite {

	private static PartInfoUiBinder uiBinder = GWT.create(PartInfoUiBinder.class);

	interface PartInfoUiBinder extends UiBinder<Widget, PartInfo> {
	}

	public PartInfo() {
		initWidget(uiBinder.createAndBindUi(this));
	}

    @UiField
    MaterialTextBox boxPKey,boxStmp,boxMemo;
    @UiField
    MaterialFloatBox boxTemp,boxPress,boxHumid;
    @UiField
    MaterialListBox cmbFormat,cmbUnitRef,cmbUnitMea,cmbEmitter;
    
    @UiField
    MaterialCheckBox chkUseLogo;

	@Override
	public void onEventShow() {
	}
	@Override
	public void onEventHide() {
	}
}
