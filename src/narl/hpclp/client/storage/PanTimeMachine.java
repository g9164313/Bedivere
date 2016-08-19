package narl.hpclp.client.storage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class PanTimeMachine extends Composite {

	private static PanTimeMachineUiBinder uiBinder = GWT
			.create(PanTimeMachineUiBinder.class);

	interface PanTimeMachineUiBinder extends UiBinder<Widget, PanTimeMachine> {
	}

	@UiField
	ListBox tickList;
	
	public PanTimeMachine() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	
	
}
