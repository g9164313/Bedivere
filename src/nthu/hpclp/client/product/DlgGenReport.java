package nthu.hpclp.client.product;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialProgress;
import nthu.hpclp.client.Main;

public class DlgGenReport extends Composite {

	private static DlgGenReportUiBinder uiBinder = GWT.create(DlgGenReportUiBinder.class);

	interface DlgGenReportUiBinder extends UiBinder<Widget, DlgGenReport> {
	}

	public DlgGenReport() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	MaterialModal root;	
	@UiField
	MaterialProgress barProgress;
	@UiField
	MaterialLabel txtProgress;
	@UiField
	TextArea boxMessage;
	@UiField
	MaterialButton btnCancel,btnRestart;
		
	public void appear(){				
		root.openModal();
		onRestart(null);
	}
	
	private AsyncCallback<String> eventCheck =
		new AsyncCallback<String>()
	{
		@Override
		public void onFailure(Throwable caught) {
		}
		@Override
		public void onSuccess(String result) {			
			progress(idx,max);
			idx++;
			if(idx>=max){
				btnRestart.setEnabled(true);
				return;//we complete the task~~~
			}
			Main.rpc.checkReport1(
				lst.get(idx),
				eventCheck
			);//next turn~~~
		}
	};
	
	private AsyncCallback<ArrayList<String>> eventReset = 
		new AsyncCallback<ArrayList<String>>()
	{
		@Override
		public void onFailure(Throwable caught) {
			print("內部錯誤！！");
			print(caught.getMessage());
		}
		@Override
		public void onSuccess(ArrayList<String> result) {
			idx = 0;
			max = result.size();
			lst = result;
			Main.rpc.checkReport1(
				lst.get(idx),
				eventCheck
			);//launch the first task~~~
		}
	};
	private int idx,max;
	private ArrayList<String> lst;
	
	private void progress(int stp,int max){
		float val = ((float)stp/(float)max)*100f;
		barProgress.setPercent(val);
		txtProgress.setText(""+Math.round(val)+"％ （"+stp+"/"+max+"）");
	}
	
	private void print(String msg){
		String txt = boxMessage.getText();
		txt = txt +"\r\n" + msg;
		boxMessage.setText(txt);
		boxMessage.getElement().getFirstChildElement().setScrollTop(
			boxMessage.getElement().getFirstChildElement().getScrollHeight()
		);//very strange method~~~~
	}
	
	@UiHandler("btnRestart")
    void onRestart(ClickEvent e){
		print("初始化...");
		progress(0,1);
		btnRestart.setEnabled(false);
		Main.rpc.resetReport1(eventReset);
    }
	
    @UiHandler("btnCancel")
    void onCancel(ClickEvent e){
    	root.closeModal();
    }
}
