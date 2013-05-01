// version 2.7.4 (04/05/2007) 
// XHTML PoUpMenu position Fix
// New FrameTarget param added
// All-in-One File PopUpMenu2
/* svn: last revision $Rev: 163620 $ by $Author: pascaree $ at $Date: 2009-06-17 09:22:53 -0400 (Wed, 17 Jun 2009) $ */

//addEvent in case utils.js not available
PopUpMenu = {};
PopUpMenu.addEvent = function(el, what, f) {
  function ff(e) {
     e.preventDefault = e.preventDefault || function() { window.event.returnValue = false; }
     e.stopPropagation = e.stopPropagation || function() { window.event.cancelBubble = true; }
     return f(e);
 }
 if (!el) { return; }
 if (window.addEventListener) {
    el.addEventListener(what, ff, false);
 } else {
    el.attachEvent("on"+what, ff);
 }
};

var PopUpMenu2_title4layer
var PopUpMenu2_inheight         // menu Height , will be calculated dep. on links count for older browsers
var PopUpMenu2_inwidth          // menu width , will be calculated dep. on longer link lenth for older browsers
var PopUpMenu2_offsetx   = 2    // show menu offset X
var PopUpMenu2_offsety   = 2    // show menu offset Y

// DEFAULT VARIABLES 

var Table_Cell_MouseOut_color_self_style ="";


var Table_Cell_MouseOver_color= { 
        def:'#F2F5F7', 
        entrez_table:'#F2F5F7'
        };  
var Table_Cell_MouseOut_color= { 
        def:'#E1E6EB', 
        entrez_top_table:'#E1E6EB'
        };  

var PopUpMenu2_name_entrez_table='"entrez_table"';
var PopUpMenu2_name_entrez_top_table='"entrez_top_table"';
var PopUpMenu2_pageX;
var PopUpMenu2_pageY;
var PopUpMenu2_milliseconds=0;
var PopUpMenu2_doNOThide = false;

var PopUpMenu2_default_config = [ 
["ColorTheme" , "blue"],
["TitleText" , "Links"],
["ShowTitle" , "yes"],
["Help" , "none"],
["ShowCloseIcon" , "no"],
["AlignCenter" , "no"],
["AlignLR" , "right"],
["AlignTB" , "bottom"],
["FreeText" , "no"],
["TitleColor","white"],
["TitleSize","11px"],
["TitleBackgroundImage","http://www.ncbi.nlm.nih.gov/coreweb/images/popupmenu/top_bg2.gif"],
["ItemColor","Navy"],
["ItemSize","11px"],
["ItemFont","Verdana, arial, geneva, helvetica"],
["ItemBulletImage","http://www.ncbi.nlm.nih.gov/coreweb/images/popupmenu/marrow.gif"],
["SeparatorColor","#006A50"],
["BorderColor","#32787A"],
["BackgroundColor","#E1E6EB"],
["HideTime" ,300],
["ToolTip" , "no"],
["FrameTarget" , ""]
];


// NEW STYLE DEFAULT GLOBAL VARIABLES
var PopUpMenu2_ColorTheme_index = 0;
var PopUpMenu2_TitleText_index  = 1;
var PopUpMenu2_ShowTitle_index = 2;
var PopUpMenu2_Help_index = 3;
var PopUpMenu2_ShowCloseIcon_index = 4;
var PopUpMenu2_AlignCenter_index = 5;
var PopUpMenu2_AlignLR_index = 6;
var PopUpMenu2_AlignTB_index =7;
var PopUpMenu2_FreeText_index = 8;
var PopUpMenu2_TitleColor_index = 9;
var PopUpMenu2_TitleSize_index = 10;
var PopUpMenu2_TitleBackgroundImage_index = 11;
var PopUpMenu2_ItemColor_index =12;
var PopUpMenu2_ItemSize_index = 13;
var PopUpMenu2_ItemFont_index = 14;
var PopUpMenu2_ItemBulletImage_index = 15;
var PopUpMenu2_SeparatorColor_index = 16;
var PopUpMenu2_BorderColor_index = 17;
var PopUpMenu2_BackgroundColor_index = 18;
var PopUpMenu2_HideTime_index = 19;
var PopUpMenu2_ToolTip_index = 20;
var PopUpMenu2_FrameTarget_index = 21;

var PopUpMenu2_DelayTime = 300;
var PopUpMenu2_HideTime = PopUpMenu2_default_config[PopUpMenu2_HideTime_index][1];
var PopUpMenuHelpLink =PopUpMenu2_default_config[PopUpMenu2_Help_index][1];
    
var PopUpMenu2_linkArray_sum;
var PopUpMenu2_theobj;
var PopUpMenu2_thetext;
var PopUpMenu2_winHeight;
var PopUpMenu2_winWidth;
var PopUpMenu2_tableColor;
var PopUpMenu2_timerID;
var PopUpMenu2_first_time=false;
var PopUpMenu2_closeHTML;
var PopUpMenu2_ShowTitle=true;
var PopUpMenu2_scrollbaroff = 0;
var PopUpMenu2_center_offset=false;
var PopUpMenu2_boxposLR;
var PopUpMenu2_boxposTB;
var PopUpMenu2_This_Frame_Target

var PopUpMenu2_ToolTipNum = 1; 
var PopUpMenu2_ToolTipText = "Nety";
var PopUpMenu2_ToolTipOnly = "no";
var window_pageX;
var window_pageY;

