package com.autentia.tutoriales.tweets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

public class TweetsReceiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(TweetsReceiver.class);

	public void receiveTweet(String rawTweet) {
		final Status tweet = parse(rawTweet);

		LOGGER.info(String.format("@%s : %s", tweet.getUser().getName(), tweet.getText()));
	}

	private Status parse(String rawJson) {
		try {
			return TwitterObjectFactory.createStatus(rawJson);
		} catch (TwitterException e) {
			LOGGER.warn("Invalid tweet" + rawJson, e);
			return null;
		}
	}
}