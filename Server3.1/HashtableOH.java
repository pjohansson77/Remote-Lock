package lock;
import java.util.Iterator;

/******************************************
 * En klass som innehåller en Hashtable med 
 * öppen hashing metod. Innehåller metoder
 * för att skapa hashindex och lagra ett 
 * värde med en nyckel som är hashindex.
 * @author Peter Johansson den 2/3 2014
 ******************************************/
public class HashtableOH<K,V> implements Map<K,V> { 
    private LinkedList<Entry<K,V>>[] table;
    private int size;
    
    /**
	 * Konstruktorn skapar och initierar en
	 * länkad array lista som ska bli en Hashtable.
	 * @param size kapaciteten i hashtabellen
	 **/
    public HashtableOH( int size ) {
        table = (LinkedList<Entry<K,V>>[])new LinkedList[ size ];
        for( int i = 0; i < size; i++ ) {
            table[ i ] = new LinkedList<Entry<K,V>>();
        }
    }
    
    private int hashIndex( K key ) {
        int hashCode = key.hashCode();
        hashCode = hashCode % table.length;
        return ( hashCode < 0 ) ? -hashCode : hashCode;
    }
    
    /**
	 * Metoden placerar en nyckel och ett värde i en 
	 * hashtable samt ökar antalet element med ett. 
	 * @param key en nyckel
	 * @param value ett värde
	 * @return värdet för en nyckel
	 **/
    public V put( K key, V value ) {
    	V res = null;
        int hashIndex = hashIndex( key );
        Entry<K,V> entry = new Entry<K,V>( key, value );
        int index = table[ hashIndex ].indexOf( entry );
        if( index == -1 ) {
            table[ hashIndex ].addFirst( entry );
            size++;
        }
        else {
        	res = table[ hashIndex ].set( index, entry ).value;
        }
        return res;
    }
    
    /**
	 * Metoden skriver ut alla nycklar och värden i hashtabellen. 
	 **/
    public void list() {
        Entry<K,V> entry;
        for(int i=0; i<table.length; i++) {
            System.out.print( i + ":");
            for( int j = 0; j < table[ i ].size(); j++ ) {
                entry = table[ i ].get( j );
                System.out.print(" <" + entry.key +"," + entry.value + ">" );
            }
            System.out.println();
        }
    }

    /**
	 * Metoden returnerar värdet för en specifik nyckel.
	 * @param key en nyckel
	 * @return värdet för en nyckel 
	 **/
	public V get(K key) {
		int hashIndex = hashIndex( key );
		Entry<K,V> entry = new Entry<K,V>( key, null );
		int index = table[ hashIndex ].indexOf( entry );
		if( index >= 0 ) {
			return table[ hashIndex ].get( index ).value;
		}
		return null;
	}

	/**
	 * Metoden tar bort värdet till en nyckel ur hashtabellen
	 * och returnerar det samt minskar antalet element med ett.
	 * @param key en nyckel
	 * @return värdet för en nyckel
	 **/
	public V remove(K key) {
		int hashIndex = hashIndex( key );
		Entry<K,V> entry = new Entry<K,V>( key, null );
		int index = table[ hashIndex ].indexOf( entry );
		if( index >= 0 ) {
			V value = table[ hashIndex ].get( index ).value;
			table[ hashIndex ].remove( index );
			size--;
			return value;
		}
		return null;
	}

	/**
	 * Metoden returnerar antalet element i hashtabellen.
	 * @return antal element i hashtabellen
	 **/
	public int size() {
		return size;
	}

	/**
	 * Metoden kollar om hashtabellen är tom.
	 * @return sant eller falskt
	 **/
	public boolean isEmpty() {
		return ( size == 0 );
	}

	/**
	 * Metoden kollar om en viss nyckel finns i hashtabellen.
	 * @param key en nyckel
	 * @return sant eller falskt
	 **/
	public boolean containsKey(K key) {
		return get( key ) != null;
	}

	/**
	 * Metoden rensar hashtabellen på alla nycklar och värden.
	 **/
	public void clear() {
		for( int i = 0; i < table.length; i++ ) {
			table[ i ].clear();
		}
		size = 0;
	}

	/**
	 * Metoden returnerar alla nycklar i hashtabellen.
	 * @return alla nycklar i trädet
	 **/
	public Iterator<K> keys() {
		LinkedList<K> keys = new LinkedList<K>();
		for( int i = 0; i < table.length; i++ ) {
			for( int index = 0; index < table[ i ].size(); index++ ) {
				keys.add( table[ i ].get( index ).key );
			}
		}
		return keys.iterator();
	}

	/**
	 * Metoden returnerar alla värden i hashtabellen.
	 * @return alla värden i trädet
	 **/
	public Iterator<V> values() {
		LinkedList<V> values = new LinkedList<V>();
		for( int i = 0; i < table.length; i++ ) {
			for( int index = 0; index < table[ i ].size(); index++ ) {
				values.add( table[ i ].get( index ).value );
			}
		}
		return values.iterator();
	}
}