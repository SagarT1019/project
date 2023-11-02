package com.project.project.entity;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class BlackListToken {

    private Set<String> blacklist = new HashSet<>();

    public void addToBlacklist(String token) {

        blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
