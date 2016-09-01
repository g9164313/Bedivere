package nthu.hpclp.client.setting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import nthu.hpclp.client.Main;

public class PartParamDetType extends BaseParam {

	private static PartParamDetTypeUiBinder uiBinder = GWT.create(PartParamDetTypeUiBinder.class);

	interface PartParamDetTypeUiBinder extends UiBinder<Widget, PartParamDetType> {
	}

	public PartParamDetType() {
		initWidget(uiBinder.createAndBindUi(this));
		initPartByStyle(
			"DETTYPE",
			"偵檢儀類型",null
		);		
	}
	
	@Override
	public void onEventShow() {
		provider.setList(Main.param.prodxDetType);
		provider.refresh();
	}

	@UiField(provided=true) 
	SimplePanel arch1=archGrid, arch2=archPage;

	@UiHandler("btnCreate")
	void onCreate(ClickEvent e){
		createParam();	
	}
	
	@UiHandler("btnDelete")
	void onDelete(ClickEvent e){
		deleteParam();
	}

	@Override
	public void onEventHide() {
	}
}
