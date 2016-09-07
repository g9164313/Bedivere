package nthu.hpclp.client.setting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import nthu.hpclp.client.Main;

public class PartParamService extends BaseParam {

	private static PartParamServiceUiBinder uiBinder = GWT.create(PartParamServiceUiBinder.class);

	interface PartParamServiceUiBinder extends UiBinder<Widget, PartParamService> {
	}

	public PartParamService() {
		initWidget(uiBinder.createAndBindUi(this));
		initPartByStyle(
			"SERVICE",
			"服務名稱","15em",
			"費用","7em"
		);
		grid.setSize("90vw","43vh");
	}
	
	@Override
	public void onEventShow() {
		provider.setList(Main.param.accntService);
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
