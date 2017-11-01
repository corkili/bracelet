package org.bracelet.service.impl;

import org.bracelet.service.TestService;
import org.springframework.stereotype.Service;

/**
 * Created by 李浩然
 * On 2017/8/7.
 */
@Service
public class TestServiceImpl implements TestService {
    public String test() {
        return "test";
    }
}
