package nthu.hpclp.client.setting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import nthu.hpclp.client.Main;

public class PartParamRestday extends BaseParam {

	private static PartParamRestdayUiBinder uiBinder = GWT.create(PartParamRestdayUiBinder.class);

	interface PartParamRestdayUiBinder extends UiBinder<Widget, PartParamRestday> {
	}

	public PartParamRestday() {
		initWidget(uiBinder.createAndBindUi(this));
		initPartByStyle(
			"RESTDAY",
			"休假日期","6em",
			"事由","11em"
		);
		grid.setWidth("18em");
	}
	
	@Override
	public void onEventShow() {
		provider.setList(Main.param.otherRestDay);
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
