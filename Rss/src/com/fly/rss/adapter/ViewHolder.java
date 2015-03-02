package com.fly.rss.adapter;

import android.util.SparseArray;
import android.view.View;
/**
 * 在BaseAdapter中使用时，不能对根节点View使用setTag操作�?
 * @author jian.fu
 *
 */
public class ViewHolder {
	
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view,int id){
	 	 SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
         if (viewHolder == null) {
             viewHolder = new SparseArray<View>();
             view.setTag(viewHolder);
         }
         View childView = viewHolder.get(id);
         if (childView == null) {
             childView = view.findViewById(id);
             viewHolder.put(id, childView);
         }
         return (T) childView;
	}
}