// Browser Check 
var PopUpMenu2_opera=PopUpMenu2_opera_6=PopUpMenu2_opera_7up=false;
var PopUpMenu2_menu_possible=false;
PopUpMenu2_ns4=(document.layers)?true:false
PopUpMenu2_mac45=(navigator.appVersion.indexOf("MSIE 4.5")!=-1)?true:false
PopUpMenu2_safari=(navigator.userAgent.indexOf("Safari")!=-1)?true:false
PopUpMenu2_ns6up=(navigator.userAgent.indexOf("Gecko")!=-1)?true:false
PopUpMenu2_ns6x=(navigator.userAgent.indexOf("Netscape6")!=-1)?true:false

if(PopUpMenu2_ns6up||PopUpMenu2_ns4)mac=false;
PopUpMenu2_icab=(navigator.userAgent.indexOf("iCab")!=-1)?true:false
PopUpMenu2_ie55=((navigator.appVersion.indexOf("MSIE 8.")!=-1||navigator.appVersion.indexOf("MSIE 7.")!=-1||navigator.appVersion.indexOf("MSIE 6.")!=-1||navigator.appVersion.indexOf("MSIE 5.5")!=-1))?true:false;
PopUpMenu2_ie5mac=((navigator.appVersion.indexOf("MSIE 5")!=-1&&navigator.appVersion.indexOf("Mac")!=-1))?true:false;

// Check if browser Opera and version (Menu not possible in 6.x)
if (navigator.userAgent.indexOf("Opera")!=-1) {
PopUpMenu2_opera_6=(navigator.userAgent.indexOf("6.")!=-1)?true:false
PopUpMenu2_opera_7up=(navigator.userAgent.indexOf("10.")!=-1)||(navigator.userAgent.indexOf("7.")!=-1)||(navigator.userAgent.indexOf("8.")!=-1)||(navigator.userAgent.indexOf("9.")!=-1)?true:false
PopUpMenu2_opera=true;
}

// Check if browser Icab or Opera 6.x for Mac (Menu not possible)
if ((PopUpMenu2_ie5mac&&PopUpMenu2_icab) || (PopUpMenu2_ie5mac&&PopUpMenu2_opera_6)) {PopUpMenu2_ie5mac=false;}

PopUpMenu2_iens6 = PopUpMenu2_ns6up || PopUpMenu2_ie55 || PopUpMenu2_opera_7up;

if ( PopUpMenu2_iens6 ) PopUpMenu2_menu_possible=true;

if (PopUpMenu2_ns4) document.captureEvents(Event.MOUSEMOVE)

PopUpMenu.addEvent(window, 'load', function() {
    document.onmousemove=getMouseXY
});

function getMouseXY(e)
{
    e = e || window.event;
    window_pageX = e.pageX || e.clientX;
    window_pageY = e.pageY || e.clientY;
    if ((PopUpMenu2_ie55 || PopUpMenu2_ie5mac ) && (!PopUpMenu2_opera_7up))
    {   
    var ScrOffY = 0;
    var ScrOffX = 0;
    
    if (document.documentElement)
        {
            ScrOffY = document.documentElement.scrollTop;
            ScrOffX = document.documentElement.scrollLeft;
        }
        window_pageX += document.body.scrollLeft + ScrOffX;
        window_pageY += document.body.scrollTop + ScrOffY;
    }
    return true
}

function PopUpMenu2_SetToolTip(PopUpMenu2_links_ids_line,PopUpMenu2_ToolTipId) {
    PopUpMenu2_ToolTipOnly = "yes"
    if  (arguments.length != 1) {
//  alert (parseInt(PopUpMenu2_ToolTipId));
    if ( parseInt(PopUpMenu2_ToolTipId) > 0 ) { PopUpMenu2_ToolTipNum  = PopUpMenu2_ToolTipId ; } else { PopUpMenu2_ToolTipText=PopUpMenu2_ToolTipId; }
    }
    PopUpMenu2_Set(PopUpMenu2_links_ids_line)
}

