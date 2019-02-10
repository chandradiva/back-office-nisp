// Custom js for IE8
// version 1.0

$(document).on('click', '.dropdown-toggle', function() {
    var that = this;
    if ($(that).siblings('.dropdown-menu').is(':visible')) {
        if ($(that).closest('.dropdown.yamm-fw').hasClass('nav-2-col')) {
            if ($(that).siblings('.dropdown-menu').find('.yamm-content .row > .column').length == 0) {
                $(that).siblings('.dropdown-menu').find('.yamm-content .row').columnize({ columns: 2 });
            }
        }

        if ($(that).closest('.dropdown.yamm-fw').hasClass('nav-3-col')) {
            if ($(that).siblings('.dropdown-menu').find('.yamm-content .row > .column').length == 0) {
                $(that).siblings('.dropdown-menu').find('.yamm-content .row').columnize({ columns: 3 });
            }
        }

        if ($(that).closest('.dropdown.yamm-fw').hasClass('nav-4-col')) {
            if ($(that).siblings('.dropdown-menu').find('.yamm-content .row  > .column').length == 0) {
                $(that).siblings('.dropdown-menu').find('.yamm-content .row').columnize({ columns: 3 });
            }
        }
    }
});