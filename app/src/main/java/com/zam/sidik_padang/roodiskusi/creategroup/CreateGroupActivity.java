package com.zam.sidik_padang.roodiskusi.creategroup;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.firestore.Transaction;
//import com.google.firebase.firestore.WriteBatch;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.roodiskusi.Common;
import com.zam.sidik_padang.roodiskusi.Group;
import com.zam.sidik_padang.roodiskusi.OnItemClickListener;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;


public class CreateGroupActivity extends BaseLogedinActivity {

    private List<User> selectedUserList, allUserList, filteredAllUserList;

    private RecyclerView.Adapter selectedListAdapter, allUserListAdapter;
    private View textNoData, textPilihUser, progressBar;
    private String filter = "";

    private Group group;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        textNoData = findViewById(R.id.textViewNoData);
        textPilihUser = findViewById(R.id.textViewPilihUser);
        progressBar = findViewById(R.id.progressBar);
        setResult(RESULT_CANCELED);
        RecyclerView rvSelectedUser = findViewById(R.id.recyclerViewSelectedUser);
        rvSelectedUser.setLayoutManager(new GridLayoutManager(this, 2));

        RecyclerView rvAllUser = findViewById(R.id.recyclerViewAllUser);
        rvAllUser.setLayoutManager(new LinearLayoutManager(this));
        rvAllUser.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        selectedUserList = new ArrayList<>();
        allUserList = new ArrayList<>();
        filteredAllUserList = new ArrayList<>();

        allUserListAdapter = new AllUserListAdapter(filteredAllUserList, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {

                String added = "";
                for (User u : selectedUserList) added += u.userid;
                if (!added.contains(((User) o).userid)) selectedUserList.add((User) o);
                selectedListAdapter.notifyDataSetChanged();
                textPilihUser.setVisibility(selectedUserList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                toolbar.getMenu().findItem(R.id.action_done).setVisible(!selectedUserList.isEmpty());
            }
        });

        rvAllUser.setAdapter(allUserListAdapter);

        selectedListAdapter = new SelectedUserListAdapter(selectedUserList, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                selectedUserList.remove(position);
                selectedListAdapter.notifyDataSetChanged();
                textPilihUser.setVisibility(selectedUserList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                toolbar.getMenu().findItem(R.id.action_done).setVisible(!selectedUserList.isEmpty());
            }
        });

        rvSelectedUser.setAdapter(selectedListAdapter);

        if (getIntent().hasExtra(Common.EXTRA_GROUP))
            group = (Group) getIntent().getSerializableExtra(Common.EXTRA_GROUP);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_create_group, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        ((SearchView) item.getActionView()).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter = newText;
                filterUsers();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            if (group == null)
                showDialogCreateGroup();
            else tambahkan();
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    private void tambahkan() {
        final DocumentReference groupRef = firestore.collection(Group.GROUPS).document(group.id);
        final Dialog d = Util.showProgressDialog(this, "Menambahkan anggota...", false);
        firestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                Group g = transaction.get(groupRef).toObject(Group.class);
                if (g == null) {
                    debug(getClass(), "Run transaction. Retrived group is null.Exiting...");
                    return null;
                }
                for (User u : selectedUserList) {
                    Map<String, Object> additionMap = new HashMap<>();
                    additionMap.put(u.userid, true);
                    additionMap.put("name", u.nama);
                    additionMap.put("foto", u.foto);
                    g.users.put(u.userid, additionMap);
                }
                transaction.update(groupRef, User.USERS, g.users);
                return null;
            }
        }).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                d.dismiss();
                debug(getClass(), "Delete transaction completed. Success=" + task.isSuccessful());
                Toast.makeText(CreateGroupActivity.this, task.isSuccessful() ? "Berhasil" : "Gagal", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
     */

    @Override
    protected void onStart() {
        super.onStart();
        //loadAllUser();
        //progressBar.setVisibility(View.VISIBLE);
    }

    /*
    private void loadAllUser() {
        firestore.collection(User.USERS).get().addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    new BindAllUser(CreateGroupActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, task.getResult());
                }
            }
        });
    }
     */

    private void filterUsers() {

        filteredAllUserList.clear();
        for (User u : allUserList) {
            if (u.nama.toLowerCase().contains(filter.trim().toLowerCase()))
                filteredAllUserList.add(u);
        }

        allUserListAdapter.notifyDataSetChanged();
        textNoData.setVisibility(filteredAllUserList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        debug(getClass(), "Filtering alluser size: " + allUserList.size() + " filtered size: " + filteredAllUserList.size());
    }

    /*
    private void showDialogCreateGroup() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_create_group, null, false);
        final EditText editText = view.findViewById(R.id.editText);
        new AlertDialog.Builder(this).setTitle("Obrolan Baru").setView(view)
                .setPositiveButton("Buat Obrolan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String s = editText.getText().toString().trim();
                        buatGroup(s);
                    }
                }).show();

    }

    private void buatGroup(String s) {
        Group group = new Group();
        group.name = s;
        for (User u : selectedUserList) {
            Map<String, Object> othersMap = new HashMap<>();
            othersMap.put(u.userid, true);
            othersMap.put("name", u.nama);
            othersMap.put("foto", u.foto);
            group.users.put(u.userid, othersMap);
        }
        Map<String, Object> sayaMap = new HashMap<>();
        sayaMap.put(user.userid, true);
        sayaMap.put("name", user.nama);
        sayaMap.put("foto", user.foto);
        group.users.put(user.userid, sayaMap);

        group.owner = user.userid;
        DocumentReference groupRef = firestore.collection(Group.GROUPS).document();
        group.id = groupRef.getId();
        WriteBatch batch = firestore.batch();
        batch.set(groupRef, group);
        batch.update(firestore.collection(User.USERS).document(user.userid), Group.GROUPS + "." + groupRef.getId(), true);
        for (User u : selectedUserList)
            batch.update(firestore.collection(User.USERS).document(u.userid), Group.GROUPS + "." + groupRef.getId(), true);

        final Dialog d = Util.showProgressDialog(this, "Membuat obrolan baru", false);
        batch.commit().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                d.dismiss();
                if (!task.isSuccessful()) {
                    Toast.makeText(CreateGroupActivity.this, "Terjadi kesalahan " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });

    }

    private static class BindAllUser extends AsyncTask<QuerySnapshot, Void, Void> {

        private WeakReference<CreateGroupActivity> weakReference;

        BindAllUser(CreateGroupActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(QuerySnapshot... querySnapshots) {
            myDoinBackground(querySnapshots[0]);
            return null;
        }

        private synchronized void myDoinBackground(QuerySnapshot querySnapshot) {
            CreateGroupActivity activity = weakReference.get();
            if (activity == null) return;
            activity.allUserList.clear();
            User u;
            StringBuilder already = new StringBuilder();
            if (activity.group != null) {
                for (String s : activity.group.users.keySet()) {
                    already.append(s);
                }
            }
            String alreadyString = already.toString();

            for (DocumentSnapshot ds : querySnapshot.getDocuments()) {
                u = ds.toObject(User.class);
                if (!u.userid.equals(activity.user.userid)) {
                    if (alreadyString.contains(u.userid)) continue;
                    activity.allUserList.add(u);
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            CreateGroupActivity activity = weakReference.get();
            if (activity == null) return;
            activity.filterUsers();
        }
    }
     */
}
