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
            
            var myid = '';
            if (oThis.getInput("Shutter")){
                myid = oThis.getInput("Shutter").getAttribute('id');
            }

            // if the event was triggered on this portlet instance
            if (t.attr('id') == myid){
                // get the toggle state
                var sectionClosed = (eventType === 'ncbitogglerclose')? 'true' : 'false';
                // react to the toggle event
                oThis.ToggleSection(oThis.getInput("Shutter"), sectionClosed);
            }
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
/*
  From a Portal/JS vantage, we are subclassing NCBIPageSection, even though 
  this portlet doesn't actually extend that one.
*/

Portal.Portlet.NCBIMultiPageSection = Portal.Portlet.NCBIPageSection.extend ({

    init: function(path, name, notifier) {
        this.base(path, name, notifier);
    },
    
    "getPortletPath" : function(){
        return (this.realname);
    },

    "AddListeners": function() {
        var oThis = this;

        /*
          Add a listener event for the ncbitogglerclose and ncbitoggleropen events.
          Note that this get's called whenever these events are triggered, even for
          completely unrelated toggler objects.  So we need to check that the object
          it was called on matches one of ours.
        */
        jQuery(document).bind('ncbitogglerclose ncbitoggleropen', function( event ) {
            var $ = jQuery;
            var eventType = event.type;
            var t = $(event.target);
            var targetId = t.attr('id');
            if (!targetId) return;

            // Get the total number of sections from the hidden form field
            var numSections = oThis.getValue('numSections');
            //console.info("numSections = " + numSections);

            for (var i = 1; i <= numSections; ++i) {
                var shutterNode = oThis.getInput("Shutter" + i);
                if (shutterNode && typeof(shutterNode) == "object") {
                    var shutterId = shutterNode.getAttribute('id');
                    if (targetId == shutterId) {
                        // get the toggle state
                        var sectionClosed = (eventType === 'ncbitogglerclose') ? 'true' : 'false';
                        // react to the toggle event
                        oThis.ToggleSection(shutterNode, sectionClosed);
                        // Found the one we want, let's get out.
                        return;
                    }
                }
            }
        });
    }

});

;
Portal.Portlet.LinkListPageSection = Portal.Portlet.NCBIMultiPageSection.extend ({
  init: function (path, name, notifier){
    this.base (path, name, notifier);
  },
  
  "getPortletPath" : function() {
      return (this.realname + ".NCBIPageSection");
  }
});


;
/*
  PMCVCitedRefBlocksJS
  
  This code is adapted from the JS that Aaron Cohen wrote, in PPMCArticlePageJS.
  I (cfm) use the term CRB (cited reference block) to refer to the portlets on
  an article page that are aligned with the paragraphs.
  
  This module performs two main functions:
  - Initializes and manages the CRBs, and the links within them.
    These come as div.pmc_para_cit elements from the backend.
  - Initializes and manages the poppers on the links within the
    body of the article (called body links).  There are two types:
      - Body links that have a corresponding CRB link.  These will,
        for the most part, correspond to citations to articles that
        are in PubMed. In these cases, the popper text gets cloned
        from the CRB links.
      - Body links that do not have a corresponding CRB link.  These
        are mostly citations that don't have a corresponding PubMed
        entry.  In these cases, the popper text gets cloned from the
        references section.

  CRB initial state:
  
    The CRBs should all be rendered on the page in their fully-expanded
    states, in the discovery column.  They should appear below any "fixed"
    portlets -- which are portlets of the normal kind, that appear 
    one-after-another at the top of the disco column.  They should have no
    special positioning.

    They should all have the class pmc_para_cit.  In the CSS, we initially
    set the visibility of these to "hidden", to prevent the flash of unstyled
    content.
      
  For reference, here are the jQuery data items that we save with the
  DOM elements:
  
    - CRB (div.pmc_para_cit)
        - myParagraph - reference to the jQuery wrapper object for the
          paragraph corresponding to this CRB.
        - myTop - integer - top of this CRB.  This is constant and is
          set during initialization.  If there's no para corresponding
          to this CRB (edge case), then this will be 0.
        - expandedHeight - int
        - collapsedHeight - int
        - collapsedNumLinks - int
        - isCollapsed - boolean
        - autoCollapseTimerId - int

    - Links in a CRB (div.pmc_para_cit li):
        - myCrb - ref. to the jQuery object for this link's containing CRB.
        - expandedHeight - int
        - myBodyLinks - reference to the jQuery object that holds the
          (one-to-many) body links that correspond to the *same citation*
          as this CRB link.  Note that this is *not* the complement of the
          body link's myCrbLink data member.  Here's the difference.  For
          a body link to have a myCrbLink pointing to this CRB link, it 
          must be in the paragraph corresponding to this CRB.  But, the
          CRB link's myBodyLinks lists *all* of the links in the body,
          regardless of paragraph, that correspond to the same citation.
          
        - isExtra - boolean.
        - isCollapsed - boolean

    - Body links:
        - myCrbLink - reference to the jQuery object for the
          CRB <li> corresponding to this citation link in the body.
        - ncbipopper - reference to the jQuery object for this link's
          popper.
*/