function BuildLinks(PopUpMenu2_links)
{

    PopUpMenu2_linkArraytmp = new Array;
    PopUpMenu2_linkArraytmp = PopUpMenu2_links;
    PopUpMenu2_linkArray_sum =" ";
    PopUpMenu2_TotalLinksCount=0;
    var PopUpMenu2_linkArrayMaxlength = 0;
    var PopUpMenu2_TotalSeparatorsCount=0;
    var PopUpMenu2_linkArray_starts = 0;
    PopUpMenu2_local_config_tmp = new Array;
    PopUpMenu2_default_config_tmp = new Array;
    for (var temp_i = 0; temp_i < PopUpMenu2_default_config.length; temp_i++) { 
    PopUpMenu2_default_config_tmp[temp_i]=PopUpMenu2_default_config[temp_i].join(',').split(',')
    }
    PopUpMenu2_center_offset=false;
    // Default Close Icon HTML 
    PopUpMenu2_closeHTML="<img src='http://www.ncbi.nlm.nih.gov/coreweb/template1/pix/pixel.gif' width='12' height='11' border='0'>";
    // Default False Hide Title
    PopUpMenu2_ShowTitle=true;
    PopUpMenuHelpLink = "none";

    if (PopUpMenu2_linkArraytmp[0][0] == "UseLocalConfig" && PopUpMenu2_linkArraytmp[0][1] != "") {
    // set local congif from PopUpMenu2_Set_local_Config array
        PopUpMenu2_local_config_tmp = eval('PopUpMenu2_LocalConfig_'+PopUpMenu2_linkArraytmp[0][1]);
    
    if (PopUpMenu2_local_config_tmp[0][0] != "UseThisLocalConfig" || PopUpMenu2_local_config_tmp[0][1] != "no") {
        for (var temp_i = 0; temp_i < PopUpMenu2_local_config_tmp.length; temp_i++) {   
            for (var temp_i2 = 0; temp_i2 < PopUpMenu2_default_config_tmp.length; temp_i2++) {  
                if (PopUpMenu2_default_config_tmp[temp_i2][0] == PopUpMenu2_local_config_tmp[temp_i][0])
                {
                PopUpMenu2_default_config_tmp[temp_i2][1] = PopUpMenu2_local_config_tmp[temp_i][1];
                }
            }
        }
     } 
        // PopUpMenu2_default_config_tmp is config array for current menu 
        PopUpMenu2_linkArray_starts = 1;

  }
    // Set Hide Time from Local config 
    PopUpMenu2_HideTime = PopUpMenu2_default_config_tmp[PopUpMenu2_HideTime_index][1];

        for (var temp_i2 = 0; temp_i2 < PopUpMenu2_default_config_tmp.length; temp_i2++) {  
                
                PopUpMenu2_default_config_tmpKey = PopUpMenu2_default_config_tmp[temp_i2][0]
                PopUpMenu2_default_config_tmpValue = PopUpMenu2_default_config_tmp[temp_i2][1];
                
                if (PopUpMenu2_default_config_tmpKey=="ShowCloseIcon" && PopUpMenu2_default_config_tmpValue=="yes") {
                PopUpMenu2_closeHTML="<a href='#' CLASS='popmenu' onClick='javascript:PopUpMenu2_Stop(true); return false;'><img src='http://www.ncbi.nlm.nih.gov/coreweb/images/popupmenu/close.gif' width='12' height='11' alt='Close' border='0'></a>";
            } else if (PopUpMenu2_default_config_tmpKey=="TitleText" && PopUpMenu2_default_config_tmpValue !="") {
                PopUpMenu2_title4layer = PopUpMenu2_default_config_tmpValue;
                } else if (PopUpMenu2_default_config_tmpKey=="ShowTitle" && PopUpMenu2_default_config_tmpValue=="no") {
                PopUpMenu2_ShowTitle=false;
            } else if (PopUpMenu2_default_config_tmpKey=="AlignCenter"  && PopUpMenu2_default_config_tmpValue=="yes") {
                PopUpMenu2_center_offset=true;
            } else if (PopUpMenu2_default_config_tmpKey=="Help" && PopUpMenu2_default_config_tmpValue !="none") {
                PopUpMenuHelpLink = PopUpMenu2_default_config_tmpValue;
                }   
            }

    if (PopUpMenu2_default_config_tmp[PopUpMenu2_ToolTip_index][1] != "no" || PopUpMenu2_ToolTipOnly != "no") {

    var ToolTip = "";
    if (PopUpMenu2_default_config_tmp[PopUpMenu2_ToolTip_index][1] != "no") {
    var tippars = parseInt(PopUpMenu2_default_config_tmp[PopUpMenu2_ToolTip_index][1]);
    if ( typeof(tippars) == "number") { ToolTip = PopUpMenu2_linkArraytmp[1][0]; } else { ToolTip = PopUpMenu2_linkArraytmp[tippars][0]; }
    }
    if (PopUpMenu2_ToolTipOnly =="yes") {  
        if (PopUpMenu2_ToolTipText !="Nety") { ToolTip = PopUpMenu2_ToolTipText; } else if ( PopUpMenu2_ToolTipNum > 0 && PopUpMenu2_ToolTipNum <= PopUpMenu2_linkArraytmp.length) { ToolTip = PopUpMenu2_linkArraytmp[PopUpMenu2_ToolTipNum][0]; }
        PopUpMenu2_ToolTipOnly = "no";
    }
PopUpMenu2_linkArray_sum ='<tr><td width="1"><img src="http://www.ncbi.nlm.nih.gov/coreweb/template1/pix/pixel.gif" width="1" height="5" border="0"></td><td align="left" width="100%" nowrap><font CLASS="popmenu" style="color:'+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemColor_index][1]+'; font-family:'+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemFont_index][1]+'; font-size: '+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemSize_index][1]+';">'+ToolTip+'</font></td><td width="1"><img src="http://www.ncbi.nlm.nih.gov/coreweb/template1/pix/pixel.gif" width="1" height="5" border="0"></td></tr>';
return;
}
 if (PopUpMenu2_default_config_tmp[PopUpMenu2_FreeText_index][1] == "no" ) {

if (PopUpMenu2_default_config_tmp[PopUpMenu2_FrameTarget_index][1] == "") { PopUpMenu2_This_Frame_Target = ""; } else { PopUpMenu2_This_Frame_Target = 'target="'+PopUpMenu2_default_config_tmp[PopUpMenu2_FrameTarget_index][1]+'"'; }

for (var i = PopUpMenu2_linkArray_starts; i < PopUpMenu2_linkArraytmp.length; i++) {

        PopUpMenu2_linkArrayConstr  = PopUpMenu2_OnMouseOut_link = PopUpMenu2_OnMouseOver_link =  "";
        PopUpMenu2_linkArrayKey = PopUpMenu2_linkArraytmp[i][0];
        PopUpMenu2_linkArrayKey_length = PopUpMenu2_linkArraytmp[i][0].length;
        PopUpMenu2_linkArrayValue = PopUpMenu2_linkArraytmp[i][1];
        PopUpMenu2_linkArrayMOver = PopUpMenu2_linkArraytmp[i][2];
        PopUpMenu2_linkArrayMOut = PopUpMenu2_linkArraytmp[i][3];

        
        if (PopUpMenu2_linkArraytmp[i]) {
            if (PopUpMenu2_linkArrayMOver) {    
                PopUpMenu2_OnMouseOver_link='onMouseOver="javascript:'+PopUpMenu2_linkArrayMOver+' "';              
            }
            if (PopUpMenu2_linkArrayMOut) { 
                PopUpMenu2_OnMouseOut_link='onMouseOut="javascript:'+PopUpMenu2_linkArrayMOut+' "';
            }
            if (PopUpMenu2_linkArrayMaxlength < PopUpMenu2_linkArrayKey_length) {  
                PopUpMenu2_linkArrayMaxlength = PopUpMenu2_linkArrayKey_length;
            }
            
            if (PopUpMenu2_linkArrayKey=="Help") { 
                PopUpMenuHelpLink = PopUpMenu2_linkArrayValue;
            } else {

            if (PopUpMenu2_linkArrayValue!="-" && PopUpMenu2_linkArrayValue!="none") { 
                    if (PopUpMenu2_linkArrayValue.indexOf("aname#") != -1) {
                        PopUpMenu2_linkArrayConstr = '<a href="'+PopUpMenu2_linkArrayValue.substring(5,PopUpMenu2_linkArrayValue.length)+'"  CLASS="popmenu"  onClick="javascript:PopUpMenu2_Stop(true);"  '+PopUpMenu2_OnMouseOver_link+' '+PopUpMenu2_OnMouseOut_link+' style="color:'+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemColor_index][1]+'; font-family:'+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemFont_index][1]+'; font-size: '+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemSize_index][1]+';">'+PopUpMenu2_linkArrayKey+'</a>';
                    } else {
                    if( PopUpMenu2_linkArrayValue.substring(0, 11) != 'javascript:' ) {
                        if (PopUpMenu2_linkArrayValue.indexOf("window.") != -1 ) { PopUpMenu2_linkArrayValue="javascript:"+PopUpMenu2_linkArrayValue; }
                    }
                    PopUpMenu2_linkArrayConstr = '<a href="'+PopUpMenu2_linkArrayValue+'"  CLASS="popmenu" style="color:'+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemColor_index][1]+'; font-family:'+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemFont_index][1]+'; font-size: '+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemSize_index][1]+';" '+PopUpMenu2_OnMouseOver_link+' '+PopUpMenu2_OnMouseOut_link+' '+PopUpMenu2_This_Frame_Target+'>'+PopUpMenu2_linkArrayKey+'</a>';
                    }
                    PopUpMenu2_linkArray_sum+="<tr onMouseOver='PopUpMenu2_Table_Cell_MouseOver(this,1,"+PopUpMenu2_name_entrez_top_table+")' onMouseOut='PopUpMenu2_Table_Cell_MouseOver(this,0,"+PopUpMenu2_name_entrez_top_table+");' valign='middle'><td valign='middle' align='right' width='10' ><img src='"+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemBulletImage_index][1]+"' width='10' height='15' border='0' align='middle'></td><td nowrap align=left width='100%'><font size=2 face='Verdana, arial, geneva, helvetica' >"+PopUpMenu2_linkArrayConstr+"</font></td><td width='1'><img src='http://www.ncbi.nlm.nih.gov/coreweb/template1/pix/pixel.gif' width='1' height='5' border='0'></td></tr>";
                    PopUpMenu2_TotalLinksCount++;
                    } else { 
// separator cell   

                if (PopUpMenu2_linkArray_sum!==" ")  {
                    PopUpMenu2_linkArray_sum+='<tr><td colspan="3"><table width="100%" border="0" cellspacing="0" cellpadding="0" align="left"><tr><td background="http://www.ncbi.nlm.nih.gov/coreweb/images/popupmenu/separator.gif"><img src="http://www.ncbi.nlm.nih.gov/coreweb/template1/pix/pixel.gif" width="1" height="2" border="0"></td></tr></table></td></tr>';
                    PopUpMenu2_TotalSeparatorsCount++;
                    }
// name of new groups after separator
                if (PopUpMenu2_linkArrayKey!=="-") {
                    PopUpMenu2_linkArray_sum+="<tr><td colspan='3' nowrap align=center><font size=2 face='Verdana, arial, geneva, helvetica' style='color:"+PopUpMenu2_default_config_tmp[PopUpMenu2_SeparatorColor_index][1]+"; font-family:"+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemFont_index][1]+"; font-size: "+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemSize_index][1]+";'><img src='http://www.ncbi.nlm.nih.gov/coreweb/template1/pix/pixel.gif' width='10' height='1' border='0'><b>"+PopUpMenu2_linkArrayKey+"</b></font><img src='http://www.ncbi.nlm.nih.gov/coreweb/template1/pix/pixel.gif' width='10' height='1' border='0'></td></tr>";
                    PopUpMenu2_TotalLinksCount++;
            }
          }
        }
      }
    }
    if (!PopUpMenu2_ShowTitle && PopUpMenuHelpLink != "none") {
                   PopUpMenu2_linkArray_sum+="<tr onMouseOver='PopUpMenu2_Table_Cell_MouseOver(this,1,"+PopUpMenu2_name_entrez_top_table+")' onMouseOut='PopUpMenu2_Table_Cell_MouseOver(this,0,"+PopUpMenu2_name_entrez_top_table+");' valign='middle'><td valign='middle' align='right'width='10' ><img src='"+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemBulletImage_index][1]+"' width='10' height='15' border='0' align='middle'></td><td nowrap align=left width='100%'><font size=2 face='Verdana, arial, geneva, helvetica' ><a href='javascript:PopUpMenu2_showpopuphelp();' CLASS='popmenu' style='color:"+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemColor_index][1]+"; font-family:"+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemFont_index][1]+"; font-size: "+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemSize_index][1]+";' >Help</a></font></td><td width='1'><img src='http://www.ncbi.nlm.nih.gov/coreweb/template1/pix/pixel.gif' width='1' height='5' border='0'></td></tr>";
                   PopUpMenu2_TotalLinksCount++;
    }
  } else {
 // Free Text 
PopUpMenu2_linkArray_sum ='<tr><td width="1"><img src="http://www.ncbi.nlm.nih.gov/coreweb/template1/pix/pixel.gif" width="1" height="5" border="0"></td><td align="left" width="100%" nowrap><font CLASS="popmenu" style="color:'+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemColor_index][1]+'; font-family:'+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemFont_index][1]+'; font-size: '+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemSize_index][1]+';">'+PopUpMenu2_default_config_tmp[PopUpMenu2_FreeText_index][1]+'</font></td><td width="1"><img src="http://www.ncbi.nlm.nih.gov/coreweb/template1/pix/pixel.gif" width="1" height="5" border="0"></td></tr>';
  }
    // menu Height and Width calc.

    PopUpMenu2_inheight = 18 + 17 * PopUpMenu2_TotalLinksCount;
    if (PopUpMenu2_TotalSeparatorsCount > 0) PopUpMenu2_inheight += 4 * PopUpMenu2_TotalSeparatorsCount;
    if (PopUpMenu2_linkArrayMaxlength < 15 ) {
        PopUpMenu2_inwidth = 120; 
    } else {
        PopUpMenu2_inwidth = 120 + (PopUpMenu2_linkArrayMaxlength - 14) * 7;
    }
}


