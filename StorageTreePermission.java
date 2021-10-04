package id.indosw.configpubg.utils.java;

@SuppressWarnings("SpellCheckingInspection")
public class StorageTreePermission {
    public static String pathAndroidObb = "content://com.android.externalstorage.documents/tree/primary%3AAndroid/document/primary%3AAndroid%2Fobb%2Fcom.tencent.ig";
    public static String pathAndroidData = "content://com.android.externalstorage.documents/tree/primary%3AAndroid/document/primary%3AAndroid%2Fdata";
    public static String pathAppConfig = "content://com.android.externalstorage.documents/tree/primary%3A.configpubg15/document/primary%3A.configpubg15";
    public static String slashTreeFormat = "%2F";
    public static String obbFileName = "main.15336.com.tencent.ig.obb";
    public static String obbPathName = "obb";
    public static String noRecoilPathName = "norecoil";
    public static String originalObbPathName = "original";

    public static String activeSavPath = "activesav";
    public static String activeSavLight = "light";
    public static String activeSavNight = "night";
    public static String activeSavFileName = "Active.sav";

    public static String paksPath = "paks";
    public static String paksNormalSky = "normalsky";
    public static String paksBlackSky = "blacksky";
    public static String paksFileName = "game_patch_1.5.0.15334.pak";

    public static String extentionPathAndroidDataActiveSav = "com.tencent.ig"+slashTreeFormat+"files"+slashTreeFormat+"UE4Game"+slashTreeFormat+"ShadowTrackerExtra"+slashTreeFormat+"ShadowTrackerExtra"+slashTreeFormat+"Saved"+slashTreeFormat+"SaveGames"+slashTreeFormat;
    public static String extentionPathAndroidDataPaks = "com.tencent.ig"+slashTreeFormat+"files"+slashTreeFormat+"UE4Game"+slashTreeFormat+"ShadowTrackerExtra"+slashTreeFormat+"ShadowTrackerExtra"+slashTreeFormat+"Saved"+slashTreeFormat+"Paks"+slashTreeFormat;
    public static String pathPaksGame = "content://com.android.externalstorage.documents/tree/primary%3AAndroid/document/primary%3AAndroid%2Fdata%2Fcom.tencent.ig%2Ffiles%2FUE4Game%2FShadowTrackerExtra%2FShadowTrackerExtra%2FSaved%2FPaks";
    public static String pathActiveSavGame = "content://com.android.externalstorage.documents/tree/primary%3AAndroid/document/primary%3AAndroid%2Fdata%2Fcom.tencent.ig%2Ffiles%2FUE4Game%2FShadowTrackerExtra%2FShadowTrackerExtra%2FSaved%2FSaveGames";
}
