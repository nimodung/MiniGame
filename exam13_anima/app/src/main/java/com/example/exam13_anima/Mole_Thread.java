package com.example.exam13_anima;

public class Mole_Thread extends Thread {

    public CatchMoleView m_Mole;

    public Mole_Thread(CatchMoleView mole) {
        m_Mole = mole;
    }
    final int mole_count = 10;
    @Override
    public void run() { //이미지 그릴 좌표, 이미지 변경 (어떤 그림을 그릴 것인지)
        // 5(가로) x 6(세로) 화면

        while(true) {
            if(m_Mole.m_state == m_Mole.DRAW_LEVEL) {
                m_Mole.postInvalidate();
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 20 + m_Mole.m_level * 10
            if(m_Mole.m_state != m_Mole.DRAW_GAMEOVER) {
                for (int i = 0; i < mole_count; i++) {
                    m_Mole.m_x = ((int) (Math.random() * 5)) * m_Mole.scr_width / 5; //0부터 1미만(0 ~ 0.999999...)
                    m_Mole.m_y = ((int) (Math.random() * 5 + 1)) * m_Mole.scr_height / 7; //0~5까지에 +1해서 1~6까지 //맨 위의 좌표는 제목때문에 안쓴다 (0번은 쓰지않고 1번부터)
                    m_Mole.m_state = m_Mole.DRAW_PRE; //pre
                    m_Mole.postInvalidate();
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    m_Mole.m_state = m_Mole.DRAW_MOLE; //mole
                    m_Mole.postInvalidate();
                    try {
                        Thread.sleep(800 - m_Mole.m_level * 100); // 500 400 300
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (m_Mole.m_state == m_Mole.DRAW_HURT) {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    m_Mole.m_state = m_Mole.DRAW_PRE; //pre
                    m_Mole.postInvalidate();
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (i == mole_count-1) {
                        if (m_Mole.m_catch_count >= 5) {
                            m_Mole.m_level++;
                            if (m_Mole.m_level == 4) m_Mole.m_level = 3;
                            m_Mole.m_state = m_Mole.DRAW_LEVEL;
                            m_Mole.m_catch_count = 0;
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            m_Mole.postInvalidate();
                        } else {
                            m_Mole.m_state = m_Mole.DRAW_GAMEOVER;
                            m_Mole.postInvalidate();
                            m_Mole.m_level = 1;
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }



        }
    }
}
