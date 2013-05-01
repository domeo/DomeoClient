$(function(){

  // sets up highlighted class
  $("#results strong").addClass("highlighted");
  
  // toggles highlighting
  $(".toggle_highlighting").click(function(){
    if($("#results strong").hasClass("highlighted")) {
      $("#results strong").removeClass("highlighted");
    }
    else {
      $("#results strong").addClass("highlighted");
    }
  });

  // sets up toggles
  $(".toggle_content").hide();
  $(".toggle").addClass("closed");
  placeToggleIcons();

  // toggle toggle content
  $(".toggle").click(function(){
    var toggleRel = $(this).attr("rel");
    $("#" + toggleRel).slideToggle("fast");
    if($(this).hasClass("open")) {
      $(this).removeClass("open").addClass("closed");
    }
    else {
      $(this).removeClass("closed").addClass("open");
    }    
    placeToggleIcons();
  });

  // drawer
  function autosizeDrawer(){
    var accordionHeight = $(".accordion").height();
    var drawerHeight = accordionHeight - 20;
    $(".drawer").css("height", drawerHeight + "px");
  }

  // open & closed icons
  function placeToggleIcons(){
    $(".toggle span").empty();
    $(".toggle.open").prepend("<span>&#x25BE;</span> ");
    $(".toggle.closed").prepend("<span>&#x25B8;</span> ");
  }
  function placeAccordionIcons(){
    $(".accordion .ui-accordion-header .ui-icon").empty();
    $(".accordion .ui-accordion-header .ui-icon-triangle-1-e").append("&#x25B8;");
    $(".accordion .ui-accordion-header .ui-icon-triangle-1-s").append("&#x25BE;");
    $(".accordion .ui-accordion-header .external").siblings().empty();
  }

  // sets up jQueryUI accordion
  $( ".accordion" ).accordion({
    collapsible: true,
    active: false,
    autoHeight: false,
    navigation: true,
    changestart: function(event, ui) {
      placeAccordionIcons();
    },
    change: function(event, ui) {
      placeAccordionIcons();
      autosizeDrawer();
    }
  });
  autosizeDrawer();
  placeAccordionIcons();
  
  // tipTip - tooltip
  $(".definition").tipTip({
    maxWidth: "400px"
  });

  $(".clinical-feature").tipTip({
    maxWidth: "800px",
    keepAlive: true
  });

  $(".sample-searches").tipTip({
    maxWidth: "800px",
    keepAlive: true
  });

  // show & hide drawer
  $(".floating-menu h3 a").click(function(){
    $(".drawer.shown").animate({
      marginLeft: "0"
    }).fadeOut().css("overflow", "hidden").removeClass("shown");
    $(".toggle_drawer.active").removeClass("active");
  });

  $(".toggle_drawer").click(function(){
    if($(this).hasClass("active")) {
      var drawerRel = $(this).attr("rel");
      $(this).removeClass("active");
      $("#" + drawerRel).animate({
        marginLeft: "0"
      }).fadeOut().css("overflow", "hidden").removeClass("shown");
    }
    else {
      var drawerRel = $(this).attr("rel");
      $(".drawer.shown").animate({
          marginLeft: "0"
      }).fadeOut().css("overflow", "hidden").removeClass("shown");
      $(".toggle_drawer").removeClass("active");
      $(this).addClass("active");

    if($(this).hasClass("details")) {
      $("#" + drawerRel).css({
        'position': 'fixed',
        'left': $('.floating-menu > div:first').offset().left,
        'top': $('.floating-menu > div:first').offset().top + 15
        });
       $("#" + drawerRel).fadeIn().animate({
        'left': $('.floating-menu > div:first').offset().left -
        $('.floating-menu > div:first').innerWidth() + 12,
        'top': $('.floating-menu > div:first').offset().top + 15
        }).css("overflow", "auto").addClass("shown");
    }
    else {
      $("#" + drawerRel).fadeIn().animate({
        marginLeft: "-180px"
      }).css("overflow", "auto").addClass("shown");
    }
   }
  });
  
//   $('.accordion').before("<form><label class=\"off\"><input type=\"checkbox\" /> <span>pin menu</span></label></form>");
//   $('.floating-menu label').click(function() {
//     if ($('.floating-menu input').attr('checked')) {
//       $(this).children('span').html('unpin menu');
//     }
//     else {
//       $(this).children('span').html('pin menu');
//     }
//   });
  // sidebar follows page scroll
//   $(window).scroll(function(){
//       $(".floating-menu").animate(
//         { top: $(window).scrollTop() + "px" },
//         { queue: false, duration: 200 }
//       );
//   });
  
  // adds hover class for drop-down menus in IE
  $(".nav>li").hover(
    function() { $(this).addClass("hover"); },
    function() { $(this).removeClass("hover"); }
  );

  // adds active classes to labels on page ready
  $(".advanced .checkbox input:checked").parent().addClass("active");

  // toggles active class based on checkbox state
  $(".advanced .checkbox input").change(function(){
    if ($(this).attr("checked"))
    {
      $(this).parent().addClass("active");
      return;
    }
    $(this).parent().removeClass("active");
  });

  // clears out active classes on form reset
  $(".advanced input[type='reset']").click(function(){
    $(".advanced .checkbox label").removeClass("active");  
    $(".advanced .checkbox input").removeAttr("checked");
    $(".advanced .input input").removeAttr("value");
    $(".advanced input#entrySearch").removeAttr("value");
    $(".advanced input#clinicalSynopsisSearch").removeAttr("value");
    $(".advanced input#geneMapSearch").removeAttr("value");
  });
  
  // sets up search result links module
  $(".dialog").dialog({
    autoOpen: false,
    closeText: "x",
    width: 215
//     width: 185
  });
  
  // opens search result links module
  $(".open_dialog").click(function(){
    var offset = $(this).offset();
    var dialogLeft = offset.left - 154;
    var dialogRel = $(this).attr("rel");
    $(".dialog").dialog("close");
    $("#" + dialogRel).dialog("option", "position", [dialogLeft,offset.top]);
    $("#" + dialogRel).dialog("open");
    return false;
  });

  // autofill configuration
  $(".autofill #homeSearch").autofill({
    value: "Search OMIM",
    activeTextColor: "#111",
    defaultTextColor: "#999"
  });
  
  // autofill configuration
  $(".autofill #entrySearch").autofill({
    value: "Search OMIM",
    activeTextColor: "#111",
    defaultTextColor: "#999"
  });
  
  // autofill configuration
  $(".autofill #clinicalSynopsisSearch").autofill({
    value: "Search clinical synopses",
    activeTextColor: "#111",
    defaultTextColor: "#999"
  });
  
  // autofill configuration
  $(".autofill #geneMapSearch").autofill({
    value: "Search gene map",
    activeTextColor: "#111",
    defaultTextColor: "#999"
  });
  
  // autofill configuration
//   $(".autofill #phenotypeMapSearch").autofill({
//     value: "Search phenotype map",
//     activeTextColor: "#111",
//     defaultTextColor: "#999"
//   });
  
  // contact form validation
  $("form[name='contact']").validate({
    rules: {
      email: "required",
      email2: {
        equalTo: "#email"
      }
    },
    messages:{
      lastName: ' Please enter your last name',
      email:    ' Please enter an email address',
      email2:   ' Please confirm your email address'
    }
  });

  // downloads form validation
  $("form[name='downloads']").validate({
    rules: {
      email: "required",
      email2: {
        equalTo: "#email"
      }
    },
    messages:{
      firstName:   ' Please enter your first name',
      lastName:    ' Please enter your last name',
      institution: ' Please enter your institution',
      email:       ' Please enter an email address',
      email2:      ' Please confirm your email address',
      accept:      ' Please accept',
      description: ' Please describe your research'
    }
  });

  // login form validation
  $("form[name='login']").validate({
    rules: {
      username: "required",
      password: "required"
    },
    messages:{
      username: ' Please enter your user name',
      password: ' Please enter your password'
    }
  });

    
  // Autocomplete
  $("#entrySearch").autocomplete({url: '/autocomplete', useCache: false, matchSubset: false, extraParams: {'index': 'entry'}});

  $("#clinicalSynopsisSearch").autocomplete({url: '/autocomplete', useCache: false, matchSubset: false, extraParams: {'index': 'clinicalSynopsis'}});

  $("#geneMapSearch").autocomplete({url: '/autocomplete', useCache: false, matchSubset: false, extraParams: {'index': 'geneMap'}});


});