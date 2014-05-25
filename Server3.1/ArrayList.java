package lock;

import java.util.Iterator;
import java.util.NoSuchElementException;

/****************************************
 * En klass som innehåller metoder för att
 * lägga till, ta bort, hämta, sätta, söka
 * eller rensa element i en arraylist.
 * @author Peter Johansson den 9/2 2014
 ****************************************/
public class ArrayList<E> implements List<E> {
	private E[] elements;
	private int size;

	/**
	 * Ifall konstruktorn inte får en kapacitet som parameter
	 * så initieras arrayen med en plats för 20 element. 
	 **/
	public ArrayList() {
		this( 20 );
	}

	/**
	 * Konstruktorn initierar arrayen med värdet i
	 * parametern och ett minimum sätts till 1.
	 * @param initialCapacity kapaciteten i arraylistan
	 **/
	public ArrayList(int initialCapacity) {
		initialCapacity = Math.max(1, initialCapacity);
		elements = (E[])new Object[initialCapacity];
	}

	/**
	 * Dubblar arraylistans kapacitet ifall antalet element skulle
	 * överstiga gränsen. Skapar en ny dubbelt så stor array
	 * och överför alla element. Sen ges listan arrayens värde.
	 **/
	private void grow() {
		E[] newArray = ( E[] ) new Object[ elements.length * 2 ];
		for( int i = 0; i < elements.length; i++ ) {
			newArray[ i ] = elements[ i ];
		}
		elements = newArray;
	}

	/**
	 * För ett index och ett element som parameter och det
	 * läggs till på angiven plats i arraylistan. Metoden kollar
	 * index och så att listan inte är full annars anropas
	 * metoden grow() och sen flyttas alla element vid index
	 * framåt innan elementet läggs på plats och size ökar med 1.
	 * @param index angiven plats
	 * @param element valt element
	 **/
	public void add(int index, E element) {
		if(index<0 || index>size)
			throw new IndexOutOfBoundsException();
		if(size==elements.length)
			grow();
		for(int i=size; i>index; i--) {
			elements[i]=elements[i-1];
		}
		elements[index] = element;
		size++;
	}

	/**
	 * Får ett element som parameter och det läggs till
	 * sist i arraylistan. Metoden skickar elementet vidare
	 * till add() metoden med ett index som anger sist i listan.
	 * @param element valt element
	 **/
	public void add( E element ) {
		add( size, element );
	}

	/**
	 * Får ett element som parameter och det läggs till
	 * först i arraylistan. Metoden skickar elementet vidare
	 * till add() metoden med ett index som anger först i listan.
	 * @param element valt element
	 **/
	public void addFirst(E element) {
		add( 0, element );
	}

	/**
	 * Får ett element som parameter och det läggs till
	 * sist i arraylistan. Metoden skickar elementet vidare
	 * till add() metoden med ett index som anger sist i listan.
	 * @param element valt element
	 **/
	public void addLast(E element) {
		add( size, element );
	}

	/**
	 * Får ett index som parameter för att kunna ta bort elementet
	 * ur arraylistan på angiven platsen. Metoden kollar index och 
	 * sen sparas elementet tillfälligt i en ny variabel för att
	 * kunna nulla värdet i listan och returnera det. Sist minskas
	 * size med 1 och alla element vid index flyttas bakåt.
	 * @param index angiven plats
	 * @return värdet i elementet
	 **/
	public E remove(int index) {
		if( index < 0 || index >= size ) {
			throw new IndexOutOfBoundsException();
		}
		E element = elements[ index ];
		elements[ index ] = null;
		size--;
		for( int i = index; i < size; i++ ) {
			elements[ i ] = elements[ i + 1 ];
		}
		elements[ size ] = null;
		return element;
	}

	/**
	 * Tar bort det första elementet i arraylistan
	 * genom att anropa metoden remove() med ett index
	 * som anger första platsen och returnerar värdet.
	 * @return värdet i elementet
	 **/
	public E removeFirst() {
		return remove( 0 );
	}

