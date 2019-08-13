package com.example.exam13_anima;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

public class CatchMoleView extends View {

    public Bitmap Mole, Mole_pre, Mole_hurt, btmScore, btmGameOver, btmReplay;
    public Bitmap[] btmLevel;
    public Bitmap[] btmNumber;
    public MediaPlayer sndMole, sndCatch;
    Rect m_MoleRect;
    Rect m_ReplayRect;

    final int DRAW_LEVEL = 0, DRAW_MOLE = 1, DRAW_PRE = 2, DRAW_HURT = 3, DRAW_GAMEOVER = 4 ;

    int m_x, m_y;   // 두더지 이미지 그려질 좌표
    int m_state;    //두더지의 상태 (mole, pre, hurt)
    int scr_width, scr_height; //화면 가로폭, 세로폭
    int m_score, m_level = 1, m_catch_count;

    public Paint m_paint;

    public CatchMoleView(Context context) {
        super(context);

        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inSampleSize = 1;
        option.inPurgeable = true;
        option.inDither = true;

        Mole = BitmapFactory.decodeResource(getResources(), R.mipmap.mole2, option);
        Mole_pre = BitmapFactory.decodeResource(getResources(), R.mipmap.mole_pre2, option);
        Mole_hurt = BitmapFactory.decodeResource(getResources(), R.mipmap.mole_hurt2, option);

        btmScore = BitmapFactory.decodeResource(getResources(), R.mipmap.score, option);
        btmGameOver = BitmapFactory.decodeResource(getResources(), R.mipmap.gameover, option);
        btmReplay = BitmapFactory.decodeResource(getResources(), R.mipmap.replay, option);

     btmLevel = new Bitmap[] {
                    BitmapFactory.decodeResource(getResources(), R.mipmap.level1_1, option),
                    BitmapFactory.decodeResource(getResources(), R.mipmap.level2_2, option),
                    BitmapFactory.decodeResource(getResources(), R.mipmap.level3_3, option)
            };

        btmNumber = new Bitmap[] {
                BitmapFactory.decodeResource(getResources(), R.mipmap.num0, option),
                BitmapFactory.decodeResource(getResources(), R.mipmap.num1, option),
                BitmapFactory.decodeResource(getResources(), R.mipmap.num2, option),
                BitmapFactory.decodeResource(getResources(), R.mipmap.num3, option),
                BitmapFactory.decodeResource(getResources(), R.mipmap.num4, option),
                BitmapFactory.decodeResource(getResources(), R.mipmap.num5, option),
                BitmapFactory.decodeResource(getResources(), R.mipmap.num6, option),
                BitmapFactory.decodeResource(getResources(), R.mipmap.num7, option),
                BitmapFactory.decodeResource(getResources(), R.mipmap.num8, option),
                BitmapFactory.decodeResource(getResources(), R.mipmap.num9, option)
        };


        m_MoleRect = new Rect(0,0,0,0);
        m_ReplayRect = new Rect(0, 0, 0, 0);

        sndMole = MediaPlayer.create(context, R.raw.mole);
        sndCatch = MediaPlayer.create(context, R.raw.catch_mole);

       /* m_paint = new Paint();
        m_paint.setTextSize(50);
        m_paint.setColor(Color.BLACK);*/

        Mole_Thread _thread = new Mole_Thread(this);
        _thread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setBackgroundResource(R.mipmap.mole_background);
        scr_width = this.getWidth() * 9 / 10;
        scr_height = this.getHeight() * 9 / 10;

        if(m_state == DRAW_LEVEL) { //레벨 이미지 띄워주기
            Rect rect = new Rect(0, 0, 0, 0);

            rect.set((this.getWidth() / 5), //20% 지점 (이미지 60%)
                    (this.getHeight() - (this.getWidth() * 6 / 10 * 2 / 7)) / 2, //(this.getWidth() * 6 / 10) * 2 / 7) : 이미지의 높이
                    this.getWidth() * 4/ 5,
                    (this.getHeight() - (this.getWidth() * 6 / 10 * 2 / 7))/2 + (this.getWidth() * 6 / 10 * 2 / 7)); //top + 이미지의 높이
            canvas.drawBitmap(btmLevel[m_level - 1],null, rect, null);
        }
        else if(m_state == DRAW_GAMEOVER) {
            Rect rect = new Rect(0, 0, 0, 0);

            rect.set(scr_width/5 , //20% 지점 (이미지 60%)
                    scr_height/7 + this.getHeight() / 10,
                    scr_width * 4 / 5 + this.getWidth() / 10,
                    scr_height * 4 / 7 + this.getHeight() / 10);
            canvas.drawBitmap(btmGameOver,null, rect, null);

            m_ReplayRect.set(scr_width * 2 / 5,
                    scr_height * 4 / 7 + this.getHeight() / 10,
                    scr_width * 3 / 5 + this.getWidth() / 10,
                    scr_height * 5 / 7 + this.getHeight() / 10);
            canvas.drawBitmap(btmReplay,null, m_ReplayRect, null);
        }
        else {
            //시간과 관계된 일은 thread 사용하는게 좋다
            m_MoleRect.set(m_x + (scr_width /20),
                    m_y + (scr_height / 20),
                    m_x + (scr_width /20) + (scr_width/5) ,
                    m_y + (scr_height / 20) + (scr_height/7));
            if(m_state == DRAW_PRE) {  //pre
                canvas.drawBitmap(Mole_pre,null, m_MoleRect, null);
            }
            else if(m_state == DRAW_MOLE) { //mole
                canvas.drawBitmap(Mole,null, m_MoleRect, null);
                sndMole.start();
            }
            else if(m_state == DRAW_HURT) { //hurt
                canvas.drawBitmap(Mole_hurt,null, m_MoleRect, null);
                sndCatch.start();
            }
        }


        Rect rect = new Rect(0,0,0,0);
        rect.set(scr_width - 255,  scr_height -30, scr_width - 95, scr_height + 20);
        canvas.drawBitmap(btmScore,null, rect, null);


        rect.set(scr_width - 105, scr_height - 20, scr_width - 65, scr_height + 20);
        if(m_score >= 1000) canvas.drawBitmap(btmNumber[m_score / 1000 % 10], null, rect, null);

        rect.set(scr_width - 75, scr_height - 20, scr_width - 35, scr_height + 20);
        if(m_score >= 100) canvas.drawBitmap(btmNumber[m_score / 100 % 10], null, rect, null);

        rect.set(scr_width - 45, scr_height -20, scr_width - 5, scr_height + 20);
        if(m_score >= 10) canvas.drawBitmap(btmNumber[m_score / 10 % 10], null, rect, null);

        rect.set(scr_width - 15, scr_height -20, scr_width + 25, scr_height + 20);
        canvas.drawBitmap(btmNumber[m_score % 10], null, rect, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x, y; //좌표 변수

        x = (int)event.getX();
        y = (int)event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(m_MoleRect.contains(x,y) && m_state == DRAW_MOLE) { //터치 좌표가 RECT 안에 포함 되어있고 두더지가 나와있는 상태 일때
                m_score += 10;
                m_catch_count++;
                m_state = DRAW_HURT;
                invalidate();
            }

            if( m_ReplayRect.contains(x, y) && m_state == DRAW_GAMEOVER) {
                m_level = 1;
                m_state = DRAW_LEVEL;
                m_score = 0;
                m_catch_count = 0;


                invalidate();
            }
        }


        return super.onTouchEvent(event);
    }
}
