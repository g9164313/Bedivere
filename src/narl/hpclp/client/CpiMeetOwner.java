package narl.hpclp.client;

import narl.hpclp.shared.ItmBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class CpiMeetOwner extends Composite {

	private static CpiMeetOwnerUiBinder uiBinder = GWT
			.create(CpiMeetOwnerUiBinder.class);

	interface CpiMeetOwnerUiBinder extends UiBinder<Widget, CpiMeetOwner> {
	}

	public CpiMeetOwner() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public CpiMeetOwner(ItmBase itm) {
		initWidget(uiBinder.createAndBindUi(this));
	}


}
