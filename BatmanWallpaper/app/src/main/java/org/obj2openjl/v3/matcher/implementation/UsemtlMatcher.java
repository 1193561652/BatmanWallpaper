package org.obj2openjl.v3.matcher.implementation;

import org.obj2openjl.v3.matcher.MatchHandler;

import java.util.regex.Pattern;

public class UsemtlMatcher extends MatchHandler<String> {
    private Pattern usemtlLinePattern = Pattern.compile("^usemtl .*$");
    @Override
    protected Pattern getPattern() {
        return usemtlLinePattern;
    }

    @Override
    protected void handleMatch(String group) {
        String[] sub = group.split(" ");
        if(sub.length == 2) {
            addMatch(sub[1]);
        }
    }
}
