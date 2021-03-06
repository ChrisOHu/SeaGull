package com.shawnhu.seagull.seagull;


import android.os.StrictMode;

import com.shawnhu.seagull.R;
import com.shawnhu.seagull.seagull.twitter.TwitterManager;
import com.shawnhu.seagull.seagull.twitter.fragments.SeagullHomeFragment;
import com.shawnhu.seagull.seagull.twitter.fragments.SeagullProfileFragment;
import com.shawnhu.seagull.utils.ActivityUtils;
import com.shawnhu.seagull.widgets.AnyViewArrayAdapterItem;
import com.shawnhu.seagull.app.AppPreferences;
import com.shawnhu.seagull.app.SeagullApplication;
import com.shawnhu.seagull.fragments.DraftsFragment;
import com.shawnhu.seagull.fragments.FollowersFragment;
import com.shawnhu.seagull.fragments.FollowingsFragment;
import com.shawnhu.seagull.fragments.NotificationsFragment;
import com.shawnhu.seagull.fragments.SearchFragment;
import com.shawnhu.seagull.fragments.TweetsFragment;
import com.shawnhu.seagull.misc.IconicItem;
import com.shawnhu.seagull.seagull.activities.SeagullPreferenceActivity;
import com.shawnhu.seagull.views.AvatarCard;

public class Seagull extends SeagullApplication {

    /**
     * NAVIGATION MENU ITEMS
     */
    public static final String DRAWER_MENU_PROF = "Profile";
    public static final String DRAWER_MENU_HOME = "Home";
    public static final String DRAWER_MENU_PRFS = "Preferences";

    static public AvatarCard aC = new AvatarCard(null, "Twitter", "@Twitter");
    public static AnyViewArrayAdapterItem mSeagullDrawerItems[] = {
            /*  layout,          content,  getViewInterface,           targetActionClass, actionBarTitle */
            new AnyViewArrayAdapterItem(R.layout.layout_avatar, aC, aC, SeagullProfileFragment.class, DRAWER_MENU_PROF),

            /* ~default_layout~, content, ~default getViewInterface~,  targetActionClass, actionBarTitle */
            new AnyViewArrayAdapterItem(new IconicItem(R.drawable.ic_home_128, DRAWER_MENU_HOME), SeagullHomeFragment.class,       DRAWER_MENU_HOME),
            new AnyViewArrayAdapterItem(new IconicItem(R.drawable.ic_settings_128,  DRAWER_MENU_PRFS), SeagullPreferenceActivity.class, DRAWER_MENU_PRFS),
    };

    /**
     * PREFERENCE KEYS
     */
    static public String PREF_SEAGULL_NOTIFICATION_ON;
    static public String PREF_SEAGULL_NOTIFICATION_RINGTONE;
    static public String PREF_SEAGULL_NOTIFICATION_VIRATE;

    /**
     * Seagull's themes, KEY is PREF_APP_THEME, which will be handled by wrapper
     */
    static public String SEAGULL_THEMES[] = {
            Integer.toString(R.style.Theme_Day),
            Integer.toString(R.style.Theme_Night),
    };

    static public final boolean DEVELOPER_MODE = true;
    static public final int NAV_PREFRENCE = 2;

    @Override
    public void onCreate() {
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyLog()
                    .build());
        }
        super.onCreate();

        AppPreferences.addPreferencesToMap(AppPreferences.PREF_APP_THEME, SEAGULL_THEMES);
        AppPreferences.mDefaultAppTheme = ActivityUtils.getTheme(getApplicationContext(), AppPreferences.mDefaultAppTheme);

        PREF_SEAGULL_NOTIFICATION_ON = getString(R.string.PREF_SEAGULL_NOTIFICATION_ON);
        PREF_SEAGULL_NOTIFICATION_RINGTONE = getString(R.string.PREF_SEAGULL_NOTIFICATION_RINGTONE);
        PREF_SEAGULL_NOTIFICATION_VIRATE = getString(R.string.PREF_SEAGULL_NOTIFICATION_VIRATE);

        TwitterManager.init(this);
    }


    static public CurrentAccount sCurrentAccount = new CurrentAccount();
    /** app's globle data */
    static public class CurrentAccount {
        public long sAccountId = -1;
    }
}
