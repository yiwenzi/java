package com.joker.util.callback;

/**
 * Created by hunter on 2017/10/27.
 */
public class Wang implements CallBack {
    private Li li;
    public Wang(Li li){
        this.li = li;
    }

    public void askQuestion(final String question){
        //这里用一个线程就是异步，
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 小王调用小李中的方法，在这里注册回调接口
                 */
                li.executeMessage(Wang.this, question);
            }
        }).start();

        //小网问完问题挂掉电话就去干其他的事情了，诳街去了
        play();
    }

    public void play() {
        System.out.println("I am playing");
    }
    @Override
    public void solve(String result) {
        System.out.println("The result is : " + result);
    }
}
