package lock;

/****************************************
 * En klass som innehåller metoder för
 * att hämta eller sätta värdet i noder
 * och nästa node i en länkad lista.
 * @author Peter Johansson den 9/2 2014
 ****************************************/
public class ListNode<E> {
    private E data;
    private ListNode<E> next;
    
    /**
	 * Konstruktorn tar emot ett värde
	 * och referensen till nästa node.
	 * @param data valt värde
	 * @param next referens till nästa node
	 **/
    public ListNode( E data, ListNode<E> next ) {
        this.data = data;
        this.next = next;
    }
    
    /**
   	 * Returnerar värdet i noden.
   	 * @return värdet i noden
   	 **/
    public E getData() {
        return this.data;
    }
    
    /**
   	 * Sätter värdet i noden.
   	 * @param data valt värde
   	 **/
    public void setData( E data ) {
        this.data = data;
    }
    
    /**
   	 * Returnerar referensen till nästa node.
   	 * @return referensen till nästa node
   	 **/
    public ListNode<E> getNext() {
        return this.next;
    }
    
    /**
   	 * Sätter referensen till nästa node.
   	 **/
    public void setNext( ListNode<E> next ) {
        this.next = next;
    }
    
    /**
	 * Kollar listan och skriver ut alla värden på ett specifikt sätt.
	 * @return en sträng
	 **/
    public String toString() {
    	StringBuilder str = new StringBuilder("[ ");
    	str.append(data.toString());
    	ListNode<E> node = next;
        while( node!=null ) {
        	str.append( "; ");
            str.append( node.getData().toString() );
            node = node.getNext();
        }
        str.append( " ]");
        return str.toString();
    }
}