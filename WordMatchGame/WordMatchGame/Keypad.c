/*
 * Keypad.c
 *
 * Created: 2019-04-16 오후 12:06:32
 *  Author: Kim Hee Ram
 */ 

#include <avr/io.h>
#define F_CPU 16000000UL
#include <util/delay.h>
#include <stdio.h>

#include "Keypad.h"


void Keypad_init(void)
{
	Keypad_PORT_DDR |= 0b00001111; //포트D 상위 4bit 출력으로 설정
	Keypad_PORT |= 0b00001111; // 상위 4bit 끄고 시작(풀업저항)
	
	Keypad_PIN_DDR &= 0b11000011; //포트B 하위 4bit 입력으로 설정
	Keypad_PIN_PORT |= 0b00111100;
	//MCUCR Register : bit 4번 PUD : Pull-up Disable //내부적으로 풀업저항 사용 가능 //initial value : 0
	//DDRxn : 0 (입력 설정), PORTxn : 1, PUD : 0 => pull-up yes
	
	return;
}

char Keyscan_sub(void)
{
	if(!(Keypad_PIN & 0b00000100)) return 1; //키가 눌렸을때
	else if(!(Keypad_PIN & 0b00001000)) return 2;
	else if(!(Keypad_PIN & 0b00010000)) return 3;
	else if(!(Keypad_PIN & 0b00100000)) return 4;
	else return 0;
}

char Keyscan(void)
{
	for(int i = 0; i < 4; i++)
	{
		Keypad_PORT |= 0b00001111;
		Keypad_PORT &= ~(0b00001000 >> i); 
		_delay_us(1);
		if(Keyscan_sub()) return 'A' + Keyscan_sub() + (i*4);
	}
	
	return 'A'; //아무것도 눌리지 않았음
}

