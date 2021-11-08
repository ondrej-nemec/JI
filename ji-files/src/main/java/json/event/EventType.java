package json.event;

public enum EventType {
	
	OBJECT_START,
	OBJECT_END,
	OBJECT_ITEM,
	LIST_START,
	LIST_END,
	LIST_ITEM,
	EMPTY
//	DOCUMENT_START, // TODO can be removed and replaced with object start
//	DOCUMENT_END // will be only for empty documents
	;
}
