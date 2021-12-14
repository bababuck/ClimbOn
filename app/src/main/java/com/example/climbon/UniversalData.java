package com.example.climbon;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class UniversalData {
    /* Stores data to be stored across activities.

    Namely, we want to store search results for after viewing route.
    Can also store current route (while searching for other things)
    */
        Map<String, WeakReference<Object>> data = new HashMap<String, WeakReference<Object>>();
        void save(String id, Object object) {
            data.put(id, new WeakReference<Object>(object));
        }

        Object retrieve(String id) {
            WeakReference<Object> objectWeakReference = data.get(id);
            return objectWeakReference.get();
        }
}
