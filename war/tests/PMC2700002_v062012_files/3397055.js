if (typeof(jQuery) != "undefined") {
  (function($j) {
      var formState = {
          overrideBackends: false,
          backends: {}
      };
      
      // Name of the cookie
      var cookieName;
      
      // Mostly just for debugging, store the cookie string value here
      // rather than in the sub-function scope
      var cookieStr;
      
      // An object representation of the cookie.  This is converted from the
      // XML cookie value on init.  The form controls will manipulate this,
      // and when the user clicks "Go", this will be converted back into
      // XML.
      var cookieObj;

      ///////////////////////////////////////////////////////////////////////////////
      function cbChanged(event) {
          //console.info("Event caught: " + event);
          var target = $j(event.target);
          var id = target.attr("id");
          var value = target.attr("value");
          var checked = target.attr("checked");
          /*console.info("target id: '" + id + 
                       "', value: '" + value + 
                       "', checked: '" + checked + "'");*/
          
          
          if (id == "besetsel-cb") {
              if (checked) {
                  $j("#besetsel-sel").removeAttr("disabled");
                  besetSelFormToObj();
              }
              else {
                  $j("#besetsel-sel").attr("disabled", 1);
                  delete cookieObj.besetName;
              }
          }
          else if (id == "besetsel-sel") {
              besetSelFormToObj();
          }
          else {
              var m;
              if (m = id.match(/besetsel-be-(.*?)-cb/)) {
                  var backend = m[1];
                  //console.info(">>>backend checkbox:  " + backend);
                  if (checked) {
                      $j("#besetsel-be-" + backend + "-text").removeAttr("disabled");
                      beUrlFormToObj(backend);
                  }
                  else {
                      $j("#besetsel-be-" + backend + "-text").attr("disabled", 1);
                      delete cookieObj.backendUrls[backend];
                  }
              }
              else if (m = id.match(/besetsel-be-(.*?)-text/)) {
                  backend = m[1];
                  //console.info(">>>backend text:  " + backend);
                  beUrlFormToObj(backend);
              }
          }
          
          // PMC-11784 and PMC-11785.
          // This fixes a nasty IE bug.  It causes a slight flash when the user
          // clicks a checkbox, but it works.
          if (jQuery.browser.msie){
              target.hide();
              window.setTimeout( function(){ target.show();}, 0 );
          }
          
      }

      ///////////////////////////////////////////////////////////////////////////////
      // besetSelFormToObj()
      // This is called by a couple of event handlers and decodes the
      // currently selected BESet (in the drop-down form) and sets the
      // cookieObj.besetName accordingly.

      function besetSelFormToObj()
      {
          $j("#besetsel-sel option").each(function() {
              var opt = $j(this);
              if (opt.attr("selected")) {
                  var id = opt.attr("id");
                  cookieObj.besetName = id.match(/besetsel-opt-(.*)/)[1];
              }
          });
      }

      ///////////////////////////////////////////////////////////////////////////////
      // beUrlFormToObj(backend)
      // This is similar, and takes care of reading the text value from the
      // form and stuffing it into the object

      function beUrlFormToObj(backend) {
          var value = $j("#besetsel-be-" + backend + "-text").attr("value");
          if (value) cookieObj.backendUrls[backend] = value;
      }

      ///////////////////////////////////////////////////////////////////////////////
      function init() {
          cookieName = $j("#besetsel-form").attr("cookieName");
          cookieObj = cookieXmlToJson(cookieName);
          initFormState();

          // Set event handers
          $j("#besetsel-form .besetsel-control").change(function(event) {
              cbChanged(event);
          });
          $j("#besetsel-go-button").click(function(event) {
              goButton(event);
          });
          $j("#besetsel-reset-button").click(function(event) {
              resetButton(event);
          });
          
          // This "pullout" might be empty, in the case of the BESet being
          // selected by path segment instead of cookie.  In that case, the
          // tab acts as a watermark, just to identify the BESet, and we
          // don't want to allow it to be "pulled out".  So we'll set the
          // width to 0 in that case.
          var w = $j("#besetsel-go-button").length > 0 ? "400px" : "0px";

          // Put it into the sidecontent pullout
          $j("#besetsel-form").sidecontent({
              classmodifier: "besetsel",
              attachto: "rightside",
              width: w,
              opacity: "0.8",
              pulloutpadding: "5",
              textdirection: "vertical",
              clickawayclose: 0,
              titlenoupper: 1
          });
          var pulloutColor = $j("#besetsel-form").attr("pulloutColor");
          //alert("color is " + pulloutColor);
          $j(".besetselpullout").css("background-color", pulloutColor);
          
          if ($j("#besetsel-go-button").length > 0) {
              $j(".besetselpullout").css({
                  "border-top": "ridge gray 5px",
                  "border-bottom": "ridge gray 5px",
                  "border-left": "ridge gray 5px"
              });
          }
      }

      ///////////////////////////////////////////////////////////////////////////////
      // goButton(event)
      // Handle the user-click of the "Go!" button.
      
      function goButton(event) {
          // Convert the object into XML
          var cookieXml = 
            "<BESet" +
            ( cookieObj.besetName ? (" name='" + cookieObj.besetName + "'>")
                                  : ">" );
          for (var backend in cookieObj.backendUrls) {
              //console.info("+++ backend " + backend);
              cookieXml += 
                "<BackendUrl backend='" + backend + "' " +
                "url='" + xmlEscape(cookieObj.backendUrls[backend]) + "'/>";
          }
          cookieXml += "</BESet>";
          //console.info(cookieXml);
          
          // Set the cookie
          document.cookie = cookieName + "=" + encodeURIComponent(cookieXml) +
                            "; max-age=604800" +
                            "; path=/" +
                            "; domain=nih.gov";
          // Reload the page
          window.location.reload();
      }
      
      ///////////////////////////////////////////////////////////////////////////////
      // resetButton(event)
      // Handle the user-click of the "Reset" button.
      // Does the same thing as "Go!", but sets the cookie to the empty string.

      function resetButton(event) {
          // Clear the cookie
          document.cookie = cookieName + "=" + 
                            "; max-age=604800" +
                            "; path=/" +
                            "; domain=nih.gov";
          // Reload the page
          window.location.reload();
      }
      
      ///////////////////////////////////////////////////////////////////////////////
      function xmlEscape(str) {
          str = str.replace(/\&/g, '&amp;')
                   .replace(/\</g, '&lt;')
                   .replace(/\>/g, '&gt;')
                   .replace(/\"/g, '&quot;')
                   .replace(/\'/g, '&apos;');
          return str;
      }

      ///////////////////////////////////////////////////////////////////////////////
      // This function reads the cookie value and initializes the form state
      // Don't assume anything about the form state -- redo everything.
      function initFormState() {

          var besetName = cookieObj.besetName;
          if (!besetName) {
              $j("#besetsel-cb").removeAttr("checked");
              $j("#besetsel-sel").attr("disabled", 1);
          }
          else {
              var selBESet = $j("#besetsel-opt-" + besetName);
              if (selBESet.length != 0) {
                  $j("#besetsel-cb").attr("checked", 1);
                  $j("#besetsel-sel").removeAttr("disabled");
                  selBESet.attr("selected", 1);
              }
              else {
                  $j("#besetsel-cb").removeAttr("checked");
                  $j("#besetsel-sel").attr("disabled", 1);
              }
          }
          
          // Foreach backend in the form
          $j(".besetsel-be-cb").each(function(i) {
              var id = $j(this).attr("id");
              var beName = id.match(/besetsel-be-(.*?)-cb/)[1];
              //console.info("### backend, id is '" + id + "', beName is '" + beName + "'");
              if (!beName) return;
              
              // See if there's a corresponding element in the cookie
              if (!cookieObj.backendUrls ||
                  !cookieObj.backendUrls[beName]) {
                  //console.info("Didn't find " + beName);
                  $j("#besetsel-be-" + beName + "-cb").removeAttr("checked");
                  $j("#besetsel-be-" + beName + "-text").attr("disabled", 1);
              }
              else {
                  //console.info("Found " + beName);
                  $j("#besetsel-be-" + beName + "-cb").attr("checked", 1);
                  var textbox = $j("#besetsel-be-" + beName + "-text");
                  textbox.removeAttr("disabled");
                  textbox.attr("value", cookieObj.backendUrls[beName]);
              }
          });
      }
      
      ///////////////////////////////////////////////////////////////////////////////
      // This gets the value of the <snapshot>_beset cookie, which is in XML, and turns it
      // from this:
      //   <BESet name='test'>
      //     <BackendUrl backend='tagserver' url='bingo'/>
      //     ...
      //   </BESet>
      // Into this (note that everything is optional):
      //   { besetName: 'test',
      //     backendUrls: {
      //         tagserver: 'bingo', ... }
      //   }
      // If there is no cookie set or parsing fails, this returns {}.
      
      function cookieXmlToJson(cookieName) {
          var cookieObj = {
              backendUrls: {}
          };

          cookieStr = getCookie(cookieName);
          //console.info("cookie value is '" + cookieStr + "'");

          // Parse XML
          try {
              var cookieXml = $j(cookieStr);
          }
          catch(err) {
              return cookieObj;
          }
          
          var besetElem = cookieXml;
          if (besetElem.length == 0) {
              // No valid cookie value found.
              return cookieObj;
          }
          var besetName = besetElem.attr("name");
          if (besetName) {
              cookieObj.besetName = besetName; 
          }
          
          var backends = besetElem.find("BackendUrl");
          if (backends.length != 0) {
              backends.each(function (i) {
                  var e = $j(backends[i]);
                  cookieObj.backendUrls[e.attr("backend")] = e.attr("url");
                  //console.info("Setting " + e.attr("backend") + ": " + e.attr("url"));
              })
          }
          
          return cookieObj;
      }

      ///////////////////////////////////////////////////////////////////////////////
      function getCookie(name) {
          var allCookies = document.cookie;
          //console.info("allCookies = " + allCookies);
          var pos = allCookies.indexOf(name + "=");
          if (pos != -1) {
              var start = pos + (name + "=").length;
              var end = allCookies.indexOf(";", start);
              if (end == -1) end = allCookies.length;
              return decodeURIComponent(allCookies.substring(start, end)); 
          }
          return "";
      }
        
      ///////////////////////////////////////////////////////////////////////////////
      $j(document).ready(init);
  })(jQuery);

}



;
// This script was written by Steve Fenton
// http://www.stevefenton.co.uk/Content/Jquery-Side-Content/
// Feel free to use this jQuery Plugin
// Version: 3.0.2

// *** Modified slightly by Chris Maloney
//  - Added a config switch to let me turn off conversion of the title
//    to uppercase (config.titlenoupper)
//  - Note that this doesn't work well with two sliders on the same page.
//    For example, you can't have one slider with "clickawayclose" and one
//    slider that does not.

(function($)
{
  
  var classModifier = "";
  var sliderCount = 0;
  var sliderWidth = "400px";
  
  var attachTo = "rightside";
  
  var totalPullOutHeight = 0;
  
  function CloseSliders (thisId) {
    // Reset previous sliders
    for (var i = 0; i < sliderCount; i++) {
      var sliderId = classModifier + "_" + i;
      var pulloutId = sliderId + "_pullout";
      
      // Only reset it if it is shown
      if ($("#" + sliderId).width() > 0) {

        if (sliderId == thisId) {
          // They have clicked on the open slider, so we'll just close it
          showSlider = false;
        }

        // Close the slider
        $("#" + sliderId).animate({
          width: "0px"
        }, 100);
        
        // Reset the pullout
        if (attachTo == "leftside") {
          $("#" + pulloutId).animate({
            left: "0px"
          }, 100);
        } else {
          $("#" + pulloutId).animate({
            right: "0px"
          }, 100);
        }
      }
    }
  }
  
  function ToggleSlider () {
  
    var rel = $(this).attr("rel");

    var thisId = classModifier + "_" + rel;
    var thisPulloutId = thisId + "_pullout";
    var showSlider = true;
    
    if ($("#" + thisId).width() > 0) {
      showSlider = false;
    }
    
    CloseSliders(thisId);
    
    if (showSlider) {
      // Open this slider
      $("#" + thisId).animate({
        width: sliderWidth
      }, 250);
      
      // Move the pullout
      if (attachTo == "leftside") {
        $("#" + thisPulloutId).animate({
          left: sliderWidth
        }, 250);
      } else {
        $("#" + thisPulloutId).animate({
          right: sliderWidth
        }, 250);
      }
    }

    return false;
  };

  $.fn.sidecontent = function (settings) {

    var config = {
      classmodifier: "sidecontent",
      attachto: "rightside",
      width: "300px",
      opacity: "0.8",
      pulloutpadding: "5",
      textdirection: "vertical",
      clickawayclose: false
    };

    if (settings) {
      $.extend(config, settings);
    }
    
    return this.each(function () {
    
      $This = $(this);
      
      // Hide the content to avoid flickering
      $This.css({ opacity: 0 });
      
      classModifier = config.classmodifier;
      sliderWidth = config.width;
      attachTo = config.attachto;
      
      var sliderId = classModifier + "_" + sliderCount;
      var sliderTitle = config.title;
      
      // Get the title for the pullout
      sliderTitle = $This.attr("title");
      
      // Start the totalPullOutHeight with the configured padding
      if (totalPullOutHeight == 0) {
        totalPullOutHeight += parseInt(config.pulloutpadding);
      }

      if (config.textdirection == "vertical") {
        var newTitle = "";
        var character = "";
        for (var i = 0; i < sliderTitle.length; i++) {
          character = sliderTitle.charAt(i)
          if (!config.titlenoupper) character = character.toUpperCase();

          if (character == " ") {
            character = "&nbsp;";
          }
          newTitle = newTitle + "<span>" + character + "</span>";
        }
        sliderTitle = newTitle;
      }
      
      // Wrap the content in a slider and add a pullout     
      $This.wrap('<div class="' + classModifier + '" id="' + sliderId + '"></div>')
           .wrap('<div style="width: ' + sliderWidth + '"></div>');
      $("#" + sliderId).before('<div class="' + classModifier + 'pullout" id="' + 
                               sliderId + '_pullout" rel="' + sliderCount + '">' + 
                               sliderTitle + '</div>');
      
      if (config.textdirection == "vertical") {
        $("#" + sliderId + "_pullout span").css({
          display: "block",
          textAlign: "center"
        });
      }
      
      // Hide the slider
      $("#" + sliderId).css({
        position: "absolute",
        overflow: "hidden",
        top: "0",
        width: "0px",
        zIndex: "1000",
        opacity: config.opacity
      });
      
      // For left-side attachment
      if (attachTo == "leftside") {
        $("#" + sliderId).css({
          left: "0px"
        });
      } else {
        $("#" + sliderId).css({
          right: "0px"
        });
      }
      
      // Set up the pullout
      $("#" + sliderId + "_pullout").css({
        position: "absolute",
        top: totalPullOutHeight + "px",
        zIndex: "1000",
        cursor: "pointer",
        opacity: config.opacity
      })
      
      $("#" + sliderId + "_pullout").live("click", ToggleSlider);
      
      var pulloutWidth = $("#" + sliderId + "_pullout").width();
      
      // For left-side attachment
      if (attachTo == "leftside") {
        $("#" + sliderId + "_pullout").css({
          left: "0px",
          width: pulloutWidth + "px"
        });
      } else {
        $("#" + sliderId + "_pullout").css({
          right: "0px",
          width: pulloutWidth + "px"
        });
      }
      
      totalPullOutHeight += parseInt($("#" + sliderId + "_pullout").height());
      totalPullOutHeight += parseInt(config.pulloutpadding);
      
      var suggestedSliderHeight = totalPullOutHeight + 30;
      if (suggestedSliderHeight > $("#" + sliderId).height()) {
        $("#" + sliderId).css({
          height: suggestedSliderHeight + "px"
        });
      }
      
      if (config.clickawayclose) {
        $("body").click( function () {
          CloseSliders("");
        });
      }
      
      // Put the content back now it is in position
      $This.css({ opacity: 1 });
      
      sliderCount++;
    });
    
    return this;
  };
})(jQuery);


;
/* Override this file with one containing code that belongs on every page of your application */


;
// See PMC-7567 - Google suggestions
// This is an AJAX implementation of functionality that already exists in the backend.
// This Portal/AJAX implementation overrides the backend implementation.
//
// This JS looks for a span with id "esearch-result-number".  If it exists,
// then it does an ajax call to esearch (at the url specified in the @ref
// attribute) and gets the count.  It then replaces the contents of this
// span with that count.  Finally, it shows the outer div.
//
// An example of an esearch query url is
//     /entrez/eutils/esearch.fcgi?term=unemployed&db=pmc&rettype=count&itool=QuerySuggestion
// which would return something like this:
//   <eSearchResult>
//     <Count>10573</Count>
//   </eSearchResult>
//
// PMC-11350 - itool=QuerySuggestion query parameter is required to filter
// out such queries while calculating statistics.

jQuery(document).ready(
    function() {
        var $ = jQuery;

        $("#esearch-result-number").each(
            function() {
                var countSpan = $(this);
                var esearchUrl = countSpan.attr("ref");
                if (esearchUrl.length > 0) {
                
                    $.ajax({
                        type: "GET",
                        url: esearchUrl,
                        dataType: "xml",
                        success: function(xml) {
                            $(xml).find('Count').each(function(){
                                var count = $(this).text();

                                // Insert the count into the element content,
                                // and show the outer div.
                                countSpan.text(count);
                                $("div#esearchMessageArea").show();
                            });
                        }
                    }); 

                }
            }
        );
    }
);


;
// See BK-5746.
// /sandbox/cfm/Components/CFM_NCBISearchBarJS - copied from 
// /projects/PAF/BaseComponents/Support/NCBISearchBarJS 1.8

// This code uses JavaScript to build search URL and send search request directly to the database
// that is asking for the request. This works only for resources at a top-level URL.
// If JavaScript is turned off, or a redirect loop would occur, the search form's HTTP GET is
// allowed to continue.

if (typeof(jQuery) != 'undefined') {
    (function($) {
        // BK-5746.
        // This is the default function that returns the search URL
        // You can override this by defining NCBISearchBar_searchUrl.
        var defaultSearchUrl = function() {
            var db = $('#entrez-search-db');
            var term = $('#term');
            if (db && term && db[0] && term[0]) {
                var scriptUrl = "/" + db[0].value + "/";
                var termParam = 
                    (term[0].value.replace(/^\s+/,'').length != 0) ?
                        "?term=" + encodeURIComponent(term[0].value) : 
                        "";
                return scriptUrl + termParam;
            }
        }
        
        function searchUrl() {
            // If the user has overridden the URL function:
            var url = "";
            if (typeof(NCBISearchBar_customSearchUrl) != "undefined") {
                url = NCBISearchBar_customSearchUrl();
            }
            if (!url) {
                url = defaultSearchUrl();
            }
            return url;
        }

        $(document).ready(function() {

            var db = $('#entrez-search-db');
            var term = $('#term');

            db.removeAttr('disabled'); // Reenable if this is backbutton
            
            // BK-5746:  this fixes the problem first identified in PMH-673.
            // Focus the text box, but only if url has no hash
            /*
              PMC-14231 - Removed this code.  Setting the focus is handled by
              ncbiautofocus.
              if (window.location.hash.length < 2) {
                  term.focus();
              }
            */

            $('#entrez-search-form').submit(function(e) {
                var form = $('#entrez-search-form');
                var appname = $('#ncbisearchbar-app-name')[0].value;

                // Disable crap portal-injected parameters
                $('input[type="hidden"][name^="p$"]', form).each(function() {
                    $(this).attr('disabled', 'disabled');
                });

                if (db && term && db[0] && term[0]) {
                    // Avoid redirect loop if running subsequent search in same db
                    if (db[0].value == appname) {
                        db.attr('disabled','true');
                        return true;
                    } 
                    else {
                        e.stopPropagation();
                        e.preventDefault();
                        var url = searchUrl();
                        if (url) {
                            window.location = url;
                            return false;
                        }
                    }
                }

                // Don't redirect if there would be a loop or there is some other problem
                return true;

            }) // End submit function
        }); // End document.ready
    })(jQuery); // Close scope
};




;
(function( $ ){ // pass in $ to self exec anon fn

    // on page ready
    $( function() {
    
        $( 'div.portlet' ).each( function() {

            // get the elements we will need
            var $this = $( this );
            var anchor = $this.find( 'a.portlet_shutter' );
            var portBody = $this.find( 'div.portlet_content' );

            // we need an id on the body, make one if it doesn't exist already
            // then set toggles attr on anchor to point to body
            var id = portBody.attr('id') || $.ui.jig._generateId( 'portlet_content' );
            portBody.attr('id', id );
            anchor.attr('toggles', id );

            // initialize jig toggler with proper configs, then remove some classes that interfere with 
            // presentation
            var togglerOpen = anchor.hasClass('shutter_closed')? false : true; 
            anchor.ncbitoggler({
                isIcon: false,
                initOpen: togglerOpen 
            }).
                removeClass('ui-ncbitoggler-no-icon').
                removeClass('ui-widget');

            // get rid of ncbitoggler css props that interfere with portlet styling, this is hack
            // we should change how this works for next jig release
            anchor.css('position', 'absolute').
                css('padding', 0 );

            $this.find( 'div.ui-helper-reset' ).
                removeClass('ui-helper-reset');

            portBody.removeClass('ui-widget').
                css('margin', 0);

            // trigger an event with the id of the node when closed
            anchor.bind( 'ncbitogglerclose', function() {
                anchor.addClass('shutter_closed');
            });

            anchor.bind('ncbitoggleropen', function() {
                anchor.removeClass('shutter_closed');
            });

        });  // end each loop          
    });// end on page ready
})( jQuery );
/*
jQuery(document).bind('ncbitogglerclose ncbitoggleropen', function( event ) {
           var $ = jQuery;
           var eventType = event.type;
           var t = $(event.target);
           
          alert('event happened ' + t.attr('id'));
   
           if ( t.hasClass('portlet_shutter') || false ) { // if it's a portlet
               // get the toggle state
               var sectionClosed = (eventType === 'ncbitogglerclosed')? 'true' : 'false';
               alert ('now call xml-http');

            }
        });
*/

Portal.Portlet.NCBIPageSection = Portal.Portlet.extend ({
	init: function (path, name, notifier){
		this.base (path, name, notifier);
		
		this.AddListeners();
	},
    
	"AddListeners": function(){
        var oThis = this;
        
		jQuery(document).bind('ncbitogglerclose ncbitoggleropen', function( event ) {
            var $ = jQuery;
            var eventType = event.type;
            var t = $(event.target);
            
            // proceed only if this is a page section portlet {
            if ( t.hasClass('portlet_shutter')){
                var myid = '';
                if (oThis.getInput("Shutter")){
                    myid = oThis.getInput("Shutter").getAttribute('id');
                }
    
                // if the event was triggered on this portlet instance
                if (t.attr('id') && t.attr('id') == myid){
                    // get the toggle state
                    var sectionClosed = (eventType === 'ncbitogglerclose')? 'true' : 'false';
                    // react to the toggle event
                    oThis.ToggleSection(oThis.getInput("Shutter"), sectionClosed);
                }
            } // if portlet            
        });
	},
	
	"ToggleSection": function(target, sectionClosed){
	   // if remember toggle state, save the selection and log it
	   if (target.getAttribute('remembercollapsed') == 'true'){
	       this.UpdateCollapsedState(target, sectionClosed);
	   }else {
	       this.LogCollapsedState(target, sectionClosed);
	   }
	},
	
	"UpdateCollapsedState": function(target, sectionClosed){
	    var site = document.forms[0]['p$st'].value;
	    var args = { "PageSectionCollapsed": sectionClosed, "PageSectionName": target.getAttribute('pgsec_name')};
	    // Issue asynchronous call to XHR service
        var resp = xmlHttpCall(site, this.getPortletPath(), "UpdateCollapsedState", args, this.receiveCollapse, {}, this);  
	},
	
	"LogCollapsedState": function(target, sectionClosed){
	    var site = document.forms[0]['p$st'].value;
	    // Issue asynchronous call to XHR service
        var resp = xmlHttpCall(site, this.getPortletPath(), "LogCollapsedState", {"PageSectionCollapsed": sectionClosed}, this.receiveCollapse, {}, this);  
	},
	
	'getPortletPath': function(){
        return this.realname;
    }, 
    
    receiveCollapse: function(responseObject, userArgs) {
    }
	
});
		 
;
Portal.Portlet.LinkListPageSection = Portal.Portlet.NCBIPageSection.extend ({
	init: function (path, name, notifier){
		this.base (path, name, notifier);
	},
	
	"getPortletPath" : function(){
	    return (this.realname + ".NCBIPageSection");
	}
});
;
(function( $ ){ // pass in $ to self exec anon fn
    // on page ready
    $( function() {
        $('li.brieflinkpopper').each( function(){
            var $this = $( this );
            var popper = $this.find('a.brieflinkpopperctrl') ;
            var popnode = $this.find('div.brieflinkpop');
            var popid = popnode.attr('id') || $.ui.jig._generateId('brieflinkpop');
            popnode.attr('id', popid);
            popper.ncbipopper({
                destSelector: "#" + popid,
                destPosition: 'top right', 
                triggerPosition: 'middle left', 
                hasArrow: true, 
                arrowDirection: 'right',
                isTriggerElementCloseClick: false,
                adjustFit: 'none',
                openAnimation: 'none',
                closeAnimation: 'none',
                delayTimeout : 130
            });
        }); // end each loop  
    });// end on page ready
})( jQuery );

Portal.Portlet.BriefLinkPageSection = Portal.Portlet.LinkListPageSection.extend({

	init: function(path, name, notifier) {
	    console.info("Created BriefLinkPageSection");
		this.base(path, name, notifier);
	},
	
	"getPortletPath" : function(){
	    return (this.realname + ".LinkListPageSection.NCBIPageSection");
	}
	
});
;
Portal.Portlet.PMCVPubmedRelatedArticlesP = Portal.Portlet.BriefLinkPageSection.extend({

    init: function(path, name, notifier) {
        console.info("Created PMCVPubmedRelatedArticlesP");
        this.base(path, name, notifier);
    },

    /*SetPortletName: function(){
        Portal.Portlet.BriefLinkPageSection.portletname = 'PPMCPubmedRA';
    },*/

    'getPortletPath': function(){
        return this.realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection";
    }

    /*popNodeDelay: 500*/
});


;
Portal.Portlet.PMCVCitedByPmcArticlesP = Portal.Portlet.BriefLinkPageSection.extend({

    init: function(path, name, notifier) {
        console.info("Created PMCVCitedByPmcArticlesP");
        this.base(path, name, notifier);
    },

    /*SetPortletName: function(){
        Portal.Portlet.BriefLinkPageSection.portletname = 'PPMCPubmedRA';
    },*/

    'getPortletPath': function(){
        return this.realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection";
    }

    /*popNodeDelay: 500*/
});




;
Portal.Portlet.DiscoveryDbLinks = Portal.Portlet.BriefLinkPageSection.extend({
    
    init: function(path, name, notifier) {
		this.base(path, name, notifier);
	},
	
	"getPortletPath" : function(){
	    return (this.realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection");
	}
});
;
Portal.Portlet.PMCVDiscoveryDbLinksP = Portal.Portlet.DiscoveryDbLinks.extend({
    
  init: function(path, name, notifier) {
    this.base(path, name, notifier);
  },
  
  'getPortletPath': function(){
        return this.realname + ".DiscoveryDbLinks.BriefLinkPageSection.LinkListPageSection.NCBIPageSection";
    }
});




;
(function( $ ){ // pass in $ to self exec anon fn
    // on page ready
    $( function() {
        $('li.ralinkpopper').each( function(){
            var $this = $( this );
            var popper = $this;
            var popnode = $this.find('div.ralinkpop');
            var popid = popnode.attr('id') || $.ui.jig._generateId('ralinkpop');
            popnode.attr('id', popid);
            popper.ncbipopper({
                destSelector: "#" + popid,
                destPosition: 'top right', 
                triggerPosition: 'middle left', 
                hasArrow: true, 
                arrowDirection: 'right',
                isTriggerElementCloseClick: false,
                adjustFit: 'none',
                openAnimation: 'none',
                closeAnimation: 'none',
                delayTimeout : 130
            });
        }); // end each loop  
    });// end on page ready
})( jQuery );

Portal.Portlet.HistoryDisplay = Portal.Portlet.NCBIPageSection.extend({

	init: function(path, name, notifier) {
		console.info("Created History Ad...");
		this.base(path, name, notifier);    
	},
	
	send: {
      'Cmd': null      
    },   
    
    receive: function(responseObject, userArgs) {  
         var cmd = userArgs.cmd;
         var rootNode = document.getElementById('HTDisplay'); 
         var ul = document.getElementById('activity');
         var resp = responseObject.responseText;
             
         if (cmd == 'HTOn') { 
            rootNode.className = '';    // hide all msg and the turnOn link
            try {
            //alert(resp);
                // Handle timeouts
                if (responseObject.status == 408) { 
                    rootNode.className = 'HTOn'; // so that the following msg will show up
                    rootNode.innerHTML = "<p class='HTOn'>Your browsing activity is temporarily unavailable.</p>";
                    return;
                }
                   
                 // Looks like we got something...
                 resp = '(' + resp + ')';
                 var JSONobj = eval(resp);
                 
                 // Build new content (ul)
                 var newHTML = JSONobj.Activity;
                 var newContent = document.createElement('div');
                 newContent.innerHTML = newHTML;
                 var newUL = newContent.getElementsByTagName('ul')[0];
                 //alert(newHTML);
                 //alert(newContent.innerHTML);
                 //alert(newUL.innerHTML);
                 // Update content
                 rootNode.replaceChild(newUL, ul);
                 //XHR returns no activity (empty ul), e.g. activity cleared
                 if (newUL.className == 'hide')                     
                     rootNode.className = 'HTOn';  // show "Your browsing activity is empty." message
                 
            }         
            catch (e) {
                //alert('error');
                rootNode.className = 'HTOn'; // so that the following msg will show up
                rootNode.innerHTML = "<p class='HTOn'>Your browsing activity is temporarily unavailable.</p>";
           }
         }
         else if (cmd == 'HTOff') {                         
             if (ul != null) { 
                 ul.className='hide'; 
                 ul.innerHTML = ''; // clear activity
             }
             rootNode.className = 'HTOff';    // make "Activity recording is turned off." and the turnOn link show up             
         }
         else if (cmd == 'ClearHT') { 
             var goAhead = confirm('Are you sure you want to delete all your saved Recent Activity?');
             if (goAhead == true) { 
                 if ( rootNode.className == '') { //                 
                     rootNode.className = 'HTOn';  // show "Your browsing activity is empty." message                                  
                     if (ul != null) {
                         ul.className='hide'; 
                         ul.innerHTML = '';
                     }
                 }
             }
         } 
         
    },
    
	listen: {
	  'Cmd' : function(sMessage, oData, sSrc){
			console.info("Inside Cmd in HistoryDisplay: " + oData.cmd);
			this.setValue("Cmd", oData.cmd);
	  },	  
		
      "HistoryToggle<click>" : function(e, target, name){
         //alert(target.getAttribute("cmd"));
         this.send.Cmd({'cmd': target.getAttribute("cmd")});         
         console.info("Inside HistoryToggle in HistoryDisplay: " + target.getAttribute("cmd"));
         
         //var site = document.forms[0]['p$st'].value;
         var cmd =  target.getAttribute("cmd");     
               
         // Issue asynchronous call to XHR service, callback is to update the portlet output            
         this.doRemoteAction(target.getAttribute("cmd"));                      
      }, 
      
      "HistoryOn<click>" : function(e, target, name){
         this.send.Cmd({'cmd': target.getAttribute("cmd")});
         //$PN('Pubmed_ResultsSearchController').getInput('RecordingHistory').value = 'yes';		 
         console.info("Inside HistoryOn in HistoryDisplay: " + target.getAttribute("cmd"));
         this.doRemoteAction(target.getAttribute("cmd"));         
      },
      
      "ClearHistory<click>" : function(e, target, name){
         this.send.Cmd({'cmd': target.getAttribute("cmd")});
         this.doRemoteAction(target.getAttribute("cmd"));         
      }
    },
    
    'getPortletPath': function(){
        return this.realname + ".NCBIPageSection";
    }, 
    
    'doRemoteAction': function(command) {
         var site = document.forms[0]['p$st'].value;          
	     var resp = xmlHttpCall(site, this.realname, command, {}, this.receive, {'cmd': command}, this);
    }
});
