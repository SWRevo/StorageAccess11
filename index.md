# StorageAccess11

# Project Storage Management Android 11

### Storage Permission untuk android di bawah 10

```java
if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
}else {
    //Tambahkan kode di sini jika akses sudah diberikan sebelumnya
}

/**
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
**/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            //Tambahkan kode di sini jika akses sudah diberikan sebelumnya
        }
    }
```

## Storage Permission untuk android 10 dan ke atasnya

```java
if(SDK_INT >= Build.VERSION_CODES.R) {
    android11Permission();
}
```

### Berikut kode untuk request permission nya
```java

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void android11Permission() {
        if (!Environment.isExternalStorageManager()) {
            Snackbar.make(findViewById(android.R.id.content), "Android versi 11 atau diatasnya, izin khusus dibutuhkan", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Izinkan Sekarang", v -> {
                        try {
                            Uri uri = Uri.parse("package:" + "id.indosw.configpubg");
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                            startActivityForResult(intent, SETTINGS_PERMISSION_ANDROID11);
                        } catch (Exception ex) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivityForResult(intent, SETTINGS_PERMISSION_ANDROID11);
                        }
                    }).show();
        }
    }
    
    /**
    ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    **/
    
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {
            case SETTINGS_PERMISSION_ANDROID11:
                permissionCount = 0;
                muri = Uri.parse(pathAndroidObb);
                askPermissionSDK30();
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    showMessage(getApplicationContext(), "Akses Penyimpanan Android 11 Diberikan");
                } else {
                    showMessage(getApplicationContext(), "Allow permission for storage access!");
                }
                break;
            case FOLDER_ANDROID11_PERMISSION:
                
                break;
        }
    }
    
    /**
    ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    **/
    
    public void askPermissionSDK30 () {
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        i.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            i.putExtra(DocumentsContract.EXTRA_INITIAL_URI, muri);
        }
        startActivityForResult(i, FOLDER_ANDROID11_PERMISSION);
    }
    
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode) {
            case SETTINGS_PERMISSION_ANDROID11:
                break;
                
            /**
            --------------------------------------------------------------------------------------------------------
            //Method untuk menerima data dari permission path khusus yang sudah diberikan
            --------------------------------------------------------------------------------------------------------
            **/
            case FOLDER_ANDROID11_PERMISSION:
                if (_resultCode == Activity.RESULT_OK) {
                    if (_data != null) {
                        muri = _data.getData();
                        if (Uri.decode(muri.toString()).endsWith(":")) {
                            showMessage(getApplicationContext(),String.valueOf(R.string.cannot_rootpath));
                            askPermissionSDK30();
                        } else {
                            permissionCount++;
                            final int takeFlags = i.getFlags()&(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            getContentResolver().takePersistableUriPermission(muri,takeFlags);

                            sp.edit().putString(
                                    "FOLDER_URI" + (long) (permissionCount),
                                    muri.toString()
                            ).apply();

                            mfile = DocumentFile.fromTreeUri(MainActivity.this, muri);
                            if (mfile != null) {
                                mfile1 = mfile.createFile("*/*", "test.file");
                            }
                            if (mfile1 != null) {
                                uri2 = mfile1.getUri();
                            }

                            sp.edit().putString(
                                    "DIRECT_FOLDER_URI" + (long) (permissionCount),
                                    uri2.toString().substring(0,
                                            uri2.toString().length() - 9)
                            ).apply();

                            try {
                                DocumentsContract.deleteDocument(getApplicationContext().getContentResolver(),uri2);
                            } catch (FileNotFoundException ignored) {}

                            if (permissionCount == 1) {
                                muri = Uri.parse(pathPaksGame);
                                askPermissionSDK30();
                            } else {
                                if (permissionCount == 2) {
                                    muri = Uri.parse(pathActiveSavGame);
                                    askPermissionSDK30();
                                } else {
                                    if (permissionCount == 3) {
                                        muri = Uri.parse(pathAppConfig);
                                        askPermissionSDK30();
                                    } else {
                                        showMessage(getApplicationContext(),String.valueOf(R.string.permission_grant));
                                    }
                                }
                            }
                        }
                    } else {}
                } else {
                    showMessage(getApplicationContext(),String.valueOf(R.string.permission_revused));
                    finishAffinity();
                }
                break;
                /**
                --------------------------------------------------------------------------------------------------------
                //Method untuk menerima data dari permission path khusus yang sudah diberikan
                --------------------------------------------------------------------------------------------------------
                **/
        }
    }
    
    
```
