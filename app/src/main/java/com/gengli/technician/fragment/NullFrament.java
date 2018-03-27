package com.gengli.technician.fragment;

import com.gengli.technician.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NullFrament extends Fragment {

	private View mViewContent; // 缓存视图内容

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mViewContent == null) {
			mViewContent = inflater.inflate(R.layout.fragment_null, container, false);
		}

		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
		ViewGroup parent = (ViewGroup) mViewContent.getParent();
		if (parent != null) {
			parent.removeView(mViewContent);
		}
		return mViewContent;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}