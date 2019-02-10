var biRadixBase=2;var biRadixBits=16;var bitsPerDigit=biRadixBits;var biRadix=1<<16;var biHalfRadix=biRadix>>>1;var biRadixSquared=biRadix*biRadix;var maxDigitVal=biRadix-1;var maxInteger=9999999999999998;var maxDigits;var ZERO_ARRAY;var bigZero,bigOne;function setMaxDigits(aa){maxDigits=aa;ZERO_ARRAY=new Array(maxDigits);for(var ba=0;ba<ZERO_ARRAY.length;ba++)ZERO_ARRAY[ba]=0;bigZero=new BigInt();bigOne=new BigInt();bigOne.digits[0]=1;}
setMaxDigits(20);var dpl10=15;var lr10=biFromNumber(1000000000000000);function BigInt(ca){if(typeof ca=="boolean"&&ca==true){this.digits=null;}
else{this.digits=ZERO_ARRAY.slice(0);}
this.isNeg=false;}
function biFromDecimal(s){var da=s.charAt(0)=='-';var i=da?1:0;var ea;while(i<s.length&&s.charAt(i)=='0')++i;if(i==s.length){ea=new BigInt();}
else{var fa=s.length-i;var ga=fa%dpl10;if(ga==0)ga=dpl10;ea=biFromNumber(Number(s.substr(i,ga)));i+=ga;while(i<s.length){ea=biAdd(biMultiply(ea,lr10),biFromNumber(Number(s.substr(i,dpl10))));i+=dpl10;}
ea.isNeg=da;}
return ea;}
function biCopy(bi){var ha=new BigInt(true);ha.digits=bi.digits.slice(0);ha.isNeg=bi.isNeg;return ha;}
function biFromNumber(i){var ia=new BigInt();ia.isNeg=i<0;i=Math.abs(i);var j=0;while(i>0){ia.digits[j++]=i&maxDigitVal;i>>=biRadixBits;}
return ia;}
function reverseStr(s){var ja="";for(var i=s.length-1;i>-1;--i){ja+=s.charAt(i);}
return ja;}
var hexatrigesimalToChar=new Array('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');function biToString(x,ka){var b=new BigInt();b.digits[0]=ka;var qr=biDivideModulo(x,b);var la=hexatrigesimalToChar[qr[1].digits[0]];while(biCompare(qr[0],bigZero)==1){qr=biDivideModulo(qr[0],b);digit=qr[1].digits[0];la+=hexatrigesimalToChar[qr[1].digits[0]];}
return(x.isNeg?"-":"")+reverseStr(la);}
function biToDecimal(x){var b=new BigInt();b.digits[0]=10;var qr=biDivideModulo(x,b);var ma=String(qr[1].digits[0]);while(biCompare(qr[0],bigZero)==1){qr=biDivideModulo(qr[0],b);ma+=String(qr[1].digits[0]);}
return(x.isNeg?"-":"")+reverseStr(ma);}
var hexToChar=new Array('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f');function digitToHex(n){var na=0xf;var oa="";for(i=0;i<4;++i){oa+=hexToChar[n&na];n>>>=4;}
return reverseStr(oa);}
function biToHex(x){var pa="";var n=biHighIndex(x);for(var i=biHighIndex(x);i>-1;--i){pa+=digitToHex(x.digits[i]);}
return pa;}
function charToHex(c){var qa=48;var ra=qa+9;var sa=97;var ta=sa+25;var ua=65;var va=65+25;var wa;if(c>=qa&&c<=ra){wa=c-qa;}
else if(c>=ua&&c<=va){wa=10+c-ua;}
else if(c>=sa&&c<=ta){wa=10+c-sa;}
else{wa=0;}
return wa;}
function hexToDigit(s){var xa=0;var sl=Math.min(s.length,4);for(var i=0;i<sl;++i){xa<<=4;xa|=charToHex(s.charCodeAt(i))}
return xa;}
function biFromHex(s){var ya=new BigInt();var sl=s.length;for(var i=sl,j=0;i>0;i-=4,++j){ya.digits[j]=hexToDigit(s.substr(Math.max(i-4,0),Math.min(i,4)));}
return ya;}
function biFromString(s,za){var Aa=s.charAt(0)=='-';var Ba=Aa?1:0;var Ca=new BigInt();var Da=new BigInt();Da.digits[0]=1;for(var i=s.length-1;i>=Ba;i--){var c=s.charCodeAt(i);var Ea=charToHex(c);var Fa=biMultiplyDigit(Da,Ea);Ca=biAdd(Ca,Fa);Da=biMultiplyDigit(Da,za);}
Ca.isNeg=Aa;return Ca;}
function biFromChar(c,Ga){var Ha=new BigInt();var Ia=new BigInt();Ia.digits[0]=1;var Ja=charToHex(c);var Ka=biMultiplyDigit(Ia,Ja);return Ha;}
function biDump(b){return(b.isNeg?"-":"")+b.digits.join(" ");}
function biAdd(x,y){var La;if(x.isNeg!=y.isNeg){y.isNeg=!y.isNeg;La=biSubtract(x,y);y.isNeg=!y.isNeg;}
else{La=new BigInt();var c=0;var n;for(var i=0;i<x.digits.length;++i){n=x.digits[i]+y.digits[i]+c;La.digits[i]=n&0xffff;c=Number(n>=biRadix);}
La.isNeg=x.isNeg;}
return La;}
function biSubtract(x,y){var Ma;if(x.isNeg!=y.isNeg){y.isNeg=!y.isNeg;Ma=biAdd(x,y);y.isNeg=!y.isNeg;}
else{Ma=new BigInt();var n,c;c=0;for(var i=0;i<x.digits.length;++i){n=x.digits[i]-y.digits[i]+c;Ma.digits[i]=n&0xffff;if(Ma.digits[i]<0)Ma.digits[i]+=biRadix;c=0-Number(n<0);}
if(c==-1){c=0;for(var i=0;i<x.digits.length;++i){n=0-Ma.digits[i]+c;Ma.digits[i]=n&0xffff;if(Ma.digits[i]<0)Ma.digits[i]+=biRadix;c=0-Number(n<0);}
Ma.isNeg=!x.isNeg;}
else{Ma.isNeg=x.isNeg;}}
return Ma;}
function biHighIndex(x){var Na=x.digits.length-1;while(Na>0&&x.digits[Na]==0)--Na;return Na;}
function biNumBits(x){var n=biHighIndex(x);var d=x.digits[n];var m=(n+1)*bitsPerDigit;var Oa;for(Oa=m;Oa>m-bitsPerDigit;--Oa){if((d&0x8000)!=0)break;d<<=1;}
return Oa;}
function biMultiply(x,y){var Pa=new BigInt();var c;var n=biHighIndex(x);var t=biHighIndex(y);var u,uv,k;for(var i=0;i<=t;++i){c=0;k=i;for(j=0;j<=n;++j,++k){uv=Pa.digits[k]+x.digits[j]*y.digits[i]+c;Pa.digits[k]=uv&maxDigitVal;c=uv>>>biRadixBits;}
Pa.digits[i+n+1]=c;}
Pa.isNeg=x.isNeg!=y.isNeg;return Pa;}
function biMultiplyDigit(x,y){var n,c,uv;result=new BigInt();n=biHighIndex(x);c=0;for(var j=0;j<=n;++j){uv=result.digits[j]+x.digits[j]*y+c;result.digits[j]=uv&maxDigitVal;c=uv>>>biRadixBits;}
result.digits[1+n]=c;return result;}
function arrayCopy(Qa,Ra,Sa,Ta,n){var m=Math.min(Ra+n,Qa.length);for(var i=Ra,j=Ta;i<m;++i,++j){Sa[j]=Qa[i];}}
var highBitMasks=new Array(0x0000,0x8000,0xC000,0xE000,0xF000,0xF800,0xFC00,0xFE00,0xFF00,0xFF80,0xFFC0,0xFFE0,0xFFF0,0xFFF8,0xFFFC,0xFFFE,0xFFFF);function biShiftLeft(x,n){var Ua=Math.floor(n/bitsPerDigit);var Va=new BigInt();arrayCopy(x.digits,0,Va.digits,Ua,Va.digits.length-Ua);var Wa=n%bitsPerDigit;var Xa=bitsPerDigit-Wa;for(var i=Va.digits.length-1,i1=i-1;i>0;--i,--i1){Va.digits[i]=((Va.digits[i]<<Wa)&maxDigitVal)|((Va.digits[i1]&highBitMasks[Wa])>>>(Xa));}
Va.digits[0]=((Va.digits[i]<<Wa)&maxDigitVal);Va.isNeg=x.isNeg;return Va;}
var lowBitMasks=new Array(0x0000,0x0001,0x0003,0x0007,0x000F,0x001F,0x003F,0x007F,0x00FF,0x01FF,0x03FF,0x07FF,0x0FFF,0x1FFF,0x3FFF,0x7FFF,0xFFFF);function biShiftRight(x,n){var Ya=Math.floor(n/bitsPerDigit);var Za=new BigInt();arrayCopy(x.digits,Ya,Za.digits,0,x.digits.length-Ya);var $a=n%bitsPerDigit;var ab=bitsPerDigit-$a;for(var i=0,i1=i+1;i<Za.digits.length-1;++i,++i1){Za.digits[i]=(Za.digits[i]>>>$a)|((Za.digits[i1]&lowBitMasks[$a])<<ab);}
Za.digits[Za.digits.length-1]>>>=$a;Za.isNeg=x.isNeg;return Za;}
function biMultiplyByRadixPower(x,n){var bb=new BigInt();arrayCopy(x.digits,0,bb.digits,n,bb.digits.length-n);return bb;}
function biDivideByRadixPower(x,n){var cb=new BigInt();arrayCopy(x.digits,n,cb.digits,0,cb.digits.length-n);return cb;}
function biModuloByRadixPower(x,n){var db=new BigInt();arrayCopy(x.digits,0,db.digits,0,n);return db;}
function biCompare(x,y){if(x.isNeg!=y.isNeg){return 1-2*Number(x.isNeg);}
for(var i=x.digits.length-1;i>=0;--i){if(x.digits[i]!=y.digits[i]){if(x.isNeg){return 1-2*Number(x.digits[i]>y.digits[i]);}
else{return 1-2*Number(x.digits[i]<y.digits[i]);}}}
return 0;}
function biDivideModulo(x,y){var nb=biNumBits(x);var tb=biNumBits(y);var eb=y.isNeg;var q,r;if(nb<tb){if(x.isNeg){q=biCopy(bigOne);q.isNeg=!y.isNeg;x.isNeg=false;y.isNeg=false;r=biSubtract(y,x);x.isNeg=true;y.isNeg=eb;}
else{q=new BigInt();r=biCopy(x);}
return new Array(q,r);}
q=new BigInt();r=x;var t=Math.ceil(tb/bitsPerDigit)-1;var fb=0;while(y.digits[t]<biHalfRadix){y=biShiftLeft(y,1);++fb;++tb;t=Math.ceil(tb/bitsPerDigit)-1;}
r=biShiftLeft(r,fb);nb+=fb;var n=Math.ceil(nb/bitsPerDigit)-1;var b=biMultiplyByRadixPower(y,n-t);while(biCompare(r,b)!=-1){++q.digits[n-t];r=biSubtract(r,b);}
for(var i=n;i>t;--i){var ri=(i>=r.digits.length)?0:r.digits[i];var gb=(i-1>=r.digits.length)?0:r.digits[i-1];var hb=(i-2>=r.digits.length)?0:r.digits[i-2];var yt=(t>=y.digits.length)?0:y.digits[t];var ib=(t-1>=y.digits.length)?0:y.digits[t-1];if(ri==yt){q.digits[i-t-1]=maxDigitVal;}
else{q.digits[i-t-1]=Math.floor((ri*biRadix+gb)/yt);}
var c1=q.digits[i-t-1]*((yt*biRadix)+ib);var c2=(ri*biRadixSquared)+((gb*biRadix)+hb);while(c1>c2){--q.digits[i-t-1];c1=q.digits[i-t-1]*((yt*biRadix)|ib);c2=(ri*biRadix*biRadix)+((gb*biRadix)+hb);}
b=biMultiplyByRadixPower(y,i-t-1);r=biSubtract(r,biMultiplyDigit(b,q.digits[i-t-1]));if(r.isNeg){r=biAdd(r,b);--q.digits[i-t-1];}}
r=biShiftRight(r,fb);q.isNeg=x.isNeg!=eb;if(x.isNeg){if(eb){q=biAdd(q,bigOne);}
else{q=biSubtract(q,bigOne);}
y=biShiftRight(y,fb);r=biSubtract(y,r);}
if(r.digits[0]==0&&biHighIndex(r)==0)r.isNeg=false;return new Array(q,r);}
function biDivide(x,y){return biDivideModulo(x,y)[0];}
function biModulo(x,y){return biDivideModulo(x,y)[1];}
function biMultiplyMod(x,y,m){return biModulo(biMultiply(x,y),m);}
function biPow(x,y){var jb=bigOne;var a=x;while(true){if((y&1)!=0)jb=biMultiply(jb,a);y>>=1;if(y==0)break;a=biMultiply(a,a);}
return jb;}
function biPowMod(x,y,m){var kb=bigOne;var a=x;var k=y;while(true){if((k.digits[0]&1)!=0)kb=biMultiplyMod(kb,a,m);k=biShiftRight(k,1);if(k.digits[0]==0&&biHighIndex(k)==0)break;a=biMultiplyMod(a,a,m);}
return kb;}