package com.zam.sidik_padang.roodiskusi.group.info;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
/*
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Transaction;
*/
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.roodiskusi.Common;
import com.zam.sidik_padang.roodiskusi.Group;
import com.zam.sidik_padang.roodiskusi.creategroup.CreateGroupActivity;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;

public class GroupInfoActivity extends BaseLogedinActivity implements View.OnClickListener, AnggotaListAdapter.OnDeleteButtonClickListener {

    private List<User> list;
    private AnggotaListAdapter adapter;
    private View progressBar;
    private TextView textJudulgroup, textOwnerName;
    private ImageView imageOwner;

    //private ListenerRegistration listenerRegistration;

    private Group group;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (group != null) outState.putSerializable(Common.EXTRA_GROUP, group);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(Common.EXTRA_GROUP)) {
            group = (Group) savedInstanceState.getSerializable(Common.EXTRA_GROUP);
        } else {
            Intent it = getIntent();
            if (it.hasExtra(Common.EXTRA_GROUP))
                group = (Group) it.getSerializableExtra(Common.EXTRA_GROUP);
        }

        if (group == null) Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();

        list = new ArrayList<>();
        adapter = new AnggotaListAdapter(list, group.owner, this);

        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setAdapter(adapter);
        progressBar = findViewById(R.id.progressBar);
        textJudulgroup = findViewById(R.id.textViewTitle);
        textJudulgroup.setText(group.name);
        textOwnerName = findViewById(R.id.textViewNama);
        imageOwner = findViewById(R.id.imageView);
        findViewById(R.id.imageViewEdit).setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imageViewEdit) {
            if (!user.userid.equals(group.owner)) {
                new AlertDialog.Builder(this).setMessage("Hanya pemilik grup yang berhak mengubah judul")
                        .setPositiveButton(android.R.string.ok, null).show();
                return;
            }
            showDialogEditJudul();
        } else if (id == R.id.button) {
            if (!user.userid.equals(group.owner)) {
                new AlertDialog.Builder(this).setMessage("Hanya pemilik grup yang berhak menambahkan anggota")
                        .setPositiveButton(android.R.string.ok, null).show();
                return;
            }
            Intent intent = new Intent(this, CreateGroupActivity.class);
            intent.putExtra(Common.EXTRA_GROUP, group);
            startActivity(intent);
        }
    }

    private void showDialogEditJudul() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_judul_group, null, false);
        final TextView editText = view.findViewById(R.id.editText);
        new AlertDialog.Builder(this).setTitle("Edit Judul Group")
                .setView(view)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //saveGroupTitle(editText.getText().toString().trim());
                    }
                })
                .show();
    }

    @Override
    public void onDeleteButtonClick(User user) {

    }

    /*
    private void saveGroupTitle(final String title) {
        final Dialog d = Util.showProgressDialog(this, "Harap tunggu", false);
        firestore.collection(Group.GROUPS).document(group.id)
                .update("name", title)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        d.dismiss();
                        if (task.isSuccessful()) {
                            textJudulgroup.setText(title);
                            Toast.makeText(GroupInfoActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GroupInfoActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            if (task.getException() != null) task.getException().printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDeleteButtonClick(final User u) {
        final DocumentReference groupRef = firestore.collection(Group.GROUPS).document(group.id);
        final Dialog d = Util.showProgressDialog(this, "Menghapus anggota...", false);
        firestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                Group g = transaction.get(groupRef).toObject(Group.class);
                if (g == null) {
                    debug(getClass(), "Run transaction. Retrived group is null.Exiting...");
                    return null;
                }
                g.users.remove(u.userid);
                transaction.update(groupRef, User.USERS, g.users);
                return null;
            }
        }).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                d.dismiss();
                debug(getClass(), "Delete transaction completed. Success=" + task.isSuccessful());
                Toast.makeText(GroupInfoActivity.this, task.isSuccessful() ? "Berhasil" : "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadAnggota();
    }

    @Override
    protected void onStop() {
        if (listenerRegistration != null) listenerRegistration.remove();
        super.onStop();
    }

    private void loadAnggota() {
        debug(getClass(), "Load anggota");
        if (group == null) return;
        progressBar.setVisibility(View.VISIBLE);
        listenerRegistration = firestore.collection(Group.GROUPS).document(group.id).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                debug(getClass(), "Load anggota complite");
                progressBar.setVisibility(View.INVISIBLE);
                if (e != null) {
                    listenerRegistration = null;
                    debug(getClass(), e.getMessage());
                    return;
                }
                if (documentSnapshot == null || !documentSnapshot.exists()) {
                    debug(getClass(), "Document null || not exist");
                    return;
                }
                group = documentSnapshot.toObject(Group.class);
                if (group == null) {
                    debug(getClass(), "Document to group object == Null");
                    return;
                }
                list.clear();
                Set<String> keySet = group.users.keySet();
                User u;
                for (String s : keySet) {
                    Map<String, Object> userMap = (Map<String, Object>) group.users.get(s);
                    if (userMap != null) {
                        u = new User();
                        u.userid = s;
                        u.nama = (String) userMap.get("name");
                        u.foto = (String) userMap.get("foto");
                        if (s.equals(GroupInfoActivity.this.group.owner)) {
                            textOwnerName.setText(u.nama);
                            if (u.foto != null && !u.foto.isEmpty())
                                Glide.with(GroupInfoActivity.this)
                                        .load(u.foto).into(imageOwner);
                            continue;
                        }
                        list.add(u);
                    }
                }
                Collections.sort(list);
                adapter.notifyDataSetChanged();
                debug(getClass(), "Load anggota finish.list size = " + list.size());
            }
        });
    }


     */

}
