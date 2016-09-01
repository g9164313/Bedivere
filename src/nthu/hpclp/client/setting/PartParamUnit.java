package nthu.hpclp.client.setting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import nthu.hpclp.client.Main;

public class PartParamUnit extends BaseParam {

	private static PartParamUnitUiBinder uiBinder = GWT
		.create(PartParamUnitUiBinder.class);

	interface PartParamUnitUiBinder extends UiBinder<Widget, PartParamUnit> {
	}

	public PartParamUnit() {
		initWidget(uiBinder.createAndBindUi(this));
		initPartByStyle(
			"UNIT",
			"輻射單位",null
		);
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
	public void onEventShow() {
		provider.setList(Main.param.prodxRadUnit);
		provider.refresh();
	}

	@Override
	public void onEventHide() {
	}
}
