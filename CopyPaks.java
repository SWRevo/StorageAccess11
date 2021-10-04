package id.indosw.configpubg.utils.java;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.documentfile.provider.DocumentFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import id.indosw.configpubg.utils.kotlin.AppUtils;

@SuppressWarnings("SpellCheckingInspection")
public class CopyPaks {
    public static boolean copyFilePaksFromUri(Context context, Uri fileUri, Uri targetUri)
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

    public static void copyFilePaks(Context applicationContext, Uri uriFrom, Uri uriTarget) {
        DocumentFile mFileFrom = DocumentFile.fromTreeUri(applicationContext, uriFrom);
        DocumentFile mFileTarget = DocumentFile.fromTreeUri(applicationContext, uriTarget);
        if (mFileTarget != null) {
            if (mFileFrom != null) {
                mFileTarget = mFileTarget.createFile("*/*", "game_patch_1.5.0.15334.pak");
            }
        }
        if (mFileTarget != null) {
            uriTarget = mFileTarget.getUri();
        }
        if (copyFilePaksFromUri(applicationContext, uriFrom, uriTarget)) {
            AppUtils.showMessage(applicationContext, "Penyiapan Mini Paks Berhasil");
        } else {
            AppUtils.showMessage(applicationContext, "Penyiapan Mini Paks Gagal");
        }
    }
}
