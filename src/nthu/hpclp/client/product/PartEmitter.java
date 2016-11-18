package nthu.hpclp.client.product;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import nthu.hpclp.shared.ParmEmitter;

public class PartEmitter extends Composite {

	private static PartEmitterUiBinder uiBinder = GWT.create(PartEmitterUiBinder.class);

	interface PartEmitterUiBinder extends UiBinder<Widget, PartEmitter> {
	}
	
	@UiField
	MaterialPanel root;
	
	@UiField MaterialLabel 
		txtTitle,txtKind,
		txtArea,txtStrength,txtSurface,
		txtFactorK,txtFactorP,txtUncertain,
		txtSerial,txtCriteron;
	
	public PartEmitter() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	//---------------------------------------//

	public void setTarget(String txt){
		ParmEmitter emt = new ParmEmitter(txt);
		txtTitle.setText(emt.getTitle());
		txtKind.setText(emt.getKind());
		txtArea.setText(emt.getArea());		
		txtStrength.setText(emt.getStrength());
		txtSurface.setText(emt.getSurface());
		txtFactorK.setText(emt.getFactorK());
		txtFactorP.setText(emt.getFactorP());
		txtUncertain.setText(emt.getUncertain());
		txtSerial.setText(emt.getSerial());
		txtCriteron.setText(emt.getCriterion());
	}
}
