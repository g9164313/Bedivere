package narl.hpclp.client.setting;

import narl.hpclp.client.Main;
import narl.itrc.client.ExComposite;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class PanMain extends ExComposite {

	private static PanMainUiBinder uiBinder = GWT.create(PanMainUiBinder.class);

	interface PanMainUiBinder extends UiBinder<Widget, PanMain> {
	}

	public PanMain() {
		initWidget(uiBinder.createAndBindUi(this));
		
		arch1.add(new PartParamService());
		arch3.add(new PartParamRestday());
		arch5.add(new PartSPoint());
		
		arch2.add(new PartParamDetType());
		arch4.add(new PartParamUnit());
		arch6.add(new PartParamEmitter());
	}

	@UiField
	HTMLPanel arch1,arch2,arch3,arch4,arch5,arch6;
	
	@Override
	public void onEventShow() {
	}

	@Override
	public void onEventHide() {
	}
	
	@UiHandler("lnkPanMeet")
	void onPanMeet(ClickEvent e){
		Main.switchToMeeting();
	}
	
	@UiHandler("lnkPanAccnt")
	void onPanAccnt(ClickEvent e){
		Main.switchToAccount();
	}
	
	@UiHandler("lnkPanProdx")
	void onPanProdx(ClickEvent e){
		Main.switchToProduct();
	}
}
