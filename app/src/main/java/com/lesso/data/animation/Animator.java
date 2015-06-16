package com.lesso.data.animation;

import android.view.View;

public abstract class Animator {

	public interface AnimatorCallback {
		void callback();
	}

	public abstract void init(View layout);

	public abstract void start(AnimatorCallback callback);

	public abstract void dismiss(AnimatorCallback callback);
}
