package edu.stanford.cs108.worldcreator;

import java.util.Vector;
import java.util.StringTokenizer;
import android.media.MediaPlayer;

public class Script{
	public Object parent;
	//these arrays are of length 4 and contain targets for goto, play, show, and hide, respectively
	public Vector<Object>[] onClick;
	public Vector<Object>[] onDrop;
	public Vector<Object>[] onEnter;
	
	
	public Script(String input) {
		for(int i = 0; i < 4; i++) {
			onClick[i] = new Vector<Object>();
			onDrop[i] = new Vector<Object>();
			onEnter[i] = new Vector<Object>();
		}
		String[] strs = input.split(";");
		for(String str : strs) {
			str.trim();
			StringTokenizer st = new StringTokenizer(str);
			handleTokens(st);
		}
	}
	
	private void handleTokens(StringTokenizer st) {
		st.nextToken(); //ignore "on" before click drop or add
		String action = st.nextToken();
		while(st.hasMoreTokens()) {
			String command = st.nextToken();
			String target = st.nextToken();
			switch(action) {
			case "click": 
				addToOnClick(command, target);
				break;
			case "drop":
				addToOnDrop(command, target);
				break;
			case "enter":
				addToOnEnter(command, target);
				break;
			}
		}
	}
	
	public Vector<Object>[] getOnClickActions() { return onClick;}
	public Vector<Object>[] getOnDropActions() { return onDrop;}
	public Vector<Object>[] getOnEnterActions() { return onClick;}
	
	public void addToOnClick(String command, String target) {
		switch(command) {
		case "goto": 
			onClick[0].add(Game.curGame.getPage(target));
		case "play":
//			onClick[1].add(Game.curGame.getSound(target));
		case "hide":
			onClick[2].add(Game.curGame.getShape(target));
		case "show":
			onClick[3].add(Game.curGame.getShape(target));
		}
	}
	
	public void addToOnDrop(String command, String target) {
		switch(command) {
		case "goto": 
			onDrop[0].add(Game.curGame.getPage(target));
		case "play":
//			onDrop[1].add(Game.curGame.getSound(target));
		case "hide":
			onDrop[2].add(Game.curGame.getShape(target));
		case "show":
			onDrop[3].add(Game.curGame.getShape(target));
		}
	}
	
	public void addToOnEnter(String command, String target) {
		switch(command) {
		case "goto": 
			onEnter[0].add(Game.curGame.getPage(target));
		case "play":
//			onEnter[1].add(Game.curGame.getSound(target));
		case "hide":
			onEnter[2].add(Game.curGame.getShape(target));
		case "show":
			onEnter[3].add(Game.curGame.getShape(target));
		}
	}
}
