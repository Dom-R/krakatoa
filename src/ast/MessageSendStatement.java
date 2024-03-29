/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class MessageSendStatement extends Statement {

	public MessageSendStatement(MessageSend messageSend) {
		this.messageSend = messageSend;
	}

   public void genC(PW pw) {
      pw.printIdent("");
      messageSend.genC(pw, false);
      pw.println(";");
   }

   public void genKra(PW pw) {
	   pw.printIdent("");
	   messageSend.genKra(pw, false);
	   pw.println(";");
   }

   public Type getType() {
	   return messageSend.getType();
   }

   public String getName() {
	   return messageSend.getName();
   }

   private MessageSend  messageSend;

}
