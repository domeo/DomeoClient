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

Portal.Portlet.Gene_StaticParts = Portal.Portlet.extend({
	init: function(path, name, notifier) {
		this.base(path, name, notifier);
		
		var allSectionTogglers = jQuery('a.section-toggler');
	
		// fix grid's footer when close/open each toggler
		// those jig-grids need refreshing
		allSectionTogglers.bind('ncbitoggleropen', function() {
			refreshJigGrids(this);
			
			//refresh SQ if the the section is genomic product
			sectionName = jQuery(this).attr('id');
			if (sectionName === 'm-genomic-regions-transcripts-products') {
				refreshSQApp(jQuery('#accessionList :selected').val());
			}
		});
		
		allSectionTogglers.bind('ncbitogglerclose', function() {
			refreshJigGrids(this);
		});
		
		function refreshJigGrids(oThis){
			// grab immediate parents
			var rprtSection = jQuery(oThis).parents('div.rprt-section');
			
			// refresh jig grids
			rprtSection.find ('table.jig-ncbigrid').trigger("ncbigridupdated");
		}
		
		function refreshSQApp (accession) {
		    try {
    			var svApp = SeqView.App.findAppByIndex(0);	
    			
    			if (svApp) {
    			    accession = accession.replace(/\./g,'_');
    				var relString = jQuery('input#relString_' + accession).val().replace(/&amp;/g,'&');
    				
    				// update
    				if (relString !== '') {
    					svApp.reload(relString);
    				}
    			}
    		} catch (e) {
    		    if (console && console.log) {
    		        console.log('Error: ' + e);
    		    }
    		}
		}
	
	} // end init
	
});


try {
    // For NLM interop
    document.domain = "nlm.nih.gov";
} catch (e) {}


