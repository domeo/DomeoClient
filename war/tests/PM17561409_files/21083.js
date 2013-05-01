Portal.Portlet.DbConnector = Portal.Portlet.extend({

	init: function(path, name, notifier) {
		var oThis = this;
		console.info("Created DbConnector");
		this.base(path, name, notifier);
		
		// reset Db value to original value on page load. Since LastDb is the same value as Db on page load and LastDb is not changed on
		// the client, this value can be used to reset Db. This is a fix for back button use.
		if (this.getValue("Db") != this.getValue("LastDb")){
		    this.setValue("Db", this.getValue("LastDb"));
		}
     
		// the SelectedIdList and id count from previous iteration (use a different attribute from IdsFromResult to prevent back button issues)
		Portal.Portlet.DbConnector.originalIdList = this.getValue("LastIdsFromResult");
		console.info("originalIdList " + Portal.Portlet.DbConnector.originalIdList);
		// if there is an IdList from last iteration set the count
		if (Portal.Portlet.DbConnector.originalIdList != ''){
			Portal.Portlet.DbConnector.originalCount = Portal.Portlet.DbConnector.originalIdList.split(/,/).length;
		}

		notifier.setListener(this, 'HistoryCmd', 
        	function(oListener, custom_data, sMessage, oNotifierObj) {
           		var sbTabCmd = $N(oThis.path + '.TabCmd');
           		sbTabCmd[0].value = custom_data.tab;
        	}
    		, null);
    
	},

	send: {
   		'SelectedItemCountChanged': null,
   		'newUidSelectionList': null,
   		'SavedSelectedItemCount': null,
   		'SavedUidList': null
	},

	listen: {
	
		//message from Display bar on Presentation change 
		'PresentationChange' : function(sMessage, oData, sSrc){
			
			// set link information only if it exists
			if (oData.dbfrom){
				console.info("Inside PresentationChange in DbConnector: " + oData.readablename);
				this.setValue("Db", oData.dbto);
				this.setValue("LinkSrcDb", oData.dbfrom);
				this.setValue("LinkName", oData.linkname);
				this.setValue("LinkReadableName", oData.readablename);
			}
			//document.forms[0].submit();
		},
		
		// various commands associated with clicking different form control elements
		'Cmd' : function(sMessage, oData, sSrc){
			console.info("Inside Cmd in DbConnector: " + oData.cmd);
			this.setValue("Cmd", oData.cmd);
			
			// back button fix, clear TabCmd
			if (oData.cmd == 'Go' || oData.cmd == 'PageChanged' || oData.cmd == 'FilterChanged' || 
			oData.cmd == 'DisplayChanged' || oData.cmd == 'HistorySearch' || oData.cmd == 'Text' || 
			oData.cmd == 'File' || oData.cmd == 'Printer' || oData.cmd == 'Order' || 
			oData.cmd == 'Add to Clipboard' || oData.cmd == 'Remove from Clipboard' || 
			oData.cmd.toLowerCase().match('details')){
				this.setValue("TabCmd", '');
				console.info("Inside Cmd in DbConnector, reset TabCmd: " + this.getValue('TabCmd'));
			}

		},
		
		
		// the term to be shown in the search bar, and used from searching
		'Term' : function(sMessage, oData, sSrc){
			console.info("Inside Term in DbConnector: " + oData.term);
			this.setValue("Term", oData.term);
		},
		
		
		// to indicate the Command Tab to be in
		'TabCmd' : function(sMessage, oData, sSrc){
			console.info("Inside TABCMD in DbConnector: " + oData.tab);
			this.setValue("TabCmd", oData.tab);
			console.info("DbConnector TabCmd: " + this.getValue("TabCmd"));
		},
		
		
		// message sent from SearchBar when db is changed while in a Command Tab
		'DbChanged' : function(sMessage, oData, sSrc){
			console.info("Inside DbChanged in DbConnector");
			this.setValue("Db", oData.db);
		},
		
		// Handles item select/deselect events
		// Argument is { 'id': item-id, 'selected': true or false }
		'ItemSelectionChanged' : function(sMessage, oData, oSrc) {
			var sSelection = this.getValue("IdsFromResult");
			var bAlreadySelected = (new RegExp("\\b" + oData.id + "\\b").exec(sSelection) != null);
	       	var count =0;
	       	
			if (oData.selected && !bAlreadySelected) {
				sSelection += ((sSelection > "") ? "," : "") + oData.id;
			   	this.setValue("IdsFromResult", sSelection);
			   	if (sSelection.length > 0){
			   		count = sSelection.split(',').length;
			   	}
			   	this.send.SelectedItemCountChanged({'count': count});
			   	this.send.newUidSelectionList({'list': sSelection});
		   	} else if (!oData.selected && bAlreadySelected) {
				sSelection = sSelection.replace(new RegExp("^"+oData.id+"\\b,?|,?\\b"+oData.id+"\\b"), '');
		   	   	this.setValue("IdsFromResult", sSelection);
				console.info("Message ItemSelectionChanged - IdsFromResult after change:  " + this.getValue("IdsFromResult"));
			   	if (sSelection.length > 0){
			   		count = sSelection.split(',').length;
			   	}
				console.info("Message ItemSelectionChanged - IdsFromResult length:  " + count);   
				this.send.SelectedItemCountChanged({'count': count});
			   	this.send.newUidSelectionList({'list': sSelection});
		   	}
		},
				
		// FIXME: This is the "old message" that is being phased out.
		// when result citations are selected, the list of selected ids are intercepted here,
		// and notification sent that selected item count has changed.
		'newSelection' : function(sMessage, oData, sSrc){
		
			// Check if we already have such IDs in the list
			var newList = new Array();
			var haveNow = new Array();
			if(Portal.Portlet.DbConnector.originalIdList){
				haveNow = Portal.Portlet.DbConnector.originalIdList.split(',');
				newList = haveNow;
			}
			
			var cameNew = new Array();
			if (oData.selectionList.length > 0) {
				cameNew = oData.selectionList;
			}
			
			if (cameNew.length > 0) {
				for(var ind=0;ind<cameNew.length;ind++) {
					var found = 0;
					for(var i=0;i<haveNow.length;i++) {
						if (cameNew[ind] == haveNow[i]) {
							found = 1;
							break;
						}
					}
						//Add this ID if it is not in the list
					if (found == 0) {
						newList.push(cameNew[ind]);
					}
				}
			}
			else {
				newList = haveNow;
			}

				// if there was an IdList from last iteration add new values to old
			var count = 0;
			if ((newList.length > 0) && (newList[0].length > 0)){
				count = newList.length;
			}
			
			console.info("id count = " + count);
			this.setValue("IdsFromResult", newList.join(","));
			
			this.send.SelectedItemCountChanged({'count': count});
			this.send.newUidSelectionList({'list': newList.join(",")});
		},


		// empty local idlist when list was being collected for other purposes.
		//used by Mesh and Journals (empty UidList should not be distributed, otherwise Journals breaks)
		// now used by all reports for remove from clipboard function.
		'ClearIdList' : function(sMessage, oData, sSrc){
			this.setValue("IdsFromResult", '');
			this.send.SelectedItemCountChanged({'count': '0'});
			this.send.newUidSelectionList({'list': ''});
		}, 


		// back button fix: when search backend click go or hot enter on term field,
		//it also sends db. this db should be same as dbconnector's db
		'SearchBarSearch' : function(sMessage, oData, sSrc){
			if (this.getValue("Db") != oData.db){
				this.setValue("Db", oData.db);
			}
		},
		
		// back button fix: whrn links is selected from DisplayBar,
		//ResultsSearchController sends the LastQueryKey from the results on the page
		// (should not be needed by Entrez 3 code)
		'LastQueryKey' : function(sMessage, oData, sSrc){
			if (this.getInput("LastQueryKey")){
				this.setValue("LastQueryKey", oData.qk);
			}
		},
		
		'QueryKey' : function(sMessage, oData, sSrc){
			if (this.getInput("QueryKey")){
				this.setValue("QueryKey", oData.qk);
			}
		},
		
		
		//ResultsSearchController asks for the initial item count in case of send to file 
		'needSavedSelectedItemCount' : function(sMessage, oData, sSrc){
			var count = 0;
			if(this.getInput("IdsFromResult")){
				if (this.getValue("IdsFromResult").length > 0){
					count = this.getValue("IdsFromResult").split(',').length;
				}
				console.info("sending SavedSelectedItemCount from IdsFromResult: " + count);
			}
			else{
				count = Portal.Portlet.DbConnector.originalCount;
				console.info("sending SavedSelectedItemCount from OriginalCount: " + count);
			}
			this.send.SavedSelectedItemCount({'count': count});
		},
		
		// Force form submit, optionally passing db, term and cmd parameters
		'ForceSubmit': function (sMessage, oData, sSrc)
		{
		    if (oData.db)
    			this.setValue("Db", oData.db);
		    if (oData.cmd)
    			this.setValue("Cmd", oData.cmd);
		    if (oData.term)
    			this.setValue("Term", oData.term);
    		Portal.requestSubmit ();
		},
		
		'LinkName': function (sMessage, oData, sSrc){
		    this.setValue("LinkName", oData.linkname);
		},
		
		'SendSavedUidList': function (sMessage, oData, sSrc){
		    this.send.SavedUidList({'idlist': this.getValue("IdsFromResult")});
		}
		
	}, //listen
	
	/* other portlet functions */
	
	// DisplayBar in new design wants selected item count
	'SelectedItemCount': function(){
	    var count = 0;
		if(this.getInput("IdsFromResult")){
			if (this.getValue("IdsFromResult") != ''){
				count = this.getValue("IdsFromResult").split(',').length;
			}
		}
		else{
			count = Portal.Portlet.DbConnector.originalCount;
		}
		return count;
	}
		
},
{
	originalIdList: '',
	originalCount: 0
});

