package com.qi.demo;

import com.qi.demo.utils.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
public class UtilTest {
    @Test
    public void test() {
        String path  = "E:\\NERCMS相关\\软件后端\\xxxx\\";
        FileUtil.mkdirs(path);
    }
}