function buildText() {

// !!! SINGLE QUOTES INSIDE DOUBLE QUOTES. 
PopUpMenu2_text="";
if (PopUpMenu2_ShowTitle) {
    PopUpMenu2_text="<table width='100%' border='0' cellspacing='0' cellpadding='1' background='"+PopUpMenu2_default_config_tmp[PopUpMenu2_TitleBackgroundImage_index][1]+"'>";
    PopUpMenu2_text+="<tr><td nowrap bgcolor='"+PopUpMenu2_default_config_tmp[PopUpMenu2_BorderColor_index][1]+"' background='"+PopUpMenu2_default_config_tmp[PopUpMenu2_TitleBackgroundImage_index][1]+"'>";
if (PopUpMenuHelpLink != "none") {
PopUpMenu2_text+="<a href='javascript:PopUpMenu2_showpopuphelp();' CLASS='popmenu'><img src='http://www.ncbi.nlm.nih.gov/coreweb/images/popupmenu/help.gif' width='12' height='11' alt='Help' border='0'></a>";
} else {
PopUpMenu2_text+="<img src='http://www.ncbi.nlm.nih.gov/coreweb/template1/pix/pixel.gif' width='12' height='11' alt='Help' border='0'></a>";
}
PopUpMenu2_text+="</td><td nowrap bgcolor='"+PopUpMenu2_default_config_tmp[PopUpMenu2_BorderColor_index][1]+"' background='"+PopUpMenu2_default_config_tmp[PopUpMenu2_TitleBackgroundImage_index][1]+"'><center><font  class='menutitle' style='color:"+PopUpMenu2_default_config_tmp[PopUpMenu2_TitleColor_index][1]+"; font-family:"+PopUpMenu2_default_config_tmp[PopUpMenu2_ItemFont_index][1]+"; font-size: "+PopUpMenu2_default_config_tmp[PopUpMenu2_TitleSize_index][1]+";'>&nbsp;<b>"+PopUpMenu2_title4layer+"</b>&nbsp;</font></center></td><td nowrap bgcolor='"+PopUpMenu2_default_config_tmp[PopUpMenu2_BorderColor_index][1]+"' align='right' background='"+PopUpMenu2_default_config_tmp[PopUpMenu2_TitleBackgroundImage_index][1]+"'>"+PopUpMenu2_closeHTML+"";
    PopUpMenu2_text+="</td></tr></table>";
}
    PopUpMenu2_text+="<table width='100%' border='0' cellspacing='0' cellpadding='1' bgcolor='"+PopUpMenu2_default_config_tmp[PopUpMenu2_BorderColor_index][1]+"'>";
    PopUpMenu2_text+="<tr><td>";
    PopUpMenu2_text+="<table border='0' cellspacing='0' cellpadding='0' width='100%'>";
    PopUpMenu2_text+="<tr><td bgcolor='"+PopUpMenu2_default_config_tmp[PopUpMenu2_BorderColor_index][1]+"' align='center' valign='top'>";
    PopUpMenu2_text+="<table width='100%' border='0' cellspacing='0' cellpadding='1' bgcolor='"+PopUpMenu2_default_config_tmp[PopUpMenu2_BackgroundColor_index][1]+"'>";
    PopUpMenu2_text+=PopUpMenu2_linkArray_sum;
    PopUpMenu2_text+="</table></td></tr></table></td></tr></table>";
//  document.write(PopUpMenu2_text);
    return PopUpMenu2_text;
    
}


