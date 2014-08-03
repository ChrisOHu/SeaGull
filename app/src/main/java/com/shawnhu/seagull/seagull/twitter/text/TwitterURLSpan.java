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

package com.shawnhu.seagull.seagull.twitter.text;

import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

public class TwitterURLSpan extends URLSpan {

	public static final int VALUE_LINK_HIGHLIGHT_OPTION_CODE_NONE = 0x0;
	public static final int VALUE_LINK_HIGHLIGHT_OPTION_CODE_HIGHLIGHT = 0x1;
	public static final int VALUE_LINK_HIGHLIGHT_OPTION_CODE_UNDERLINE = 0x2;
	public static final int VALUE_LINK_HIGHLIGHT_OPTION_CODE_BOTH =
            VALUE_LINK_HIGHLIGHT_OPTION_CODE_HIGHLIGHT | VALUE_LINK_HIGHLIGHT_OPTION_CODE_UNDERLINE;

    private final int type, highlightStyle, highlightColor;
	private final long accountId;
	private final String url, orig;
	private final boolean sensitive;
	private final OnLinkClickListener listener;

	public TwitterURLSpan(final String url, final long accountId, final int type, final boolean sensitive,
                          final OnLinkClickListener listener, final int highlightStyle, final int highlightColor) {
		this(url, null, accountId, type, sensitive, listener, highlightStyle, highlightColor);
	}

	public TwitterURLSpan(final String url, final String orig, final long accountId, final int type,
                          final boolean sensitive, final OnLinkClickListener listener, final int highlightStyle,
                          final int highlightColor) {
		super(url);
		this.url = url;
		this.orig = orig;
		this.accountId = accountId;
		this.type = type;
		this.sensitive = sensitive;
		this.listener = listener;
		this.highlightStyle = highlightStyle;
		this.highlightColor = highlightColor;
	}

	@Override
	public void onClick(final View widget) {
		if (listener != null) {
			listener.onLinkClick(url, orig, accountId, type, sensitive);
		}
	}

	@Override
	public void updateDrawState(final TextPaint ds) {
		if ((highlightStyle & VALUE_LINK_HIGHLIGHT_OPTION_CODE_UNDERLINE) != 0) {
			ds.setUnderlineText(true);
		}
		if ((highlightStyle & VALUE_LINK_HIGHLIGHT_OPTION_CODE_HIGHLIGHT) != 0) {
			ds.setColor(highlightColor != 0 ? highlightColor : ds.linkColor);
		}
	}

    public interface OnLinkClickListener {
		public void onLinkClick(String link, String orig, long account_id, int type, boolean sensitive);
	}
}
