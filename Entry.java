package lock;

/******************************************
 * En klass som innehåller en metod för
 * att lagra en nyckel och ett värde.
 * Kan även jämföra sin nyckel med en annan.
 * @author Peter Johansson den 2/3 2014
 ******************************************/
class Entry<K,V> {
    K key;
    V value;
    
    /**
	 * Konstruktorns parametrar tar emot
	 * en nyckel och ett värde.
	 * @param key nyckeln
	 * @param value värdet
	 **/
    public Entry( K key, V value ) {
        this.key = key;
        this.value = value;
    }
    
    /**
	 * Metoden jämför två nycklar
	 * och returnerar sant om lika.
	 **/
    public boolean equals( Object obj ) {
        if( !(obj instanceof Entry) )
            return false;
        Entry<K,V> entry = ( Entry<K,V> )obj;
        return key.equals( entry.key );
    }
}