function PopUpMenu2_doNOThideFunc() {
    PopUpMenu2_doNOThide = true;
}


function PopUpMenu2_Hide_Layer() {
    PopUpMenu2_Hide_It(PopUpMenu2_HideTime);
}


function PopUpMenu2_Hide(ms) {
    if (PopUpMenu2_timerID) {window.clearTimeout(PopUpMenu2_timerID); }
    if (!ms) { ms=PopUpMenu2_HideTime; }
    PopUpMenu2_Hide_It(ms);
}


function PopUpMenu2_Hide_It(ms) {
    PopUpMenu2_milliseconds=parseInt(ms);
    if (PopUpMenu2_milliseconds > 0) {
        PopUpMenu2_milliseconds -= PopUpMenu2_DelayTime;
        PopUpMenu2_timerID=window.setTimeout('PopUpMenu2_Hide_It(PopUpMenu2_milliseconds)',PopUpMenu2_DelayTime);
    } else {
        PopUpMenu2_Stop(false);
    }
}


function PopUpMenu2_showpopuphelp() {
    if (PopUpMenuHelpLink.indexOf("window.open(") != -1) {
    eval (PopUpMenuHelpLink);
    } else {
    eval ('window.top.location="'+PopUpMenuHelpLink + '"');
    PopUpMenu2_Stop(true);
    }

}


