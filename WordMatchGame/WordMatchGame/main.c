/*
 * WordMatchGame.c
 *
 * Created: 2019-05-27 오전 9:59:40
 * Author : Kim Hee Ram
 */ 

#include <avr/io.h>
#define F_CPU 16000000UL
#include <util/delay.h>
#include <avr/interrupt.h>
#include <string.h>
#include <math.h>

#include "FND4digit.h"
#include "Keypad.h"
#include "Speaker.h"
#include "Timer.h"

#define NOT_MATCH 0
#define MATCH 1
#define IS_NULL 5

#define READY 0
#define GAME 1
#define END 2

void Card_shuffle(void);
void Ready_process(char key_value);
void Game_process(char key_value);
void End_process(char key_value);
void onDraw(void);
void print_FND_word(char *word);

extern char FND4digit_font_alphabet[26], FND[4];
extern volatile char start_flag, speakout_flag, next_beat_flag, time_print_flag,FND_reflash_flag, time_reset_flag;
extern volatile int msec, music_i,show_time;
extern volatile char i, sec, min, hour;
extern volatile int beat, interval;

char word[] = {'C', 'A', 'L', 'L'};
char *words[] = {"COLD", "NINE", "STOP", "BOOK", "COOL", "BEAR", "MATH", "FINE"};
char start_info[] = "    PAIR GAME    ";

char *word_card[4][4];
char state_card[4][4];
char x,y, first_x = 5, first_y;
char game_state = READY;

int main(void)
{
	int i = 0;
	char long_key_flag = 0;
    //초기화 순서도 중요
	Timer0_init(); //fnd 출력
	Timer1_init_CTC_outA(); //speaker
	FND4digit_init_shiftR(); //fnd 초기화
	Keypad_init();
	sei();
	
	
    while (1) 
    {
		if(FND_reflash_flag) //FND 출력 갱신 //1초에 한번씩 ISR에서 갱신
		{
			FND_reflash_flag = 0;
			onDraw();
		}		
		
		if(long_key_flag)            
		{
			if(Keyscan() != 'A')
			{
				_delay_us(200);
				if (Keyscan() != 'A')
				{
					if(game_state == READY) Ready_process(Keyscan());
					else if(game_state == GAME) Game_process(Keyscan()); 
					else if(game_state == END) End_process(Keyscan()); 
					long_key_flag = 0;
				}                                                  
			}
		}
		else
		{
			if(Keyscan() == 'A')
			{
				long_key_flag = 1;
			}
		}
		_delay_ms(10);
    }
}

void Card_shuffle(void){
	
	char random_x1,random_x2,random_y1,random_y2;
	char *temp_card;
	for(y = 0; y < 4; y++) {
		for(x = 0; x < 4; x++) {
			word_card[y][x] = words[(x + y * 4)/2];
			state_card[y][x] = NOT_MATCH;
		}
	}
	srandom(msec);
	for(int i = 0; i<100; i++)
	{
		random_x1 = random()%4;
		random_y1 = random()%4;
		random_x2 = random()%4;
		random_y2 = random()%4;
		temp_card = word_card[random_y1][random_x1];
		word_card[random_y1][random_x1] = word_card[random_y2][random_x2];
		word_card[random_y2][random_x2] = temp_card;
	}
	return;
}

void Ready_process(char key_value)
{
	Card_shuffle();
	game_state = GAME;
	time_reset_flag = 1;
	time_print_flag = 1;
	FND_reflash_flag = 1;
	Keysound();
	return;
}

void Game_process(char key_value) {
	
	//key_value를 이용해서 키패드 배열의 위치값 찾기
	y = (key_value - 'A' - 1)/4;
	x = (key_value - 'A' - 1)%4;
	
	if(state_card[y][x] == MATCH || ((first_y == y) && (first_x == x)))
	{ //카드 상태가 MATCH 이거나 직전에 선택한 카드와 같은 카드(짝 카드X, 진짜 그 카드)를 선택했을 때
		return;
	}
	
	print_FND_word(word_card[y][x]);
	
	if(first_x == IS_NULL) {
		first_x = x; first_y = y; time_print_flag = 0;
		Keysound();
	}
	else { //first_x == NOT_NULL
		if(!strcmp(word_card[first_y][first_x],word_card[y][x])) {
			state_card[first_y][first_x] = MATCH;
			state_card[y][x] = MATCH;
			doorOpen();
			
			for(y = 0; y < 4; y++) {
				for(x = 0; x < 4; x++) {
					if(state_card[y][x] != MATCH) {
						FND_reflash_flag = 1;
						show_time = 500;
						time_print_flag = 1;
						first_x= IS_NULL;
						return;
					}
				}
			}
			
			game_state = END;
			FND_clock(sec, min);
			//time_print_flag = 0;
			speakout_flag = 1;
			//next_beat_flag = 1;
		}
		else { //state_card == NOT_MATCH
			doorClose();
			
		}
		FND_reflash_flag = 1;
		show_time = 500;
		time_print_flag = 1;
		first_x= IS_NULL;
	}
	return;
}

void End_process(char key_value)
{
	game_state = READY;
	speakout_flag = 0;
	music_i = 0;
	interval = 0;
	next_beat_flag = 0;
	OCR1A = 0;
	FND_reflash_flag = 1;
	return;
}

void onDraw(void) {
	
	static int i = 0;
	
	if(game_state == READY) {
		FND_reflash_flag = 0;
		print_FND_word(start_info + i );
		i++;
		if(i>=13)i=0;
	}
	
	else if(game_state == GAME) {
		i = 0;
		if(time_print_flag && !show_time) {
			FND_clock(sec, min);
		}
	}
	
	return;
}

void print_FND_word(char *word) {
	
	for(int i = 0; i < 4; i++) {
		if('A' <=*(word+i) && *(word+i) <= 'Z')
			FND[3-i] = FND4digit_font_alphabet[*(word+i)- 'A'];
		else FND[3-i] = 0xff;
	}
	
	return;
}