function getEntrezSelectedItemCount() {
    return $PN('DbConnector').SelectedItemCount();
}

Portal.Portlet.EmailTab = Portal.Portlet.extend({

	init: function(path, name, notifier) {
		this.base(path, name, notifier);
	},
	
	listen: {
		
		/* browser events */
		
		'SendMail': function(sMessage, oData, sSrc) {
			this.setValue('EmailReport', oData.report);
			this.setValue('EmailFormat', oData.format);
			this.setValue('EmailCount', oData.count);
			this.setValue('EmailStart', oData.start);
			this.setValue('EmailSort', oData.sort);
			this.setValue('Email', oData.email);
			this.setValue('EmailText', oData.text);
            this.setValue('EmailQueryKey', oData.querykey);
			this.setValue('QueryDescription', oData.querydesc);
		}

	}
});
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
		 
(function( $ ){ // pass in $ to self exec anon fn
    // on page ready
    $( function() {
        $('li.ralinkpopper').each( function(){
            var $this = $( this );
            var popper = $this.find('a.ralinkpopperctrl') ;
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

Portal.Portlet.LinkListPageSection = Portal.Portlet.NCBIPageSection.extend ({
	init: function (path, name, notifier){
		this.base (path, name, notifier);
	},
	
	"getPortletPath" : function(){
	    return (this.realname + ".NCBIPageSection");
	}
});
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
Portal.Portlet.DiscoveryDbLinks = Portal.Portlet.BriefLinkPageSection.extend({
    
    init: function(path, name, notifier) {
		this.base(path, name, notifier);
	},
	
	"getPortletPath" : function(){
	    return (this.realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection");
	}
});
Portal.Portlet.Pubmed_DiscoveryDbLinks = Portal.Portlet.DiscoveryDbLinks.extend({
    
    init: function(path, name, notifier) {
		this.base(path, name, notifier);
	},
	
	"getPortletPath" : function(){
	    return (this.realname + ".DiscoveryDbLinks.BriefLinkPageSection.LinkListPageSection.NCBIPageSection");
	}
});
Portal.Portlet.Pubmed_Discovery_PMC = Portal.Portlet.BriefLinkPageSection.extend({
    
    init: function(path, name, notifier) {
        console.info("Created Pubmed_Discovery_PMC");
		this.base(path, name, notifier);
	},
	
	"getPortletPath" : function(){
	    return (this.realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection");
	}
});
Portal.Portlet.Pubmed_Discovery_RA = Portal.Portlet.BriefLinkPageSection.extend({
    
    init: function(path, name, notifier) {
        console.info("Created Pubmed_Discovery_RA");
		this.base(path, name, notifier);
	},
	
	/*SetPortletName: function(){
	    Portal.Portlet.BriefLinkPageSection.portletname = 'Pubmed_Discovery_RA';
	}*/
	
	"getPortletPath" : function(){
	    return (this.realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection");
	}
});
jQuery(function(){
    if (typeof imageStripObj !== 'undefined'){
        jQuery(".img_strip_arrow").hover(selectLeftRight,unSelectLeftRight);
        jQuery(".img_strip_arrow").bind("click",moveLeftRight);
        jQuery(window).bind("resize",resizeImageStrip).bind("load",resizeImageStrip);
        imageStripObj.showImageStrip = function () {resizeImageStrip();};
    }
    
    function moveLeftRight(e){
         e.preventDefault();
         var pingData = {};
         if (jQuery(this).hasClass("img_strip_arrow_inactiv")){
             pingData.Scrolled = "No";
             pingData.ScrollSide = jQuery(this).hasClass("img_strip_arrow_right") ? "Right" : "Left";
         }
         else{
             pingData.Scrolled = "Yes";
            if (jQuery(this).hasClass("img_strip_arrow_right")){
                pingData.ScrollSide = "Right";
                imageStripObj.start = (imageStripObj.start == 0 ) ? imageStripObj.count -1 : imageStripObj.start - 1;
            }
            else{
                pingData.ScrollSide = "Left";
                imageStripObj.start = (imageStripObj.start == imageStripObj.count -1 ) ? 0 : imageStripObj.start + 1;
            }
            for (i = imageStripObj.start, j=0; j < imageStripObj.maxImagesPerView; i++, j++){
                var ind = i % (imageStripObj.count);
                var colegendId = "imgstrip_colegend" + ind;
                var colegend = '<div style="display:none;" id="' + colegendId + '">' + 
                    '<div class="legend-from"><span class="legend-from-title">' + decodeURI(imageStripObj.images[ind].Label) + '</span>' + decodeURI(imageStripObj.images[ind].CaptionTitle) + '</div>' +
                    '<div>' + imageStripObj.images[ind].Caption + '</div>' +
                    '<div class="legend-citation">' +
                        '<div><a href="' + imageStripObj.images[ind].SourceId + '"' +
                        ' ref="log$=' + imageStripObj.ImageStripLogVal + '_title_link&amp;'+ imageStripObj.ImageStripPreviewLogName + '=' + imageStripObj.images[ind].ImagePosition +
                        '&amp;ncbi_uid=' + imageStripObj.images[ind].ncbi_uid + '&amp;link_uid=' + imageStripObj.images[ind].link_uid + '">' + decodeURI(imageStripObj.images[ind].ArticleTitle) + '</a></div>' +
                        '<div>' + (imageStripObj.images[ind].Citation != ''? imageStripObj.images[ind].Citation : imageStripObj.CommonCitation) + ' </div>' +
                    '</div> </div>';
                              
                var thumb = '<a class="figpopup" co-legend-rid="' + colegendId + '" href="' + imageStripObj.images[ind].ImageSource + '"' +
                    'ref="log$=' + imageStripObj.ImageStripLogVal + '&amp;'+ imageStripObj.ImageStripPreviewLogName + '=' + imageStripObj.images[ind].ImagePosition + 
                    '&amp;ncbi_uid=' + imageStripObj.images[ind].ncbi_uid + '&amp;link_uid=' + imageStripObj.images[ind].link_uid + '" ' +
                    'image-pos="' + imageStripObj.images[ind].ImagePosition  + '"' +
                    '><img src="' + imageStripObj.images[ind].ThumbUrl + '" src-large="' + imageStripObj.images[ind].ImageUrl + '" alt="' + decodeURI(imageStripObj.images[ind].Label) + '"></a>' + colegend;
            
                
                var tmpContent = jQuery(thumb);
                jQuery(jQuery(".img_strip_item")[j]).html(tmpContent);
                
                /*
                var tmpDiv = jQuery(jQuery(".img_strip_item")[j]);
                var thumbUrl = imageStripObj.images[ind].ThumbUrl;
                //the closure here would only update the last content, thus an outside function
                htmlAfterImageLoad(jQuery(jQuery(".img_strip_item")[j]),imageStripObj.images[ind].ThumbUrl,thumb,j+1);
                */
                
            }
            var fp_cfg = new jQuery.fn.figPopup();
            jQuery('.img_strip .figpopup').hoverIntent(fp_cfg).popupSensor();
            window.setTimeout(function (){resizeImageStrip();},200);
            //resizeImageStrip();
        }
        ncbi.sg.ping(pingData);
    }
    function htmlAfterImageLoad(divNode,thumbUrl,thumb,pos){
        jQuery('<img/>').attr('src',thumbUrl).load(function(){
            divNode.html(jQuery(thumb));
            var fp_cfg = new jQuery.fn.figPopup();
            jQuery('.img_strip .pos' + pos + ' .figpopup').hoverIntent(fp_cfg).popupSensor();
        });
        
    }
    function selectLeftRight(){
        if (!jQuery(this).hasClass("img_strip_arrow_inactiv"))
            jQuery(this).addClass("img_strip_arrow_select");
    }
    function unSelectLeftRight(){
        jQuery(this).removeClass("img_strip_arrow_select");
    }
    function resizeImageStrip(){
        var  minImageStripWidth = imageStripObj.minWidth !== 'undefined' ? imageStripObj.minWidth : 0;
        var availAreaWidth = jQuery(".rprt").width();
        //calc the width of the container
        var cont = jQuery(imageStripObj.containerSelector);
        function isNumber (v) { return ! isNaN (v-0);}
        function getNumberVal(v) {return isNumber(v)? v : 0;}
        var padBorder = getNumberVal(parseInt(cont.css("margin-left").replace("px",""))) + getNumberVal(parseInt(cont.css("margin-right").replace("px",""))) +
            getNumberVal(parseInt(cont.css("padding-left").replace("px",""))) + getNumberVal(parseInt(cont.css("padding-right").replace("px","")));
        availAreaWidth -= padBorder;
        var posssibleNumImages = parseInt((availAreaWidth - 40) / 120);
        imageStripObj.imagesPerView = (imageStripObj.count > posssibleNumImages) ? posssibleNumImages : imageStripObj.count;
        imageStripObj.imagesPerView = (imageStripObj.maxImagesPerView < imageStripObj.imagesPerView) ? imageStripObj.maxImagesPerView : imageStripObj.imagesPerView;
        var calcWidth = 120 * imageStripObj.imagesPerView + 40 ;
        calcWidth = calcWidth < minImageStripWidth ? minImageStripWidth : calcWidth;
        jQuery(".img_strip").css("width", calcWidth + "px");
        cont.css("width", (calcWidth + padBorder) + "px");
        if (imageStripObj.count <= imageStripObj.imagesPerView)
            jQuery(".img_strip_arrow").addClass("img_strip_arrow_inactiv");
        else
            jQuery(".img_strip_arrow").removeClass("img_strip_arrow_inactiv");
        
        //determine the heights and hide and show the thumbs
        jQuery(".img_strip_item").filter(function(){
                return jQuery(this).attr("class").match(/pos?/);
            }).hide();
        var maxImgHeight =0;
        for(var i=0; i<imageStripObj.imagesPerView; i++){
            var currHeight =  jQuery(".img_strip_item").filter(".pos" + (i+1)).show().height();
            maxImgHeight = maxImgHeight > currHeight ? maxImgHeight : currHeight;
        }
        
        jQuery(".img_strip").css("height",(maxImgHeight + 20) + "px");
        jQuery(".img_strip_arrow").css("height",(maxImgHeight + 20) + "px");
    }
    

});


Portal.Portlet.Entrez_RVBasicReport = Portal.Portlet.extend({
	
	init: function(path, name, notifier) {
		console.info("Created report portlet");
		this.base(path, name, notifier);
	},
	
	send: {
		'ItemSelectionChanged': null,
		'ClearIdList': null,
		'Cmd': null
	},
	
	listen: {
		"uid<click>" : function(e, target, name){
		    this.UidClick(e, target, name);
		},
		
		"RemoveClip<click>" : function(e, target, name){
		    this.ClipRemoveClick(e, target, name);              
		}
	},
	
	'UidClick': function(e, target, name){	
		this.send.ItemSelectionChanged( { 'id': target.value,
		                                  'selected': target.checked });
	},
	
	'ClipRemoveClick': function(e, target, name){
	    this.send.ClearIdList();
		this.send.Cmd({'cmd': 'deletefromclipboard'});
		this.send.ItemSelectionChanged( { 'id': target.getAttribute('uid'),
		                                  'selected': true });
		Portal.requestSubmit();
	}
});
   

Portal.Portlet.Pubmed_RVAbstract = Portal.Portlet.Entrez_RVBasicReport.extend({
	init: function(path, name, notifier) {
		this.base(path, name, notifier);
	},
	
	send: {
		'ItemSelectionChanged': null,
		'ClearIdList': null,
		'Cmd': null,
        'AppendTerm': null
	},
	
	listen: {
		"img_strip_Closed<click>" : function(e, target, name){
		    this.saveImageStripStateLocal(e, target, name);
		},
		"uid<click>" : function(e, target, name){
		    this.UidClick(e, target, name);
		},
		
		"RemoveClip<click>" : function(e, target, name){
		    this.ClipRemoveClick(e, target, name);              
		}
	},
	"saveImageStripStateLocal":function(e, target, name){
	    var site = document.forms[0]['p$st'].value;
	    var args = {"ImageStripClosed": this.getValue("img_strip_Closed")};
	    // Issue asynchronous call to XHR service
        var resp = xmlHttpCall(site, this.realname, "SaveImageStripState", args, this.receiveImageStripState, {}, this);    
	},
    'receiveImageStripState':function(responseObject, userArgs){
        var resp = responseObject.responseText;
        try {
            // Handle timeouts
            if (responseObject.status == 408) {
                //this.showMessage("Server currently unavailable. Please check connection and try again.","error");
                alert("Server currently unavailable. Please check connection and try again.");
                return;
            }
/*            console.log("response = " + resp);
            resp = '(' + resp + ')';
            var JSONobj = eval(resp);
            console.dir(JSONobj); */           
 
        } catch (e) {
            //this.showMessage("Server error: " + e, "error");
            alert("Server error: " + e);
        }
    }
	
});

jQuery(document).ready( function(){
    jQuery("span.status_icon").click(
        function(e){
            e = e || window.event;
            ncbi.sg.ping (this, e, "mistakenlink");
        }
    );
});   

function HistViewTerm(term, op, num) {
    Portal.$send('AppendTerm', {'op': op, 'term': term.replace(/%22/g,"\"")})
}

jQuery(function(){
    var AL_Cache = new Object();
    var AL_cache_key;
    var AL_CgiUrl = "/entrez/utils/abstract_link.fcgi";
    //var AL_CgiUrl = "http://pdev2.be-md.ncbi.nlm.nih.gov:2441/entrez/utils/abstract_link.cgi";
    jQuery('a').filter('[abstractLink=yes]').bind('click',function(e){
        var oThis = this;
        var sec=jQuery(this).attr('alsec');
        var term=jQuery(this).attr('alterm')
        var url = AL_CgiUrl+"?db=pubmed&base=1&sec="+sec+"&term="+encodeURIComponent(term);        
        jQuery(this).ncbilinksmenu({"webservice" : url }).ncbilinksmenu('open');
        e.preventDefault();
    });
});



jQuery(function(){
    if (typeof imageStripObj !== 'undefined'){
        jQuery("div.pmc_images p.img_strip_header").bind("click",openCloseImageStrip);
        jQuery("span.img_strip_title").bind("click",function(e) { e = e || window.event; ncbi.sg.ping(this,e,"mistakenlink");});
        if (jQuery("#img_strip_Closed").val() == 'true')
                closeImageStrip();
    }
    
    function openCloseImageStrip(event){
        if (event.target.tagName.toUpperCase() != 'P'){
            return;
        }
        var pingData = {};
        if (jQuery("#img_strip_Closed").val() == 'false')
            closeImageStrip();
        else
            openImageStrip()
        pingData.ImageStripClosed = jQuery("#img_strip_Closed").val();
        ncbi.sg.ping(pingData);
        jQuery("#img_strip_Closed").trigger("click");
    }
    
    function openImageStrip(){
           jQuery("#img_strip_Closed").val('false');
           jQuery(".pmc_images").removeClass("img_strip_closed");
           jQuery(".pmc_images .img_strip_header").removeClass("img_strip_closed_icon").addClass("img_strip_open_icon");
    }
    function closeImageStrip(){
            jQuery("#img_strip_Closed").val('true');
            jQuery(".pmc_images").addClass("img_strip_closed");
            jQuery(".pmc_images .img_strip_header").addClass("img_strip_closed_icon").removeClass("img_strip_open_icon");
    }
    
});



Portal.Portlet.Entrez_ResultsController = Portal.Portlet.extend({

	init: function(path, name, notifier) {
		console.info("Created Entrez_ResultsController");
		this.base(path, name, notifier);
	},	
		
	send: {
	    'Cmd': null
	},
		
	listen: {
	
	    /* page events */
	    
	    "RemoveFromClipboard<click>": function(e, target, name){
            this.RemoveFromClipboardClick(e, target, name);
	    },
	    
		/* messages */
		
		'Cmd': function(sMessage, oData, sSrc){
		    this.ReceivedCmd(sMessage, oData, sSrc);
		},
		
		'SelectedItemCountChanged' : function(sMessage, oData, sSrc){
		    this.ItemSelectionChangedMsg(sMessage, oData, sSrc);
		},
		
		// currently sent by searchbox pubmed in journals 
		'RunLastQuery' : function(sMessage, oData, sSrc){
			if (this.getInput("RunLastQuery")){
				this.setValue ("RunLastQuery", 'true');
			}
		}
		
	},//listen
	
	'RemoveFromClipboardClick': function(e, target, name){
	    if(confirm("Are you sure you want to delete these items from the Clipboard?")){
	        this.send.Cmd({'cmd': 'deletefromclipboard'});
		    Portal.requestSubmit();  
    	}
	},
	
	// fix to not show remove selected items message when Remove from clipboard was clicked directly on one item
	'ReceivedCmd': function(sMessage, oData, sSrc){
	    if (oData.cmd == 'deletefromclipboard'){
	        Portal.Portlet.Entrez_ResultsController.RemoveOneClip = true;
	    }
	},
	
	'ItemSelectionChangedMsg': function(sMessage, oData, sSrc){
	    // do not show any messages if one item from clipbaord was removed with direct click.
	    if (Portal.Portlet.Entrez_ResultsController.RemoveOneClip){
	        Portal.Portlet.Entrez_ResultsController.RemoveOneClip = false;
	    }
	    else{
    		this.SelectedItemsMsg(oData.count);
    	    this.ClipRemoveMsg(oData.count);
    	}
	},
	
	'SelectedItemsMsg': function(count){
	    SelMsgNode = document.getElementById('result_sel');
	    if (SelMsgNode){
	        if (count > 0){
	            SelMsgNode.className = 'result_sel';
 	            SelMsgNode.innerHTML = "Selected: " + count;
 	        }
 	        else {
 	            SelMsgNode.className = 'none';
 	            SelMsgNode.innerHTML = "";
 	        }
	    }
	},
	
	'ClipRemoveMsg': function(count){
	    ClipRemNode = document.getElementById('rem_clips');
 	    if (ClipRemNode){
 	        if (count > 0){
 	            ClipRemNode.innerHTML = "Remove selected items";
 	        }
 	        else {
 	            ClipRemNode.innerHTML = "Remove all items";
 	        }
 	    }
	},
	
	'ResultCount': function(){
	    var totalCount = parseInt(this.getValue("ResultCount"));
	    totalCount = totalCount > 0 ? totalCount : 0;
	    return totalCount;
	}

},
{
    RemoveOneClip: false
});

function getEntrezResultCount() {
    return $PN('Entrez_ResultsController').ResultCount();
}

Portal.Portlet.Pubmed_ResultsController = Portal.Portlet.Entrez_ResultsController.extend({

	init: function(path, name, notifier) {
		this.base(path, name, notifier);
	}
});

function getEntrezResultCount() {
    return $PN('Pubmed_ResultsController').ResultCount();
}
Portal.Portlet.Entrez_Messages = Portal.Portlet.extend({

	init: function(path, name, notifier) {
		this.base(path, name, notifier);
		
		this.setMsgAreaClassName();
	},
	
	listen: {
	   /* messages from message bus*/
		
		'AddUserMessage' : function(sMessage, oData, sSrc) {
		    // create new message node
		    var msgnode = document.createElement('li');
		    if (oData.type != ''){
		        msgnode.className = oData.type; 
		    }
		    if (oData.name != ''){
		        msgnode.id = oData.name; 
		    }
		    msgnode.innerHTML = oData.msg;
		    
		    // add new node as first message in message block (not ads that look like messages)
		    var parent = document.getElementById('msgportlet');
		    if (parent){
    		    var oldnode = document.getElementById(oData.name);
    		    if (oldnode){
    		        parent.removeChild(oldnode);
    		    }
    		    var firstchild = parent.firstChild;
    	        if (firstchild){
                    parent.insertBefore(msgnode, firstchild);
                }
                else{
                    parent.appendChild(msgnode);
                }
                this.setMsgAreaClassName('true');
            }
		},
		
		'RemoveUserMessage' : function(sMessage, oData, sSrc) {
		    var msgnode = document.getElementById(oData.name);
		    if (msgnode){
		        var parent = document.getElementById('msgportlet'); 
		        if (parent){
    		        parent.removeChild(msgnode);
    		        this.setMsgAreaClassName();
    		    }
		    }
		}
	}, // end listen
	
	'setMsgAreaClassName' : function(hasMsg){
        var msgarea = document.getElementById('messagearea');
	    if (msgarea){
	        var msgclass = "empty";
	        
    	    // if a message was added, hasMsg is set to true at call time to avoid checks. 
    	    // by default, hasMsg is false.
    	    if (hasMsg == 'true'){
    	        msgclass = "messagearea";
    	    }
    	    else if (msgarea.getElementsByTagName('li').length > 0){
                msgclass = "messagearea"; 
        	}
        	
            msgarea.className = msgclass;
        }
	} // end setMsgAreaClassName
});
		
		
Portal.Portlet.SensorPageSection = Portal.Portlet.NCBIPageSection.extend ({
	init: function (path, name, notifier){
		this.base (path, name, notifier);
	}
});

(function( $ ){ // pass in $ to self exec anon fn

    // on page ready
    $( function() {
    
        $( 'div.sensor' ).each( function() {

            // get the elements we will need
            var $this = $( this );
            var anchor = $this.find( 'a.portlet_shutter' );
            var portBody = $this.find( 'div.sensor_content' );

            // we need an id on the body, make one if it doesn't exist already
            // then set toggles attr on anchor to point to body
            var id = portBody.attr('id') || $.ui.jig._generateId( 'sensor_content' );
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
Portal.Portlet.SmartSearch = Portal.Portlet.SensorPageSection.extend ({
	init: function (path, name, notifier){
		this.base (path, name, notifier);
	}
});


Portal.Portlet.Entrez_DisplayBar = Portal.Portlet.extend({

	init: function(path, name, notifier) {
		console.info("Created DisplayBar");
		this.base(path, name, notifier);
		
		// for back button compatibility reset values when page loads
		if (this.getInput("Presentation")){
		    this.setValue("Presentation", this.getValue("LastPresentation"));
		    Portal.Portlet.Entrez_DisplayBar.Presentation = this.getValue("LastPresentation");
		}
		if (this.getInput("Format")){
		    this.setValue("Format", this.getValue("LastFormat"));
		    Portal.Portlet.Entrez_DisplayBar.Format = this.getValue("LastFormat");
		}
		if (this.getInput("PageSize")){
		    this.setValue("PageSize", this.getValue("LastPageSize"));
		    Portal.Portlet.Entrez_DisplayBar.PageSize = this.getValue("LastPageSize");
		}
		if (this.getInput("Sort")){
		    this.setValue("Sort", this.getValue("LastSort"));
		    Portal.Portlet.Entrez_DisplayBar.Sort = this.getValue("LastSort");
		}
		this.ResetDisplaySelections();
		this.ResetSendToSelection();
		        
	},
	
	
	send: {
		'Cmd': null, 
		'PageSizeChanged': null,
		'ResetSendTo': null,
		'ResetCurrPage': null
	},
	
	
	
	listen: {
		
		/* browser events */
			
		"sPresentation<click>": function(e, target, name){
		    this.PresentationClick(e, target, name); 
		},
		
		"sPresentation2<click>": function(e, target, name){
		    this.PresentationClick(e, target, name); 
		},
		
		"sPageSize<click>": function(e, target, name){	
		    this.PageSizeClick(e, target, name);
		},
		
		"sPageSize2<click>": function(e, target, name){	
		    this.PageSizeClick(e, target, name);
		},
		
		"sSort<click>": function(e, target, name){
		    this.SortClick(e, target, name);
		},
		
		"sSort2<click>": function(e, target, name){
		    this.SortClick(e, target, name);
		},
		
		"SetDisplay<click>": function(e, target, name){
			this.DisplayChange(e, target, name); 
		},
		
		"SendTo<click>": function(e, target, name){
			var sendto = target.value;
            var idx = target.getAttribute('sid') > 10? "2" : "";
			this.SendToClick(sendto, idx, e, target, name); 
		},
		
		"SendToSubmit<click>": function(e, target, name){
		    var cmd = target.getAttribute('cmd').toLowerCase();
		    var idx = target.getAttribute('sid') > 10? "2" : "";
			this.SendToSubmitted(cmd, idx, e, target, name); 
		},
		
		/* messages from message bus*/
		
		'ResetSendTo' : function(sMessage, oData, sSrc) {
		    this.ResetSendToSelection();
		}
	
	}, // end listen
	
	
	
	/* functions */
	
	'PresentationClick': function(e, target, name){
		Portal.Portlet.Entrez_DisplayBar.Presentation = target.value;
		Portal.Portlet.Entrez_DisplayBar.Format = target.getAttribute('format');
	},
	
	'PageSizeClick': function(e, target, name){ 
		Portal.Portlet.Entrez_DisplayBar.PageSize = target.value;
	},
	
	'SortClick': function(e, target, name){
		Portal.Portlet.Entrez_DisplayBar.Sort = target.value;
	},
	
	'DisplayChange': function(e, target, name){
        this.send.Cmd({'cmd': 'displaychanged'});
        
	    this.SetPresentationChange(e, target, name);
	    this.SetPageSizeChange(e, target, name);
	    this.SetSortChange(e, target, name);
	    
	    Portal.requestSubmit();
	},
	
	'SetPresentationChange': function(e, target, name){
        this.setValue("Presentation", Portal.Portlet.Entrez_DisplayBar.Presentation);
	    this.setValue("Format", Portal.Portlet.Entrez_DisplayBar.Format);
	},
	
	'SetPageSizeChange': function(e, target, name){
	    this.setValue("PageSize", Portal.Portlet.Entrez_DisplayBar.PageSize);
		if (this.getValue("PageSize") != this.getValue("LastPageSize")){
    		//send PageSizeChanged
    		this.send.PageSizeChanged({
    			'size': this.getValue("PageSize"),
                'oldsize': this.getValue("LastPageSize")
    		});	
		}
	},
		
	'SetSortChange': function(e, target, name){
	    if (this.getInput("Sort")){
	        this.setValue("Sort", Portal.Portlet.Entrez_DisplayBar.Sort);
            if (this.getValue("Sort") != this.getValue("LastSort")){
                // ask to reset CurrPage 
    		    this.send.ResetCurrPage();
    		}
        }    	
	},
		
	'SendToClick': function(sendto, idx, e, target, name) {
		if(sendto.toLowerCase() == 'file'){
			this.SendToFile(sendto, idx);
		}
		else if(sendto.toLowerCase() == 'addtocollections'){
			this.SendToCollections(sendto, idx);
		}
		else if(sendto.toLowerCase() == 'addtoclipboard'){
		    this.SendToClipboard(sendto, idx);
		}
	},
	
	'SendToSubmitted': function(cmd, idx, e, target, name){
	    if (cmd == 'file'){
	         this.SendToFileSubmitted(cmd, idx, target);
	    }
	    this.send.Cmd({'cmd': cmd});
	    Portal.requestSubmit();
	},
	
	'ResetSendToSelection': function(){
	    var SendToInputs = this.getInputs("SendTo");
	    for (var j = 0; j < SendToInputs.length; j++){
		    if (SendToInputs[j].checked){
		        SendToInputs[j].checked = false;
			}
		}
	},
	
	'SendToFile': function(name, idx){
	    // generate content
	    var count = this.getItemCount();
		var content = 'Download ' + count + ' items.';
		this.addSendToHintContent(name, idx, content);
	},
	
	'SendToCollections': function(name, idx){
	    // generate content
        var count = this.getItemCount();
        var content= 'Add ';
        if (count > Portal.Portlet.Entrez_DisplayBar.CollectionsUpperLimit){
            content += "the first " + Portal.Portlet.Entrez_DisplayBar.CollectionsUpperLimitText;
        }
        else{
            content += count;
        }
        content += " items.";
        this.addSendToHintContent(name, idx, content);	
	},
	
	'SendToClipboard': function(name, idx){
	    // generate content
	    var count = this.getItemCount();
        var content= 'Add ';
        if (count > Portal.Portlet.Entrez_DisplayBar.ClipboardLimit){
            content += "the first " + Portal.Portlet.Entrez_DisplayBar.ClipboardLimit;
        }
        else{
            content += count;
        }
        content += " items.";
        this.addSendToHintContent(name, idx, content);
	},
	
	'getItemCount': function(){
	    // ask for selected items count from DbConnector
	    var selectedItemCount = getEntrezSelectedItemCount();
	    if (selectedItemCount > 0){
	        return selectedItemCount;
	    }
	    else{
	        // ask for result count from Entrez_ResultsController
	        return getEntrezResultCount();
	    }
	},
	
	'addSendToHintContent': function(name, idx, content){
	    var hintNode = document.getElementById("submenu_" + name + "_hint" + idx);
	    if (hintNode){
	        hintNode.innerHTML = content;
	        hintNode.className = 'hint';
	    }
	},
	
	'AddSendToSubmitEvent': function(){
	    // add event for SendTo submit button click. 
	    // This call is needed if the position of the submit button node has changed in relation to its parent node. 
        this.addEvent("SendToSubmit", "click", function(e, target, name) {
            var cmd = target.getAttribute('cmd');
            this.SendToSubmitted(cmd, e, target, name); 
        }, false);
    },
    
    'SendToFileSubmitted': function(cmd, idx, target){
         if (this.getInput("FFormat" + idx)){
             this.setValue("FileFormat", this.getValue("FFormat" + idx));
         }
         if (this.getInput("FSort" + idx)){
             this.setValue("FileSort", this.getValue("FSort" + idx));
         }
    },
    
    'ResetDisplaySelections': function(){
        if (this.getInput("Presentation")){
            var selection = this.getValue("Presentation").toLowerCase() + this.getValue("Format").toLowerCase();
            if (document.getElementById(selection)){
                document.getElementById(selection).checked = true;
            }
            // bottom display bar
            if (document.getElementById(selection + "2")){
                document.getElementById(selection + "2").checked = true;
            }
            
        }
        if (this.getInput("PageSize")){
            var selection = 'ps' + this.getValue("PageSize");
            if (document.getElementById(selection)){
                document.getElementById(selection).checked = true;
            }
            // bottom display bar
            if (document.getElementById(selection + "2")){
                document.getElementById(selection + "2").checked = true;
            }
        }
        if (this.getInput("Sort")){
            var selection = this.getValue("Sort") || 'none'; 
            if (document.getElementById(selection)){
                document.getElementById(selection).checked = true;
            }
            // bottom display bar
            if (document.getElementById(selection + "2")){
                document.getElementById(selection + "2").checked = true;
            }
        }
    }
	
},
{
    Presentation: '',
    Format: '',
    PageSize: '',
    Sort: '',
    CollectionsUpperLimit: 1000,
	CollectionsUpperLimitText: '1,000',
	ClipboardLimit: 500
});


Portal.Portlet.Pubmed_DisplayBar = Portal.Portlet.Entrez_DisplayBar.extend({

	init: function(path, name, notifier) {
		this.base(path, name, notifier);
	},
	
	send: {
		'Cmd': null, 
		'PageSizeChanged': null,
		'ResetSendTo': null,
		'ResetCurrPage': null,
		'SendMail': null
	},
	
	/* functions */
	'SendToClick': function(sendto, idx,  e, target, name) {
	    if (sendto.toLowerCase() == 'order'){
	    }
	    else if (sendto.toLowerCase() == 'mail'){
	        this.SendToMail(sendto, idx);
	    }
	    else if (sendto.toLowerCase() == 'addtobibliography'){
	        this.SendToBib(sendto, idx);
	    }
	    else
	        this.base(sendto, idx, e, target, name);
	},
	
	'SendToMail': function(sendto, idx){
	    // hide any previous alert messages 
	    var alertnode = document.getElementById("email_alert" + idx);
	    alertnode.className = 'hidden';
	    
	    // ask for selected items count from DbConnector
	    var selectedItemCount = getEntrezSelectedItemCount() || 0;
	    var descNode = document.getElementById("email_desc" + idx);
	    
        // if ids are selected, save old description, and create new description
        if (selectedItemCount > 0){
            if (Portal.Portlet.Pubmed_DisplayBar.Description == '')
	            Portal.Portlet.Pubmed_DisplayBar.Description = descNode.innerHTML;
	        descNode.innerHTML = selectedItemCount + " selected item" + (selectedItemCount > 1? "s" : "");
	    }
	    // if ids are not selected, and an old description is present, restore that
	    else{
	        if (Portal.Portlet.Pubmed_DisplayBar.Description != '')
	            descNode.innerHTML = Portal.Portlet.Pubmed_DisplayBar.Description;
        }
        
        // get total number of items about to be sent
        var count = this.getItemCount();
        
        // don't show email count and start options if less than 5 items are in search result, or user has selected some items
        if (document.getElementById("email_count_option" + idx)){
            if (count <= 5 || selectedItemCount > 1){
                document.getElementById("email_count_option" +  idx).style.display = "none";
            }
            else {
                document.getElementById("email_count_option" + idx).style.display = "list-item";
            }
        }
        if (document.getElementById("email_start_option" + idx)){
            if (count <= 5 || selectedItemCount > 1){
                document.getElementById("email_start_option" +  idx).style.display = "none";
            }
            else {
                document.getElementById("email_start_option" + idx).style.display = "list-item";
            }
        }
        
         // don't show sort option if 1 item is selected
        if (document.getElementById("email_sort_option" + idx)){
            if (count == 1){
                document.getElementById("email_sort_option" + idx).style.display = "none";
            }
            else {
                document.getElementById("email_sort_option" + idx).style.display = "list-item";
            }
        }
	},
	
	'SendToBib': function(name, idx){
	    // generate content
        var count = this.getItemCount();
        var content= 'Add ';
        if (count > Portal.Portlet.Pubmed_DisplayBar.BibUpperLimit){
            content += "the first " + Portal.Portlet.Pubmed_DisplayBar.BibUpperLimit;
        }
        else{
            content += count;
        }
        content += " items.";
        this.addSendToHintContent(name, idx, content);	
	},
	
	'SendToSubmitted': function(cmd, idx, e, target, name){
	    if (cmd.toLowerCase() == 'mail'){
	         this.SendToEmailSubmitted(cmd, idx, target);
	    }
	    else{
	        this.base(cmd, idx, e, target, name);
	    }
	},
	
	'SendToEmailSubmitted': function(cmd, idx, target){
	    
	    var alertnode = document.getElementById("email_alert" + idx);
	    alertnode.className = 'hidden';
	    
	    var email = document.getElementById("email_address" + idx).value.replace(/^\s*|\s*$/g,'');
	    if (email == ''){
	        alertnode.innerHTML = 'Please provide an email address.';
	        alertnode.className = 'email_alert';
	    }
	    else {
    	    var emailRegexp = /^[A-Za-z0-9._\'%-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/;
    		if (emailRegexp.test(email)){
        	    this.SendMailInfo(cmd, idx, target, email);		
    		}
    		else {
    			alertnode.innerHTML = 'The email address is invalid!';
	            alertnode.className = 'email_alert';
    		}
        }    		
	    
	},
	
	'SendMailInfo': function(cmd, idx, target, email){
	    // collect options, description and extra text
	    var emailFormat = document.getElementById("email_format" + idx);
	    var report = emailFormat.value;
	    var format =  emailFormat.options[emailFormat.selectedIndex].getAttribute('format');
	    var sort = document.getElementById("email_sort" + idx)? document.getElementById("email_sort").value : "";
	    var count = document.getElementById("email_count" + idx)? document.getElementById("email_count" + idx).value : "5";
	    var start = document.getElementById("email_start" + idx)? document.getElementById("email_start" + idx).value : "1";
	    var text = document.getElementById("email_add_text" + idx).value;
	    var querykey = target.getAttribute('qk');
	    var querydesc = document.getElementById("email_desc" + idx).innerHTML;
	    var suppData = jQuery('#chkSupplementalData' + idx).attr('checked');
	    if (suppData && report == 'abstract' && format != 'text' ) report = 'AbstractWithSupp';
	    
	    // send message to email portlet with data
	    this.send.SendMail({
	        'report' : report,
	        'format' : format,
	        'count' : count,
	        'start' : start,
	        'sort' : sort,
	        'email' : email,
	        'text' : text,
	        'querykey': querykey,
	        'querydesc': querydesc /*,
	        'suppData' : suppData */
	    });
	    
	    this.send.Cmd({'cmd': cmd});
	    Portal.requestSubmit();
    }
	
},
{
    BibUpperLimit: 500,
    Description: ''
});

jQuery(document).ready(function(){
    jQuery('#email_format').bind('change',emailFormatChanged);
    jQuery('#email_format2').bind('change',emailFormatChanged);
    //emailFormatChanged(); //initial state
    jQuery('#email_format').trigger('change');
    jQuery('#email_format2').trigger('change');
});

function emailFormatChanged(){ 
   var sel = jQuery(this); 
   var chkSpan = sel.next(); 
   var visibility =  ( sel.val() == 'abstract' && 
                    sel.find("option:selected").attr('format') != 'text' ) ? "visible" : "hidden";
   chkSpan.css('visibility', visibility); 
}






Portal.Portlet.Entrez_SearchBar = Portal.Portlet.extend ({
  
	init: function (path, name, notifier) 
	{
		console.info ("Created SearchBar"); 
		this.base (path, name, notifier);
		
		var AutoCompSelectFnc = function(){ 
            Portal.$send('AutoCompSelect'); 
        } 
        jQuery("#search_term").bind("ncbiautocompleteenter", AutoCompSelectFnc ).bind("ncbiautocompleteoptionclick", AutoCompSelectFnc ); 
        	
	},

	send: {
		"Cmd": null,
		"Term": null,
		"AutoCompSelect": null
	},
	
	listen: {
		// messages
		
		'AppendTerm': function(sMessage, oData, sSrc) {
		    this.ProcessAppendTerm(sMessage, oData, sSrc);
		},
		
		// to allow any other portlet to clear term if needed  
		'ClearSearchBarTerm': function(sMessage, oData, sSrc) {
			this.setValue("Term", '');
		},
		
		// request current search bar term to be broadcast  
		'SendSearchBarTerm': function(sMessage, oData, sSrc) {
			this.send.Term({'term' : this.getValue("Term")});
		},
		
		'AutoCompleteControl': function(sMessage, oData, sSrc) {
		    this.ChangeAutoCompleteState(sMessage, oData, sSrc);
        },
        
        'AutoCompSelect': function(sMessage, oData, sSrc) {
		    this.AutoCompleteOptionSelected();
        },
        
		// Browser events
		
		"SearchResourceList<change>": function(e, target, name) {
		    this.ResourceSelected(e, target, name);
		},
		
		"Term<keypress>": function(e, target, name) {
			var event = e || utils.fixEvent (window.event);
			if ((event.keyCode || event.which) == 13) 
			{
			    // Emulate Search button click
			    this.ProcessTermKeyPress(event, e, target, name);
			}
		},
		
		// Cmd is set to Go, so ResultsView of other database can choose component based 
		// on value of Cmd. The existing search term is also passed down.
		"Search<click>": function(e, target, name) {
		    this.ProcessSearchClick (e, target, name);
		},
		
		"Preview<click>": function(e, target, name) {
            this.ProcessPreviewClick(e, target, name);
		},
		
		// On Advanced Search click, append term
		"Advanced<click>": function (e, target, name) {
		    this.ProcessAdvancedClick(e, target, name);
		},
		
		// On Advanced Search click, append term
		"LimitsLink<click>": function (e, target, name) {
		    this.ProcessLimitsClick(e, target, name);
		},
		
		"CreateRssFeed<click>": function (e, target, name) {
		    this.ProcessCreateRssFeed(e, target, name);
		}
		
	}, //end listen
	
	"ProcessAppendTerm" : function(sMessage, oData, sSrc){
	    var newTerm = this.getValue("Term");
	    if (newTerm != '' && oData.op != ''){
	        newTerm = '(' + newTerm + ') ' + oData.op + ' ';
	    }
	    newTerm += oData.term;
	    this.setValue("Term", newTerm); 
	    
	    var oTerm = this.getInput("Term");
        if (oTerm) {
           oTerm.focus();
        }
	},
	
	"ResourceSelected" : function(e, target, name){
	    /*
	    if (this.getValue("SearchResourceList") == 'customize'){
            window.location = "/sites/myncbi/searchbar/" + db;
        }
        else
        */
        
	    // turn autocomplete off or on if database is changed in selector.
	    if (this.getValue("Term:suggest") == 'true'){
		    // change to if the current database has a dictionary 
		    this.EnableDisableAutocomplete(target.options[target.selectedIndex].getAttribute('resource'));
        }
       
	},
	
	"ProcessTermKeyPress" : function(event, e, target, name){
	    event.returnValue = false;
		if (event.stopPropagation != undefined)
              event.stopPropagation ();   
		if (event.preventDefault != undefined)
              event.preventDefault ();
              
		this.ProcessSearchClick (e, target, name);
		return false;
	},
	
	"ProcessSearchClick" : function(e, target, name){
	    if (!Portal.Portlet.Entrez_SearchBar.Searched){
	        // run code only once, the first time user hits enter or clicks go. Ignore repeated clicks.
	        Portal.Portlet.Entrez_SearchBar.Searched = true;
	        
	        this.doSearchPing();
	        
    	    var resource = this.getValue("SearchResourceList");
    	    var term = this.getValue("Term");
    	    var db = this.getValue("CurrDb");
    	    
    	    if (resource == db) {
    	        if (document.getElementById('search_term').getAttribute('pg') == 'limits' 
    	            || document.getElementById('search_term').getAttribute('pg') == 'advanced'){
        	        this.send.Cmd({
        				'cmd' : 'Go'
        			});
        		   	this.send.Term({
        				'term' : this.getValue("Term")
        			});
        			Portal.requestSubmit();
                }
                else{
                     window.location = '/' + db + (term != '' ? "?term=" + encodeURIComponent(term) : "");
                }
    	    } 
    	    else {
    	        window.location = resource 
    	            + (term != '' ? (resource.match(/\?/) ? "&term=" : "?term=") + encodeURIComponent(term) : "");
    	    }
        }
	},
	
	"ProcessPreviewClick" : function(e, target, name){
        this.send.Cmd({
			'cmd' : 'Preview'
		});
	   	this.send.Term({
			'term' : this.getValue("Term")
		});
		Portal.requestSubmit(); 
	},
	
	"ProcessAdvancedClick" : function(e, target, name){
	   	window.location = target.href;
	},
	
	"ProcessLimitsClick" : function(e, target, name){
	   	window.location = target.href
	   	    + (this.getValue("Term") != '' ? "?term=" + encodeURIComponent(this.getValue("Term")) : "");
	},
	
	"ProcessCreateRssFeed" : function(e, target, name){
	   	// do xml http to create the feed using portal code, then update the screen with link to feed
	   	var site = document.forms[0]['p$st'].value;
	   	var portletPath = this.getPortletPath(); 
	   	var args = {
            "QueryKey": target.getAttribute('qk'),
            "Db": this.getValue("CurrDb"),
            "RssFeedName": this.getValue("FeedName"),
            "RssFeedLimit": this.getValue("FeedLimit") || this.getInput("FeedLimit").options[this.getInput("FeedLimit").selectedIndex].text,
            "HID": target.getAttribute('hid')
        };
        try{
            var resp = xmlHttpCall(site, portletPath, "CreateRssFeed", args, this.receiveRss, {}, this);
        }
        catch (err){
            alert ('Could not create RSS feed.');
        }
	},
	
	/* this function created to be able to create a hack to overcome shortcoming in current portal framework
	Because of using portlet inheritence, the action defined in the base portlet cannot be found.
	The hack is to allow derived portlets to hard-code the path to themselves by overriding this function.
	This hack can be removed after new implementation of portal is in place which will view objects as a flat model.
	*/ 
	"getPortletPath" : function(){
	    return this.realname;
	},
	
	receiveRss: function(responseObject, userArgs) {
	    try{
    	    //Handle timeouts 
    	    if(responseObject.status == 408){
    	        //display an error indicating a server timeout
    	        alert('RSS feed creation timed out.');
    	    }
    	    
    	    // deserialize the string with the JSON object 
    	    var response = '(' + responseObject.responseText + ')'; 
    	    var JSONobject = eval(response);
    	    // display link to feed
    	    document.getElementById('rss_menu').innerHTML = JSONobject.Output;
        }
        catch(e){
            alert('RSS unavailable.');
        }
    },
    
    'ChangeAutoCompleteState': function(sMessage, oData, sSrc){
        this.setValue("Term:suggest", 'false');
        var site = document.forms[0]['p$st'].value;
        var portletPath = this.getPortletPath(); 
        var resp = xmlHttpCall(site, portletPath, "AutoCompleteControl", {"ShowAutoComplete": 'false'}, this.receiveAutoComp, {}, this);
    },        
        
    'receiveAutoComp': function(responseObject, userArgs) {
    },
    
    'AutoCompleteOptionSelected': function(){
        this.ProcessSearchClick ();
	},
	
	'EnableDisableAutocomplete': function(resource){
	    var site = document.forms[0]['p$st'].value;
	    var portletPath = this.getPortletPath(); 
        var resp2 = xmlHttpCall(site, portletPath, "SetAutoCompleteDictionary", {"Db": resource}, this.receiveDictionary, {}, this);
	},
	
	'receiveDictionary': function(responseObject, userArgs){ 
        try {
            // deserialize the string with the JSON object
            var response = '(' + responseObject.responseText + ')';
            var JSONobject = eval(response);
            
            var dict = JSONobject.Dictionary || "";
            
            // turn autocomplete off or on if database is changed in selector.
            if(dict != ''){
               jQuery("#search_term").ncbiautocomplete("option","isEnabled",true).ncbiautocomplete("option","dictionary",dict);
            }
            else{
               jQuery("#search_term").ncbiautocomplete("turnOff",true);    
            }
        }
        catch (e){
        
        }
    },
    'doSearchPing':function(){
        var cVals = ncbi.sg.getInstance()._cachedVals;

        var searchDetails = {}
        searchDetails["jsEvent"] = "search";
        
        var app = cVals["ncbi_app"];
        var db = cVals["ncbi_db"];
        var pd = cVals["ncbi_pdid"];
        var pc = cVals["ncbi_pcid"];
        
        var sel = document.getElementById("database");
        var searchDB = sel.options[sel.selectedIndex].value;
        var searchText = document.getElementById("search_term").value;
        
        if( app ){ searchDetails["ncbi_app"] = app.value; }
        if( db ){ searchDetails["ncbi_db"] = db.value; }
        if( pd ){ searchDetails["ncbi_pdid"] = pd.value; }
        if( pc ){ searchDetails["ncbi_pcid"] = pc.value; }
        if( searchDB ){ searchDetails["searchdb"] = searchDB;}
        if( searchText ){ searchDetails["searchtext"] = searchText;}
        
        ncbi.sg.ping( searchDetails );
    }
	
},

{
    Searched: false
});

function EntrezSearchBarAutoComplCtrl(){
    Portal.$send('AutoCompleteControl');
}
Portal.Portlet.Pubmed_SearchBar = Portal.Portlet.Entrez_SearchBar.extend ({
  
	init: function (path, name, notifier) {
		this.base (path, name, notifier);
	},
	
	/* ######### this is a hack. See detailed comment on same function in base */
	"getPortletPath" : function(){
	    return (this.realname + ".Entrez_SearchBar");
	}
});

