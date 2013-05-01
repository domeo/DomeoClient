if (typeof(jQuery) != "undefined") {
  (function($j) {
      var formState = {
          overrideDepot: false,
          selectedDepot: "",
          overrideBackends: false,
          backends: {}
      };
      
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
          
          
          if (id == "depsel-depot-cb") {
              if (checked) {
                  $j("#depsel-depot-sel").removeAttr("disabled");
                  depotSelFormToObj();
              }
              else {
                  $j("#depsel-depot-sel").attr("disabled", 1);
                  delete cookieObj.depotName;
              }
          }
          else if (id == "depsel-depot-sel") {
              depotSelFormToObj();
          }
          else {
              var m;
              if (m = id.match(/depsel-be-(.*?)-cb/)) {
                  var backend = m[1];
                  //console.info(">>>backend checkbox:  " + backend);
                  if (checked) {
                      $j("#depsel-be-" + backend + "-text").removeAttr("disabled");
                      beUrlFormToObj(backend);
                  }
                  else {
                      $j("#depsel-be-" + backend + "-text").attr("disabled", 1);
                      delete cookieObj.backendUrls[backend];
                  }
              }
              else if (m = id.match(/depsel-be-(.*?)-text/)) {
                  backend = m[1];
                  //console.info(">>>backend text:  " + backend);
                  beUrlFormToObj(backend);
              }
          }
          //console.info("Done cbChanged");
      }

      ///////////////////////////////////////////////////////////////////////////////
      // depotSelFormToObj()
      // This is called by a couple of event handlers and decodes the
      // currently selected depot (in the drop-down form) and sets the
      // cookieObj.depotName accordingly.

      function depotSelFormToObj()
      {
          $j("#depsel-depot-sel option").each(function() {
              var opt = $j(this);
              if (opt.attr("selected")) {
                  var id = opt.attr("id");
                  cookieObj.depotName = id.match(/depsel-depot-opt-(.*)/)[1];
              }
          });
      }

      ///////////////////////////////////////////////////////////////////////////////
      // beUrlFormToObj(backend)
      // This is similar, and takes care of reading the text value from the
      // form and stuffing it into the object

      function beUrlFormToObj(backend) {
          var value = $j("#depsel-be-" + backend + "-text").attr("value");
          if (value) cookieObj.backendUrls[backend] = value;
      }

      ///////////////////////////////////////////////////////////////////////////////
      function init() {
          cookieObj = cookieXmlToJson();
          initFormState();

          // Set event handers
          $j("#depsel-form .depsel-control").change(function(event) {
              cbChanged(event);
          });
          $j("#depsel-go-button").click(function(event) {
              goButton(event);
          });
          $j("#depsel-reset-button").click(function(event) {
              resetButton(event);
          });

          // Put it into the sidecontent pullout
          $j("#depsel-form").sidecontent({
              classmodifier: "depotselector",
              attachto: "rightside",
              width: "400px",
              opacity: "0.8",
              pulloutpadding: "5",
              textdirection: "vertical",
              clickawayclose: 0,
              titlenoupper: 1
          });
          var pulloutColor = $j("#depsel-form").attr("pulloutColor");
          //alert("color is " + pulloutColor);
          $j(".depotselectorpullout").css("background-color", pulloutColor);
      }

      ///////////////////////////////////////////////////////////////////////////////
      // goButton(event)
      // Handle the user-click of the "Go!" button.
      
      function goButton(event) {
          // Convert the object into XML
          var cookieXml = 
            "<Depot" +
            ( cookieObj.depotName ? (" name='" + cookieObj.depotName + "'>")
                                  : ">" );
          for (var backend in cookieObj.backendUrls) {
              //console.info("+++ backend " + backend);
              cookieXml += 
                "<BackendUrl backend='" + backend + "' " +
                "url='" + xmlEscape(cookieObj.backendUrls[backend]) + "'/>";
          }
          cookieXml += "</Depot>";
          //console.info(cookieXml);
          
          // Set the cookie
          document.cookie = "ncbi_depot=" + encodeURIComponent(cookieXml) +
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
          document.cookie = "ncbi_depot=" + 
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

          var depotName = cookieObj.depotName;
          if (!depotName) {
              $j("#depsel-depot-cb").removeAttr("checked");
              $j("#depsel-depot-sel").attr("disabled", 1);
          }
          else {
              var selDepotOptId = "#depsel-depot-opt-" + depotName;
              //console.info("selDepotOptId is " + selDepotOptId);
              var selDepot = $j("#depsel-depot-opt-" + depotName);
              if (selDepot.length != 0) {
                  $j("#depsel-depot-cb").attr("checked", 1);
                  $j("#depsel-depot-sel").removeAttr("disabled");
                  selDepot.attr("selected", 1);
              }
              else {
                  $j("#depsel-depot-cb").removeAttr("checked");
                  $j("#depsel-depot-sel").attr("disabled", 1);
              }
          }
          
          // Foreach backend in the form
          $j(".depsel-be-cb").each(function(i) {
              var id = $j(this).attr("id");
              var beName = id.match(/depsel-be-(.*?)-cb/)[1];
              //console.info("### backend, id is '" + id + "', beName is '" + beName + "'");
              if (!beName) return;
              
              // See if there's a corresponding element in the cookie
              if (!cookieObj.backendUrls ||
                  !cookieObj.backendUrls[beName]) {
                  //console.info("Didn't find " + beName);
                  $j("#depsel-be-" + beName + "-cb").removeAttr("checked");
                  $j("#depsel-be-" + beName + "-text").attr("disabled", 1);
              }
              else {
                  //console.info("Found " + beName);
                  $j("#depsel-be-" + beName + "-cb").attr("checked", 1);
                  var textbox = $j("#depsel-be-" + beName + "-text");
                  textbox.removeAttr("disabled");
                  textbox.attr("value", cookieObj.backendUrls[beName]);
              }
          });
      }
      
      ///////////////////////////////////////////////////////////////////////////////
      // This gets the "ncbi_depot" cookie, which is in XML, and turns it
      // from this:
      //   <Depot name='test'>
      //     <BackendUrl backend='tagserver' url='bingo'/>
      //     ...
      //   </Depot>
      // Into this (note that everything is optional):
      //   { depotName: 'test',
      //     backendUrls: {
      //         tagserver: 'bingo', ... }
      //   }
      // If there is no cookie set or parsing fails, this returns {}.
      
      function cookieXmlToJson() {
          var cookieObj = {
              backendUrls: {}
          };

          cookieStr = getCookie("ncbi_depot");
          //console.info("cookie value is '" + cookieStr + "'");

          // Parse XML
          try {
              var cookieXml = $j(cookieStr);
          }
          catch(err) {
              return cookieObj;
          }
          
          var depotElem = cookieXml;
          if (depotElem.length == 0) {
              // No valid cookie value found.
              return cookieObj;
          }
          var depotName = depotElem.attr("name");
          if (depotName) {
              cookieObj.depotName = depotName; 
          }
          
          var backends = depotElem.find("BackendUrl");
          if (backends.length != 0) {
              var backendUrls = cookieObj.backendUrls;
              backends.each(function (i) {
                  var e = $j(backends[i]);
                  backendUrls[e.attr("backend")] = e.attr("url");
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


//
//		 PAFAppResources: Initialize client side of Portlet Application Framework
//

Portal.Portlet.PAFAppResources = Portal.Portlet.extend({

   // In PAF, the form always posts to itself. It should update and interpret data, and
   // then *redirect* to another page if necessary.
   
   // This code fixes up the EntrezForm in PAF templates to automatically post
   // to the current window pathname.
   
   // If the programmer specifies a hidden input with id="entrez-action",
   // that action (instead of POST) is used for the form.
   
   // If the programmer specifies a hidden input with id="entrez-method",
   // that method is used for the form. 
       
   // FIXME: This is a hack. EntrezForm should default to posting to same page,
   // and there should be some way of indicating how to post (or get) elsewhere.
   // The problem here is that we've mixed Entrez into PAF. This should be refactored.
   
   init: function(path, name, notifier) {
      var oThis = this;  // Use <oThis> instead of <this> for registering callbacks
      this.base(path, name, notifier);  // Call superclass constructor
      
      if (!document.forms[0]) return; // Do nothing if no form.
      
      var getNodeValue = function(id, dfl) {
         var node = $(id);
         return node ? node.value : dfl;
      };
      
      var entrezAction = getNodeValue('entrez-action', window.location.pathname);
      var entrezMethod = getNodeValue('entrez-method', null);
      var entrezSubmit = getNodeValue('entrez-submit', false);
      
      document.forms[0].action = entrezAction;
      
      // FIXME: We need to include query string here, too, I think... At least for POST...      
      if (entrezMethod) {
         document.forms[0].method = entrezMethod;
      }

      // By default, entrez form ignores submits. Setting "entrezSubmit" to "true" means let Entrez form
      // actually process the submit. This probably will only work correctly for action="get"       
      if (entrezSubmit) {
         document.forms[0].onsubmit="return true;"
      }
   }
});

/*
  This invokes the TagServer functionality.
  See the comments in the NcbiTagServer.js module.
*/

if (typeof NcbiTagServer === "object") {
    NcbiTagServer.initRenderByTagNode();
}
// makes an ext js looking portlet w/o using extjs
if (typeof(noext) == 'undefined') {
    noext = {};
}
noext.parseConfig = function(n) {
    var configStr = n.getAttribute('config') || '';
    try {
        var jsonObj = eval('({' + configStr + '})');
    }
    catch(e) {
        if (window.console) {
            console.error(e.message + ' in local config object in noext.Portlet: file: ' + e.fileName + ', line ' + e.lineNumber + '.' );
        }
    }
    return jsonObj;
};
noext.getClassArray = function(n) {
    var classNameStr = n.className;
    return classNameStr.split(' ');
};
noext.getFirstHeader = function(n) {
    var fc = utils.getFirstChild(n);
    if(fc.tagName.search(/^h\d/i) != -1) {
        return fc;
    }
};
noext.Portlet = function(n, beenClassed) {
    this.n = n;
    this.beenClassed = beenClassed;
    if (!beenClassed) {
        this.headerNode = noext.getFirstHeader(this.n);
        this.headerDiv = document.createElement('div');
        this.bodyDiv = document.createElement('div');
        utils.addClass(this.bodyDiv, 'x-panel-body');
    }
    else {
        var divNodes = n.getElementsByTagName("div");
        for (var i = 0; i < divNodes.length; ++i) {
            var divNode= divNodes[i];
            if (utils.hasClass(divNode, 'x-panel-header')) {
                this.headerDiv = divNode;
            }
            else if (utils.hasClass(divNode, 'x-panel-body')) {
                this.bodyDiv = divNode;
            }
        }
    }
    var configObj = noext.parseConfig(this.n);
    this.collapsible = configObj.collapsible == false ? false : true; 
    this.closeable = configObj.closeable == false ? false : true; 
    this.collapsed = configObj.collapsed == true ? true : false;
    if (this.collapsed == true) {
        utils.addClass(this.n, 'x-panel-collapsed');
        this.bodyDiv.style.display = 'none';
    }
    this.make();
};

noext.Portlet.instances = [];
noext.Portlet.ready = function() {
    var nodes = $C('port', 'class', document, 'div');
    for (var i = 0; i < nodes.length; i++) {
        var n = nodes[i];
        if (utils.hasClass(n, 'norender')) {
            continue;
        }
        // CFM:  If the top-level div already has the 'x-panel' class, then 
        // assume that it has been completely "classed up".
        var beenClassed = utils.hasClass(n, 'x-panel');
        portlet = new noext.Portlet(n, beenClassed);
    }
}
noext.Portlet.prototype = {
    make: function() {
        var beenClassed = this.beenClassed;
        
        var toolCloseA, toggleA;
        if (!beenClassed) {
            // put a parent around body content
            //var innerContainingDiv = document.createElement('div');
            utils.addClass(this.n, 'x-panel');
            //this.n.style.width = 'auto';
            utils.addClass(this.headerDiv, 'x-panel-header');
            utils.addClass(this.headerDiv, 'x-unselectable');
            if (this.headerNode) { 
                this.n.insertBefore(this.headerDiv, this.headerNode);
                var header = this.n.removeChild(this.headerNode);
            }
            toolCloseA = document.createElement('a');
            toolCloseA.setAttribute('href', '#');
            utils.addClass(toolCloseA, 'x-tool');
            utils.addClass(toolCloseA, 'x-tool-close');
            toggleA = document.createElement('a');
            toggleA.setAttribute('href', '#');
            utils.addClass(toggleA, 'x-tool');
            utils.addClass(toggleA, 'x-tool-toggle');
            if (this.closeable == true) {
                this.headerDiv.appendChild(toolCloseA);
            }
            if (this.collapsible) {
                this.headerDiv.appendChild(toggleA);
            }
            if (this.headerNode) {
                this.headerDiv.appendChild(header);
                utils.addClass(this.headerNode, 'x-panel-header-text');
            }
        }
        else {
            // Find the close and toggle <a> elements, if they exist.  To do this, we
            // iterate through all the <a> children, looking for the right classes.
            var aKids = this.n.getElementsByTagName("a");
            for (var i = 0; i < aKids.length; ++i) {
                var aKid = aKids[i];
                if (utils.hasClass(aKid, 'x-tool-close')) {
                    toolCloseA = aKid;
                }
                else if (utils.hasClass(aKid, 'x-tool-toggle')) {
                    toggleA = aKid;
                }
            }
        }
        this.toolCloseA = toolCloseA;
        this.toggleA = toggleA;
        if (!beenClassed) {
            this.reParent();
        }
        this.setEvents();
    }, 
    reParent: function() {
        var bodyElems = [];
        utils.insertAfter(this.n, this.bodyDiv, this.headerDiv);
        for (var i = this.n.childNodes.length -1; i >= 0; i--) {
            var fc = this.n.childNodes[i];
            if (fc != this.headerDiv && fc != this.bodyDiv) { 
                bodyElems.push(this.n.removeChild(fc));
            }
            for (var j = 0; j < bodyElems.length; j++) {
                this.bodyDiv.appendChild(bodyElems[j]);
            }
        }
    },
    setEvents: function(toggleA) {
        var that = this;
        if (this.toggleA) {
            utils.addEvent(this.toggleA, 'click', function(e) { that.toggle(e)});
        }
        if (this.toolCloseA) {
            utils.addEvent(this.toolCloseA, 'click', function(e) { that.close(e)});
        }
    }, 
    toggle: function(e) {
        utils.preventDefault(e);
        if (utils.hasClass(this.n, 'x-panel-collapsed')) {
            utils.removeClass(this.n, 'x-panel-collapsed')
            this.bodyDiv.style.display = 'block';
        } else {
            utils.addClass(this.n, 'x-panel-collapsed');
            this.bodyDiv.style.display = 'none';
        }
    },
    close: function(e) {
        utils.preventDefault(e); 
        this.n.parentNode.removeChild(this.n);
    }
};

utils.addEvent(window, 'load', noext.Portlet.ready);

// This 2.x branch of this file was to add a brief delay to the pop-up, but it
// was decided not to merge this change into the main trunk.  I.e. this branch
// is PMC specific.  See PP-207.

Portal.Portlet.BriefLinkPageSection = Portal.Portlet.extend({

	init: function(path, name, notifier) {
	    console.info("Created BriefLinkPageSection");
		this.base(path, name, notifier);
        
        this.SetPortletName();
        if (Portal.Portlet.BriefLinkPageSection.portletname != ''){
            Portal.Portlet.BriefLinkPageSection.portletname = "." + Portal.Portlet.BriefLinkPageSection.portletname;
        }
        
        this.CreatePopNodes();
        
	},
	
	SetPortletName: function(){
	    // derived portlet should override and provide portlet name;
	},
	
    popNodeDelay: 0,

    CreatePopNodes: function(){
        var pop = cssQuery("div.brieflink div" + Portal.Portlet.BriefLinkPageSection.portletname + " ul");
        var poptype = pop[0].className;
        
        // when value is pop0
        if (poptype.match(/pop0/)){
            // no popnodes need to be created
        }
        // for all other cases try to create popnode
        else{
            // Create ElementPopper for each li.item
    		var elemid = "div.brieflink div" + Portal.Portlet.BriefLinkPageSection.portletname + " li.item";
    		var elements = cssQuery(elemid); 
    		
    		// go through the list of elements
    		for (var i = 0; i < elements.length; i++) {
                var elem = elements[i];
                var content = ""; 
                
                // when value is pop1
                if (poptype.match(/pop1/)){
                    // create pop with title + desc
                    var linktext = cssQuery("a", elem);
                    if (linktext && linktext.length > 0){
                        content += linktext[0].innerHTML + "<br/>";
                    }
                }
                // default case - when value is pop2 or anything else but pop3
                else if (!poptype || !poptype.match(/pop3/)){
                    // if visible content length is larger than 105, then show the entire text in popup,
                    // followed by any included link description.
                    var linktext = cssQuery("a", elem);
                    var note = cssQuery("p.note", elem);
                    var content = ""; 
                    var linklength = 0;
                    if (linktext && linktext.length > 0){
                       linklength += linktext[0].innerHTML.length;
                    }
                    if (note && note.length > 0){
                       linklength += note[0].innerHTML.length;
                    }
                    if (linklength > 100){
                       content += linktext[0].innerHTML + "<br/>";
                    }
                } // end default case
	           
	           
                // add Description to pop node when it exists for all popnode cases	           
	            var desc = cssQuery("p.desc", elem);
                if (desc && desc.length > 0){
                   content += desc[0].innerHTML;
                }
                
                // create popnode when content is present
                if (content && content.length > 0) {
                  var elemclass = elem.className;
                  if (!elemclass.match(/popnode/)){
                      elem.className = elemclass + " popnode";
                  }
                  //new ElementPopper(elem, content);
                  new ElementPopper(elem, { 
                      content: content,
                      delay: this.popNodeDelay 
                  });
                }
                
            } // end for 
        } // end else
	} // end CreatePopNodes function
},
{
portletname: '' 

});
//
// Java Module PopDiv_JS
//

//
// Shows and hides a div at a specific location, either (x,y), or relative to
// an element in a specific direction.
//
// Limitations:
//  - height or width must be set on the popper div, or it may not position correctly.
//
// You're probably better off using ElementPopper instead of PopDiv for hover pops. Popper handles mouse events
// correctly.
//
// Arguments:
//   divId: the id of the div to use for the popper. You can also pass a DOM node, and PopDiv will use that node.
//       The default is "popperDiv".
//   className: Add this className to the popper div. Optional--no default. This is useful for setting up default state.
//
// It's preferable to use a single PopDiv to render popups all over
// a single page. You only need as many PopDivs as the maximum number of popups you may
// ever have open at once. See ElementPopper for details.

var PopDiv = function(divId, className) {

   var innerdiv = null;
   
   // default is a string
   if (typeof(divId) == 'undefined') {
      divId = "popperDiv";
   }
   
   // Find or make popper div
   if ((this.div = document.getElementById(divId)) == null) {
   
      this.div = document.createElement("div");
      this.div.id = divId;
      this.div.className = "popper";
      if (className) {
         this.div.className += " " + className;
      }
      document.body.appendChild(this.div);
      this.div.innerHTML = "<div class='popperInnerDiv'/>";
   } else {
      utils.addClass(this.div, "popper"); // Be sure it has this
   }

   // this.innerdiv is first element of this.div
   for (this.innerdiv = this.div.firstChild;
      this.innerdiv && this.innerdiv.nodeType != 1;
      this.innerdiv = this.innerdiv.nextSibling) {
   }
   
   // If caller provided a div with no inner divs, add one.
   if (!this.innerdiv) {
       this.innerdiv = document.createElement("div");
       this.innerdiv.className = "popperInnerDiv";
       this.div.appendChild(this.innerdiv);
   }
}

PopDiv.POP_ABOVE = 0;
PopDiv.POP_RIGHT = 1;
PopDiv.POP_BELOW = 2;
PopDiv.POP_LEFT = 3;
PopDiv.POP_CLASSES = ['pop-above', 'pop-right', 'pop-below', 'pop-left'];

PopDiv.POPPED = 'popped';
PopDiv.POP_GETXY = 'pop-getxy';

PopDiv.prototype = {
   // Show it at x, y
   showAt: function(html, x, y) {

    //console.info("POP @ (" + x + ", " + y + ")");
    //console.info("----");
    
      // Set content if provided
      if (html) {
         this.innerdiv.innerHTML = html;
      }

      this.div.style.left = x + "px";
      this.div.style.top = y + "px";

      // Pop it
      utils.addClass(this.div, PopDiv.POPPED);

   },

   // Show it near element ("dir" indications direction)
   // Dimensions don't exist if element isn't shown.
   getXY: function() {
      var popped = utils.hasClass(this.div, PopDiv.POPPED);

      if (!popped) {
         utils.addClass(this.div, PopDiv.POPPED);
      }
      var dim = utils.getXY(this.div);
      if (!popped) {
         utils.removeClass(this.div, PopDiv.POPPED);
      }

      return dim;
   },

   hide: function() {
      utils.removeClass(this.div, PopDiv.POPPED);
      if (this.popClass) {
         utils.removeClass(this.div, this.popClass);
         this.popClass = null;
         this.popnode = null;
      }
   },

   // showBy shows a popper adjacent to another item.
   showBy: function(html, elem, dir) {
      var dx = 0;
      var dy = 0;

      // Already popped over a particular popnode
      if (this.popnode && (this.popnode == elem)) {
         return;
      }
      this.popnode = elem;

      // Default direction
      if (typeof(dir) != "number") {
         dir = PopDiv.POP_LEFT;
      }

      // Set class "pop-<direction>": this determines margin
      this.popClass = PopDiv.POP_CLASSES[dir];
      //console.info("dir = " + dir +", popclass = " + this.popClass);
      utils.addClass(this.div, this.popClass);

      // Set text so that sizing is correct
      this.innerdiv.innerHTML = html;

      // Get geometries of target and popper
      var thatdim = utils.getXY(elem);

      // PMC-7454 - work around a bug in utils.js.  When the element is a bit
      // of text that begins near the end of one line and wraps to the next
      // line, getXY() returns an X value which marks the x-coordinate of the
      // start of the text, rather than the left margin, which it should.
      // This only applies to the popups over references inside the article
      // body (POP_RIGHT) not the the cited-reference blocks in the discovery
      // column (which are POP_LEFT).
      if (dir == PopDiv.POP_RIGHT) {
          thatdim.x = jQuery(elem).position().left;
      }

      var popdim = this.getXY(this.div);
      //console.info("that =");
      //console.info(thatdim);
      //console.info("pop = ");
      //console.info(popdim);

      // Calculate position
      switch (dir) {
      case PopDiv.POP_ABOVE: dy = -1*popdim.h;  break;
      case PopDiv.POP_LEFT:  dx = -1*popdim.w;  break;
      case PopDiv.POP_BELOW: dy =    thatdim.h; break;
      case PopDiv.POP_RIGHT: dx =    thatdim.w; break;
      default:
        throw "PopDiv: Invalid direction: " + dir;
      }
      //console.info("Popping at: [x=" + (thatdim.x + dx) + ", y="+(thatdim.y + dy)+"]");

      this.showAt(null, thatdim.x + dx + 5, thatdim.y + dy);
   }
};



// An ElementPopper shows a popup adjacent to a target element when the user mouses over the target.
// It handles browser events and manipulates a popper to show and hide detail data.
//
// When the mouse cursor enters the target, the content appears in the popper nearby.
//  When the mouse cursor leaves the target, the popper is hidden.
//
// This rather intense event handling code is necessary because mouseover and mouseout doesn't work
// as expected--mouseouts occur when moving over child nodes. The "relatedTarget" trick described
// by PPK (quirksmode.org) doesn't work either, because browsers sometimes drop events
// with related targets. So the only option is to capture document.onmousemove, and detect when
// the mouse either exits document.body, or moves over something that is neither the target nor
// one of its children.
//
// "popnode" is the target node
// "content" is either a string (the content to show), a DOM node (show its innerHTML), or a function.
// "config" is configuration; if it's just a number, then it's "direction" (PopDiv.POP_*); otherwise, it's a JS object with 
//     attribute/value pairs
// "popperId" is the ID to assign to the popper; default is to make one up.
//
// When used with Ext.Ncbi, the popper should initialize itself on the page a few hundred
// ms after the page loads. IE apparently creates a race condition where popper and Ext.Ncbi initialization
// throw away the event handlers on which ElementPopper depends.
//
// So in that case, instead of new ElementPopper(a,b), you want to do:
//     setTimeout(function() {new ElementPopper(a,b);}), 500);

//
var ElementPopper = function(popnode, content, config, popperId) {

    var that = this;

    // Inner functions
    // If target is popnode or one of its children, returns popnode; otherwise, null
    function getPopTarget(target) {
       while (target && !utils.hasClass(target, "popnode")) {
          target = target.parentNode;
       }
       return target;
    };

    // Use inner functions here because removeEvent requires a reference to the
    // function object passed to addEvent, and we need to use closure to maintain scope.

    // Show popper and start tracking mouse motions
    var handlePop = function(e) {
       var target = getPopTarget(utils.getTargetObj(e));
       
       if (target) {
	       // If Ajax URL is supplied, then fetch URL.
	       //   If AJAX URL is a function, the URL is the return value of the function
	       //   Otherwise AJAX URL must be a string
	       // When AJAX call succeeds, pop up the content. If there's a contentFunction, the content is
	       // the result of applying the contentFunction to the AJAX return data; otherwise, it's just
	       // the AJAX return data itself.
	       //
	       if (that.url) {
	          var url = (typeof(that.url) == 'function') ? that.url(target) : that.url;
	
	          jQuery.ajax({
	             cache: false,
	             'url': url,
	             success: function(data, textStatus) {
	                doPop(target, data, textStatus);
	             }
	          });
	       } else {
	          doPop(target);
	       }
	       
	       // Set up to listen for mouse out
	       // This is the node currently being handled; it's cleared when
	       // the user mouses out.
	       that.handlingNode = target;
	    
           utils.removeEvent(target, "mousemove", handlePop);
           utils.addEvent(document.body, "mousemove", trackMouse);
           utils.addEvent(document.body, "mouseout", trackMouse);
       }
    }
    
    var doPop = function(target, data, status) {
       
       // Pop, shift listener to document body
       if (target) {
          that.timer = setTimeout(function() {
             // If we're still waiting for a pop, do the pop.
             // We're only waiting for a pop when handlingNode is set.
             if (target == that.handlingNode) {
                that.thePopper.showBy(that.contentFunction(target, data, status), target, that.direction);
             } else {
               // Ignore the pop request, because we're no longer interested in this pop.
             }
             that.timer = null;             
          }, that.delay);

       }
    };

    // Unpop, shift listener back to popnode
    var trackMouse = function(e) {
       var target = getPopTarget(utils.getTargetObj(e));

       // If mouse left document.body, or the target's not over the popnode, hide.
       if (e.type == "mouseout" || target == null) {
          // Clear pop timer if it hasn't popped
          if (that.timer) {
             window.clearTimeout(that.timer);
             that.timer = null;
          }
          that.thePopper.hide();
          that.handlingNode = null;
          
          utils.removeEvent(document.body, "mousemove", trackMouse);
          utils.removeEvent(document.body, "mouseout", trackMouse);
          utils.addEvent(popnode, "mousemove", handlePop);
       }
    };

    // END inner functions

    //
    // ctor main
    //
     
    // Support new signature ElementPopper(element, {config...}); in that case, "content" is a config object,
    // all other arguments are ignored, and config.content must be set 
    if (typeof(content) == 'object') {
       config = content;
       content = config.content;
    }
    
    // If a content URL is defined (AJAX URL string or function that returns URL), remember it
    if (config && config.url) {
       this.url = config.url;
    }
    
    // If content is a string, make it a function that returns the string.
    // If it's a dom node, make it a function that returns the dom node's innerHTML
    // If content is undefined, then assume data is coming from AJAX callback
    if (typeof(content) == 'undefined') {
       this.contentFunction = function(target, data, status) { return data };
    } else if (typeof(content) == 'string') {
       this.contentFunction = function() { return content; }
    } else if (content.innerHTML) {
       this.contentFunction = function() { return content.innerHTML; }
    } else if (typeof(content) == 'function') {
       this.contentFunction = content; // Better be a function...
    }
    
    // Backward compatibility: "config" arg used to be "direction", so
    // interpret it as "direction" if config is a number.
    // Otherwise parse out config info
    if (typeof(config) === 'undefined') {
       config = {
          direction: PopDiv.POP_LEFT,
          delay: 0
       }
    }
    
    if (typeof(config) == 'number') {
       this.direction = config;
    } else {
       this.direction = (typeof(config.direction) != 'undefined') ? config.direction : PopDiv.POP_LEFT;
       this.delay = config.delay || 0;
    }
    this.popperId = config.popperId || popperId || ("popper" + (ElementPopper.popIndex++)); 

    // Create the popper
    this.thePopper = new PopDiv(this.popperId);
    var popdiv = $(this.popperId);
    
    // FIXME: Is this necessary? I think it's maybe wrong!
    if (popdiv) {
       utils.addClass(popdiv, "popnode");
    }

    // Get popnode if it's an id string
    if (typeof(popnode) == 'string') {
        popnode = $(popnode);
        if (!popnode) {
           throw "ElementPopper: id not found";
        }
    }
    this.popnode = popnode;
    this.timer = null; // Only exists when waiting for a pop
    
    // When the user mouses over target, pop it up
    utils.addEvent(popnode, "mousemove", handlePop);
}

ElementPopper.popIndex = 0;

ElementPopper.prototype = {
   setDelay: function(delay) {
      this.delay = delay;
   },
   getDelay: function() {
      return delay;
   }
};



 utils.addEvent(window, "load", function(){     
     theDEP = new DeferredElementPopper("rapopper", { 
          content: function(node) { 
               var c = cssQuery(".hidden", node);               
               if (c && c.length > 0) {
                  var label = (c[0].textContent || c[0].innerText); 
                  var cit = getCitation(node);
                  label = "<p id='htbpoptext'>" + label + "</p>";
                  if (cit && cit > ""){ // avoid "undefined" value
                      label = label + "<p id='htbpcit'>" + cit + "</p>";
                  } 
                  return label;
               }                
          }, 
          direction: PopDiv.POP_LEFT, 
          popperId: "ppPopper", 
          delay: 0 
     }); 
});

Portal.Portlet.HistoryDisplay = Portal.Portlet.extend({

	init: function(path, name, notifier) {
		console.info("Created History Ad...");
		this.base(path, name, notifier);    
	},
	
	send: {
      'Cmd': null
      //'Recording': null
    },   
    
    receive: function(responseObject, userArgs) {  
         var cmd = userArgs.cmd;
         var rootNode = document.getElementById('HTDisplay'); 
         var ul = document.getElementById('activity');
         var resp = responseObject.responseText;
             
         if (cmd == 'HTOn') { 
            rootNode.className = '';    // hide all msg and the turnOn link
            try {
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
             if ( rootNode.className == '') { //                 
                 rootNode.className = 'HTOn';  // show "Your browsing activity is empty." message                                  
                 if (ul != null) {
                     ul.className='hide'; 
                     ul.innerHTML = '';
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
         
         var site = document.forms[0]['p$st'].value;
         var cmd =  target.getAttribute("cmd");     
               
         // Issue asynchronous call to XHR service, callback is to update the portlet output
	     var resp = xmlHttpCall(site, this.path, cmd, {}, this.receive, {'cmd': target.getAttribute("cmd")}, this);
      }, 
      
      "HistoryOn<click>" : function(e, target, name){
         this.send.Cmd({'cmd': target.getAttribute("cmd")});
         //$PN('Pubmed_ResultsSearchController').getInput('RecordingHistory').value = 'yes';		 
         console.info("Inside HistoryOn in HistoryDisplay: " + target.getAttribute("cmd"));
                
         var site = document.forms[0]['p$st'].value;   
	     var resp = xmlHttpCall(site, this.path, "HTOn", {}, this.receive, {'cmd': target.getAttribute("cmd")}, this);
	     //Portal.requestSubmit();
      },
      
      "ClearHistory<click>" : function(e, target, name){
         this.send.Cmd({'cmd': target.getAttribute("cmd")});	
         var site = document.forms[0]['p$st'].value;   
	     var resp = xmlHttpCall(site, this.path, "ClearHT", {}, this.receive, {'cmd': target.getAttribute("cmd")}, this);	 
      }
    }
});

function getCitation(node) {
   // var withHistorys = cssQuery("*[title]", node); does not work!!!   
   var withTitles = $AN('title', node, '*');
   //alert(withTitles.length + " " + withTitles[0].tagName + " " + withTitles[withTitles.length-1].tagName);
   //alert(withTitles[1].getAttribute('title'));
   if (withTitles.length > 0) {      
      var p = withTitles[0];
      var s = p.getAttribute("title");
      if (s && s > "") { 
          //alert(s);
          p.setAttribute("title", "");
          node._title = s;
          for (var i = 0; i < withTitles.length; i++) {
              withTitles[i].removeAttribute("title");    
          }
      }
      /*else { //fixed in utils.js         
          s = node._title; // required for IE b/c of the bug in $AN
      }*/
   } else {
      s = node._title;
      //alert("old s "+ s);
   }
   return s;
}
// Requires: ElementPopper_JS, EventDispatcher_JS

//
// A DeferredElementPopper creates an ElementPopper on any node with a given className the first time the
// node receives a mouseover event. It collects all of the poppers it creates (in an array), and
// remembers the dispatcher rule that invokes the initialization.
//
// "content" is the same as "content" for ElementPopper (see which)
//
DeferredElementPopper = function(className, config) {
   var that = this;
   this.config = config;
   this.poppers = [];
   this.dispatcher = new EventDispatcher("mouseover", className, function(event, udata, dispatcher) {
         if (!this._popup_init) {
            this._popup_init = 1;
            that.poppers[that.poppers.length] = new ElementPopper(this, that.config.content, that.config);
         }
    });
    
}
DeferredElementPopper.prototype = {
   debug: function() {
     var s = "";
     for (var i = 0; i < this.poppers.length; i++) {
        var z = this.poppers[i];
        s += i + ": ";
        s += z.popnode.innerText + "(" + z.popnode.getAttribute("title") +"); ";
     }
     alert(s);
   }
};


//
// A EventDispatcher does a popup on demand based on a classname on an element.
// Every mouse motion on the page notifies EventDispatcher that an event occurred.
// Any time an event occurs that has one of EventDispatcher's classnames on it,
// the handlers associated with that className are executed.
//
// EventDispatcher is designed for popups, but can be used for anything; the handler
// is a generic function:
//    handler(event, udata, dispatchrule)
// <event> is the platform event object; use utils.getTargetObj() to get its target
// <udata> is the udata that was passed when that handler was set up.
// <dispatchrule> is the EventDispatcher object; it is the dictionary
// {eventType, className, handler, udata} describing the popper behavior.
//
// dispatchrule.remove() [TBD] removes the rule from the event dispatcher
//
// When the callback occurs, <this> is the target element. 

EventDispatcher = function(eventType, className, handler, udata) {

   // Start PP listening for this event
   if (!EventDispatcher.events) {
      EventDispatcher.events = {};
   }

    // If first definition for this event type, listen for it
   if (!EventDispatcher.events[eventType]) {
      EventDispatcher.events[eventType] = {};
      utils.addEvent(document.body, eventType, EventDispatcher.handleEvent);
   }
   
   // Index of all class names managed by PP
   if (!EventDispatcher.classNames[className]) {
      EventDispatcher.classNames[className] = 1;
   }

   // ev.classNames are the classes that listen for this event
   var ev = EventDispatcher.events[eventType];
   if (!ev.classNames) {
      ev.classNames = {};
   }
   
   // Add handler for this class
   if (!ev.classNames[className]) {
      ev.classNames[className] = [];
   }
   
   this.eventType = eventType;
   this.className = className;
   this.handler = handler;
   this.udata = udata || null;
   
   var c = ev.classNames[className];
   c[c.length] = this;
}

EventDispatcher.events = {}
EventDispatcher.classNames = {}

EventDispatcher.handleEvent = function(e) {

   if (!EventDispatcher.events[e.type]) { return; } // TODO: Remove event listener here
   
   var t = utils.getTargetObj(e);
   if (t.className) {
      var theClasses = t.className.split(/\s+/);
      for (var c in theClasses) {
         if (EventDispatcher.classNames[theClasses[c]]) {
            EventDispatcher.dispatch(e, t, theClasses[c]);
         }
      }
   }
}

EventDispatcher.dispatch = function(e, t, className) {
   if (!EventDispatcher.events[e.type]) return;
   var ev = EventDispatcher.events[e.type];
   if (!ev.classNames || !ev.classNames[className]) return;
   var poppers = ev.classNames[className];
   for (var i = 0; i < poppers.length; i++) {
      poppers[i].handleEvent(t, e);
   }
}

EventDispatcher.prototype = {
   handleEvent: function(target, event) {
      this.handler.call(target, event, this.udata, this);
   }
}



// PPMCRecentActivity
// 

Portal.Portlet.PPMCRecentActivity = Portal.Portlet.HistoryDisplay.extend({

    init: function(path, name, notifier) {
        console.info("Created PPMCRecentActivity");
        this.base(path, name, notifier);
    }
});


Portal.Portlet.Pubmed_Discovery_RA = Portal.Portlet.BriefLinkPageSection.extend({
    
    init: function(path, name, notifier) {
        console.info("Created Pubmed_Discovery_RA");
		this.base(path, name, notifier);
	},
	
	SetPortletName: function(){
	    Portal.Portlet.BriefLinkPageSection.portletname = 'Pubmed_Discovery_RA';
	}
});
Portal.Portlet.PPMCPubmedRA = Portal.Portlet.BriefLinkPageSection.extend({

    init: function(path, name, notifier) {
        console.info("Created PPMCPubmedRA");
        this.base(path, name, notifier);
    },

    SetPortletName: function(){
        Portal.Portlet.BriefLinkPageSection.portletname = 'PPMCPubmedRA';
    },

    popNodeDelay: 500
});
// PPMCArticlePageJS
// This script does the following:
// - Positions reference portlets in the discovery column to the top of each paragraph;
// - Calculates how many references will fit;
// - Calculates whether it needs a "see all" link;
// - Adds mouseovers to links in paragraphs and reference portlet links

// We use a self executing anonymous function in order to protect the global namespace from
// two top-level vars: '$j', and 'main'.

( function() {
    var $j = jQuery;

    // This main function runs when the DOM is ready to be queried; it gets attached to
    // the event down at the bottom of this module.
    var main = function() {
        //var start = new Date().getTime();
        var POPPER_DELAY = 500;

        //////////////////////////////////////////////////////////////////////
        // Helper functions

        //-------------------------------------------------------------------
        // This function determines if two rectangles intersect.
        // Two rectangles intersect if any point along their lines share a 
        // point in space.  Because these portlets will be lined up vertically, 
        // we only worry about one dimension.  The 'dims' parameter is an 
        // object that contains top and bottom dimensions of the two rectangles.

        var rectIntersect = function(dims) {
            // L1t is 'Line'1'Top' etc.
            var L1t = dims.L1t;
            var L1b = dims.L1b;
            var L2t = dims.L2t;
            var L2b = dims.L2b;
            return (
                (L2b > L1t && L2b < L1b) ||
                (L2t < L1b && L2t > L1t) ||
                (L2t > L1t && L2t < L1b) ||
                (L2t < L1t && L2b > L1b)
            );
        };

        //-------------------------------------------------------------------
        // This function adds a popup to the links within the main article body.
        // It adds the popup to one element, and then queues itself up to add the
        // popup to the next element after a short delay.

        var addPopper = function(elems, curr) {
            var currElem = elems[curr];
            var jqCurrElem = jQuery(currElem);

            var rid = jqCurrElem.attr('rid');
            //console.info("rid is '" + rid + "'");

            if (rid) {
                // Find the text for this popup from the element pointed to by 
                // @rid.  This will be in the reference section of the article.
                // Some id's from the backend have special characters, so we
                // need to escape them, see PMC-6384 and http://tinyurl.com/2qfqgc

                var ridEscaped = rid.replace(/:/g, "\\:")
                                    .replace(/\./g, "\\.")
                                    .replace(/·/g, "\\·");
                var refElem = jQuery('#' + ridEscaped);
                
                // Check to make sure that we found something
                if (refElem.length > 0) {

                    var citeText = refElem.text();
                    //console.info("citeText is '" + citeText + "'");

                    var popText = jQuery.trim(citeText);
                    //console.info("popText is '" + popText + "'");
    
                    // If the @href element of the current <a> tag (currElem)
                    // has a '#' portion, meaning that it is a link to the
                    // reference list in this same article, then indicate 
                    // that in the popup text.
                    if (currElem.hash) {
                        popText += ' [Reference List]';
                    }
    
                    new ElementPopper(currElem,
                        { content: popText, 
                          direction: PopDiv.POP_RIGHT, 
                          popperId: 'popper-para-links', 
                          delay: POPPER_DELAY });
                }
            }

            // Now arrange to have ourselves called for the next element that needs a popup,
            // after one millisecond.
            curr++;
            if (curr < elems.length) {
                var fnc = function(){
                    addPopper(elems, curr);
                }
                window.setTimeout(fnc, 1);
            }
        }; // end addPopper function

        //-------------------------------------------------------------------
        // This function adds a popup to the links in the cited-reference blocks.

        var addPopper2 = function(elems, curr) {
            var aDomElement = elems[curr];
            var aKids = aDomElement.childNodes;
            var aNumKids = aKids.length;
            //console.info("aDomElement is a " + aDomElement.toString() + "; num kids is " + aNumKids);

            // Generate the text that will go in the popup.
            // Get the text children of the <a> element.  This loop drops any element nodes;
            // in particular, the <span class='flag'>Review</span> element, if present.
            var txt = "";
            for (var i = 0; i < aNumKids; ++i) {
                var kid = aKids[i];
                if (kid.nodeType != 1) {
                    txt += kid.nodeValue;
                }
            }

            var l = jQuery(elems[curr]);
            txt += "<br/>" + l.next('p')[0].innerHTML;
            new ElementPopper(l[0],
                { content: txt, direction: PopDiv.POP_LEFT, popperId: 'popper-port-links', delay: POPPER_DELAY });

            // Now arrange to have ourselves called for the next element that needs a popup,
            // after one millisecond.
            curr++;
            if (curr < elems.length) {
                var fnc = function(){
                    addPopper2(elems, curr);
                }
                window.setTimeout(fnc, 1);
            }
        }; // end addPopper2 function

        // end helper function(s)

        //////////////////////////////////////////////////////////////////////
        // In this space, we define and set variables that only need to be set once, not per paragraph.

        // See if there any reference portlets in the page, by getting at least two reference
        // portlets. Later we will use these two portlets to make sure they do not interfere with
        // existing portlets in the column. It is unlikely that a third portlet would interfere
        // with anything, so let's make this as efficient as possible
        var portNodes = $j('#ArticleRefDocsums').find('div.cited-ref:lt(2)');

        // If there is at least one portlet in the page, we need to get some initial information,
        // like sample portlet elements' sizes, information about existing portlet in page etc.
        var portsInPage = false;
        if (portNodes.length > 0) { // if the page has ANY reference portlets
            // remember that there is something to do
            var portsInPage = true;
            var firstPortN = $j(portNodes[0]);

            // Max shown per page is already decided by xslt that generates portlet server-side,
            // and should be the number of <li>s per portlet
            var MAXREFS = 5;

            // get dimensions of elements inside sample portlet, account for padding by using outerHeight
            var lis = firstPortN.find('li.ovfl');
            var firstRefH = $j(lis[0]).outerHeight(true);
            var firstSeeAllH = firstPortN.find('a.all-articles').outerHeight(true);
            var firstUl = firstPortN.find('ul');
            // the ul might have padding or margins applied
            var ulPaddingMargin = parseInt(firstUl.css('padding-top')) +
                                  parseInt(firstUl.css('padding-bottom')) +
                                  parseInt(firstUl.css('margin-top')) +
                                  parseInt(firstUl.css('margin-bottom'));
            // same goes for the actual portlet div
            var portNPadding = parseInt(firstPortN.css('padding-top')) +
                               parseInt(firstPortN.css('padding-bottom'));

            // The horizontal position of all cited ref blocks should line up with recent history
            // portlet, which should be above any reference portlets
            var leftPos = $j('#recent-activity').position().left;
            //console.info('Got leftPos of all portlets: ' + leftPos);

            // Get positions of any existing portlets in side column, so we can later hide any
            // reference portlets that take up same space
            var sideBarCell = $j('td.sidebar-cell')[0];
            var portRanges = {};
            var ports = jQuery('div.port', sideBarCell);
            for (var i = 0; i < ports.length; i++) {
                var p = jQuery(ports[i]);
                var pos = p.position();
                var t = pos.top;
                portRanges[i] = {
                    't': t,
                    'b': t + p.height() // bottom is top + height of portlet
                };
            }

        } // end if portNodes in doc

        // Now, for each paragraph, get associated portlet and position appropriately, we still need
        // to iterate through each paragraph even if there are no reference links in entire document.
        // We are doing some weird stuff here.  We have to do iterate through each paragraph, but
        // for performance reasons, on large pages we need to pause a ms between iterations. So:
        // 1) we get all "paragraph" elements
        // 2) we execute the "loopdloop" function, which every ms passes all the paras to paraLoop
        // 3) paraLoop does all the positioning of the reference portlets
        // 4) When loopdloop is done, it calls hideInterfering, which hides any portlets 
        //    that overlap existing ones

        // First make a hash of all of the CRBs indexed by their @rid attributes.
        var crbs = $j('div.cited-ref');
        var numCrbs = crbs.length;
        //console.info("Number of crbs is " + numCrbs);

        var crbsByRid = {};
        for (var i = 0; i < numCrbs; ++i) {
            var crb = crbs[i];
            //console.info("crb is " + crb.toString());
            var jcrb = $j(crb);
            //console.info("jcrb is " + jcrb.toString());
            var rid = jcrb.attr('rid');
            //console.info("@rid is '" + rid + "'");
            crbsByRid[rid] = jcrb;
        }

        //var paraLoop = function(crb) {
        //    var jcrb = $j(crb);
        //    var paraRid = jcrb.attr('rid');
        //    var p = $j('#' + paraRid);
        var paraLoop = function(para) {
            //console.group('para');
            var p = $j(para);
            //console.info(p[0]);

            // Get the @id attribute, and from that, look up the CRB.
            var paraId = p.attr('id');
            //console.info("paragraph @id is " + paraId);
            var jcrb = crbsByRid[paraId];

            if (jcrb) {
                //console.info("jcrb is " + jcrb.toString());

                // if a 'paragraph' has an associated portlet, it will have a 'cite' attribute on it
                // CFM:  taking this check out, the @cite attribute is going away, see PMC-5595:
                //var citeAttr = p.attr('cite');
                //if (citeAttr) {

                // Get the number of unique references per paragraph.  This could be more than the
                // max shown in portlet.
                // CFM:  Now, getting this info from the crb, not the paragraph, because the @pmids
                // attribute will be going away from the paragraph, see PMC-5595.
//              var numRefs = p.attr('pmids').split(' ').length;
                var numRefs = jcrb.attr('pmids').split(' ').length;
                //console.info("numRefs is " + numRefs);

                // get paragraph dims. We need these to position portlet to paragraph
                var pPos = p.position();
                var pPosX = pPos.left;

                 // needs to be a number because we will add/subtract margin
                var pPosY = parseInt(pPos.top);
                var pPosH = p.height();

                // Get paragraph top and bottom margins.  We need to account for these when calculating 
                // reference portlet placement. Margin is not accounted for when getting an element's 
                // height, but it does affect appearance of element's dimensions when there is no border.
                var pMarT = parseInt(p.css('marginTop'));
                var pMarB = parseInt(p.css('marginBottom'));

                // Get associated portlet from 'cited' attribute, we can now safely overwrite portN 
                // variable from beginning of script.
                // CFM:  change from using 'portN' to using the variable 'jcrb', from above.
                //var portN = $j('#' + citeAttr);
                //var refNodes = portN.find('li');
                var refNodes = jcrb.find('li');
                var numRefsInP = refNodes.length;
                //console.info("numRefsInP is " + numRefsInP);

                // Set styles on portlet node to align to paragraph
                // We set portlet node to position: absolute in the css file, because that is more
                // efficient than setting it in JavaScript, and that property does not change.
//              portN.css( {
                jcrb.css( {
                    'top': pPosY + pMarT + 2 + 'px', // Add an xtra 2px, otherwise it appears too low
                    'left': leftPos + 'px'
                });

                // Calculate how many refs will fit per paragraph and if we need the 
                // 'See more articles cited in this paragraph' link. 
                var refsFit = null;
                var needAllLink = false;
                if (numRefs > MAXREFS || numRefs * firstRefH >= pPosH - pMarB) {
                    //console.info('more links than allowed, OR links allowed are higher than paragraph height');
                    refsFit = Math.floor( pPosH / (firstRefH + firstSeeAllH) );
                    needAllLink = true;
                    if (refsFit > MAXREFS) {
                        refsFit = MAXREFS;
                    }
                    //console.info('refsFit: ' + refsFit);
                } else {
                    //console.info('all refs in para fit');
                    refsFit = numRefs;
                    //console.info('refsFit: ' + refsFit);
                }

                var aSeeArticles = jcrb.find('a.all-articles');
                if (!needAllLink) {
                    aSeeArticles.hide();
                }
                else if (refsFit == 0) {
                    // If the visible number of links is zero, then change the text from
                    // "See more articles ..." to just "See articles ...".
                    // See PMC-6076.
                    var seeMoreT = aSeeArticles.text();
                    aSeeArticles.text( seeMoreT.replace(/more /, "") );
                    // After all, we decided that we'd rather just not see it:
                    jcrb.hide();
                }

                //console.info('need to take off refs:');
                // how many references (lis) from the portlet do we need to hide?
                var takeOff = numRefsInP - refsFit;
                //console.info(takeOff);

                if ( takeOff > 0 ) {
                    // we need to take off some nodes, loop through li nodes in reverse order and hide them
                    for (var i = numRefsInP - 1; i >= refsFit; i--) {
                        $j(refNodes[i]).hide();
                    }
                } // end if we need to take off nodes

                // we now need to add mouseovers on reference links that are not hidden. We get the text
                // from the title attribute of the link
                //var elems2 = portN.find('a.popnode:visible');
                var elems2 = jcrb.find('a.popnode:visible');
                //console.info(elems2);

                if (elems2.length > 0) {
                    addPopper2(elems2, 0);
                }

            } // end if jcrb

            // Now, we are back touching on each paragraph, regardless whether there 
            // are links with pmids or not we need to get all the reference links per 
            // paragraph and do the mouseover which links to reference section at the
            // bottom of the page.  This is a little Eric trick for performance. We get 
            // the links in the paragraph and create our own custom loop and setTimeout 
            // for each link.  This allows "multithreading" and doesn't lock up the 
            // browser.
            var elems = p.find('a.cite-reflink');
            if (elems.length > 0) {
                addPopper(elems, 0);
            }

            //console.groupEnd('para');
        }; // end paraloop function

        var hideInterfering = function(){
            // At this point all reference divs are positioned in document. We now check that 
            // the first two reference portlets (which were already queried for in beginning 
            // of the script don't interfere with any other portlets that may be at the top of 
            // column.  Loop thru them and compare them against what exists in portRanges
            if (portsInPage === true) {
                portNodes.each( function() {
                    var citedRefJ = $j(this);
                    // We refer to portRanges, which is an obj in which we stored dims of existing
                    // portlets before positioning reference portlets
                    for (var i in portRanges){
                        var range = portRanges[i];

                        var existingT = range.t;
                        var existingB = range.b;

                        var citedRefT = citedRefJ.position().top;

                        // This is the parameter to the rectIntersect helper func defined in the beginning
                        // of the script.  It contains top and bottom dims for two rectanges.  The
                        // rectIntersect returns true if they intersect.
                        // PMC-6175:  add a little buffer of ten pixels.
                        var dims = {
                            'L1t': existingT, // we are comparing against the existing portlet
                            'L1b': existingB + 10,  // little buffer to be sure.
                            'L2t': citedRefT,
                            'L2b': citedRefT + citedRefJ.height()
                        }
                        //console.info('L1t: ' + existingT);
                        //console.info('L1b: ' + existingB);
                        //console.info('L2t: ' + citedRefT);
                        //console.info('L2b: ' + (citedRefT + citedRefJ.height()));

                        if ( rectIntersect(dims) === true) {
                            //console.info('interferes');
                            citedRefJ.css('display', 'none');
                        }
                    }
                }); // end iterating through first two existing portlets
            }
        }

        // Get the list of all paragraphs in the document.
        var allParas = $j('div.p');
        var numParas = allParas.length;

        var loopdloop = function(curr){
          paraLoop(allParas[curr++]);
          if (curr >= numParas) {
            hideInterfering();
          } else {
            setTimeout( function(){ loopdloop(curr); }, 1);
          }
        }
        //document.title = new Date().getTime() - start.toString() + 'ms';
        loopdloop(0);
    };


    // We use window.load for Safari, not ready, because in Safari, we cannot be 
    // sure all CSS has loaded, and we cannot be sure
    // dims of images are accounted for when getting top positions of paragraphs
    if ( $j.browser.safari) {
        $j(window).load(main);
    } else {
        $j(main);
    }

})();


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

