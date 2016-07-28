package narl.hpclp.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;

public abstract class DlgBase<T> extends Composite {

	private MaterialModal dlgRoot;
	
	private MaterialButton btnAction,btnCancel;
	
	protected T target;
	
	public DlgBase(){
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
	
	abstract void eventAppear(T item);
	
	/**
	 * Before dialog is appearing, user should hook event.
	 * @param item - pass target
	 * @param hook1- when user click action.
	 * @param hook2- when every is done.
	 * @return
	 */
	public DlgBase<T> appear(T item,ClickHandler hook1,ClickHandler hook2){
		target = item;
		hookAction= hook1;
		hookClose = hook2;
		eventAppear(item);
		dlgRoot.openModal();
		return this;
	}

	public DlgBase<T> appear(T item){
		return appear(item,null,null);
	}
	
	public T getTarget(){
		return target;
	}
	
	private ClickHandler hookAction = null;
	private ClickHandler hookClose = null;
	
	abstract void takeAction(ClickEvent event);
	
	private ClickHandler eventAction = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			takeAction(event);
			if(hookAction!=null){
				hookAction.onClick(event);
			}
			dlgRoot.closeModal();
			if(hookClose!=null){
				hookClose.onClick(event);
			}
		}		
	};
	
	abstract void takeCancel(ClickEvent event);
	
	private ClickHandler eventCancel = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			dlgRoot.closeModal();
			if(hookClose!=null){
				hookClose.onClick(event);
			}
		}		
	};

	protected String add_or_edit(String id){
		String txt;
		if(id==null){
			txt = "新增";
		}else if(id.length()==0){
			txt = "新增";
		}else{
			txt = "修改";
		}
		return txt;
	}
}
