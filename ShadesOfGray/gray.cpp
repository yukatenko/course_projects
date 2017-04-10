#include <stdio.h>
//#include <iostream>

	using namespace std;

#pragma pack(1)	
#pragma pack(push)
	struct pixel {unsigned char blue, green, red;
	};
	
	struct BITMAPFILEHEADER{
		unsigned char bfType1, bfType2;
		unsigned long bfSize;        
		unsigned short bfReserved1; 
		unsigned short bfReserved2; 
		unsigned long bfOffBits;     
	};
	
	struct BITMAPINFOHEADER{
		unsigned long biSize; 
		long biWidth; 
		long biHeight; 
		unsigned short biPlanes; 
		unsigned short biBitCount; 
		unsigned long biCompression; 
		unsigned long biSizeImage; 
		long biXPelsPerMeter; 
		long biYPelsPerMeter; 
		unsigned long biClrUsed; 
		unsigned long biClrImportant; 	
	};
#pragma pack(pop)
		
int main(int argc, char *argv[]){
	
	BITMAPFILEHEADER bmfh;
	BITMAPINFOHEADER bmih;
	long i, j;
	unsigned char gray;
	
	if (argc<=1) {
		printf("Please, enter names of input and output files.\n");
		return 0;
		}
	if ((argc>1)&&(argc<=2)){
		printf("Please, enter an output filename.\n");
		return 0;
		}
	FILE *input=fopen(argv[1], "rb");
		if (input==0){
			printf("File not found.\n");
			return 0;
		}
	FILE *output=fopen(argv[2], "wb");
	
	//чтение информации из входного файла и запись в выходной файл
	fread(&bmfh, sizeof(bmfh), 1, input);
	fwrite(&bmfh, sizeof(bmfh), 1, output);
	fread(&bmih, sizeof(bmih), 1, input);
	fwrite(&bmih, sizeof(bmih), 1, output);	
	fseek(input, bmfh.bfOffBits, SEEK_SET); //переход указателя к началу изображения
	
	pixel pixelArray[bmih.biHeight][bmih.biWidth];
	
	//формирование массива пикселей
	for (i=0; i<bmih.biHeight; i++){
		for ( j=0; j<bmih.biWidth; j++){
			fread(&pixelArray[i][j], sizeof(pixel), 1, input);
			}
		}
	
	//фильтр - оттенки серого
	for (i=0; i<bmih.biHeight; i++){
		for (j=0; j<bmih.biWidth; j++){
			gray=0.11*pixelArray[i][j].blue+0.59*pixelArray[i][j].green+0.3*pixelArray[i][j].red;
			pixelArray[i][j].blue=gray;
			pixelArray[i][j].green=gray;
			pixelArray[i][j].red=gray;
						
			fwrite(&pixelArray[i][j], sizeof(pixel), 1, output);
			}
		}
return 0;	
}
