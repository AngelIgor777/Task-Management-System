package org.task.task_management_system.util;


import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtAlgorithm {

    public static Algorithm getRefreshAlgorithm() {
        return Algorithm.HMAC256(KeyUtil.getRefreshSecret().getBytes());
    }


    public static Algorithm getAccessAlgorithm() {
        return Algorithm.HMAC256(KeyUtil.getAccessSecret().getBytes());
    }

}
