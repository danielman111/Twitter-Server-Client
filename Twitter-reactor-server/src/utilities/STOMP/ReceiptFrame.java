package utilities.STOMP;

public class ReceiptFrame extends StompFrame{

		
	protected ReceiptFrame(String wholeMessage) {
		super(wholeMessage);
	}

	
	public static ReceiptFrame factory(String id) {
		String message= formatIntoReceiptMessage(id);
		int endlineIndex= message.indexOf(StompFrame.endlineChar);
		String headlessMessage= message.substring(endlineIndex + StompFrame.lenghtOfEndlineChar);
		return new ReceiptFrame(headlessMessage);
	}
	
	
	public static String formatIntoReceiptMessage(String id){
		StringBuilder sb= new StringBuilder("RECEIPT");
		sb.append(StompFrame.endlineChar);
		sb.append("receipt-id:").append(id).append(StompFrame.endlineChar).append(StompFrame.endlineChar).append(StompFrame.nullChar);
		return sb.toString();
	}
	
	
	public String getSTOMPType() {
		return "RECEIPT";
	}
	
	
	public String getId() {
		return new String(super._headers.get("id"));
	}
		

}
