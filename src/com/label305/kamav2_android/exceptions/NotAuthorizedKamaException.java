package com.label305.kamav2_android.exceptions;

public class NotAuthorizedKamaException extends KamaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6339478344217519840L;

	public NotAuthorizedKamaException(Exception e) {
		super(e);
	}
	
	public NotAuthorizedKamaException(Exception e, Object errorObj) {
		super(e, errorObj);
	}

	public NotAuthorizedKamaException(String e) {
		super(e);
	}
	
	public NotAuthorizedKamaException(String e, Object errorObj) {
		super(e, errorObj);
	}

}
