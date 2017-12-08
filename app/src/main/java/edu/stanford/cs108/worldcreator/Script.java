package edu.stanford.cs108.worldcreator;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.StringTokenizer;
import android.media.MediaPlayer;
import android.util.Log;

public class Script{
	//these arrays are of length 4 and contain targets for goto, play, show, and hide, respectively
	private Vector<Vector<Object>> onClick;
	private Map<Shape, Vector<Vector<Object>>> onDrop;
	private Vector<Vector<Object>> onEnter;
	private String scriptString;
	
	
	public Script(String input) {
		scriptString = input;
		onClick = new Vector<Vector<Object>>();
		onEnter = new Vector<Vector<Object>>();
		onDrop = new HashMap<Shape, Vector<Vector<Object>>>();
		for(int i = 0; i < 4; i++) {
			onClick.add(i, new Vector<Object>());
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
		if(action == "drop"){
			String shape = st.nextToken();
			Shape toBeDropped = Game.curGame.getShape(shape);
			onDrop.put(toBeDropped, new Vector<Vector<Object>>());
			while(st.hasMoreTokens()) {
				String command = st.nextToken();
				String target = st.nextToken();
				addToOnDrop(toBeDropped, command, target);
			}
			return;
		}
		while(st.hasMoreTokens()) {
			String command = st.nextToken();
			String target = st.nextToken();
			switch(action) {
			case "click": 
				addToOnClick(command, target);
				break;
			case "enter":
				addToOnEnter(command, target);
				break;
			}
		}
	}
	
	public Vector<Vector<Object>> getOnClickActions() { return onClick;}
	public Vector<Vector<Object>> getOnDropActions(Shape beingDropped) { return onDrop.get(beingDropped);}
	public Vector<Vector<Object>> getOnEnterActions() { return onClick;}
	public String getScriptString(){ return scriptString;}
	public boolean droppable(Shape toBeDropped){
		return onDrop.containsKey(toBeDropped);
	}
	
	public void addToOnClick(String command, String target) {
		switch(command) {
		case "goto":
			onClick.elementAt(0).add(Game.curGame.getPage(target));
		case "play":
//			onClick[1].add(Game.curGame.getSound(target));
		case "hide":
			onClick.elementAt(2).add(Game.curGame.getShape(target));
		case "show":
			onClick.elementAt(3).add(Game.curGame.getShape(target));
		}
	}

	public void addToOnDrop(Shape key, String command, String target) {
		switch(command) {
		case "goto":
			onDrop.get(key).elementAt(0).add(Game.curGame.getPage(target));
		case "play":
			//onDrop.get(key).elementAt(0).add(Game.curGame.getSound(target));
		case "hide":
			onDrop.get(key).elementAt(2).add(Game.curGame.getShape(target));
		case "show":
			onDrop.get(key).elementAt(3).add(Game.curGame.getShape(target));
		}
	}

	public void addToOnEnter(String command, String target) {
		switch(command) {
		case "goto":
			onEnter.elementAt(0).add(Game.curGame.getPage(target));
		case "play":
//			onEnter[1].add(Game.curGame.getSound(target));
		case "hide":
			onEnter.elementAt(2).add(Game.curGame.getShape(target));
		case "show":
			onEnter.elementAt(0).add(Game.curGame.getShape(target));
		}
	}
}
