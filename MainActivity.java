package id.indosw.configpubg.activities.java;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import id.indosw.configpubg.R;
import id.indosw.configpubg.networking.RequestNetwork;
import id.indosw.configpubg.networking.RequestNetworkController;
import id.indosw.configpubg.utils.kotlin.AppUtils;
import id.indosw.configpubg.utils.kotlin.FileUtil;
import id.indosw.lib.toggle.widget.LabeledSwitch;

import static android.os.Build.VERSION.SDK_INT;
import static id.indosw.configpubg.utils.java.CopyObb.copyFileObb;
import static id.indosw.configpubg.utils.java.CopyPaks.copyFilePaks;
import static id.indosw.configpubg.utils.java.CopySav.copyFileSav;
import static id.indosw.configpubg.utils.java.StorageTreePermission.activeSavFileName;
import static id.indosw.configpubg.utils.java.StorageTreePermission.activeSavLight;
import static id.indosw.configpubg.utils.java.StorageTreePermission.activeSavNight;
import static id.indosw.configpubg.utils.java.StorageTreePermission.activeSavPath;
import static id.indosw.configpubg.utils.java.StorageTreePermission.noRecoilPathName;
import static id.indosw.configpubg.utils.java.StorageTreePermission.obbFileName;
import static id.indosw.configpubg.utils.java.StorageTreePermission.obbPathName;
import static id.indosw.configpubg.utils.java.StorageTreePermission.originalObbPathName;
import static id.indosw.configpubg.utils.java.StorageTreePermission.paksBlackSky;
import static id.indosw.configpubg.utils.java.StorageTreePermission.paksFileName;
import static id.indosw.configpubg.utils.java.StorageTreePermission.paksNormalSky;
import static id.indosw.configpubg.utils.java.StorageTreePermission.paksPath;
import static id.indosw.configpubg.utils.java.StorageTreePermission.pathActiveSavGame;
import static id.indosw.configpubg.utils.java.StorageTreePermission.pathAndroidObb;
import static id.indosw.configpubg.utils.java.StorageTreePermission.pathAppConfig;
import static id.indosw.configpubg.utils.java.StorageTreePermission.pathPaksGame;
import static id.indosw.configpubg.utils.java.StorageTreePermission.slashTreeFormat;
import static id.indosw.configpubg.utils.kotlin.AppUtils.isConnected;
import static id.indosw.configpubg.utils.kotlin.AppUtils.showMessage;
import static id.indosw.configpubg.utils.kotlin.StringUtils.modActiveSavLight;
import static id.indosw.configpubg.utils.kotlin.StringUtils.modActiveSavNight;
import static id.indosw.configpubg.utils.kotlin.StringUtils.modPaksWhiteBodyBlackSky;
import static id.indosw.configpubg.utils.kotlin.StringUtils.modPaksWhiteBodyNormalSky;
import static id.indosw.configpubg.utils.kotlin.StringUtils.obbModNoRecoil;
import static id.indosw.configpubg.utils.kotlin.StringUtils.obbModOri;
import static id.indosw.configpubg.utils.kotlin.StringUtils.pathActiveSav;
import static id.indosw.configpubg.utils.kotlin.StringUtils.pathApp;
import static id.indosw.configpubg.utils.kotlin.StringUtils.pathOBB;
import static id.indosw.configpubg.utils.kotlin.StringUtils.pathPaks;

@SuppressWarnings({"SpellCheckingInspection", "deprecation", "StatementWithEmptyBody", "FieldCanBeLocal"})
public class MainActivity extends AppCompatActivity {

    private final Timer _timer = new Timer();

    private static final int SETTINGS_PERMISSION_ANDROID11 = 2000;
    private static final int FOLDER_ANDROID11_PERMISSION = 43;

    private LinearLayout linearsetting;
    private LinearLayout linearAkses;
    private Button runPUBG;
    private LabeledSwitch originalObb;
    private LabeledSwitch noRecoilObb;
    private LabeledSwitch whiteBodyNormalSky;
    private LabeledSwitch whiteBodyBlackSky;
    private LabeledSwitch lightMode;
    private LabeledSwitch nightMode;
    private EditText codeAkses;

