/*
 * 				Twidere - Twitter client for Android
 * 
 *  Copyright (C) 2012-2014 Mariotaku Lee <mariotaku.lee@gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.shawnhu.seagull.seagull.twitter.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.shawnhu.seagull.seagull.twitter.utils.Utils;
import com.shawnhu.seagull.seagull.twitter.utils.content.ContentResolverUtils;
import com.shawnhu.seagull.seagull.twitter.content.TweetStore.Accounts;
import com.shawnhu.seagull.utils.querybuilder.Columns;
import com.shawnhu.seagull.utils.querybuilder.RawItemArray;
import com.shawnhu.seagull.utils.querybuilder.Where;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TwitterAccount implements Parcelable {

    public static final Parcelable.Creator<TwitterAccount> CREATOR = new Parcelable.Creator<TwitterAccount>() {

        @Override
        public TwitterAccount createFromParcel(final Parcel in) {
            return new TwitterAccount(in);
        }

        @Override
        public TwitterAccount[] newArray(final int size) {
            return new TwitterAccount[size];
        }
    };

    public final String screen_name, name, profile_image_url, profile_banner_url;
    public final long account_id;
    public final int color;
    public final boolean is_activated;
    public final boolean is_dummy;

    public TwitterAccount(final Cursor cursor, final Indices indices) {
        is_dummy = false;
        screen_name = indices.screen_name != -1 ? cursor.getString(indices.screen_name) : null;
        name = indices.name != -1 ? cursor.getString(indices.name) : null;
        account_id = indices.account_id != -1 ? cursor.getLong(indices.account_id) : -1;
        profile_image_url = indices.profile_image_url != -1 ? cursor.getString(indices.profile_image_url) : null;
        profile_banner_url = indices.profile_banner_url != -1 ? cursor.getString(indices.profile_banner_url) : null;
        color = indices.color != -1 ? cursor.getInt(indices.color) : Color.TRANSPARENT;
        is_activated = indices.is_activated != -1 ? cursor.getInt(indices.is_activated) == 1 : false;
    }

    public TwitterAccount(final Parcel source) {
        is_dummy = source.readInt() == 1;
        is_activated = source.readInt() == 1;
        account_id = source.readLong();
        name = source.readString();
        screen_name = source.readString();
        profile_image_url = source.readString();
        profile_banner_url = source.readString();
        color = source.readInt();
    }

    private TwitterAccount() {
        is_dummy = true;
        screen_name = null;
        name = null;
        account_id = -1;
        profile_image_url = null;
        profile_banner_url = null;
        color = 0;
        is_activated = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "TwitterAccount{screen_name=" + screen_name + ", name=" + name + ", profile_image_url=" + profile_image_url
                + ", profile_banner_url=" + profile_banner_url + ", account_id=" + account_id + ", color=" + color
                + ", is_activated=" + is_activated + ", is_dummy=" + is_dummy + "}";
    }

    @Override
    public void writeToParcel(final Parcel out, final int flags) {
        out.writeInt(is_dummy ? 1 : 0);
        out.writeInt(is_activated ? 1 : 0);
        out.writeLong(account_id);
        out.writeString(name);
        out.writeString(screen_name);
        out.writeString(profile_image_url);
        out.writeString(profile_banner_url);
        out.writeInt(color);
    }

    public static TwitterAccount dummyInstance() {
        return new TwitterAccount();
    }

    public static TwitterAccount getAccount(final Context context, final long account_id) {
        if (context == null) return null;
        final Cursor cur = ContentResolverUtils.query(context.getContentResolver(), Accounts.CONTENT_URI,
                Accounts.COLUMNS, Accounts.ACCOUNT_ID + " = " + account_id, null, null);
        if (cur != null) {
            try {
                if (cur.getCount() > 0 && cur.moveToFirst()) {
                    final Indices indices = new Indices(cur);
                    cur.moveToFirst();
                    return new TwitterAccount(cur, indices);
                }
            } finally {
                cur.close();
            }
        }
        return null;
    }

    public static long[] getAccountIds(final TwitterAccount[] accounts) {
        final long[] ids = new long[accounts.length];
        for (int i = 0, j = accounts.length; i < j; i++) {
            ids[i] = accounts[i].account_id;
        }
        return ids;
    }

    public static TwitterAccount[] getAccounts(final Context context, final boolean activatedOnly,
            final boolean officialKeyOnly) {
        final List<TwitterAccount> list = getAccountsList(context, activatedOnly, officialKeyOnly);
        return list.toArray(new TwitterAccount[list.size()]);
    }

    public static TwitterAccount[] getAccounts(final Context context, final long[] accountIds) {
        if (context == null) return new TwitterAccount[0];
        final String where = accountIds != null ? Where.in(new Columns.Column(Accounts.ACCOUNT_ID),
                new RawItemArray(accountIds)).getSQL() : null;
        final Cursor cur = ContentResolverUtils.query(context.getContentResolver(), Accounts.CONTENT_URI,
                Accounts.COLUMNS_NO_CREDENTIALS, where, null, null);
        if (cur == null) return new TwitterAccount[0];
        try {
            final Indices idx = new Indices(cur);
            cur.moveToFirst();
            final TwitterAccount[] names = new TwitterAccount[cur.getCount()];
            while (!cur.isAfterLast()) {
                names[cur.getPosition()] = new TwitterAccount(cur, idx);
                cur.moveToNext();
            }
            return names;
        } finally {
            cur.close();
        }
    }

    public static List<TwitterAccount> getAccountsList(final Context context, final boolean activatedOnly) {
        return getAccountsList(context, activatedOnly, false);
    }

    public static List<TwitterAccount> getAccountsList(final Context context, final boolean activatedOnly,
            final boolean officialKeyOnly) {
        if (context == null) return Collections.emptyList();
        final ArrayList<TwitterAccount> accounts = new ArrayList<TwitterAccount>();
        final Cursor cur = ContentResolverUtils.query(context.getContentResolver(), Accounts.CONTENT_URI,
                Accounts.COLUMNS_NO_CREDENTIALS, activatedOnly ? Accounts.IS_ACTIVATED + " = 1" : null, null, null);
        if (cur != null) {
            final Indices indices = new Indices(cur);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!officialKeyOnly) {
                    accounts.add(new TwitterAccount(cur, indices));
                } else {
                    final String consumerKey = cur.getString(indices.consumer_key);
                    final String consumerSecret = cur.getString(indices.consumer_secret);
                    if (Utils.shouldForceUsingPrivateAPIs(context)
                            || Utils.isOfficialConsumerKeySecret(context, consumerKey, consumerSecret)) {
                        accounts.add(new TwitterAccount(cur, indices));
                    }
                }
                cur.moveToNext();
            }
            cur.close();
        }
        return accounts;
    }

    public static TwitterAccountWithCredentials getAccountWithCredentials(final Context context, final long account_id) {
        if (context == null) return null;
        final Cursor cur = ContentResolverUtils.query(context.getContentResolver(), Accounts.CONTENT_URI,
                Accounts.COLUMNS, Accounts.ACCOUNT_ID + " = " + account_id, null, null);
        if (cur != null) {
            try {
                if (cur.getCount() > 0 && cur.moveToFirst()) {
                    final Indices indices = new Indices(cur);
                    cur.moveToFirst();
                    return new TwitterAccountWithCredentials(cur, indices);
                }
            } finally {
                cur.close();
            }
        }
        return null;
    }

    public static class TwitterAccountWithCredentials extends TwitterAccount {

        public final int auth_type;
        public final String consumer_key, consumer_secret;
        public final String basic_auth_username, basic_auth_password;
        public final String oauth_token, oauth_token_secret;
        public final String api_url_format;
        public final boolean same_oauth_signing_url;

        public TwitterAccountWithCredentials(final Cursor cursor, final Indices indices) {
            super(cursor, indices);
            auth_type = cursor.getInt(indices.auth_type);
            consumer_key = cursor.getString(indices.consumer_key);
            consumer_secret = cursor.getString(indices.consumer_secret);
            basic_auth_username = cursor.getString(indices.basic_auth_username);
            basic_auth_password = cursor.getString(indices.basic_auth_password);
            oauth_token = cursor.getString(indices.oauth_token);
            oauth_token_secret = cursor.getString(indices.oauth_token_secret);
            api_url_format = cursor.getString(indices.api_url_format);
            same_oauth_signing_url = cursor.getInt(indices.same_oauth_signing_url) == 1;
        }

        @Override
        public String toString() {
            return "TwitterAccountWithCredentials{auth_type=" + auth_type + ", consumer_key=" + consumer_key
                    + ", consumer_secret=" + consumer_secret + ", basic_auth_password=" + basic_auth_password
                    + ", oauth_token=" + oauth_token + ", oauth_token_secret=" + oauth_token_secret
                    + ", api_url_format=" + api_url_format + ", same_oauth_signing_url=" + same_oauth_signing_url + "}";
        }

        public static final boolean isOfficialCredentials(final Context context, final TwitterAccountWithCredentials account) {
            if (account == null) return false;
            final boolean isOAuth = account.auth_type == Accounts.AUTH_TYPE_OAUTH
                    || account.auth_type == Accounts.AUTH_TYPE_XAUTH;
            final String consumerKey = account.consumer_key, consumerSecret = account.consumer_secret;
            return isOAuth && Utils.isOfficialConsumerKeySecret(context, consumerKey, consumerSecret);
        }
    }

    public static final class Indices {

        public final int screen_name, name, account_id, profile_image_url, profile_banner_url, color, is_activated,
                auth_type, consumer_key, consumer_secret, basic_auth_username, basic_auth_password, oauth_token,
                oauth_token_secret, api_url_format, same_oauth_signing_url;

        public Indices(final Cursor cursor) {
            screen_name = cursor.getColumnIndex(Accounts.SCREEN_NAME);
            name = cursor.getColumnIndex(Accounts.NAME);
            account_id = cursor.getColumnIndex(Accounts.ACCOUNT_ID);
            profile_image_url = cursor.getColumnIndex(Accounts.PROFILE_IMAGE_URL);
            profile_banner_url = cursor.getColumnIndex(Accounts.PROFILE_BANNER_URL);
            color = cursor.getColumnIndex(Accounts.COLOR);
            is_activated = cursor.getColumnIndex(Accounts.IS_ACTIVATED);
            auth_type = cursor.getColumnIndex(Accounts.AUTH_TYPE);
            consumer_key = cursor.getColumnIndex(Accounts.CONSUMER_KEY);
            consumer_secret = cursor.getColumnIndex(Accounts.CONSUMER_SECRET);
            basic_auth_username = cursor.getColumnIndex(Accounts.BASIC_AUTH_USERNAME);
            basic_auth_password = cursor.getColumnIndex(Accounts.BASIC_AUTH_PASSWORD);
            oauth_token = cursor.getColumnIndex(Accounts.OAUTH_TOKEN);
            oauth_token_secret = cursor.getColumnIndex(Accounts.OAUTH_TOKEN_SECRET);
            api_url_format = cursor.getColumnIndex(Accounts.API_URL_FORMAT);
            same_oauth_signing_url = cursor.getColumnIndex(Accounts.SAME_OAUTH_SIGNING_URL);
        }

        @Override
        public String toString() {
            return "Indices{screen_name=" + screen_name + ", name=" + name + ", account_id=" + account_id
                    + ", profile_image_url=" + profile_image_url + ", profile_banner_url=" + profile_banner_url
                    + ", color=" + color + ", is_activated=" + is_activated + ", auth_type=" + auth_type
                    + ", consumer_key=" + consumer_key + ", consumer_secret=" + consumer_secret
                    + ", basic_auth_password=" + basic_auth_password + ", oauth_token=" + oauth_token
                    + ", oauth_token_secret=" + oauth_token_secret + ", api_url_format=" + api_url_format
                    + ", same_oauth_signing_url=" + same_oauth_signing_url + "}";
        }
    }
}
