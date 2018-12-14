#include <stdio.h>
#define endl "\n"

int main (){

    int a, b;
    scanf("%d %d", &a, &b);
    int c = getSum( a, b);

    printf("%d\n", c);
    return 0;
}

int getSum(int a, int b) {


    if( a + b > 0) {
        a = a+b;
    }
    else b = b +b;

    if( (b < 0) && a > 0 ) {
        a = a*b;
        
        if( b < -2) {
            b = b -a;
        }
    }

    for(int i=0; i<6; i++) {
        if(a+b < 5) {
            a = 5;
        }
    }

    return a;
}