package edu.stanford.cs108.worldcreator;

import java.util.Vector;
import java.util.StringTokenizer;
import android.media.MediaPlayer;
import android.util.Log;

public class Script{
	public Object parent;
	//these arrays are of length 4 and contain targets for goto, play, show, and hide, respectively
	public Vector<Vector<Object>> onClick;
	public Vector<Vector<Object>> onDrop;
	public Vector<Vector<Object>> onEnter;
	public String scriptString;
	
	
	public Script(String input) {
		scriptString = input;
		onClick = new Vector<Vector<Object>>();
		onEnter = new Vector<Vector<Object>>();
		onDrop = new Vector<Vector<Object>>();
		for(int i = 0; i < 4; i++) {
			onClick.add(i, new Vector<Object>());
			onDrop.add(i, new Vector<Object>());
			onEnter.add(i, new Vector<Object>());
		}
		Log.d("MESSAGE", input);
		String[] strs = input.split(";");
			for (String str : strs) {
				str.trim();
				StringTokenizer st = new StringTokenizer(str);
				handleTokens(st);
			}
	}
	
	private void handleTokens(StringTokenizer st) {
		if (!st.hasMoreTokens()) return;
		st.nextToken();
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
	
	public Vector<Vector<Object>> getOnClickActions() { return onClick;}
	public Vector<Vector<Object>> getOnDropActions() { return onDrop;}
	public Vector<Vector<Object>> getOnEnterActions() { return onClick;}
	public String getScriptString(){ return scriptString;}
	
	public void addToOnClick(String command, String target) {
		switch(command) {
		case "goto":
			onClick.elementAt(0).add(Game.curGame.getPage(target));
			//onClick[0].add(Game.curGame.getPage(target));
		case "play":
//			onClick[1].add(Game.curGame.getSound(target));
		case "hide":
			onClick.elementAt(2).add(Game.curGame.getShape(target));
			//onClick[2].add(Game.curGame.getShape(target));
		case "show":
			onClick.elementAt(3).add(Game.curGame.getShape(target));
			//onClick[3].add(Game.curGame.getShape(target));
		}
	}
	
	public void addToOnDrop(String command, String target) {
		switch(command) {
		case "goto":
			onDrop.elementAt(0).add(Game.curGame.getPage(target));
			//onDrop[0].add(Game.curGame.getPage(target));
		case "play":
//			onDrop[1].add(Game.curGame.getSound(target));
		case "hide":
			onDrop.elementAt(2).add(Game.curGame.getShape(target));
			//onDrop[2].add(Game.curGame.getShape(target));
		case "show":
			onDrop.elementAt(3).add(Game.curGame.getShape(target));
			//onDrop[3].add(Game.curGame.getShape(target));
		}
	}
	
	public void addToOnEnter(String command, String target) {
		switch(command) {
		case "goto":
			onEnter.elementAt(0).add(Game.curGame.getPage(target));
			//onEnter[0].add(Game.curGame.getPage(target));
		case "play":
//			onEnter[1].add(Game.curGame.getSound(target));
		case "hide":
			onEnter.elementAt(2).add(Game.curGame.getShape(target));
			//onEnter[2].add(Game.curGame.getShape(target));
		case "show":
			onEnter.elementAt(0).add(Game.curGame.getShape(target));
			//onEnter[3].add(Game.curGame.getShape(target));
		}
	}
}
