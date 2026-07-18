package org.example.hrms.security;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final Map<String, Date> blacklist = new ConcurrentHashMap<>();

    public void blacklist(String token, Date expiry) {
        blacklist.put(token, expiry);
    }

    public boolean isBlacklisted(String token) {
        Date expiry = blacklist.get(token);
        if (expiry == null) {
            return false;
        }
        if (expiry.before(new Date())) {
            blacklist.remove(token);
            return false;
        }
        return true;
    }
}
