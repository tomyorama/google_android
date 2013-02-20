package com.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class CommentsXmlParser {
	// We don't use namespaces
	private static final String ns = null;

	public List<Comment> parse(InputStream in) throws XmlPullParserException,
			IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}
	}

	private List<Comment> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		List entries = new ArrayList();

		parser.require(XmlPullParser.START_TAG, ns, "termin");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("comments")) {
				entries.add(readEntry(parser));
			} else {
				skip(parser);
			}
		}
		return entries;
	}

	public static class Comment {
		public final Long id;
		public final String user;
		public final String text;
		public final Long dateCreated;

		private Comment(String id, String user, String text, String dateCreated) {
			this.id = Long.parseLong(id);
			this.user = user;
			this.text = text;
			this.dateCreated = Long.parseLong(dateCreated);
		}
	}

	// Parses the contents of an entry. If it encounters a title, summary, or
	// link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the
	// tag.
	private Comment readEntry(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "comments");
		String id = null;
		String user = null;
		String text = null;
		String dateCreated = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("user")) {
				user = readUser(parser);
			} else if (name.equals("text")) {
				text = readTextX(parser);
			} else if (name.equals("DateCreated")) {
				dateCreated = readTag(parser, "DateCreated");
			} else if (name.equals("Id")) {
				id = readTag(parser, "Id");
			} else {
				skip(parser);
			}
		}
		return new Comment(id, user, text, dateCreated);
	}

	// Processes title tags in the feed.
	private String readUser(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "user");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "user");
		return title;
	}

	// Processes title tags in the feed.
	private String readTag(XmlPullParser parser, String tagName)
			throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, tagName);
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, tagName);
		return title;
	}

	// Processes title tags in the feed.
	private String readTextX(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "text");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "text");
		return title;
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}
