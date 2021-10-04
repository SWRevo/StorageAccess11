package id.indosw.configpubg.utils.java;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.documentfile.provider.DocumentFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import id.indosw.configpubg.utils.kotlin.AppUtils;

@SuppressWarnings("ReturnInsideFinallyBlock")
public class CopyObb {

    public static boolean copyFileObbFromUri(Context context, Uri fileUri, Uri targetUri)
    {
        InputStream fis = null;
        OutputStream fos = null;

        try {
            ContentResolver content = context.getContentResolver();
            fis = content.openInputStream(fileUri);
            fos = content.openOutputStream(targetUri);

            byte[] buff = new byte[1024];
            int length;

            while ((length = fis.read(buff)) > 0) {
                fos.write(buff, 0, length);
            }
        } catch (IOException e) {
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    return false;
                }
            }
            if (fos != null) {
                try {
                    fos.close();

                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void
     copyFileObb(Context applicationContext, Uri muri, Uri uri2) {
        DocumentFile mfile = DocumentFile.fromTreeUri(applicationContext, muri);
        DocumentFile mfile1 = DocumentFile.fromTreeUri(applicationContext, uri2);
        if (mfile1 != null) {
            if (mfile != null) {
                mfile1 = mfile1.createFile("*/*", "main.15336.com.tencent.ig.obb");
            }
        }
        if (mfile1 != null) {
            uri2 = mfile1.getUri();
        }
        if (copyFileObbFromUri(applicationContext, muri, uri2)) {
            AppUtils.showMessage(applicationContext, "Menyiapkan File Obb Berhasil");
        } else {
            AppUtils.showMessage(applicationContext, "Menyiapkan File Obb Gagal");
        }
    }

    /**public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }**/
}
