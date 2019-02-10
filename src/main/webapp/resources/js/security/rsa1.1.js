function parseBigInt(aa,r){return new BigInteger(aa,r);}
function linebrk(s,n){var ca="";var i=0;while(i+n<s.length){ca+=s.substring(i,i+n)+"\n";i+=n;}
return ca+s.substring(i,s.length);}
function byte2Hex(b){if(b<0x10)return "0"+b.toString(16);else return b.toString(16);}
function pkcs1pad2(s,n){if(n<s.length+11){alert("Message too long for RSA");return null;}
var ba=new Array();var i=s.length-1;while(i>=0&&n>0)ba[--n]=s.charCodeAt(i--);ba[--n]=0;var da=new SecureRandom();var x=new Array();while(n>2){x[0]=0;while(x[0]==0)da.nextBytes(x);ba[--n]=x[0];}
ba[--n]=2;ba[--n]=0;return new BigInteger(ba);}
function RSAKey(){this.n=null;this.e=0;this.d=null;this.p=null;this.q=null;this.dmp1=null;this.dmq1=null;this.coeff=null;}
function RSASetPublic(N,E){if(N!=null&&E!=null&&N.length>0&&E.length>0){this.n=parseBigInt(N,16);this.e=parseBigInt(E,16);}
else alert("Invalid RSA public key");}
function RSADoPublic(x){return x.modPowInt(this.e,this.n);}
function RSAEncrypt(ea){var m=parseBigInt(ea,16);if(m==null)return null;var c=this.doPublic(m);if(c==null)return null;var h=c.toString(16);if((h.length&1)==0)return h;else return "0"+h;}
RSAKey.prototype.doPublic=RSADoPublic;RSAKey.prototype.setPublic=RSASetPublic;RSAKey.prototype.encrypt=RSAEncrypt;