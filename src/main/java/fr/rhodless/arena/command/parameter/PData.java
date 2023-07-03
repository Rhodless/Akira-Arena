package fr.rhodless.arena.command.parameter;

import fr.rhodless.arena.command.annotations.Param;
import lombok.Getter;

/*
 * This file is part of Akira-UHC.
 *
 * Copyright © 2023, Rhodless. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
@Getter
public class PData {

    private final String name;
    private final boolean wildcard;
    private final String baseValue;
    private final boolean required;
    private final String[] tabComplete;
    private final Class<?> clazz;


    public PData(Param parameter, Class<?> clazz) {
        this.name = parameter.name();
        this.wildcard = parameter.wildcard();
        this.baseValue = parameter.baseValue();
        this.required = baseValue.equalsIgnoreCase("");
        this.tabComplete = parameter.tabCompleteFlags();
        this.clazz = clazz;
    }
}
