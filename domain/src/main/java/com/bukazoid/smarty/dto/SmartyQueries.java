package com.bukazoid.smarty.dto;

public class SmartyQueries {
	public final static String EXCHANGE_NAME = "smarty.msg.exchange";

	public final static String ROUTE_KEY_REQULAR = "smarty.msg.tele.regular";
	public final static String ROUTE_KEY_ALL = "smarty.msg.tele.*";
	public final static String ROUTE_KEY_ALL_ALL = "smarty.msg.tele.#";
	/**
	 * incoming data from sensors or external systems
	 */
	public final static String QUEUE_MESSAGE_PROCESSOR = "smarty.msg.message-processor";

	public final static String QUEUE_RANGE_CHECKER = "smarty.msg.range-checker";

	/**
	 * warning signal or message
	 */
	public final static String QUEUE_SIGNAL = "smarty.msg.signal";

	/*
	 * mds listen these two
	 */
	public final static String MDS_REGISTER = "smarty.mds.register";
	public final static String MDS_ALERT = "smarty.mds.alert";

	/**
	 * mds sends message there, {module} is a registered module to send messages
	 */
	public final static String MDS_SEND_MESSAGE = "smarty.mds.send.{module}";
	public final static String MDS_SEND_BASE = "smarty.mds.send.";
}
