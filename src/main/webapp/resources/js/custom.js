Number.prototype.formatMoney = function(c, d, t){
var n = this, 
    c = isNaN(c = Math.abs(c)) ? 2 : c, 
    d = d == undefined ? "." : d, 
    t = t == undefined ? "," : t, 
    s = n < 0 ? "-" : "", 
    i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", 
    j = (j = i.length) > 3 ? j % 3 : 0;
   return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
 };
 
function getTransactionType(type){
	type = parseInt(type);
	 switch (type) {
		 case 1: return 'Subscription';
		 case 2: return 'Redemption';
		 case 3: return 'Switching In';
		 case 4: return 'Switching Out';
		 default:break;
	 }
}
function getTransactionStatus(status){
	status = parseInt(status);
	 switch (status) {
	 	case 0: return 'In Progress';
		case 1: return 'Successful';
		case 2: return 'Rejected';
		case 3: return 'Cancelled (by Branch)';
		case 4: return 'Cancelled (by PO)';
		 default:break;
	 }
}
function getTransactionSource(source){
	 switch (source) {
	 	case 'M': return 'Mobile Banking';
		case 'I': return 'Internet Banking';
		case 'B': return 'Branch';
		case 'P': return 'Phone';
		default:break;
	 }
}