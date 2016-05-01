package lock;

import java.util.Iterator;
import java.util.NoSuchElementException;

/****************************************
 * En klass som innehåller metoder för att
 * lägga till, ta bort, hämta, sätta, söka
 * eller rensa noder i en länkad lista.
 * @author Peter Johansson den 9/2 2014
 ****************************************/
public class LinkedList<E> implements List<E>, Iterable<E> {
	private ListNode<E> list = null;

	/**
	 * Lokaliserar en node i en länkad lista med en for-loop.
	 * Returnerar värdet i noden och referens till nästa node.
	 * @param index angiven plats
	 * @return en node
	 **/
	private ListNode<E> locate( int index ) {
		ListNode<E> node = list;
		for( int i = 0; i < index; i++)
			node = node.getNext();
		return node;
	}

	/**
	 * Räknar ut storleken på den länkade listan genom
	 * att räkna alla noder och returnera antalet.
	 * @return antalet noder
	 **/
	public int size() {
		int n = 0;
		ListNode<E> node = list;
		while( node != null ) {
			node = node.getNext();
			n++;
		}
		return n;
	}

	/**
	 * Får ett index som parameter för att kunna hämta en node
	 * i den länkade listan. Index kollas innan värdet returneras.
	 * @param index angiven plats
	 * @return värdet i noden
	 **/
	public E get( int index ) {
		if( ( index < 0 ) || ( index >= size() ) ) {
			throw new IndexOutOfBoundsException( "size=" + size() + ", index=" + index );
		}
		ListNode<E> node = locate( index );
		return node.getData();
	}

	/**
	 * Får ett index och ett värde som parameter för att
	 * kunna skriva in värdet på rätt plats i den
	 * länkade listan. Metoden kollar index och sen sparas det
	 * gamla värdet tillfälligt i en ny variabel som sen
	 * returneras efter att det nya värdet har sparats.
	 * @param index angiven plats
	 * @param data valt värde
	 * @return värdet i noden
	 **/
	public E set( int index, E data ) { 
		if( index < 0 || index >= size() ) {
			throw new IndexOutOfBoundsException( "size=" + size() + ", index=" + index );
		}

		E temp = null;
		if( index == 0 ) {
			temp = list.getData();
			list.setData( data );
		} else {
			ListNode<E> node = locate( index );
			temp = node.getData();
			node.setData( data );
			node.setNext( node.getNext() );
		}
		return temp;
	}

	/**
	 * Får data som parameter och datan skickas vidare till add() 
	 * metoden med ett index som anger sist i den länkade listan.
	 * @param data valt värde
	 **/
	public void add( E data ) {
		add( size(), data );
	}

	/**
	 * Får data som parameter och datan skickas vidare till add() 
	 * metoden med ett index som anger först i den länkade listan.
	 * @param data valt värde
	 **/
	public void addFirst( E data ) {
		add( 0, data );
	}

	/**
	 * Får data som parameter och datan skickas vidare till add() 
	 * metoden med ett index som anger sist i den länkade listan.
	 * @param data valt värde
	 **/
	public void addLast( E data ) {
		add( size(), data );
	}

	/**
	 * Får ett index och data som parameter och datan läggs till
	 * på angiven plats i den länkade listan. Metoden kollar
	 * index och vart i listan datan ska läggas med en if else-sats.
	 * Den nya noden kopplas ihop med noden innan och med noden efter.
	 * @param index angiven plats
	 * @param data valt värde
	 **/
	public void add( int index, E data ) {
		if( ( index < 0 ) || ( index > size() ) ){
			throw new IndexOutOfBoundsException( "size=" + size() + ", index=" + index );
		}
		if( index == 0 ) {
			list = new ListNode<E>( data, list );
		} else {
			ListNode<E> node = locate( index - 1 );
			ListNode<E> newNode = new ListNode<E>( data, node.getNext() );
			node.setNext( newNode );
		}
	}

	/**
	 * Tar bort den första noden i den länkade listan
	 * genom att anropa metoden remove() med ett index
	 * som anger första platsen och returnerar värdet.
	 * @return värdet i elementet
	 **/
	public E removeFirst() {
		return remove( 0 );
	}

