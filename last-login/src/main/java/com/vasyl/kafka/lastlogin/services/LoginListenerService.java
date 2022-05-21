package com.vasyl.kafka.lastlogin.services;

import com.vasyl.kafka.lastlogin.model.UserDetails;
import com.vasyl.kafka.lastlogin.model.UserLogin;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.function.BiConsumer;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LoginListenerService {

    @Bean
    public BiConsumer<KTable<String, UserDetails>, KTable<String, UserLogin>> user() {
        return (users, logins) ->
        {
            users.toStream().foreach((k, v) -> log.info("User Key: {}, Last Login: {}, Value: {}",
                    k, Instant.ofEpochMilli(v.getLastLogin()).atOffset(ZoneOffset.UTC), v));

            logins.toStream().foreach((k, v) -> log.info("Login Key: {}, Last Login: {}, Value: {}",
                    k, Instant.ofEpochMilli(v.getCreatedTime()).atOffset(ZoneOffset.UTC), v));

            logins.join(users, (l, u) -> {
                        u.setLastLogin(l.getCreatedTime());
                        return u;
                    }).toStream()
                    .foreach((k, v) -> log.info("Updated Last Login Key: {}, Last Login: {} ", k,
                            Instant.ofEpochMilli(v.getLastLogin()).atOffset(ZoneOffset.UTC)));
        };
    }
}
