/*
 * Speaker.h
 *
 * Created: 2019-05-27 오전 10:02:31
 *  Author: tjoeun
 */ 


#ifndef SPEAKER_H_
#define SPEAKER_H_


#define DO_05 523
#define DO_05s 554
#define RE_05 587
#define RE_05s 622
#define MI_05 659
#define PA_05 698
#define PA_05s 740
#define SOL_05 784
#define SOL_05s 831
#define LA_05 880
#define LA_05s 932
#define SI_05 988
#define DO_06 1046

#define BEAT_2 2728
#define BEAT_3_2 2046
#define BEAT_1 1364 //1박자
#define BEAT_6_8 1080
#define BEAT_3_4 1023
#define BEAT_1_2 682 //1/2박자
#define BEAT_1_4 341
#define BEAT_2_8 340
#define BEAT_1_8 170
#define BEAT_3_8 540
#define BEAT_1_16 86
#define BEAT_1_32 42

#define DO_06 523
#define DO_06s 554
#define RE_06 587
#define RE_06s 622
#define MI_06 659
#define FA_06 698
#define FA_06s 740
#define SO_06 784
#define SO_06s 831
#define LA_06 880
#define LA_06s 932
#define SI_06 988
#define DO_07 1047
#define DO_07s 1109
#define RE_07 1175
#define RE_07s 1245
#define MI_07 1319
#define FA_07 1397
#define FA_07s 1480
#define SO_07 1568
#define SO_07s 1661
#define LA_07 1760
#define LA_07s 1865
#define SI_07 1976

void Music_player(int *tune, int *beat);
void delay_ms(int ms);
void beep(char repeat);
void siren(char repeat);
void doorOpen(void);
void doorClose(void);
void Keysound(void);



#endif /* SPEAKER_H_ */