function call_opener(id,symbols,descriptions,species, research_info)
{ 
   opener.add_link(id, symbols, descriptions.substr(0,2000),species, research_info.substr(0,2000));
   window.close(); 
}
   
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
//
//		 Gene_Subscription: Describe portlet here
//
Portal.Portlet.Gene_Subscription = Portal.Portlet.BriefLinkPageSection.extend({
	
	init: function (path, name, notifier) {
		this .base(path, name, notifier);
	},
	
	"getPortletPath" : function () {
		return (this .realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection");
	}
});
//
//		 Gene_Feedback: Describe portlet here
//
Portal.Portlet.Gene_Feedback = Portal.Portlet.BriefLinkPageSection.extend({
	
	init: function (path, name, notifier) {
		this .base(path, name, notifier);
	},
	
	"getPortletPath" : function () {
		return (this .realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection");
	}
});
//
//		 Gene_RelatedSites: Describe portlet here
//
Portal.Portlet.Gene_RelatedSites = Portal.Portlet.BriefLinkPageSection.extend({
	
	init: function (path, name, notifier) {
		this .base(path, name, notifier);
	},
	
	"getPortletPath" : function () {
		return (this .realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection");
	}
});
//
//		 Gene_GeneralInfo: Describe portlet here
//
Portal.Portlet.Gene_GeneralInfo  = Portal.Portlet.BriefLinkPageSection.extend({

	init: function(path, name, notifier) {
		this.base(path, name, notifier);
	},
	
	"getPortletPath" : function(){
	    return (this.realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection");
	}
	
});
//
//		 Gene_LinksTwo: Describe portlet here
//
Portal.Portlet.Gene_LinksTwo = Portal.Portlet.BriefLinkPageSection.extend({
	
	init: function (path, name, notifier) {
		this .base(path, name, notifier);
	},
	
	"getPortletPath" : function () {
		return (this .realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection");
	}
});
//
//		 Gene_LinksOne: Describe portlet here
//
Portal.Portlet.Gene_LinksOne = Portal.Portlet.BriefLinkPageSection.extend({

	init: function(path, name, notifier) {
		this.base(path, name, notifier);
	},
	
	"getPortletPath" : function(){
	    return (this.realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection");
	}
	
});
//
//		 Gene_TableOfContents: Describe portlet here
//
Portal.Portlet.Gene_TableOfContents = Portal.Portlet.BriefLinkPageSection.extend({

	init: function(path, name, notifier) {
		this.base(path, name, notifier);
	},
	
	"getPortletPath" : function(){
	    return (this.realname + ".BriefLinkPageSection.LinkListPageSection.NCBIPageSection");
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

Portal.Portlet.Gene_ResultsController = Portal.Portlet.Entrez_ResultsController.extend({
	init: function(path, name, notifier) {
		this.base(path, name, notifier);
	}
});
   

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
		
		
//
//		 Gene_Refseq: Will update to use Popper instead when Jig 1.3 is deployed in entrez
//
Portal.Portlet.Gene_Refseq = Portal.Portlet.extend({
	
	// Init function.
	// init() called when page loads. Required for all portlet JS objects.
	//
	init: function (path, name, notifier) {
		var oThis = this; // Use <oThis> instead of <this> for registering callbacks
		this .base(path, name, notifier);
		jQuery('#expl-refseg').bind ('click', function() {
			jQuery('#refseq-expl').toggle();
			return false;
		});
		
		jQuery('#expl-genomes').bind ('click', function() {
			jQuery('#expl-genomes-desc').toggle();
			return false;
		});	
		
		jQuery('#expl-suppressed').bind ('click', function() {
			jQuery('#expl-suppressed-desc').toggle();
			return false;
		});
	}
	
});
Portal.Portlet.Gene_Ontology = Portal.Portlet.extend({
	init: function(path, name, notifier) {
		this.base(path, name, notifier);				


		var $aPopper = jQuery('a.ecode_i');
		
    	$aPopper.each(function() {
    	
    	    var $oThis = jQuery(this);
        	var $divPopper = $oThis.next('div.ecode');
        	var popId = $divPopper.attr('id');
        		
        	$aPopper.ncbipopper({
        		groupName: 'ecode',
        		isTriggerElementCloseClick: false,
        		destSelector: "#" + popId,
        		hasArrow: true,
        		openAnimation: 'none',
        		closeAnimation: 'none',
        		delayTimeout: 130,
        		sourceSelector: "#" + popId,
        		adjustFit: 'none',
        		arrowDirection: 'left'
        	});
		});
		
	} // end init
});


Portal.Portlet.Gene_InfoPathway = Portal.Portlet.extend({

	init: function (path, name, notifier) {
		this.base(path, name, notifier);
		
		/* initialize poppers via javascript, so the page won't look messy, with the popper config attribute */
		jQuery(function ($) {
			var $liPoppers = $('ul.infopathway li');
			
			$liPoppers.each(function () {
				var $othis = $(this);
				var $aPopper = $othis.find('a.infopath-popper');
				var $divPopper = $othis.find('div.infopath-popbody');
				var popId = $divPopper.attr('id');
								
				$aPopper.ncbipopper({
					groupName: 'GeneInfoPath',
					isTriggerElementCloseClick: false,
					destSelector: "#" + popId,
					hasArrow: true,
					openAnimation: 'none',
					closeAnimation: 'none',
					delayTimeout: 130,
					sourceSelector: "#" + popId,
					adjustFit: 'none',
					arrowDirection: 'left'
				});
				
				
			});
		});
		
		
		
		
	}
});
Portal.Portlet.Gene_RifP = Portal.Portlet.extend({
	init: function (path, name, notifier) {
		this.base(path, name, notifier);
		
		jQuery(function ($) {
			var $liPoppers = $('ol.generef-link li.gene-rif-popper');
			
			$liPoppers.each(function () {
				var $othis = $(this);
				var $aPopper = $othis.find('a.gene-rif-popper');
				var $divPopper = $othis.find('div.gene-rif-popper');
				
				var popId = $divPopper.attr('id') ;
				
				$aPopper.ncbipopper({
					groupName: 'geneRIF',
					isTriggerElementCloseClick: false,
					destSelector: "#" + popId,
					hasArrow: true,
					adjustFit: 'autoAdjust',
					openAnimation: 'none',
					closeAnimation: 'none',
					delayTimeout: 130,
					sourceSelector: "#" + popId,
					triggerPosition: 'middle right'
				});
				
			});
		});
	}
});

/*
Text Link/Image Map Tooltip Script- 
¿ Dynamic Drive (www.dynamicdrive.com)
For full source code, installation instructions,
100's more DHTML scripts, and Terms Of
Use, visit dynamicdrive.com
*/

if (!document.layers&&!document.all)
event="test"

var saved_width;


function showtip(current,e,text)
{
    text=unescape(text);
  if (document.all) {
    text=text.replace(new RegExp("<BR>","gi")," \n");
    text=text.replace(new RegExp("<[^>]+>","g"),'');
    current.title=text
  } else if (document.layers) {
    document.tooltip.document.write('<layer bgColor="yellow" style="border:1px solid black;font-size:12px;">'+text+'</layer>')
    document.tooltip.document.close()
    if (e.pageX+5+document.tooltip.width>window.pageXOffset + window.innerWidth) 
      document.tooltip.pageX = window.pageXOffset + window.innerWidth-document.tooltip.clip.width
    else
      document.tooltip.pageX=e.pageX+5
    if (e.pageY+5+document.tooltip.clip.height>window.pageYOffset + window.innerHeight) 
      document.tooltip.pageY = e.pageY-document.tooltip.clip.height
    else
      document.tooltip.pageY=e.pageY+5
    document.tooltip.visibility="show"
  } else if (document.getElementById) {
    text=text.replace(new RegExp("<BR>","g"),"<br>");
    thetitle=text.split('<br>')
    if (thetitle.length>1) {
      tipNode=document.getElementById('tooltip');
      dynamiccontentNS6(tipNode,text);
      tipNode.style.backgroundColor='yellow';

      var width,height;
      if (window.document.body && typeof(window.document.body.clientWidth) == 'number') {
        // Gecko 1.0 (Netscape 7) and Internet Explorer 5+
        width = window.document.body.clientWidth;  
        height = window.document.body.clientHeight;  
      } else if (typeof(window.innerWidth) == 'number') {
        // Navigator 4.x, Netscape 6.x, CompuServe 7 and Opera
        width = window.innerWidth;
        height = window.innerHeight;
      }

     saved_width = tipNode.style.width;
     tipNode.style.width = tipNode.offsetWidth;

     if (e.pageX+5+tipNode.offsetWidth>window.pageXOffset + width )
        tipNode.style.left = window.pageXOffset + width-tipNode.offsetWidth
      else
        tipNode.style.left=e.pageX+5
      if (e.pageY+5+tipNode.offsetHeight>window.pageYOffset + height )
        tipNode.style.top = e.pageY-tipNode.offsetHeight
      else
        tipNode.style.top=e.pageY+5

      tipNode.style.visibility="visible"

    } else {
      text=text.replace(new RegExp("<[^>]+>","g"),'');
      current.title=text
    }
  }
}

function hidetip(){
  if (document.all) {
  }
  else if (document.layers) {
    document.tooltip.visibility="hidden"
  }
  else if (document.getElementById) {
    tipNode=document.getElementById('tooltip');
    if (tipNode){  
      tipNode.style.visibility="hidden"
      tipNode.style.left = 0
      tipNode.style.top = 0
      tipNode.style.width = saved_width
    }
  }
}


Portal.Portlet.Gene_GenomicProductsP = Portal.Portlet.extend({
	
	init: function (path, name, notifier) {
		this.base(path, name, notifier);
		this.showAccessionList();
		this.resetAccession();
	},
	
	'resetAccession': function() {
		jQuery('#accessionList option').eq(0).attr('selected', 'selected');
	},
	
	'showAccessionList': function () {
		var selAccession = jQuery('#accessionList :selected').val();
		var $divAccession = jQuery('#' + selAccession + '.divAccession');
	   $divAccession.show();
	},
	
	'getSelAccessionString' : function() {
		return jQuery('#accessionList :selected').val();
	},
	
	'refreshSQApp' : function(accession) {
		var svApp = SeqView.App.findAppByIndex(0);
		
		if (svApp) {
		
			//add \\ before the accession version
			// or jquery cannot fine the input
			
            var accession = accession.replace(/\./g,'_'),
			    relStringId = accession.replace(/\./g, '\\.'),
			    relString = jQuery('input#relString_' + relStringId).val().replace(/&amp;/g,'&'),
			    nucleotideLink = jQuery('input#nucleotideLink_' + relStringId).val();
			    
			jQuery('div.seq-viewer p.note-link').html(nucleotideLink);
			
			if (relString !== '') {
				console.debug("\n" + "this is the relstring that is passed to sequence viewer"+ "\n");
				console.info(relString);
				svApp.reload(relString);
			}			
		}
	},
	
	listen: {
		"accessionList<change>": function (event, target) {
			this.refreshSQApp(this.getSelAccessionString());
		}
	}
	// end listen
	
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


Portal.Portlet.Gene_DisplayBar = Portal.Portlet.Entrez_DisplayBar.extend({
	
	init: function (path, name, notifier) {
		this .base(path, name, notifier);
	}
});
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
Portal.Portlet.Gene_SearchBar = Portal.Portlet.Entrez_SearchBar.extend ({
	init: function (path, name, notifier) {
		this.base (path, name, notifier);
	}
});

