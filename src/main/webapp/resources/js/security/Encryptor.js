var INVALID_HEX_CHAR_ERROR=1;var NOT_VALID_HEX_CHARACTER=-1;var NO_HEX_CONVERSION_ERRORS=0;var ERR_NO_ERROR=0;var HEX_RADIX=16;var NUM_OF_NIBBLES_PER_BYTE=2;var HEX_CASE=0;var C_String='';var P_String='';var key;function getEncryptedUserLoginMsg(){return C_String;}
function getEncodingParameter(){return P_String;}
function fillByteArray(aa,ba){var ca;var da=aa.length;for(ca=0;ca<da;ca++){aa[ca]=ba;}
return aa;}
function convertHexArrayToString(ea){var fa;var ga='';var ha=ea.length;var ia;var ja;for(fa=0;fa<ha;fa++){ia=ea[fa]&0xF0>>4;ja=parseInt(ia,HEX_RADIX);ga=ga+ja.toUpperCase();ia=ea[fa]&0x0F;ja=parseInt(ia,HEX_RADIX);ga=ga+ja.toUpperCase();}
return ga;}
function convertToHexString(ka){var la=HEX_CASE?'0123456789ABCDEF':'0123456789abcdef';var ma='';for(var i=0;i<ka.length;i++){ma+=la.charAt((ka[i]>>4)&0xF)+la.charAt((ka[i])&0xF);}
return ma;}
function convertAsciiArrayToHexByteArray(na,oa,pa,qa){var ra;var sa;var ta;var ua;ua=parseInt((qa+1)/2);for(sa=0,ta=0;sa<ua;sa++){if(ta<(qa-1)){ra=(parseInt(na[ta]&0x0F))<<4;ra=ra|parseInt(na[ta+1]&0x0F);}
else{ra=parseInt(oa[sa+pa]&0x0F);ra=ra|(parseInt(na[ta]&0x0F))<<4;}
oa[sa+pa]=ra;ta+=NUM_OF_NIBBLES_PER_BYTE;}
return oa;}
function convertStringToPackedHexByteArray(va,wa,xa){var ya=va.length;var za,byteCount,hexValue,temp;var Aa;for(za=0,byteCount=0;za<ya;za++,byteCount++){Aa=va.charAt(za);hexValue=parseInt(Aa,HEX_RADIX);if(hexValue==NOT_VALID_HEX_CHARACTER){return INVALID_HEX_CHAR_ERROR;}
temp=hexValue<<4;za++;if(za<ya){Aa=va.charAt(za);hexValue=parseInt(Aa,HEX_RADIX);if(hexValue==NOT_VALID_HEX_CHARACTER){return INVALID_HEX_CHAR_ERROR;}
wa[byteCount+xa]=(temp|hexValue);}
else{wa[byteCount+xa]=(temp|0x0F);}}
return NO_HEX_CONVERSION_ERRORS;}