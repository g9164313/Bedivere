package nthu.hpclp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

public class PartEmitter extends Composite {

	private static PartEmitterUiBinder uiBinder = GWT.create(PartEmitterUiBinder.class);

	interface PartEmitterUiBinder extends UiBinder<Widget, PartEmitter> {
	}
	
	@UiField
	MaterialPanel root;
	
	@UiField MaterialLabel 
		txtKindArea,txtStrength,txtSurface,
		txtFactorK,txtFactorP,txtUncertain,
		txtSerial,txtCriteron;
	
	public PartEmitter() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	
	
}
