if (typeof(noext) === 'undefined') {
   noext = {}; 
}

if (typeof jQuery === 'undefined') {
    throw new Error('noext.Menu: jQuery must be present');
}

// Constructor
noext.Menu = function (n, subMenuStyle) {
    this.n = n;
    this.subMenuStyle = (subMenuStyle === 'ext') ? 'opened-ext' : 'opened';
    this.firstLi = this.n.children('li');
    this.subMenu = this.firstLi.find('ul');
    this.subMenuItems = this.subMenu.find('li');
    this.menu = this.subMenu.closest('ul.noext-menu');
    this.menuStyle = 'noext-menu-opened';
    this.init();
    noext.Menu.menuInstances.push(this);
};
noext.Menu.menuInstances = [];

noext.Menu.prototype = {
    init: function () { // add events to what we need
        var that = this;
        this.subMenuItems.mouseover( this.hover); // do mouseovers for ie6
        this.subMenuItems.mouseout( this.unHover);
        this.n.click( function (event) { that.update(event);}); // update the menu
        
        jQuery(document).click( function (event) { that.closeAllSubMenus(event); }); // close other menus when we click one
    },
    update: function (e) {
            var t = jQuery(e.target);          
            // don't follow link if first item, whose target is '<u>'
            if (t.hasClass('first-link') ) {
                e.preventDefault();
            }

            var sm = this.subMenu;
            var smStyle = this.subMenuStyle;
            var smState = sm.hasClass(smStyle)
            
            var m  = this.menu;
            var mStyle = this.menuStyle;


            // close other menus
            this.closeAllSubMenus(e); //Other
       
            
            
            if ( ! smState ) {
                sm.addClass(smStyle);
                m.addClass(mStyle);
            } else {
                sm.removeClass(smStyle);
                m.removeClass(mStyle);
            }
            e.stopPropagation(); // don't let default event occur (removeMenus from document.click)

            

    },
    hover: function () {
        jQuery(this).addClass('active-item');

    },
    unHover: function () {
        jQuery(this).removeClass('active-item');

    },
    closeAllSubMenus: function (event) {
        // for when user clicks on document
        var menuInstances = noext.Menu.menuInstances;
        for (var i = 0; i < menuInstances.length; i++) {
            var menuInstance = menuInstances[i]
            var subMenu = menuInstance.subMenu;
            var menu = menuInstance.menu;
            subMenu.removeClass(this.subMenuStyle);
            menu.removeClass(this.menuStyle);
        }
    }
};
// end of menu objecta


jQuery(document).ready( function () {
    jQuery('ul.noext-menu').removeClass('noext-menu-hidden').each( function () {
        noext.Menu.menuInstances.push( new noext.Menu( jQuery(this) ) );
    });
});