package com.zam.sidik_padang.roodiskusi;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
/*
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

 */

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.roodiskusi.creategroup.CreateGroupActivity;
import com.zam.sidik_padang.roodiskusi.group.Message;
import com.zam.sidik_padang.roodiskusi.group.OpenGroupActivity;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;

/**
 * Created by supriyadi on 5/7/17.
 */

public class RoomDiskusiActivity extends BaseLogedinActivity implements OnItemClickListener, OnItemLongClickListener {
    Toolbar toolbar;
    private List<Group> groupList;
    private GroupListAdapter groupListAdapter;
    private View textNoData, progressBar;
    private DrawerLayout drawerLayout;
    //private ListenerRegistration listenerRegistration;
    private boolean selectionMode = false;
    private NotificationManager notifManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * Firestore setting
         * */
        /*
        if (savedInstanceState == null) try {
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setTimestampsInSnapshotsEnabled(true)
                    .build();
            firestore.setFirestoreSettings(settings);
        } catch (Exception e) {
            e.printStackTrace();
        }
         */


        setContentView(R.layout.activity_room_diskusi);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectionMode) {
                    exitSelectionMode();
                } else finish();
            }
        });
        groupList = new ArrayList<>();
        groupListAdapter = new GroupListAdapter(groupList, this, this);
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setAdapter(groupListAdapter);
        textNoData = findViewById(R.id.textViewNoData);
        progressBar = findViewById(R.id.progressBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        if (user != null) {
            getSupportActionBar().setTitle(user.nama);
        }

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewGroup();
            }
        });
        loadPicttureProfile();
        if (user != null && user.foto != null && user.foto.contains("/"))
            AndroidNetworking.get(user.foto).build().getAsBitmap(new BitmapRequestListener() {
                @Override
                public void onResponse(Bitmap response) {
                    if (response == null) return;
                    new SaveBitmap(RoomDiskusiActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                }

                @Override
                public void onError(ANError anError) {
                    anError.printStackTrace();
                }
            });

        notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void loadPicttureProfile() {
        File file = new File(getFilesDir(), "pp");
        if (!file.exists()) return;
        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (bm == null) return;
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bm);
        toolbar.setLogo(drawable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //if (id == R.id.action_delete) {
            //deleteSelectedGroup();
        //} else
            if (id == R.id.action_logout) {
            Util.logout(this);
        }
        return super.onOptionsItemSelected(item);
    }


    private void createNewGroup() {
        startActivityForResult(new Intent(this, CreateGroupActivity.class), 1);
    }

    /*
    private void loadGroupList() {
        if (Util.isOffline(this)) return;
        debug(getClass(), "Load Groups list");
        progressBar.setVisibility(View.VISIBLE);
        listenerRegistration = firestore.collection(Common.FireStore.GROUPS)
                .whereEqualTo(User.USERS + "." + user.userid + "." + user.userid, true)
                .addSnapshotListener(RoomDiskusiActivity.this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        debug(getClass(), "Load Groups list. Response: " + queryDocumentSnapshots);
                        if (e != null) {
                            listenerRegistration = null;
                            e.printStackTrace();
                            Toast.makeText(RoomDiskusiActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (queryDocumentSnapshots == null) {
                            debug(getClass(), "Query snapshot null");
                        }

                        groupList.clear();
                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            groupList.add(ds.toObject(Group.class));
                            debug(getClass(), "ds: " + ds);
                        }
                        Collections.sort(groupList);
                        textNoData.setVisibility(groupList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                        groupListAdapter.notifyDataSetChanged();
                    }
                });


    }

     */
    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            //loadGroupList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifManager.cancel(Common.GROUP_NOTIF_ID);
    }

    @Override
    protected void onStop() {
        /*
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
         */
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        /*
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
         */
        super.onDestroy();
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (selectionMode) {
            ((Group) o).selected = !((Group) o).selected;
            groupListAdapter.notifyDataSetChanged();
        } else {
            Intent it = new Intent(this, OpenGroupActivity.class);
            it.putExtra(Common.EXTRA_GROUP, (Group) o);
            startActivity(it);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else if (selectionMode) {
            exitSelectionMode();
        } else super.onBackPressed();
         */
    }


    @Override
    public void onItemLongClick(Object o, int position) {
        if (((Group) o).selected) {
            exitSelectionMode();
        } else {
            ((Group) o).selected = true;
            selectionMode = true;
            groupListAdapter.notifyDataSetChanged();
            toolbar.getMenu().findItem(R.id.action_delete).setVisible(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void exitSelectionMode() {
        for (Group group : groupList) {
            group.selected = false;
        }
        groupListAdapter.notifyDataSetChanged();
        selectionMode = false;
        toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    /*
    private void deleteSelectedGroup() {
        WriteBatch batch = firestore.batch();
        boolean tryingDeleteOtherGroup = false;
        boolean nihil = true;
        for (Group group : groupList) {
            if (!group.selected) continue;
            if (!group.owner.equals(user.userid)) {
                tryingDeleteOtherGroup = true;
                continue;
            }

            //delete allMessage
            firestore.collection(Group.GROUPS).document(group.id).collection(Message.MESSAGES).get()
                    .addOnCompleteListener(new MyDeleteAllMessageTask(group));

            //update user
            for (String s : group.users.keySet()) {
                batch.update(firestore.collection(User.USERS).document(s), Group.GROUPS + "." + group.id, FieldValue.delete());
            }

            //hapus group
            batch.delete(firestore.collection(Group.GROUPS).document(group.id));
            nihil = false;
        }
        if (nihil) {
            new AlertDialog.Builder(this).setMessage("Hanya obrolan yang dibuat oleh anda sendiri yang bisa anda hapus").setPositiveButton("Ok", null).show();
            return;
        }
        final boolean deletingOtherGroup = tryingDeleteOtherGroup;
        final Dialog d = Util.showProgressDialog(this, "Harap tunggu...", false);
        batch.commit().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(RoomDiskusiActivity.this, "Gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                d.dismiss();
                if (deletingOtherGroup)
                    Toast.makeText(RoomDiskusiActivity.this, "Hanya bisa menhapus obrolan buatan sendiri", Toast.LENGTH_SHORT).show();
            }
        });
        exitSelectionMode();
    }
     */

    private static class SaveBitmap extends AsyncTask<Bitmap, Void, Void> {

        private WeakReference<RoomDiskusiActivity> weakReference;

        public SaveBitmap(RoomDiskusiActivity a) {
            this.weakReference = new WeakReference<>(a);
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            RoomDiskusiActivity a = weakReference.get();
            if (a == null) return null;
            int w = bitmaps[0].getWidth(), h = bitmaps[0].getHeight();
            float left = w > h ? (w - h) / 2 : 0;
            float top = h > w ? (h - w) / 2 : 0;
            float size = Math.min(w, h);
            float right = left + size;
            float bottom = top + size;
            float density = a.getResources().getDisplayMetrics().density;
            Bitmap bm = Bitmap.createBitmap((int) (60 * density), (int) (60 * density), Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(bm);
            Path path = new Path();
            path.addCircle(bm.getWidth() / 2 - 5 * density, bm.getWidth() / 2, bm.getHeight() / 2 - 5 * density, Path.Direction.CW);
            cv.clipPath(path);
            cv.drawBitmap(bitmaps[0], new Rect((int) left, (int) top, (int) right, (int) bottom), new RectF(0, 5 * density, bm.getWidth() - 10 * density, bm.getHeight() - 5 * density), null);
            try {
                a = weakReference.get();
                if (a == null) return null;
                a.debug(getClass(), "Do inbackground");
                File f = new File(a.getFilesDir(), "pp");
                if (!f.exists()) f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.PNG, 70, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            RoomDiskusiActivity activity = weakReference.get();
            if (activity != null) {
                activity.loadPicttureProfile();
                activity.debug(getClass(), "onPostExecute");
            }
        }
    }

    /*
    private class MyDeleteAllMessageTask implements OnCompleteListener<QuerySnapshot> {

        private WriteBatch writeBatch;
        private Group group;

        MyDeleteAllMessageTask(Group group) {
            this.group = group;
            writeBatch = firestore.batch();
        }

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                for (DocumentSnapshot ds : querySnapshot.getDocuments()) {
                    if (ds.exists()) {
                        DocumentReference ref = firestore.collection(Group.GROUPS).document(group.id).collection(Message.MESSAGES).document(ds.getId());
                        writeBatch.delete(ref);
                        debug(getClass(), "Deleting " + ref.getPath());
                    }
                }

                writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        debug(getClass(), group.name + "'s messages. deleted");
                    }
                });
            }
        }
    }
     */

}
