package com.bukazoid.smarty.dto.mds;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class MdsRegister implements Serializable {
	String name;

	/**
	 * how to use it
	 */
	String description;

	/**
	 * if required for telegram it could be link to telegram bot 
	 */
	String link;

	Set<MdsParameterDefinition> params = new HashSet<>();
}
