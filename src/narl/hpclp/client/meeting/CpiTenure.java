package narl.hpclp.client.meeting;

import narl.hpclp.shared.ItemTenur;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CpiTenure extends Composite {

	private static CpiTenureUiBinder uiBinder = GWT
			.create(CpiTenureUiBinder.class);

	interface CpiTenureUiBinder extends UiBinder<Widget, CpiTenure> {
	}

	@UiField
	MaterialCollapsibleItem cpiEntry;
	
	@UiField
	MaterialLabel txtDevVendor,txtDevSerial,txtDevNumber;
	
	@UiField
	MaterialLabel txtDetType,txtDetSerial,txtDetNumber;
	
	@UiField
	MaterialLabel txtArea,txtFactor,txtSteer,txtMemo;
	
	public CpiTenure() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	//private ItemTenur tenur;

	public CpiTenure(ItemTenur item){
		initWidget(uiBinder.createAndBindUi(this));
		//this.tenur= item;
		//head title
		txtDevVendor.setText(item.getDeviceVendor());
		txtDevSerial.setText(item.getDeviceSerial());
		txtDevNumber.setText(item.getDeviceNumber());
		//body context
		txtDetType.setText(item.getDetectType());
		txtDetSerial.setText(item.getDetectSerial());
		txtDetNumber.setText(item.getDeviceNumber());
		txtArea.setText(item.getArea());
		txtFactor.setText(item.getFactor());
		txtSteer.setText(item.getSteer());
		txtMemo.setText(item.getMemo());
	}
	
}
