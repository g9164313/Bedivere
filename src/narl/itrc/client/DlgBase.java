package narl.itrc.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;

public abstract class DlgBase<T> extends Composite {

	protected MaterialModal dlgRoot;
	
	protected MaterialButton btnAction,btnCancel;
	
	protected T target;
	
	public DlgBase(){
	}

	private EventBoxChain chain = new EventBoxChain();
	
	public void chainBox(MaterialWidget... listBox){
		EventBoxChain.link(chain,listBox);
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

	public abstract void eventAppear(T item);
	
	/**
	 * Before dialog is appearing, user should hook event.<p>
	 * @param obj - pass target.<p>
	 * @param hook1 - when user click action.<p>
	 * @param hook2 - when user cancel.<p>
	 * @return - self
	 */
	public DlgBase<T> appear(T obj,ClickHandler hook1,ClickHandler hook2){
		target = obj;
		hookAction= hook1;
		hookCancel= hook2;
		eventAppear(obj);
		dlgRoot.openModal();
		return this;
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
	
	private ClickHandler hookAction = null;
	private ClickHandler hookCancel = null;
	
	public abstract void takeAction(ClickEvent event);
	
	private ClickHandler eventAction = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			takeAction(event);
			if(hookAction!=null){
				hookAction.onClick(event);				
			}
			dlgRoot.closeModal();
		}		
	};
	
	protected void dialog_done(){
		dlgRoot.closeModal();
		if(hookCancel!=null){
			hookCancel.onClick(null);
		}
	}
	
	public abstract void takeCancel(ClickEvent event);
	
	private ClickHandler eventCancel = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			dialog_done();
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
