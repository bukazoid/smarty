package com.bukazoid.smarty.ping;

import java.io.IOException;
import java.net.InetAddress;

import lombok.extern.slf4j.Slf4j;

/**
 * testing ping requires to have some ip which always accessible, since it is
 * hard to provide some object in every developers' network, i will use
 * personal(not junit) test
 * 
 * BTW: pings may work bad at linux, coz of user privileges and required action
 * to proceed, so, some tricky see seconds answer at
 * https://stackoverflow.com/questions/11506321/how-to-ping-an-ip-address
 * 
 * @author pavel
 *
 */
@Slf4j
public class IsReachable {

	static final int TIMEOUT = 500;

	public static void main(String[] args) throws IOException {
		log.info("router reachable: {}", isReachable("192.168.112.38"));

		log.info("yandex reachable: {}", isReachable("ya.ru"));
		
		log.info("TV reachable: {}", isReachable("192.168.112.91"));
		
		log.info("PC reachable: {}", isReachable("192.168.112.111"));
		
		log.info("PHONE reachable: {}", isReachable("192.168.112.79"));
		
		// add you device to check how it is detected
	}

	public static boolean isReachable(String target) {
		try {
			InetAddress object = InetAddress.getByName(target);
			return object.isReachable(TIMEOUT);
		} catch (Exception ex) {
			// not found means unreachable
			return false;
		}
	}

}
