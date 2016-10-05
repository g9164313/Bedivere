package narl.itrc.client;

import java.util.ArrayList;

import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import nthu.hpclp.client.Main;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

public abstract class ExComposite extends Composite {
	
	protected DlgNotify dlgNotify = new DlgNotify();
	
	protected DlgApprove dlgApprove = new DlgApprove();
	
	protected MaterialPanel _root = new MaterialPanel();

	public ExComposite(){
		addAttachHandler(eventShowHide);//default~~~
		_root.add(dlgApprove);
		//RootPanel.get().addDomHandler(handler, type)
	}
	
	protected void initAddins(MaterialPanel root){
		root.add(dlgNotify);		
	}
	
	public abstract void onEventShow();
	public abstract void onEventHide();

	private AttachEvent.Handler eventShowHide = new AttachEvent.Handler(){
		@Override
		public void onAttachOrDetach(AttachEvent event) {
			if(event.isAttached()==true){
				//At this time, we can prepare enviroment paramters
				onEventShow();
			}else{
				onEventHide();
			}
		}
	};

	//------------------//
	
	private ArrayList<MaterialTextBox> lstBox = new ArrayList<MaterialTextBox>();
	
	private boolean isChainBack = true;
	
	protected void chainBox(MaterialTextBox... listBox){
		lstBox.clear();
		for(MaterialTextBox box:listBox){
			if(box==null){
				isChainBack = false;
				break;
			}
			lstBox.add(box);
			box.addKeyDownHandler(eventChain);
		}
	}
	
	private	KeyDownHandler eventChain = new KeyDownHandler(){
		@Override
		public void onKeyDown(KeyDownEvent event) {
			
			int code = event.getNativeKeyCode();
			
			if(code!=KeyCodes.KEY_ENTER){
				if(code!=KeyCodes.KEY_RIGHT&&code!=KeyCodes.KEY_LEFT){
					return;
				}
				if(event.isControlKeyDown()==false){
					return;
				}
			}
			
			if(event.isControlKeyDown()==true){
				
			}
			
			MaterialTextBox cur = (MaterialTextBox)((TextBox)event.getSource()).getParent();
			if(lstBox.contains(cur)==false){
				return;
			}
			
			int idx = lstBox.indexOf(cur);
			switch(code){
			case KeyCodes.KEY_ENTER:
			case KeyCodes.KEY_RIGHT:
				idx++;
				if(idx>=lstBox.size()){					
					idx = 0;
					if(isChainBack==false){
						return;
					}
				}
				break;			
			case KeyCodes.KEY_LEFT:
				idx--;
				if(idx<0){					
					idx = lstBox.size() - 1;
					if(isChainBack==false){
						return;
					}
				}
				break;
			}
						
			lstBox.get(idx).setFocus(true);
		}
	};
}
