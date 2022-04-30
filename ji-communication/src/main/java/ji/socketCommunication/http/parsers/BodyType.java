package ji.socketCommunication.http.parsers;

public enum BodyType {

	EMPTY,
//	PLAIN_TEXT,
//	BINARY,
//	@Deprecated
	PLAIN_TEXT_OR_BINARY,
	FORM_DATA,
	URLENCODED_DATA,
	JSON;
	
}
