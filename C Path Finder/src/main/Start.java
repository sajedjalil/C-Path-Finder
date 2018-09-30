package main;

import java.io.File;

import inputCodeBeautifier.CodeBeautifier;
import inputFileLoader.InputFileCopyMachine;

public class Start {

	public static void main(String[] args) {
		
		new Start();
	}
	
	
	public Start() {
		
		init();
	}
	
	
	private void init() {
		long startTime = System.nanoTime();
		/** */
		new InputFileCopyMachine("D:\\eclipse-workspace\\Datasets", "D:\\eclipse-workspace\\Results");
		
		long endTime   = System.nanoTime();
		System.out.println(endTime-startTime);
		/** */
		new CodeBeautifier( new File("D:\\eclipse-workspace\\Results"));
		
		endTime = System.nanoTime();
		System.out.println(endTime-startTime);
		
	}
}
