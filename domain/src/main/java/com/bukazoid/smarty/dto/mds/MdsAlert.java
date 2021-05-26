package com.bukazoid.smarty.dto.mds;

import lombok.Data;

@Data
public class MdsAlert {
	String message;
	String deliverySystem;
	/**
	 * depends on delivery system
	 * e-mail for email
	 * telegram nick for telegram
	 */
	String target;
}
