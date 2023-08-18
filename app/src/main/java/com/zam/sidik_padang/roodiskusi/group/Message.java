package com.zam.sidik_padang.roodiskusi.group;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.zam.sidik_padang.util.User;

public class Message implements Serializable, Comparable<Message> {

    public static final String MESSAGES = "messages";
    public long timeStamp = 0;
    public String id, message;
    public int status = 1;

    public Map<String, String> owner = new HashMap<>();

    public boolean isMine(User im) {
        return owner.get("userid").equals(im.userid);
    }

    @Override
    public int compareTo(@NonNull Message message) {

        if (message.timeStamp < timeStamp) return 1;
        if (message.timeStamp > timeStamp) return -1;
        return 0;

    }

    public static final class Status {
        public static final int SENDINGG = 1, SENT = 2, DELIVERY = 3, READ = 4, FAILED = 5;
    }
}
