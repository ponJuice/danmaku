package com.example.danmaku.Transition;

import android.graphics.Bitmap;

import com.example.danmaku.GameManager;
import com.example.danmaku.Screen.Screenable;
import com.example.opengles20util.core.GLES20Util;
import com.example.opengles20util.graphic.blending_mode.GLES20COMPOSITION_ALPHA;

public class LoadingTransition implements Transitionable {
	private static LoadingTransition instance;

	public static LoadingTransition getInstance(){
		if(instance == null)
			instance = new LoadingTransition();
		return instance;
	}
	private Class<?> nextScreenClass;
	private Screenable nextScreen;
	public void setNextScreen(Class<?> nextScreen) {
		this.nextScreenClass = nextScreen;
		this.nextScreen = null;
	}
	public void setNextScreen(Screenable nextScreen){
		this.nextScreenClass = null;
		this.nextScreen = nextScreen;
	}
	public void setSleepTime(int n){
		sleepTime = n;
	}
	private int sleepTime = 0;
	private float alpha = 0;
	private float deltaAlpha =0;
	private int mode = 0;
	private Bitmap bitmap = GLES20Util.createBitmap(255, 0, 0, 255);
	private int transitionTime = 60;

	@Override
	public boolean Transition() {
		if(mode == 0){
			deltaAlpha = 1f/(float)transitionTime;
			mode = 1;
		}
		if(mode == 1){
			GameManager.nowScreen.Draw(0,0);
			alpha += deltaAlpha;
			GLES20Util.DrawGraph(0, 0, GLES20Util.getAspect()*2f, 2f, bitmap,alpha,GLES20COMPOSITION_ALPHA.getInstance());
			GLES20Util.DrawString("NowLoading...", 1, 255,255,255,alpha, 0.5f, 0,GLES20COMPOSITION_ALPHA.getInstance());
			transitionTime--;
			if(transitionTime <= 0)
				mode = 2;
			return true;
		}
		else if(mode == 2){
			GLES20Util.DrawGraph(0, 0, GLES20Util.getAspect()*2f, 2f, bitmap,1f,GLES20COMPOSITION_ALPHA.getInstance());
			GLES20Util.DrawString("NowLoading...", 1, 255,255,255,1f, 0.5f, 0,GLES20COMPOSITION_ALPHA.getInstance());
			try {
				if(nextScreenClass != null)
					GameManager.nowScreen  = (Screenable)(nextScreenClass.newInstance());
				else
					GameManager.nowScreen = nextScreen;
			} catch (InstantiationException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			mode = 3;
			alpha = 1f;
			transitionTime = 30;
			deltaAlpha = 1f/(float)transitionTime;
			return true;
		}
		else if(mode == 3){
			GameManager.nowScreen.Draw(0,0);
			GLES20Util.DrawGraph(0, 0, GLES20Util.getAspect()*2f, 2f, bitmap,alpha,GLES20COMPOSITION_ALPHA.getInstance());
			GLES20Util.DrawString("NowLoading...", 1, 255,255,255,alpha, 0.5f, 0,GLES20COMPOSITION_ALPHA.getInstance());
			alpha -= deltaAlpha;
			transitionTime--;
			if(transitionTime <= 0)
				mode = 4;
			return true;
		}
		else{
			GameManager.nowScreen.Draw(0,0);
			mode = 0;
			alpha = 0;
			deltaAlpha = 0;
			nextScreen = null;
			return false;
		}
	}

	public void setTransitionTime(int transitionTime) {
		this.transitionTime = transitionTime;
	}

}
