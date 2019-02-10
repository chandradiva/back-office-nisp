var MAX_HASH_SIZE_IN_BYTES=20;var NUM_OF_BITS_PER_BLOCK=512;var NUM_OF_BITS_PER_BYTE=8;var NUM_OF_BITS_PER_WORD=32;var NUM_OF_BITS_FOR_MSG_LENGTH=64;var MSG_LENGTH_BYTE_ARRAY_OFFSET=60;var NUM_OF_BYTES_PER_BLOCK=64;var NUM_OF_BYTES_PER_WORD=4;var NUM_OF_WORDS_PER_BLOCK=16;var MAX_NUM_OF_PROCESS_STEPS=80;var PROCESSING_STEP_19=19;var PROCESSING_STEP_39=39;var PROCESSING_STEP_59=59;var PROCESSING_STEP_79=79;var K1=1518500249;var K2=1859775393;var K3=-1894007588;var K4=-899497514;var A,B,C,D,E;var temp;var W=new Array(MAX_NUM_OF_PROCESS_STEPS);var M=new Array(NUM_OF_WORDS_PER_BLOCK);var numOfIntegralBlocksInMsg;var numOfPaddedMsgBlocks;var hashByteArray=new Array(MAX_HASH_SIZE_IN_BYTES);var lastPaddedMsgBlock=new Array(NUM_OF_WORDS_PER_BLOCK);var secondLastPaddedMsgBlock=new Array(NUM_OF_WORDS_PER_BLOCK);function doHash(aa,ba){var H0=1732584193;var H1=-271733879;var H2=-1732584194;var H3=271733878;var H4=-1009589776;var ca,t,tempWord;padInputMessage(aa,ba);for(ca=1;ca<=numOfPaddedMsgBlocks;ca++){if(ca<=numOfIntegralBlocksInMsg){convertByteArrayToBlock(M,aa,(ca-1));}
else if(ca==numOfPaddedMsgBlocks){arrayCopy(lastPaddedMsgBlock,0,M,0,NUM_OF_WORDS_PER_BLOCK);}
else{arrayCopy(secondLastPaddedMsgBlock,0,M,0,NUM_OF_WORDS_PER_BLOCK);}
arrayCopy(M,0,W,0,NUM_OF_WORDS_PER_BLOCK);for(t=NUM_OF_WORDS_PER_BLOCK;t<MAX_NUM_OF_PROCESS_STEPS;t++){tempWord=W[t-3]^W[t-8]^W[t-14]^W[t-16];W[t]=rotateWordLeft(tempWord,1);}
A=H0;B=H1;C=H2;D=H3;E=H4;for(t=0;t<=PROCESSING_STEP_79;t++){if(t>=0&&t<=PROCESSING_STEP_19){temp=rotateWordLeft(A,5)+((B&C)|(~B&D))+E+W[t]+K1;}
else if(t>PROCESSING_STEP_19&&t<=PROCESSING_STEP_39){temp=rotateWordLeft(A,5)+(B^C^D)+E+W[t]+K2;}
else if(t>PROCESSING_STEP_39&&t<=PROCESSING_STEP_59){temp=rotateWordLeft(A,5)+((B&C)|(B&D)|(C&D))+E+W[t]+K3;}
else{temp=rotateWordLeft(A,5)+(B^C^D)+E+W[t]+K4;}
E=D;D=C;C=rotateWordLeft(B,30);B=A;A=temp;}
H0=safe_add(H0,A);H1=safe_add(H1,B);H2=safe_add(H2,C);H3=safe_add(H3,D);H4=safe_add(H4,E);}
convertIntToByteArray(H0,hashByteArray,0);convertIntToByteArray(H1,hashByteArray,4);convertIntToByteArray(H2,hashByteArray,8);convertIntToByteArray(H3,hashByteArray,12);convertIntToByteArray(H4,hashByteArray,16);return hashByteArray;}
function padInputMessage(da,ea){var fa,remainingBytesInMsg,remainingBitsInMsg;var ga;var ha=new Array(NUM_OF_BYTES_PER_BLOCK);fa=ea*NUM_OF_BITS_PER_BYTE;numOfIntegralBlocksInMsg=parseInt(ea/NUM_OF_BYTES_PER_BLOCK);ga=numOfIntegralBlocksInMsg*NUM_OF_BYTES_PER_BLOCK;remainingBytesInMsg=ea-ga;remainingBitsInMsg=remainingBytesInMsg*NUM_OF_BITS_PER_BYTE;numOfPaddedMsgBlocks=numOfIntegralBlocksInMsg+1;fillByteArray(ha,0);arrayCopy(da,ga,ha,0,remainingBytesInMsg);ha[remainingBytesInMsg]=parseInt(0x80);if(remainingBitsInMsg>(NUM_OF_BITS_PER_BLOCK-NUM_OF_BITS_FOR_MSG_LENGTH-1)){secondLastPaddedMsgBlock=new Array(NUM_OF_WORDS_PER_BLOCK);convertByteArrayToBlock(secondLastPaddedMsgBlock,ha,0);fillByteArray(ha,0);convertIntToByteArray(fa,ha,MSG_LENGTH_BYTE_ARRAY_OFFSET);convertByteArrayToBlock(lastPaddedMsgBlock,ha,0);numOfPaddedMsgBlocks++;}
else{convertIntToByteArray(fa,ha,MSG_LENGTH_BYTE_ARRAY_OFFSET);convertByteArrayToBlock(lastPaddedMsgBlock,ha,0);}
return;}
function rotateWordLeft(ia,ja){var ka;ka=(ia<<ja)|(ia>>>(NUM_OF_BITS_PER_WORD-ja));return ka;}
function convertByteArrayToBlock(la,ma,na){var oa,wordCount,byteCount,wordOffset,tempWord;oa=na*NUM_OF_BYTES_PER_BLOCK;for(wordCount=0;wordCount<NUM_OF_WORDS_PER_BLOCK;wordCount++){wordOffset=oa+wordCount*NUM_OF_BYTES_PER_WORD;la[wordCount]=convertByteArrayToInt(ma,wordOffset);}
return;}
function convertByteArrayToInt(pa,qa){var ra,outputWord;ra=parseInt(pa[qa]&0x000000FF);outputWord=ra<<24;ra=parseInt(pa[qa+1]&0x000000FF);outputWord=outputWord|(ra<<16);ra=parseInt(pa[qa+2]&0x000000FF);outputWord=outputWord|(ra<<8);ra=parseInt(pa[qa+3]&0x000000FF);outputWord=outputWord|ra;return outputWord;}
function convertIntToByteArray(sa, ta, ua) { ta[ua] = ShiftRightFunc(sa, 24) & 0x000000FF; ta[ua + 1] = ShiftRightFunc(sa, 16) & 0x000000FF; ta[ua + 2] = ShiftRightFunc(sa, 8) & 0x000000FF; ta[ua + 3] = sa & 0x000000FF; return; }
function safe_add(x, y) { var va = (x & 0xFFFF) + (y & 0xFFFF); var wa = ShiftRightFunc(x, 16) + ShiftRightFunc(y, 16) + ShiftRightFunc(va, 16); return (wa << 16) | (va & 0xFFFF); }
function ShiftRight(n, noOfBits) {
	return (n >> noOfBits);
}
var powerOfTwo = [
	0x00000001, 0x00000002, 0x00000004, 0x00000008,
	0x00000010, 0x00000020, 0x00000040, 0x00000080,
	0x00000100, 0x00000200, 0x00000400, 0x00000800,
	0x00001000, 0x00002000, 0x00004000, 0x00008000,
	0x00010000, 0x00020000, 0x00040000, 0x00080000,
	0x00100000, 0x00200000, 0x00400000, 0x00800000,
	0x01000000, 0x02000000, 0x04000000, 0x08000000,
	0x10000000, 0x20000000, 0x40000000, 0x80000000
]
var _0x9e07=[];var bitMaskArray=null;function ShiftRight2(_0x1c81x3,_0x1c81x4){if(bitMaskArray==null){bitMaskArray= new Array(32);for(i=0;i<32;i++){if(i>0){bitMaskArray[i]=bitMaskArray[i-1]+powerOfTwo[i];} else {bitMaskArray[i]=powerOfTwo[i];} ;} ;} ;if(_0x1c81x3>=-2147483648&&_0x1c81x3<=2147483647){return (_0x1c81x3>>_0x1c81x4);} else {if(_0x1c81x4<32){return (((_0x1c81x3&(~bitMaskArray[_0x1c81x4-1]))/powerOfTwo[_0x1c81x4])|((_0x1c81x3&powerOfTwo[31])==(powerOfTwo[31])?(~bitMaskArray[_0x1c81x4-1]):0));} else {return 0;} ;} ;} ;var ShiftRightFunc=((-2160220835>>16)==32573)?ShiftRight:ShiftRight2;