	/**
	 * Tar bort den sista noden i den länkade listan
	 * genom att anropa metoden remove() med ett index
	 * som anger sista platsen och returnerar värdet.
	 * @return värdet i elementet
	 **/
	public E removeLast() {
		return remove( size() - 1 );
	}

	/**
	 * Får ett index som parameter för att kunna ta bort noden
	 * ur den länkade listan på angiven plats. Metoden kollar
	 * index och vart i listan datan ska läggas med en
	 * if else-sats. Sen sparas värdet tillfälligt i en ny node
	 * för att kunna nulla noden i listan och returnera värdet.
	 * @param index angiven plats
	 * @return värdet i noden
	 **/
	public E remove( int index ) {
		if( ( index < 0 ) || ( index >= size() ) ) {
			throw new IndexOutOfBoundsException( "size=" + size() + ", index=" + index );
		}
		E res;
		if( index == 0 ) {
			res = list.getData();
			list.setData( null );
			list = list.getNext();
		} else {
			ListNode<E> node = locate( index - 1 );
			res = node.getNext().getData();
			node.getNext().setData( null );
			node.setNext( node.getNext().getNext() );
		}
		return res;
	}

	/**
	 * Tar bort alla noder i den länkade listan genom att anropa
	 * metoden remove() i en for-loop  med ett index som börjar
	 * från sista noden och går framåt till alla platser i listan.
	 **/
	public void clear() {
		list = null;
	}

	/**
	 * Får ett värde som parameter och skickar det vidare
	 * till indexOf() metoden med ett index som börjar från
	 * första noden. Sen returneras positionen för noden.
	 * @param data valt värde
	 * @return en position eller -1 ifall positionen inte finns
	 **/
	public int indexOf(E data) {
		return indexOf( 0, data );
	}

	/**
	 * Får ett index och ett värde som parameter för att kunna
	 * hitta den sökta nodens position. Metoden kollar index
	 * och sen anropas metoden locate() för att börja sökningen
	 * på rätt position. Sen jämförs alla värden med equals. Sist
	 * returneras positionen för noden eller -1 ifall värdet inte finns.
	 * @param startIndex angiven plats
	 * @param data valt värde
	 * @return en position eller -1 ifall positionen inte finns
	 **/
	public int indexOf(int startIndex, E data) {
		if( startIndex < 0 || startIndex > size() )  {
			throw new IndexOutOfBoundsException();
		}
		ListNode<E> node = locate( startIndex );
		for( int i = startIndex; i < size(); i++ ) {
			if( node.getData().equals( data ) ) {
				return i;
			}
			node = node.getNext();
		}
		return -1;
	}

	/**
	 * Returnerar en iterator som kan loopa genom den länkade listan.
	 * @return ett nytt Iter objekt
	 **/
	public Iterator<E> iterator() {
		return new Iter();
	}    

	/**
	 * Kollar listan och skriver ut alla värden på ett specifikt sätt.
	 * @return en sträng
	 **/
	public String toString() {
		if( list != null ) {
			return list.toString();
		}
		return "[]";
	}

	/**
	 * En privat klass med en iterator
	 * som kan loopa genom den länkade listan
	 **/
	private class Iter implements Iterator<E> {
		ListNode<E> node = list;
		int index = 0;

		/**
		 * Returnerar svaret på om det finns fler noder.
		 * @return sant eller falskt
		 **/
		public boolean hasNext() {
			return index < size();
		}

		/**
		 * Kollar om det finns fler noder och sen var man är i listan.
		 * Om det är första noden så ökas index med 1 och sen
		 * returneras listans värde. Annars ökas index med 1 och nästa
		 * node sparas i en variabel för att kunna returnera värdet. 
		 * @return värdet i noden
		 **/
		public E next() {
			if( !hasNext() ) {
				throw new NoSuchElementException();
			}
			if( index == 0 ) {
				index++;
				return node.getData();
			} else {
				index++;
				node = node.getNext();
				return node.getData();
			}
		}

		/**
		 * Stöds inte utan tas bara med som tvång och kastas.
		 **/
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
