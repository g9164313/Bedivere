package nthu.hpclp.client.setting;

import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;

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
		
		arch2.add(new PartParamRestday());		
		arch3.add(new PartSPoint());
		
		arch4.add(new PartParamEmitter());
		
		arch5.add(new PartParamDetType());
		arch6.add(new PartParamUnit());
	}

	@UiField
	HTMLPanel arch1,arch2,arch3,arch4,arch5,arch6;
	
	@Override
	public void onEventShow() {
	}

	@Override
	public void onEventHide() {
	}
}
