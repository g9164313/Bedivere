package narl.hpclp.client.storage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialPanel;

public class PartParamUnit extends Composite {

	private static PartParamUnitUiBinder uiBinder = GWT
		.create(PartParamUnitUiBinder.class);

	interface PartParamUnitUiBinder extends UiBinder<Widget, PartParamUnit> {
	}

	public PartParamUnit() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	MaterialPanel root;
	
	@UiField
	SimplePanel arch1,arch2;
	
}
