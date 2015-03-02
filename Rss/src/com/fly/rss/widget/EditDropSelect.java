package com.fly.rss.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;

import com.fly.rss.R;
/**
 * 编辑下拉选择
 * @author jian.fu
 *
 */
public class EditDropSelect extends RelativeLayout{
	
	private ListAdapter mAdapter = null;
	private EditText editText = null;
	private ListPopupWindow popupWindow  = null;
	public EditDropSelect(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	
	
	public EditDropSelect(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	
	
	public EditDropSelect(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		Context context = getContext();
		int editId = 0x000011;
		editText = new EditText(context);
		editText.setId(editId);
		LayoutParams editParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		editText.setLayoutParams(editParams);
		editText.setMinWidth(300);
		addView(editText);
		
		Button btnDrop = new Button(context);
		LayoutParams btnParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		btnParams.addRule(RelativeLayout.RIGHT_OF, editId);
		btnDrop.setLayoutParams(btnParams);
		btnDrop.setText("下拉");
		btnDrop.setOnClickListener(new DropClick());
		
		addView(btnDrop);
		
		super.onFinishInflate();
	}

	
	public void setAdapter(ListAdapter adapter){
		mAdapter = adapter;
	}


	public String getEditTextStr(){
		String str = "";
		if(editText != null){
			str = editText.getText().toString().trim();
		}
		return str;
	}
	
	private class DropClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Context context = getContext();
			if(popupWindow == null){
				popupWindow = new ListPopupWindow(context);
				Drawable mDrawable = context.getResources().getDrawable(R.color.c_f1f1f1);
				popupWindow.setBackgroundDrawable(mDrawable);
				popupWindow.setWidth(getMeasuredWidth());
				popupWindow.setHeight(android.view.WindowManager.LayoutParams.WRAP_CONTENT);
				if(mAdapter != null){
					popupWindow.setAdapter(mAdapter);
				}
				popupWindow.setOnItemClickListener(new ItemClick());
				popupWindow.setAnchorView(EditDropSelect.this);
			}
			if(!popupWindow.isShowing()){
				popupWindow.show();
			}else{
				popupWindow.dismiss();
			}
		}
		
	}

	private class ItemClick implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Object object = parent.getItemAtPosition(position);
			if(editText != null){
				editText.setText(object.toString());
			}
			popupWindow.dismiss();
		}
		
	}

	



}