	/**
	 * Tar bort det sista elementet i arraylistan genom
	 * att anropa metoden remove() med ett index
	 * som anger sista platsen och returnerar värdet.
	 * @return värdet i elementet
	 **/
	public E removeLast() {
		return remove( size - 1 );
	}

	/**
	 * Tar bort alla elementet i arraylistan genom att anropa metoden
	 * remove() i en for-loop  med ett index som börjar från
	 * sista elementet och går framåt till alla platser i listan.
	 **/
	public void clear() {
		for(int i = size - 1; i >= 0; i-- ) {
			remove( i );
		}
		size = 0;
	}

	/**
	 * Får ett index som parameter för att kunna hämta ett element
	 * i arraylistan. Index kollas innan värdet returneras.
	 * @param index angiven plats
	 * @return värdet i elementet
	 **/
	public E get(int index) {
		if( index < 0 || index > size ) {
			throw new IndexOutOfBoundsException();
		}
		return elements[ index ];
	}

	/**
	 * Får ett index och ett element som parameter för att
	 * kunna skriva in det nya elementet på rätt plats i
	 * arraylistan. Metoden kollar index och sen sparas det
	 * gamla värdet tillfälligt i en ny variabel som sen
	 * returneras efter att det nya värdet har sparats.
	 * @param index angiven plats
	 * @param element valt element
	 * @return värdet i elementet
	 **/
	public E set(int index, E element) {
		if( index < 0 || index > size ) {
			throw new IndexOutOfBoundsException();
		}
		E temp = elements[ index ];
		elements[ index ] = element;
		return temp;
	}

	/**
	 * Får ett element som parameter och skickar det vidare
	 * till indexOf() metoden med ett index som börjar från
	 * första elementet. Sen returneras positionen för elementet.
	 * @param element ett sökt element
	 * @return en position eller -1 ifall positionen inte finns
	 **/
	public int indexOf(E element) {
		return indexOf( 0, element );
	}

	/**
	 * Får ett index och ett element som parameter för att kunna
	 * hitta det sökta elementets position. Metoden kollar index
	 * och sen jämförs alla värden med equals. Sist returneras
	 * positionen för elementet eller -1 ifall värdet inte finns.
	 * @param startIndex angiven plats
	 * @param element ett sökt element
	 * @return en position eller -1 ifall positionen inte finns
	 **/
	public int indexOf(int startIndex, E element) {
		if( startIndex < 0 || startIndex > size ) 
			throw new IndexOutOfBoundsException();

		for( int i = startIndex; i < size; i++ ) {
			if( elements[ i ].equals( element ) ) {
				return i;
			}
		}
		return -1;
	}


	/**
	 * Returnerar storleken på arraylistan
	 * @return storleken på arraylistan
	 **/
	public int size() {
		return size;
	}


	/**
	 * Skriver ut alla värden på ett specifikt sätt.
	 * @return en sträng
	 **/
	public String toString() {
		StringBuilder res = new StringBuilder("[ ");
		for(int i=0; i<size; i++) {
			res.append(elements[i]);
			if(i<size-1)
				res.append("; ");
		}
		res.append(" ]");
		return res.toString();
	}


	/**
	 * Returnerar en iterator som kan loopa genom arraylistan.
	 * @return ett nytt Iter objekt
	 **/
	public Iterator<E> iterator() {
		return new Iter();
	}

	/**
	 * En privat klass med en iterator
	 * som kan loopa genom arraylistan.
	 **/
	private class Iter implements Iterator<E> {
		private int index=0;

		/**
		 * Returnerar svaret på om det finns fler element.
		 * @return sant eller falskt
		 **/
		public boolean hasNext() {
			return index<size;
		}

		/**
		 * Kollar index mot storleken på arraylistan och
		 * returnerar elementets värde samt ökar index med 1.
		 * @return värdet i elementet
		 **/
		public E next() {
			if(index==size)
				throw new NoSuchElementException();
			return elements[index++];
		}

		/**
		 * Stöds inte utan tas bara med som tvång och kastas.
		 **/
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
