package edu.stanford.cs108.worldcreator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.StringTokenizer;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

public class Script{
	//these arrays are of length 4 and contain targets for goto, play, show, and hide, respectively
	private Vector<Vector<Object>> onClick;
	private Map<Shape, Vector<Vector<Object>>> onDrop;
	private Vector<Vector<Object>> onEnter;
	private String scriptString;
    public static Vector<String> media;
    private boolean valid;
	
	
	public Script(String input) {
        valid = true;
		scriptString = input;
        Log.d("MESSAGE", "Script: ORIGINAL: " + input);
        onClick = new Vector<Vector<Object>>();
		onEnter = new Vector<Vector<Object>>();
		onDrop = new HashMap<Shape, Vector<Vector<Object>>>();
		for(int i = 0; i < 4; i++) {
			onClick.add(i, new Vector<Object>());
			onEnter.add(i, new Vector<Object>());
		}
		String[] strs = input.split(";");
		for (String str : strs) {
			Log.d("MESSAGE", "Script: SPLIT: " + str);
			StringTokenizer st = new StringTokenizer(str);
			handleTokens(st);
		}
	}
	
	private void handleTokens(StringTokenizer st) {
		if (!st.hasMoreTokens()) return;
		String first = st.nextToken();
		if(!first.equals("on")) {
			if (MainActivity.loadingFlag) return;
			Toast.makeText(MainActivity.curContext, "Invalid script: trigger must begin with 'on'",
					Editor.TOAST_LENGTHS).show();
            valid = false;
			return;
		}
		String action = st.nextToken();
		if(action.equals("drop")){
			String shape = st.nextToken();
            Log.d("MESSAGE", "Script: DROP: " + shape);
			Shape toBeDropped = Game.curGame.getShape(shape);
			if(toBeDropped == null) {
				showShapeFailToast(shape);
                valid = false;
				return;
			}
			Vector<Vector<Object>> temp = new Vector<Vector<Object>>();
			for(int i = 0; i < 4; i++) temp.add(i, new Vector<Object>());
			onDrop.put(toBeDropped, temp);
			while(st.hasMoreTokens()) {
				String command = st.nextToken();
				String target = st.nextToken();
                Log.d("MESSAGE", "Script: COMMAND: " + command + " TARGET: " + target);
				addToOnDrop(toBeDropped, command, target);
			}
			return;
		}

		if(!action.equals("click") && !action.equals("enter")) {
			if (MainActivity.loadingFlag) return;
			Toast.makeText(MainActivity.curContext, "Invalid script: no trigger on "
					+ action, Editor.TOAST_LENGTHS).show();
			Log.d("MESSAGE", "handleTokens: action isnt click or enter");
			valid = false;
			return;
		}
		while(st.hasMoreTokens()) {
			String command = st.nextToken();
			String target = st.nextToken();
			switch(action) {
			case "click":
                Log.d("MESSAGE", "Script: CLICK: " + "COMMAND: " + command + " TARGET: " + target);
				addToOnClick(command, target);
				break;
			case "enter":
                Log.d("MESSAGE", "Script: ENTER: " + "COMMAND: " + command + " TARGET: " + target);
				addToOnEnter(command, target);
				break;
			}
		}
	}
	
	public Vector<Vector<Object>> getOnClickActions() { return onClick;}
	public Vector<Vector<Object>> getOnDropActions(Shape beingDropped) { return onDrop.get(beingDropped);}
	public Vector<Vector<Object>> getOnEnterActions() { return onEnter;}
	public String getScriptString(){ return scriptString;}
	public boolean droppable(Shape toBeDropped){
		return onDrop.containsKey(toBeDropped);
	}
	public boolean getValid() {return valid;}


