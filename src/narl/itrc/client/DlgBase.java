package narl.itrc.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;

public abstract class DlgBase<T> extends Composite {

	protected MaterialModal _dlg_root = new MaterialModal();
	
	protected MaterialButton _btn_action = new MaterialButton();
	protected MaterialButton _btn_cancel = new MaterialButton();
	
	protected T target;
	
	public DlgBase(){
		_btn_action.addClickHandler(eventAction);
		_btn_cancel.addClickHandler(eventCancel);
	}

	private EventBoxChain chain = new EventBoxChain();
	
	public void chainBox(MaterialWidget... listBox){
		EventBoxChain.link(chain,listBox);
	}
	
	public abstract void eventAppear(T item);
	
	/**
	 * Before dialog is appearing, user should hook event.<p>
	 * @param obj - pass target.<p>
	 * @param hook1 - When user click action.<p>
	 * @param hook2 - When user cancel or After action.<p>
	 * @return - self
	 */
	public DlgBase<T> appear(T obj,ClickHandler hook1,ClickHandler hook2){
		target = obj;
		hookAction= hook1;
		hookCancel= hook2;
		eventAppear(obj);
		_dlg_root.openModal();
		return this;
	}

	/**
	 * Just hook action event.<p>
	 * @param obj - pass target.<p>
	 * @param hook1 - when user click action.<p>
	 * @return - self
	 */
	public DlgBase<T> appear(T obj,ClickHandler hook){
		return appear(obj,hook,null);
	}
	
	/**
	 * just show dialog,do nothing
	 * @param obj - pass target
	 * @return - self
	 */
	public DlgBase<T> appear(T obj){
		return appear(obj,null,null);
	}
	
	public T getTarget(){
		return target;
	}
	
	public String toString(){
		return "";
	}
	
	private ClickHandler hookAction = null;
	private ClickHandler hookCancel = null;
	
	public abstract void takeAction(ClickEvent event);
	
	protected ClickHandler eventAction = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			takeAction(event);
			if(hookAction!=null){
				hookAction.onClick(event);				
			}
			_dlg_root.closeModal();
		}		
	};
	
	protected void dialog_done(){
		_dlg_root.closeModal();
		if(hookCancel!=null){
			hookCancel.onClick(null);
		}
	}
	
	public abstract void takeCancel(ClickEvent event);
	
	protected ClickHandler eventCancel = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			dialog_done();
		}		
	};
}
