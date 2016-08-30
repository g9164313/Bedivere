package narl.hpclp.client.setting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import narl.hpclp.client.Main;

public class PartParamEmitter extends BaseParam {

	private static PartParamEmitterUiBinder uiBinder = GWT.create(PartParamEmitterUiBinder.class);

	interface PartParamEmitterUiBinder extends UiBinder<Widget, PartParamEmitter> {
	}

	public PartParamEmitter() {
		initWidget(uiBinder.createAndBindUi(this));
		initPartByStyle(
			"EMITTER",
			"射源名稱","10em",
			"種類"    ,"7em",
			"有效面積","7em",
			"強度"    ,"25em",
			"表面"    ,"10em",
			"序號"    ,"7em",
			"認證"    ,"28em",
			"涵蓋因子","7em",
			"涵蓋機率","7em",
			"不確定度","10em"
		);		
		grid.setWidth("30em");		
	}
	
	@Override
	public void onEventShow() {
		provider.setList(Main.param.prodxEmitter);
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
