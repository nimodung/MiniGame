package com.example.exam13_anima;

public class CardGameThread extends Thread {
    CardGameView m_View;
    CardGameThread(CardGameView view)  //생성자
    {
        super();
        m_View = view;
    }

    //쓰레드에 반드시 있어야 하는 함수
    //쓰레드가 start 되면 실행되는 함수
    @Override
    public void run() {
        while(true) //checkMatch 함수를 무한 호출
        {
            m_View.CheckMatch();
        }
    }
}