    private RequestNetwork pingServer;
    private RequestNetwork.RequestListener _pingServer_request_listener;
    private final Intent p = new Intent(Intent.ACTION_GET_CONTENT);
    private SharedPreferences cacheAkses;
    private final Intent iReqAccess = new Intent();

    private double permissionCount = 0;
    private final Intent i = new Intent();
    private SharedPreferences sp;


    private DocumentFile mfile;
    private DocumentFile mfile1;
    private Uri muri;
    private Uri uri2;
    private Uri uriForDelete;

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private ProgressDialog progressDialog;
    private TimerTask prog;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
        else {
            initializeLogic();
        }
    }

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
                    })
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            initializeLogic();
        }
    }

    @SuppressLint("HandlerLeak")
    private void initialize() {
        /*ScrollView vscroll1 = findViewById(R.id.vscroll1);
        LinearLayout linear1 = findViewById(R.id.linear1);
        LinearLayout headerLinear = findViewById(R.id.headerLinear);
        ImageView imageview1 = findViewById(R.id.imageview1);
        LinearLayout linear5 = findViewById(R.id.linear5);
        TextView title = findViewById(R.id.title);
        TextView copyright = findViewById(R.id.copyright);
        LinearLayout linearObbSwitch = findViewById(R.id.linearObbSwitch);
        LinearLayout linearPaksSwitch = findViewById(R.id.linearPaksSwitch);
        TextView noteLessGrass = findViewById(R.id.noteLessGrass);
        LinearLayout linearActiveSavSwitch = findViewById(R.id.linearActiveSavSwitch);
        LinearLayout linear4 = findViewById(R.id.linear4);
        ImageView imageview2 = findViewById(R.id.imageview2);
        TextView noteIpadView = findViewById(R.id.noteIpadView);*/

        linearsetting = findViewById(R.id.linearsetting);
        linearAkses = findViewById(R.id.linearAkses);

        runPUBG = findViewById(R.id.runPUBG);
        originalObb = findViewById(R.id.originalObb);
        noRecoilObb = findViewById(R.id.noRecoilObb);
        whiteBodyNormalSky = findViewById(R.id.whiteBodyNormalSky);
        whiteBodyBlackSky = findViewById(R.id.whiteBodyBlackSky);
        lightMode = findViewById(R.id.lightMode);
        nightMode = findViewById(R.id.nightMode);
        codeAkses = findViewById(R.id.codeAkses);
        Button submit = findViewById(R.id.submit);
        Button mintaAkses = findViewById(R.id.mintaAkses);
        pingServer = new RequestNetwork(this);
        p.setType("*/*");
        p.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        cacheAkses = getSharedPreferences("cacheAkses", Activity.MODE_PRIVATE);
        sp = getSharedPreferences("PERMISSION", Activity.MODE_PRIVATE);

        runPUBG.setOnClickListener(_view -> {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.ig");
            //noinspection IfStatementWithIdenticalBranches
            if (intent != null) {
                startActivity(intent);
            } else {
                intent = new Intent(Intent.ACTION_VIEW); intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("market://details?id=" + "com.tencent.ig")); startActivity(intent);
            }
        });

        originalObb.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = "ORIGINAL";
                runner.execute(sleepTime);
            }
        });

        noRecoilObb.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = "NORECOIL";
                runner.execute(sleepTime);
            }
        });

        whiteBodyNormalSky.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) {

                if(SDK_INT >= Build.VERSION_CODES.R) {
                    uriForDelete = Uri.parse(sp.getString(
                            "DIRECT_FOLDER_URI2",
                            "")
                            +paksFileName
                    );

                    DocumentFile sourceFile = DocumentFile.fromSingleUri(getApplicationContext(), uriForDelete);
                    boolean boolPaksExists = false;
                    if (sourceFile != null) {
                        boolPaksExists = sourceFile.exists();
                    }

                    if (boolPaksExists) {
                        try{
                            DocumentsContract.deleteDocument(
                                    getApplicationContext()
                                            .getContentResolver(),
                                    uriForDelete
                            );
                        } catch (FileNotFoundException e) {
                            AppUtils.showMessage(getApplicationContext(), e.getMessage());
                        }
                    }

                    muri = Uri.parse(sp.getString(
                            "DIRECT_FOLDER_URI4",
                            "")
                            +paksPath
                            +slashTreeFormat
                            +paksNormalSky
                            +slashTreeFormat
                            +paksFileName
                    );
                    uri2 = Uri.parse(sp.getString(
                            "DIRECT_FOLDER_URI2",
                            "")
                    );
                    copyFilePaks(getApplicationContext(), muri, uri2);
                } else {
                    FileUtil.copyFile(modPaksWhiteBodyNormalSky, pathPaks + paksFileName);
                }

                whiteBodyBlackSky.setOn(false);
                runPUBG.setVisibility(View.VISIBLE);
            }
        });

        whiteBodyBlackSky.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) {
                if(SDK_INT >= Build.VERSION_CODES.R) {
                    uriForDelete = Uri.parse(sp.getString(
                            "DIRECT_FOLDER_URI2",
                            "")
                            +paksFileName
                    );

                    DocumentFile sourceFile = DocumentFile.fromSingleUri(getApplicationContext(), uriForDelete);
                    boolean boolPaksExists = false;
                    if (sourceFile != null) {
                        boolPaksExists = sourceFile.exists();
                    }

                    if (boolPaksExists) {
                        try{
                            DocumentsContract.deleteDocument(
                                    getApplicationContext()
                                            .getContentResolver(),
                                    uriForDelete
                            );
                        } catch (FileNotFoundException e) {
                            AppUtils.showMessage(getApplicationContext(), e.getMessage());
                        }
                    }

                    muri = Uri.parse(sp.getString(
                            "DIRECT_FOLDER_URI4",
                            "")
                            +paksPath
                            +slashTreeFormat
                            +paksBlackSky
                            +slashTreeFormat
                            +paksFileName
                    );
                    uri2 = Uri.parse(sp.getString(
                            "DIRECT_FOLDER_URI2",
                            "")
                    );
                    copyFilePaks(getApplicationContext(), muri, uri2);
                } else {
                    FileUtil.copyFile(modPaksWhiteBodyBlackSky, pathPaks + paksFileName);
                }

                whiteBodyNormalSky.setOn(false);
                runPUBG.setVisibility(View.VISIBLE);
            }
        });

        lightMode.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) {

                if(SDK_INT >= Build.VERSION_CODES.R) {
                    uriForDelete = Uri.parse(sp.getString(
                            "DIRECT_FOLDER_URI3",
                            "")
                            +activeSavFileName
                    );
                    try{
                        DocumentsContract.deleteDocument(
                                getApplicationContext()
                                        .getContentResolver(),
                                uriForDelete
                        );
                    } catch (FileNotFoundException ignored) {}

                    muri = Uri.parse(sp.getString(
                            "DIRECT_FOLDER_URI4",
                            "")
                            +activeSavPath
                            +slashTreeFormat
                            +activeSavLight
                            +slashTreeFormat
                            +activeSavFileName
                    );
                    uri2 = Uri.parse(sp.getString(
                            "DIRECT_FOLDER_URI3",
                            "")
                    );
                    copyFileSav(getApplicationContext(), muri, uri2);
                } else {
                    FileUtil.copyFile(modActiveSavLight, pathActiveSav);
                }

                runPUBG.setVisibility(View.VISIBLE);
                nightMode.setOn(false);
            }
        });

        nightMode.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) {

                if(SDK_INT >= Build.VERSION_CODES.R) {
                    uriForDelete = Uri.parse(sp.getString(
                            "DIRECT_FOLDER_URI3",
                            "")
                            +activeSavFileName
                    );
                    try{
                        DocumentsContract.deleteDocument(
                                getApplicationContext()
                                        .getContentResolver(),
                                uriForDelete
                        );
                    } catch (FileNotFoundException ignored) {}

                    muri = Uri.parse(sp.getString(
                            "DIRECT_FOLDER_URI4",
                            "")
                            +activeSavPath
                            +slashTreeFormat
                            +activeSavNight
                            +slashTreeFormat
                            +activeSavFileName
                    );
                    uri2 = Uri.parse(sp.getString(
                            "DIRECT_FOLDER_URI3",
                            "")
                    );
                    copyFileSav(getApplicationContext(), muri, uri2);
                } else {
                    FileUtil.copyFile(modActiveSavNight, pathActiveSav);
                }

                runPUBG.setVisibility(View.VISIBLE);
                lightMode.setOn(false);
            }
        });

        submit.setOnClickListener(_view -> {
            if (isConnected(getApplicationContext())) {
                pingServer.startRequestNetwork(RequestNetworkController.GET,
                        "https://config-pubg-v15-default-rtdb.firebaseio.com/AksesApp/"
                                + codeAkses.getText().toString()
                                + ".json", "", _pingServer_request_listener);
            }
            else {
                showMessage(getApplicationContext(), "Periksa Koneksi Internetmu");
            }
        });

        mintaAkses.setOnClickListener(_view -> {
            iReqAccess.setAction(Intent.ACTION_VIEW);
            iReqAccess.setData(Uri.parse("https://t.me/IndoSW"));
            startActivity(iReqAccess);
        });

        _pingServer_request_listener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(@Nullable String _param1, @Nullable String _param2, @NotNull HashMap<String, Object> _param3) {
                if (_param2 != null) {
                    if (_param2.equals("1")) {
                        linearAkses.setVisibility(View.GONE);
                        linearsetting.setVisibility(View.VISIBLE);
                        cacheAkses.edit().putString("open", "yes").apply();
                    }
                    else {
                        showMessage(getApplicationContext(), "Belum Ada Akses");
                    }
                }
            }

            @Override
            public void onErrorResponse(String _param1, String _param2) {

            }
        };
    }

    private void initializeLogic() {

        if(SDK_INT >= Build.VERSION_CODES.R) {
            android11Permission();
        }

        runPUBG.setVisibility(View.GONE);
        linearsetting.setVisibility(View.GONE);

        if (FileUtil.isExistFile(pathApp)) {
            FileUtil.makeDir(pathApp);
        }


        if (cacheAkses.getString("open", "").equals("yes")) {
            linearAkses.setVisibility(View.GONE);
            linearsetting.setVisibility(View.VISIBLE);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        if (SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initYouTubePlayerView();
    }

    private void initYouTubePlayerView() {
        String idVideo = "OP0k4gIGtOY";
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        initPictureInPicture(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer initializedYouTubePlayer) {
                youTubePlayer = initializedYouTubePlayer;
                youTubePlayer.cueVideo(idVideo,0);
                //YouTubePlayerUtils.loadOrCueVideo(youTubePlayer, getLifecycle(), finalIdVideo,0f);
            }
        });
    }

    private void initPictureInPicture(YouTubePlayerView youTubePlayerView) {
        ImageView pictureInPictureIcon = new ImageView(this);
        pictureInPictureIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_picture_in_picture_24));

        pictureInPictureIcon.setOnClickListener( view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                boolean supportsPIP = getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
                if(supportsPIP)
                    enterPictureInPictureMode();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Can't enter picture in picture mode")
                        .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
                        .show();
            }
        });

        youTubePlayerView.getPlayerUiController().addView(pictureInPictureIcon);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        if(isInPictureInPictureMode) {
            youTubePlayerView.enterFullScreen();
            youTubePlayerView.getPlayerUiController().showUi(false);
        } else {
            youTubePlayerView.exitFullScreen();
            youTubePlayerView.getPlayerUiController().showUi(true);
        }
    }

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
                if (_resultCode == Activity.RESULT_OK) {
                    if (_data != null) {
                        muri = _data.getData();
                        if (Uri.decode(muri.toString()).endsWith(":")) {
                            showMessage(
                                    getApplicationContext(),
                                    String.valueOf(R.string.cannot_rootpath)
                            );
                            askPermissionSDK30();
                        } else {
                            permissionCount++;
                            final int takeFlags = i.getFlags()
                                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                            getContentResolver()
                                    .takePersistableUriPermission(
                                            muri,
                                            takeFlags
                                    );

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
                                DocumentsContract.deleteDocument(
                                        getApplicationContext().getContentResolver(),
                                        uri2
                                );
                            } catch (FileNotFoundException ignored) {
                            }

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
                                        showMessage(
                                                getApplicationContext(),
                                                String.valueOf(R.string.permission_grant)
                                        );
                                    }
                                }
                            }
                        }
                    } else {
                    }
                } else {
                    showMessage(
                            getApplicationContext(),
                            String.valueOf(R.string.permission_revused)
                    );
                    finishAffinity();
                }
                break;
        }
    }

    public void askPermissionSDK30 () {
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        i.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            i.putExtra(DocumentsContract.EXTRA_INITIAL_URI, muri);
        }
        startActivityForResult(i, FOLDER_ANDROID11_PERMISSION);
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunner extends AsyncTask<String, Integer, String> {

        private String resp;
        private int counter;

        @Override
        protected String doInBackground(String... params) {
            //publishProgress("Menutup..."); // Calls onProgressUpdate()
            prog = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        counter++;
                        publishProgress(counter*10);
                        if (counter == 10){
                            prog.cancel();
                        }
                    });
                }
            };
            _timer.scheduleAtFixedRate(prog, 0, 1000);

            try {
                int time = Integer.parseInt("10") * 1000;
                Thread.sleep(time);
                resp = params[0];

                if (params[0].equals("ORIGINAL")){
                    if(SDK_INT >= Build.VERSION_CODES.R) {
                        uriForDelete = Uri.parse(sp.getString(
                                "DIRECT_FOLDER_URI1",
                                "")
                                +obbFileName
                        );
                        try{
                            DocumentsContract.deleteDocument(
                                    getApplicationContext()
                                            .getContentResolver(),
                                    uriForDelete
                            );
                        } catch (FileNotFoundException ignored) {}

                        muri = Uri.parse(sp.getString(
                                "DIRECT_FOLDER_URI4",
                                "")
                                +obbPathName
                                +slashTreeFormat
                                +originalObbPathName
                                +slashTreeFormat
                                +obbFileName
                        );
                        uri2 = Uri.parse(sp.getString(
                                "DIRECT_FOLDER_URI1",
                                "")
                        );
                        copyFileObb(getApplicationContext(), muri, uri2);
                    } else {
                        FileUtil.deleteFile(pathOBB);
                        FileUtil.makeDir(pathOBB);
                        FileUtil.copyFile(obbModOri, pathOBB + "main.15336.com.tencent.ig.obb");
                    }
                }
                else {
                    if(SDK_INT >= Build.VERSION_CODES.R) {
                        uriForDelete = Uri.parse(sp.getString(
                                "DIRECT_FOLDER_URI1",
                                "")
                                +obbFileName
                        );
                        try{
                            DocumentsContract.deleteDocument(
                                    getApplicationContext()
                                            .getContentResolver(),
                                    uriForDelete
                            );
                        } catch (FileNotFoundException ignored) {}

                        muri = Uri.parse(sp.getString(
                                "DIRECT_FOLDER_URI4",
                                "")
                                +obbPathName
                                +slashTreeFormat
                                +noRecoilPathName
                                +slashTreeFormat
                                +obbFileName
                        );
                        uri2 = Uri.parse(sp.getString(
                                "DIRECT_FOLDER_URI1",
                                "")
                        );
                        copyFileObb(getApplicationContext(), muri, uri2);
                    } else {
                        try {
                            FileUtil.deleteFile(pathOBB);
                            FileUtil.makeDir(pathOBB);
                            FileUtil.copyFile(obbModNoRecoil, pathOBB + "main.15336.com.tencent.ig.obb");
                        } catch (Exception e) {
                            showMessage(getApplicationContext(), e.getMessage());
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            //finalResult.setText(result);
            if (result.equals("ORIGINAL")){
                runPUBG.setVisibility(View.VISIBLE);
                noRecoilObb.setOn(false);
            } else if(result.equals("NORECOIL")){
                runPUBG.setVisibility(View.VISIBLE);
                originalObb.setOn(false);
            }
        }


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Proses menyiapkan file OBB 600 MB, mohon tunggu...");
            progressDialog.setTitle("Menyiapkan Config RECOIL");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }


        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setProgress(progress[0]);
        }
    }
}
