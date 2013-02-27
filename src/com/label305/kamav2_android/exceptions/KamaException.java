package com.label305.kamav2_android.exceptions;

public class KamaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8374881555005292022L;

	public KamaException(Exception e) {
		super(e);
	}
	
	public KamaException(String e) {
		super(e);
	}

}
