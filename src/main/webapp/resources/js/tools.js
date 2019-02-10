// custom js - v.2.0
$(function () {

	// Scroll To Top
	var offset = 220;
	var duration = 500;
	jQuery(window).scroll(function() {
		if (jQuery(this).scrollTop() > offset) {
			jQuery('.back-to-top').fadeIn(duration);
		} else {
			jQuery('.back-to-top').fadeOut(duration);
		}
	});
	jQuery('.back-to-top').click(function(event) {
		event.preventDefault();
		jQuery('html, body').animate({scrollTop: 0}, duration);
		return false;
	});

	// Loader
    $(document).ajaxStart(function() {
       $(".loader-personal").removeClass('hide');
       $(".loader-personal").show();
    });
    $(document).ajaxComplete(function() {
       $(".loader-personal").addClass('hide');
       $(".loader-personal").hide();
    });
    jQuery("form").on("submit", function() {
       $(".loader-personal").removeClass('hide');
       $(".loader-personal").show();
    });

	// Personalization
    jQuery("form#dashboard-setting").on("submit", function(event) {
        event.preventDefault();

        var mainWidget = jQuery('.widget-main-content').sortable('serialize');
        var sidebarWidget = jQuery('.widget-sidebar').sortable('serialize');
        var formData = jQuery(this).serialize();

        // post dashboard configuration
        jQuery.ajax({
            data: mainWidget + '&' + sidebarWidget + '&' + formData,
            type: 'post',
            url: 'dashboard-setting.php',
            success: function() {
                window.location.href = document.URL;
            }
        });
    });

    jQuery("form#choose-theme").on("submit", function(event) {
        event.preventDefault();
        var post = jQuery(this).serialize();
        localTheme(post);
        // post theme selection
        jQuery.ajax({
            data: jQuery(this).serialize(),
            type: 'post',
            url: 'theme-selector.php',
            success: function() {
                window.location.href = document.URL;
            }
        });
    });

	function localTheme(data){
       var splits = data.split('=');
       var themesName = splits[1];
       localStorage.setItem('custom',themesName);
    }
    // reset theme
    $('.reset-theme').click(function() {
        // post theme selection
		localStorage.removeItem('custom');
        jQuery.ajax({
            data: 'act=reset-theme',
            type: 'post',
            url: 'theme-selector.php',
            success: function() {
                bootbox.alert('Theme has reset', function() {
                    window.location.href = document.URL;
                });
            }
        });
    });

    $('.reset-setting').click(function() {
        // post theme selection
        jQuery.ajax({
            data: 'act=reset-setting',
            type: 'post',
            url: 'theme-selector.php',
            success: function() {
                bootbox.alert('Setting has reset', function() {
                    window.location.href = document.URL;
                });
            }
        });
    });

	// Message Inbox Page
    $('input.select-all').change(function() {
        if ($(this).is(':checked')) {
            $(this).closest('table').find('tbody input[type="checkbox"]').prop("checked", true);
        } else {
            $(this).closest('table').find('tbody input[type="checkbox"]').prop("checked", false);
        }
    });

    $('#inbox-tabs a').click(function() {
        if ($(this).attr('id') == 'message-detail') {
            $(this).removeClass('hide');
        } else {
            $('#message-tab').addClass('hide');
        }
    });

    $('th.star_important').click(function() {
        $(this).closest('table').find('tbody tr').each(function() {
            var id = $(this).data('message-id');
            // TODO for starrring all
        });
    });

    $('#message-detail').on('click', '.action-buttons .replay', function(e) {
        e.preventDefault();
        $('#form-field-recipient').val($(this).closest('.message-header').find('.sender').html());
        $('#form-field-subject').val($(this).closest('.message-header').find('.content-message-title').html());
        $('input[name="replay_to"]').val($("#message-detail").data('parent-id'));
        $('#write-tab').click();
    });
    $('#message-detail').on('click', '.back-to-inbox',function(e) {
        e.preventDefault();

        $('#message-tab').removeClass('active').addClass('hide');
        $('#inbox-tab').removeClass('hide').addClass('active');
        $('#inbox-tab').click();
    });
    $('#write').on('click', '.btn-back-message-list',function(e) {
        e.preventDefault();

        $('#message-tab').removeClass('active').addClass('hide');
        $('#inbox-tab').removeClass('hide').addClass('active');
        $('#inbox-tab').click();
    });
});

