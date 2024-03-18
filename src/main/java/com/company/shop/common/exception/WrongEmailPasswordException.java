package com.company.shop.common.exception;

public class WrongEmailPasswordException extends RuntimeException{

	public WrongEmailPasswordException(String message) {
		super(message);
	}	
	
}
