package com.label305.kamav2_android.exceptions;

public class JsonKamaException extends KamaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2255430502927282272L;

	public JsonKamaException(Exception e) {
		super(e);
	}

    public JsonKamaException(String message){
        super(message);
    }

}