$(function () {
	//default themes
    var customThemes = localStorage.getItem('custom');
    if(customThemes == null ){
            localStorage.setItem('themes','default');
            var themesName = localStorage.getItem('themes');
            $('.locate-theme').attr('href' ,'themes/'+themesName+'/css/default.css')
    }else{
        
         var themesName = localStorage.getItem('custom');
         $('.locate-theme').attr('href' ,'themes/'+themesName+'/css/default.css')
    }
   
});

// SECTION COLUMN SELECTOR
$(function() {
    $('.column-selector').on('click', function(event){
        event.stopPropagation();
    });

    function checkAll(dropzone) {
        if (dropzone.find('input[type="checkbox"][id!="column-selector-x"]').not(':checked').length == 0) {
            $('#column-selector-x').prop('checked', true);
            return false;
        } else {
            $('#column-selector-x').prop('checked', false);
            return false;
        }
    }

    var dropzone = $('#columnSelector');
    var tableHeader = $('table.custom-popup thead th');

    var html = '<li><label class="column-select-all"><input type="checkbox" id="column-selector-x" value="-1"> All</label></li>';
    tableHeader.each(function(index) {

        if ($(this).data('priority') == 'hidden') {
            $(tableHeader[index]).hide();
            $('table.custom-popup tbody tr td:nth-child('+ (index + 1) +')').hide();
            $($('table.custom-popup tfoot th')[index]).hide();
        }

        var isHide = 'checked="checked"';
        if ($(this).is(':hidden')) {
            isHide = '';
        }
        var name = $(this).data('selector-name');
        if (!name) {
            name = $(this).text();
        }
        if ($(this).data('priority') != 'critical') {
            html += '<li><label><input type="checkbox" id="column-selector-' + index +'" value="' + index +'" '+ isHide +'> ' + name + '</label></li>';
        }
    });
    dropzone.html(html);
    checkAll(dropzone);

    dropzone.on('change', 'input[type="checkbox"][id!="column-selector-x"]', function() {
        var isChecked = $(this).is(':checked');
        if (isChecked) {
            // show column
            $(tableHeader[$(this).val()]).show();
            $('table.custom-popup tbody tr td:nth-child('+ (parseInt($(this).val()) + 1) +')').show();
            $($('table.custom-popup tfoot th')[$(this).val()]).show();
        } else {
            // hide column
            $(tableHeader[$(this).val()]).hide();
            $('table.custom-popup tbody tr td:nth-child('+ (parseInt($(this).val()) + 1) +')').hide();
            $($('table.custom-popup tfoot th')[$(this).val()]).hide();
        }
        checkAll(dropzone);
    });
    dropzone.on('change', '#column-selector-x', function() {
        var isChecked = $(this).is(':checked');
        if (isChecked) {
            $('#columnSelector input[type="checkbox"][id!="column-selector-x"]').prop('checked', true);
            $('#columnSelector input[type="checkbox"][id!="column-selector-x"]').change();
        } else {
            $('#columnSelector input[type="checkbox"][id!="column-selector-x"]').attr('checked', false);
            $('#columnSelector input[type="checkbox"][id!="column-selector-x"]').change();
        }
    });
});
function convertToDate(strDate){
	var date = strDate.split('/');
	return new Date(date[2], date[1], date[0]);
}
function compareDate(strDate1, strDate2){
	var date1 = convertToDate(strDate1);
	var date2 = convertToDate(strDate2);
	if( date1 > date2 )
		return 1;
	if( date1 < date2 )
		return -1;
	return 0;
}