function PopUp2WindowOpen(url,name,attributes) {
    var PopUpWindowHandle;
    PopUpWindowHandle = window.open(url,name,attributes);
}


function PopUpMenu2_ClearTime(){
    window.clearTimeout(PopUpMenu2_timerID);
}

String.prototype.Conf2Boolean = 
function() {
    return ~"1|yes|da|si|true|on".indexOf(this.toString().toLowerCase());
}

function PopUpMenu2_Set_GlobalConfig(){
    
    if (PopUpMenu2_GlobalConfig[0][0] == "UseThisGlobalConfig" && PopUpMenu2_GlobalConfig[0][1] == "yes") {
    // set global congif from PopUpMenu2_Set_GlobalConfig array
        for (var temp_i = 1; temp_i < PopUpMenu2_GlobalConfig.length; temp_i++) {   
            for (var temp_i2 = 0; temp_i2 < PopUpMenu2_default_config.length; temp_i2++) {  
                if (PopUpMenu2_default_config[temp_i2][0] == PopUpMenu2_GlobalConfig[temp_i][0])
                {
                PopUpMenu2_default_config[temp_i2][1] = PopUpMenu2_GlobalConfig[temp_i][1];
                }
            }
        }
    } else {

    // use default global congif
    return;
    }
}

/* This code places div and iframe in page after page load. */
if (PopUpMenu2_menu_possible) {
   PopUpMenu2_Set_GlobalConfig();
     
   if (PopUpMenu2_iens6) {
       PopUpMenu.addEvent(window, 'load', function() {
          var body = document.getElementsByTagName('body')[0];
          var div = document.createElement('div');
          div.innerHTML = "<div id='PopUpMenu2viewer' "+
             "style='background-color:transparent;width:0;height:0;margin-left:0;visibility:hidden;" +
             "position:absolute;z-index:1;overflow:hidden' onmouseover='PopUpMenu2_ClearTime();' "+
             "onmouseout='PopUpMenu2_Hide_Layer()'></div>";
          body.insertBefore(div.firstChild, body.firstChild);
    });
     
     if (PopUpMenu2_ie55 && !PopUpMenu2_opera) { 
             PopUpMenu.addEvent(window, 'load', function() {
                var body = document.getElementsByTagName('body')[0];
                var div = document.createElement('div');
                div.innerHTML = "<iframe id='PoupMenuIEFrame' src='javascript:false;' "+
                "scrolling='no' frameborder='0' style='position:absolute; top:0px; left:0px;"+
                "display:none;'></iframe>";
                body.insertBefore(div.firstChild, body.firstChild);
             });
      }
    }
}

// version 2.7.3 (01/04/2007) IE NS 6.X Browsers 
// XHTML PoUpMenu position Fix
// New Frame_Target param added
// Fight with Safari by Full Merge

