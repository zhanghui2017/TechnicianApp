package com.gengli.technician.view;

import com.gengli.technician.R;
import com.gengli.technician.listener.MyDialogListener;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MyDialog extends Dialog implements View.OnClickListener {
	private Button dialog_button1,dialog_button2;
	private TextView dialog_title;
	private MyDialogListener listener;
	private String dialogTitle;
	private String makesuer;
	private String cancle;
	//声明一个借口用于调用Onclick事件
	/*public interface MyDialogListener{
		public void onClick(View view);
	}*/
	//构造函数
	public MyDialog(Context context,String dialogTitle,int theme,MyDialogListener listener){
		super(context,theme);
		this.listener = listener;
		this.dialogTitle = dialogTitle;
	}
	/**
	 * 自定义按钮，标题的dialog
	 * @param dialogTitle
	 * @param makesuer
	 * @param cancle
	 * */
	public MyDialog(Context context, int theme,MyDialogListener listener,
			String dialogTitle, String makesuer, String cancle) {
		super(context,theme);
		this.listener = listener;
		this.dialogTitle = dialogTitle;
		this.makesuer = makesuer;
		this.cancle = cancle;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_exit_view);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		initView();
	}
	public void initView(){
		dialog_button1 = (Button) findViewById(R.id.dialog_button1);
		dialog_button2 = (Button) findViewById(R.id.dialog_button2);
		dialog_title = (TextView) findViewById(R.id.dialog_title);
		dialog_title.setText(dialogTitle);
		dialog_button1.setOnClickListener(this);
		dialog_button2.setOnClickListener(this);
		
		if(makesuer != null && !makesuer.equals("")){
			dialog_button1.setText(makesuer);
		}
		if(cancle != null && !cancle.equals("")){
			dialog_button2.setText(cancle);
		}
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		listener.onClick(view);
	}
}
