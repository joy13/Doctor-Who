package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PlayUtilities {
    
    public static <K,V extends Comparable<? super V>> 
    Map<K, V> entriesSortedByValues(Map<K,V> map) {

List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());

Collections.sort(sortedEntries, 
    new Comparator<Entry<K,V>>() {
        @Override
        public int compare(Entry<K,V> e1, Entry<K,V> e2) {
            return e2.getValue().compareTo(e1.getValue());
        }
    }
);

Map<K, V> result = new LinkedHashMap<K, V>();
for (Map.Entry<K, V> entry : sortedEntries)
{
    result.put( entry.getKey(), entry.getValue() );
}
return result;
}

}
