package narl.hpclp.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Composite;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;

public abstract class DlgItem extends Composite {

	protected MaterialModal dlgRoot;
	
	private MaterialButton btnAction,btnCancel;
	
	public DlgItem(){
	}
	
	/**
	 * The child class must call this to override widget
	 * @param arg - first is MaterialModal, 
	 * 	second is MaterialButton
	 */
	protected void refxWidget(MaterialWidget... arg){
		dlgRoot = (MaterialModal)(arg[0]);
		btnAction = (MaterialButton)(arg[1]);
		btnAction.addClickHandler(eventAction);
		btnCancel = (MaterialButton)(arg[2]);
		btnCancel.addClickHandler(eventCancel);	
	}

	private Runnable eventClose = null; 
	
	protected void handleClose(){
		if(eventClose!=null){
			eventClose.run();
		}
	}
	
	protected DlgItem appear(String id,Runnable event){
		eventClose = event;
		if(id==null){
			btnAction.setText("新增");
		}else if(id.length()==0){
			btnAction.setText("新增");
		}else{
			btnAction.setText("修改");
		}
		dlgRoot.openModal();
		return this;
	}

	abstract void takeAction(ClickEvent event);
	
	private ClickHandler eventAction = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			takeAction(event);			
		}		
	};
	
	private ClickHandler eventCancel = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			dlgRoot.closeModal();
		}		
	};
}
