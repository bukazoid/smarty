package com.bukazoid.smarty.dto.mds;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MdsParameterDefinition implements Serializable {
	String name;
	String description;
	boolean required = true;
}
