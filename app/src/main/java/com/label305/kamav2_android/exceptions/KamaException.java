package com.label305.kamav2_android.exceptions;

public class KamaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8374881555005292022L;
	
	private Object mErrorObj;
	
	private int httpStatusCode;

	public KamaException(Exception e) {
		super(e);
	}
	
	public KamaException(Exception e, Object errorObj) {
		super(e);
		
	}
	
	public KamaException(String e, Object errorObj) {
		super(e);
		mErrorObj = errorObj;
	}
	
	public KamaException(String e) {
		super(e);
	}

	public Object getErrorObj() {
		return mErrorObj;
	}

	/**
	 * @return the httpStatusCode
	 */
	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	/**
	 * @param httpStatusCode the httpStatusCode to set
	 */
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

}
