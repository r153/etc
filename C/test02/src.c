/*
* test02 
*
*/

#include <stdio.h>
#include <stdlib.h>

typedef struct{
	int fs;
	int bits;
	int length;
	double *s;
} MONO_PCM;

void mono_wave_read(MONO_PCM*pcm,char*file_name)
{
	FILE*fp;
	int	 n;
	char riff_chunk_ID[4];
	long riff_chunk_size;
	char riff_form_type[4];
	char fmt_chunk_ID[4];
	long fmt_chunk_size;
	short fmt_wave_format_type;
	short fmt_channel;
	long fmt_samples_per_sec;
	long fmt_bytes_per_sec;
	short fmt_block_size;
	short fmt_bits_per_sample;
	char data_chunk_ID[4];
	long data_chunk_size;
	short data;

	fp=fopen(file_name,"rb");

	fread(riff_chunk_ID,1,4,fp);
	fread(&riff_chunk_size,4,1,fp);
	fread(riff_form_type,1,4,fp);
	fread(fmt_chunk_ID,1,4,fp);
	fread(&fmt_chunk_size,4,1,fp);
	fread(&fmt_wave_format_type,2,1,fp);
	fread(&fmt_channel,2,1,fp);
	fread(&fmt_samples_per_sec,4,1,fp);
	fread(&fmt_bytes_per_sec,4,1,fp);
	fread(&fmt_block_size,2,1,fp);
	fread(&fmt_bits_per_sample,2,1,fp);
	fread(data_chunk_ID,1,4,fp);
	fread(&data_chunk_size,4,1,fp);
	
	pcm->fs=fmt_samples_per_sec;
	pcm->bits=fmt_bits_per_sample;
	pcm->length=data_chunk_size/2;
	pcm->s=calloc(pcm->length,sizeof(double));

	for(n=0;n<pcm->length;n++){
		fread(&data,2,1,fp);
		pcm->s[n]=(double)data / 32768.0;
	}
	fclose(fp);

}

void mono_wave_write(MONO_PCM*pcm,char*file_name){
	FILE*fp;
	int	 n;
	char	riff_chunk_ID[4];
	long	riff_chunk_size;
	char	riff_form_type[4];
	char	fmt_chunk_ID[4];
	long	fmt_chunk_size;
	short fmt_wave_format_type;
	short fmt_channel;
	long	fmt_samples_per_sec;
	long	fmt_bytes_per_sec;
	short fmt_block_size;
	short fmt_bits_per_sample;
	char	data_chunk_ID[4];
	long	data_chunk_size;
	short data;
	double s;
	
	riff_chunk_ID[0]='R';
	riff_chunk_ID[1]='I';
	riff_chunk_ID[2]='F';
	riff_chunk_ID[3]='F';
	
	riff_chunk_size=36+ pcm->length * 2;
	riff_form_type[0]='W';
	riff_form_type[1]='A';
	riff_form_type[2]='V';
	riff_form_type[3]='E';
	
	fmt_chunk_ID[0]='f';
	fmt_chunk_ID[1]='m';
	fmt_chunk_ID[2]='t';
	fmt_chunk_ID[3]=' ';
	
	fmt_chunk_size=16;
	fmt_wave_format_type=1;
	fmt_channel=1;
	fmt_samples_per_sec=pcm->fs;
	fmt_bytes_per_sec=pcm->fs * pcm->bits / 8;
	fmt_block_size=pcm->bits / 8;
	fmt_bits_per_sample=pcm->bits;
	
	data_chunk_ID[0]='d';
	data_chunk_ID[1]='a';
	data_chunk_ID[2]='t';
	data_chunk_ID[3]='a';
	data_chunk_size=pcm->length*2;

	fp=fopen(file_name,"wb");

	fwrite(riff_chunk_ID,1,4,fp);
	fwrite(&riff_chunk_size,4,1,fp);
	fwrite(riff_form_type,1,4,fp);
	fwrite(fmt_chunk_ID,1,4,fp);
	fwrite(&fmt_chunk_size,4,1,fp);
	fwrite(&fmt_wave_format_type,2,1,fp);
	fwrite(&fmt_channel,2,1,fp);
	fwrite(&fmt_samples_per_sec,4,1,fp);
	fwrite(&fmt_bytes_per_sec,4,1,fp);
	fwrite(&fmt_block_size,2,1,fp);
	fwrite(&fmt_bits_per_sample,2,1,fp);
	fwrite(data_chunk_ID,1,4,fp);
	fwrite(&data_chunk_size,4,1,fp);

	for(n=0;n<pcm->length;n++){
		s=(pcm->s[n]+1.0) / 2.0 * 65536.0;
		if(s>65535.0){
			s=65535.0;
		}else if(s<0.0){
			s=0.0;
		}
		data=(short)(s+0.5)-32768;
		fwrite(&data,2,1,fp);
	}
	fclose(fp);

}


int main(void){
	MONO_PCM pcm0,pcm1;
	int n;

//	printf("%d\n",sizeof(short));

	mono_wave_read(&pcm0,"a.wav");

	pcm1.fs		=pcm0.fs;
	pcm1.bits	=pcm0.bits;
	pcm1.length=pcm0.length;
	pcm1.s		 =calloc(pcm1.length,sizeof(double));
	for(n=0;n<pcm1.length;n++){
		pcm1.s[n]=pcm0.s[n];
	}
	mono_wave_write(&pcm1,"b.wav");
	free(pcm0.s);
	free(pcm1.s);
	return 0;
}