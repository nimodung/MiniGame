package com.example.cardmatch_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class CardGameView extends View {

    Bitmap m_CardBackside;
    Bitmap m_ImageRed, m_ImageBlue, m_ImageBlack;
    Bitmap m_MyImg_level1[], m_MyImg_level2[];
    int[] level1_id = {R.mipmap.level1_1, R.mipmap.level1_2, R.mipmap.level1_3, R.mipmap.level1_4,
            R.mipmap.level1_5, R.mipmap.level1_6, R.mipmap.level1_7, R.mipmap.level1_8};
    int[] level2_id = {R.mipmap.level2_1, R.mipmap.level2_2, R.mipmap.level2_3, R.mipmap.level2_4,
            R.mipmap.level2_5, R.mipmap.level2_6, R.mipmap.level2_7, R.mipmap.level2_8};
    Rect box[][];

    Card m_Card[][];
    Card_Level1 m_Card_Level1[][];
    Card_Level2 m_Card_Level2[][];

    Card m_SeclectedCard1 = null, m_SeclectedCard2 = null;
    Card m_shuffle_Card1 = null, m_shuffle_Card2 = null;
    int card_width, offset_x, card_height, offset_y;
    int row, colum;

    //게임 진행 상태 //준비 , 게임 중
    static final int STATE_READY = 0;
    static final int STATE_START = 1;
    static final int STATE_END = 2;

    int game_state;
    int level = 0;


    public CardGameView(Context context) { //생성자 : 초기화 하는 역할 //리턴 타입 없음
        super(context);

        //카드 뒷면 비트맵 이미지
        m_CardBackside = BitmapFactory.decodeResource(getResources(), R.mipmap.backside); //참조 변수, 이미지의 주소를 가져다 넣는 것

        //카드 앞면 비트맵 이미지
        m_ImageRed = BitmapFactory.decodeResource(getResources(), R.mipmap.front_red);
        m_ImageBlue = BitmapFactory.decodeResource(getResources(), R.mipmap.front_blue);
        m_ImageBlack = BitmapFactory.decodeResource(getResources(), R.mipmap.front_black);
        // m_CardBackside = Bitmap.createScaledBitmap(m_CardBackside, 100, 150, true);
        m_MyImg_level1 = new Bitmap[8];
        m_MyImg_level2 = new Bitmap[8];
       for(int i = 0; i < 8; i++)
        {
            m_MyImg_level1[i] = BitmapFactory.decodeResource(getResources(), level1_id[i]);
            m_MyImg_level2[i] = BitmapFactory.decodeResource(getResources(), level2_id[i]);
        }

        //m_MyCat[7] = BitmapFactory.decodeResource(getResources(), mycat_id[7]);


        row = 2;
        colum = 3;

        Card_Shuffle();

        CardGameThread _thread = new CardGameThread(this); //THREAD 사용하기 위한 선언
        _thread.start(); // thread 안에 있는 run  함수 호출 // 직접 run 함수 호출하면 안된다. //계속해서 match 함수 호출


        /*
            for(int x = 0 ; x < 3; x++)
            {
                box[0][x] = new Rect(offset_x * (x+1) + (card_width * x), offset_y, (offset_x * (x+1)) + (card_width * (x+1)), offset_y + card_height);
            }
            //box[0][0] = new Rect(offset_x,                             offset_y,   offset_x + card_width,offset_y   + card_height);
           // box[0][1] = new Rect((offset_x * 2) + card_width,          offset_y,   (offset_x * 2) + (card_width * 2),offset_y + card_height);
            //box[0][2] = new Rect((offset_x * 3) + (card_width * 2),      offset_y, (offset_x * 3) + (card_width * 3),offset_y + card_height);
            for(int y = 0; y < 3; y++)
            {
                box[1][y] = new Rect((offset_x * (y+1) + card_width * y), (offset_y * 2) + card_height, (offset_x * (y+1)) + (card_width * (y+1)), (offset_y * 2) + (card_height * 2));
            }
            //box[1][0] = new Rect(offset_x,                   (offset_y * 2) + card_height,      offset_x + card_width,           (offset_y * 2) + (card_height * 2));
            //box[1][1] = new Rect((offset_x * 2) + card_width, (offset_y * 2) + card_height,      (offset_x * 2) + (card_width * 2),(offset_y * 2) + (card_height * 2));
            //box[1][2] = new Rect((offset_x * 3) + (card_width * 2), (offset_y * 2) + card_height, (offset_x * 3) + (card_width * 3),(offset_y * 2) + (card_height * 2));
        */

    }

    void Card_Shuffle() {
        int x, y, temp;

        box = new Rect[row][colum];
        for (y = 0; y < row; y++) {
            for (x = 0; x < colum; x++) {
                box[y][x] = new Rect(0, 0, 0, 0);
                // m_Card[y][x].m_Image = x+1;
            }
        }

        m_Card = new Card[2][3]; //배열을 만든것
        m_Card_Level1 = new Card_Level1[4][4];
        m_Card_Level2 = new Card_Level2[4][4];

        for (y = 0; y < row; y++) {
            for ( x = 0; x < colum; x++) {
                if(level == 0 )
                     m_Card[y][x] = new Card(((x+y*colum) / 2 + 1 )); // 카드를 만들어주는것
                else if(level == 1){
                    m_Card_Level1[y][x] = new Card_Level1(((x+y*colum) / 2 + 1 + 3 + (8 * (level - 1))));
                }
                else if(level == 2){
                    m_Card_Level2[y][x] = new Card_Level2(((x+y*colum) / 2 + 1 + 3 + (8 * (level - 1))));
                }
                // m_Card[y][x].m_Image = x+1;
            }
        }

        for (int i = 0; i < 100; i++) {  //random : 0 이상 1 이하의 값
            x = (int) (Math.random() * colum);
            y = (int) (Math.random() * row);
            if(level == 0)
            {
                m_shuffle_Card1 = m_Card[y][x]; //6장 카드중에 랜덤하게 한장
            }
            else if(level == 1){
                m_shuffle_Card1 = m_Card_Level1[y][x];
            }
            else if(level == 2){
                m_shuffle_Card1 = m_Card_Level2[y][x];
            }

            x = (int) (Math.random() * colum);
            y = (int) (Math.random() * row);
            if(level == 0)
            {
                m_shuffle_Card2 = m_Card[y][x]; //6장 카드중에 랜덤하게 한장
            }
            else if(level == 1) {
                m_shuffle_Card2 = m_Card_Level1[y][x];
            }
            else if(level == 2) {
                m_shuffle_Card2 = m_Card_Level2[y][x];
            }

            temp = m_shuffle_Card1.m_Image;
            m_shuffle_Card1.m_Image = m_shuffle_Card2.m_Image;
            m_shuffle_Card2.m_Image = temp;

            m_shuffle_Card1 = null;
            m_shuffle_Card2 = null;
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        setBackgroundResource(R.mipmap.background);

        //화면에 대한 비율로 위치 설정 //1 : 1.6 비율의 이미지 사용
        card_width = this.getWidth() * 4 / 5 / colum * 4 / 5;
        offset_x = this.getWidth() / 5 / (colum + 1);

        card_height = (int)(card_width * 1.6);
        offset_y = (this.getHeight() * 4 / 5) / 5 / (row + 1);
        //offset_height : 10%


        // 카드 위치 설정
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < colum; x++) {
                box[y][x].set(
                        (offset_x * (x + 1)) + (card_width * x) + (this.getWidth() /10),
                        (offset_y * (y + 1)) + (card_height * y) + (this.getHeight() / 5),
                        (offset_x * (x + 1)) + (card_width * (x + 1)) + (this.getWidth() /10),
                        (offset_y * (y + 1)) + (card_height * (y + 1)) + (this.getHeight() / 5)
                );
            }
        }

        //상태에 따라 전체 앞면 or 전체 뒷면 카드 그려주기
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < colum; x++) {

                if(level == 0)
                {
                    if(m_Card[y][x].m_State == Card.CARD_CLOSE)
                    {
                        canvas.drawBitmap(m_CardBackside, null, box[y][x], null);
                    }
                    else //전체 카드 보여주기 || 플레이어가 선택한 카드 || 카드 매치 상태이면 앞면 그려주기
                    {
                        switch (m_Card[y][x].m_Image) {
                                case Card.IMG_RED:
                                    canvas.drawBitmap(m_ImageRed, null, box[y][x], null);
                                    break;
                                case Card.IMG_BLUE:
                                    canvas.drawBitmap(m_ImageBlue, null, box[y][x], null);
                                    break;
                                case Card.IMG_BLACK:
                                    canvas.drawBitmap(m_ImageBlack, null, box[y][x], null);
                                    break;
                        }
                    }
                }

                if(level == 1)
                {
                    if(m_Card_Level1[y][x].m_State == Card_Level1.CARD_CLOSE)
                    {
                        canvas.drawBitmap(m_CardBackside, null, box[y][x], null);
                    }
                    else //전체 카드 보여주기 || 플레이어가 선택한 카드 || 카드 매치 상태이면 앞면 그려주기
                    {
                        switch (m_Card_Level1[y][x].m_Image) {
                            case Card_Level1.IMG_LEVEL1_1:
                                canvas.drawBitmap(m_MyImg_level1[0], null, box[y][x], null);
                                break;
                            case Card_Level1.IMG_LEVEL1_2:
                                canvas.drawBitmap(m_MyImg_level1[1], null, box[y][x], null);
                                break;
                            case Card_Level1.IMG_LEVEL1_3:
                                canvas.drawBitmap(m_MyImg_level1[2], null, box[y][x], null);
                                break;
                            case Card_Level1.IMG_LEVEL1_4:
                                canvas.drawBitmap(m_MyImg_level1[3], null, box[y][x], null);
                                break;
                            case Card_Level1.IMG_LEVEL1_5:
                                canvas.drawBitmap(m_MyImg_level1[4], null, box[y][x], null);
                                break;
                            case Card_Level1.IMG_LEVEL1_6:
                                canvas.drawBitmap(m_MyImg_level1[5], null, box[y][x], null);
                                break;
                            case Card_Level1.IMG_LEVEL1_7:
                                canvas.drawBitmap(m_MyImg_level1[6], null, box[y][x], null);
                                break;
                            case Card_Level1.IMG_LEVEL1_8:
                                canvas.drawBitmap(m_MyImg_level1[7], null, box[y][x], null);
                                break;

                        }
                    }
                }

                if(level == 2)
                {
                    if(m_Card_Level2[y][x].m_State == Card_Level2.CARD_CLOSE)
                    {
                        canvas.drawBitmap(m_CardBackside, null, box[y][x], null);
                    }
                    else //전체 카드 보여주기 || 플레이어가 선택한 카드 || 카드 매치 상태이면 앞면 그려주기
                    {
                        switch (m_Card_Level2[y][x].m_Image) {
                            case Card_Level2.IMG_LEVEL2_1:
                                canvas.drawBitmap(m_MyImg_level2[0], null, box[y][x], null);
                                break;
                            case Card_Level2.IMG_LEVEL2_2:
                                canvas.drawBitmap(m_MyImg_level2[1], null, box[y][x], null);
                                break;
                            case Card_Level2.IMG_LEVEL2_3:
                                canvas.drawBitmap(m_MyImg_level2[2], null, box[y][x], null);
                                break;
                            case Card_Level2.IMG_LEVEL2_4:
                                canvas.drawBitmap(m_MyImg_level2[3], null, box[y][x], null);
                                break;
                            case Card_Level2.IMG_LEVEL2_5:
                                canvas.drawBitmap(m_MyImg_level2[4], null, box[y][x], null);
                                break;
                            case Card_Level2.IMG_LEVEL2_6:
                                canvas.drawBitmap(m_MyImg_level2[5], null, box[y][x], null);
                                break;
                            case Card_Level2.IMG_LEVEL2_7:
                                canvas.drawBitmap(m_MyImg_level2[6], null, box[y][x], null);
                                break;
                            case Card_Level2.IMG_LEVEL2_8:
                                canvas.drawBitmap(m_MyImg_level2[7], null, box[y][x], null);
                                break;
                        }
                    }
                }
              }
        }



       /*
            int offset_card_x = (this.getWidth() - (m_CardBackside.getWidth() * 3)) / 4; //카드 사이의 가로 폭 간격
            int offset_card_y = (this.getHeight() - 150 - (m_CardBackside.getHeight() * 2)) / 3; // 세로 폭 간격 //title 부분 150 빼준다
            int card_width = m_CardBackside.getWidth();
            int card_height = m_CardBackside.getHeight();
       */
       /*
           canvas.drawBitmap(m_CardBackside, offset_card_x, 150 + offset_card_y, null);
            canvas.drawBitmap(m_CardBackside, (offset_card_x * 2) + card_width, 150 + offset_card_y, null);
            canvas.drawBitmap(m_CardBackside, (offset_card_x * 3) + (card_width * 2), 150 + offset_card_y, null);
            canvas.drawBitmap(m_CardBackside, offset_card_x, 150 + (offset_card_y *2) + card_height, null);
            canvas.drawBitmap(m_CardBackside, (offset_card_x * 2) + card_width, 150 + (offset_card_y *2) + card_height, null);
            canvas.drawBitmap(m_CardBackside, (offset_card_x * 3) + (card_width * 2), 150 + (offset_card_y *2) + card_height, null);
       */

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int px = (int)event.getX();
        int py = (int)event.getY();

        if(game_state == STATE_READY) {
            game_state = STATE_START;
            for (int y = 0; y < row; y++) {
                for (int x = 0; x < colum; x++) {
                    if(level == 0) m_Card[y][x].m_State = Card.CARD_CLOSE;
                    else if(level == 1) m_Card_Level1[y][x].m_State = Card_Level1.CARD_CLOSE;
                    else if(level == 2) m_Card_Level2[y][x].m_State = Card_Level2.CARD_CLOSE;
                }
            }
        }
        else if(game_state == STATE_START)
        { //카드를 터치 했는지 안했는지를 봐야함
            for (int y = 0; y < row; y++) {
                for (int x = 0; x < colum; x++) {
                    if(box[y][x].contains(px,py)) //포함되어 있으면 TRUE, 안되어있으면 FALSE 리턴
                    {
                        if(level == 0) {
                            if(m_Card[y][x].m_State == Card.CARD_CLOSE)
                            {
                                if(m_SeclectedCard1 == null)
                                {
                                    m_SeclectedCard1 = m_Card[y][x];
                                    m_Card[y][x].m_State = Card.CARD_PLAYER_OPEN;
                                }
                                else if(m_SeclectedCard2 == null)
                                {
                                    m_SeclectedCard2 = m_Card[y][x];
                                    m_Card[y][x].m_State = Card.CARD_PLAYER_OPEN;
                                }
                            }
                        }
                        if(level == 1) {
                            if(m_Card_Level1[y][x].m_State == Card_Level1.CARD_CLOSE)
                            {
                                if(m_SeclectedCard1 == null)
                                {
                                    m_SeclectedCard1 = m_Card_Level1[y][x];
                                    m_Card_Level1[y][x].m_State = Card.CARD_PLAYER_OPEN;
                                }
                                else if(m_SeclectedCard2 == null)
                                {
                                    m_SeclectedCard2 = m_Card_Level1[y][x];
                                    m_Card_Level1[y][x].m_State = Card.CARD_PLAYER_OPEN;
                                }
                            }
                        }
                        if(level == 2) {
                            if(m_Card_Level2[y][x].m_State == Card_Level2.CARD_CLOSE)
                            {
                                if(m_SeclectedCard1 == null)
                                {
                                    m_SeclectedCard1 = m_Card_Level2[y][x];
                                    m_Card_Level2[y][x].m_State = Card.CARD_PLAYER_OPEN;
                                }
                                else if(m_SeclectedCard2 == null)
                                {
                                    m_SeclectedCard2 = m_Card_Level2[y][x];
                                    m_Card_Level2[y][x].m_State = Card.CARD_PLAYER_OPEN;
                                }
                            }
                        }

                    }
                }
            }
        }
        else if(game_state == STATE_END) //다음 레벨 위한 초기화
        {
            row = 4;
            colum = 4;

            if(level == 1) level = 2;
            else { //level == 0 || level == 2
                level = 1;
            }


            Card_Shuffle();
            game_state = STATE_READY;
        }

        invalidate(); //화면 다시 그리기 //안드로이드에 onDraw 호출 요청 // onDraw 함수 호출 X

        return super.onTouchEvent(event); //터치를 연속적으로 계속 받기 true
    }

    void CheckMatch() //플레이어가 선택한 두장의 카드가 맞는지 확인
    {
        if(m_SeclectedCard1 != null && m_SeclectedCard2 != null)
        {
            if(m_SeclectedCard1.m_Image == m_SeclectedCard2.m_Image) //카드 짝이 맞을때
            {

                m_SeclectedCard1.m_State = Card.CARD_MATCH;
                m_SeclectedCard2.m_State = Card.CARD_MATCH;

                m_SeclectedCard1 = null;
                m_SeclectedCard2 = null;

                for(int y = 0; y < row; y++)
                {
                    for(int x = 0; x < colum; x++)
                    {
                        if(level == 0) {
                            if (m_Card[y][x].m_State != Card.CARD_MATCH) {
                                postInvalidate();
                                return;
                            }
                        }
                        else if(level == 1){
                           if(m_Card_Level1[y][x].m_State != Card_Level1.CARD_MATCH) {
                               postInvalidate();
                               return;
                           }
                        }
                        else if(level == 2){
                            if(m_Card_Level2[y][x].m_State != Card_Level2.CARD_MATCH) {
                                postInvalidate();
                                return;
                            }
                        }
                    }
                }
                game_state = STATE_END;
            }
            else //카드 짝이 안맞을때
            {
                try {
                    Thread.sleep(500);//화면을 잠깐 보여주기 //다른 일 하면서 나만 잠깐 멈춘다
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                m_SeclectedCard1.m_State = Card.CARD_CLOSE;
                m_SeclectedCard2.m_State = Card.CARD_CLOSE;

                m_SeclectedCard1 = null;
                m_SeclectedCard2 = null;
            }
            postInvalidate(); //하던거 마저 다하고 화면 그려라
        }

    }
}
