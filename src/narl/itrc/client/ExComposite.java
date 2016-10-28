package narl.itrc.client;

import java.util.ArrayList;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Composite;

public abstract class ExComposite extends Composite {
	
	protected DlgNotify dlgNotify = new DlgNotify();
	
	protected DlgApprove dlgApprove = new DlgApprove();
	
	protected MaterialPanel _root = new MaterialPanel();

	public ExComposite(){
		addAttachHandler(eventShowHide);//default~~~
		_root.add(dlgApprove);		
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
				//At this time, we can prepare environment parameters
				onEventShow();
			}else{
				onEventHide();
			}
		}
	};
	//------------------------------------//

	/**
	 * select item in combo list, if no this item, just add it~~~.<p>
	 * @param box - combo box.<p>
	 * @param name - item name.<p>
	 * @return
	 */
	protected static int cmbSelect(
		MaterialListBox box,
		String name
	){
		return cmbSelect(box,name,name);
	}
	
	/**
	 * select item in combo list, if no this item, just add it~~~.<p>
	 * @param box - combo box.<p>
	 * @param name - item name.<p>
	 * @param value - item value.<p>
	 * @return the index of combo list
	 */
	protected static int cmbSelect(
		MaterialListBox box,
		String name,
		String value
	){
		int cnt = box.getItemCount();
		if(value==null){
			value = name;
		}
		for(int i=0; i<cnt; i++){
			String _name = box.getItemText(i);
			String _value = box.getValue(i);
			if(
				name.equalsIgnoreCase(_name)==true||
				value.equalsIgnoreCase(_value)==true
			){
				box.setSelectedIndex(i);
				return i;
			}
		}
		box.addItem(name, value);
		box.setSelectedIndex(cnt+1);
		return cnt+1;
	}
	//------------------------------------//
	
	private static class BoxCombo implements KeyDownHandler {
		public int idx = 0;
		public String[] lst = null;
		private MaterialTextBox box;
		public BoxCombo(MaterialTextBox input,String[] list){
			lst = list;
			box = input;
			box.addKeyDownHandler(this);
		}
		@Override
		public void onKeyDown(KeyDownEvent event) {
			int code = event.getNativeKeyCode();
			switch(code){
			case KeyCodes.KEY_UP:
				idx--;
				if(idx<0){
					idx = lst.length - 1;
				}
				box.setText(lst[idx]);
				break;
			case KeyCodes.KEY_DOWN:
				idx++;
				if(idx>=lst.length){
					idx = 0;
				}
				box.setText(lst[idx]);
				break;
			}
		}
	};
	
	public void appendCombo(MaterialTextBox box,String[] lst){
		new BoxCombo(box,lst);
	}
	//------------------------------------//

	private static EventBoxChain g_chain = new EventBoxChain();
	
	public static void chainBox(MaterialWidget... listBox){
		EventBoxChain.link(g_chain,listBox);
	}		
	
	private EventBoxChain l_chain = new EventBoxChain();
	
	public void chainLocalBox(MaterialWidget... listBox){
		EventBoxChain.link(l_chain,listBox);
	}
	//------------------------------------//

	private boolean isHooking = false;
	
	private ArrayList<Integer> nullShortcut = new ArrayList<Integer>();
	private ArrayList<Integer> ctrlShortcut = new ArrayList<Integer>();
	private ArrayList<Integer> alt_Shortcut = new ArrayList<Integer>();
	
	public void eventShortcut(Integer keycode,Integer appx){
		//user must override this function~~~~
	}
	
	private Event.NativePreviewHandler hookShortcut = 
		new Event.NativePreviewHandler()
	{
		@Override
		public void onPreviewNativeEvent(NativePreviewEvent event) {
			if(event.getTypeInt()!=Event.ONKEYDOWN){
				return;
			}
			NativeEvent ne = event.getNativeEvent();			
			int key = ne.getKeyCode();
			if(nullShortcut.contains(key)==true){
				eventShortcut(key,null);
				event.cancel();
			}
			if(ctrlShortcut.contains(key)==true && ne.getCtrlKey()==true){
				eventShortcut(key,KeyCodes.KEY_CTRL);
				event.cancel();
			}
			if(alt_Shortcut.contains(key)==true && ne.getAltKey()==true){
				eventShortcut(key,KeyCodes.KEY_ALT);
				event.cancel();
			}
			//boolean meta = event.getMetaKey();
			//boolean shift = event.getShiftKey();
		}
	};
	
	protected void addShortcut(int keycode){
		add_shortcut(nullShortcut,keycode);
	}
	
	protected void addCtrlShortcut(int keycode){
		add_shortcut(ctrlShortcut,keycode);
	}
	
	protected void addAltShortcut(int keycode){
		add_shortcut(alt_Shortcut,keycode);
	}

	private void add_shortcut(ArrayList<Integer> lst,int keycode){
		if(isHooking==false){
			Event.addNativePreviewHandler(hookShortcut);
			isHooking = true;
			//RootPanel.get().addDomHandler(eventHookShortcut,type);
		}
		lst.add(keycode);
	}
}
