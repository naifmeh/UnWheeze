package com.unwheeze.utils;

public class JsonErrorStatus {
	
	public static final String errorDbNotInit = "{ 'errorStatus':' Db not initialized' }";
	public static final String errorCodeInsertion = "{ 'errorStatus':' User could not be added' }";
	public static final String errorEmailInDb = "{ 'errorStatus':' Email already in database' }";
	public static final String errorEmailNotInDb = "{ 'errorStatus':' Email not found in database' }";
	public static final String errorInvalidFormatEmail = "{ 'errorStatus':' Invalid email format' }";
	public static final String errorIncorrectCred = "{ 'errorStatus':' Incorrect credentials' }";
	public static final String errorInvalidAuthTkn = "{ 'errorStatus':' Incorrect authorization token'}";
}