function PopUpMenu2_Set(PopUpMenu2_links_ids_line) {

    if (!PopUpMenu2_menu_possible) {  return; }

    PopUpMenu2_first_time=true;
    BuildLinks(PopUpMenu2_links_ids_line);

PopUpMenu2_boxposLR=PopUpMenu2_default_config_tmp[PopUpMenu2_AlignLR_index][1]; 
PopUpMenu2_boxposTB=PopUpMenu2_default_config_tmp[PopUpMenu2_AlignTB_index][1];

    window.clearTimeout(PopUpMenu2_timerID);
    PopUpMenu2_thetext=buildText();

    if (document.getElementById) {
    PopUpMenu2_theobj=document.getElementById('PopUpMenu2viewer');
    } else { return; }
    
//  if (PopUpMenu2_iens6&&document.all) {
//            PopUpMenu2_theobj.innerHTML = "";
//            PopUpMenu2_theobj.insertAdjacentHTML("BeforeEnd","<table cellspacing=0 id='Menu2_main_table' height="+PopUpMenu2_winHeight+" border=0><tr><td width='100%' valign=top><font  style='font-weight:normal'>"+PopUpMenu2_thetext+"</font></td></tr></table>");
//      }
        if (PopUpMenu2_iens6) {
            PopUpMenu2_theobj.innerHTML = '';
            PopUpMenu2_theobj.innerHTML = "<table id='Menu2_main_table' cellspacing=0 height="+PopUpMenu2_winHeight+" border=0><tr><td width='100%' valign=top><font style='font-weight:normal'>"+PopUpMenu2_thetext+"</font></td></tr></table>";
        }

    var PopUpMenu2_main_table=document.getElementById("Menu2_main_table")
    PopUpMenu2_inwidth = PopUpMenu2_main_table.offsetWidth
    PopUpMenu2_inheight = PopUpMenu2_main_table.offsetHeight

    PopUpMenu2_browserHeight = getViewportHeight();
    PopUpMenu2_browserWidth = getViewportWidth();

    var PopUpMenu2_ScrOffY = 0;
    var PopUpMenu2_ScrOffX = 0;
        
        // Try to get the scrolls offsets
        
  if( typeof( window.pageYOffset ) == 'number' ) {
    //Netscape compliant
    PopUpMenu2_ScrOffY = window.pageYOffset;
    PopUpMenu2_ScrOffX = window.pageXOffset;
  } else if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) {
    //DOM compliant
    PopUpMenu2_ScrOffY = document.body.scrollTop;
    PopUpMenu2_ScrOffX = document.body.scrollLeft;
  } else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) {
    //IE6 standards compliant mode
    PopUpMenu2_ScrOffY = document.documentElement.scrollTop;
    PopUpMenu2_ScrOffX = document.documentElement.scrollLeft;
  }
  
    PopUpMenu2_scrollbaroff=1;

    if (PopUpMenu2_ns6x) {
        PopUpMenu2_browserWidth=window.innerWidth;
        PopUpMenu2_browserHeight=window.innerHeight;
        PopUpMenu2_ScrOffX=window.pageXOffset;
        PopUpMenu2_ScrOffY=window.pageYOffset;
        PopUpMenu2_scrollbaroff=16;
    }


    PopUpMenu2_boxPrePositionLR=PopUpMenu2_boxposLR;
    PopUpMenu2_boxPrePositionTB=PopUpMenu2_boxposTB;
        
    if (!PopUpMenu2_center_offset) {
    
    if ( PopUpMenu2_browserHeight+PopUpMenu2_ScrOffY < window_pageY+PopUpMenu2_inheight ) 
    { 
        PopUpMenu2_boxPrePositionTB="top" 
    } 

    if ( PopUpMenu2_browserWidth+PopUpMenu2_ScrOffX < window_pageX+PopUpMenu2_inwidth ) { PopUpMenu2_boxPrePositionLR="left" } else if ( window_pageX-PopUpMenu2_inwidth < 0 ) { PopUpMenu2_boxPrePositionLR = "right" } 

    PopUpMenu2_boxPosition=PopUpMenu2_boxPrePositionTB+PopUpMenu2_boxPrePositionLR;
 
    if (PopUpMenu2_boxPosition == "bottomright") { 
        window_pageX += PopUpMenu2_offsetx;
        window_pageY += PopUpMenu2_offsety;
    } else if (PopUpMenu2_boxPosition == "bottomleft") { 
        window_pageX -= (PopUpMenu2_offsetx+2)+PopUpMenu2_inwidth;
        window_pageY -= PopUpMenu2_offsety;
    } else if (PopUpMenu2_boxPosition == "topright") {
        window_pageX += PopUpMenu2_offsetx;
        window_pageY += PopUpMenu2_offsety-PopUpMenu2_inheight;
    } else if (PopUpMenu2_boxPosition == "topleft") { 
        window_pageX -= (PopUpMenu2_offsetx+2)+PopUpMenu2_inwidth;
        window_pageY += PopUpMenu2_offsety-PopUpMenu2_inheight;
     }

    } else {
    
   if ( PopUpMenu2_browserWidth+PopUpMenu2_ScrOffX-PopUpMenu2_scrollbaroff < window_pageX+(PopUpMenu2_inwidth/2) ) {
    window_pageX = PopUpMenu2_browserWidth+PopUpMenu2_ScrOffX-PopUpMenu2_inwidth-PopUpMenu2_scrollbaroff;
        } else if ( window_pageX-PopUpMenu2_ScrOffX-PopUpMenu2_inwidth/2 < 0 ) { window_pageX = PopUpMenu2_ScrOffX + PopUpMenu2_scrollbaroff; 
        } else { window_pageX -= Math.round(PopUpMenu2_inwidth/2); }

    if ( PopUpMenu2_browserHeight+PopUpMenu2_ScrOffY-PopUpMenu2_scrollbaroff < window_pageY+PopUpMenu2_inheight/2 ) { 
        window_pageY = PopUpMenu2_browserHeight+PopUpMenu2_ScrOffY-PopUpMenu2_inheight-PopUpMenu2_scrollbaroff;
        } else if ( window_pageY-PopUpMenu2_ScrOffY-PopUpMenu2_inheight/2 < 0 ) { window_pageY = PopUpMenu2_ScrOffY + PopUpMenu2_scrollbaroff; 
        } else { window_pageY -= Math.round(PopUpMenu2_inheight/2); }
    }


    PopUpMenu2_viewIt();
}

