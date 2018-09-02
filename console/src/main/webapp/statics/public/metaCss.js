/**
 * Created by gaojx on 2018/9/1.
 */
var strFullPath=window.document.location.href;
var strPath=window.document.location.pathname;
var pos=strFullPath.indexOf(strPath);
var prePath=strFullPath.substring(0,pos);
var postPath=strPath.substring(0,strPath.substr(1).indexOf('/')+1);
/*http://localhost:8087/umsConsole*/
var webPath = prePath+postPath;

document.write('<link href="'+ webPath +'/statics/images/favicon.ico" rel="icon" type="image/x-icon">');
document.write('<link href="'+ webPath +'/statics/images/favicon.ico" rel="shortcut icon" type="image/x-icon">');
document.write('<link href="'+ webPath +'/statics/images/favicon.ico" rel="bookmark" type="image/x-icon">');

document.write('<link href="'+ webPath +'/statics/css/bootstrap.min.css" rel="stylesheet" type="text/css">');
document.write('<link href="'+ webPath +'/statics/css/font-awesome.min.css" rel="stylesheet" type="text/css">');
document.write('<link href="'+ webPath +'/statics/plugins/AdminLTE/css/AdminLTE.min.css" rel="stylesheet" type="text/css">');
/*AdminLTE Skins. Choose a skin from the css/skins
     folder instead of downloading all of them to reduce the load.*/
document.write('<link href="'+ webPath +'/statics/css/all-skins.min.css" rel="stylesheet" type="text/css">');
document.write('<link href="'+ webPath +'/statics/css/main.css" rel="stylesheet" type="text/css">');
