package com.zam.sidik_padang.home.sklb.setting.kuantitatif;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.zam.sidik_padang.cutout.CutOut;
import com.zam.sidik_padang.cutout.CutOutActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
//import com.zam.sidik_padang.BuildConfig;
import com.zam.sidik_padang.databinding.ActivityTtdBinding;
import com.zam.sidik_padang.util.Util;
import io.paperdb.Paper;

import static com.zam.sidik_padang.cutout.CutOut.CUTOUT_ACTIVITY_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.zam.sidik_padang.home.sklb.setting.kuantitatif.KuantitatifListFragment.CUTOUT_EXTRA_SOURCE;
import static com.zam.sidik_padang.util.Config.CATATAN_SERTIFIKAT;
import static com.zam.sidik_padang.util.Config.JABATAN_DINAS;

public class TtdActivity extends BaseLogedinActivity {

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    private static final int IMAGE_CHOOSER_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 3;
    private static final int PICK_PICTURE_FROM_GALLERY = 4;
    private static final int TAKE_PICTURE = 5;
    private static final int SOURCE_CHOOSER = 6;
    private String KEY_LAST_CAMERA_PHOTO;

    private static final String setting_dinas = "setting_dinas";
    private ActivityTtdBinding binding;
    private SettingDinas settingDinas;
    private String oldTtd, newTtd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTtdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Tanda tangan");
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        settingDinas = Paper.book().read(setting_dinas, new SettingDinas("", "", JABATAN_DINAS, "", "", "", CATATAN_SERTIFIKAT));
        oldTtd = newTtd = settingDinas.ttd;
        Bitmap bmp = Util.base64ToBitmap(oldTtd);
        binding.ivTtd.setImageBitmap(bmp);
        saveImage(bmp);
        logging();

        binding.add.setOnClickListener(v -> {
            pilihFoto();
        });

        binding.crop.setOnClickListener(v -> {
            Uri uri = getUri();
            if (uri != null) {
                CropImage.ActivityBuilder cropImageBuilder = CropImage.activity(uri);
                cropImageBuilder.setGuidelines(CropImageView.Guidelines.ON)
                        .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                        .setAspectRatio(4, 3)
                        .setInitialCropWindowPaddingRatio(0)
                        .start(this);
            } else {
                Toast.makeText(this, "Tidak ada foto tanda tangan", Toast.LENGTH_SHORT).show();
            }
        });

        binding.edit.setOnClickListener(v -> {
            Uri uri = getUri();
            if (uri != null) {
                Intent intent = new Intent();
                intent.setClass(TtdActivity.this, CutOutActivity.class);
                intent.putExtra(CUTOUT_EXTRA_SOURCE, getUri());
                //intent.putExtra(CUTOUT_EXTRA_BORDER_COLOR, borderColor);
                //intent.putExtra(CUTOUT_EXTRA_CROP, true);
                startActivityForResult(intent, CUTOUT_ACTIVITY_REQUEST_CODE);
            } else {
                Toast.makeText(this, "Tidak ada foto tanda tangan", Toast.LENGTH_SHORT).show();
            }
        });

