package nthu.hpclp.client.meeting;

import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialModal;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.ItemMeeting;
import nthu.hpclp.shared.ItemTenur;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CpiTenure extends Composite {

	private static CpiTenureUiBinder uiBinder = GWT.create(CpiTenureUiBinder.class);

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
	
	private PanMain root;
	private ItemMeeting meet;
	private ItemTenur tenu;
	
	public CpiTenure(PanMain root,ItemMeeting meet,ItemTenur item){
		initWidget(uiBinder.createAndBindUi(this));
		this.root = root;
		this.meet = meet;
		this.tenu = item;
		mapping();
	}
	
	@UiHandler("icoEdit")
	void onEditItem(ClickEvent e){
		Main.dlgModifyTenur.appear(
			tenu, null, new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				mapping();			
				arrange();
				root.redraw();
			}			
		});
	}
	
	private void mapping(){
		//head title
		txtDevVendor.setText(tenu.getDeviceVendor());
		txtDevSerial.setText(tenu.getDeviceSerial());
		txtDevNumber.setText(tenu.getDeviceNumber());
		//body context
		txtDetType.setText(tenu.getDetectType());
		txtDetSerial.setText(tenu.getDetectSerial());
		txtDetNumber.setText(tenu.getDeviceNumber());
		txtArea.setText(tenu.getArea());
		txtFactor.setText(tenu.getFactor());
		txtSteer.setText(tenu.getSteer());
		txtMemo.setText(tenu.getMemo());
	}
	
	private void arrange(){
		//update again!!!
		//find a new slot and insert item~~~
		String t_day = Main.fmtDate.format(tenu.meet);
		if(t_day.equalsIgnoreCase(meet.getSDay())==true){			
			return;//we fit the target
		}
		meet.lst.remove(tenu);
		for(ItemMeeting mee:root.lstMeet){
			if(t_day.equalsIgnoreCase(mee.getSDay())==true){
				mee.lst.add(tenu);
				return;//we fit the target
			}
		}
		//no meeting slot, so we create one~~~~
		ItemMeeting mee = new ItemMeeting(meet,tenu);
		mee.setSDay(t_day,tenu.meet);
		root.lstMeet.add(mee);
	}
}
