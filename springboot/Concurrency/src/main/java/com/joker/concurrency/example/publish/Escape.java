package com.joker.concurrency.example.publish;

import com.joker.concurrency.annotaions.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by hunter on 2018/4/8.
 */
@Slf4j
@NotThreadSafe
public class Escape {
    private int thisCanBeEscape = 0;

    public Escape() {
        new InnerClass();
    }

    private class InnerClass {
        public InnerClass() {
            log.info("{}", Escape.this.thisCanBeEscape);
        }
    }

    public static void main(String[] args) {
        new Escape();
    }
}
