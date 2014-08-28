package com.shawnhu.seagull.seagull.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.shawnhu.seagull.R;
import com.shawnhu.seagull.seagull.twitter.SeagullTwitterConstants;
import com.shawnhu.seagull.seagull.twitter.fragments.SeagullProfileFragment;

public class ShowUserActivity extends FragmentActivity {
    protected long mAccountId = -1;
    protected long mUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        if (savedInstanceState == null) {
            mAccountId = getIntent().getLongExtra(SeagullTwitterConstants.EXTRA_ACCOUNT_ID, -1);
            mUserId    = getIntent().getLongExtra(SeagullTwitterConstants.EXTRA_USER_ID, -1);

            Bundle args = new Bundle();
            args.putLong(SeagullTwitterConstants.EXTRA_ACCOUNT_ID, mAccountId);
            args.putLong(SeagullTwitterConstants.EXTRA_USER_ID, mUserId);
            SeagullProfileFragment fragment = SeagullProfileFragment.newInstance(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}