        binding.simpan.setOnClickListener(v -> {
            if (!newTtd.equals(oldTtd)) {
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setMessage("Tanda tangan sebelumnya akan diganti")
                        .setPositiveButton("SIMPAN", (dialog, which) -> {
                            saveResult();
                        })
                        .setNegativeButton("BATAL", null)
                        .show();
            }
        });
    }

    private void pilihFoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openChooserWithGallery(this, "Pilih Foto", IMAGE_CHOOSER_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pilihFoto();
        } else {
            //not granted
        }
    }

    private void saveResult() {
        settingDinas.ttd = newTtd;
        Paper.book().write(setting_dinas, settingDinas);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!oldTtd.equals(newTtd)) {
            warning();
        } else {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            super.onBackPressed();
        }
    }

    private void warning() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("Perubahan belum disimpan")
                .setPositiveButton("BUANG", (dialog, which) -> {
                    Intent intent = new Intent();
                    setResult(RESULT_CANCELED, intent);
                    finish();
                })
                .setNegativeButton("KEMBALI", null)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CUTOUT_ACTIVITY_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Uri imageUri = CutOut.getUri(data);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        binding.ivTtd.setImageBitmap(bitmap);
                        newTtd = Util.bitmapToBase64(bitmap);
                        settingDinas.ttd = newTtd;
                        saveImage(bitmap);
                        logging();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CutOut.CUTOUT_ACTIVITY_RESULT_ERROR_CODE:
                    Exception ex = CutOut.getError(data);
                    break;
                default:
                    System.out.print("User cancelled the CutOut screen");
            }
        } else if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                    binding.ivTtd.setImageBitmap(bitmap);
                    newTtd = Util.bitmapToBase64(bitmap);
                    settingDinas.ttd = newTtd;
                    saveImage(bitmap);
                    logging();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == PICK_PICTURE_FROM_GALLERY ||
                requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                if (requestCode == PICK_PICTURE_FROM_GALLERY && !isPhoto(data)) {
                    onPictureReturnedFromGallery(data);
                } else if (requestCode == TAKE_PICTURE) {
                    onPictureReturnedFromCamera();
                } else if (isPhoto(data)) {
                    onPictureReturnedFromCamera();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isPhoto(Intent data) {
        return data == null || (data.getData() == null && data.getClipData() == null);
    }

    private void onPictureReturnedFromGallery(Intent data) {
        try {
            ClipData clipData = data.getClipData();
            File file;
            Uri uri;
            if (clipData == null) {
                uri = data.getData();
            } else {
                uri = clipData.getItemAt(0).getUri();
            }
            file = pickedExistingPicture(uri);
            onImagesPicked(file, ImageSource.GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
            onImagePickerError(e, ImageSource.GALLERY);
        }
    }

    private void onPictureReturnedFromCamera() {
        try {
            String lastImageUri = KEY_LAST_CAMERA_PHOTO;
            File photoFile = null;
            if (!TextUtils.isEmpty(lastImageUri)) {
                revokeWritePermission(Uri.parse(lastImageUri));
                photoFile = new File(KEY_LAST_CAMERA_PHOTO);
            }

            if (photoFile == null) {
                Exception e = new IllegalStateException("Unable to get the picture returned from camera");
                onImagePickerError(e, ImageSource.CAMERA_IMAGE);
            } else {
                File file = pickedExistingPicture(uriFromFile(photoFile));
                onImagesPicked(file, ImageSource.CAMERA_IMAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onImagePickerError(e, ImageSource.CAMERA_IMAGE);
        }
    }

    private File pickedExistingPicture(Uri photoUri) throws IOException {
        InputStream pictureInputStream = getContentResolver().openInputStream(photoUri);
        File photoFile = new File(getFilesDir(), "ttd.png");
        //photoFile.createNewFile();
        try {
            Bitmap bitmap = getResizedBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri), 800);
            //File newFile = new File(file);
            FileOutputStream fileOutputStream = new FileOutputStream(photoFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 95, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            pictureInputStream.close();
            /*
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
             */
        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoFile;
    }

    private Uri getUri() {
        if (settingDinas.ttd.equals("")) {
            return null;
        } else {
            File file = new File(getFilesDir(), "ttd.png");
            return uriFromFile(file);
        }
    }

    private Uri uriFromFile(File f) {
        try {
            f.setReadable(true, false);
            return FileProvider.getUriForFile(this,
                    "com.zam.sidik_padang.provider",//BuildConfig.APPLICATION_ID + ".provider",
                    f);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveImage(Bitmap bitmap) {
        try {
            File newFile = new File(getFilesDir(), "ttd.png");
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);//context.openFileOutput("Your File Name", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 95, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logging() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(settingDinas);
        Log.d("json", json);
    }

    public void openChooserWithGallery(Activity activity, @Nullable String chooserTitle, int type) {
        try {
            Intent intent = createChooserIntent(activity, chooserTitle, true, type);
            activity.startActivityForResult(intent, PICK_PICTURE_FROM_GALLERY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Intent createChooserIntent(@NonNull Context context, @Nullable String chooserTitle, boolean showGallery, int type) throws IOException {
        File file = new File(getFilesDir(), "ttd_camera.png");
        KEY_LAST_CAMERA_PHOTO = file.toString();
        Uri outputFileUri = FileProvider.getUriForFile(context,
                "com.zam.sidik_padang.provider",//BuildConfig.APPLICATION_ID + ".provider",
                file);
        List<Intent> cameraIntents = new ArrayList<>();
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = context.getPackageManager();
        @SuppressLint("QueryPermissionsNeeded")
        List<ResolveInfo> camList = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : camList) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            grantWritePermission(context, intent, outputFileUri);
            cameraIntents.add(intent);
        }
        Intent galleryIntent;

        if (showGallery) {
            galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            galleryIntent = intent;
        }

        Intent chooserIntent = Intent.createChooser(galleryIntent, chooserTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        return chooserIntent;
    }

    private void grantWritePermission(@NonNull Context context, Intent intent, Uri uri) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    public void onImagePickerError(Exception e, ImageSource source) {
        //exitWithError(e);
    }

    public void onImagesPicked(@NonNull File imageFiles, ImageSource source) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imageFiles.toURI().toString()));
            binding.ivTtd.setImageBitmap(bitmap);
            newTtd = Util.bitmapToBase64(bitmap);
            settingDinas.ttd = newTtd;
            //saveImage(bitmap);
            //logging();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void revokeWritePermission(Uri uri) {
        revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public enum ImageSource {
        GALLERY, DOCUMENTS, CAMERA_IMAGE, CAMERA_VIDEO
    }
}