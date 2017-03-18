package com.github.hyacinth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface IContainer {
    Map getAttrsMap();

    Map getColumnsMap();

    Set getModifyFlagSet();

    static final IContainer defaultContainer = new IContainer() {

        public Map<String, Object> getAttrsMap() {
            return new HashMap<String, Object>();
        }

        public Map<String, Object> getColumnsMap() {
            return new HashMap<String, Object>();
        }

        public Set<String> getModifyFlagSet() {
            return new HashSet<String>();
        }
    };
}