// We use an IIFE in order to not pollute the global namespace.
( function($j) {

    // This constant can be set to control the maximum number of
    // links ever to show in a CRB.  But note that this is already 
    // coded in the xslt that generates the portlet on the server.
    // So we probably don't need this.  Anyway, it might be nice to
    // have.  So I'll keep it and set it to a high value.
    var MAX_LINKS_PER_CRB = 50;

    // Specify a time delay before the CRB will "auto-collapse".
    var AUTOCOLLAPSE_TIMEOUT = 4000; 

    // Attach the initialize() function to the window load event.
    // I used to think this was only necessary for Safari, but see 
    // PMC-14368, and this SE post:  
    // http://stackoverflow.com/questions/544993/official-way-to-ask-jquery-wait-for-all-images-to-load-before-executing-somethin
    $j(window).load(initialize);

    //-------------------------------------------------------------
    // This initialize function runs when the DOM is ready.
    // All the other function definitions are nested inside this one,
    // so that they close over the local variables that are initialized
    // here.
    
    function initialize() {
        // Get the list of all CRBs in the document
        var crbs = $j('div.pmc_para_cit');
        var numCrbs = crbs.length;


        // We need to hide all CRBs that interfere with one of the main 
        // discovery portlets.  We'll assume that these main portlets all 
        // appear at the top.  So we have to find out the bottom-most 
        // boundary of all of these.  This calculation is in a function,
        // because it gets re-computed every time one of those fixed
        // portlets expands or collapses.

        var fixedPortlets = $j('div.portlet').not('div.pmc_para_cit');
        var mainPortletsBottom = getMainPortletsBottom();
        
        // Attach a handler to all of these fixed portlets, that gets activate
        // whenever a shutter button clicks.  Since the shutter button click
        // causes the size to change, we have to re-hide/show CRBs as necessary.
        fixedPortlets.find("a.portlet_shutter")
            .click(fixedPortletShutterClick);

        // These will store the values of the inner and outer height
        // of a 'collapsed' (two_line) link.  But note that since the links
        // are initially in the expanded state, we have to defer grabbing these
        // values until the first time we collapse one of them.  
        // (linkHeight == -1) marks that we haven't gotten the values yet.
        var linkHeight = -1;
        var linkOuterHeight;
        
        // This points to the current expanded CRB, or to null if there isn't one.
        var currentExpandedCrb = null;

        // Set the hoverintent mouseover event handlers for all <li>s.
        // I tweaked sensitivity and interval so that it's not quite so 
        // sensitive -- I think otherwise users are likely to get annoyed.
        crbs.find('li').hoverIntent({
            over: hoverOverCrbLink, 
            out: hoverOutCrbLink,
            sensitivity: 1,
            interval: 150
        });

        // Set a mouse-over event on all CRBs to control an automatic timer.
        // The timer causes the CRB to re-collapse if the user hasn't had her
        // mouse over it for a while.
        crbs.mouseover(clearAutoCollapseTimerHandler);
        crbs.mouseleave(setAutoCollapseTimerHandler);

        // Set up the CRB loop driver.  The CRBs are initialized a few at a time in
        // a loop that's called once a millisecond.  This progressive rendering
        // improves responsiveness when the page first loads.
        
        // This is a pointer into the array of CRBs, that's used by the driver loop.
        var crbNum = 0;
        // How many CRBs to process with each iteration of the driver loop.
        var CRB_BATCH_SIZE = 3;

        // Next add poppers for every link in the body of the text.  This also
        // uses a driver loop.  Set it up here.  
        
        // We're interested in all links that have 'cite-reflink' class, and that
        // have a @rid attribute.
        var allBodyLinks = $j('a.cite-reflink[rid]');
        var numBodyLinks = allBodyLinks.length;
        // This acts as a pointer into the array of links, that's used by the
        // driver loop.
        var bodyLinkNum = 0;
        // How many links to process with each iteration of the driver loop.
        var BODY_LINK_BATCH_SIZE = 10;
        // This is the container div for the poppers that we'll create.  This
        // should be present in the document from the server.
        var blPopperDiv = $j('#body-link-poppers');
        
        // We'll keep track of all the poppers we create, indexed by the @rid
        // value of the link.  That way, we're reuse duplicate poppers.
        var blPoppers = {};

        // The driver loops are chained together.  The order should not matter.
        // At the end of both driver loops, then we set hoverIntent event handlers 
        // on the body links.  This step must be performed last.  That's because 
        // both the ncbipopper (set up by the body link driver loop) and the CRB 
        // functionality (which sets hover events to highlight the text and the 
        // corresponding links in the CRB) use hoverIntent; so the events 
        // conflict.  To fix it, I put calls to the ncbipopper open() and 
        // close() methods at the end of the CRB hover event handlers, thus 
        // chaining the handlers together. 
        crbDriverLoop();


        //-----------------------------------------------------------------
        // Here is the CRB driver loop, which handles a few CRBs, and then
        // queues itself up to execute again after one ms.
        function crbDriverLoop() {
            for (var i = 0; i < CRB_BATCH_SIZE; ++i) {
                if (crbNum >= numCrbs) break;
                positionOneCrb($j(crbs[crbNum]));
                crbNum++;
            }
            // Set up to call ourselves after one millisecond
            if (crbNum < numCrbs) {
                setTimeout(crbDriverLoop, 1);
            }
            // When this driver loop is done, it daisy-chains to the
            // body-link loop
            else {
                setTimeout(bodyLinkDriverLoop, 1);
            }
        };

        //-----------------------------------------------------------------
        // This is a similar driver loop for setting the poppers on all
        // of the body links.
        function bodyLinkDriverLoop() {
            for (var i = 0; i < BODY_LINK_BATCH_SIZE; ++i) {
                if (bodyLinkNum >= numBodyLinks) break;
                addBodyLinkPopper($j(allBodyLinks[bodyLinkNum]));
                bodyLinkNum++;
            }
            // Set up to call ourselves after one millisecond
            if (bodyLinkNum < numBodyLinks) {
                setTimeout(bodyLinkDriverLoop, 1);
            }
            // When this driver loop is done, it invokes a function that
            // sets all of the custom hoverIntent handlers on the body links.
            else {
                setTimeout(setBodyLinkHoverHandlers, 1);
            }
        }

        //-----------------------------------------------------------------
        // This sets the hoverIntent handlers on all body links.  As mentioned
        // above, this must be done last.
        function setBodyLinkHoverHandlers() {
            $j('a.cite-reflink[rid]')
                .hoverIntent({
                    over: hoverOverBodyLink, 
                    out: hoverOutBodyLink,
                    sensitivity: 1,
                    interval: 150
                });
        }

        //-----------------------------------------------------------------
        // This function absolutely positions one of the CRBs.  For progressive-
        // rendering, this gets called from the driver loop above.
        function positionOneCrb(jcrb)
        {
            // Get the paragraph to which this CRB corresponds
            var rid = jcrb.attr('rid');
            var p = $j('#' + rid);
            // If there is no paragraph, just hide the CRB and return
            if (p.length != 1) {
                jcrb.hide();
                jcrb.data('myTop', 0);
                return;
            }
            // Record this paragraph
            jcrb.data('myParagraph', p);


            // We'll compute the "visual top", which takes into account the
            // reported top, the top margin, and a fudge factor.
            var myTop = parseInt(p.position().top) + parseInt(p.css('marginTop')) - 7;
            jcrb.data('myTop', myTop);

            // Do some work for each link within this CRB
            var crbLinks = jcrb.find('li');
            crbLinks.each(function() {
                var li = $j(this);

                // Get the expanded heights of each of the <li>s.
                var h = li.height();


                // Find the matching links in the body (might be several)
                var referenceId = li.attr('reference_id');
                var bodyLinks = p.find('a.cite-reflink[rid="' + referenceId + '"]');
                
                // Set a data pointer from the body links back to this CRB link
                bodyLinks.data('myCrbLink', li);
                
                // Now find myBodyLinks.  Note that this is *not* the same as the
                // bodyLinks above.  Here's the difference.  For
                // a body link to have a myCrbLink pointing to this CRB link, it 
                // must be in the paragraph corresponding to this CRB.  But, the
                // CRB link's myBodyLinks lists *all* of the links in the body,
                // regardless of paragraph, that correspond to the same citation.
                var myBodyLinks = $j('a.cite-reflink[rid="' + referenceId + '"]');

                // Store these data in the CRB link
                li.data({
                    'myCrb': jcrb,   // reference from link back to containing CRB
                    'expandedHeight': h,
                    'myBodyLinks': myBodyLinks
                }); 
            });

            // Now collapse the CRB into its default resting state
            
            // First collapse each of the <li>s by adding the 'two_line' class.
            crbLinks.each(function() {
                var li = $j(this);
                li.removeClass('expanded')
                  .removeClass('highlight')
                  .addClass('two_line');
                li.data('isCollapsed', true);
                
                // If this is the first-ever 'two_line' link that we've seen, 
                // then record its heights
                if (linkHeight == -1) {
                    linkHeight = li.height();
                    linkOuterHeight = li.outerHeight(true);
                }
            });

            // Get the height of the paragraph.  This gives the maximum height
            // of the 'collapsed' CRB.  In other words, in the collapsed state,
            // the CRB should not extend below the bottom of the para.
            var pHeight = p.height();

            // The number of links in this CRB, as delivered from the server.
            var numLinks = crbLinks.length;

            // Calculate how many links will fit on this CRB, according to the
            // height of the paragraph.
            // Note that this is a little bit tricky.  When we figure out whether
            // or not we need to hide links, and, as a consequence, add the "see more"
            // link, then we *do not* take the height of the "see more" link into
            // account.  But, once we know that we need to hide links, and thus add
            // the "see more" link, then we need to take the height of that link into
            // account to figure out *how many links to hide*.
            var linksFit = Math.floor( pHeight / linkOuterHeight );

            // Maximum number of links to show here is based either on policy
            // (MAX_LINKS_PER_CRB) or on the number that will fit.
            var maxLinks = Math.min(MAX_LINKS_PER_CRB, linksFit);
            
            // See if we need to hide some.
            if (numLinks > maxLinks) {
                // We need to take off some links.  But first, let's add the "more"
                // link, and measure the total expanded height.

                // Add the "more" link.  This gets appended as the next sibling of
                // the <ul>.  Measure the height before and after, so we know how
                // much height this link contributes.
                var heightBefore = jcrb.height();

                var moreLink = $j("<a href='' class='seemore'>See more ...</a>");
                jcrb.find('div.portlet_content').append(moreLink);
                
                // Add the event handler.
                moreLink.click(moreLinkClick);
                
                // Measure the expanded height
                var expandedHeight = jcrb.height();
                
                // Figure out how much the "see more" link added
                var moreLinkHeight = jcrb.height() - heightBefore;
                
                // Based on this new information, figure out how many links we will
                // show (never go below 0).
                var linksToShow = 
                    Math.max(0, Math.floor( (pHeight - moreLinkHeight) / linkOuterHeight ));

                // Now hide the excess links.  For each, record that it is an extra.
                for (var i = linksToShow; i < numLinks; i++) {
                    $j(crbLinks[i])
                        .hide()
                        .data('isExtra', true);
                }

                // If we're showing no links, change "See more ..." to "See links ..."
                if (linksToShow <= 0) moreLink.text("See links ...");

                // Measure the height again and save as collapsedHeight
                var collapsedHeight = jcrb.height();

                // Now store a bunch of this data with the CRB DOM object
                jcrb.data({
                    'expandedHeight': expandedHeight,
                    'collapsedHeight': collapsedHeight,
                    'collapsedNumLinks': linksToShow
                });
            }

            // Whether or not we need to hide any links, initialize
            // the isCollapsed flag, and the autoCollapseTimerId.
            jcrb.data({
                'isCollapsed': true,
                'autoCollapseTimerId': -1
            });

            // Capture the width prior to repositioning.  Since we're changing
            // from position-static to position-absolute, we'll need to set
            // this to a fixed value.
            var width = jcrb.css('width');

            // The final thing we do is to position it next to the para,
            // and make it visible.
            jcrb.css( {
                'position': 'absolute',
                'visibility': 'visible',
                'width': width,
                'top': myTop 
            });

            // If it would interfere with one of the fixed portlets, hide it.
            if (myTop <= mainPortletsBottom) jcrb.hide();
        }

        //-----------------------------------------------------------------
        // This function handles the mouseover event when the user hovers 
        // over one of the CRB links items.
        function hoverOverCrbLink(event) {
            //console.info("hoverOverCrbLink");
            var link = $j(this);
            // Call expandLink with checkHidden false - we know its not 
            // hidden, because the user has his mouse over it.
            expandLink(link, false);
        }

        //-----------------------------------------------------------------
        // This gets called to highlight a particular link in a CRB.
        // It checkHidden is true, then this first checks to see if this
        // link's CRB is collapsed, and this link is hidden.  If so, then
        // this first expands the CRB.  
        // [Note that the 'checkHidden' feature is not used.  We originally
        // implemented this so that if the user hovers over a body link
        // corresponding to an "extra" CRB link, then that would cause the
        // CRB to expand to show it.  But David didn't like so much animation
        // going on in the disco column while the user is moving his mouse
        // around over the body.  Even though it's not used, I left the code in.]
        
        function expandLink(link, checkHidden) {
            //console.info("expandLink");
            var jcrb = link.data('myCrb');
            jcrb.addClass("stretched");
            // Set the z-index.  This ensures that when the CRBs are expanded, they
            // are layered on top of ones that are collapsed.
            jcrb.css('z-index', 1);
            
            // Enforce the policy that only one CRB is stretched at a time.
            collapseOtherCrbs(jcrb);
            
            // See if we need to expand the whole CRB (equivalent to "see more").
            if ( checkHidden && 
                 link.data('isExtra') &&
                 jcrb.data('isCollapsed') )
            {
                expandCrb(jcrb);
            }
            
            var h = link.data('expandedHeight');
            link.data('isCollapsed', false);

            // Animate the height change.  Whenever we do this, we have
            // to specify the exact numeric value of the final height.
            // When done, we set the CSS property back to 'auto'.
            link.animate(
                {'height': h},
                { 'complete': function() {
                    jcrb.css({height: "auto"});
                  },
                  'duration': 'fast'
                }
            );
            
            // Do this *after* kicking off the animation, because this is
            // what causes the actual height of the link to change.
            link.removeClass('two_line')
                .addClass('expanded');
            
            // This causes the link in the CRB, and all its corresponding
            // links in the body, to be highlighted with a special color.
            highlightLink(link);
        }


        //-----------------------------------------------------------------
        // This function handles the event when the user moves the mouse 
        // out of the <li> items.
        function hoverOutCrbLink(event) {
            //console.info("hoverOutCrbLink");
            var link = $j(this);
            unexpandLink(link);
        }

        //-----------------------------------------------------------------
        function unexpandLink(link) {
            //console.info("unexpandLink");
            var jcrb = link.data('myCrb');

            link.removeClass('expanded')
                .addClass('two_line');
            // Remove the highlight color from the link in the CRB and
            // all the corresponding links in the body.
            unhighlightLink(link);
            
            link.animate(
                {'height': linkHeight},
                { 'complete': function() {
                      link.data('isCollapsed', true);
                      // Check if we still need the 'stretched' class on the 
                      // CRB parent.
                      checkStretched(jcrb);
                      // Reset the height property to auto.
                      jcrb.css({height: "auto"});
                      
                  },
                  'duration': 'fast'
                }
            );
        }
        
        //-----------------------------------------------------------------
        // This function handles the hovering over a body link.  Note that
        // some of these will have corresponding CRB links, and some will
        // not.
        function hoverOverBodyLink(event) {
            //console.info("hoverOverBodyLink");
            var bodyLink = $j(this);

            var crbLink = bodyLink.data('myCrbLink');
            if (crbLink) {
                highlightLink(crbLink);
                var jcrb = crbLink.data('myCrb');
                clearAutoCollapseTimer(jcrb);
            }
            
            // If there's a popper associated with this, open it.
            // Both this and JIG's ncbipopper use hoverIntent, so we have
            // to chain these events together.
            var popper = bodyLink.data('ncbipopper');
            if (popper) popper.open();
        }

        //-----------------------------------------------------------------
        function hoverOutBodyLink(event) {
            //console.info("hoverOutBodyLink");
            var bodyLink = $j(this);
            var crbLink = bodyLink.data('myCrbLink');
            if (crbLink) {
                unhighlightLink(crbLink);
                var jcrb = crbLink.data('myCrb');
                setAutoCollapseTimer(jcrb);
            }
            
            // If there's a popper associated with this, close it
            var popper = bodyLink.data('ncbipopper');
            if (popper) popper.close();
        }

        //-----------------------------------------------------------------
        // This function highlights a CRB link, and all of the body
        // links that correspond to it.
        function highlightLink(link) {
            //console.info("highlightLink");
            link.addClass('highlight');
            link.data('myBodyLinks').addClass('highlight');
        }

        //-----------------------------------------------------------------
        // This function unhighlights a CRB link, and all of the body links
        // that correspond to it.
        function unhighlightLink(link) {
            //console.info("unhighlightLink");
            link.removeClass('highlight');
            link.data('myBodyLinks').removeClass('highlight');
        }

        //-----------------------------------------------------------------
        // This function removes, when necessary, the 'stretched' class on 
        // the CRB object.  If the jcrb isCollapsed is true, and all of
        // the links' isCollapsed are true, then clear this class.  Note 
        // that we don't check whether or not to *set* the class - that is 
        // done instantly whenever any of those things starts to expand.
        function checkStretched(jcrb) {
            //console.info("checkStretched");
            if (!jcrb.data('isCollapsed')) return;
            
            var hasExpandedLink = false;
            jcrb.find('li').each(function() {
                if (!$j(this).data('isCollapsed')) hasExpandedLink = true;
            });
            if (hasExpandedLink) return;
            
            jcrb.removeClass('stretched');
            jcrb.css('z-index', 0);
        }
        
        //-----------------------------------------------------------------
        // Click handler for the "see more" link at the bottom of the portlet.
        // Note that this might be either a "see more" or a "see less" click.
        function moreLinkClick(event) {
            //console.info("moreLinkClick");
            var moreLink = $j(this);
            var jcrb = moreLink.parents('div.pmc_para_cit');

            // If we're collapsed, then we need to expand
            if (jcrb.data('isCollapsed')) {
                expandCrb(jcrb);
            }
            else {
                collapseCrb(jcrb);
            }

            // Override the default action
            return false;
        }
        
        //-----------------------------------------------------------------
        // This function animates the expansion of a CRB to show all of the
        // links.  We will never call this unless this is a collapsible CRB.
        // (In other words, if all the links fit within the height of the
        // paragraph, then we'll guarantee that we'll never call this 
        // function.)
        
        function expandCrb(jcrb) {
            //console.info("expandCrb");
            
            // Make sure this CRB is now on top
            collapseOtherCrbs(jcrb);

            // Update the state variable
            jcrb.data('isCollapsed', false);
            currentExpandedCrb = jcrb;
        
            // Add the 'stretched' class to the outer div
            jcrb.addClass("stretched");
            jcrb.css('z-index', 1);
            
            // Change the label on the "more" link
            jcrb.find('.seemore').text("See less ...");
            
            // Animate the height change.
            var d = jcrb.data();
            jcrb.animate(
                {'height': d.expandedHeight},
                { 'complete': function() {
                      jcrb.css({height: "auto"});
                  },
                  'duration': 'fast'
                }
            );

            // Show all the links.  This is done *after* we kick off
            // the animation because this is the step that causes 
            // the actual height to change.
            var crbLinks = jcrb.find('ul li.two_line');
            var totalNumLinks = crbLinks.length;
            for (var i = d.collapsedNumLinks; i < totalNumLinks; ++i) {
                $j(crbLinks[i]).show();
            }
        }
        
        //-----------------------------------------------------------------
        // This function animates the collapse of the CRB into it's default 
        // resting state, including ensuring that all child links are in 
        // their two_line, collapsed state.  This gets called from two 
        // places: when the user clicks on "see less", and when the 
        // auto-collapse timer triggers.
        
        function collapseCrb(jcrb) {
            //console.info("collapseCrb");
            
            var d = jcrb.data();

            // Change the label on the link
            var linkText = d.collapsedNumLinks > 0 ? "See more ..." : "See links ...";
            jcrb.find('.seemore').text(linkText);

            // Get the link children
            var crbLinks = jcrb.find('ul li');
            var totalNumLinks = crbLinks.length;

            // Animate the height change.  We use the 'complete' option of 
            // the animation function to hide all of the excess links after
            // the animation is done.  If we hid them all right away, the 
            // height would change abruptly (no animation).
            jcrb.animate(
                { 'height': d.collapsedHeight },
                { 'complete': function() {
                      // For all links
                      crbLinks.each(function() {
                          var li = $j(this);
                          
                          // Put the link into its collapsed state
                          li.removeClass('expanded')
                            .removeClass('highlight')
                            .addClass('two_line');
                          li.data('isCollapsed', true);

                          // Hide 'overflow' links
                          if (li.data('isExtra')) li.hide();
                      });
                      
                      // Now put the CRB object itself into the collapsed state
                      jcrb.data('isCollapsed', true);
                      jcrb.removeClass('stretched');
                      jcrb.css('z-index', 0);
                      
                      // Reset the height property to auto.
                      jcrb.css({height: "auto"});
                  },
                  'duration': 'fast'
                }
            );
            
            currentExpandedCrb = null;
        }

        //-----------------------------------------------------------------
        // This function sets a timer on a CRB object so that it will
        // reclose after a fixed period of time after the last time that
        // the user has moved his/her mouse out of the div.  This is set 
        // up as a handler for mouseleave events.  Note that it the CRB 
        // is already collapsed, this does nothing.

        // Handler for the mouse-out event for the whole CRB div.
        function setAutoCollapseTimerHandler(event) {
            //console.info("setAutoCollapseTimerHandler");
            setAutoCollapseTimer($j(this));
        }
        
        // This does the work.  It is also called from the handler for 
        // the mouse-out event on a corresponding body link.
        function setAutoCollapseTimer(jcrb) {        
            //console.info("setAutoCollapseTimer");
            if (jcrb.data('isCollapsed')) return;
            
            // Set the timer
            var timerId = window.setTimeout(function() { 
                collapseCrb(jcrb); 
            }, AUTOCOLLAPSE_TIMEOUT);
            
            // Record it as a data attribute on this object
            jcrb.data('autoCollapseTimerId', timerId);
        }

        //-----------------------------------------------------------------
        // This function clears the auto-collapse timer.  It is set up as
        // a handler for the mouseover event.
        
        // Handler for the mouse-over event for the whole CRB div.
        function clearAutoCollapseTimerHandler(event) {
            //console.info("clearAutoCollapseTimerHandler");
            clearAutoCollapseTimer($j(this));
        }
        
        // This does the work.  It is also called from the handler for 
        // the mouse-over event on a corresponding body tag.
        function clearAutoCollapseTimer(jcrb) {
            //console.info("clearAutoCollapseTimer");
            var timerId = jcrb.data('autoCollapseTimerId');
            if (timerId == -1) return;
            
            window.clearTimeout(timerId);
            jcrb.data('autoCollapseTimerId', -1);
        }

        //-----------------------------------------------------------------
        // This function is used to collapse all of the *other* CRBs.
        // It is invoked whenever we highlight a link or expand a CRB.
        // This makes sure that the focus is always on only one at a time.
        function collapseOtherCrbs(jcrb) {
            //var cstr = (currentExpandedCrb) ?
            //    (", currentExpandedCrb = " + currentExpandedCrb.attr('id')) :
            //    "";
            //console.info("collapseOtherCrbs; jcrb = " + jcrb.attr('id') + cstr);
            
            /* This used to loop through all of the CRBs, collapsing all of
              them except myId.  But now I just keep track of the (at most
              one) CRB that's currently expanded, and collapse that. 
            var myId = jcrb.attr('id');

            crbs.each(function() {
                var thisCrb = $j(this);
                if (thisCrb.attr('id') != myId) {
                    collapseCrb(thisCrb);
                }
            });
            */
            //console.info("Checking, currentExpandedCrb = " + currentExpandedCrb);
            if (currentExpandedCrb && currentExpandedCrb.attr('id') != jcrb.attr('id')) {
                collapseCrb(currentExpandedCrb);
            }
        }

        //-----------------------------------------------------------------
        // This function is set as an event handler when the user clicks
        // on any of the shutter buttons on any of the fixed portlets at the
        // top.  When these fixed portlets change size, we have to go
        // through all the CRBs again and hide/show depending on whether
        // or not they are interfering.
        // But, because the portlet size change is animated, the new size
        // won't be known for a while, so this in turn sets a timer that
        // invokes hideInterfering().
        
        function fixedPortletShutterClick(event) {
            setTimeout(hideInterfering, 200);
        }
        
        //-----------------------------------------------------------------
        // hideInterfering iterates through all of the CRBs and hides any
        // that would interfere with any of the fixed portlets at the top.
        // It is called every time the user clicks a shutter button on one 
        // of the fixed portlets.
        
        function hideInterfering() {
            var mainPortletsBottom = getMainPortletsBottom();
            crbs.each(function() {
                var jcrb = $j(this);
                if (jcrb.data('myTop') <= mainPortletsBottom) {
                    jcrb.hide();
                }
                else {
                    jcrb.show();
                }
            });
        }

        //-----------------------------------------------------------------
        // This function gets the y-coordinate of the bottom of the lowest
        // of the fixed portlets.
        
        function getMainPortletsBottom() {
            var bottom = 0;
            fixedPortlets.each(function() {
                var p = $j(this);
                var top = parseInt(p.position().top) + parseInt(p.css('marginTop'));
                var thisBottom = top + p.height();
                bottom = Math.max(bottom, thisBottom);
            });
            return bottom;
        }
        
        //-----------------------------------------------------------------
        // This function adds a popper to one of the links in the body.
        // It is called by the driver loop above.  The link is guaranteed
        // to have the 'cite-reflink' class, and a @rid attribute.
        var popperCount = 0;
        function addBodyLinkPopper(link) {
            // If we never found a container for our poppers in the source
            // document, then we can't do anything.
            if (blPopperDiv.length < 1) return;
            
            var rid = link.attr('rid');
            // Some id's from the backend have special characters, so we
            // need to escape them whenever they're used as jQuery selectors.
            // See PMC-6384 and http://tinyurl.com/2qfqgc
            var ridEscaped = rid.replace(/:/g, "\\:")
                                .replace(/\./g, "\\.")
                                .replace(/·/g, "\\·");

            var popperId = "body-link-popper-" + rid; // + "-" + popperCount;
            var popperIdEscaped = "body-link-popper-" + ridEscaped; // + "-" + popperCount;
            popperCount++

            // If we've already made a popper for this particular reference,
            // let's use that.
            var popper = blPoppers[rid];
            
            // Otherwise, we need to make one.
            if (!popper) {
                //console.info("Adding pooper for body link, rid = " + rid);
                
                popper = $j("<div/>", { 
                    'id': popperId,
                    'class': 'body-link-popper',
                    'style': 'display: none'
                });

                // If this link has a corresponding CRB link, then we'll
                // use that for the popper text
                var myCrbLink = link.data('myCrbLink');
                if (myCrbLink) {
                    // Get the popper text from the CRB link
                    //console.info("Got a CRB link, rid = " + rid);
                    popper.append(myCrbLink.children("a").contents().clone());
                    popper.append(myCrbLink.children(".alt-note").children().clone());
                }
                
                // Otherwise we'll copy the text from the references section.
                else {
                    // Find the text for this popup from the element pointed to by 
                    // @rid.  This will be in the reference section of the article.
                    //console.info("Not a CRB link, rid = " + rid);
                    var refElem = $j('#' + ridEscaped);
                    
                    // Check to make sure that we found something
                    if (refElem.length > 0) {
    
                        var citeText = refElem.text();
                        var popText = $j.trim(citeText);
        
                        // If the @href element of the current <a> tag (currElem)
                        // has a '#' portion, meaning that it is a link to the
                        // reference list in this same article, then indicate 
                        // that in the popup text.
                        if (link.attr("href").charAt(0) == '#') {
                            popText += ' [Reference List]';
                        }
                    }
                    popper.append(popText);
                }
                
                // Put this new popper into the container div.
                blPopperDiv.append(popper);
                
                // And record it for posterity.
                blPoppers[rid] = popper;
            }
            
            // FIXME:
            // It would be nice if I could pass the configuration parameters
            // to JIG as a JS object; but it doesn't work.
            //var popperConfig = {
            //    'destSelector': ('#' + popperId) 
            //};
            // Note that I have to set a fixed width here.  "auto" doesn't work.
            // Nor does setting it in the CSS file.
            
            // FIXME:  This is a little debug feature that lets you enable the
            // popper's autoAdjust feature.  Because of JSL-1519, I have it
            // turned off by default.
            var loc = window.location.href;
            var qs = loc.substring(loc.indexOf("?") + 1);
            var qsParams = qs.split(/&/);
            var numQsParams = qsParams.length;
            var gotAutoAdjust = false;
            for (var i = 0; i < numQsParams; ++i) {
                if (qsParams[i] == "__autoadjust") gotAutoAdjust = true;
            }
            //console.info("qsParams = " + qsParams[0] + ", " + qsParams[1]);
            gotAutoAdjust = true;
            var adjustFitStr = gotAutoAdjust ? 'autoAdjust' : 'none';

            link.addClass('jig-ncbipopper');
            
            // FIXME:  See JSL-1528.  It would be nice if we could pass config
            // parameters as true JS objects, rather than first converting them 
            // to JSON-encoded strings.
            link.attr('data-jigconfig', 
                'destSelector: "#' + popperIdEscaped + '", ' +
                'isTriggerElementCloseClick: false, ' +
                'hasArrow: true, ' + 
                'width: "30em", ' +
                'adjustFit: "' + adjustFitStr + '", ' +
                'triggerPosition: "center right"');
            $j.ui.jig.scan(link);
        }
    } /* end of initialize() */
})(jQuery);