function PopUpMenu2_viewIt() {

    PopUpMenu2_theobj.style.left=window_pageX +"px";
    PopUpMenu2_theobj.style.top=window_pageY + "px";
    PopUpMenu2_theobj.style.width=PopUpMenu2_inwidth  +"px";;
    PopUpMenu2_theobj.style.height=PopUpMenu2_inheight  +"px";;
//  PopUpMenu2_theobj.display="block";
    PopUpMenu2_theobj.style.visibility="visible";
    if (PopUpMenu2_ie55) {
    IfrRef = document.getElementById('PoupMenuIEFrame');
    if (!IfrRef) return;
    IfrRef.style.width = PopUpMenu2_inwidth;
    IfrRef.style.height = PopUpMenu2_inheight;
    IfrRef.style.top = window_pageY;
    IfrRef.style.left = window_pageX;
    IfrRef.style.zIndex = PopUpMenu2_theobj.style.zIndex - 1;
    IfrRef.style.display = "block";
    }
}

function PopUpMenu2_Stop(PopUpMenu2_Stop_now) {
    
    if (!PopUpMenu2_Stop_now) {
        if (!PopUpMenu2_first_time) {
           return;
        }
    } else { 
        PopUpMenu2_doNOThide=false;
    }
    
    if (PopUpMenu2_iens6 && !PopUpMenu2_doNOThide) {
        
        if (PopUpMenu2_ie55 && IfrRef) {
        IfrRef.style.display = "none";
        }
        PopUpMenu2_theobj.style.visibility="hidden";
//      PopUpMenu2_theobj.display="none";
        PopUpMenu2_theobj.innerHTML = "";
        
    if (!PopUpMenu2_safari) {
        PopUpMenu2_theobj.style.width=1  +"px";
        PopUpMenu2_theobj.style.height=1  +"px";
        }

    }
    PopUpMenu2_doNOThide=false;
    PopUpMenu2_Stop_now=false;
}


 function PopUpMenu2_Table_Cell_MouseOver( tableCellRef, hoverFlag, navStyle , event_color ) 
{
    var Table_Cell_MouseOver_color_tmp = "";
    var Table_Cell_MouseOut_color_tmp = "";

    if ( hoverFlag ) 
    {
    if (event_color) 
        { 
        Table_Cell_MouseOver_color_tmp = event_color; 
        } else { 
        try {
                if ( eval('Table_Cell_MouseOver_color.'+navStyle) != undefined ) {
                    Table_Cell_MouseOver_color_tmp = eval('Table_Cell_MouseOver_color.'+navStyle);
                } else {
                Table_Cell_MouseOver_color_tmp = Table_Cell_MouseOver_color.def;
                }
            } catch(exception) {
          }
        }
        if (Table_Cell_MouseOver_color_tmp) 
                { 
                Table_Cell_MouseOut_color_self_style = tableCellRef.style.backgroundColor;
                tableCellRef.style.backgroundColor = Table_Cell_MouseOver_color_tmp; 
                }
//          if ( document.getElementsByTagName ) {
//                  tableCellRef.getElementsByTagName( 'a' )[0].style.color = '#F0F8FF';
//              }
    } else {
    if (event_color) 
        { 
        Table_Cell_MouseOut_color_tmp = event_color; 
        } else { 
    if (Table_Cell_MouseOut_color_self_style) { 
            tableCellRef.style.backgroundColor = Table_Cell_MouseOut_color_self_style; }
        try {
                if ( eval('Table_Cell_MouseOut_color.'+navStyle) != "undefined" ) {
                    Table_Cell_MouseOut_color_tmp = eval('Table_Cell_MouseOut_color.'+navStyle);
                } else {
                Table_Cell_MouseOut_color_tmp = Table_Cell_MouseOut_color.def;
                }
            } catch(exception) {
            }
        }
        if (Table_Cell_MouseOut_color_tmp) 
                { tableCellRef.style.backgroundColor = Table_Cell_MouseOut_color_tmp; }
        }
}

        /**
         * Returns the current width of the viewport.
         * @method getViewportWidth
         * @return {Int} The width of the viewable area of the page (excludes scrollbars).
         */

        getViewportWidth = function() {
            var width = self.innerWidth;  // Safari
            var mode = document.compatMode;

            if (mode) { // IE, Gecko, Opera
                width = (mode == 'CSS1Compat') ?
                        document.documentElement.clientWidth : // Standards
                        document.body.clientWidth; // Quirks
            }
            return width;
        }
        
        /**
         * Returns the current height of the viewport.
         * @method getViewportHeight
         * @return {Int} The height of the viewable area of the page (excludes scrollbars).
         */
        getViewportHeight = function() {
            var height = self.innerHeight; // Safari, Opera
            var mode = document.compatMode;

            if ( mode && !PopUpMenu2_opera ) { // IE, Gecko
                height = (mode == 'CSS1Compat') ?
                        document.documentElement.clientHeight : // Standards
                        document.body.clientHeight; // Quirks
            }

            return height;
        }

