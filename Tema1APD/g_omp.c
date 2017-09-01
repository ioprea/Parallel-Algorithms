#include <stdlib.h>
#include <stdio.h>
#include <limits.h>
#include <string.h>
#include <omp.h>


//Functie copiere matrici
void copyMatrix(int** a,int** b,int n,int m){
	int i,j;
	
	#pragma omp parallel for private(i,j) collapse(2)
	for(i=0;i<n+2;i++)
		for(j=0;j<m+2;j++)
			a[i][j]=b[i][j];

}

//Bordare matrice
void update(int** config1, int n,int m){
	int i,j;

	//Bordare coloane
	for(i=1;i<n+1;i++){
		config1[i][0] = config1[i][m];
		config1[i][m+1] = config1[i][1];
	}

	//Bordare linii
	for(j=1;j<m+1;j++){
		config1[0][j] = config1[n][j];
		config1[n+1][j] = config1[1][j];
	}

	//Bordare colturi
	config1[0][0] = config1[n][m]; //stanga sus
	config1[n+1][0] = config1[1][m]; //stanga jos
	config1[0][m+1] = config1[n][1]; //dreapta sus
	config1[n+1][m+1] = config1[1][1]; //drepata jos

}


//Calculez numarul de vecini alive
int neightbours (int** m,int i,int j){
	return m[i-1][j-1] + m[i-1][j] + m[i-1][j+1] +
		   m[i][j-1] + m[i][j+1] +
		   m[i+1][j-1] + m[i+1][j] + m[i+1][j+1];

}

//Verificare reguli Game of Life
void check (int** config1,int** config2,int n,int m){
	int i,j;

	#pragma omp parallel for collapse(2) private(i,j)
	for(i=1;i<n+1;i++){
			for(j=1;j<m+1;j++){
			int count = neightbours(config1,i,j);
			
			if(count < 2) config2[i][j] = 0;
			else if(config1[i][j] == 1 && ((count == 2) || (count==3))) config2[i][j] = 1;
			else if(count > 3) config2[i][j] = 0;
			else if(config1[i][j] == 0 && count == 3) config2[i][j] = 1;
			else config2[i][j]=config1[i][j];
			}
	}

}

int main(int argc, char **argv){

	//Fisiere de intrare/iesire
	FILE* in;
	FILE* out;
	int n,m;
	int i,j,k;

	//Numarul de iteratii
	int iteratii = atoi(argv[2]);

	//Deschidere fisiere citire si scriere
	in = fopen(argv[1],"r");
	out = fopen(argv[3],"w");

	char temp,temp1;
	char* line = malloc(50*sizeof(char));

	if(!in) exit(0);
	if(!out) exit(0);


	//Citire linii si coloane
	fscanf(in,"%d",&n);
	fscanf(in,"%d",&m);
	fgets(line,INT_MAX,in);

	int** config1;
	int** config2;

	//Alocare prima matrice (configuratia la pasul i)
	config1 = (int**) malloc ((n+2)*sizeof(int*));
	for(i=0;i<n+2;i++){
		config1[i]=(int*) malloc( (m+2)*sizeof(int));
	}

	//Alocare a doua matrice matrice (configuratia la pasul i+1)
	config2 = (int**) malloc ((n+2)*sizeof(int*));
	for(i=0;i<n+2;i++){
		config2[i]=(int*) malloc( (m+2)*sizeof(int));
	}

	//Citire matrice
	for(i=1;i<n+1;i++){
		for(j=1;j<m+1;j++){
			fscanf(in,"%c%c",&temp,&temp1);
			if(temp == 'X') {config1[i][j]=1;}
			else {config1[i][j]=0;}
		}
		fgets(line,INT_MAX,in);
	}
	fclose(in);

	update(config1,n,m);

	//#pragma omp parallel for shared(k)
	for(k=0;k<iteratii;k++){
		
		check(config1,config2,n,m);
		update(config2,n,m);
		copyMatrix(config1,config2,n,m);

	}

	//Scriere in fisier
	for(i=1;i<n+1;i++){
		for(j=1;j<m+1;j++){
			if(config2[i][j] == 1) fprintf(out,"X ");
			else if(config2[i][j] == 0) fprintf(out,". ");
		}
		fprintf(out,"\n");

	}
}