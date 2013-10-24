package com.peter.rogue.tween;

import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteAccessor implements TweenAccessor<Sprite>{

	public static final int ALPHA = 0;
	
	@Override
	public int getValues(Sprite arg0, int arg1, float[] arg2) {
		switch(arg1){
		case ALPHA:
			arg2[0] = arg0.getColor().a;
			return 1;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Sprite arg0, int arg1, float[] arg2) {
		switch(arg1){
		case ALPHA:
			arg0.setColor(arg0.getColor().r, arg0.getColor().g, arg0.getColor().b, arg2[0]);
			break;
		default:
			assert false;
		}
	}

}
