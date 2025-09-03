package com.wcs.vcc.main.postiamge;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import com.wcs.vcc.utilities.Const;
import com.wcs.vcc.utilities.ResizeImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class GridImage {

    public static final String TAG = GridImage.class.getSimpleName();

    public static ArrayList<File> updateGridImage(Context context, Intent data, ThumbImageAdapter adapter) {
        ArrayList<File> files = new ArrayList<>();
        ArrayList<Thumb> valuesThumb = new ArrayList<>();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = Const.SAMPLE_SIZE;

        Uri uriImage = data.getData();
        ClipData clipData = data.getClipData();

        if (uriImage != null) {
            String path = ImageUtils.getPath(context, uriImage);
            if (path == null || path.length() == 0) {
                Toast.makeText(context, "Không thể tìm đúng đường dẫn của hình ảnh đã chọn", Toast.LENGTH_LONG).show();
            } else {
                try {
                    path = ResizeImage.resizeImageFromFile(path, Const.IMAGE_UPLOAD_WIDTH);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                files.add(new File(path));
                Bitmap thumb = BitmapFactory.decodeFile(path, options);

                valuesThumb.add(new Thumb(thumb, path));
            }
            if (adapter != null) {
                adapter.clear();
                adapter.addAll(valuesThumb);
            }

        } else if (clipData != null) {
            for (int i = 0, n = clipData.getItemCount(); i < n; i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                String path = ImageUtils.getPath(context, uri);
                if (path == null || path.length() == 0) {
                    Toast.makeText(context, "Không thể tìm đúng đường dẫn của hình ảnh đã chọn", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        path = ResizeImage.resizeImageFromFile(path, Const.IMAGE_UPLOAD_WIDTH);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    files.add(new File(path));

                    Bitmap thumb = BitmapFactory.decodeFile(path, options);
                    valuesThumb.add(new Thumb(thumb, path));
                }
            }
            if (adapter != null) {
                adapter.clear();
                adapter.addAll(valuesThumb);
            }
        } else {
            Toast.makeText(context, "Không thể nhận được hình ảnh đã chọn.", Toast.LENGTH_LONG).show();
        }


        return files;
    }

    public static ArrayList<File> updateGridImage(Context context, Intent data, ThumbImageAdapter adapter, int number) {
        ArrayList<File> files = new ArrayList<>();
        ArrayList<Thumb> valuesThumb = new ArrayList<>();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = Const.SAMPLE_SIZE;

        Uri uriImage = data.getData();
        ClipData clipData = data.getClipData();

        if (uriImage != null) {
            String path = ImageUtils.getPath(context, uriImage);

            if (path == null || path.length() == 0) {
                Toast.makeText(context, "Không thể tìm đúng đường dẫn của hình ảnh đã chọn", Toast.LENGTH_LONG).show();
            } else {
                try {
                    path = ResizeImage.resizeImageFromFile(path, Const.IMAGE_UPLOAD_WIDTH);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                files.add(new File(path));
                Bitmap thumb = BitmapFactory.decodeFile(path, options);

                valuesThumb.add(new Thumb(thumb, path));
            }
            if (adapter != null) {
                adapter.clear();
                adapter.addAll(valuesThumb);
            }

        } else if (clipData != null) {
            for (int i = 0, n = clipData.getItemCount(); i < n; i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                String path = ImageUtils.getPath(context, uri);
                if (path == null || path.length() == 0) {
                    Toast.makeText(context, "Không thể tìm đúng đường dẫn của hình ảnh đã chọn", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        path = ResizeImage.resizeImageFromFile(path, Const.IMAGE_UPLOAD_WIDTH);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    files.add(new File(path));

                    Bitmap thumb = BitmapFactory.decodeFile(path, options);
                    valuesThumb.add(new Thumb(thumb));
                }
            }
            if (adapter != null) {
                adapter.clear();
                adapter.addAll(valuesThumb);
            }
        } else {
            Toast.makeText(context, "Không thể nhận được hình ảnh đã chọn.", Toast.LENGTH_LONG).show();
        }

        if (files.size() + number > 3) {
            Toast.makeText(context, "Không thể chọn hơn 3 hình", Toast.LENGTH_LONG).show();
            return new ArrayList<File>();
        }

        return files;
    }

    public static ArrayList<File> updateGridImage(String path, ThumbImageAdapter adapter) {
        ArrayList<File> files = new ArrayList<>();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = Const.SAMPLE_SIZE;

        try {
            path = ResizeImage.resizeImageFromFile(path, Const.IMAGE_UPLOAD_WIDTH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        files.add(new File(path));

        if (adapter != null) {
            ArrayList<Thumb> valuesThumb = new ArrayList<>();

            Bitmap thumb = BitmapFactory.decodeFile(path, options);

            valuesThumb.add(new Thumb(thumb, path));

            adapter.clear();
            adapter.addAll(valuesThumb);
        }

        return files;
    }

    public static File updateGridImage2(String path, ThumbImageAdapter adapter) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = Const.SAMPLE_SIZE;

//        try {
//            path = ResizeImage.resizeImageFromFile(path, Const.IMAGE_UPLOAD_WIDTH);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        if (adapter != null) {
            ArrayList<Thumb> valuesThumb = new ArrayList<>();

            Bitmap thumb = BitmapFactory.decodeFile(path, options);

            valuesThumb.add(new Thumb(thumb, path));

            adapter.clear();
            adapter.addAll(valuesThumb);
        }

        return new File(path);
    }

    public static File updateGridImage3(String path, ThumbImageAdapter adapter, Context c) throws FileNotFoundException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = Const.SAMPLE_SIZE;

//            path = ResizeImage.resizeImageFromFile(path, Const.IMAGE_UPLOAD_WIDTH);
        path = ResizeImage.compressImage(path, c);


        if (adapter != null) {
            ArrayList<Thumb> valuesThumb = new ArrayList<>();

            Bitmap thumb = BitmapFactory.decodeFile(path, options);

            valuesThumb.add(new Thumb(thumb, path));

            adapter.clear();
            adapter.addAll(valuesThumb);
        }

        return new File(path);
    }

}
