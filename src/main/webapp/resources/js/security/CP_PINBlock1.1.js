var TWO_PIN_BLOCKS_IN_MESSAGE=2;function CP_createPINBlock(aa,ba,ca){var da;var ea=new Array(MAX_PIN_STRING_SIZE);var fa=new Array(MAX_NUMERIC_PIN_STRING_SIZE);var ga;var ha;var ia=false;var ja=false;if(aa==''){return ERR_INVALID_PIN;}
PINLength=aa.length;if(PINLength>MAX_PIN_STRING_SIZE||PINLength<MIN_PIN_STRING_SIZE){return ERR_INVALID_PIN_LENGTH;}
if(PINLength<=MAX_NUMERIC_PIN_STRING_SIZE){ja=true;}
for(da=0;da<PINLength;da++){ga=aa.charAt(da);ha=aa.charCodeAt(da);if(ja){if(isNaN(ga)){ja=false;}
else{fa[da]=parseInt(ga,DECIMAL_RADIX);}}
ea[da]=ha;}
var ka=new Array(MAX_PIN_STRING_SIZE);var la=new Array(MAX_NUMERIC_PIN_STRING_SIZE);var ma;var na;if(ba==''){return ERR_INVALID_PIN;}
PINLength=ba.length;if(PINLength>MAX_PIN_STRING_SIZE||PINLength<MIN_PIN_STRING_SIZE){return ERR_INVALID_PIN_LENGTH;}
if(PINLength<=MAX_NUMERIC_PIN_STRING_SIZE){ja=true;}
for(da=0;da<PINLength;da++){ma=ba.charAt(da);na=ba.charCodeAt(da);if(ja){if(isNaN(ma)){ja=false;}
else{la[da]=parseInt(ma,DECIMAL_RADIX);}}
ka[da]=na;}
ja=false;
if(ja){CP_createFormat2PINBlock(fa,la,PINLength,ca);}
else{CP_createFormat12PINBlock(ea,ka,PINLength,ca);}
return 0;}
function CP_createFormat2PINBlock(oa,pa,qa,ra){var sa;PINBlockType=ISO_FORMAT_2_TYPE;PINBlockLength=NUM_OF_BYTES_IN_FMT2_PIN_BLOCK;PINBlockByteArray=new Array(NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);PINBlockByteArray=fillByteArray(PINBlockByteArray,PIN_BLOCK_FILL_CHARACTER);sa=FMT_2_CONTROL_BYTE<<4;PINBlockByteArray[0]=(sa|(qa&0x00FF));PINBlockByteArray=convertAsciiArrayToHexByteArray(oa,PINBlockByteArray,1,qa);NEW_PINBlockByteArray=new Array(NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);NEW_PINBlockByteArray=fillByteArray(NEW_PINBlockByteArray,PIN_BLOCK_FILL_CHARACTER);sa=FMT_2_CONTROL_BYTE<<4;NEW_PINBlockByteArray[0]=(sa|(qa&0x00FF));NEW_PINBlockByteArray=convertAsciiArrayToHexByteArray(pa,NEW_PINBlockByteArray,1,qa);CP_createPINMessage(PINBlockByteArray,NEW_PINBlockByteArray,ra);return;}
function CP_createFormat12PINBlock(ta,ua,va,wa){var xa;PINBlockType=ISO_FORMAT_12_TYPE;if(va<=6){xa=1;}
else{xa=2+parseInt((va-7)/NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);}
PINBlockLength=xa*NUM_OF_BYTES_IN_FMT2_PIN_BLOCK;switch(xa){case 1:PINBlockByteArray=new Array(NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);NEW_PINBlockByteArray=new Array(NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);break;case 2:PINBlockByteArray=new Array(2*NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);NEW_PINBlockByteArray=new Array(2*NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);break;case 3:PINBlockByteArray=new Array(3*NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);NEW_PINBlockByteArray=new Array(3*NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);break;default:PINBlockByteArray=new Array(4*NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);NEW_PINBlockByteArray=new Array(4*NUM_OF_BYTES_IN_FMT2_PIN_BLOCK);}
PINBlockByteArray=fillByteArray(PINBlockByteArray,PIN_BLOCK_FILL_CHARACTER);PINBlockByteArray[0]=FMT_12_CONTROL_BYTE;PINBlockByteArray[1]=va;arrayCopy(ta,0,PINBlockByteArray,2,va);NEW_PINBlockByteArray=fillByteArray(NEW_PINBlockByteArray,PIN_BLOCK_FILL_CHARACTER);NEW_PINBlockByteArray[0]=FMT_12_CONTROL_BYTE;NEW_PINBlockByteArray[1]=va;arrayCopy(ua,0,NEW_PINBlockByteArray,2,va);CP_createPINMessage(PINBlockByteArray,NEW_PINBlockByteArray,wa);return;}
function CP_createPINMessage(ya,za,Aa){var Ba;var Ca;var Da;var Ea;var Fa;var Ga;var Ha;var Ia;Ba=new Array(MAX_MESSAGE_SIZE_IN_BYTES);Ba[0]=TWO_PIN_BLOCKS_IN_MESSAGE;Ca=1;if(ya==null){return ERR_INVALID_PIN_BLOCK;}
if(za==null){return ERR_INVALID_PIN_BLOCK;}
Da=ya.length;arrayCopy(ya,0,Ba,Ca,Da);Ca=Ca+Da;Da=za.length;arrayCopy(za,0,Ba,Ca,Da);Ca=Ca+Da;if(Aa==null){return ERR_INVALID_RANDOM_NUMBER;}
Fa=Aa.length;Ga=parseInt((Fa+1)/2);Ha=(MAX_MESSAGE_SIZE_IN_BYTES-Ca)*NUM_OF_NIBBLES_PER_BYTE;if(Fa<MIN_RANDOM_NUMBER_STRING_LENGTH||Fa>Ha||Fa!=(Ga*2)){return ERR_INVALID_RANDOM_NUMBER_LENGTH;}
Ia=convertStringToPackedHexByteArray(Aa,Ba,Ca);if(Ia!=NO_HEX_CONVERSION_ERRORS){return ERR_INVALID_RANDOM_NUMBER;}
Ca=Ca+Ga;Ea=new Array(Ca);for(var Ja=0;Ja<Ca;Ja++){Ea[Ja]=Ba[Ja];}
OAEPEncodedPINMessage(Ea);return;}