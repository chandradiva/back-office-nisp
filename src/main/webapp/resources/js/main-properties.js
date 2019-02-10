// Custom Menu Properties - v1.0

$(document).ready(function(){
    //load English as default menu language
	var langselect = (($.cookie('language') === false) || $.cookie('language') === undefined)? 'en' : $.cookie('language');
    if (langselect == 'id') {
        $('#langid img').addClass('active');
    } else if (langselect == 'en') {
        $('#langen img').addClass('active');
    }

    loadBundles(langselect);

    if (langselect == 'id') {
        $.getScript("js/i18n/jquery.validate.messages_" + langselect + ".js");
        $.fn.datetimepicker.defaults.language = (($.cookie('language') === false) || $.cookie('language') === undefined)? 'en' : $.cookie('language');
    }
});

$('#langid').click(function (){
    var langselect='id';
    $.cookie('language', langselect);
    location.reload();
});
$('#langen').click(function (){
    var langselect='en';
    $.cookie('language', langselect);
    location.reload();
});

// From https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/keys
if (!Object.keys) {
    Object.keys = (function () {
        'use strict';
        var hasOwnProperty = Object.prototype.hasOwnProperty,
            hasDontEnumBug = !({toString: null}).propertyIsEnumerable('toString'),
            dontEnums = [
                'toString',
                'toLocaleString',
                'valueOf',
                'hasOwnProperty',
                'isPrototypeOf',
                'propertyIsEnumerable',
                'constructor'
            ],
            dontEnumsLength = dontEnums.length;

        return function (obj) {
            if (typeof obj !== 'object' && (typeof obj !== 'function' || obj === null)) {
                throw new TypeError('Object.keys called on non-object');
            }

            var result = [], prop, i;

            for (prop in obj) {
                if (hasOwnProperty.call(obj, prop)) {
                    result.push(prop);
                }
            }

            if (hasDontEnumBug) {
                for (i = 0; i < dontEnumsLength; i++) {
                    if (hasOwnProperty.call(obj, dontEnums[i])) {
                        result.push(dontEnums[i]);
                    }
                }
            }
            return result;
        };
    }());
}

if(!Array.isArray) {
    Array.isArray = function(arg) {
        return Object.prototype.toString.call(arg) === '[object Array]';
    };
}

if (!String.prototype.trim) {
    String.prototype.trim = function () {
        return this.replace(/^\s+|\s+$/g, '');
    };
}

function loadBundles(lang) {
    jQuery.i18n.properties({
        name:'Menu',
        path:'js/i18n/',
        mode:'both',
        language:lang,
        callback: function() {
            generateMenu();
            generateOther();
        }
    });
}


function generateOther(parentSelector) {
    parentSelector = (parentSelector === undefined)? 'body' : parentSelector;

    $(parentSelector + " .lang" ).each(function() {

        var attributToReplace = $(this).data('trans');
        if (attributToReplace) {
            attributToReplace = attributToReplace.split(',');
        } else {
            attributToReplace = ['content'];
        }
        var lang = $(this).attr('lang');
        if (lang) {
            var localLanguage = jQuery.i18n.prop(lang);
            for(var i=0; i < attributToReplace.length; i++) {
                var currentAttribute = attributToReplace[i].trim().toLowerCase();
                if (currentAttribute == 'content') {
                    $(this).empty().append(localLanguage);
                } else {
                    $(this).attr(currentAttribute, localLanguage);
                }
            }

        }

        var placeHolder = $(this).attr('placeholder');
        if (placeHolder) {
            $(this).attr('placeholder', jQuery.i18n.prop(placeHolder));
        }
    });
}

//generate menu
function generateMenu() {
    $.getJSON('js/i18n/original.json', function(data){
        var ul = $('ul.navbar-nav');
        var output = "";
        // foreach main menu
        $.each(data.tableUserProfileAccess,function(key,data){
            var col = Object.keys(data).length > 4 ? 4 : Object.keys(data).length;
            output += '<li class="dropdown yamm-fw nav-'+col+'-col">';

            output += '<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+jQuery.i18n.prop(key)+' <b class="caret"></b> </a>';

            output += '<ul class="dropdown-menu">';
            output += '<li>';
            output += '<div class="yamm-content">';
            output += '<div class="row">';

            // menu level 1
            var menu1 = data;
            $.each(menu1,function(key,data){

                output += '<ul class="col-sm-'+col+' list-unstyled dontsplit">';
                output += '<li><h4>'+jQuery.i18n.prop(key)+'</h4>';

                // menu level 2
                var menu2 = data;
                output += generateSubMenu(menu2);

                output += '</li></ul>';

            });

            output += ' </div>';
            output += '</div>';
            output += '</li>';

            output +=  '</ul>';
            output += '</li>';
        });

        ul.html(output);
    });
}

function generateSubMenu(menu2) {
    var output = '';
    if (Array.isArray(menu2)) {
        output += '<ul class="sub list-unstyled">';
        $.each(menu2,function(key, data){
            var child = data.split("|");
            output += '<li><a href="'+child[2]+'"> '+jQuery.i18n.prop( child[1] )+' </a></li>';
        });
        output += '</ul>';

        return output;
    } else {
        output += '<ul class="sub list-unstyled">';
        $.each(menu2,function(key,data){

            output += '<li class="parent">'+jQuery.i18n.prop(key);

            // menu level 2
            var subMenu = data;
            output += generateSubMenu(subMenu);

            output += '</li>';

        });
        output += '</ul>';
    }

    return output;
}