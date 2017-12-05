package edu.stanford.cs108.worldcreator;

import java.util.Vector;

public class Script{
	public Object parent;
	//these arrays are of length 4 and contain targets for goto, play, show, and hide, respectively
	public Vector<Object>[] onClick;
	public Vector<Object>[] onDrop;
	public Vector<Object>[] onEnter;
	
	public Script(Object obj) {
		parent = obj;
		for(int i = 0; i < 4; i++) {
			onClick[i] = new Vector<Object>();
			onDrop[i] = new Vector<Object>();
			onEnter[i] = new Vector<Object>();
		}
	}
	
	public Vector<Object>[] getOnClickActions() { return onClick;}
	public Vector<Object>[] getOnDropActions() { return onDrop;}
	public Vector<Object>[] getOnEnterActions() { return onClick;}
	
	public void addToOnClick(String command, Object target) {
		switch(command) {
		case "goto": 
			onClick[0].add(target);
		case "play":
			onClick[1].add(target);
		case "hide":
			onClick[2].add(target);
		case "show":
			onClick[3].add(target);
		}
	}
	
	public void addToOnDrop(String command, Object target) {
		switch(command) {
		case "goto": 
			onDrop[0].add(target);
		case "play":
			onDrop[1].add(target);
		case "hide":
			onDrop[2].add(target);
		case "show":
			onDrop[3].add(target);
		}
	}
	
	public void addToOnEnter(String command, Object target) {
		switch(command) {
		case "goto": 
			onEnter[0].add(target);
		case "play":
			onEnter[1].add(target);
		case "hide":
			onEnter[2].add(target);
		case "show":
			onEnter[3].add(target);
		}
	}
}
