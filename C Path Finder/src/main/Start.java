package main;

import java.io.File;

import codeBeautify.CodeBeautifier;
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
		
		new InputFileCopyMachine("E:\\Datasets", "E:\\Results");
		
		long endTime   = System.nanoTime();
		System.out.println(endTime-startTime);
		
		new CodeBeautifier( new File("E:\\Results"));
		
		endTime = System.nanoTime();
		System.out.println(endTime-startTime);
	}
}
