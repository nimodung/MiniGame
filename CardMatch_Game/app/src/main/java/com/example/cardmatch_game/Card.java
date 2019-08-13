package com.example.cardmatch_game;

public class Card { //c언어의 구조체 역할
    //처음 시작해서
    public static final int CARD_SHOW = 1;
    public static final int CARD_CLOSE = 2;

    //사용자가 터치해서 카드 오픈
    public static final int CARD_PLAYER_OPEN = 3;

    //MATCH 상태
    public static final int CARD_MATCH = 4;

    public static final int IMG_RED = 1;
    public static final int IMG_BLUE = 2;
    public static final int IMG_BLACK = 3;

    int m_State; //앞 뒷면의 상태
    int m_Image; //카드 종류

   Card(int _Image){ //생성자 //return 값이 없어야함
       m_State =  CARD_SHOW;//처음 시작에 카드 앞면 보여주기
       m_Image = _Image;
    }
}
