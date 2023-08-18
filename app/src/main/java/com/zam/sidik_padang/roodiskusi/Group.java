package com.zam.sidik_padang.roodiskusi;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Group implements Serializable, Comparable<Group> {


    public static final String GROUPS = Common.FireStore.GROUPS;
    public static final String LAST_MESSAGE = "lastMessage";

    public String name, id, owner;
    public Map<String, Object> users = new HashMap<>();
    public Map<String, Object> lastMessage = new HashMap<>();

    public boolean selected = false;


    @Override
    public int compareTo(@NonNull Group group) {
        Object o1 = lastMessage.get("timestamp");
        Object o2 = group.lastMessage.get("timestamp");
        long l1 = o1 == null ? 0 : (long) o1;
        long l2 = o2 == null ? 0 : (long) o2;
        if (l1 > l2) return -1;
        if (l2 > l1) return 1;
        return 0;
    }
}