	private void showCmdFailToast(String command) {
		if (MainActivity.loadingFlag) return;
		Toast.makeText(MainActivity.curContext, "Invalid script: no command of name "
						+ command, Editor.TOAST_LENGTHS).show();
        valid = false;
		Log.d("MESSAGE", "showCmdFailToast: ");
	}
	private void showPageFailToast(String target /*page*/) {
		if (MainActivity.loadingFlag) return;
		Toast.makeText(MainActivity.curContext, "Invalid script: page " + target
				+ " could not be found", Editor.TOAST_LENGTHS).show();
        valid = false;
		Log.d("MESSAGE", "showPageFailToast: ");
	}
	private void showSoundFailToast(String target /*sound*/) {
		if (MainActivity.loadingFlag) return;
		Toast.makeText(MainActivity.curContext, "Invalid script: sound " + target
				+ " could not be found", Editor.TOAST_LENGTHS).show();
        valid = false;
		Log.d("MESSAGE", "showSoundFailToast: ");
	}
	private void showShapeFailToast(String target /*shape*/) {
		if (MainActivity.loadingFlag) return;
		Toast.makeText(MainActivity.curContext, "Invalid script: shape " + target
				+ " could not be found", Editor.TOAST_LENGTHS).show();
        valid = false;
		Log.d("MESSAGE", "showShapeFailToast: ");
	}

	public void addToOnClick(String command, String target) {
		if(!command.equals("goto") && !command.equals("play") && !command.equals("hide") && !command.equals("show")) {
			showCmdFailToast(command);
			return; //TODO EXPAND TO ALL POSSIBLE ADDED COMMANDS
		}
		switch(command) {
		case "goto":
			Page page = Game.curGame.getPage(target);
			if(page == null) {
				showPageFailToast(target);
				return;
			}
			onClick.elementAt(0).add(page);
			break;
		case "play":
            if (!media.contains(target)) return;
            MediaPlayer mp = Game.curGame.getSound(target);
            if(mp == null) {
				showSoundFailToast(target);
				return;
			}
			onClick.elementAt(1).add(mp);
			break;
		case "hide":
			Shape shape = Game.curGame.getShape(target);
			if(shape == null) {
				showShapeFailToast(target);
				return;
			}
			onClick.elementAt(2).add(shape);
			break;
		case "show":
			Shape shape2 = Game.curGame.getShape(target);
			if(shape2 == null) {
				showShapeFailToast(target);
				return;
			}
			onClick.elementAt(3).add(shape2);
			break;
		}
	}

	public void addToOnDrop(Shape key, String command, String target) {
		if(!command.equals("goto") && !command.equals("play") && !command.equals("hide") && !command.equals("show")) {
			showCmdFailToast(command);
			return; //TODO: Make more robust
		}
		switch(command) {
		case "goto":
			Page page = Game.curGame.getPage(target);
			if(page == null) {
				showPageFailToast(target);
				return;
			}
			onDrop.get(key).elementAt(0).add(page);
			break;
		case "play":
		    if (!media.contains(target)) return;
			MediaPlayer mp = Game.curGame.getSound(target);
			if(mp == null) {
				showSoundFailToast(target);
				return;
			}
			onDrop.get(key).elementAt(1).add(mp);
			break;
		case "hide":
			Shape shape = Game.curGame.getShape(target);
			if(shape == null) {
				showShapeFailToast(target);
				return;
			}
			onDrop.get(key).elementAt(2).add(shape);
			break;
		case "show":
			Shape shape2 = Game.curGame.getShape(target);
			if(shape2 == null) {
				showShapeFailToast(target);
				return;
			}
			onDrop.get(key).elementAt(3).add(shape2);
			break;
		}
	}

	public void addToOnEnter(String command, String target) {
		if(!command.equals("goto") && !command.equals("play") && !command.equals("hide") && !command.equals("show")) {
			showCmdFailToast(command);
			return;
		}
		switch(command) {
			case "goto":
				Page page = Game.curGame.getPage(target);
				if(page == null) {
					showPageFailToast(target);
					return;
				}
				onEnter.elementAt(0).add(page);
				break;
			case "play":
                if (!media.contains(target)) return;
                MediaPlayer mp = Game.curGame.getSound(target);
                if(mp == null) {
					showSoundFailToast(target);
					return;
				}
				onEnter.elementAt(1).add(mp);
				break;
			case "hide":
				Shape shape = Game.curGame.getShape(target);
				if(shape == null) {
					showShapeFailToast(target);
					return;
				}
				onEnter.elementAt(2).add(shape);
				break;
			case "show":
				Shape shape2 = Game.curGame.getShape(target);
				if(shape2 == null) {
					showShapeFailToast(target);
					return;
				}
				onEnter.elementAt(3).add(shape2);
				break;
		}
	}
}
