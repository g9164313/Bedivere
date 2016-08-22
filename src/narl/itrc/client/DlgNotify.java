package narl.itrc.client;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialModal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DlgNotify extends Composite {

	private static DlgNotifyUiBinder uiBinder = GWT
			.create(DlgNotifyUiBinder.class);

	interface DlgNotifyUiBinder extends UiBinder<Widget, DlgNotify> {
	}

	public DlgNotify() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * This is just a sprintboard to open modal
	 * @param full_text - message for relaying
	 */
	public void show(String full_text){
		show(IconType.WARNING,full_text);
	}
	
	/**
	 * This is just a sprintboard to open modal.<p>
	 * @param icon - user can determine icon, default is "icon-warning"
	 * @param full_text - message for relaying
	 */
	public void show(IconType ico,String full_text){
		panIcon.setIconType(IconType.WARNING);
		panText.clear();
		String[] paragraph = full_text.split("\n");
		for(String txt:paragraph){
			panText.add(new Label(txt));
		}
		root.openModal();
	}
		
	@UiField
	MaterialModal root;
	
	@UiField
	MaterialIcon panIcon;
	
	@UiField
	HTMLPanel panText;
			
	@UiHandler("btnRoger")
	void actionRoger(ClickEvent event) {
		root.closeModal();
	}
}
