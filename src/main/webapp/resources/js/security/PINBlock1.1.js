var PIN_BLOCK_FILL_CHARACTER=0xFF;var FMT_2_CONTROL_BYTE=0x02;var FMT_12_CONTROL_BYTE=0xC1;var ISO_FORMAT_2_TYPE=1;var ISO_FORMAT_12_TYPE=2;var MAX_PIN_STRING_SIZE=30;var MIN_PIN_STRING_SIZE=4;var MAX_NUMERIC_PIN_STRING_SIZE=12;var MAX_NUMERIC_PIN_BYTE_SIZE=6;var DECIMAL_RADIX=10;var NUM_OF_BYTES_IN_FMT2_PIN_BLOCK=8;var NUM_OF_BYTES_PER_CNTRL_AND_PIN_LENGTH=2;var ENCODING_PARAMETER_SIZE_IN_BYTES=16;var RANDOM_SEED_SIZE_IN_BYTES=20;var RSA_MODULUS_SIZE_IN_BITS=1024;var RSA_MODULUS_SIZE_IN_BYTES=parseInt(RSA_MODULUS_SIZE_IN_BITS/8);var RSA_EXPONENT_SIZE_IN_BYTES=parseInt(RSA_MODULUS_SIZE_IN_BITS/8);var ONE_PIN_BLOCK_IN_MESSAGE=1;var SHA1_HASH_SIZE_IN_BYTES=20;var OAEP_SHA1_OFFSET_IN_BYTES=42;var MIN_PIN_MESSAGE_SIZE_IN_BYTES=17;var MIN_PIN_BLOCK_SIZE=8;var MAX_MESSAGE_SIZE_IN_BYTES=RSA_MODULUS_SIZE_IN_BYTES-OAEP_SHA1_OFFSET_IN_BYTES;var MIN_RANDOM_NUMBER_STRING_LENGTH=MIN_PIN_BLOCK_SIZE*NUM_OF_NIBBLES_PER_BYTE;var ENCODED_MESSAGE_SIZE_IN_BYTES=RSA_MODULUS_SIZE_IN_BYTES-1;var DATA_BLOCK_SIZE_IN_BYTES=ENCODED_MESSAGE_SIZE_IN_BYTES-SHA1_HASH_SIZE_IN_BYTES;var MAX_PIN_MESSAGE_SIZE_IN_BYTES=RSA_MODULUS_SIZE_IN_BYTES-OAEP_SHA1_OFFSET_IN_BYTES;var ERR_INVALID_PIN_LENGTH=10;var ERR_INVALID_PIN=11;var ERR_INVALID_PIN_BLOCK=20;var ERR_INVALID_RANDOM_NUMBER_LENGTH=21;var ERR_INVALID_RANDOM_NUMBER=22;var ERR_INVALID_PIN_MESSAGE=30;var ERR_INVALID_PIN_MESSAGE_LENGTH=31;var ERR_INVALID_ENCODED_MSG_LENGTH=40;var ERR_INVALID_RSA_KEY_LENGTH=41;var ERR_INVALID_RSA_KEY=42;var publicExponentString='0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010001';var modulus;var publicExponent;var chunkSize;var P=new Array(ENCODING_PARAMETER_SIZE_IN_BYTES);var maxOutputDataSizeInBytes;function PINBlock(aa){this.length=aa.length;this.byteArray=aa;}
function createPINBlock(ba,ca){var da;var ea=new Array(MAX_PIN_STRING_SIZE);var fa=new Array(MAX_NUMERIC_PIN_STRING_SIZE);var ga;var ha;var ia=false;var ja=false;if(ba==''){return ERR_INVALID_PIN;}
PINLength=ba.length;if(PINLength>MAX_PIN_STRING_SIZE||PINLength<MIN_PIN_STRING_SIZE){return ERR_INVALID_PIN_LENGTH;}
if(PINLength<=MAX_NUMERIC_PIN_STRING_SIZE){ja=true;}
for(da=0;da<PINLength;da++){ga=ba.charAt(da);ha=ba.charCodeAt(da);if(ja){if(isNaN(ga)){ja=false;}
else{fa[da]=parseInt(ga,DECIMAL_RADIX);}}
ea[da]=ha;}
ja=false;
if(ja){createFormat2PINBlock(fa,PINLength,ca);}
else{createFormat12PINBlock(ea,PINLength,ca);}
return 0;}
function createFormat2PINBlock(ka,la,ma){var na;PINBlockType=ISO_FORMAT_2_TYPE;PINBlockLength=NUM_OF_BYTES_IN_FMT2_PIN_BLOCK;PINBlockByteArray=new Array(NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);PINBlockByteArray=fillByteArray(PINBlockByteArray,PIN_BLOCK_FILL_CHARACTER);na=FMT_2_CONTROL_BYTE<<4;PINBlockByteArray[0]=(na|(la&0x00FF));PINBlockByteArray=convertAsciiArrayToHexByteArray(ka,PINBlockByteArray,1,la);createPINMessage(PINBlockByteArray,ma);return;}
function createFormat12PINBlock(oa,pa,qa){var ra;PINBlockType=ISO_FORMAT_12_TYPE;if(pa<=6){ra=1;}
else{ra=2+parseInt((pa-7)/NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);}
PINBlockLength=ra*NUM_OF_BYTES_IN_FMT2_PIN_BLOCK;switch(ra){case 1:PINBlockByteArray=new Array(NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);break;case 2:PINBlockByteArray=new Array(2*NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);break;case 3:PINBlockByteArray=new Array(3*NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);break;default:PINBlockByteArray=new Array(4*NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);}
PINBlockByteArray=fillByteArray(PINBlockByteArray,PIN_BLOCK_FILL_CHARACTER);PINBlockByteArray[0]=FMT_12_CONTROL_BYTE;PINBlockByteArray[1]=pa;arrayCopy(oa,0,PINBlockByteArray,2,pa);createPINMessage(PINBlockByteArray,qa);return;}
function createPINMessage(sa,ta){var ua;var va;var wa;var xa;var ya;var za;var Aa;var Ba;ua=new Array(MAX_MESSAGE_SIZE_IN_BYTES);ua[0]=ONE_PIN_BLOCK_IN_MESSAGE;va=1;if(sa==null){return ERR_INVALID_PIN_BLOCK;}
wa=sa.length;arrayCopy(sa,0,ua,va,wa);va=va+wa;if(ta==null){return ERR_INVALID_RANDOM_NUMBER;}
ya=ta.length;za=parseInt((ya+1)/2);Aa=(MAX_MESSAGE_SIZE_IN_BYTES-va)*NUM_OF_NIBBLES_PER_BYTE;if(ya<MIN_RANDOM_NUMBER_STRING_LENGTH||ya>Aa||ya!=(za*2)){return ERR_INVALID_RANDOM_NUMBER_LENGTH;}
Ba=convertStringToPackedHexByteArray(ta,ua,va);if(Ba!=NO_HEX_CONVERSION_ERRORS){return ERR_INVALID_RANDOM_NUMBER;}
va=va+za;xa=new Array(va);for(var Ca=0;Ca<va;Ca++){xa[Ca]=ua[Ca];}
OAEPEncodedPINMessage(xa);return;}
function OAEPEncodedPINMessage(Da){var Ea=new Array(ENCODED_MESSAGE_SIZE_IN_BYTES);var Fa;var Ga;var Ha;Ea=doOAEPEncoding(Da);Fa=convertToHexString(Ea);Ga=convertToHexString(P);P_String=Ga.toUpperCase();Ha=encryptMessageRSA(Fa);C_String=Ha.toUpperCase();return;}
function doOAEPEncoding(Ia){var Ja=new Array(ENCODED_MESSAGE_SIZE_IN_BYTES);var Ka;var La;var Ma;var Na=new Array(MAX_PIN_MESSAGE_SIZE_IN_BYTES);var Oa=new Array(SHA1_HASH_SIZE_IN_BYTES);var DB=new Array(DATA_BLOCK_SIZE_IN_BYTES);var Pa=new Array(DATA_BLOCK_SIZE_IN_BYTES);var Qa=new Array(DATA_BLOCK_SIZE_IN_BYTES);var Ra=new Array(SHA1_HASH_SIZE_IN_BYTES);var Sa=new Array(SHA1_HASH_SIZE_IN_BYTES);var Ta=new Array(SHA1_HASH_SIZE_IN_BYTES);if(Ia==null){return ERR_INVALID_PIN_MESSAGE;}
Ka=Ia.length;if(Ka<MIN_PIN_MESSAGE_SIZE_IN_BYTES||Ka>MAX_PIN_MESSAGE_SIZE_IN_BYTES){return ERR_INVALID_PIN_MESSAGE_LENGTH;}
P=randomGenerator(ENCODING_PARAMETER_SIZE_IN_BYTES);Oa=doHash(P,ENCODING_PARAMETER_SIZE_IN_BYTES);fillByteArray(DB,0x00);arrayCopy(Oa,0,DB,0,SHA1_HASH_SIZE_IN_BYTES);La=SHA1_HASH_SIZE_IN_BYTES;Ma=DATA_BLOCK_SIZE_IN_BYTES-SHA1_HASH_SIZE_IN_BYTES-Ka-1;La+=Ma;DB[La]=parseInt(0x01);La++;copyByteArray(Ia,Na,Ka);arrayCopy(Na,0,DB,La,Ka);Ra=randomGenerator(RANDOM_SEED_SIZE_IN_BYTES);var Ua=convertToHexString(Ra);Ra=fixSeedGenerator('FDC5F13736239504EEAC8364A9F1FC70E9A4EDE7');var Va=convertToHexString(Ra);MGF1(Ra,Pa,DATA_BLOCK_SIZE_IN_BYTES);xorByteArrays(DB,Pa,Qa);MGF1(Qa,Sa,SHA1_HASH_SIZE_IN_BYTES);xorByteArrays(Ra,Sa,Ta);arrayCopy(Ta,0,Ja,0,SHA1_HASH_SIZE_IN_BYTES);arrayCopy(Qa,0,Ja,SHA1_HASH_SIZE_IN_BYTES,DATA_BLOCK_SIZE_IN_BYTES);return Ja;}
function MGF1(Z,T,l){var C=new Array(NUM_OF_BYTES_PER_WORD);var Wa=new Array(ENCODED_MESSAGE_SIZE_IN_BYTES);var Xa=new Array(SHA1_HASH_SIZE_IN_BYTES);var Ya,seedLength,offset,remainingBytes,numberOfBytesToCopy;seedLength=Z.length;Ya=parseInt(l/SHA1_HASH_SIZE_IN_BYTES);remainingBytes=l-Ya*SHA1_HASH_SIZE_IN_BYTES;if(remainingBytes>0){Ya++;}
numberOfBytesToCopy=SHA1_HASH_SIZE_IN_BYTES;for(var Za=0;Za<Ya;Za++){convertIntToByteArray(Za,C,0);arrayCopy(Z,0,Wa,0,seedLength);arrayCopy(C,0,Wa,seedLength,NUM_OF_BYTES_PER_WORD);Xa=doHash(Wa,(seedLength+NUM_OF_BYTES_PER_WORD));offset=Za*SHA1_HASH_SIZE_IN_BYTES;if(Za==(Ya-1)&&remainingBytes>0){numberOfBytesToCopy=remainingBytes;}
arrayCopy(Xa,0,T,offset,numberOfBytesToCopy);}
return;}
function xorByteArrays($a,ab,bb){var cb,byteArrayLength;byteArrayLength=$a.length;for(cb=0;cb<byteArrayLength;cb++){bb[cb]=parseInt($a[cb]^ab[cb]);}
return;}
function randomGenerator(db){var eb=new Array(db);var fb;var gb;for(var i=0;i<eb.length;i++){fb=Math.floor(Math.random()*16);gb=Math.floor(Math.random()*16);eb[i]=((fb<<4)+gb);}
return eb;}
function fixPGenerator(hb){var ib=new Array(ENCODING_PARAMETER_SIZE_IN_BYTES);for(var i=0;i<hb.length;i+=2){ib[parseInt(i/2)]=((parseInt(hb.charAt(i),HEX_RADIX)<<4)+parseInt(hb.charAt(i+1),HEX_RADIX));}
return ib;}
function fixSeedGenerator(jb){var kb=new Array(ENCODING_PARAMETER_SIZE_IN_BYTES);for(var i=0;i<jb.length;i+=2){kb[parseInt(i/2)]=((parseInt(jb.charAt(i),HEX_RADIX)<<4)+parseInt(jb.charAt(i+1),HEX_RADIX));}
return kb;}
function copyByteArray(lb,mb,nb){for(var ob=0;ob<nb;ob++){mb[ob]=lb[ob];}
return;}
function encryptMessageRSA(pb){var qb;if(modulusString==null||publicExponentString==null){return ERR_INVALID_RSA_KEY_LENGTH;}
if(pb==null){return ERR_INVALID_ENCODED_MSG_LENGTH;}
var rb=new RSAKey();rb.setPublic(modulusString,publicExponentString);validateRSAEncInputData(pb.length);qb=rb.encrypt(pb);return qb;}
function validateRSAEncInputData(sb){var tb;var ub;var vb=parseInt((modulusString.length+1)/2);var wb=parseInt((publicExponentString.length+1)/2);if((vb!=RSA_MODULUS_SIZE_IN_BYTES)||(wb!=RSA_MODULUS_SIZE_IN_BYTES)){return ERR_INVALID_RSA_KEY_LENGTH;}
tb=vb*8;maxOutputDataSizeInBytes=parseInt((tb+7)/8);ub=maxOutputDataSizeInBytes-1;if(sb>(ub+1)){return ERR_INVALID_ENCODED_MSG_LENGTH;}
return;}