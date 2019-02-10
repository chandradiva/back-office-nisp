var INVALID_HEX_CHAR_ERROR=1;var NOT_VALID_HEX_CHARACTER=-1;var NO_HEX_CONVERSION_ERRORS=0;var ERR_NO_ERROR=0;var HEX_RADIX=16;var NUM_OF_NIBBLES_PER_BYTE=2;var HEX_CASE=0;var C_String='';var P_String='';var key;function getEncryptedUserLoginMsg(){return C_String;}
function getEncodingParameter(){return P_String;}
function fillByteArray(aa,ba){var ca;var da=aa.length;for(ca=0;ca<da;ca++){aa[ca]=ba;}
return aa;}
function convertHexArrayToString(ea){var fa;var ga='';var ha=ea.length;var ia;var ja;for(fa=0;fa<ha;fa++){ia=ShiftRight2(ea[fa]&0xF0,4);ja=parseInt(ia,HEX_RADIX);ga=ga+ja.toUpperCase();ia=ea[fa]&0x0F;ja=parseInt(ia,HEX_RADIX);ga=ga+ja.toUpperCase();}
return ga;}
function convertToHexString(ka){var la=HEX_CASE?'0123456789ABCDEF':'0123456789abcdef';var ma='';for(var i=0;i<ka.length;i++){ma+=la.charAt((ShiftRight2(ka[i],4))&0xF)+la.charAt((ka[i])&0xF);}
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

//========= Alternate Shift Right Function ===========

function ShiftRight(x, noOfBits) 
{
	return (x >> noOfBits);
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
var bitMaskArray = null;
function ShiftRight2(n, noOfBits) //NOTE: int must be 32-bit so max noOfBits = 32
{
	if (bitMaskArray == null) 
	{
		bitMaskArray = new Array(32);
		for(i = 0; i < 32; i++) 
		{
			if (i > 0) {
				bitMaskArray[i] = bitMaskArray[i-1] + powerOfTwo[i];
			}
			else {
				bitMaskArray[i] = powerOfTwo[i];
			}
		}
	}
	if (n >= -2147483648 && n <= 2147483647) //if within normal 32-bit range just use the same logic (FASTER & MORE RELIABLE)
	{
		return (n >> noOfBits);
	}
	else 
	{
		if (noOfBits < 32) {
			return (
				((n & (~bitMaskArray[noOfBits-1])) / powerOfTwo[noOfBits]) //as >> n basically can be read as faster way to do "1/(2^noOfBits)" that's why the clean up result; (n & (~bitMaskArray[noOfBits-1]) will remove the low digit value so that the divide result will always be integer value
				| ((n&powerOfTwo[31])==(powerOfTwo[31])?(~bitMaskArray[noOfBits-1]):0) //if the sign digit is 1 then fill the start of the bit with 1. 2^31 = sign digit
			); 
		}
		else {
			return 0;
		}
	}
}
var shiftRightFunc = ((-2160220835 >> 16) == 32573)?ShiftRight:ShiftRight2; //detect which function to use