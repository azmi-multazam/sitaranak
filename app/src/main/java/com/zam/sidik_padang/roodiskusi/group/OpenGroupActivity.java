package com.zam.sidik_padang.roodiskusi.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
/*
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.roodiskusi.Common;
import com.zam.sidik_padang.roodiskusi.Group;
import com.zam.sidik_padang.roodiskusi.fcm.Fcm;
import com.zam.sidik_padang.roodiskusi.group.info.GroupInfoActivity;
import com.zam.sidik_padang.util.User;


public class OpenGroupActivity extends BaseLogedinActivity implements View.OnClickListener {

    public static String CURRENT_GROUP_ID = "";
    private View progressBar, textNoData;
    private Group group;
    private List<Message> messageList;
    private MessageListAdapter messageListAdapter;
    //private ListenerRegistration listenerRegistration;
    private TextView textInput;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (group != null) outState.putSerializable(Common.EXTRA_GROUP, group);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle s) {
        super.onRestoreInstanceState(s);
        if (s != null && s.containsKey(Common.EXTRA_GROUP))
            group = (Group) s.getSerializable(Common.EXTRA_GROUP);
    }

    @Override
    protected void onCreate(@Nullable Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (s != null && s.containsKey(Common.EXTRA_GROUP))
            group = (Group) s.getSerializable(Common.EXTRA_GROUP);
        else {
            Intent it = getIntent();
            if (it.hasExtra(Common.EXTRA_GROUP))
                group = (Group) it.getSerializableExtra(Common.EXTRA_GROUP);
        }
        if (group == null) {
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            return;
        }
        CURRENT_GROUP_ID = group.id;

        RecyclerView rv = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rv.setLayoutManager(layoutManager);
        messageList = new ArrayList<>();
        //messageListAdapter = new MessageListAdapter(messageList, firestore.collection(Group.GROUPS).document(group.id));
        rv.setAdapter(messageListAdapter);
        progressBar = findViewById(R.id.progressBar);
        textNoData = findViewById(R.id.textViewNoData);
        textInput = findViewById(R.id.editText);
        findViewById(R.id.button).setOnClickListener(this);

        String title = group.name;
        if (title.isEmpty()) title = "Obrolan";
        getSupportActionBar().setTitle(title);
        String subtitle = "Anda";
        for (String key : group.users.keySet()) {
            if (key.equals(user.userid)) continue;
            Object o = group.users.get(key);
            if (o instanceof Map) {
                subtitle += ", " + ((Map) o).get("name");
            }
        }
        toolbar.setSubtitle(subtitle);

        //firestore.collection(Group.GROUPS).document(group.id).update(User.USERS + "." + user.userid + ".unread", 0);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGroupInfo();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_obrolan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info_group) {
            openGroupInfo();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openGroupInfo() {
        Intent it = new Intent(OpenGroupActivity.this, GroupInfoActivity.class);
        it.putExtra(Common.EXTRA_GROUP, group);
        startActivity(it);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button) {
            //kirimPesan();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //loadMessage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //if (listenerRegistration != null) listenerRegistration.remove();
    }

    @Override
    protected void onDestroy() {
        CURRENT_GROUP_ID = "";
        super.onDestroy();
    }

    /*
    private void loadMessage() {
        progressBar.setVisibility(View.VISIBLE);
        listenerRegistration = firestore.collection(Group.GROUPS).document(group.id).collection(Message.MESSAGES)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot querySnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (e != null) {
                            Toast.makeText(OpenGroupActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        }
                        messageList.clear();
                        int i = 1;
                        if (querySnapshot != null) {
                            for (DocumentSnapshot ds : querySnapshot.getDocuments()) {
                                messageList.add(ds.toObject(Message.class));
                                i++;
                                if (i >= 200) break;
                            }
                        }
                        Collections.sort(messageList);
                        messageListAdapter.notifyDataSetChanged();
                        textNoData.setVisibility(messageList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                        if (layoutManager.canScrollVertically())
                            layoutManager.scrollToPosition(messageList.size() - 1);
                        firestore.collection(Group.GROUPS).document(group.id).update(User.USERS + "." + user.userid + ".unread", 0);
                    }
                });
    }


    private void kirimPesan() {
        String text = textInput.getText().toString().trim();
        if (text.isEmpty()) return;
        final Message message = new Message();
        message.timeStamp = System.currentTimeMillis();
        message.message = text;
        message.owner.put("userid", user.userid);
        message.owner.put("nama", user.nama);
        message.owner.put("foto", user.foto);
        final DocumentReference ref = firestore.collection(Group.GROUPS).document(group.id).collection(Message.MESSAGES).document();
        message.id = ref.getId();
        WriteBatch writeBatch = firestore.batch();
        writeBatch.set(ref, message);
        Map<String, Object> map = new HashMap<>();
        //last message info
        Map<String, Object> lastMessageMap = new HashMap<>();
        lastMessageMap.put("userid", user.userid);
        lastMessageMap.put("message", text);
        lastMessageMap.put("nama", user.nama);
        lastMessageMap.put("timestamp", System.currentTimeMillis());
        map.put(Group.LAST_MESSAGE, lastMessageMap);


        writeBatch.update(firestore.collection(Group.GROUPS).document(group.id), map);
        writeBatch.commit()
                .addOnCompleteListener(OpenGroupActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        message.status = task.isSuccessful() ? Message.Status.SENT : Message.Status.FAILED;
                        for (int i = messageList.size() - 1; i >= 0; i--) {
                            if (messageList.get(i).id.equals(ref.getId())) {
                                messageList.get(i).status = message.status;
                                messageListAdapter.notifyDataSetChanged();
                                ref.update("status", message.status);
                                break;
                            }
                        }

                        if (task.isSuccessful()) incrementUnreadCount();
                    }
                });
        textInput.setText("");
        Fcm.getInstance(this).send(group, message);
    }

    private void incrementUnreadCount() {
        final DocumentReference ref = firestore.collection(Group.GROUPS).document(group.id);
        firestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) {
                try {
                    DocumentSnapshot ds = transaction.get(ref);
                    Group g = ds.toObject(Group.class);
                    for (String s : g.users.keySet()) {
                        if (s.equals(user.userid)) continue;
                        Map<String, Object> userMap = (Map<String, Object>) group.users.get(s);
                        long total = userMap.get("unread") == null ? 0 : (long) (userMap.get("unread"));
                        total++;
                        transaction.update(ref, User.USERS + "." + s + ".unread", total);
                    }
                } catch (FirebaseFirestoreException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
     */
}
